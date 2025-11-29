package es.lasalle.videoconferencia.s03.models

// =====================================
// ⚡ UI EFFECTS - SEALED CLASSES PARA EFECTOS LATERALES
// =====================================

/**
 * 🎯 UI EFFECTS - Efectos laterales one-shot
 * 
 * 📖 QUÉ SON LOS UI EFFECTS:
 * Los UiEffects representan acciones que deben ejecutarse una sola vez
 * como respuesta a cambios de estado, pero que NO forman parte del estado UI.
 * 
 * 🧠 DIFERENCIA CLAVE: STATE vs EFFECTS:
 * 
 * 📊 UiState (Estado persistente):
 * - Persiste durante la vida del ViewModel
 * - Se re-emite en cada recomposición
 * - Ejemplo: "isLoading = true", "userList = [...]"
 * 
 * ⚡ UiEffect (Efecto one-shot):
 * - Se ejecuta una vez y se consume
 * - NO se re-emite en recomposiciones
 * - Ejemplo: "mostrar snackbar", "navegar a pantalla", "mostrar dialog"
 * 
 * 💡 PATRÓN DE USO:
 * ```kotlin
 * // En ViewModel
 * private val _uiEffects = Channel<UiEffect>()
 * val uiEffects = _uiEffects.receiveAsFlow()
 * 
 * // En UI
 * LaunchedEffect(Unit) {
 *     viewModel.uiEffects.collect { effect ->
 *         when (effect) {
 *             is UiEffect.ShowSnackbar -> snackbarHostState.showSnackbar(effect.message)
 *             is UiEffect.Navigate -> navController.navigate(effect.route)
 *         }
 *     }
 * }
 * ```
 * 
 * 🔄 FLUJO COMPLETO CON EFFECTS:
 * UiEvent → ViewModel → [Business Logic] → UiState + UiEffect → UI
 *                                             ↓
 *                                        ⚡ One-shot actions
 */

// =====================================
// 👤 PROFILE SCREEN - EFECTOS DE FORMULARIO
// =====================================

/**
 * 📝 ProfileUiEffect - Efectos laterales de la pantalla de perfil
 * 
 * 📖 EFECTOS DE FORMULARIO:
 * - ShowSnackbar: Mostrar mensaje temporal al usuario
 * - NavigateBack: Navegar a pantalla anterior después de guardar
 * - FocusField: Cambiar focus entre campos de formulario
 * 
 * 🧠 CONCEPTO CLAVE - ONE-SHOT ACTIONS:
 * Estos efectos se ejecutan una vez y no se almacenan en el estado.
 * Si el usuario rota la pantalla, estos efectos NO se vuelven a ejecutar.
 * 
 * 💡 EJEMPLO PRÁCTICO:
 * Cuando el usuario envía el formulario con éxito:
 * 1. ViewModel cambia UiState a Success
 * 2. ViewModel emite ShowSnackbar("Perfil guardado")
 * 3. ViewModel emite NavigateBack después de 2 segundos
 * 4. UI muestra snackbar Y navega back
 */
sealed class ProfileUiEffect {
    
    /**
     * 📢 ShowSnackbar - Mostrar mensaje temporal
     * Muestra un mensaje de feedback al usuario.
     * Se consume una vez y desaparece.
     * 
     * @param message Mensaje a mostrar al usuario
     * @param actionLabel Texto del botón de acción (opcional)
     * @param duration Duración del snackbar (por defecto corta)
     */
    data class ShowSnackbar(
        val message: String,
        val actionLabel: String? = null,
        val duration: androidx.compose.material3.SnackbarDuration = androidx.compose.material3.SnackbarDuration.Short
    ) : ProfileUiEffect()
    
    /**
     * 🔙 NavigateBack - Navegar a pantalla anterior
     * Se ejecuta después de guardar exitosamente el perfil.
     * Permite al usuario volver automáticamente a donde estaba.
     */
    object NavigateBack : ProfileUiEffect()
    
    /**
     * 🎯 FocusField - Cambiar focus a un campo específico
     * Útil para mejorar UX dirigiendo la atención del usuario.
     * Por ejemplo, focus automático en campo de email después de llenar nombre.
     * 
     * @param fieldName Nombre del campo a enfocar ("name" o "email")
     */
    data class FocusField(
        val fieldName: String
    ) : ProfileUiEffect()
    
    /**
     * 📱 HideKeyboard - Ocultar teclado virtual
     * Se ejecuta después de enviar formulario exitosamente.
     * Mejora la experiencia visual del usuario.
     */
    object HideKeyboard : ProfileUiEffect()
}

