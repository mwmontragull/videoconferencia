package es.lasalle.videoconferencia.s03.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import es.lasalle.videoconferencia.s03.models.*
import es.lasalle.videoconferencia.s03.viewmodels.SettingsViewModel
import es.lasalle.videoconferencia.ui.theme.Dimensions
import es.lasalle.videoconferencia.ui.theme.VideoconferenciaTheme

// =====================================
// ⚙️ SETTINGS SCREEN - CONFIGURACIÓN SIMPLE CON MVVM
// =====================================

/**
 * 🎯 SettingsScreen - Demostración de configuración simple con MVVM + UDF
 * 
 * 📖 CONCEPTOS EDUCATIVOS CUBIERTOS:
 * 
 * 🔘 SWITCH CONTROLS:
 * - Toggle switches para preferencias booleanas
 * - Estados inmediatos sin persistencia
 * - Feedback visual con Toast messages
 * - Iconografía descriptiva para cada setting
 * 
 * 🏗️ MVVM SIMPLIFICADO:
 * - Solo dos estados: Loading y Loaded
 * - Eventos simples de toggle
 * - Sin validación compleja
 * - Efectos para feedback inmediato
 * 
 * 🎭 COMPOSE PATTERNS:
 * - ListItem para consistent layout
 * - Switch components integrados
 * - Icon + Text + Switch pattern
 * - Loading state con skeleton UI
 * 
 * ⚡ EFFECTS SIMPLES:
 * - Toast para confirmaciones rápidas
 * - Sin navegación compleja
 * - Feedback inmediato de cambios
 * - Reset functionality
 * 
 * 🧠 ARQUITECTURA SIMPLE:
 * - Demostración de MVVM sin complejidad
 * - Perfect para entender conceptos básicos
 * - Estado en memoria (no persiste)
 * - Ideal para aprender antes de casos complejos
 */
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = viewModel()
) {
    /**
     * 🎭 State observation - Observación del estado
     * 
     * 📖 SIMPLE STATE OBSERVATION:
     * Solo observamos un StateFlow simple con dos estados posibles.
     * Mucho más directo que formularios complejos.
     */
    val uiState by viewModel.uiState.collectAsState()
    
    /**
     * 🍞 Toast context - Para mostrar toasts
     * 
     * 📖 ANDROID CONTEXT:
     * LocalContext.current nos da acceso al Context de Android
     * necesario para mostrar Toast messages.
     */
    val context = LocalContext.current

    /**
     * ⚡ Effects handling - Manejo de efectos
     * 
     * 📖 SIMPLE EFFECTS:
     * Principalmente Toast messages para feedback inmediato.
     * Mucho más simple que los efectos del ProfileScreen.
     */
    LaunchedEffect(Unit) {
        viewModel.uiEffects.collect { effect ->
            when (effect) {
                is SettingsUiEffect.ShowToast -> {
                    val duration = if (effect.isLong) {
                        Toast.LENGTH_LONG
                    } else {
                        Toast.LENGTH_SHORT
                    }
                    Toast.makeText(context, effect.message, duration).show()
                }
                is SettingsUiEffect.RequestPermission -> {
                    // En app real: solicitar permisos del sistema
                    Toast.makeText(
                        context, 
                        "Solicitando permiso: ${effect.permission}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                is SettingsUiEffect.RestartApp -> {
                    // En app real: reiniciar la aplicación
                    Toast.makeText(
                        context,
                        "App se reiniciaría: ${effect.reason}",
                        Toast.LENGTH_LONG
                    ).show()
                }
                is SettingsUiEffect.OpenSystemSettings -> {
                    // En app real: abrir configuración del sistema
                    Toast.makeText(
                        context,
                        "Abriendo configuración: ${effect.settingsType}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    /**
     * 🎪 State-based UI - UI basada en estado
     * 
     * 📖 SIMPLE STATE HANDLING:
     * Solo dos estados: Loading (spinner) y Loaded (configuraciones).
     * Mucho más directo que estados complejos.
     */
    when (val currentState = uiState) {
        SettingsUiState.Loading -> {
            SettingsLoadingContent()
        }
        is SettingsUiState.Loaded -> {
            SettingsContent(
                settings = currentState,
                onEvent = viewModel::handleEvent
            )
        }
    }
}

// =====================================
// ⏳ LOADING CONTENT - Estado de carga
// =====================================

/**
 * ⏳ SettingsLoadingContent - UI durante carga inicial
 * 
 * 📖 SKELETON LOADING:
 * Muestra un skeleton de cómo se verán las configuraciones.
 * Mejor UX que solo un spinner genérico.
 * 
 * 🧠 CONCEPTO CLAVE - SKELETON UI:
 * El skeleton imita la estructura final para que el usuario
 * sepa qué esperar cuando termine de cargar.
 */
@Composable
private fun SettingsLoadingContent() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(Dimensions.spaceMedium)
    ) {
        /**
         * 📱 Header con loading
         */
        SettingsHeader()
        
        Spacer(modifier = Modifier.height(Dimensions.spaceMedium))
        
        /**
         * 🔄 Skeleton items
         */
        repeat(4) { index ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = Dimensions.spaceXSmall),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(Dimensions.spaceMedium),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(Dimensions.spaceMedium)
                    ) {
                        // Skeleton icon
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .background(
                                    MaterialTheme.colorScheme.outline,
                                    MaterialTheme.shapes.small
                                )
                        )
                        
                        // Skeleton text
                        Box(
                            modifier = Modifier
                                .width(120.dp)
                                .height(16.dp)
                                .background(
                                    MaterialTheme.colorScheme.outline,
                                    MaterialTheme.shapes.small
                                )
                        )
                    }
                    
                    // Skeleton switch
                    Box(
                        modifier = Modifier
                            .width(48.dp)
                            .height(24.dp)
                            .background(
                                MaterialTheme.colorScheme.outline,
                                MaterialTheme.shapes.medium
                            )
                    )
                }
            }
        }
        
        /**
         * ⏳ Loading indicator
         */
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Dimensions.spaceSmall)
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    strokeWidth = 2.dp
                )
                Text(
                    text = "Cargando configuración...",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

// =====================================
// ⚙️ SETTINGS CONTENT - Configuración cargada
// =====================================

/**
 * 🎛️ SettingsContent - Lista de configuraciones interactivas
 * 
 * 📖 SETTINGS LIST DESIGN:
 * - Card containers para cada sección
 * - Icon + Label + Switch pattern
 * - Grouping lógico de configuraciones
 * - Botón de reset al final
 * 
 * @param settings Estado actual de las configuraciones
 * @param onEvent Callback para enviar eventos
 */
@Composable
private fun SettingsContent(
    settings: SettingsUiState.Loaded,
    onEvent: (SettingsUiEvent) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(Dimensions.spaceMedium),
        verticalArrangement = Arrangement.spacedBy(Dimensions.spaceSmall)
    ) {
        /**
         * 📱 Header Section
         */
        SettingsHeader()
        
        Spacer(modifier = Modifier.height(Dimensions.spaceMedium))
        
        /**
         * 🔔 Notifications Section
         */
        SettingsSection(title = "Notificaciones") {
            SettingsItem(
                icon = Icons.Default.Notifications,
                title = "Notificaciones Push",
                description = "Recibir notificaciones de la app",
                checked = settings.notificationsEnabled,
                onCheckedChange = { onEvent(SettingsUiEvent.ToggleNotifications) }
            )
        }
        
        /**
         * 🎨 Appearance Section
         */
        SettingsSection(title = "Apariencia") {
            SettingsItem(
                icon = Icons.Default.DarkMode,
                title = "Modo Oscuro",
                description = "Usar tema oscuro en toda la app",
                checked = settings.darkModeEnabled,
                onCheckedChange = { onEvent(SettingsUiEvent.ToggleDarkMode) }
            )
        }
        
        /**
         * 🔊 Audio & Haptics Section
         */
        SettingsSection(title = "Audio y Vibración") {
            SettingsItem(
                icon = Icons.Default.VolumeUp,
                title = "Sonidos",
                description = "Reproducir sonidos de feedback",
                checked = settings.soundEnabled,
                onCheckedChange = { onEvent(SettingsUiEvent.ToggleSound) }
            )
            
            SettingsItem(
                icon = Icons.Default.Vibration,
                title = "Vibración",
                description = "Feedback táctil en interacciones",
                checked = settings.vibrationEnabled,
                onCheckedChange = { onEvent(SettingsUiEvent.ToggleVibration) }
            )
        }
        
        Spacer(modifier = Modifier.height(Dimensions.spaceLarge))
        
        /**
         * 🔄 Reset Section
         */
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.errorContainer
            )
        ) {
            Column(
                modifier = Modifier.padding(Dimensions.spaceMedium)
            ) {
                Text(
                    text = "Zona de Peligro",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
                
                Spacer(modifier = Modifier.height(Dimensions.spaceSmall))
                
                Text(
                    text = "Restablecer toda la configuración a valores por defecto",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
                
                Spacer(modifier = Modifier.height(Dimensions.spaceMedium))
                
                OutlinedButton(
                    onClick = { onEvent(SettingsUiEvent.ResetToDefaults) },
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    ),
                    border = ButtonDefaults.outlinedButtonBorder.copy(
                        brush = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            MaterialTheme.colorScheme.error
                        ).brush
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.RestartAlt,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(Dimensions.spaceSmall))
                    Text("Resetear Configuración")
                }
            }
        }
    }
}

