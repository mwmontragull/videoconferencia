package es.lasalle.videoconferencia.s02

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Accessibility
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.House
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Task
import androidx.compose.material.icons.filled.LinearScale
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material3.*
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.*
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.hideFromAccessibility
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.semantics.toggleableState
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import es.lasalle.videoconferencia.ui.theme.CustomShapes
import es.lasalle.videoconferencia.ui.theme.Dimensions
import es.lasalle.videoconferencia.ui.theme.VideoconferenciaTheme

/**
 * 🎯Layout Playground
 *
 * 📖 QUÉ HACE:
 * Demuestra los conceptos fundamentales de layout en Compose:
 * - Row: disposición horizontal con weight
 * - Box: superposición con alignment
 * - Column: disposición vertical con spacing
 *
 * 🧠 CONCEPTOS CLAVE:
 * - Weight (peso): distribuye espacio proporcional (1f = 1 parte, 2f = 2 partes)
 * - Alignment: posiciona elementos dentro de contenedores
 * - Modifier order: el orden importa (clip → background → padding → clickable)
 *
 * 💡 PATRÓN DE DISEÑO:
 * - Container (Column) con elementos hijo que demuestran diferentes layouts
 * - Uso de Dimensions para espaciado consistente
 * - Semantic headings para accesibilidad
 */
@Composable
fun LayoutPlayground() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(Dimensions.spaceMedium),
        verticalArrangement = Arrangement.spacedBy(Dimensions.spaceMedium),
    ) {
        Text(
            text = "Layout Playground",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.semantics { heading() }
        )

        // Row with weight demonstration
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp),
            horizontalArrangement = Arrangement.spacedBy(Dimensions.spaceSmall)
        ) {
            DemoChip(
                text = "1x",
                modifier = Modifier.weight(1f)
            )
            DemoChip(
                text = "2x",
                modifier = Modifier.weight(2f)
            )
            DemoChip(
                text = "1x",
                modifier = Modifier.weight(1f)
            )
        }

        // Box with alignment demonstration
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .background(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = CustomShapes.card
                )
        ) {
            DemoChip(
                text = "Top Start",
                modifier = Modifier.align(Alignment.TopStart)
            )
            DemoChip(
                text = "Center",
                modifier = Modifier.align(Alignment.Center)
            )
            DemoChip(
                text = "Bottom End",
                modifier = Modifier.align(Alignment.BottomEnd)
            )
        }
    }
}

/**
 * 🎯 DemoChip
 *
 * 📖 QUÉ HACE:
 * Chip personalizado que demuestra el orden crítico de modifiers
 *
 * 🧠 CONCEPTO CLAVE - ORDEN DE MODIFIERS:
 * 1. clip() - PRIMERO: define la forma
 * 2. background() - SEGUNDO: aplica color dentro de la forma
 * 3. padding() - TERCERO: añade espacio interno
 * 4. clickable() - ÚLTIMO: área de click incluye padding
 *
 * ⚠️ IMPORTANTE:
 * Si cambias el orden, el resultado visual cambia!
 * Ejemplo: background → clip = esquinas cuadradas
 *
 * 💡 PATRÓN:
 * - Text como contenido base
 * - Modifier chain que construye la apariencia final
 * - Uso de theme colors y shapes
 */
@Composable
fun DemoChip(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        modifier = modifier
            .clip(CustomShapes.chip)
            .background(MaterialTheme.colorScheme.primary)
            .padding(
                horizontal = Dimensions.spaceSmall + 4.dp,
                vertical = Dimensions.spaceXSmall + 2.dp
            )
            .clickable { },
        color = MaterialTheme.colorScheme.onPrimary,
        style = MaterialTheme.typography.labelMedium
    )
}

/**
 * 🎯 Counter Demo
 *
 * 📖 QUÉ HACE:
 * Demuestra manejo básico de estado con contador simple
 *
 * 🧠 CONCEPTO CLAVE - STATE MANAGEMENT:
 * - Recibe state como parámetro (state hoisting)
 * - Emite eventos hacia arriba (onValueChange)
 * - No maneja state internamente (stateless)
 *
 * 🔄 PATRÓN "DATA DOWN, EVENTS UP":
 * - quantity1: datos fluyen hacia abajo
 * - onValueChange: eventos fluyen hacia arriba
 *
 * 💡 USO EN COMBINACIÓN:
 * Se usa junto con rememberSaveable en el padre
 * para demostrar persistencia de estado
 */
@Composable
fun CounterDemo(
    quantity1: Int,
    onValueChange: (Int) -> Unit,
) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Dimensions.spaceSmall)
    ) {
        Text(
            text = "Count: $quantity1",
            style = MaterialTheme.typography.headlineSmall
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(Dimensions.spaceSmall)
        ) {
            Button(
                modifier = Modifier.testTag("decrease_button"),
                onClick = { onValueChange(quantity1 - 1) }) {
                Text(
                    text = "-",
                )
            }
            Button(
                modifier = Modifier.testTag("increase_button"),
                onClick = { onValueChange(quantity1 + 1) }) {
                Text(
                    text = "+",
                )
            }
        }
    }
}

