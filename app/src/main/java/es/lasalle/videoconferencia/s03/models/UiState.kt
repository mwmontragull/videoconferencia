package es.lasalle.videoconferencia.s03.models

// =====================================
// 📋 UI STATE - SEALED CLASSES PARA ESTADO DE UI
// =====================================

/**
 * 🎯 SEALED CLASSES - Modelado de estados type-safe
 * 
 * 📖 QUÉ SON LAS SEALED CLASSES:
 * Las sealed classes son clases que restringen la herencia a un conjunto fijo
 * de subclases. Son perfectas para representar estados finitos en UI.
 * 
 * 🧠 VENTAJAS SOBRE ENUMS:
 * - Pueden tener datos asociados (data classes)
 * - Type safety completo en when expressions
 * - No necesitan else exhaustivo
 * - Mejor expresividad del dominio
 * 
 * 💡 PATRÓN MVVM + UDF:
 * El UiState representa el estado completo de una pantalla.
 * El ViewModel emite estos estados y la UI los consume de forma reactiva.
 * 
 * 🔄 FLUJO UNIDIRECCIONAL (UDF):
 * UiEvent → ViewModel → UiState → UI → UiEvent (ciclo completo)
 */

// =====================================
// 👤 PERFIL SCREEN - ESTADOS DE FORMULARIO
// =====================================

/**
 * 📝 ProfileUiState - Estados de la pantalla de perfil
 * 
 * 📖 ESTADOS DEL FORMULARIO:
 * - Loading: Mientras se envía el formulario
 * - Idle: Estado normal, usuario puede interactuar
 * - Success: Formulario enviado correctamente
 * - Error: Error en validación o envío
 * 
 * 🧠 CONCEPTO CLAVE - DATA CLASSES EN SEALED:
 * Cada estado puede tener datos específicos asociados.
 * Esto es mucho más potente que usar booleanos separados.
 * 
 * 💡 EJEMPLO DE USO:
 * ```kotlin
 * when (uiState) {
 *     is ProfileUiState.Loading -> ShowSpinner()
 *     is ProfileUiState.Error -> ShowError(uiState.message)
 *     is ProfileUiState.Success -> ShowSuccess(uiState.userName)
 *     is ProfileUiState.Idle -> ShowForm(uiState.formData)
 * }
 * ```
 */
sealed class ProfileUiState {
    
    /**
     * 🔄 Loading - Formulario siendo procesado
     * Estado cuando el usuario ha enviado el formulario y estamos simulando
     * una llamada de red o validación async.
     */
    object Loading : ProfileUiState()
    
    /**
     * 😴 Idle - Estado normal de interacción
     * El usuario puede editar el formulario libremente.
     * Contiene todos los datos del formulario actual.
     * 
     * @param name Nombre actual en el campo
     * @param email Email actual en el campo
     * @param nameError Error de validación del nombre (null si válido)
     * @param emailError Error de validación del email (null si válido)
     * @param isFormValid True si el formulario es válido para envío
     */
    data class Idle(
        val name: String = "",
        val email: String = "",
        val nameError: String? = null,
        val emailError: String? = null,
        val isFormValid: Boolean = false
    ) : ProfileUiState()
    
    /**
     * ✅ Success - Formulario enviado correctamente
     * Estado temporal después de un envío exitoso.
     * 
     * @param userName Nombre del usuario guardado exitosamente
     */
    data class Success(
        val userName: String
    ) : ProfileUiState()
    
    /**
     * ❌ Error - Error general del formulario
     * Estado cuando hay un error que no es específico de un campo.
     * 
     * @param message Mensaje de error para mostrar al usuario
     */
    data class Error(
        val message: String
    ) : ProfileUiState()
}

// =====================================
// ⚙️ SETTINGS SCREEN - ESTADOS DE CONFIGURACIÓN
// =====================================

/**
 * 🔧 SettingsUiState - Estados de la pantalla de configuración
 * 
 * 📖 ESTADOS DE CONFIGURACIÓN:
 * - Loading: Cargando configuración inicial (simulado)
 * - Loaded: Configuración cargada y lista para modificar
 * 
 * 🧠 CONCEPTO CLAVE - ESTADO SIMPLE:
 * Para pantallas simples, a veces solo necesitamos 2 estados.
 * No todos los screens necesitan estados complejos.
 * 
 * 💡 SIN PERSISTENCIA:
 * En este ejemplo educativo, las configuraciones solo existen en memoria.
 * Al cerrar la app, todo vuelve a valores por defecto.
 */
sealed class SettingsUiState {
    
