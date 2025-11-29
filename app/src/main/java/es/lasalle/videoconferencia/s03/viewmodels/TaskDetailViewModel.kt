package es.lasalle.videoconferencia.s03.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import es.lasalle.videoconferencia.s03.models.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

// =====================================
// 📄 TASK DETAIL VIEW MODEL - CARGA ASYNC + PARÁMETROS
// =====================================

/**
 * 🎯 TaskDetailViewModel - Demostración de carga async con parámetros
 * 
 * 📖 CARGA DE DATOS CON PARÁMETROS:
 * Este ViewModel demuestra cómo manejar pantallas que:
 * - Reciben parámetros de navegación (taskId)
 * - Cargan datos asíncronamente basándose en esos parámetros
 * - Manejan estados de carga, éxito, error y "no encontrado"
 * - Permiten operaciones sobre los datos cargados
 * 
 * 🧠 CONCEPTOS CLAVE DEMOSTRADOS:
 * - Parameterized data loading
 * - Complex async state management
 * - Error handling with different error types
 * - Data manipulation operations
 * - Navigation with parameters
 * - Simulated network/database operations
 * 
 * 💡 DIFERENCIAS CON OTROS VIEWMODELS:
 * - Recibe parámetros externos (taskId)
 * - Estados más complejos (Loading, Success, Error, NotFound)
 * - Operaciones sobre datos existentes (marcar completada, tags)
 * - Simulación de operaciones de red realistas
 * 
 * 🔄 FLUJO TÍPICO:
 * 1. Navegación con taskId → loadTask(taskId)
 * 2. Loading state → Mostrar spinner
 * 3. Success/Error/NotFound → Mostrar contenido apropiado
 * 4. Usuario interactúa → Modificar datos localmente
 * 5. Refresh → Volver a cargar desde "servidor"
 * 
 * 📚 PEDAGÓGICO:
 * Ideal para entender async operations, error handling,
 * y manejo de parámetros en arquitectura MVVM.
 */
class TaskDetailViewModel : ViewModel() {

    // =====================================
    // 📊 STATE MANAGEMENT - Estados complejos
    // =====================================
    
    /**
     * 🏪 _uiState - Estado de detalle de tarea
     * 
     * 📖 ESTADOS COMPLEJOS:
     * Este ViewModel tiene 4 estados posibles:
     * - Loading: Cargando datos inicial o refresh
     * - Success: Datos cargados correctamente
     * - Error: Error genérico de carga
     * - NotFound: Tarea específica no existe
     * 
     * 🧠 CONCEPTO CLAVE - GRANULAR ERROR STATES:
     * Separamos "Error" de "NotFound" porque requieren
     * UI y acciones diferentes. Error puede reintentar,
     * NotFound debe navegar back.
     * 
     * 💡 ESTADO INICIAL:
     * Empezamos en Loading porque siempre necesitamos
     * cargar datos antes de mostrar contenido.
     */
    private val _uiState = MutableStateFlow<TaskDetailUiState>(TaskDetailUiState.Loading)
    val uiState: StateFlow<TaskDetailUiState> = _uiState.asStateFlow()

    // =====================================
    // ⚡ EFFECTS MANAGEMENT - Efectos diversos
    // =====================================
    
    /**
     * 📢 _uiEffects - Efectos para operaciones complejas
     * 
     * 📖 EFFECTS EN DETAIL SCREENS:
     * - NavigateBack: Cuando tarea no existe o error fatal
     * - ShowSnackbar: Feedback de operaciones (marcar completada, etc.)
     * - ShareTask: Compartir tarea con otras apps
     * - EditTask: Navegar a pantalla de edición
     * 
     * 🧠 CONCEPTO CLAVE - DIVERSE EFFECTS:
     * Las pantallas de detalle suelen tener más tipos
     * de efectos porque permiten más operaciones.
     */
    private val _uiEffects = Channel<TaskDetailUiEffect>()
    val uiEffects = _uiEffects.receiveAsFlow()

    // =====================================
    // 🗄️ SIMULATED DATA - Datos simulados para demostración
    // =====================================
    
