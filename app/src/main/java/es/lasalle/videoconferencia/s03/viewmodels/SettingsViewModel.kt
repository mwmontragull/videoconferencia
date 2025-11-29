package es.lasalle.videoconferencia.s03.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import es.lasalle.videoconferencia.s03.models.SettingsUiEffect
import es.lasalle.videoconferencia.s03.models.SettingsUiEvent
import es.lasalle.videoconferencia.s03.models.SettingsUiState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

// =====================================
// ⚙️ SETTINGS VIEW MODEL - CONFIGURACIÓN SIMPLE
// =====================================

/**
 * 🎯 SettingsViewModel - Gestión de configuración de la app
 * 
 * 📖 CONFIGURACIÓN SIMPLE EN MEMORIA:
 * Este ViewModel demuestra cómo manejar configuraciones simples
 * que NO persisten entre sesiones. Es perfecto para aprender
 * los conceptos básicos sin la complejidad de persistencia.
 * 
 * 🧠 CONCEPTOS CLAVE DEMOSTRADOS:
 * - Estados simples (Loading/Loaded)
 * - Toggle operations sin persistencia
 * - Effects para feedback inmediato
 * - Reset a valores por defecto
 * - Simulación de carga inicial
 * 
 * 💡 DIFERENCIAS CON ProfileViewModel:
 * - Menos estados (solo Loading/Loaded)
 * - Eventos más simples (toggles)
 * - Sin validación compleja
 * - Sin operaciones async complejas
 * - Enfoque en inmediatez de cambios
 * 
 * 🔄 FLUJO TÍPICO:
 * 1. Carga inicial → Loading → Loaded con defaults
 * 2. Usuario cambia setting → Inmediatamente actualizado
 * 3. Efecto de confirmación → Toast/Snackbar
 * 4. Estado persistente hasta cierre de app
 * 
 * 📚 PEDAGÓGICO:
 * Ideal para entender StateFlow y eventos simples
 * antes de pasar a casos más complejos.
 */
class SettingsViewModel : ViewModel() {

    // =====================================
    // 📊 STATE MANAGEMENT - Estado simple
    // =====================================
    
    /**
     * 🏪 _uiState - Estado de configuración
     * 
     * 📖 ESTADO SIMPLE:
     * Solo dos estados posibles: Loading (inicial) y Loaded (con datos).
     * No necesitamos Error state porque no hay operaciones que fallen.
     * 
     * 🧠 CONCEPTO CLAVE - SIMPLICITY:
     * No todos los ViewModels necesitan estados complejos.
     * Empezar simple ayuda a entender los conceptos fundamentales.
     * 
     * 💡 ESTADO INICIAL:
     * Empezamos en Loading para simular carga de preferencias.
     * En app real esto sería leer SharedPreferences o DataStore.
     */
    private val _uiState = MutableStateFlow<SettingsUiState>(SettingsUiState.Loading)
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    // =====================================
    // ⚡ EFFECTS MANAGEMENT - Feedback inmediato
    // =====================================
    
    /**
     * 📢 _uiEffects - Efectos para feedback de usuario
     * 
     * 📖 EFFECTS EN SETTINGS:
     * Principalmente para confirmaciones y feedback:
     * - Toasts para cambios simples
     * - Solicitud de permisos cuando sea necesario
     * - Confirmaciones de reset
     * 
     * 🧠 CONCEPTO CLAVE - IMMEDIATE FEEDBACK:
     * En configuraciones, el usuario espera feedback inmediato.
     * Los efectos nos permiten confirmar cada cambio sin retrasos.
     */
    private val _uiEffects = Channel<SettingsUiEffect>()
    val uiEffects = _uiEffects.receiveAsFlow()

    // =====================================
    // 🚀 INITIALIZATION - Carga inicial
    // =====================================
    
