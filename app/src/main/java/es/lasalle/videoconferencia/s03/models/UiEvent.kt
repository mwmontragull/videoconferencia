package es.lasalle.videoconferencia.s03.models

// =====================================
// 📥 UI EVENTS - SEALED INTERFACES PARA EVENTOS DE USUARIO
// =====================================

/**
 * 🎯 SEALED INTERFACES - Eventos type-safe desde la UI
 * 
 * 📖 QUÉ SON LOS UI EVENTS:
 * Los UiEvents representan todas las acciones que el usuario puede realizar
 * en la UI. Son enviados desde la UI hacia el ViewModel.
 * 
 * 🧠 CONCEPTO CLAVE - UNIDIRECTIONAL DATA FLOW (UDF):
 * 
 *     ┌─────────────┐    UiEvent     ┌──────────────┐
 *     │     UI      │ ─────────────> │  ViewModel   │
 *     │ (Composable)│                │              │
 *     └─────────────┘                └──────────────┘
 *           ↑                               │
 *           │ UiState                       │ Business Logic
 *           └───────────────────────────────┘
 * 
 * 💡 ¿POR QUÉ SEALED INTERFACES?:
 * - Type safety: Solo eventos válidos pueden ser enviados
 * - Exhaustive when: El compiler garantiza que manejas todos los casos
 * - Extensibilidad: Fácil agregar nuevos eventos sin romper código existente
 * - Separación clara: Cada screen tiene sus propios eventos
 * 
 * 🔄 FLUJO TÍPICO:
 * 1. Usuario hace click en botón
 * 2. UI emite UiEvent
 * 3. ViewModel recibe evento
 * 4. ViewModel ejecuta lógica de negocio
 * 5. ViewModel actualiza UiState
 * 6. UI recompone con nuevo estado
 */

// =====================================
// 👤 PROFILE SCREEN - EVENTOS DE FORMULARIO
// =====================================

/**
 * 📝 ProfileUiEvent - Eventos de la pantalla de perfil
 * 
 * 📖 EVENTOS DE FORMULARIO:
 * Representa todas las acciones que el usuario puede hacer
 * en la pantalla de perfil: escribir, enviar, limpiar.
 * 
 * 🧠 CONCEPTO CLAVE - GRANULARIDAD DE EVENTOS:
 * Cada evento tiene un propósito específico y bien definido.
 * Evitamos eventos genéricos como "updateField(fieldName, value)".
 * 
 * 💡 EJEMPLO DE USO EN UI:
 * ```kotlin
 * OutlinedTextField(
 *     value = name,
 *     onValueChange = { newName ->
 *         onEvent(ProfileUiEvent.UpdateName(newName))
 *     }
 * )
 * 
 * Button(
 *     onClick = { onEvent(ProfileUiEvent.SubmitForm) }
 * ) {
 *     Text("Guardar")
 * }
 * ```
 */
sealed interface ProfileUiEvent {
    
    /**
     * ✏️ UpdateName - Usuario cambia el campo nombre
     * Se dispara cada vez que el usuario escribe en el campo nombre.
     * Permite validación en tiempo real.
     * 
     * @param name Nuevo valor del campo nombre
     */
    data class UpdateName(val name: String) : ProfileUiEvent
    
    /**
     * 📧 UpdateEmail - Usuario cambia el campo email
     * Se dispara cada vez que el usuario escribe en el campo email.
     * Permite validación de formato en tiempo real.
     * 
     * @param email Nuevo valor del campo email
     */
    data class UpdateEmail(val email: String) : ProfileUiEvent
    
    /**
     * 📤 SubmitForm - Usuario envía el formulario
     * Se dispara cuando el usuario hace click en "Guardar".
     * Solo debe estar disponible si el formulario es válido.
     */
    object SubmitForm : ProfileUiEvent
    
    /**
     * 🗑️ ClearForm - Usuario limpia el formulario
     * Se dispara cuando el usuario hace click en "Limpiar".
     * Resetea todos los campos a valores vacíos.
     */
    object ClearForm : ProfileUiEvent
    
    /**
     * ❌ DismissError - Usuario cierra mensaje de error
     * Se dispara cuando el usuario hace click en cerrar error.
     * Vuelve el formulario al estado Idle.
     */
    object DismissError : ProfileUiEvent
}

// =====================================
// ⚙️ SETTINGS SCREEN - EVENTOS DE CONFIGURACIÓN
// =====================================

/**
 * 🔧 SettingsUiEvent - Eventos de la pantalla de configuración
 * 
 * 📖 EVENTOS DE SWITCHES:
 * Representa cambios en las preferencias del usuario.
 * Cada toggle switch tiene su propio evento específico.
 * 
 * 🧠 CONCEPTO CLAVE - EVENTOS ESPECÍFICOS:
 * En lugar de un evento genérico "ToggleSetting(settingName, value)",
 * usamos eventos específicos para cada configuración.
 * Esto es más type-safe y más fácil de mantener.
 * 
 * 💡 EJEMPLO DE USO EN UI:
 * ```kotlin
 * Switch(
 *     checked = notificationsEnabled,
 *     onCheckedChange = { 
 *         onEvent(SettingsUiEvent.ToggleNotifications)
 *     }
 * )
 * ```
 */
sealed interface SettingsUiEvent {
    