// =====================================
// ⚙️ SETTINGS SCREEN - EFECTOS DE CONFIGURACIÓN
// =====================================

/**
 * 🔧 SettingsUiEffect - Efectos laterales de la pantalla de configuración
 * 
 * 📖 EFECTOS DE CONFIGURACIÓN:
 * - ShowToast: Confirmación rápida de cambios
 * - RequestPermission: Solicitar permisos del sistema cuando sea necesario
 * - RestartApp: Para cambios que requieren reinicio (como tema)
 * 
 * 🧠 CONCEPTO CLAVE - EFFECTS vs DIALOGS:
 * Los effects son para acciones automáticas.
 * Para dialogs que requieren decisión del usuario, usa UiState.
 * 
 * 💡 EJEMPLO PRÁCTICO:
 * Usuario activa notificaciones:
 * 1. Si no hay permisos → emite RequestPermission
 * 2. Si hay permisos → emite ShowToast("Notificaciones activadas")
 */
sealed class SettingsUiEffect {
    
    /**
     * 🍞 ShowToast - Mostrar mensaje rápido
     * Confirmación rápida que no interrumpe el flujo del usuario.
     * Más ligero que Snackbar para confirmaciones simples.
     * 
     * @param message Mensaje de confirmación
     * @param isLong Si debe ser un toast largo o corto
     */
    data class ShowToast(
        val message: String,
        val isLong: Boolean = false
    ) : SettingsUiEffect()
    
    /**
     * 🔐 RequestPermission - Solicitar permiso del sistema
     * Se ejecuta cuando el usuario activa una función que requiere permisos.
     * Por ejemplo: notificaciones, cámara, ubicación.
     * 
     * @param permission Tipo de permiso a solicitar
     * @param rationale Explicación de por qué se necesita el permiso
     */
    data class RequestPermission(
        val permission: SystemPermission,
        val rationale: String
    ) : SettingsUiEffect()
    
    /**
     * 🔄 RestartApp - Reiniciar aplicación
     * Para cambios que requieren reinicio completo (como cambio de idioma).
     * En este ejemplo educativo, solo mostramos el concepto.
     * 
     * @param reason Razón del reinicio para mostrar al usuario
     */
    data class RestartApp(
        val reason: String
    ) : SettingsUiEffect()
    
    /**
     * ⚙️ OpenSystemSettings - Abrir configuración del sistema
     * Redirige al usuario a configuración de Android para cambios avanzados.
     * Útil cuando la app no puede cambiar algo directamente.
     * 
     * @param settingsType Tipo de configuración a abrir
     */
    data class OpenSystemSettings(
        val settingsType: SystemSettingsType
    ) : SettingsUiEffect()
}

// =====================================
// 📋 TASK DETAIL SCREEN - EFECTOS DE DETALLE DE TAREA
// =====================================

/**
 * 📄 TaskDetailUiEffect - Efectos laterales de la pantalla de detalle de tarea
 * 
 * 📖 EFECTOS DE DETALLE:
 * - NavigateBack: Volver después de operaciones
 * - ShowSnackbar: Feedback de operaciones
 * - ShareTask: Compartir tarea con otras apps
 * 
 * 🧠 CONCEPTO CLAVE - EFFECTS COMPLEJOS:
 * Algunos effects pueden tener múltiples parámetros
 * y lógica compleja asociada.
 */
sealed class TaskDetailUiEffect {
    
    /**
     * 🔙 NavigateBack - Volver a pantalla anterior
     * Se ejecuta cuando la tarea no existe o hay error irrecuperable.
     * Puede incluir un delay para que el usuario vea el mensaje de error.
     * 
     * @param withDelay Si debe esperar antes de navegar
     * @param delayMs Millisegundos a esperar
     */
    data class NavigateBack(
        val withDelay: Boolean = false,
        val delayMs: Long = 2000
    ) : TaskDetailUiEffect()
    
    /**
     * 📢 ShowSnackbar - Mostrar mensaje con acción opcional
     * Para feedback de operaciones como marcar completada, agregar tag, etc.
     * 
     * @param message Mensaje principal
     * @param actionLabel Texto del botón de acción (ej: "Deshacer")
     * @param onActionClick Acción a ejecutar si usuario hace click
     */
    data class ShowSnackbar(
        val message: String,
        val actionLabel: String? = null,
        val onActionClick: (() -> Unit)? = null
    ) : TaskDetailUiEffect()
    