    /**
     * 📝 sampleTasks - Base de datos simulada
     * 
     * 📖 SIMULACIÓN REALISTA:
     * Creamos una "base de datos" en memoria para simular
     * operaciones de red/base de datos de forma realista.
     * 
     * 🧠 CONCEPTO CLAVE - REALISTIC SIMULATION:
     * Aunque no usamos red real, simulamos comportamientos
     * típicos: delays, posibles errores, datos no encontrados.
     */
    private val sampleTasks = mapOf(
        "1" to TaskDetail(
            id = "1",
            title = "Implementar login con OAuth",
            description = "Configurar autenticación OAuth2 con Google y GitHub para permitir a los usuarios hacer login de forma segura.",
            isCompleted = false,
            priority = TaskPriority.HIGH,
            tags = listOf("auth", "oauth", "security"),
            createdAt = System.currentTimeMillis() - 86400000 // 1 día atrás
        ),
        "2" to TaskDetail(
            id = "2", 
            title = "Diseñar pantalla de perfil",
            description = "Crear wireframes y mockups para la pantalla de perfil de usuario, incluyendo foto, datos personales y configuraciones.",
            isCompleted = true,
            priority = TaskPriority.MEDIUM,
            tags = listOf("design", "ui", "profile"),
            createdAt = System.currentTimeMillis() - 172800000 // 2 días atrás
        ),
        "3" to TaskDetail(
            id = "3",
            title = "Optimizar performance de la app",
            description = "Identificar y corregir bottlenecks de performance, especialmente en listas largas y navegación entre pantallas.",
            isCompleted = false,
            priority = TaskPriority.URGENT,
            tags = listOf("performance", "optimization", "profiling"),
            createdAt = System.currentTimeMillis() - 259200000 // 3 días atrás
        ),
        "sample-task-123" to TaskDetail(
            id = "sample-task-123",
            title = "Tarea de demostración",
            description = "Esta es una tarea creada automáticamente para demostrar la navegación con parámetros desde la lista de tareas.",
            isCompleted = false,
            priority = TaskPriority.LOW,
            tags = listOf("demo", "navigation", "params"),
            createdAt = System.currentTimeMillis()
        )
    )

    // =====================================
    // 🎯 EVENT HANDLING - Procesamiento de eventos
    // =====================================
    
    /**
     * 🎪 handleEvent - Punto de entrada único para eventos
     * 
     * 📖 COMPLEX EVENT HANDLING:
     * Este ViewModel maneja más tipos de eventos que los anteriores:
     * - Refresh data
     * - Mark as complete/incomplete  
     * - Add/remove tags
     * - Change priority
     * - Navigation back
     * 
     * 🧠 CONCEPTO CLAVE - RICH INTERACTIONS:
     * Las pantallas de detalle permiten más interacciones
     * porque muestran todos los datos de una entidad.
     * 
     * @param event Evento emitido desde la UI
     */
    fun handleEvent(event: TaskDetailUiEvent) {
        when (event) {
            TaskDetailUiEvent.RefreshData -> refreshCurrentTask()
            is TaskDetailUiEvent.MarkAsComplete -> markAsComplete(event.isCompleted)
            is TaskDetailUiEvent.AddTag -> addTag(event.tag)
            is TaskDetailUiEvent.RemoveTag -> removeTag(event.tag)
            is TaskDetailUiEvent.ChangePriority -> changePriority(event.newPriority)
            TaskDetailUiEvent.NavigateBack -> navigateBack()
        }
    }

    // =====================================
    // 📥 DATA LOADING - Carga de datos con parámetros
    // =====================================
    
    /**
     * 📊 loadTask - Cargar tarea específica por ID
     * 
     * 📖 PARAMETERIZED LOADING:
     * Esta función demuestra cómo cargar datos basándose
     * en parámetros recibidos de navegación.
     * 
     * 🧠 CONCEPTO CLAVE - ASYNC LOADING WITH PARAMS:
     * La mayoría de pantallas de detalle reciben un ID
     * y deben cargar datos específicos de ese ID.
     * 
     * 💡 ERROR HANDLING:
     * Manejamos 3 casos: éxito, no encontrado, y error genérico.
     * Cada uno requiere diferente tratamiento en la UI.
     * 
     * @param taskId ID de la tarea a cargar
     */
    fun loadTask(taskId: String) {
        _uiState.value = TaskDetailUiState.Loading
        
        viewModelScope.launch {
            try {
                // Simular delay de red
                delay(1000)
                
                // Simular posible error de red (5% probabilidad)
                if (Math.random() < 0.05) {
                    throw Exception("Error de conexión simulado")
                }
                
                // Buscar tarea en "base de datos"
                val task = sampleTasks[taskId]
                
                if (task != null) {
                    // Tarea encontrada
                    _uiState.value = TaskDetailUiState.Success(task)
                } else {
                    // Tarea no encontrada
                    _uiState.value = TaskDetailUiState.NotFound(taskId)
                    
                    // Navegar back después de mostrar error
                    _uiEffects.trySend(
                        TaskDetailUiEffect.NavigateBack(
                            withDelay = true,
                            delayMs = 2000
                        )
                    )
                }
                
            } catch (e: Exception) {
                // Error genérico de carga
                _uiState.value = TaskDetailUiState.Error(
                    message = e.message ?: "Error desconocido",
                    userMessage = "No se pudo cargar la tarea. Verifica tu conexión."
                )
            }
        }
    }