    /**
     * 🎬 init - Carga automática de configuración
     * 
     * 📖 AUTOMATIC LOADING:
     * En cuanto se crea el ViewModel, cargamos la configuración.
     * Esto simula leer configuración desde persistencia.
     * 
     * 🧠 CONCEPTO CLAVE - INITIALIZATION:
     * Los ViewModels pueden ejecutar lógica automáticamente
     * sin esperar a que la UI solicite datos.
     */
    init {
        loadSettings()
    }

    // =====================================
    // 🎯 EVENT HANDLING - Procesamiento de eventos
    // =====================================
    
    /**
     * 🎪 handleEvent - Punto de entrada único para eventos
     * 
     * 📖 CENTRALIZACIÓN DE EVENTOS:
     * Todos los eventos de settings pasan por aquí.
     * Delegamos a funciones específicas para organización.
     * 
     * 🧠 CONCEPTO CLAVE - SINGLE ENTRY POINT:
     * Facilita debugging y testing al tener un solo punto
     * donde se procesan todos los eventos.
     * 
     * @param event Evento emitido desde la UI
     */
    fun handleEvent(event: SettingsUiEvent) {
        when (event) {
            SettingsUiEvent.ToggleNotifications -> toggleNotifications()
            SettingsUiEvent.ToggleDarkMode -> toggleDarkMode()
            SettingsUiEvent.ToggleSound -> toggleSound()
            SettingsUiEvent.ToggleVibration -> toggleVibration()
            SettingsUiEvent.ResetToDefaults -> resetToDefaults()
        }
    }

    // =====================================
    // 🔄 TOGGLE OPERATIONS - Operaciones de cambio
    // =====================================
    
    /**
     * 🔔 toggleNotifications - Activar/desactivar notificaciones
     * 
     * 📖 TOGGLE PATTERN:
     * Patrón típico: leer estado actual, invertir valor, actualizar estado.
     * Cada toggle emite un efecto de confirmación inmediata.
     * 
     * 🧠 CONCEPTO CLAVE - IMMEDIATE UPDATE:
     * No hay delay ni validación. El cambio es inmediato
     * para dar sensación de respuesta instantánea.
     * 
     * 💡 REALISTIC BEHAVIOR:
     * En app real, podríamos verificar permisos antes de activar.
     * Aquí simulamos que siempre funciona para simplicidad.
     */
    private fun toggleNotifications() {
        val currentState = _uiState.value
        if (currentState is SettingsUiState.Loaded) {
            val newState = currentState.copy(
                notificationsEnabled = !currentState.notificationsEnabled
            )
            _uiState.value = newState
            
            // Efecto de confirmación
            val message = if (newState.notificationsEnabled) {
                "Notificaciones activadas"
            } else {
                "Notificaciones desactivadas"
            }
            
            _uiEffects.trySend(
                SettingsUiEffect.ShowToast(message)
            )
            
            // En app real: solicitar permisos si es necesario
            if (newState.notificationsEnabled) {
                // Simular que podríamos necesitar permisos
                // _uiEffects.trySend(
                //     SettingsUiEffect.RequestPermission(
                //         SystemPermission.NOTIFICATIONS,
                //         "Necesitamos permiso para enviar notificaciones"
                //     )
                // )
            }
        }
    }
    
    /**
     * 🌙 toggleDarkMode - Cambiar tema claro/oscuro
     * 
     * 📖 THEME SWITCHING:
     * En app real esto cambiaría el tema de toda la aplicación.
     * Aquí solo actualizamos el estado para demostración.
     * 
     * 🧠 CONCEPTO CLAVE - APP-WIDE EFFECTS:
     * Algunos settings afectan toda la app, no solo una pantalla.
     * Esto se manejaría típicamente con un ThemeViewModel global.
     */
    private fun toggleDarkMode() {
        val currentState = _uiState.value
        if (currentState is SettingsUiState.Loaded) {
            val newState = currentState.copy(
                darkModeEnabled = !currentState.darkModeEnabled
            )
            _uiState.value = newState
            
            val message = if (newState.darkModeEnabled) {
                "Modo oscuro activado"
            } else {
                "Modo claro activado"
            }
            
            _uiEffects.trySend(
                SettingsUiEffect.ShowToast(message, isLong = true)
            )
            
            // En app real: podrías necesitar reiniciar
            // _uiEffects.trySend(
            //     SettingsUiEffect.RestartApp("Aplicar cambio de tema")
            // )
        }
    }
    
