package es.lasalle.videoconferencia.s03.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import es.lasalle.videoconferencia.s03.models.ProfileUiEffect
import es.lasalle.videoconferencia.s03.models.ProfileUiEvent
import es.lasalle.videoconferencia.s03.models.ProfileUiState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

// =====================================
// 👤 PROFILE VIEW MODEL - MVVM + UDF COMPLETO
// =====================================

/**
 * 🎯 ProfileViewModel - Ejemplo completo de MVVM + UDF pattern
 * 
 * 📖 QUÉ ES UN VIEWMODEL:
 * El ViewModel es la capa intermedia entre la UI (Composable) y la lógica de negocio.
 * Su responsabilidad es:
 * - Manejar el estado de la UI (UiState)
 * - Procesar eventos de la UI (UiEvent)
 * - Ejecutar lógica de negocio
 * - Emitir efectos laterales (UiEffect)
 * 
 * 🧠 CONCEPTOS CLAVE - MVVM PATTERN:
 * 
 *     ┌─────────────┐    UiEvent     ┌──────────────┐    Repository/UseCase    ┌─────────────┐
 *     │     UI      │ ─────────────> │  ViewModel   │ ─────────────────────> │ Data Layer  │
 *     │ (Composable)│                │              │                        │             │
 *     └─────────────┘                └──────────────┘                        └─────────────┘
 *           ↑                               │
 *           │ UiState + UiEffect           │ Domain Logic
 *           └──────────────────────────────┘
 * 
 * 💡 UNIDIRECTIONAL DATA FLOW (UDF):
 * 1. UI emite UiEvent
 * 2. ViewModel procesa evento
 * 3. ViewModel actualiza UiState
 * 4. ViewModel puede emitir UiEffect
 * 5. UI recompone con nuevo estado
 * 6. UI ejecuta efectos one-shot
 * 
 * 🔄 LIFECYCLE DEL VIEWMODEL:
 * - Se crea cuando la UI lo necesita por primera vez
 * - Sobrevive a rotaciones de pantalla (configuration changes)
 * - Se destruye cuando la UI se destruye permanentemente
 * - viewModelScope se cancela automáticamente en onCleared()
 */
class ProfileViewModel : ViewModel() {

    // =====================================
    // 📊 STATE MANAGEMENT - StateFlow para estado persistente
    // =====================================
    
    /**
     * 🏪 _uiState - Estado privado y mutable
     * 
     * 📖 PATRÓN DE BACKING PROPERTY:
     * - _uiState: MutableStateFlow privado para escribir
     * - uiState: StateFlow público de solo lectura para la UI
     * 
     * 🧠 ¿POR QUÉ STATFLOW?:
     * - StateFlow mantiene el último valor emitido
     * - Es "hot" - sigue emitiendo aunque no haya collectors
     * - Perfecto para estado de UI que debe persistir
     * - Sobrevive a configuration changes del Composable
     * 
     * 💡 ESTADO INICIAL:
     * Empezamos con ProfileUiState.Idle() con valores vacíos.
     * Esto representa un formulario limpio listo para usar.
     */
    private val _uiState = MutableStateFlow<ProfileUiState>(ProfileUiState.Idle())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    // =====================================
    // ⚡ EFFECTS MANAGEMENT - Channel para efectos one-shot
    // =====================================
    
    /**
     * 📢 _uiEffects - Canal para efectos laterales
     * 
     * 📖 PATRÓN DE CHANNEL:
     * - Channel<UiEffect> para enviar efectos one-shot
     * - receiveAsFlow() convierte a Flow para la UI
     * 
     * 🧠 ¿POR QUÉ CHANNEL Y NO STATEFLOW?:
     * - Channel consume cada elemento una vez
     * - StateFlow re-emitiría el último valor en cada recomposición
     * - Los efectos deben ejecutarse una sola vez, no persistir
     * 
     * 💡 EJEMPLO PRÁCTICO:
     * Si mostramos un Snackbar y el usuario rota la pantalla,
     * NO queremos que aparezca de nuevo el mismo Snackbar.
     */
    private val _uiEffects = Channel<ProfileUiEffect>()
    val uiEffects = _uiEffects.receiveAsFlow()