/**
 * 🎯 Stepper (Stateless)
 *
 * 📖 QUÉ HACE:
 * Componente stepper completamente stateless para incrementar/decrementar valores
 *
 * 🧠 CONCEPTO CLAVE - STATELESS COMPONENT:
 * - No tiene estado interno (no usa remember)
 * - Recibe valor y callback como parámetros
 * - Completamente controlado por el padre
 * - Fácil de testear y reutilizar
 *
 * ♿ ACCESIBILIDAD:
 * - Touch targets mínimos de 48dp
 * - Content descriptions descriptivos
 * - iconos semánticamente apropiados
 *
 * 💡 PATRÓN:
 * - Row horizontal con elementos centrados
 * - IconButtons con sizing accesible
 * - Text central con width mínimo para estabilidad
 */
@Composable
fun Stepper(
    value: Int,
    onValueChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {


    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = { onValueChange(value - 1) },
            modifier = Modifier.sizeIn(minWidth = 48.dp, minHeight = 48.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Decrease"
            )
        }
        Text(
            text = value.toString(),
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.widthIn(min = 40.dp)
        )
        IconButton(
            onClick = { onValueChange(value + 1) },
            modifier = Modifier.sizeIn(minWidth = 48.dp, minHeight = 48.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Increase"
            )
        }
    }
}

/**
 * 🎯 Stepper Demo
 *
 * 📖 QUÉ HACE:
 * Demuestra cómo usar el componente Stepper stateless
 *
 * 🧠 CONCEPTO CLAVE - STATE HOISTING EN ACCIÓN:
 * - Actúa como "puente" entre estado del padre y Stepper
 * - Pasa estado hacia abajo al Stepper
 * - Reenvía eventos hacia arriba al padre
 *
 * 🔄 FLUJO DE DATOS:
 * 1. Padre (App) tiene rememberSaveable
 * 2. StepperDemo recibe state y callback
 * 3. Stepper recibe state y callback
 * 4. User hace click → evento sube hasta Padre
 */
@Composable
fun StepperDemo(quantity1: Int, onValueChange: (Int) -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Quantity:",
            style = MaterialTheme.typography.titleMedium
        )
        Stepper(
            value = quantity1,
            onValueChange = onValueChange
        )
    }
}

/**
 * 📊 DATA CLASS: Task
 *
 * 📖 QUÉ REPRESENTA:
 * Modelo de datos simple para las tareas de la demo
 *
 * 🧠 CONCEPTOS CLAVE:
 * - Data class: autogenera equals(), hashCode(), toString()
 * - Immutable: val properties (no se pueden cambiar)
 * - Simple structure: solo los datos esenciales
 *
 * 📝 FIELDS:
 * - id: identificador único para LazyColumn keys
 * - title: texto a mostrar en la UI
 */
data class Task(val id: Int, val title: String)

/**
 * 🎯 Task Card
 *
 * 📖 QUÉ HACE:
 * Card component que muestra una tarea con acciones (compartir/eliminar)
 *
 * 🧠 CONCEPTOS CLAVE - MATERIAL 3:
 * - Card: contenedor con elevación y shape
 * - Row: layout horizontal con SpaceBetween
 * - IconButton: botones accesibles con touch targets
 * - Weight: el texto ocupa espacio disponible
 *
 * ♿ ACCESIBILIDAD:
 * - ContentDescription en todos los iconos
 * - Touch targets mínimos de 48dp
 * - Textos descriptivos (no genéricos como "button")
 *
 * 💡 PATRÓN DE EVENTOS:
 * - onRemove: callback para eliminar
 * - onShare: callback para compartir
 * - Stateless: no maneja lógica, solo UI
 */
@Composable
fun TaskCard(
    title: String,
    onRemove: () -> Unit,
    onShare: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.weight(1f)
            )
            Row {
                IconButton(
                    onClick = onShare,
                    modifier = Modifier.sizeIn(minWidth = 48.dp, minHeight = 48.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "Share task"
                    )
                }
                IconButton(
                    onClick = onRemove,
                    modifier = Modifier.sizeIn(minWidth = 48.dp, minHeight = 48.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete task"
                    )
                }
            }
        }
    }
}