    /**
     * 🔔 ToggleNotifications - Activar/desactivar notificaciones
     * Cambia el estado de las notificaciones push.
     * En un app real, esto actualizaría permisos del sistema.
     */
    object ToggleNotifications : SettingsUiEvent
    
    /**
     * 🌙 ToggleDarkMode - Activar/desactivar modo oscuro
     * Cambia entre tema claro y oscuro.
     * En un app real, esto cambiaría el tema de toda la app.
     */
    object ToggleDarkMode : SettingsUiEvent
    
    /**
     * 🔊 ToggleSound - Activar/desactivar sonidos
     * Cambia si la app reproduce sonidos de feedback.
     */
    object ToggleSound : SettingsUiEvent
    
    /**
     * 📳 ToggleVibration - Activar/desactivar vibración
     * Cambia si la app usa vibración para feedback táctil.
     */
    object ToggleVibration : SettingsUiEvent
    
    /**
     * 🔄 ResetToDefaults - Resetear a valores por defecto
     * Vuelve todas las configuraciones a sus valores iniciales.
     */
    object ResetToDefaults : SettingsUiEvent
}

// =====================================
// 📋 TASK DETAIL SCREEN - EVENTOS DE DETALLE DE TAREA
// =====================================

/**
 * 📄 TaskDetailUiEvent - Eventos de la pantalla de detalle de tarea
 * 
 * 📖 EVENTOS DE DETALLE:
 * Eventos relacionados con ver y modificar una tarea específica.
 * Incluye cargar datos, marcar como completada, y refrescar.
 * 
 * 🧠 CONCEPTO CLAVE - EVENTOS DE CARGA:
 * Algunos eventos no necesitan datos (objects), otros sí (data classes).
 * RefreshData podría ser llamado cuando el usuario hace pull-to-refresh.
 * 
 * 💡 PARÁMETROS EN EVENTOS:
 * MarkAsComplete recibe el nuevo estado explícitamente,
 * en lugar de hacer toggle implícito. Esto es más claro.
 */
sealed interface TaskDetailUiEvent {
    
    /**
     * 🔄 RefreshData - Recargar datos de la tarea
     * Se dispara cuando el usuario hace pull-to-refresh o click en retry.
     * Vuelve a "llamar al servidor" para obtener datos actualizados.
     */
    object RefreshData : TaskDetailUiEvent
    
    /**
     * ✅ MarkAsComplete - Marcar tarea como completada/pendiente
     * Cambia el estado de completado de la tarea.
     * 
     * @param isCompleted Nuevo estado de la tarea (true = completada)
     */
    data class MarkAsComplete(val isCompleted: Boolean) : TaskDetailUiEvent
    
    /**
     * 🏷️ AddTag - Agregar etiqueta a la tarea
     * Permite al usuario agregar una nueva etiqueta.
     * 
     * @param tag Nueva etiqueta a agregar
     */
    data class AddTag(val tag: String) : TaskDetailUiEvent
    
    /**
     * 🗑️ RemoveTag - Quitar etiqueta de la tarea
     * Permite al usuario quitar una etiqueta existente.
     * 
     * @param tag Etiqueta a remover
     */
    data class RemoveTag(val tag: String) : TaskDetailUiEvent
    
    /**
     * 🚨 ChangePriority - Cambiar prioridad de la tarea
     * Permite cambiar la prioridad de la tarea.
     * 
     * @param newPriority Nueva prioridad de la tarea
     */
    data class ChangePriority(val newPriority: TaskPriority) : TaskDetailUiEvent
    
    /**
     * ↩️ NavigateBack - Volver a pantalla anterior
     * Se dispara cuando el usuario hace click en botón atrás.
     * El ViewModel puede hacer validaciones antes de navegar.
     */
    object NavigateBack : TaskDetailUiEvent
}

// =====================================
// 🧠 CONCEPTOS PEDAGÓGICOS ADICIONALES
// =====================================

/**
 * 💡 COMPARACIÓN: SEALED INTERFACE VS SEALED CLASS
 * 
 * SEALED INTERFACE (usado aquí para eventos):
 * ✅ Más flexible - permite implementar múltiples interfaces
 * ✅ Más semánticamente correcto para "acciones"
 * ✅ Mejor para eventos que no necesitan herencia de implementación
 * 
 * SEALED CLASS (usado en UiState):
 * ✅ Permite implementación compartida entre subclases
 * ✅ Mejor para estados que pueden tener comportamiento común
 * ✅ Tradicional para modeling de estados
 * 
 * 🎯 REGLA PRÁCTICA:
 * - Events → Sealed Interface (acciones puras)
 * - States → Sealed Class (pueden tener lógica compartida)
 */

/**
 * 🔄 PATRÓN DE NAMING PARA EVENTOS:
 * 
 * ✅ BUENOS NOMBRES:
 * - UpdateName, UpdateEmail (claros y específicos)
 * - SubmitForm, ClearForm (verbos de acción)
 * - ToggleNotifications (acción específica)
 * - RefreshData (acción clara)
 * 
 * ❌ MALOS NOMBRES:
 * - UpdateField (muy genérico)
 * - ButtonClicked (no dice qué hace)
 * - DoAction (no descriptivo)
 * - HandleInput (muy vago)
 * 
 * 💡 REGLA DE ORO:
 * El nombre del evento debe describir exactamente
 * qué acción está realizando el usuario.
 */