    // =====================================
    // 🎯 EVENT HANDLING - Punto de entrada único para eventos
    // =====================================
    
    /**
     * 🎪 handleEvent - Punto de entrada único para todos los eventos
     * 
     * 📖 SINGLE ENTRY POINT:
     * Todas las acciones de la UI pasan por esta función.
     * Esto centraliza la lógica y hace más fácil el testing.
     * 
     * 🧠 PATRÓN WHEN EXHAUSTIVO:
     * El when debe cubrir todos los casos posibles de ProfileUiEvent.
     * Si agregamos un nuevo evento, el compilador nos forzará a manejarlo.
     * 
     * 💡 SEPARACIÓN DE RESPONSABILIDADES:
     * Cada tipo de evento se delega a una función específica
     * para mantener el código organizado y legible.
     * 
     * @param event Evento emitido desde la UI
     */
    fun handleEvent(event: ProfileUiEvent) {
        when (event) {
            is ProfileUiEvent.UpdateName -> updateName(event.name)
            is ProfileUiEvent.UpdateEmail -> updateEmail(event.email)
            ProfileUiEvent.SubmitForm -> submitForm()
            ProfileUiEvent.ClearForm -> clearForm()
            ProfileUiEvent.DismissError -> dismissError()
        }
    }

    // =====================================
    // ✏️ FIELD UPDATE LOGIC - Actualización de campos con validación
    // =====================================
    
    /**
     * 👤 updateName - Actualizar campo nombre con validación en tiempo real
     * 
     * 📖 VALIDACIÓN EN TIEMPO REAL:
     * Cada vez que el usuario escribe, validamos inmediatamente.
     * Esto proporciona feedback instantáneo y mejor UX.
     * 
     * 🧠 CONCEPTO CLAVE - COPY STATE:
     * No mutamos el estado directamente, sino que creamos una nueva instancia
     * con .copy(). Esto es fundamental para el patrón inmutable.
     * 
     * 💡 LÓGICA DE VALIDACIÓN:
     * - Nombre vacío → error específico
     * - Nombre muy corto → error específico
     * - Nombre válido → sin error, recalcular validez del formulario
     * 
     * @param name Nuevo valor del campo nombre
     */
    private fun updateName(name: String) {
        val currentState = _uiState.value
        if (currentState !is ProfileUiState.Idle) return // Solo actualizar si estamos en Idle
        
        val nameError = validateName(name)
        val newState = currentState.copy(
            name = name,
            nameError = nameError,
            isFormValid = isFormValid(name, currentState.email)
        )
        
        _uiState.value = newState
    }
    
    /**
     * 📧 updateEmail - Actualizar campo email con validación en tiempo real
     * 
     * 📖 VALIDACIÓN DE EMAIL:
     * Validamos formato básico de email usando regex simple.
     * En una app real, podrías usar librerías más sofisticadas.
     * 
     * 🧠 CONCEPTO CLAVE - GUARD CLAUSE:
     * Verificamos que estamos en el estado correcto antes de proceder.
     * Si no estamos en Idle, ignoramos el evento.
     * 
     * @param email Nuevo valor del campo email
     */
    private fun updateEmail(email: String) {
        val currentState = _uiState.value
        if (currentState !is ProfileUiState.Idle) return
        
        val emailError = validateEmail(email)
        val newState = currentState.copy(
            email = email,
            emailError = emailError,
            isFormValid = isFormValid(currentState.name, email)
        )
        
        _uiState.value = newState
    }

    // =====================================
    // 📤 FORM SUBMISSION - Lógica de envío del formulario
    // =====================================
    
