package es.lasalle.videoconferencia.s03.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import es.lasalle.videoconferencia.s03.models.*
import es.lasalle.videoconferencia.s03.viewmodels.ProfileViewModel
import es.lasalle.videoconferencia.ui.theme.Dimensions
import es.lasalle.videoconferencia.ui.theme.VideoconferenciaTheme

// =====================================
// 👤 PROFILE SCREEN - FORMULARIO COMPLETO CON MVVM
// =====================================

/**
 * 🎯 ProfileScreen - Demostración completa de formulario con MVVM + UDF
 *
 * 📖 CONCEPTOS EDUCATIVOS CUBIERTOS:
 *
 * 📝 FORMULARIO COMPLEJO:
 * - Validación en tiempo real
 * - Estados de error por campo
 * - Validación global del formulario
 * - Feedback visual inmediato
 * - Manejo de estados de carga/éxito/error
 *
 * 🏗️ MVVM + UDF COMPLETO:
 * - ViewModel como single source of truth
 * - UiState para todo el estado del formulario
 * - UiEvent para todas las acciones del usuario
 * - UiEffect para efectos laterales (snackbar, navegación)
 * - Unidirectional Data Flow perfecto
 *
 * 🎭 COMPOSE BEST PRACTICES:
 * - State hoisting completo
 * - LaunchedEffect para efectos one-shot
 * - Proper keyboard management
 * - Accessibility compliance
 * - Material Design 3 components
 *
 * ⚡ EFECTOS LATERALES:
 * - SnackbarHost para mensajes
 * - Keyboard hiding automático
 * - Navegación automática después de éxito
 * - Error dismissal y recovery
 *
 * 🧠 ARQUITECTURA DEMOSTRADA:
 * - Separation of concerns perfecto
 * - Stateless UI components
 * - Reactive data flow
 * - Predictable state management
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onNavigateBack: () -> Unit = {},
    viewModel: ProfileViewModel = viewModel()
) {
    /**
     * 🎭 State observation - Observación reactiva del estado
     *
     * 📖 REACTIVE UI:
     * collectAsState() convierte el StateFlow en State observable por Compose.
     * La UI se recompone automáticamente cuando cambia el estado.
     *
     * 🧠 CONCEPTO CLAVE - DECLARATIVE UI:
     * No decimos "actualiza este campo", sino "muestra lo que está en el estado".
     * La UI es una función del estado: UI = f(State)
     */
    val uiState by viewModel.uiState.collectAsState()

    /**
     * 🍫 SnackbarHost state - Para mostrar mensajes
     *
     * 📖 SNACKBAR MANAGEMENT:
     * SnackbarHostState maneja la cola de snackbars y su lifecycle.
     * Se crea una vez y se reutiliza para todos los mensajes.
     */
    val snackbarHostState = remember { SnackbarHostState() }

    /**
     * ⌨️ Keyboard controller - Para ocultar teclado
     *
     * 📖 KEYBOARD MANAGEMENT:
     * LocalSoftwareKeyboardController permite ocultar el teclado
     * programáticamente desde efectos.
     */
    val keyboardController = LocalSoftwareKeyboardController.current

    /**
     * ⚡ Effects handling - Manejo de efectos laterales
     *
     * 📖 SIDE EFFECTS PATTERN:
     * LaunchedEffect se ejecuta cuando cambia la key (Unit = solo una vez).
     * Observa el flow de efectos y ejecuta acciones one-shot.
     *
     * 🧠 CONCEPTO CLAVE - ONE-SHOT EFFECTS:
     * Los efectos se consumen una vez y no se re-ejecutan en recomposiciones.
     * Esto es crucial para actions como snackbars y navegación.
     */
    LaunchedEffect(Unit) {
        viewModel.uiEffects.collect {
            when (it) {
                is ProfileUiEffect.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(
                        message = it.message,
                        actionLabel = it.actionLabel,
                        duration = it.duration
                    )
                }

                ProfileUiEffect.NavigateBack -> {
                    onNavigateBack()
                }

                ProfileUiEffect.HideKeyboard -> {
                    keyboardController?.hide()
                }

                is ProfileUiEffect.FocusField -> {
                    // En implementación real, enfocaríamos el campo específico
                    // focusRequesters[effect.fieldName]?.requestFocus()
                }
            }
        }
    }

    /**
     * 🎨 Main UI Structure - Estructura principal con Scaffold
     *
     * 📖 SCAFFOLD PATTERN:
     * Scaffold proporciona estructura estándar de Material Design.
     * SnackbarHost se coloca automáticamente en la posición correcta.
     */
    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { paddingValues ->
        /**
         * 🎪 State-based UI - UI basada en estados
         *
         * 📖 STATE-DRIVEN UI:
         * La UI completa se determina por el estado actual.
         * Cada estado tiene su representación visual específica.
         *
         * 🧠 CONCEPTO CLAVE - EXHAUSTIVE WHEN:
         * Cubrimos todos los estados posibles. Si agregamos un nuevo estado,
         * el compilador nos forzará a manejarlo aquí.
         */
        when (uiState) {
            ProfileUiState.Loading -> {
                ProfileLoadingContent(
                    modifier = Modifier.padding(paddingValues)
                )
            }

            is ProfileUiState.Idle -> {
                ProfileFormContent(
                    state = uiState as ProfileUiState.Idle,
                    onEvent = {
                        viewModel.handleEvent(it)
                    },
                    modifier = Modifier.padding(paddingValues)
                )
            }

            is ProfileUiState.Success -> {
                ProfileSuccessContent(
                    userName = (uiState as ProfileUiState.Success).userName,
                    onEvent = viewModel::handleEvent,
                    modifier = Modifier.padding(paddingValues)
                )
            }

            is ProfileUiState.Error -> {
                ProfileErrorContent(
                    error = uiState as ProfileUiState.Error,
                    onEvent = viewModel::handleEvent,
                    modifier = Modifier.padding(paddingValues)
                )
            }
        }
    }
}