/**
 * 📱 SettingsHeader - Encabezado de configuración
 * 
 * 📖 CONSISTENT HEADER:
 * Mismo patrón que ProfileScreen para consistencia.
 * Icon + Title + Description pattern.
 */
@Composable
private fun SettingsHeader() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Dimensions.spaceSmall)
    ) {
        Icon(
            imageVector = Icons.Default.Settings,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        
        Text(
            text = "Configuración",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
        
        Text(
            text = "Personaliza tu experiencia",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

/**
 * 🗂️ SettingsSection - Sección agrupada de configuraciones
 * 
 * 📖 SECTION GROUPING:
 * Agrupa configuraciones relacionadas bajo un título.
 * Mejora la organización y escaneabilidad.
 * 
 * @param title Título de la sección
 * @param content Contenido de la sección
 */
@Composable
private fun SettingsSection(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = Dimensions.spaceSmall)
        )
        
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Column(
                modifier = Modifier.padding(vertical = Dimensions.spaceXSmall)
            ) {
                content()
            }
        }
    }
}

/**
 * 🎛️ SettingsItem - Item individual de configuración
 * 
 * 📖 SETTINGS ITEM DESIGN:
 * - Icon para identificación visual
 * - Title + Description para claridad
 * - Switch para toggle inmediato
 * - Touch target optimizado
 * - Accessibility compliance
 * 
 * @param icon Icono representativo
 * @param title Título del setting
 * @param description Descripción del setting
 * @param checked Estado actual del switch
 * @param onCheckedChange Callback para cambios
 */