    /**
     * 🔊 toggleSound - Activar/desactivar sonidos
     * 
     * 📖 AUDIO SETTINGS:
     * Controla si la app reproduce sonidos de feedback.
     * Útil para accesibilidad y preferencias de usuario.
     */
    private fun toggleSound() {
        val currentState = _uiState.value
        if (currentState is SettingsUiState.Loaded) {
            val newState = currentState.copy(
                soundEnabled = !currentState.soundEnabled
            )
            _uiState.value = newState
            
            val message = if (newState.soundEnabled) {
                "Sonidos activados"
            } else {
                "Sonidos desactivados"
            }
            
            _uiEffects.trySend(
                SettingsUiEffect.ShowToast(message)
            )
        }
    }
    
    /**
     * 📳 toggleVibration - Activar/desactivar vibración
     * 
     * 📖 HAPTIC FEEDBACK:
     * Controla si la app usa vibración para feedback táctil.
     * Importante para accesibilidad y ahorro de batería.
     */
    private fun toggleVibration() {
        val currentState = _uiState.value
        if (currentState is SettingsUiState.Loaded) {
            val newState = currentState.copy(
                vibrationEnabled = !currentState.vibrationEnabled
            )
            _uiState.value = newState
            
            val message = if (newState.vibrationEnabled) {
                "Vibración activada"
            } else {
                "Vibración desactivada"
            }
            
            _uiEffects.trySend(
                SettingsUiEffect.ShowToast(message)
            )
        }
    }

    // =====================================
    // 🔄 RESET FUNCTIONALITY - Volver a defaults
    // =====================================
    
    /**
     * 🔄 resetToDefaults - Resetear toda la configuración
     * 
     * 📖 RESET PATTERN:
     * Vuelve todas las configuraciones a sus valores por defecto.
     * Útil cuando el usuario quiere empezar de cero.
     * 
     * 🧠 CONCEPTO CLAVE - BATCH OPERATIONS:
     * En lugar de resetear cada setting individualmente,
     * creamos un estado nuevo con todos los defaults.
     * 
     * 💡 USER EXPERIENCE:
     * Proporcionamos feedback claro de que el reset fue exitoso.
     * En app real, podrías pedir confirmación antes de resetear.
     */
    private fun resetToDefaults() {
        val defaultState = SettingsUiState.Loaded()
        _uiState.value = defaultState
        
        _uiEffects.trySend(
            SettingsUiEffect.ShowToast(
                message = "Configuración restablecida a valores por defecto",
                isLong = true
            )
        )
    }

    // =====================================
    // 📥 DATA LOADING - Carga inicial de datos
    // =====================================
    
    /**
     * 📊 loadSettings - Cargar configuración inicial
     * 
     * 📖 SIMULATED LOADING:
     * Simula cargar configuración desde persistencia.
     * En app real esto sería SharedPreferences, DataStore, o Room.
     * 
     * 🧠 CONCEPTO CLAVE - ASYNC INITIALIZATION:
     * Aunque la carga es simulada, usamos corrutinas para demostrar
     * el patrón típico de carga asíncrona de datos.
     * 
     * 💡 LOADING STATES:
     * Mostramos Loading brevemente para enseñar el patrón,
     * aunque en configuraciones reales la carga sería instantánea.
     */
    private fun loadSettings() {
        viewModelScope.launch {
            // Simular delay de carga
            delay(500)
            
            // Cargar configuración (en app real: leer de persistencia)
            val loadedSettings = SettingsUiState.Loaded(
                notificationsEnabled = true,  // Default value
                darkModeEnabled = false,      // Default value
                soundEnabled = true,          // Default value
                vibrationEnabled = true       // Default value
            )
            
            _uiState.value = loadedSettings
        }
    }
}

