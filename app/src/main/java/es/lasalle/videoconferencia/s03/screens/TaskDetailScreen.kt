package es.lasalle.videoconferencia.s03.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.remember
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import es.lasalle.videoconferencia.s03.models.*
import es.lasalle.videoconferencia.s03.viewmodels.TaskDetailViewModel
import es.lasalle.videoconferencia.ui.theme.Dimensions
import es.lasalle.videoconferencia.ui.theme.VideoconferenciaTheme
import java.text.SimpleDateFormat
import java.util.*

// =====================================
// 📄 TASK DETAIL SCREEN - PANTALLA DE DETALLE CON PARÁMETROS
// =====================================

/**
 * 🎯 TaskDetailScreen - Demostración completa de pantalla de detalle con navegación parameterizada
 * 
 * 📖 CONCEPTOS EDUCATIVOS CUBIERTOS:
 * 
 * 🧭 NAVEGACIÓN CON PARÁMETROS:
 * - Recepción de parámetros de navegación (taskId)
 * - Carga automática basada en parámetros
 * - Manejo de IDs inválidos o no encontrados
 * - Navegación back inteligente
 * 
 * 📊 ESTADOS COMPLEJOS DE CARGA:
 * - Loading: Spinner mientras carga datos
 * - Success: Datos completos con todas las operaciones
 * - Error: Error recoverable con retry
 * - NotFound: Error específico con navegación automática
 * 
 * 🎛️ OPERACIONES SOBRE DATOS:
 * - Marcar como completada/pendiente
 * - Agregar/quitar etiquetas dinámicamente
 * - Cambiar prioridad
 * - Compartir con otras apps
 * - Refresh manual de datos
 * 
 * ⚡ EFECTOS LATERALES AVANZADOS:
 * - Snackbars con acciones (Deshacer)
 * - Navegación automática en errores
 * - Share intents del sistema
 * - Toast notifications para feedback
 * 
 * 🎨 UI PATTERNS AVANZADOS:
 * - Pull-to-refresh simulation
 * - Chip groups para tags
 * - Priority badges con colores
 * - Status indicators
 * - Action buttons contextuales
 * - Empty states y error states
 * 
 * 🧠 ARQUITECTURA DEMOSTRADA:
 * - Parameter injection en ViewModels
 * - Complex state management
 * - Multi-type error handling
 * - Optimistic UI updates
 * - External app integration
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskDetailScreen(
    taskId: String,
    onNavigateBack: () -> Unit = {},
    viewModel: TaskDetailViewModel = viewModel()
) {
    /**
     * 🎭 State observation - Observación del estado complejo
     * 
     * 📖 COMPLEX STATE OBSERVATION:
     * Observamos un StateFlow con 4 estados posibles.
     * Cada estado requiere UI completamente diferente.
     */
    val uiState by viewModel.uiState.collectAsState()
    
    /**
     * 🍫 SnackbarHost state - Para mensajes con acciones
     * 
     * 📖 ADVANCED SNACKBAR USAGE:
     * SnackbarHostState permite snackbars con botones de acción,
     * especialmente útil para operaciones "Deshacer".
     */
    val snackbarHostState = remember { SnackbarHostState() }
    
    /**
     * 🍞 Toast context - Para mensajes rápidos
     */
    val context = LocalContext.current

    /**
     * 🎬 Initialization effect - Carga automática inicial
     * 
     * 📖 PARAMETER-BASED INITIALIZATION:
     * En cuanto la pantalla se compone, cargamos datos
     * basándose en el taskId recibido como parámetro.
     * 
     * 🧠 CONCEPTO CLAVE - AUTOMATIC LOADING:
     * LaunchedEffect con taskId como key garantiza que
     * si el taskId cambia, se vuelve a cargar automáticamente.
     */
    LaunchedEffect(taskId) {
        viewModel.loadTask(taskId)
    }

    /**
     * ⚡ Effects handling - Manejo de efectos avanzados
     * 
     * 📖 COMPLEX EFFECTS HANDLING:
     * Manejamos múltiples tipos de efectos:
     * - Navegación con delay opcional
     * - Snackbars con acciones complejas
     * - Share intents del sistema
     * - Navegación a edit screens
     */
    LaunchedEffect(Unit) {
        viewModel.uiEffects.collect { effect ->
            when (effect) {
                is TaskDetailUiEffect.NavigateBack -> {
                    if (effect.withDelay) {
                        kotlinx.coroutines.delay(effect.delayMs)
                    }
                    onNavigateBack()
                }
                is TaskDetailUiEffect.ShowSnackbar -> {
                    val result = snackbarHostState.showSnackbar(
                        message = effect.message,
                        actionLabel = effect.actionLabel,
                        duration = androidx.compose.material3.SnackbarDuration.Long
                    )
                    
                    // Ejecutar acción si usuario hace click
                    if (result == SnackbarResult.ActionPerformed) {
                        effect.onActionClick?.invoke()
                    }
                }
                is TaskDetailUiEffect.ShareTask -> {
                    // En app real: crear share intent
                    android.widget.Toast.makeText(
                        context,
                        "Compartiendo: ${effect.taskTitle}",
                        android.widget.Toast.LENGTH_SHORT
                    ).show()
                }
                is TaskDetailUiEffect.EditTask -> {
                    // En app real: navegar a edit screen
                    android.widget.Toast.makeText(
                        context,
                        "Editando tarea: ${effect.taskId}",
                        android.widget.Toast.LENGTH_SHORT
                    ).show()
                }
                is TaskDetailUiEffect.ConfirmDelete -> {
                    // En app real: mostrar dialog de confirmación
                    android.widget.Toast.makeText(
                        context,
                        "Confirmar eliminar: ${effect.taskTitle}",
                        android.widget.Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    /**
     * 🎨 Main UI Structure - Estructura principal con Scaffold
     */
    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        floatingActionButton = {
            // Solo mostrar FAB si tenemos datos cargados
            if (uiState is TaskDetailUiState.Success) {
                TaskDetailFAB(
                    onEdit = viewModel::editTask,
                    onShare = viewModel::shareTask
                )
            }
        }
    ) { paddingValues ->
        /**
         * 🎪 State-based UI - UI basada en estados complejos
         * 
         * 📖 EXHAUSTIVE STATE HANDLING:
         * Cubrimos todos los estados posibles con UI específica.
         * Cada estado tiene necesidades completamente diferentes.
         */
        when (val currentState = uiState) {
            TaskDetailUiState.Loading -> {
                TaskDetailLoadingContent(
                    modifier = Modifier.padding(paddingValues)
                )
            }
            is TaskDetailUiState.Success -> {
                TaskDetailSuccessContent(
                    task = currentState.task,
                    onEvent = viewModel::handleEvent,
                    modifier = Modifier.padding(paddingValues)
                )
            }
            is TaskDetailUiState.Error -> {
                TaskDetailErrorContent(
                    error = currentState,
                    onRetry = { viewModel.loadTask(taskId) },
                    onNavigateBack = onNavigateBack,
                    modifier = Modifier.padding(paddingValues)
                )
            }
            is TaskDetailUiState.NotFound -> {
                TaskDetailNotFoundContent(
                    taskId = currentState.taskId,
                    onNavigateBack = onNavigateBack,
                    modifier = Modifier.padding(paddingValues)
                )
            }
        }
    }
}

// =====================================
// ⏳ LOADING CONTENT - Estado de carga
// =====================================

/**
 * ⏳ TaskDetailLoadingContent - UI durante carga de datos
 * 
 * 📖 DETAILED LOADING STATE:
 * Skeleton específico para vista de detalle con placeholders
 * que imitan la estructura final de la pantalla.
 */
@Composable
private fun TaskDetailLoadingContent(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(Dimensions.spaceMedium),
        verticalArrangement = Arrangement.spacedBy(Dimensions.spaceMedium)
    ) {
        /**
         * 📱 Header skeleton
         */
        Card {
            Column(
                modifier = Modifier.padding(Dimensions.spaceMedium),
                verticalArrangement = Arrangement.spacedBy(Dimensions.spaceSmall)
            ) {
                // Title skeleton
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .height(24.dp)
                        .background(
                            MaterialTheme.colorScheme.outline,
                            RoundedCornerShape(4.dp)
                        )
                )
                
                // Status skeleton
                Box(
                    modifier = Modifier
                        .width(120.dp)
                        .height(16.dp)
                        .background(
                            MaterialTheme.colorScheme.outline,
                            RoundedCornerShape(4.dp)
                        )
                )
            }
        }
        
        /**
         * 📝 Description skeleton
         */
        Card {
            Column(
                modifier = Modifier.padding(Dimensions.spaceMedium),
                verticalArrangement = Arrangement.spacedBy(Dimensions.spaceSmall)
            ) {
                repeat(3) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(16.dp)
                            .background(
                                MaterialTheme.colorScheme.outline,
                                RoundedCornerShape(4.dp)
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
                    modifier = Modifier.size(24.dp),
                    strokeWidth = 2.dp
                )
                Text(
                    text = "Cargando detalles de la tarea...",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

// =====================================
// ✅ SUCCESS CONTENT - Datos cargados exitosamente
// =====================================

/**
 * 📋 TaskDetailSuccessContent - Vista completa de la tarea
 * 
 * 📖 COMPREHENSIVE DETAIL VIEW:
 * Muestra todos los datos de la tarea con capacidad
 * de interacción completa para modificar datos.
 */
@Composable
private fun TaskDetailSuccessContent(
    task: TaskDetail,
    onEvent: (TaskDetailUiEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(Dimensions.spaceMedium),
        verticalArrangement = Arrangement.spacedBy(Dimensions.spaceMedium)
    ) {
        /**
         * 📱 Header Section - Información principal
         */
        TaskDetailHeader(task = task, onEvent = onEvent)
        
        /**
         * 📝 Description Section - Descripción completa
         */
        TaskDetailDescription(task = task)
        
        /**
         * 🏷️ Tags Section - Etiquetas editables
         */
        TaskDetailTags(task = task, onEvent = onEvent)
        
        /**
         * 📊 Metadata Section - Información adicional
         */
        TaskDetailMetadata(task = task)
        
        /**
         * 🔄 Actions Section - Acciones principales
         */
        TaskDetailActions(onEvent = onEvent)
        
        // Espacio para el FAB
        Spacer(modifier = Modifier.height(80.dp))
    }
}

/**
 * 📱 TaskDetailHeader - Encabezado con información principal
 */
@Composable
private fun TaskDetailHeader(
    task: TaskDetail,
    onEvent: (TaskDetailUiEvent) -> Unit
) {
    Card {
        Column(
            modifier = Modifier.padding(Dimensions.spaceMedium),
            verticalArrangement = Arrangement.spacedBy(Dimensions.spaceSmall)
        ) {
            /**
             * 📋 Title and Status Row
             */
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = task.title,
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                
                TaskStatusChip(
                    isCompleted = task.isCompleted,
                    onToggle = { onEvent(TaskDetailUiEvent.MarkAsComplete(!task.isCompleted)) }
                )
            }
            
            /**
             * 🚨 Priority Badge
             */
            TaskPriorityBadge(
                priority = task.priority,
                onClick = {
                    // En app real: mostrar dropdown de prioridades
                    val newPriority = when (task.priority) {
                        TaskPriority.LOW -> TaskPriority.MEDIUM
                        TaskPriority.MEDIUM -> TaskPriority.HIGH
                        TaskPriority.HIGH -> TaskPriority.URGENT
                        TaskPriority.URGENT -> TaskPriority.LOW
                    }
                    onEvent(TaskDetailUiEvent.ChangePriority(newPriority))
                }
            )
        }
    }
}

/**
 * 📝 TaskDetailDescription - Descripción de la tarea
 */
@Composable
private fun TaskDetailDescription(task: TaskDetail) {
    Card {
        Column(
            modifier = Modifier.padding(Dimensions.spaceMedium),
            verticalArrangement = Arrangement.spacedBy(Dimensions.spaceSmall)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Dimensions.spaceSmall)
            ) {
                Icon(
                    imageVector = Icons.Default.Description,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Descripción",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            
            Text(
                text = task.description,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

/**
 * 🏷️ TaskDetailTags - Etiquetas con capacidad de edición
 */
@Composable
private fun TaskDetailTags(
    task: TaskDetail,
    onEvent: (TaskDetailUiEvent) -> Unit
) {
    var showAddTagDialog by remember { mutableStateOf(false) }
    
    Card {
        Column(
            modifier = Modifier.padding(Dimensions.spaceMedium),
            verticalArrangement = Arrangement.spacedBy(Dimensions.spaceSmall)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(Dimensions.spaceSmall)
                ) {
                    Icon(
                        imageVector = Icons.Default.Tag,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "Etiquetas",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                
                IconButton(
                    onClick = { showAddTagDialog = true }
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Agregar etiqueta"
                    )
                }
            }
            
            if (task.tags.isEmpty()) {
                Text(
                    text = "Sin etiquetas",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                // Tags chips con capacidad de eliminar
                Column(
                    verticalArrangement = Arrangement.spacedBy(Dimensions.spaceSmall)
                ) {
                    task.tags.chunked(3).forEach { rowTags ->
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(Dimensions.spaceSmall)
                        ) {
                            rowTags.forEach { tag ->
                                InputChip(
                                    selected = false,
                                    onClick = {
                                        onEvent(TaskDetailUiEvent.RemoveTag(tag))
                                    },
                                    label = { Text(tag) },
                                    trailingIcon = {
                                        Icon(
                                            imageVector = Icons.Default.Close,
                                            contentDescription = "Eliminar $tag",
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
    
    // Dialog para agregar tag (simplificado)
    if (showAddTagDialog) {
        var newTag by remember { mutableStateOf("") }
        
        AlertDialog(
            onDismissRequest = { showAddTagDialog = false },
            title = { Text("Agregar Etiqueta") },
            text = {
                OutlinedTextField(
                    value = newTag,
                    onValueChange = { newTag = it },
                    label = { Text("Nueva etiqueta") },
                    singleLine = true
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (newTag.isNotBlank()) {
                            onEvent(TaskDetailUiEvent.AddTag(newTag))
                        }
                        showAddTagDialog = false
                        newTag = ""
                    }
                ) {
                    Text("Agregar")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { 
                        showAddTagDialog = false
                        newTag = ""
                    }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }
}

/**
 * 📊 TaskDetailMetadata - Información adicional
 */
@Composable
private fun TaskDetailMetadata(task: TaskDetail) {
    Card {
        Column(
            modifier = Modifier.padding(Dimensions.spaceMedium),
            verticalArrangement = Arrangement.spacedBy(Dimensions.spaceSmall)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Dimensions.spaceSmall)
            ) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Información",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            
            val dateFormat = SimpleDateFormat("dd/MM/yyyy 'a las' HH:mm", Locale.getDefault())
            val createdDate = dateFormat.format(Date(task.createdAt))
            
            Text(
                text = "Creada: $createdDate",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            Text(
                text = "ID: ${task.id}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.outline
            )
        }
    }
}

/**
 * 🔄 TaskDetailActions - Acciones principales
 */
@Composable
private fun TaskDetailActions(onEvent: (TaskDetailUiEvent) -> Unit) {
    Card {
        Column(
            modifier = Modifier.padding(Dimensions.spaceMedium),
            verticalArrangement = Arrangement.spacedBy(Dimensions.spaceSmall)
        ) {
            Text(
                text = "Acciones",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            
            Button(
                onClick = { onEvent(TaskDetailUiEvent.RefreshData) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(Dimensions.spaceSmall))
                Text("Actualizar Datos")
            }
        }
    }
}

// =====================================
// 🎨 UI COMPONENTS - Componentes de UI específicos
// =====================================

/**
 * ✅ TaskStatusChip - Chip de estado con toggle
 */
@Composable
private fun TaskStatusChip(
    isCompleted: Boolean,
    onToggle: () -> Unit
) {
    FilterChip(
        selected = isCompleted,
        onClick = onToggle,
        label = {
            Text(
                text = if (isCompleted) "Completada" else "Pendiente",
                style = MaterialTheme.typography.labelMedium
            )
        },
        leadingIcon = {
            Icon(
                imageVector = if (isCompleted) Icons.Default.CheckCircle else Icons.Default.Schedule,
                contentDescription = null,
                modifier = Modifier.size(16.dp)
            )
        },
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
            selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
        )
    )
}

/**
 * 🚨 TaskPriorityBadge - Badge de prioridad con colores
 */
@Composable
private fun TaskPriorityBadge(
    priority: TaskPriority,
    onClick: () -> Unit
) {
    val (color, icon, text) = when (priority) {
        TaskPriority.LOW -> Triple(
            MaterialTheme.colorScheme.secondary,
            Icons.Default.KeyboardArrowDown,
            "Baja"
        )
        TaskPriority.MEDIUM -> Triple(
            MaterialTheme.colorScheme.primary,
            Icons.Default.Remove,
            "Media"
        )
        TaskPriority.HIGH -> Triple(
            MaterialTheme.colorScheme.tertiary,
            Icons.Default.KeyboardArrowUp,
            "Alta"
        )
        TaskPriority.URGENT -> Triple(
            MaterialTheme.colorScheme.error,
            Icons.Default.PriorityHigh,
            "Urgente"
        )
    }
    
    AssistChip(
        onClick = onClick,
        label = {
            Text(
                text = "Prioridad: $text",
                style = MaterialTheme.typography.labelMedium
            )
        },
        leadingIcon = {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(16.dp),
                tint = color
            )
        }
    )
}

/**
 * 🎯 TaskDetailFAB - Floating Action Button con acciones
 */
@Composable
private fun TaskDetailFAB(
    onEdit: () -> Unit,
    onShare: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    
    Column {
        // Expanded FABs
        if (expanded) {
            FloatingActionButton(
                onClick = {
                    onShare()
                    expanded = false
                },
                modifier = Modifier.padding(bottom = Dimensions.spaceSmall),
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            ) {
                Icon(
                    imageVector = Icons.Default.Share,
                    contentDescription = "Compartir tarea"
                )
            }
            
            FloatingActionButton(
                onClick = {
                    onEdit()
                    expanded = false
                },
                modifier = Modifier.padding(bottom = Dimensions.spaceSmall),
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Editar tarea"
                )
            }
        }
        
        // Main FAB
        FloatingActionButton(
            onClick = { expanded = !expanded }
        ) {
            Icon(
                imageVector = if (expanded) Icons.Default.Close else Icons.Default.MoreVert,
                contentDescription = if (expanded) "Cerrar opciones" else "Más opciones"
            )
        }
    }
}

// =====================================
// ❌ ERROR CONTENT - Estados de error
// =====================================

/**
 * 🚫 TaskDetailErrorContent - UI para errores generales
 */
@Composable
private fun TaskDetailErrorContent(
    error: TaskDetailUiState.Error,
    onRetry: () -> Unit,
    onNavigateBack: () -> Unit,
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
                    Icon(
                        imageVector = Icons.Default.Error,
                        contentDescription = null,
                        modifier = Modifier.size(40.dp),
                        tint = MaterialTheme.colorScheme.onErrorContainer
                    )
                }
            }
            
            Text(
                text = "Error al Cargar",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.error
            )
            
            Text(
                text = error.userMessage,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            Text(
                text = "Detalles: ${error.message}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.outline
            )
            
            Spacer(modifier = Modifier.height(Dimensions.spaceMedium))
            
            Row(
                horizontalArrangement = Arrangement.spacedBy(Dimensions.spaceSmall)
            ) {
                OutlinedButton(onClick = onNavigateBack) {
                    Text("Volver")
                }
                
                Button(onClick = onRetry) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(Dimensions.spaceSmall))
                    Text("Reintentar")
                }
            }
        }
    }
}

/**
 * 🔍 TaskDetailNotFoundContent - UI para tarea no encontrada
 */
@Composable
private fun TaskDetailNotFoundContent(
    taskId: String,
    onNavigateBack: () -> Unit,
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
            Card(
                modifier = Modifier.size(80.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                        modifier = Modifier.size(40.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            
            Text(
                text = "Tarea No Encontrada",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurface
            )
            
            Text(
                text = "La tarea con ID '$taskId' no existe o ha sido eliminada.",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            Text(
                text = "Navegando automáticamente...",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.outline
            )
            
            Spacer(modifier = Modifier.height(Dimensions.spaceMedium))
            
            Button(onClick = onNavigateBack) {
                Text("Volver Ahora")
            }
        }
    }
}

// =====================================
// 🎨 PREVIEWS - Para desarrollo y testing
// =====================================

/**
 * ⏳ Preview del estado de carga
 */
@Preview(name = "Task Detail Loading")
@Composable
private fun TaskDetailLoadingPreview() {
    VideoconferenciaTheme {
        TaskDetailLoadingContent()
    }
}

/**
 * ✅ Preview con tarea completada
 */
@Preview(name = "Task Detail Success - Completed")
@Composable
private fun TaskDetailSuccessCompletedPreview() {
    VideoconferenciaTheme {
        TaskDetailSuccessContent(
            task = TaskDetail(
                id = "1",
                title = "Implementar login con OAuth",
                description = "Configurar autenticación OAuth2 con Google y GitHub para permitir a los usuarios hacer login de forma segura.",
                isCompleted = true,
                priority = TaskPriority.HIGH,
                tags = listOf("auth", "oauth", "security"),
                createdAt = System.currentTimeMillis() - 86400000
            ),
            onEvent = {}
        )
    }
}

/**
 * ⏳ Preview con tarea pendiente
 */
@Preview(name = "Task Detail Success - Pending")
@Composable
private fun TaskDetailSuccessPendingPreview() {
    VideoconferenciaTheme {
        TaskDetailSuccessContent(
            task = TaskDetail(
                id = "3",
                title = "Optimizar performance de la app",
                description = "Identificar y corregir bottlenecks de performance, especialmente en listas largas y navegación entre pantallas.",
                isCompleted = false,
                priority = TaskPriority.URGENT,
                tags = listOf("performance", "optimization", "profiling"),
                createdAt = System.currentTimeMillis() - 259200000
            ),
            onEvent = {}
        )
    }
}

/**
 * ❌ Preview del estado de error
 */
@Preview(name = "Task Detail Error")
@Composable
private fun TaskDetailErrorPreview() {
    VideoconferenciaTheme {
        TaskDetailErrorContent(
            error = TaskDetailUiState.Error(
                message = "Network timeout",
                userMessage = "No se pudo cargar la tarea. Verifica tu conexión."
            ),
            onRetry = {},
            onNavigateBack = {}
        )
    }
}

/**
 * 🔍 Preview del estado no encontrado
 */
@Preview(name = "Task Detail Not Found")
@Composable
private fun TaskDetailNotFoundPreview() {
    VideoconferenciaTheme {
        TaskDetailNotFoundContent(
            taskId = "invalid-id-123",
            onNavigateBack = {}
        )
    }
}

/**
 * 🌙 Preview en modo oscuro
 */
@Preview(
    name = "Task Detail Success - Dark Mode",
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun TaskDetailSuccessDarkPreview() {
    VideoconferenciaTheme {
        TaskDetailSuccessContent(
            task = TaskDetail(
                id = "2",
                title = "Diseñar pantalla de perfil",
                description = "Crear wireframes y mockups para la pantalla de perfil de usuario, incluyendo foto, datos personales y configuraciones.",
                isCompleted = false,
                priority = TaskPriority.MEDIUM,
                tags = listOf("design", "ui", "profile"),
                createdAt = System.currentTimeMillis() - 172800000
            ),
            onEvent = {}
        )
    }
}

// =====================================
// 🧠 CONCEPTOS PEDAGÓGICOS ADICIONALES
// =====================================

/**
 * 💡 PATRONES DE DETAIL SCREEN DEMOSTRADOS:
 * 
 * 🧭 NAVIGATION PATTERNS:
 * - Parameter-based initialization: LaunchedEffect(taskId)
 * - Automatic navigation on errors: NavigateBack with delay
 * - Back navigation control: ViewModel-managed navigation
 * - Deep linking support: taskId parameter handling
 * 
 * 📊 COMPLEX STATE MANAGEMENT:
 * - Loading state with realistic skeletons
 * - Success state with full interaction capability
 * - Granular error states (Error vs NotFound)
 * - State-specific UI components and actions
 * 
 * 🎛️ DATA MANIPULATION PATTERNS:
 * - Optimistic updates: Immediate UI changes
 * - Undo functionality: Snackbar actions
 * - Collection operations: Add/remove tags
 * - Enum updates: Priority changes
 * - Refresh patterns: Manual data reload
 * 
 * ⚡ ADVANCED EFFECT PATTERNS:
 * - Conditional navigation: NavigateBack with delay
 * - Interactive snackbars: Actions with callbacks
 * - System integration: Share intents
 * - Multiple effect types: Toast, navigation, dialogs
 */

/**
 * 🎨 UI/UX PATTERNS DEMOSTRADOS:
 * 
 * 🎯 INTERACTION PATTERNS:
 * - Expandable FAB: Multiple actions in one button
 * - Editable chips: Tags with remove capability
 * - Toggleable status: Tap to change completion
 * - Priority cycling: Tap to change priority
 * - Pull-to-refresh simulation: Manual refresh button
 * 
 * 📱 MOBILE UX PATTERNS:
 * - Card-based layout: Grouped information
 * - Contextual actions: FAB with relevant operations
 * - Status indicators: Visual feedback for states
 * - Progressive disclosure: Expandable sections
 * - Touch-friendly targets: Adequate button sizes
 * 
 * 🧠 ACCESSIBILITY PATTERNS:
 * - Content descriptions: All interactive elements
 * - Semantic structure: Proper heading hierarchy
 * - Color independence: Icons + text for states
 * - Touch target sizing: 48dp minimum targets
 * - Screen reader support: Meaningful descriptions
 */

/**
 * 🧪 TESTING STRATEGIES FOR DETAIL SCREENS:
 * 
 * ✅ STATE TESTING:
 * ```kotlin
 * @Test
 * fun `detail screen should show loading state initially`() {
 *     composeTestRule.setContent {
 *         TaskDetailScreen(taskId = "1")
 *     }
 *     
 *     composeTestRule.onNode(hasText("Cargando detalles")).assertIsDisplayed()
 * }
 * 
 * @Test
 * fun `detail screen should show task data when loaded`() {
 *     // Given: ViewModel with loaded task
 *     composeTestRule.setContent {
 *         TaskDetailScreen(taskId = "1", viewModel = mockViewModel)
 *     }
 *     
 *     composeTestRule.onNodeWithText("Implementar login").assertIsDisplayed()
 *     composeTestRule.onNodeWithText("OAuth2").assertIsDisplayed()
 * }
 * ```
 * 
 * 🎛️ INTERACTION TESTING:
 * ```kotlin
 * @Test
 * fun `clicking status chip should toggle completion`() {
 *     val events = mutableListOf<TaskDetailUiEvent>()
 *     
 *     composeTestRule.setContent {
 *         TaskDetailSuccessContent(
 *             task = sampleTask.copy(isCompleted = false),
 *             onEvent = { events.add(it) }
 *         )
 *     }
 *     
 *     composeTestRule.onNodeWithText("Pendiente").performClick()
 *     
 *     assertTrue(events.any { it is TaskDetailUiEvent.MarkAsComplete })
 * }
 * ```
 * 
 * ⚡ EFFECTS TESTING:
 * ```kotlin
 * @Test
 * fun `error state should trigger navigation effect`() = runTest {
 *     val viewModel = TaskDetailViewModel()
 *     
 *     viewModel.loadTask("invalid-id")
 *     advanceUntilIdle()
 *     
 *     val effect = viewModel.uiEffects.first()
 *     assertTrue(effect is TaskDetailUiEffect.NavigateBack)
 * }
 * ```
 */