@Composable
private fun SettingsItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    description: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    /**
     * 🎯 Touch target optimization
     * 
     * 📖 ACCESSIBILITY:
     * ListItem proporciona touch targets optimizados
     * y layout consistente para elementos de lista.
     */
    ListItem(
        headlineContent = {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge
            )
        },
        supportingContent = {
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        },
        leadingContent = {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        },
        trailingContent = {
            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange
            )
        },
        modifier = Modifier.clickable {
            onCheckedChange(!checked)
        }
    )
}

// =====================================
// 🎨 PREVIEWS - Para desarrollo y testing
// =====================================

/**
 * ⏳ Preview del estado de carga
 */
@Preview(name = "Settings Loading State")
@Composable
private fun SettingsLoadingPreview() {
    VideoconferenciaTheme {
        SettingsLoadingContent()
    }
}

/**
 * ⚙️ Preview de configuraciones cargadas
 */
@Preview(name = "Settings Loaded State")
@Composable
private fun SettingsContentPreview() {
    VideoconferenciaTheme {
        SettingsContent(
            settings = SettingsUiState.Loaded(
                notificationsEnabled = true,
                darkModeEnabled = false,
                soundEnabled = true,
                vibrationEnabled = false
            ),
            onEvent = {}
        )
    }
}

/**
 * 🌙 Preview en modo oscuro
 */