// =====================================
// 🧠 CONCEPTOS PEDAGÓGICOS ADICIONALES
// =====================================

/**
 * 💡 DIFERENCIAS ENTRE VIEWMODELS:
 * 
 * 🔄 ProfileViewModel (Complejo):
 * - Estados múltiples: Loading, Idle, Success, Error
 * - Validación en tiempo real
 * - Operaciones async complejas
 * - Manejo de errores detallado
 * - Forms con múltiples campos
 * 
 * ⚙️ SettingsViewModel (Simple):
 * - Estados simples: Loading, Loaded
 * - Operaciones inmediatas
 * - Sin validación compleja
 * - Sin manejo de errores
 * - Toggles independientes
 * 
 * 🎯 CUÁNDO USAR CADA PATRÓN:
 * - Simple: Configuraciones, toggles, preferencias
 * - Complejo: Formularios, flujos de trabajo, operaciones de red
 */

/**
 * 🔧 SETTINGS BEST PRACTICES:
 * 
 * ✅ DO:
 * - Proporciona feedback inmediato
 * - Usa valores por defecto sensatos
 * - Agrupa configuraciones relacionadas
 * - Persiste cambios automáticamente
 * - Considera accesibilidad
 * 
 * ❌ DON'T:
 * - No abrumes con demasiadas opciones
 * - No hagas cambios que requieran reinicio
 * - No olvides validar permisos del sistema
 * - No uses configuraciones ambiguas
 * - No cambies configuraciones sin confirmación
 * 
 * 🎨 UI PATTERNS:
 * - Switch: Para configuraciones booleanas
 * - Slider: Para valores numéricos
 * - DropdownMenu: Para opciones múltiples
 * - ListPreference: Para listas de valores
 */

/**
 * 💾 PERSISTENCIA EN SETTINGS:
 * 
 * 📱 ANDROID OPTIONS:
 * - SharedPreferences: Simple key-value storage
 * - DataStore: Reemplazo moderno de SharedPreferences
 * - Room Database: Para configuraciones complejas
 * - Files: Para configuraciones personalizadas
 * 
 * 🔄 REACTIVE PATTERNS:
 * ```kotlin
 * // Con DataStore
 * val darkModeFlow = dataStore.data.map { preferences ->
 *     preferences[DARK_MODE_KEY] ?: false
 * }
 * 
 * // Con Room + Flow
 * @Query("SELECT * FROM settings WHERE id = 1")
 * fun getSettingsFlow(): Flow<Settings>
 * ```
 * 
 * 🧠 CONCEPTO CLAVE - REACTIVE SETTINGS:
 * Los settings deberían ser observables para que la UI
 * se actualice automáticamente cuando cambien.
 */

/**
 * 🧪 TESTING SETTINGS VIEWMODEL:
 * 
 * ✅ FÁCIL DE TESTEAR:
 * - Operaciones sincrónicas (toggles)
 * - Estados predecibles
 * - Sin dependencias externas
 * - Lógica simple
 * 
 * 🔍 EJEMPLO DE TEST:
 * ```kotlin
 * @Test
 * fun `when toggle notifications, should update state and emit effect`() = runTest {
 *     // Given
 *     viewModel.loadSettings() // Esperar carga inicial
 *     
 *     // When
 *     viewModel.handleEvent(SettingsUiEvent.ToggleNotifications)
 *     
 *     // Then
 *     val state = viewModel.uiState.value as SettingsUiState.Loaded
 *     assertFalse(state.notificationsEnabled)
 *     
 *     val effect = viewModel.uiEffects.first()
 *     assertTrue(effect is SettingsUiEffect.ShowToast)
 * }
 * ```
 */