/**
 * 🎯 Tasks Screen
 *
 * 📖 QUÉ HACE:
 * Pantalla completa que demuestra Scaffold + LazyColumn + State management
 *
 * 🧠 CONCEPTOS CLAVE - SCAFFOLD:
 * - TopAppBar: barra superior con título
 * - FloatingActionButton: acción principal (agregar)
 * - SnackbarHost: mensajes temporales
 * - Content area: área principal con padding automático
 *
 * 📝 ESTADO COMPLEJO:
 * - Lista de tareas (rememberSaveable)
 * - Contador de ID (mutableIntStateOf)
 * - Título compartido (mutable state)
 * - SnackbarHostState (para mostrar mensajes)
 *
 * 🔄 SIDE EFFECTS:
 * - LaunchedEffect: maneja snackbar cuando cambia sharedTaskTitle
 * - Cleanup automático: sharedTaskTitle = null
 *
 * 📊 LAZYCOLUMN PERFORMANCE:
 * - key = { it.id }: ayuda a Compose trackear items
 * - Solo renderiza items visibles
 * - contentPadding: espaciado interno
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TasksScreen() {
    var tasks by rememberSaveable { mutableStateOf(listOf<Task>()) }
    var nextId by rememberSaveable { mutableIntStateOf(1) }
    var sharedTaskTitle by remember { mutableStateOf<String?>(null) }
    val snackbarHostState = remember { SnackbarHostState() }

    // Handle snackbar for shared tasks
    LaunchedEffect(sharedTaskTitle) {
        sharedTaskTitle?.let { title ->
            snackbarHostState.showSnackbar("Shared: $title")
            sharedTaskTitle = null
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Tasks",
                        modifier = Modifier.semantics { heading() }
                    )
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    tasks = tasks + Task(nextId, "Task $nextId")
                    nextId++
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add task"
                )
            }
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            items(tasks, key = { it.id }) { task ->
                TaskCard(
                    title = task.title,
                    onRemove = {
                        tasks = tasks.filter { it.id != task.id }
                    },
                    onShare = {
                        sharedTaskTitle = task.title
                    }
                )
            }
        }
    }
}

/**
 * 🎯 Theming Showcase
 *
 * 📖 QUÉ HACE:
 * Muestra todo el sistema de theming personalizado de La Salle
 *
 * 🧠 CONCEPTOS CLAVE - MATERIAL 3 THEMING:
 * - Typography Scale: jerarquía completa de estilos de texto
 * - Color Palette: primary, secondary, tertiary + variants
 * - Shape System: diferentes corner radius para componentes
 * - Component Theming: cómo los componentes heredan el theme
 *
 * 🎨 SISTEMA DE COLORES:
 * - Semantic roles: primary (acción), secondary (apoyo), tertiary (acento)
 * - Automatic contrast: onPrimary, onSecondary garantizan legibilidad
 * - Dark mode support: colores adaptativos automáticamente
 *
 * 🔤 TYPOGRAPHY SCALE:
 * - Display: headlines grandes
 * - Headline: títulos importantes
 * - Title: subtitles y headers de sección
 * - Body: contenido regular
 * - Label: botones, inputs, captions
 *
 * 💡 SHAPES:
 * - Consistencia visual: mismo corner radius para componentes similares
 * - CustomShapes object: shapes específicos para casos especiales
 * - Automatic inheritance: MaterialTheme.shapes se aplica automáticamente
 */