// =====================================
// 🔄 LOADING CONTENT - Estado de carga
// =====================================

/**
 * ⏳ ProfileLoadingContent - UI durante carga/envío
 *
 * 📖 LOADING STATE UI:
 * Muestra spinner con mensaje mientras se procesa el formulario.
 * Bloquea interacción del usuario durante operaciones async.
 *
 * 🧠 CONCEPTO CLAVE - LOADING FEEDBACK:
 * El usuario debe saber que algo está pasando.
 * Loading states previenen múltiples envíos accidentales.
 */
@Composable
private fun ProfileLoadingContent(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(Dimensions.spaceMedium)
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(48.dp),
                color = MaterialTheme.colorScheme.primary
            )

            Text(
                text = "Guardando perfil...",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

// =====================================
// 📝 FORM CONTENT - Formulario principal
// =====================================

/**
 * 📋 ProfileFormContent - Formulario interactivo principal
 *
 * 📖 FORM DESIGN:
 * - OutlinedTextField para input elegante
 * - Validación visual en tiempo real
 * - Botones habilitados condicionalmente
 * - Scroll para pantallas pequeñas
 * - Spacing consistente
 *
 * 🧠 CONCEPTO CLAVE - CONTROLLED COMPONENTS:
 * Todos los campos son "controlled" - su valor viene del estado,
 * no manejan su propio estado interno.
 *
 * @param state Estado actual del formulario
 * @param onEvent Callback para enviar eventos al ViewModel
 */
@Composable
private fun ProfileFormContent(
    state: ProfileUiState.Idle,
    onEvent: (ProfileUiEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(Dimensions.spaceMedium)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(Dimensions.spaceMedium)
    ) {
        /**
         * 👤 Header Section - Sección de encabezado
         */
        ProfileHeader()

        /**
         * 📝 Form Fields Section - Campos del formulario
         */
        ProfileFormFields(
            state = state,
            onEvent = onEvent
        )

        /**
         * 🔘 Action Buttons Section - Botones de acción
         */
        ProfileActionButtons(
            state = state,
            onEvent = onEvent
        )
    }
}

/**
 * 👤 ProfileHeader - Encabezado del formulario
 *
 * 📖 HEADER DESIGN:
 * Icono grande + título + descripción para dar contexto.
 * Sigue patrones de Material Design para headers.
 */
@Composable
private fun ProfileHeader() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Dimensions.spaceSmall)
    ) {
        Icon(
            imageVector = Icons.Default.Person,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.primary
        )

        Text(
            text = "Configurar Perfil",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onSurface
        )

        Text(
            text = "Completa tu información personal",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

/**
 * 📝 ProfileFormFields - Campos del formulario
 *
 * 📖 FORM FIELDS DESIGN:
 * - Campo nombre con validación de longitud
 * - Campo email con validación de formato
 * - Error states visuales
 * - Labels y placeholders descriptivos
 * - Iconos para claridad
 *
 * 🧠 CONCEPTO CLAVE - REAL-TIME VALIDATION:
 * La validación ocurre mientras el usuario escribe,
 * proporcionando feedback inmediato.
 */
@Composable
private fun ProfileFormFields(
    state: ProfileUiState.Idle,
    onEvent: (ProfileUiEvent) -> Unit
) {
    /**
     * 👤 Name Field - Campo de nombre
     */
    OutlinedTextField(
        value = state.name,
        onValueChange = { newName ->
            onEvent(ProfileUiEvent.UpdateName(newName))
        },
        label = {
            Text("Nombre completo")
        },
        placeholder = {
            Text("Ej: María García")
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = null
            )
        },
        trailingIcon = if (state.name.isNotEmpty()) {
            {
                IconButton(
                    onClick = { onEvent(ProfileUiEvent.UpdateName("")) }
                ) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = "Limpiar nombre"
                    )
                }
            }
        } else null,
        isError = state.nameError != null,
        supportingText = state.nameError?.let { error ->
            {
                Text(
                    text = error,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error
                )
            }
        },
        singleLine = true,
        modifier = Modifier.fillMaxWidth()
    )

    /**
     * 📧 Email Field - Campo de email
     */
    OutlinedTextField(
        value = state.email,
        onValueChange = { newEmail ->
            onEvent(ProfileUiEvent.UpdateEmail(newEmail))
        },
        label = {
            Text("Email")
        },
        placeholder = {
            Text("Ej: maria@ejemplo.com")
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Person, // En app real: usar Email icon
                contentDescription = null
            )
        },
        trailingIcon = if (state.email.isNotEmpty()) {
            {
                IconButton(
                    onClick = { onEvent(ProfileUiEvent.UpdateEmail("")) }
                ) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = "Limpiar email"
                    )
                }
            }
        } else null,
        isError = state.emailError != null,
        supportingText = state.emailError?.let { error ->
            {
                Text(
                    text = error,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error
                )
            }
        },
        keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
            keyboardType = KeyboardType.Email
        ),
        singleLine = true,
        modifier = Modifier.fillMaxWidth()
    )
}