    /**
     * 📤 ShareTask - Compartir tarea con otras apps
     * Abre el share sheet del sistema con información de la tarea.
     * 
     * @param taskTitle Título de la tarea a compartir
     * @param taskDescription Descripción de la tarea
     * @param shareText Texto final formateado para compartir
     */
    data class ShareTask(
        val taskTitle: String,
        val taskDescription: String,
        val shareText: String
    ) : TaskDetailUiEffect()
    
    /**
     * 📝 EditTask - Navegar a pantalla de edición
     * Abre una pantalla modal o nueva para editar la tarea.
     * 
     * @param taskId ID de la tarea a editar
     */
    data class EditTask(
        val taskId: String
    ) : TaskDetailUiEffect()
    
    /**
     * 🗑️ ConfirmDelete - Mostrar diálogo de confirmación
     * Aunque los dialogs podrían ser parte del UiState,
     * a veces es más simple tratarlos como effects one-shot.
     * 
     * @param taskTitle Título de la tarea a eliminar
     * @param onConfirm Acción si usuario confirma
     */
    data class ConfirmDelete(
        val taskTitle: String,
        val onConfirm: () -> Unit
    ) : TaskDetailUiEffect()
}

// =====================================
// 📝 ENUMS DE APOYO - PARA EFFECTS ESPECÍFICOS
// =====================================

/**
 * 🔐 SystemPermission - Tipos de permisos del sistema
 * 
 * 📖 PERMISOS COMUNES:
 * Enum para los permisos más comunes que una app puede necesitar.
 * En un app real, esto se mapearía a las constantes de Android.
 */
enum class SystemPermission {
    NOTIFICATIONS,      // POST_NOTIFICATIONS (Android 13+)
    CAMERA,            // CAMERA
    LOCATION,          // ACCESS_FINE_LOCATION
    MICROPHONE,        // RECORD_AUDIO
    STORAGE,           // READ_EXTERNAL_STORAGE
    CONTACTS           // READ_CONTACTS
}

/**
 * ⚙️ SystemSettingsType - Tipos de configuración del sistema
 * 
 * 📖 PANTALLAS DE CONFIGURACIÓN:
 * Para abrir pantallas específicas de configuración de Android.
 */
enum class SystemSettingsType {
    APP_NOTIFICATION_SETTINGS,    // Configuración de notificaciones de la app
    DISPLAY_SETTINGS,             // Configuración de pantalla
    SOUND_SETTINGS,               // Configuración de sonido
    ACCESSIBILITY_SETTINGS,       // Configuración de accesibilidad
    APP_DETAILS                   // Detalles de la aplicación
}


// =====================================
// 🧠 CONCEPTOS PEDAGÓGICOS ADICIONALES
// =====================================

/**
 * 💡 CUÁNDO USAR EFFECTS vs STATE:
 * 
 * ✅ USA EFFECTS PARA:
 * - Navegación después de operaciones
 * - Mostrar Snackbars/Toasts
 * - Abrir dialogs del sistema
 * - Compartir contenido
 * - Solicitar permisos
 * - Acciones que pasan una vez
 * 
 * ✅ USA STATE PARA:
 * - Datos que se muestran en pantalla
 * - Loading/Error states
 * - Dialogs que forman parte de la lógica de UI
 * - Cualquier cosa que pueda cambiar y necesite persistir
 * 
 * 🎯 REGLA DE ORO:
 * Si rota la pantalla y quieres que pase de nuevo → STATE
 * Si rota la pantalla y NO quieres que pase de nuevo → EFFECT
 */

/**
 * 🔄 PATRÓN DE IMPLEMENTACIÓN EN VIEWMODEL:
 * 
 * ```kotlin
 * class MyViewModel : ViewModel() {
 *     // State - persiste y se re-emite
 *     private val _uiState = MutableStateFlow(MyUiState.Loading)
 *     val uiState = _uiState.asStateFlow()
 *     
 *     // Effects - one-shot channel
 *     private val _uiEffects = Channel<MyUiEffect>()
 *     val uiEffects = _uiEffects.receiveAsFlow()
 *     
 *     fun handleEvent(event: MyUiEvent) {
 *         when (event) {
 *             is MyUiEvent.SaveData -> {
 *                 _uiState.value = MyUiState.Loading
 *                 // ... lógica de guardado ...
 *                 _uiState.value = MyUiState.Success
 *                 _uiEffects.trySend(MyUiEffect.ShowSnackbar("Guardado!"))
 *             }
 *         }
 *     }
 * }
 * ```
 */