@Composable
fun ThemingShowcase() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(Dimensions.spaceMedium),
        verticalArrangement = Arrangement.spacedBy(Dimensions.spaceMedium)
    ) {
        item {
            Text(
                text = "La Salle Theme System",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.semantics { heading() }
            )
        }

        item {
            Text(
                text = "Typography Scale",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.semantics { heading() }
            )
            Column(verticalArrangement = Arrangement.spacedBy(Dimensions.spaceSmall)) {
                Text("Display Large", style = MaterialTheme.typography.displayLarge)
                Text("Headline Medium", style = MaterialTheme.typography.headlineMedium)
                Text("Title Large", style = MaterialTheme.typography.titleLarge)
                Text("Body Large", style = MaterialTheme.typography.bodyLarge)
                Text("Label Medium", style = MaterialTheme.typography.labelMedium)
            }
        }

        item {
            Text(
                text = "Color Palette",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.semantics { heading() }
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(Dimensions.spaceSmall)
            ) {
                ColorSwatch(
                    "Primary",
                    MaterialTheme.colorScheme.primary,
                    MaterialTheme.colorScheme.onPrimary
                )
                ColorSwatch(
                    "Secondary",
                    MaterialTheme.colorScheme.secondary,
                    MaterialTheme.colorScheme.onSecondary
                )
                ColorSwatch(
                    "Tertiary",
                    MaterialTheme.colorScheme.tertiary,
                    MaterialTheme.colorScheme.onTertiary
                )
            }
        }

        item {
            Text(
                text = "Component Shapes",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.semantics { heading() }
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(Dimensions.spaceSmall)
            ) {
                ShapeDemo("Button", CustomShapes.button)
                ShapeDemo("Card", CustomShapes.card)
                ShapeDemo("Chip", CustomShapes.chip)
            }
        }

        item {
            Text(
                text = "Interactive Components",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.semantics { heading() }
            )
            Column(verticalArrangement = Arrangement.spacedBy(Dimensions.spaceSmall)) {
                Button(
                    onClick = { }
                    // No explicit shape - inherits from MaterialTheme.shapes.small
                ) {
                    Text("Auto-Themed Button")
                }
                Card(
                    modifier = Modifier.fillMaxWidth()
                    // No explicit shape - inherits from MaterialTheme.shapes.medium
                ) {
                    Text(
                        "This card uses automatic theming from MaterialTheme.shapes",
                        modifier = Modifier.padding(Dimensions.spaceMedium),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                // Show explicit override example for comparison
                Text(
                    "Explicit Override Examples:",
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier.padding(top = Dimensions.spaceSmall)
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(Dimensions.spaceSmall)
                ) {
                    Button(
                        onClick = { },
                        shape = CustomShapes.chip, // Explicitly overridden
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Chip Shape")
                    }

                    Button(
                        onClick = { },
                        shape = CustomShapes.floatingActionButton, // Explicitly overridden
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("FAB Shape")
                    }
                }
            }
        }
    }
}

/**
 * 🎨 COLOR SWATCH - Muestra visual de un color del theme
 *
 * 📖 QUÉ HACE:
 * Componente de demostración que visualiza un color con su nombre
 *
 * 🧠 CONCEPTOS CLAVE - COLOR PREVIEW:
 * - Box coloreado: Muestra el color de forma visual
 * - Text descriptivo: Etiqueta clara del rol del color
 * - Layout compacto: Column con width fijo para grid layouts
 *
 * 🎯 PARÁMETROS:
 * - name: Etiqueta descriptiva del color (ej: "Primary")
 * - color: Color a mostrar en el swatch
 * - onColor: Color de contraste (no usado aquí, para consistencia)
 */
@Composable
fun ColorSwatch(
    name: String,
    color: Color,
    onColor: Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(80.dp)
    ) {
        Box(
            modifier = Modifier
                .size(60.dp)
                .background(color, CustomShapes.card)
        )
        Text(
            text = name,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

/**
 * ⭕ SHAPE DEMO - Muestra visual de una forma del theme
 *
 * 📖 QUÉ HACE:
 * Componente de demostración que visualiza un RoundedCornerShape
 *
 * 🧠 CONCEPTOS CLAVE - SHAPE PREVIEW:
 * - Box con shape aplicado: Demuestra el corner radius visualmente
 * - Background con primaryContainer: Resalta la forma sin distraer
 * - Text descriptivo: Identifica el tipo de shape
 *
 * 🎯 PARÁMETROS:
 * - name: Etiqueta del shape (ej: "Button", "Card", "Chip")
 * - shape: RoundedCornerShape a demostrar visualmente
 *
 */
@Composable
fun ShapeDemo(
    name: String,
    shape: RoundedCornerShape
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(80.dp)
    ) {
        Box(
            modifier = Modifier
                .size(60.dp)
                .background(MaterialTheme.colorScheme.primaryContainer, shape)
        )
        Text(
            text = name,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

/**
 * ♿ ACCESSIBILITY SHOWCASE - Demostración completa de accesibilidad
 *
 * 📖 QUÉ HACE:
 * Pantalla educativa que demuestra las mejores prácticas de accesibilidad en Compose
 *
 * 🧠 CONCEPTOS CLAVE - A11Y (ACCESSIBILITY):
 * - Semantic roles: heading(), button(), etc.
 * - Content descriptions: Texto para screen readers
 * - Touch targets: Mínimo 48dp para usabilidad
 * - Live regions: Anuncios automáticos de cambios
 * - Toggleable states: Estados claros para switches/checkboxes
 *
 * 🎯 SECCIONES EDUCATIVAS:
 * 1. SemanticExamples(): Estructura semántica correcta
 * 2. ContentDescriptionExamples(): Descripciones para screen readers
 * 3. TouchTargetExamples(): Tamaños de touch apropiados
 * 4. AccessibleFormExamples(): Formularios accesibles
 * 5. StateAnnouncementExamples(): Anuncios de estado
 */
@Composable
fun AccessibilityShowcase() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(Dimensions.spaceMedium),
        verticalArrangement = Arrangement.spacedBy(Dimensions.spaceLarge)
    ) {
        item {
            Text(
                text = "Accessibility Showcase",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.semantics { heading() }
            )
            Text(
                text = "Examples of accessible UI components in Compose",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        item { SemanticExamples() }
        item { ContentDescriptionExamples() }
        item { TouchTargetExamples() }
        item { AccessibleFormExamples() }
        item { StateAnnouncementExamples() }
    }
}

/**
 * 🏷️ SEMANTIC EXAMPLES - Roles semánticos y estructura jerárquica
 *
 * 📖 QUÉ DEMUESTRA:
 * Cómo crear estructura semántica correcta para screen readers
 *
 * 🧠 CONCEPTOS CLAVE - SEMANTIC ROLES:
 * - heading(): Marca elementos como títulos/headers
 * - Jerarquía visual: titleLarge → titleMedium → titleSmall
 * - Jerarquía semántica: Orden lógico para navegación
 * - bodyText: Contenido regular sin rol especial
 *
 * ♿ BENEFICIOS PARA USUARIOS:
 * - Screen readers pueden navegar por títulos
 * - Usuarios con discapacidades cognitivas obtienen estructura clara
 * - Navegación rápida saltando entre secciones
 *
 * 💡 TÉCNICA CLAVE:
 * .semantics { heading() } transforma Text normal
 * en landmark navigation para accesibilidad
 */
@Composable
fun SemanticExamples() {
    Card(
        shape = CustomShapes.card,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(Dimensions.spaceMedium),
            verticalArrangement = Arrangement.spacedBy(Dimensions.spaceSmall)
        ) {
            Text(
                text = "Semantic Structure",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.semantics { heading() }
            )

            Text(
                text = "Main Section Header",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.semantics {
                    heading()
                }
            )

            Text(
                text = "This is regular body text that provides context and information to users.",
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                text = "Subsection Header",
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.semantics { heading() }
            )

            Text(
                text = "More detailed information follows the hierarchy.",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

/**
 * 📝 CONTENT DESCRIPTION EXAMPLES - Descripciones para elementos no textuales
 *
 * 📖 QUÉ DEMUESTRA:
 * Cómo proporcionar descripciones efectivas para iconos y elementos interactivos
 *
 * 🧠 CONCEPTOS CLAVE - CONTENT DESCRIPTIONS:
 * - contentDescription: Texto leído por screen readers
 * - Descriptivo vs genérico: "Add to favorites" vs "Button"
 * - null quando hay redundancia: Evita duplicación
 * - hideFromAccessibility(): Para elementos puramente decorativos
 *
 * ✅ EJEMPLOS BUENOS VS MALOS:
 * - ✅ Bueno: "Add new item to favorites" (acción específica)
 * - ❌ Malo: "Star" (demasiado genérico)
 * - ✅ Decorativo: contentDescription = null + hideFromAccessibility()
 *
 * 💡 REGLAS DE ORO:
 * 1. Describe la ACCIÓN, no el icono
 * 2. Sé específico pero conciso
 * 3. Usa null si el padre ya describe
 * 4. Marca decorativos como tal explícitamente
 */
@Composable
fun ContentDescriptionExamples() {
    Card(
        shape = CustomShapes.card,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(Dimensions.spaceMedium),
            verticalArrangement = Arrangement.spacedBy(Dimensions.spaceSmall)
        ) {
            Text(
                text = "Content Descriptions",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.semantics { heading() }
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(Dimensions.spaceSmall),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Good example - with content description
                IconButton(
                    onClick = { },
                    modifier = Modifier.semantics {
                        contentDescription = "Add new item to favorites"
                        role = Role.Button
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = null // null because parent has description
                    )
                }

                Text("Good: Descriptive content description")
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(Dimensions.spaceSmall),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Bad example - poor description
                IconButton(
                    onClick = { },
                    modifier = Modifier.semantics {
                        contentDescription = "Star" // Too generic
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null
                    )
                }

                Text("Poor: Generic description")
            }

            // Decorative example
            Row(
                horizontalArrangement = Arrangement.spacedBy(Dimensions.spaceSmall),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = null, // Decorative icon
                    modifier = Modifier.semantics {
                        hideFromAccessibility() // Explicitly mark as decorative
                    }
                )

                Text("Decorative icon (screen reader ignores)")
            }
        }
    }
}

/**
 * 👆 TOUCH TARGET EXAMPLES - Tamaños de touch targets accesibles
 *
 * 📖 QUÉ DEMUESTRA:
 * La importancia crítica del tamaño mínimo de 48dp para touch targets
 *
 * 🧠 CONCEPTOS CLAVE - TOUCH ACCESSIBILITY:
 * - 48dp mínimo: Basado en investigación ergonómica
 * - sizeIn(minWidth, minHeight): Fuerza tamaño mínimo
 * - Background visual: Muestra el área de touch real
 * - Comparación directa: Bueno (48dp) vs Malo (32dp)
 *
 * 🔬 CIENCIA DETRÁS DE 48DP:
 * - Promedio del dedo humano: ~44dp
 * - 48dp incluye margen de error
 * - Funciona para todas las edades
 * - Incluye usuarios con dificultades motoras
 *
 * ⚖️ COMPLIANCE LEGAL:
 * WCAG 2.1 AA requiere touch targets mínimos
 * para cumplir estándares de accesibilidad
 *
 * 💡 TÉCNICA VISUAL:
 * Background semi-transparente revela el área
 * de touch real vs el contenido visual
 */
@Composable
fun TouchTargetExamples() {
    Card(
        shape = CustomShapes.card,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(Dimensions.spaceMedium),
            verticalArrangement = Arrangement.spacedBy(Dimensions.spaceSmall)
        ) {
            Text(
                text = "Touch Target Sizing",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.semantics { heading() }
            )

            Text(
                text = "Minimum 48dp for reliable touch interaction",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(Dimensions.spaceMedium),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Good example - proper touch target
                IconButton(
                    onClick = { },
                    modifier = Modifier
                        .sizeIn(
                            minWidth = Dimensions.touchTargetMin,
                            minHeight = Dimensions.touchTargetMin
                        )
                        .background(
                            MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f),
                            CustomShapes.button
                        )
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Confirm action"
                    )
                }

                Column {
                    Text("✅ Good", style = MaterialTheme.typography.labelMedium)
                    Text("48dp touch target", style = MaterialTheme.typography.bodySmall)
                }
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(Dimensions.spaceMedium),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Bad example - too small
                IconButton(
                    onClick = { },
                    modifier = Modifier
                        .size(32.dp)
                        .background(
                            MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.3f),
                            CustomShapes.button
                        )
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Cancel action",
                        modifier = Modifier.size(16.dp)
                    )
                }

                Column {
                    Text("❌ Poor", style = MaterialTheme.typography.labelMedium)
                    Text("32dp too small", style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}

/**
 * 📋 ACCESSIBLE FORM EXAMPLES - Formularios completamente accesibles
 *
 * 📖 QUÉ DEMUESTRA:
 * Implementación correcta de campos de formulario accesibles
 *
 * 🧠 CONCEPTOS CLAVE - FORM ACCESSIBILITY:
 * - label: Etiqueta clara y descriptiva
 * - supportingText: Ayuda adicional para el usuario
 * - contentDescription: Información para screen readers
 * - mergeDescendants: Agrupa elementos relacionados
 * - Live announcements: Feedback inmediato de cambios
 *
 * 🔧 COMPONENTES DEMOSTRADOS:
 * 1. OutlinedTextField: Input con label y supporting text
 * 2. Checkbox: Con label clickeable y estado anunciado
 * 3. Slider: Con valor actual anunciado dinámicamente
 *
 * ♿ TÉCNICAS AVANZADAS:
 * - mergeDescendants: Checkbox + Text como unidad semántica
 * - Dynamic contentDescription: Anuncia valores actuales
 * - Clickable labels: Toda el área es interactiva
 *
 * 💡 PATRÓN CLAVE:
 * Cada input tiene label, descripción, y estado
 * claramente comunicado a screen readers
 */
@Composable
fun AccessibleFormExamples() {
    var textValue by remember { mutableStateOf("") }
    var isChecked by remember { mutableStateOf(false) }
    var sliderValue by remember { mutableFloatStateOf(0.5f) }

    Card(
        shape = CustomShapes.card,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(Dimensions.spaceMedium),
            verticalArrangement = Arrangement.spacedBy(Dimensions.spaceSmall)
        ) {
            Text(
                text = "Accessible Form Components",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.semantics { heading() }
            )

            // Text field with proper labeling
            OutlinedTextField(
                value = textValue,
                onValueChange = { textValue = it },
                label = { Text("User Name") },
                supportingText = { Text("Enter your full name") },
                modifier = Modifier
                    .fillMaxWidth()
                    .semantics {
                        contentDescription = "User name input field"
                    }
            )

            // Checkbox with clear labeling
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.semantics(mergeDescendants = true) { }
            ) {
                Checkbox(
                    checked = isChecked,
                    onCheckedChange = { isChecked = it },
                    modifier = Modifier.semantics {
                        contentDescription =
                            if (isChecked) "Agree to terms, checked" else "Agree to terms, unchecked"
                    }
                )
                Text(
                    text = "I agree to the terms and conditions",
                    modifier = Modifier.clickable { isChecked = !isChecked }
                )
            }

            // Slider with value announcement
            Column {
                Text("Volume: ${(sliderValue * 100).toInt()}%")
                Slider(
                    value = sliderValue,
                    onValueChange = { sliderValue = it },
                    modifier = Modifier.semantics {
                        contentDescription =
                            "Volume slider, current value ${(sliderValue * 100).toInt()} percent"
                    }
                )
            }
        }
    }
}

/**
 * 📢 STATE ANNOUNCEMENT EXAMPLES - Anuncios automáticos de cambios de estado
 *
 * 📖 QUÉ DEMUESTRA:
 * Técnicas avanzadas para comunicar cambios dinámicos a usuarios con screen readers
 *
 * 🧠 CONCEPTOS CLAVE - LIVE REGIONS & STATE:
 * - LiveRegionMode.Polite: Anuncia cambios cuando sea apropiado
 * - stateDescription: Describe estado actual de componentes
 * - toggleableState: Estados específicos para switches/checkboxes
 * - mergeDescendants: Agrupa elementos para anuncio conjunto
 *
 * 🔊 TIPOS DE ANUNCIOS:
 * 1. Live Region: Cambios de status automáticamente anunciados
 * 2. State Description: Estado actual de botones/controles
 * 3. Toggleable State: On/Off/Indeterminate para switches
 *
 * ⚡ TÉCNICAS DEMOSTRADAS:
 * - Status que cambia → Anuncio automático
 * - Counter con state description dinámico
 * - Switch con toggleable state apropiado
 *
 * 💡 IMPORTANCIA:
 * Usuarios ciegos dependen de estos anuncios para
 * entender cambios que otros ven visualmente
 */
@Composable
fun StateAnnouncementExamples() {
    var status by remember { mutableStateOf("Ready") }
    var counter by remember { mutableIntStateOf(0) }

    Card(
        shape = CustomShapes.card,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(Dimensions.spaceMedium),
            verticalArrangement = Arrangement.spacedBy(Dimensions.spaceSmall)
        ) {
            Text(
                text = "State Announcements",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.semantics { heading() }
            )

            // Live region for status updates
            Text(
                text = "Status: $status",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.semantics {
                    liveRegion = LiveRegionMode.Polite
                    contentDescription = "Current status: $status"
                }
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(Dimensions.spaceSmall)
            ) {
                Button(
                    onClick = {
                        status = when (status) {
                            "Ready" -> "Loading..."
                            "Loading..." -> "Success"
                            "Success" -> "Error"
                            else -> "Ready"
                        }
                    }
                ) {
                    Text("Change Status")
                }

                Button(
                    onClick = {
                        counter++
                        // This will announce the new count
                    },
                    modifier = Modifier.semantics {
                        stateDescription = "Counter button, current count: $counter"
                    }
                ) {
                    Text("Count: $counter")
                }
            }

            // Toggle with state description
            var isEnabled by remember { mutableStateOf(false) }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clickable { isEnabled = !isEnabled }
                    .semantics(mergeDescendants = true) {
                        role = Role.Switch
                        toggleableState = if (isEnabled) ToggleableState.On else ToggleableState.Off
                        contentDescription =
                            "Notifications ${if (isEnabled) "enabled" else "disabled"}"
                    }
            ) {
                Switch(
                    checked = isEnabled,
                    onCheckedChange = null // Let parent handle the click
                )
                Text("Enable Notifications")
            }
        }
    }
}

/**
 * 🚀 APP - Composable principal con navegación por tabs
 *
 * 📖 QUÉ HACE:
 * Orquesta toda la aplicación demo con navegación por bottom tabs
 *
 * 🧠 CONCEPTOS CLAVE - APP ARCHITECTURE:
 * - Scaffold: Estructura principal con bottom navigation
 * - State hoisting: selectedTab manejado aquí y pasado down
 * - rememberSaveable: Preserva tab selection durante rotaciones
 * - NavigationBar: Bottom navigation con 5 tabs principales
 *
 * 📱 ESTRUCTURA DE NAVEGACIÓN:
 * 1. Layout (🏠): LayoutPlayground - Fundamentos de layout
 * 2. State (⚙️): Manejo de estado con Counter y Stepper
 * 3. Tasks (✅): TasksScreen - Lists, CRUD, Scaffold completo
 * 4. Theme (🎨): ThemingShowcase - Sistema completo de theming
 * 5. A11y (♿): AccessibilityShowcase - Mejores prácticas accesibilidad
 *
 *
 * 💡 ARCHITECTURAL DECISIONS:
 * - Single Activity + Compose navigation (modern approach)
 * - Bottom navigation (familiar UX pattern)
 * - State preservation (mejor UX)
 * - Consistent theming across all screens
 */
@Composable
fun App() {
    var selectedTab by rememberSaveable { mutableIntStateOf(0) }

    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.House,
                            contentDescription = "Layout tab"
                        )
                    },
                    label = { Text("Layout") }
                )
                NavigationBarItem(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "State tab"
                        )
                    },
                    label = { Text("State") }
                )
                NavigationBarItem(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Tasks tab"
                        )
                    },
                    label = { Text("Tasks") }
                )
                NavigationBarItem(
                    selected = selectedTab == 3,
                    onClick = { selectedTab = 3 },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Palette,
                            contentDescription = "Theme tab"
                        )
                    },
                    label = { Text("Theme") }
                )
                NavigationBarItem(
                    selected = selectedTab == 4,
                    onClick = { selectedTab = 4 },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Accessibility,
                            contentDescription = "Accessibility tab"
                        )
                    },
                    label = { Text("A11y") }
                )
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            when (selectedTab) {
                0 -> LayoutPlayground()
                1 -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(Dimensions.spaceMedium),
                        verticalArrangement = Arrangement.spacedBy(Dimensions.spaceXLarge),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "State Management",
                            style = MaterialTheme.typography.headlineMedium,
                            modifier = Modifier.semantics { heading() }
                        )

                        var quantity by rememberSaveable { mutableIntStateOf(1) }

                        CounterDemo(quantity) {
                            quantity = it.coerceAtLeast(0)
                        }
                        StepperDemo(quantity) {
                            quantity = it.coerceAtLeast(0)
                        }
                    }
                }

                2 -> TasksScreen()
                3 -> ThemingShowcase()
                4 -> AccessibilityShowcase()
            }
        }
    }
}