    // =====================================
    // 🔄 DATA OPERATIONS - Operaciones sobre datos cargados
    // =====================================
    
    /**
     * 🔄 refreshCurrentTask - Recargar datos actuales
     * 
     * 📖 REFRESH PATTERN:
     * Permite al usuario actualizar datos manualmente.
     * Típicamente usado con pull-to-refresh o botón refresh.
     * 
     * 🧠 CONCEPTO CLAVE - CURRENT STATE REFRESH:
     * Solo podemos refrescar si tenemos datos cargados.
     * Necesitamos el ID de la tarea actual para recargar.
     */
    private fun refreshCurrentTask() {
        val currentState = _uiState.value
        if (currentState is TaskDetailUiState.Success) {
            loadTask(currentState.task.id)
        }
    }
    
    /**
     * ✅ markAsComplete - Marcar tarea como completada/pendiente
     * 
     * 📖 STATE MUTATION:
     * Actualiza el estado local inmediatamente para mejor UX.
     * En app real, esto también haría una llamada al servidor.
     * 
     * 🧠 CONCEPTO CLAVE - OPTIMISTIC UPDATES:
     * Actualizamos la UI inmediatamente asumiendo que la operación
     * va a funcionar. Si falla, podríamos revertir el cambio.
     * 
     * @param isCompleted Nuevo estado de la tarea
     */
    private fun markAsComplete(isCompleted: Boolean) {
        val currentState = _uiState.value
        if (currentState is TaskDetailUiState.Success) {
            // Simular operación async
            viewModelScope.launch {
                try {
                    // Actualizar estado optimísticamente
                    val updatedTask = currentState.task.copy(isCompleted = isCompleted)
                    _uiState.value = TaskDetailUiState.Success(updatedTask)
                    
                    // Simular delay de servidor
                    delay(300)
                    
                    // Confirmar con efecto
                    val message = if (isCompleted) {
                        "Tarea marcada como completada"
                    } else {
                        "Tarea marcada como pendiente"
                    }
                    
                    _uiEffects.trySend(
                        TaskDetailUiEffect.ShowSnackbar(
                            message = message,
                            actionLabel = "Deshacer",
                            onActionClick = {
                                // Deshacer el cambio
                                markAsComplete(!isCompleted)
                            }
                        )
                    )
                    
                } catch (e: Exception) {
                    // Revertir cambio si falla
                    _uiState.value = currentState
                    _uiEffects.trySend(
                        TaskDetailUiEffect.ShowSnackbar(
                            message = "Error al actualizar tarea"
                        )
                    )
                }
            }
        }
    }
    
    /**
     * 🏷️ addTag - Agregar etiqueta a la tarea
     * 
     * 📖 COLLECTION OPERATIONS:
     * Demuestra cómo agregar elementos a colecciones
     * de forma inmutable usando copy() y operadores de lista.
     * 
     * @param tag Nueva etiqueta a agregar
     */
    private fun addTag(tag: String) {
        val currentState = _uiState.value
        if (currentState is TaskDetailUiState.Success) {
            val trimmedTag = tag.trim().lowercase()
            
            // Validar que no esté vacío y no exista ya
            if (trimmedTag.isNotEmpty() && !currentState.task.tags.contains(trimmedTag)) {
                val updatedTask = currentState.task.copy(
                    tags = currentState.task.tags + trimmedTag
                )
                _uiState.value = TaskDetailUiState.Success(updatedTask)
                
                _uiEffects.trySend(
                    TaskDetailUiEffect.ShowSnackbar(
                        message = "Etiqueta '$trimmedTag' agregada"
                    )
                )
            } else if (currentState.task.tags.contains(trimmedTag)) {
                _uiEffects.trySend(
                    TaskDetailUiEffect.ShowSnackbar(
                        message = "La etiqueta ya existe"
                    )
                )
            }
        }
    }
    
    /**
     * 🗑️ removeTag - Quitar etiqueta de la tarea
     * 
     * 📖 COLLECTION REMOVAL:
     * Demuestra cómo remover elementos de colecciones
     * de forma inmutable usando filter().
     * 
     * @param tag Etiqueta a remover
     */
    private fun removeTag(tag: String) {
        val currentState = _uiState.value
        if (currentState is TaskDetailUiState.Success) {
            val updatedTask = currentState.task.copy(
                tags = currentState.task.tags.filter { it != tag }
            )
            _uiState.value = TaskDetailUiState.Success(updatedTask)
            
            _uiEffects.trySend(
                TaskDetailUiEffect.ShowSnackbar(
                    message = "Etiqueta '$tag' eliminada",
                    actionLabel = "Deshacer",
                    onActionClick = {
                        // Volver a agregar el tag
                        addTag(tag)
                    }
                )
            )
        }
    }
    