/**
 * 🔘 ProfileActionButtons - Botones de acción
 *
 * 📖 ACTION BUTTONS DESIGN:
 * - Botón primario para guardar (habilitado condicionalmente)
 * - Botón secundario para limpiar
 * - Estados visuales apropiados
 * - Spacing consistente
 *
 * 🧠 CONCEPTO CLAVE - CONDITIONAL ENABLING:
 * El botón guardar solo se habilita cuando el formulario es válido.
 * Esto guía al usuario y previene errores.
 */
@Composable
private fun ProfileActionButtons(
    state: ProfileUiState.Idle,
    onEvent: (ProfileUiEvent) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(Dimensions.spaceSmall)
    ) {
        /**
         * 💾 Save Button - Botón principal de guardar
         */
        Button(
            onClick = { onEvent(ProfileUiEvent.SubmitForm) },
            enabled = state.isFormValid,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Guardar Perfil",
                style = MaterialTheme.typography.labelLarge
            )
        }

        /**
         * 🗑️ Clear Button - Botón secundario de limpiar
         */
        OutlinedButton(
            onClick = { onEvent(ProfileUiEvent.ClearForm) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Limpiar Formulario",
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}

// =====================================
// ✅ SUCCESS CONTENT - Estado de éxito
// =====================================

/**
 * 🎉 ProfileSuccessContent - UI después de guardar exitosamente
 *
 * 📖 SUCCESS STATE UI:
 * Muestra confirmación visual de éxito con el nombre guardado.
 * Permite al usuario crear un nuevo perfil o salir.
 *
 * 🧠 CONCEPTO CLAVE - SUCCESS FEEDBACK:
 * El estado de éxito debe ser claro y satisfactorio.
 * Proporciona opciones para siguiente acción.
 */
@Composable
private fun ProfileSuccessContent(
    userName: String,
    onEvent: (ProfileUiEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(Dimensions.spaceMedium)
        ) {
            /**
             * ✅ Success Icon - Visual feedback de éxito
             */
            Card(
                modifier = Modifier.size(80.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "✅",
                        style = MaterialTheme.typography.headlineLarge
                    )
                }
            }

            /**
             * 📝 Success Message - Mensaje personalizado
             */
            Text(
                text = "¡Perfil Guardado!",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = "Hola, $userName",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )

            Text(
                text = "Tu perfil se ha guardado correctamente",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(Dimensions.spaceMedium))

            /**
             * 🔄 Action Button - Crear nuevo perfil
             */
            Button(
                onClick = { onEvent(ProfileUiEvent.ClearForm) }
            ) {
                Text("Crear Nuevo Perfil")
            }
        }
    }
}