// Previews
@Preview(showBackground = true)
@Composable
fun TaskCardPreview() {
    MaterialTheme {
        TaskCard(
            title = "Sample Task",
            onRemove = {},
            onShare = {}
        )
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun TaskCardDarkPreview() {
    MaterialTheme {
        TaskCard(
            title = "Sample Task",
            onRemove = {},
            onShare = {}
        )
    }
}

@Preview(showBackground = true, fontScale = 1.3f)
@Composable
fun AppFontScalePreview() {
    MaterialTheme {
        App()
    }
}

@Preview(showBackground = true, device = Devices.PIXEL_6_PRO)
@Composable
fun AppDevicePreview() {
    MaterialTheme {
        App()
    }
}

@Preview(showBackground = true)
@Composable
fun LayoutPlaygroundPreview() {
    MaterialTheme {
        LayoutPlayground()
    }
}

@Preview(showBackground = true)
@Composable
fun CounterDemoPreview() {
    VideoconferenciaTheme {
        CounterDemo(
            quantity1 = 5,
            onValueChange = { }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun StepperDemoPreview() {
    VideoconferenciaTheme {
        StepperDemo(
            quantity1 = 3,
            onValueChange = { }
        )
    }
}

@Preview(showBackground = true, name = "Theming Showcase - Light")
@Composable
fun ThemingShowcasePreview() {
    VideoconferenciaTheme {
        ThemingShowcase()
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    name = "Theming Showcase - Dark"
)
@Composable
fun ThemingShowcaseDarkPreview() {
    VideoconferenciaTheme {
        ThemingShowcase()
    }
}

@Preview(showBackground = true, name = "App with 4 Tabs - Light")
@Composable
fun AppWithThemePreview() {
    VideoconferenciaTheme {
        App()
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    name = "App with 5 Tabs - Dark"
)
@Composable
fun AppWithThemeDarkPreview() {
    VideoconferenciaTheme {
        App()
    }
}

@Preview(showBackground = true, name = "Accessibility Showcase - Light")
@Composable
fun AccessibilityShowcasePreview() {
    VideoconferenciaTheme {
        AccessibilityShowcase()
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    name = "Accessibility Showcase - Dark"
)
@Composable
fun AccessibilityShowcaseDarkPreview() {
    VideoconferenciaTheme {
        AccessibilityShowcase()
    }
}

@Preview(showBackground = true, fontScale = 1.3f, name = "Accessibility with Large Font")
@Composable
fun AccessibilityLargeFontPreview() {
    VideoconferenciaTheme {
        TouchTargetExamples()
    }
}

// =====================================
// 🎨 MAIN COMPOSABLE WRAPPER - PARA INTEGRACIÓN CON NAVEGACIÓN
// =====================================

/**
 * 🎯 S02Demos - Composable principal que envuelve todas las demos de S02
 *
 * Esta función proporciona una interfaz principal para acceder a todas las
 * demostraciones de Jetpack Compose y Material Design 3 de la sección S02.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun S02Demos(
    onNavigateBack: () -> Unit = {}
) {
    var selectedDemo by rememberSaveable { mutableIntStateOf(0) }

    val demos = listOf(
        "Layout Playground" to @Composable { LayoutPlayground() },
        "Tasks Screen" to @Composable { TasksScreen() },
        "Counter Demo" to @Composable { CounterDemo(quantity1 = 1, onValueChange = {}) },
        "Stepper Demo" to @Composable { StepperDemo(quantity1 = 1, onValueChange = {}) },
        "Shape Demo" to @Composable {
            ShapeDemo(
                name = "Naombre",
                shape = RoundedCornerShape(2.dp)
            )
        },
        "Accessibility Showcase" to @Composable { AccessibilityShowcase() }
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "S02 - Compose + Material Design",
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Volver al menú principal"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onSecondaryContainer
                )
            )
        },
        bottomBar = {
            NavigationBar {
                demos.forEachIndexed { index, (title, _) ->
                    NavigationBarItem(
                        selected = selectedDemo == index,
                        onClick = { selectedDemo = index },
                        icon = {
                            Icon(
                                imageVector = when (index) {
                                    0 -> Icons.Default.GridView
                                    1 -> Icons.Default.Task
                                    2 -> Icons.Default.Add
                                    3 -> Icons.Default.LinearScale
                                    4 -> Icons.Default.Star
                                    5 -> Icons.Default.Accessibility
                                    else -> Icons.Default.Star
                                },
                                contentDescription = title
                            )
                        },
                        label = {
                            Text(
                                text = title.take(8),
                                style = MaterialTheme.typography.labelSmall
                            )
                        }
                    )
                }
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            demos[selectedDemo].second()
        }
    }
}

// =====================================
// 🎨 PREVIEWS PRINCIPALES
// =====================================

@Preview(name = "S02 Demos - Main")
@Composable
private fun S02DemosPreview() {
    VideoconferenciaTheme {
        S02Demos()
    }
}

@Preview(
    name = "S02 Demos - Dark Mode",
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun S02DemosDarkPreview() {
    VideoconferenciaTheme {
        S02Demos()
    }
}