    /**
     * 🚨 changePriority - Cambiar prioridad de la tarea
     * 
     * 📖 ENUM UPDATES:
     * Demuestra cómo actualizar campos enum de forma type-safe.
     * 
     * @param newPriority Nueva prioridad de la tarea
     */
    private fun changePriority(newPriority: TaskPriority) {
        val currentState = _uiState.value
        if (currentState is TaskDetailUiState.Success) {
            val updatedTask = currentState.task.copy(priority = newPriority)
            _uiState.value = TaskDetailUiState.Success(updatedTask)
            
            val priorityText = when (newPriority) {
                TaskPriority.LOW -> "baja"
                TaskPriority.MEDIUM -> "media"
                TaskPriority.HIGH -> "alta"
                TaskPriority.URGENT -> "urgente"
            }
            
            _uiEffects.trySend(
                TaskDetailUiEffect.ShowSnackbar(
                    message = "Prioridad cambiada a $priorityText"
                )
            )
        }
    }
    
    /**
     * ↩️ navigateBack - Manejar navegación hacia atrás
     * 
     * 📖 MANAGED NAVIGATION:
     * Permite al ViewModel controlar cuándo y cómo navegar back.
     * Útil para validaciones o operaciones pendientes.
     * 
     * 🧠 CONCEPTO CLAVE - CONTROLLED NAVIGATION:
     * No siempre queremos navegar inmediatamente.
     * El ViewModel puede validar o guardar antes de permitir navegación.
     */
    private fun navigateBack() {
        // En app real: verificar si hay cambios sin guardar
        // if (hasUnsavedChanges()) {
        //     showSaveDialog()
        // } else {
        //     _uiEffects.trySend(TaskDetailUiEffect.NavigateBack())
        // }
        
        _uiEffects.trySend(TaskDetailUiEffect.NavigateBack())
    }

    // =====================================
    // 🎬 ADDITIONAL ACTIONS - Acciones adicionales
    // =====================================
    
    /**
     * 📤 shareTask - Compartir tarea con otras apps
     * 
     * 📖 EXTERNAL INTEGRATIONS:
     * Demuestra cómo integrar con otras apps del sistema.
     * El efecto maneja los detalles de implementación.
     */
    fun shareTask() {
        val currentState = _uiState.value
        if (currentState is TaskDetailUiState.Success) {
            val task = currentState.task
            val shareText = buildShareText(task)
            
            _uiEffects.trySend(
                TaskDetailUiEffect.ShareTask(
                    taskTitle = task.title,
                    taskDescription = task.description,
                    shareText = shareText
                )
            )
        }
    }
    
    /**
     * ✏️ editTask - Navegar a pantalla de edición
     * 
     * 📖 NAVIGATION TO EDIT:
     * Patrón común: ver detalle → editar → volver a detalle.
     * El ViewModel facilita esta navegación.
     */
    fun editTask() {
        val currentState = _uiState.value
        if (currentState is TaskDetailUiState.Success) {
            _uiEffects.trySend(
                TaskDetailUiEffect.EditTask(currentState.task.id)
            )
        }
    }

    // =====================================
    // 🛠️ HELPER FUNCTIONS - Funciones auxiliares
    // =====================================
    
    /**
     * 📝 buildShareText - Construir texto para compartir
     * 
     * 📖 TEXT FORMATTING:
     * Crea un texto bien formateado para compartir en otras apps.
     * Incluye toda la información relevante de la tarea.
     * 
     * @param task Tarea a compartir
     * @return Texto formateado para compartir
     */
    private fun buildShareText(task: TaskDetail): String {
        val status = if (task.isCompleted) "✅ Completada" else "⏳ Pendiente"
        val priority = when (task.priority) {
            TaskPriority.LOW -> "🟢 Baja"
            TaskPriority.MEDIUM -> "🟡 Media"
            TaskPriority.HIGH -> "🟠 Alta"
            TaskPriority.URGENT -> "🔴 Urgente"
        }
        
        return buildString {
            appendLine("📋 ${task.title}")
            appendLine()
            appendLine("Estado: $status")
            appendLine("Prioridad: $priority")
            if (task.tags.isNotEmpty()) {
                appendLine("Etiquetas: ${task.tags.joinToString(", ")}")
            }
            appendLine()
            appendLine("Descripción:")
            appendLine(task.description)
            appendLine()
            appendLine("Compartido desde Mi App de Tareas")
        }
    }
}