// =====================================
// ❌ ERROR CONTENT - Estado de error
// =====================================

/**
 * 🚫 ProfileErrorContent - UI durante estado de error
 *
 * 📖 ERROR STATE UI:
 * Muestra error de forma clara con opciones de recuperación.
 * Permite reintentar o volver al formulario.
 *
 * 🧠 CONCEPTO CLAVE - ERROR RECOVERY:
 * Los errores deben ser recuperables cuando sea posible.
 * Proporciona acciones claras para resolver el problema.
 */
@Composable
private fun ProfileErrorContent(
    error: ProfileUiState.Error,
    onEvent: (ProfileUiEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(Dimensions.spaceMedium)
        ) {
            /**
             * ❌ Error Icon - Visual feedback de error
             */
            Card(
                modifier = Modifier.size(80.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                )
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "❌",
                        style = MaterialTheme.typography.headlineLarge
                    )
                }
            }

            /**
             * 📝 Error Message - Mensaje de error claro
             */
            Text(
                text = "Error al Guardar",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.error
            )

            Text(
                text = error.message,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(Dimensions.spaceMedium))

            /**
             * 🔄 Recovery Actions - Acciones de recuperación
             */
            Row(
                horizontalArrangement = Arrangement.spacedBy(Dimensions.spaceSmall)
            ) {
                OutlinedButton(
                    onClick = { onEvent(ProfileUiEvent.DismissError) }
                ) {
                    Text("Volver al Formulario")
                }

                Button(
                    onClick = { onEvent(ProfileUiEvent.SubmitForm) }
                ) {
                    Text("Reintentar")
                }
            }
        }
    }
}

// =====================================
// 🎨 PREVIEWS - Para desarrollo y testing
// =====================================

/**
 * 👀 Preview del formulario en estado idle
 */