    /**
     * 📋 submitForm - Procesar envío del formulario
     * 
     * 📖 FLUJO DE ENVÍO:
     * 1. Validar que estamos en estado Idle
     * 2. Validar que el formulario es válido
     * 3. Cambiar estado a Loading
     * 4. Simular operación async (en app real: llamada a API)
     * 5. Cambiar estado a Success o Error
     * 6. Emitir efectos apropiados
     * 
     * 🧠 CONCEPTO CLAVE - ASYNC OPERATIONS:
     * Usamos viewModelScope.launch para operaciones asíncronas.
     * El scope se cancela automáticamente si el ViewModel se destruye.
     * 
     * 💡 SIMULACIÓN REALISTA:
     * Aunque no usamos red real, simulamos delays y posibles errores
     * para demostrar cómo manejar estados de carga.
     */
    private fun submitForm() {
        val currentState = _uiState.value
        if (currentState !is ProfileUiState.Idle || !currentState.isFormValid) return
        
        // 1. Cambiar a estado de carga
        _uiState.value = ProfileUiState.Loading
        
        // 2. Simular operación asíncrona
        viewModelScope.launch {
            try {
                // Simular delay de red
                delay(2000)
                
                // Simular posible error (10% de probabilidad)
                if (Math.random() < 0.1) {
                    throw Exception("Error de red simulado")
                }
                
                // 3. Éxito - guardar datos y emitir efectos
                val userName = currentState.name
                _uiState.value = ProfileUiState.Success(userName)
                
                // Emitir efectos one-shot
                _uiEffects.trySend(
                    ProfileUiEffect.ShowSnackbar(
                        message = "Perfil de $userName guardado correctamente",
                        actionLabel = "Ver"
                    )
                )
                _uiEffects.trySend(ProfileUiEffect.HideKeyboard)
                
                // Navegar de vuelta después de mostrar éxito
                delay(1500)
                _uiEffects.trySend(ProfileUiEffect.NavigateBack)
                
            } catch (e: Exception) {
                // 4. Error - mostrar estado de error
                _uiState.value = ProfileUiState.Error(
                    message = e.message ?: "Error desconocido"
                )
                
                _uiEffects.trySend(
                    ProfileUiEffect.ShowSnackbar(
                        message = "Error al guardar el perfil",
                        actionLabel = "Reintentar"
                    )
                )
            }
        }
    }

    // =====================================
    // 🧹 FORM ACTIONS - Acciones adicionales del formulario
    // =====================================
    
    /**
     * 🗑️ clearForm - Limpiar todos los campos del formulario
     * 
     * 📖 RESET A ESTADO INICIAL:
     * Vuelve el formulario a su estado inicial limpio.
     * Útil para formularios de "crear nuevo" después de guardar.
     * 
     * 🧠 CONCEPTO CLAVE - SIMPLE STATE RESET:
     * Simplemente asignamos un nuevo estado Idle con valores por defecto.
     * No necesitamos lógica compleja para resetear.
     */
    private fun clearForm() {
        _uiState.value = ProfileUiState.Idle()
        
        _uiEffects.trySend(
            ProfileUiEffect.ShowSnackbar(
                message = "Formulario limpiado"
            )
        )
    }
    
    /**
     * ❌ dismissError - Volver a estado normal desde error
     * 
     * 📖 RECOVERY FROM ERROR:
     * Permite al usuario volver a intentar después de un error.
     * Preserva los datos que había escrito el usuario.
     * 
     * 🧠 CONCEPTO CLAVE - ERROR RECOVERY:
     * No perdemos los datos del usuario cuando hay error.
     * Solo cambiamos de Error state a Idle state.
     */
    private fun dismissError() {
        val currentState = _uiState.value
        if (currentState is ProfileUiState.Error) {
            // Volver a Idle con formulario limpio (en app real, podrías preservar datos)
            _uiState.value = ProfileUiState.Idle()
        }
    }

    // =====================================
    // ✅ VALIDATION LOGIC - Lógica de validación de campos
    // =====================================
    
    /**
     * 👤 validateName - Validar campo de nombre
     * 
     * 📖 REGLAS DE VALIDACIÓN:
     * - No puede estar vacío
     * - Debe tener al menos 2 caracteres
     * - En app real: podría validar caracteres especiales, longitud máxima, etc.
     * 
     * 🧠 CONCEPTO CLAVE - PURE FUNCTIONS:
     * Las funciones de validación son "puras" - sin efectos secundarios.
     * Reciben input, devuelven resultado, sin modificar estado externo.
     * 
     * @param name Nombre a validar
     * @return String con error o null si es válido
     */
    private fun validateName(name: String): String? {
        return when {
            name.isBlank() -> "El nombre es obligatorio"
            name.trim().length < 2 -> "El nombre debe tener al menos 2 caracteres"
            else -> null
        }
    }
    