// =====================================
// 🧠 CONCEPTOS PEDAGÓGICOS ADICIONALES
// =====================================

/**
 * 💡 COMPARACIÓN DE VIEWMODELS POR COMPLEJIDAD:
 * 
 * 🔧 SettingsViewModel (Simple):
 * - 2 estados (Loading, Loaded)
 * - Operaciones síncronas
 * - Sin parámetros externos
 * - Sin validación compleja
 * 
 * 👤 ProfileViewModel (Intermedio):
 * - 4 estados (Loading, Idle, Success, Error)
 * - Validación en tiempo real
 * - Operaciones async simples
 * - Sin parámetros externos
 * 
 * 📄 TaskDetailViewModel (Complejo):
 * - 4 estados especializados (Loading, Success, Error, NotFound)
 * - Operaciones async complejas
 * - Recibe parámetros externos
 * - Múltiples tipos de operaciones sobre datos
 * - Manejo de colecciones
 * - Integración con sistemas externos
 */

/**
 * 🔄 PATRONES DE CARGA ASYNC:
 * 
 * 📊 LOADING PATTERNS:
 * 1. Parameter-based loading: loadTask(id)
 * 2. Refresh pattern: refrescar datos actuales
 * 3. Optimistic updates: actualizar UI inmediatamente
 * 4. Error recovery: reintentar operaciones fallidas
 * 
 * 🎯 STATE MANAGEMENT PATTERNS:
 * - Loading → Success (happy path)
 * - Loading → Error (retryable)
 * - Loading → NotFound (navigate away)
 * - Success → Loading (refresh)
 * 
 * 💡 ERROR HANDLING STRATEGIES:
 * - Granular error states (Error vs NotFound)
 * - User-friendly error messages
 * - Automatic retry for network errors
 * - Graceful degradation when possible
 */

/**
 * 🧪 TESTING COMPLEX VIEWMODELS:
 * 
 * 🔍 TEST STRATEGIES:
 * ```kotlin
 * @Test
 * fun `loadTask with valid id should emit Success state`() = runTest {
 *     viewModel.loadTask("1")
 *     advanceUntilIdle()
 *     
 *     val state = viewModel.uiState.value
 *     assertTrue(state is TaskDetailUiState.Success)
 *     assertEquals("1", state.task.id)
 * }
 * 
 * @Test  
 * fun `loadTask with invalid id should emit NotFound state`() = runTest {
 *     viewModel.loadTask("invalid")
 *     advanceUntilIdle()
 *     
 *     val state = viewModel.uiState.value
 *     assertTrue(state is TaskDetailUiState.NotFound)
 * }
 * 
 * @Test
 * fun `markAsComplete should update task and emit snackbar`() = runTest {
 *     viewModel.loadTask("1")
 *     advanceUntilIdle()
 *     
 *     viewModel.handleEvent(TaskDetailUiEvent.MarkAsComplete(true))
 *     
 *     val state = viewModel.uiState.value as TaskDetailUiState.Success
 *     assertTrue(state.task.isCompleted)
 *     
 *     val effect = viewModel.uiEffects.first()
 *     assertTrue(effect is TaskDetailUiEffect.ShowSnackbar)
 * }
 * ```
 */

/**
 * 📱 REAL-WORLD CONSIDERATIONS:
 * 
 * 🌐 NETWORK INTEGRATION:
 * ```kotlin
 * // Con Retrofit
 * suspend fun loadTask(id: String): TaskDetail {
 *     return try {
 *         apiService.getTask(id)
 *     } catch (e: HttpException) {
 *         when (e.code()) {
 *             404 -> throw TaskNotFoundException(id)
 *             else -> throw NetworkException(e.message())
 *         }
 *     }
 * }
 * ```
 * 
 * 💾 DATABASE INTEGRATION:
 * ```kotlin
 * // Con Room
 * @Query("SELECT * FROM tasks WHERE id = :id")
 * suspend fun getTask(id: String): TaskEntity?
 * 
 * // En ViewModel
 * val task = database.taskDao().getTask(id)
 *     ?: throw TaskNotFoundException(id)
 * ```
 * 
 * 🔄 REACTIVE DATA:
 * ```kotlin
 * // Observar cambios en tiempo real
 * @Query("SELECT * FROM tasks WHERE id = :id")
 * fun observeTask(id: String): Flow<TaskEntity?>
 * ```
 */