@Preview(name = "Profile Form - Idle State")
@Composable
private fun ProfileFormPreview() {
    VideoconferenciaTheme {
        ProfileFormContent(
            state = ProfileUiState.Idle(
                name = "María García",
                email = "maria@ejemplo.com",
                nameError = null,
                emailError = null,
                isFormValid = true
            ),
            onEvent = {}
        )
    }
}

/**
 * 🚫 Preview del formulario con errores
 */
@Preview(name = "Profile Form - With Errors")
@Composable
private fun ProfileFormErrorPreview() {
    VideoconferenciaTheme {
        ProfileFormContent(
            state = ProfileUiState.Idle(
                name = "M",
                email = "invalid-email",
                nameError = "El nombre debe tener al menos 2 caracteres",
                emailError = "Formato de email inválido",
                isFormValid = false
            ),
            onEvent = {}
        )
    }
}

/**
 * ⏳ Preview del estado de carga
 */
@Preview(name = "Profile Loading State")
@Composable
private fun ProfileLoadingPreview() {
    VideoconferenciaTheme {
        ProfileLoadingContent()
    }
}

/**
 * ✅ Preview del estado de éxito
 */
@Preview(name = "Profile Success State")
@Composable
private fun ProfileSuccessPreview() {
    VideoconferenciaTheme {
        ProfileSuccessContent(
            userName = "María García",
            onEvent = {}
        )
    }
}

/**
 * ❌ Preview del estado de error
 */
@Preview(name = "Profile Error State")
@Composable
private fun ProfileErrorPreview() {
    VideoconferenciaTheme {
        ProfileErrorContent(
            error = ProfileUiState.Error("Error de red simulado"),
            onEvent = {}
        )
    }
}

/**
 * 🌙 Preview en modo oscuro
 */
@Preview(
    name = "Profile Form - Dark Mode",
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun ProfileFormDarkPreview() {
    VideoconferenciaTheme {
        ProfileFormContent(
            state = ProfileUiState.Idle(
                name = "María García",
                email = "maria@ejemplo.com",
                isFormValid = true
            ),
            onEvent = {}
        )
    }
}

// =====================================
// 🧠 CONCEPTOS PEDAGÓGICOS ADICIONALES
// =====================================

/**
 * 💡 PATRONES DE FORMULARIO DEMOSTRADOS:
 *
 * 🎯 VALIDATION PATTERNS:
 * - Real-time validation: Mientras el usuario escribe
 * - Field-level errors: Errores específicos por campo
 * - Form-level validation: Validación global para submit
 * - Visual error feedback: Error states en UI
 *
 * 🎭 STATE MANAGEMENT PATTERNS:
 * - Single source of truth: Todo el estado en ViewModel
 * - Immutable state updates: Copy en lugar de mutación
 * - Derived state: isFormValid calculado desde otros campos
 * - State hoisting: UI sin estado propio
 *
 * ⚡ EFFECT PATTERNS:
 * - One-shot effects: Snackbars, navegación, keyboard
 * - Effect consumption: Cada efecto se ejecuta una vez
 * - Effect separation: Diferentes efectos para diferentes propósitos
 * - UI effect handling: LaunchedEffect con collect
 */

/**
 * 🎨 UI/UX BEST PRACTICES DEMOSTRADAS:
 *
 * ✅ ACCESSIBILITY:
 * - Content descriptions en iconos
 * - Labels descriptivos en campos
 * - Error messages claros
 * - Touch targets apropiados
 *
 * 🎯 USABILITY:
 * - Feedback inmediato en validación
 * - Estados de carga claros
 * - Botones habilitados condicionalmente
 * - Mensajes de éxito/error claros
 *
 * 📱 RESPONSIVE DESIGN:
 * - Scroll vertical para pantallas pequeñas
 * - Spacing consistente con design system
 * - Botones de ancho completo para touch
 * - Preview en diferentes configuraciones
 *
 * 🎨 MATERIAL DESIGN 3:
 * - OutlinedTextField estándar
 * - Color scheme consistente
 * - Typography scale apropiada
 * - Card components para destacar contenido
 */