@Preview(
    name = "Settings - Dark Mode",
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun SettingsDarkPreview() {
    VideoconferenciaTheme {
        SettingsContent(
            settings = SettingsUiState.Loaded(
                notificationsEnabled = true,
                darkModeEnabled = true,
                soundEnabled = false,
                vibrationEnabled = true
            ),
            onEvent = {}
        )
    }
}

/**
 * 📱 Preview con todas las opciones activadas
 */
@Preview(name = "Settings - All Enabled")
@Composable
private fun SettingsAllEnabledPreview() {
    VideoconferenciaTheme {
        SettingsContent(
            settings = SettingsUiState.Loaded(
                notificationsEnabled = true,
                darkModeEnabled = true,
                soundEnabled = true,
                vibrationEnabled = true
            ),
            onEvent = {}
        )
    }
}

/**
 * 📱 Preview con todas las opciones desactivadas
 */
@Preview(name = "Settings - All Disabled")
@Composable
private fun SettingsAllDisabledPreview() {
    VideoconferenciaTheme {
        SettingsContent(
            settings = SettingsUiState.Loaded(
                notificationsEnabled = false,
                darkModeEnabled = false,
                soundEnabled = false,
                vibrationEnabled = false
            ),
            onEvent = {}
        )
    }
}

// =====================================
// 🧠 CONCEPTOS PEDAGÓGICOS ADICIONALES
// =====================================

/**
 * 💡 PATRONES DE CONFIGURACIÓN DEMOSTRADOS:
 * 
 * 🎛️ SWITCH PATTERNS:
 * - Immediate feedback: Cambios instantáneos
 * - Toggle consistency: Mismo comportamiento en todos los switches
 * - Visual feedback: Toast confirmations
 * - Logical grouping: Configuraciones relacionadas juntas
 * 
 * 🎨 UI ORGANIZATION PATTERNS:
 * - Section headers: Agrupación visual clara
 * - Card containers: Separación de secciones
 * - ListItem consistency: Layout uniforme
 * - Icon consistency: Visual language coherente
 * 
 * 📱 MOBILE UX PATTERNS:
 * - Touch target optimization: Área clickeable grande
 * - Skeleton loading: Feedback durante carga
 * - Danger zone: Reset button claramente separado
 * - Descriptive text: Explicación clara de cada opción
 */

/**
 * 🔄 COMPARACIÓN CON ProfileScreen:
 * 
 * 📝 ProfileScreen (Complejo):
 * - 4 estados diferentes
 * - Validación compleja
 * - Formulario con múltiples campos
 * - Operaciones async largas
 * - Error handling detallado
 * 
 * ⚙️ SettingsScreen (Simple):
 * - 2 estados simples
 * - Sin validación
 * - Switches independientes
 * - Operaciones inmediatas
 * - Feedback simple con Toast
 * 
 * 🎯 PROPÓSITO EDUCATIVO:
 * SettingsScreen muestra que no todos los screens necesitan
 * complejidad. A veces simple es mejor y más apropiado.
 */

/**
 * 🧪 TESTING CONSIDERATIONS:
 * 
 * ✅ FÁCIL DE TESTEAR:
 * ```kotlin
 * @Test
 * fun `settings screen should show all switches`() {
 *     composeTestRule.setContent {
 *         SettingsContent(
 *             settings = SettingsUiState.Loaded(),
 *             onEvent = {}
 *         )
 *     }
 *     
 *     composeTestRule.onNodeWithText("Notificaciones Push").assertIsDisplayed()
 *     composeTestRule.onNodeWithText("Modo Oscuro").assertIsDisplayed()
 *     composeTestRule.onNodeWithText("Sonidos").assertIsDisplayed()
 *     composeTestRule.onNodeWithText("Vibración").assertIsDisplayed()
 * }
 * 
 * @Test
 * fun `clicking switch should trigger event`() {
 *     val events = mutableListOf<SettingsUiEvent>()
 *     
 *     composeTestRule.setContent {
 *         SettingsContent(
 *             settings = SettingsUiState.Loaded(notificationsEnabled = false),
 *             onEvent = { events.add(it) }
 *         )
 *     }
 *     
 *     composeTestRule.onNodeWithText("Notificaciones Push").performClick()
 *     
 *     assertEquals(SettingsUiEvent.ToggleNotifications, events.first())
 * }
 * ```
 */