    /**
     * 📧 validateEmail - Validar campo de email
     * 
     * 📖 REGLAS DE VALIDACIÓN:
     * - No puede estar vacío
     * - Debe tener formato básico de email
     * - En app real: podrías validar dominios, hacer verificación async, etc.
     * 
     * @param email Email a validar
     * @return String con error o null si es válido
     */
    private fun validateEmail(email: String): String? {
        return when {
            email.isBlank() -> "El email es obligatorio"
            !email.contains("@") || !email.contains(".") -> "Formato de email inválido"
            email.length < 5 -> "Email muy corto"
            else -> null
        }
    }
    
    /**
     * ✅ isFormValid - Verificar si todo el formulario es válido
     * 
     * 📖 VALIDACIÓN GLOBAL:
     * El formulario es válido solo si TODOS los campos son válidos.
     * Esto determina si el botón "Guardar" está habilitado.
     * 
     * 🧠 CONCEPTO CLAVE - COMPOSED VALIDATION:
     * La validez del formulario se calcula a partir de las validaciones individuales.
     * No se almacena separadamente, se deriva del estado actual.
     * 
     * @param name Valor actual del nombre
     * @param email Valor actual del email
     * @return true si el formulario es válido para envío
     */
    private fun isFormValid(name: String, email: String): Boolean {
        return validateName(name) == null && validateEmail(email) == null
    }
}

// =====================================
// 🧠 CONCEPTOS PEDAGÓGICOS ADICIONALES
// =====================================

/**
 * 💡 PATRONES IMPLEMENTADOS EN ESTE VIEWMODEL:
 * 
 * 1️⃣ MVVM (Model-View-ViewModel):
 *    - View: ProfileScreen (Composable)
 *    - ViewModel: ProfileViewModel (esta clase)
 *    - Model: ProfileUiState, ProfileUiEvent, ProfileUiEffect
 * 
 * 2️⃣ UDF (Unidirectional Data Flow):
 *    - Eventos fluyen UP: UI → ViewModel
 *    - Estado fluye DOWN: ViewModel → UI
 *    - Una sola fuente de verdad (single source of truth)
 * 
 * 3️⃣ STATE HOISTING:
 *    - El estado vive en el ViewModel, no en el Composable
 *    - La UI es stateless y recibe todo como parámetros
 *    - Facilita testing y reutilización
 * 
 * 4️⃣ SEPARATION OF CONCERNS:
 *    - UI: Solo presentación y captura de eventos
 *    - ViewModel: Lógica de negocio y gestión de estado
 *    - Models: Definición de contratos (states, events, effects)
 */

/**
 * 🔄 LIFECYCLE INTERACTION:
 * 
 * 📱 CONFIGURATION CHANGES (rotación de pantalla):
 * - El ViewModel sobrevive a configuration changes
 * - El StateFlow mantiene el último estado
 * - El Channel de effects se mantiene
 * - La UI se recompone con el estado actual
 * 
 * 💀 ViewModel DESTRUCTION:
 * - Ocurre cuando el usuario navega permanentemente away
 * - viewModelScope se cancela automáticamente
 * - Todas las corrutinas en curso se cancelan
 * - Resources se liberan automáticamente
 * 
 * 🔁 RECOMPOSITION:
 * - Cuando cambia _uiState, la UI se recompone automáticamente
 * - Solo los Composables que leen ese estado se recomponen
 * - Los effects NO se re-ejecutan en recomposiciones
 */

/**
 * 🧪 TESTING CONSIDERATIONS:
 * 
 * ✅ FÁCIL DE TESTEAR:
 * - handleEvent() es una función pura con input/output claros
 * - StateFlow se puede observar en tests
 * - Effects channel se puede verificar
 * - No hay dependencias externas (sin inyección por simplicidad)
 * 
 * 🔍 EJEMPLO DE TEST:
 * ```kotlin
 * @Test
 * fun `when user enters valid name, should update state with no error`() {
 *     viewModel.handleEvent(ProfileUiEvent.UpdateName("Alice"))
 *     
 *     val state = viewModel.uiState.value as ProfileUiState.Idle
 *     assertEquals("Alice", state.name)
 *     assertNull(state.nameError)
 * }
 * ```
 */