    /**
     * 🔄 Loading - Cargando configuración inicial
     * Estado inicial mientras "cargamos" las preferencias.
     * En un app real, esto sería leer de SharedPreferences o DataStore.
     */
    object Loading : SettingsUiState()
    
    /**
     * 📱 Loaded - Configuración lista para usar
     * Contiene todas las preferencias actuales del usuario.
     * 
     * @param notificationsEnabled Si las notificaciones están activadas
     * @param darkModeEnabled Si el modo oscuro está activado
     * @param soundEnabled Si los sonidos están activados
     * @param vibrationEnabled Si la vibración está activada
     */
    data class Loaded(
        val notificationsEnabled: Boolean = true,
        val darkModeEnabled: Boolean = false,
        val soundEnabled: Boolean = true,
        val vibrationEnabled: Boolean = true
    ) : SettingsUiState()
}

// =====================================
// 📋 TASK DETAIL SCREEN - ESTADOS DE DETALLE DE TAREA
// =====================================

/**
 * 📄 TaskDetailUiState - Estados de la pantalla de detalle de tarea
 * 
 * 📖 ESTADOS DE CARGA DE DATOS:
 * - Loading: Cargando datos de la tarea desde "servidor"
 * - Success: Tarea cargada correctamente
 * - Error: Error al cargar la tarea
 * - NotFound: Tarea no encontrada
 * 
 * 🧠 CONCEPTO CLAVE - ESTADOS DE RED:
 * Este patrón es muy común en apps reales que cargan datos.
 * Cada estado tiene exactamente los datos que necesita.
 * 
 * 💡 SIMULACIÓN REALISTA:
 * Aunque no usamos red real, simulamos los estados típicos
 * para enseñar el patrón correcto.
 */
sealed class TaskDetailUiState {
    
    /**
     * 🔄 Loading - Cargando detalles de la tarea
     * Estado inicial cuando entramos a la pantalla.
     * Simulamos una llamada de red que toma tiempo.
     */
    object Loading : TaskDetailUiState()
    
    /**
     * ✅ Success - Tarea cargada correctamente
     * Contiene todos los detalles de la tarea para mostrar.
     * 
     * @param task Datos completos de la tarea
     */
    data class Success(
        val task: TaskDetail
    ) : TaskDetailUiState()
    
    /**
     * ❌ Error - Error al cargar la tarea
     * Error genérico de red o procesamiento.
     * 
     * @param message Mensaje de error técnico
     * @param userMessage Mensaje amigable para el usuario
     */
    data class Error(
        val message: String,
        val userMessage: String = "No se pudo cargar la tarea"
    ) : TaskDetailUiState()
    
    /**
     * 🔍 NotFound - Tarea no encontrada
     * Estado específico cuando el ID no existe.
     * Permite manejar este caso de forma diferente a un error genérico.
     * 
     * @param taskId ID de la tarea que no se encontró
     */
    data class NotFound(
        val taskId: String
    ) : TaskDetailUiState()
}

// =====================================
// 📝 DATA CLASSES - MODELOS DE DATOS
// =====================================

/**
 * 📋 TaskDetail - Modelo de datos para una tarea completa
 * 
 * 📖 DOMAIN MODEL:
 * Representa una tarea con todos sus detalles.
 * En un app real, esto vendría de una API o base de datos.
 * 
 * 🧠 CONCEPTO CLAVE - IMMUTABLE DATA:
 * Data classes son inmutables por defecto (val).
 * Cambios se hacen con .copy(), no modificando directamente.
 * 
 * @param id Identificador único de la tarea
 * @param title Título de la tarea
 * @param description Descripción detallada
 * @param isCompleted Si la tarea está completada
 * @param priority Prioridad de la tarea
 * @param tags Lista de etiquetas asociadas
 * @param createdAt Cuándo se creó la tarea (timestamp simulado)
 */
data class TaskDetail(
    val id: String,
    val title: String,
    val description: String,
    val isCompleted: Boolean = false,
    val priority: TaskPriority = TaskPriority.MEDIUM,
    val tags: List<String> = emptyList(),
    val createdAt: Long = System.currentTimeMillis()
)

/**
 * 🚨 TaskPriority - Prioridades de tareas
 * 
 * 📖 ENUM PARA VALORES FIJOS:
 * Cuando tienes un conjunto fijo de valores sin datos adicionales,
 * enum es perfecto. Para estados con datos, usa sealed classes.
 * 
 * 🧠 DIFERENCIA CLAVE:
 * - Enum: Valores fijos sin datos (HIGH, MEDIUM, LOW)
 * - Sealed class: Estados con datos (Loading, Success(data), Error(message))
 */
enum class TaskPriority {
    LOW,
    MEDIUM,
    HIGH,
    URGENT
}