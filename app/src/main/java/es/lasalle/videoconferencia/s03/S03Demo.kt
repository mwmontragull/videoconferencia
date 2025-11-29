package es.lasalle.videoconferencia.s03

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import es.lasalle.videoconferencia.s03.screens.ProfileScreen
import es.lasalle.videoconferencia.s03.screens.SettingsScreen
import es.lasalle.videoconferencia.s03.screens.TaskDetailScreen
import es.lasalle.videoconferencia.ui.theme.Dimensions
import es.lasalle.videoconferencia.ui.theme.VideoconferenciaTheme

// =====================================
// 🚀 S03 DEMO - NAVEGACIÓN + MVVM + UDF
// =====================================

/**
 * 🎯 S03Demo - Demostración completa de navegación y arquitectura moderna
 * 
 * 📖 CONCEPTOS EDUCATIVOS CUBIERTOS:
 * 
 * 🧭 NAVEGACIÓN EN COMPOSE:
 * - Single Activity + Navigation Compose
 * - NavController y rutas type-safe
 * - Bottom Navigation con múltiples pantallas
 * - Paso de parámetros entre pantallas
 * - Back stack management automático
 * 
 * 🏗️ ARQUITECTURA MVVM + UDF:
 * - ViewModel como single source of truth
 * - UiState para datos persistentes
 * - UiEvent para acciones del usuario
 * - UiEffect para efectos laterales one-shot
 * - Unidirectional Data Flow completo
 * 
 * 📱 ACTIVITY LIFECYCLE:
 * - Relación entre Activity y Compose
 * - Supervivencia a configuration changes
 * - Gestión automática de recursos
 * - ViewModelScope para operaciones async
 * 
 * 💡 CARACTERÍSTICAS EDUCATIVAS:
 * - Sin inyección de dependencias (apropiado para principiantes)
 * - Sin persistencia real (todo en memoria)
 * - Datos simulados con delays realistas
 * - Documentación exhaustiva en cada componente
 * - Previews comprehensivos para desarrollo
 * 
 * 🎨 MATERIAL DESIGN 3:
 * - Bottom Navigation estándar
 * - Color scheme consistente
 * - Typography scale apropiada
 * - Shape system integrado
 * - Accessibility compliance
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun S03Demo(
    onNavigateBack: () -> Unit = {}
) {
    /**
     * 🧭 NavController - Centro de navegación
     * 
     * 📖 NAVEGACIÓN EN COMPOSE:
     * NavController maneja toda la navegación entre pantallas.
     * Se crea una vez y se pasa a todos los composables que lo necesiten.
     * 
     * 🧠 CONCEPTO CLAVE - HOISTING:
     * Creamos el NavController en el nivel más alto posible
     * y lo pasamos hacia abajo. Esto permite control centralizado.
     */
    val navController = rememberNavController()
    
    /**
     * 📍 Current Route - Estado de navegación actual
     * 
     * 📖 OBSERVACIÓN DE NAVEGACIÓN:
     * currentBackStackEntryAsState() nos permite reaccionar
     * a cambios en la navegación de forma reactiva.
     * 
     * 🧠 CONCEPTO CLAVE - REACTIVE NAVIGATION:
     * La UI se recompone automáticamente cuando cambia la ruta.
     * Esto es fundamental para highlighting en bottom navigation.
     */
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    
    /**
     * 🎨 Scaffold - Layout principal con bottom navigation
     * 
     * 📖 SCAFFOLD PATTERN:
     * Scaffold proporciona la estructura básica de una pantalla:
     * TopBar, BottomBar, FloatingActionButton, Content.
     * 
     * 🧠 CONCEPTO CLAVE - MATERIAL LAYOUT:
     * Seguimos las guías de Material Design para layout estándar.
     * El content se ajusta automáticamente al espacio disponible.
     */
    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        text = "S03 - Navegación + MVVM",
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver al menú principal"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        bottomBar = {
            S03BottomNavigation(
                navController = navController,
                currentDestination = currentDestination?.route
            )
        }
    ) { paddingValues ->
        /**
         * 🗺️ NavHost - Definición de rutas y pantallas
         * 
         * 📖 NAVIGATION GRAPH:
         * NavHost define todas las rutas disponibles y qué Composable
         * mostrar para cada una. Es el "router" de la aplicación.
         * 
         * 🧠 CONCEPTO CLAVE - DECLARATIVE NAVIGATION:
         * En lugar de imperative navigation (startActivity, fragments),
         * declaramos todas las rutas posibles de forma declarativa.
         * 
         * 💡 RUTAS COMO STRINGS:
         * Usamos strings simples para las rutas. En apps grandes,
         * considera usar sealed classes para type safety.
         */
        NavHost(
            navController = navController,
            startDestination = S03Routes.PROFILE,
            modifier = Modifier.padding(paddingValues)
        ) {
            /**
             * 👤 Pantalla de Perfil
             * Formulario con validación y estados complejos
             */
            composable(S03Routes.PROFILE) {
                ProfileScreen()
            }
            
            /**
             * ⚙️ Pantalla de Configuración
             * Switches simples con estados en memoria
             */
            composable(S03Routes.SETTINGS) {
                SettingsScreen()
            }
            
            /**
             * 📋 Lista de Tareas
             * Vista maestro con navegación a detalle
             */
            composable(S03Routes.TASKS) {
                // TODO: TaskListScreen cuando esté implementada
                PlaceholderScreen(
                    title = "Lista de Tareas",
                    description = "Lista de tareas con navegación a detalle\n• Navegación con parámetros\n• Datos simulados\n• Pull to refresh",
                    icon = Icons.Default.List,
                    onItemClick = { taskId ->
                        navController.navigate("${S03Routes.TASK_DETAIL}/$taskId")
                    }
                )
            }
            
            /**
             * 📄 Detalle de Tarea
             * Pantalla con parámetros y carga async
             */
            composable("${S03Routes.TASK_DETAIL}/{taskId}") { backStackEntry ->
                val taskId = backStackEntry.arguments?.getString("taskId") ?: "1"
                TaskDetailScreen(
                    taskId = taskId,
                    onNavigateBack = {
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}

/**
 * 🧭 S03BottomNavigation - Bottom navigation bar
 * 
 * 📖 BOTTOM NAVIGATION PATTERN:
 * Material Design recomienda bottom navigation para 3-5 destinos principales.
 * Cada tab debe representar una sección diferente de la app.
 * 
 * 🧠 CONCEPTO CLAVE - NAVIGATION STATE:
 * El state del bottom navigation se deriva de la ruta actual.
 * No mantenemos estado separado, sino que observamos el NavController.
 * 
 * @param navController Controlador de navegación
 * @param currentDestination Ruta actual para highlighting
 */
@Composable
private fun S03BottomNavigation(
    navController: NavHostController,
    currentDestination: String?
) {
    /**
     * 📝 Navigation Items - Definición de tabs
     * 
     * 📖 BOTTOM NAV ITEMS:
     * Cada item tiene ruta, etiqueta, icono y descripción.
     * El orden aquí determina el orden visual en la UI.
     */
    val navigationItems = listOf(
        BottomNavItem(
            route = S03Routes.PROFILE,
            label = "Perfil",
            icon = Icons.Default.Person,
            contentDescription = "Pantalla de perfil de usuario"
        ),
        BottomNavItem(
            route = S03Routes.SETTINGS,
            label = "Config",
            icon = Icons.Default.Settings,
            contentDescription = "Pantalla de configuración"
        ),
        BottomNavItem(
            route = S03Routes.TASKS,
            label = "Tareas",
            icon = Icons.Default.List,
            contentDescription = "Lista de tareas"
        )
    )
    
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
        contentColor = MaterialTheme.colorScheme.onSurface
    ) {
        navigationItems.forEach { item ->
            NavigationBarItem(
                selected = currentDestination == item.route,
                onClick = {
                    /**
                     * 🚀 LÓGICA DE NAVEGACIÓN PRINCIPAL
                     *
                     * Este bloque se ejecuta cuando el usuario pulsa un ítem de la barra
                     * de navegación inferior (Bottom Navigation Bar). Su objetivo es navegar
                     * a la pantalla seleccionada aplicando las mejores prácticas recomendadas
                     * por Google para una experiencia de usuario fluida y predecible.
                     *
                     * ---
                     *
                     * 🧠 CONCEPTOS CLAVE APLICADOS:
                     *
                     * 1.  popUpTo(navController.graph.startDestinationId):
                     *     Limpia el "back stack" (historial de pantallas) hasta la pantalla
                     *     inicial del gráfico de navegación. Esto evita acumular un historial
                     *     infinito de pantallas al cambiar de pestaña. El resultado es que si
                     *     el usuario pulsa el botón de "Atrás" desde cualquier pantalla principal,
                     *     la aplicación se cerrará, que es el comportamiento esperado.
                     *
                     * 2.  saveState = true:
                     *     Dentro de `popUpTo`, le indicamos que guarde el estado de la pantalla
                     *     que estamos abandonando (y de todas las que se eliminan del back stack).
                     *     Esto incluye la posición de scroll, el texto en un campo de formulario, etc.
                     *
                     * 3.  launchSingleTop = true:
                     *     Evita crear múltiples copias de la misma pantalla en el historial.
                     *     Si ya estamos en la pantalla de "Perfil" y volvemos a pulsar el ítem
                     *     "Perfil", no se creará una nueva instancia encima, simplemente se
                     *     reutilizará la existente.
                     *
                     * 4.  restoreState = true:
                     *     Restaura el estado guardado previamente con `saveState`. Si navegamos de
                     *     "Perfil" a "Tareas" y luego volvemos a "Perfil", la pantalla de "Perfil"
                     *     se mostrará exactamente como la dejamos, gracias a esta propiedad.
                     */
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.contentDescription
                    )
                },
                label = {
                    Text(
                        text = item.label,
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            )
        }
    }
}

/**
 * 📱 PlaceholderScreen - Pantalla temporal mientras desarrollamos
 * 
 * 📖 DEVELOPMENT PLACEHOLDER:
 * Muestra la estructura y conceptos de cada pantalla
 * mientras implementamos las pantallas reales.
 * 
 * @param title Título de la pantalla
 * @param description Descripción de funcionalidad
 * @param icon Icono representativo
 * @param onItemClick Callback para simular navegación a detalle
 * @param onNavigateBack Callback para navegación hacia atrás
 */
@Composable
private fun PlaceholderScreen(
    title: String,
    description: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onItemClick: ((String) -> Unit)? = null,
    onNavigateBack: (() -> Unit)? = null
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(Dimensions.spaceMedium),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        
        Spacer(modifier = Modifier.height(Dimensions.spaceMedium))
        
        Text(
            text = title,
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
        
        Spacer(modifier = Modifier.height(Dimensions.spaceSmall))
        
        Text(
            text = description,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        if (onItemClick != null) {
            Spacer(modifier = Modifier.height(Dimensions.spaceLarge))
            
            Button(
                onClick = { onItemClick("sample-task-123") }
            ) {
                Text("Ir a Detalle (Demo)")
            }
        }
        
        if (onNavigateBack != null) {
            Spacer(modifier = Modifier.height(Dimensions.spaceMedium))
            
            OutlinedButton(
                onClick = onNavigateBack
            ) {
                Text("Volver")
            }
        }
    }
}

// =====================================
// 📝 DATA CLASSES Y CONSTANTES
// =====================================

/**
 * 🗺️ S03Routes - Constantes de rutas de navegación
 * 
 * 📖 ROUTE CONSTANTS:
 * Centralizamos todas las rutas en un objeto para evitar typos
 * y facilitar refactoring. En apps grandes, usa sealed classes.
 * 
 * 🧠 CONCEPTO CLAVE - SINGLE SOURCE OF TRUTH:
 * Las rutas se definen una vez y se reutilizan en toda la app.
 * Cambiar una ruta aquí la actualiza en todos lados.
 */
object S03Routes {
    const val PROFILE = "profile"
    const val SETTINGS = "settings"
    const val TASKS = "tasks"
    const val TASK_DETAIL = "task_detail"
}

/**
 * 🧭 BottomNavItem - Modelo para items de bottom navigation
 * 
 * 📖 NAVIGATION ITEM MODEL:
 * Encapsula toda la información necesaria para un item
 * de bottom navigation de forma type-safe.
 * 
 * @param route Ruta de navegación
 * @param label Texto visible en el tab
 * @param icon Icono del tab
 * @param contentDescription Descripción para accesibilidad
 */
data class BottomNavItem(
    val route: String,
    val label: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val contentDescription: String
)

// =====================================
// 🎨 PREVIEWS - Para desarrollo y testing
// =====================================

/**
 * 👀 Preview principal en modo claro
 */
@Preview(
    name = "S03Demo - Light Mode",
    showBackground = true
)
@Composable
private fun S03DemoPreview() {
    VideoconferenciaTheme {
        S03Demo()
    }
}

/**
 * 🌙 Preview en modo oscuro
 */
@Preview(
    name = "S03Demo - Dark Mode",
    showBackground = true,
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun S03DemoDarkPreview() {
    VideoconferenciaTheme {
        S03Demo()
    }
}

/**
 * 📱 Preview en dispositivo grande
 */
@Preview(
    name = "S03Demo - Tablet",
    showBackground = true,
    device = "spec:width=1280dp,height=800dp,dpi=240"
)
@Composable
private fun S03DemoTabletPreview() {
    VideoconferenciaTheme {
        S03Demo()
    }
}

// =====================================
// 🧠 CONCEPTOS PEDAGÓGICOS ADICIONALES
// =====================================

/**
 * 💡 ACTIVITY LIFECYCLE Y COMPOSE:
 * 
 * 📱 ACTIVITY LIFECYCLE PHASES:
 * - onCreate: Activity se crea, setContent() configura UI
 * - onStart: Activity visible pero no interactiva
 * - onResume: Activity completamente interactiva
 * - onPause: Otra activity viene al frente
 * - onStop: Activity no visible
 * - onDestroy: Activity se destruye
 * 
 * 🎭 COMPOSE Y LIFECYCLE:
 * - Compose se monta cuando Activity becomes visible
 * - Compose se desmonta cuando Activity is destroyed
 * - Configuration changes (rotación) → Activity recreated, Compose remounted
 * - ViewModels sobreviven configuration changes
 * - StateFlow mantiene estado a través de recreations
 * 
 * 🔄 RECOMPOSITION LIFECYCLE:
 * - Initial composition: Primera vez que se ejecuta @Composable
 * - Recomposition: Se re-ejecuta cuando cambia el estado observado
 * - Disposal: Composable se limpia cuando sale del árbol de UI
 * 
 * 💾 STATE SURVIVAL:
 * - ViewModel state → Sobrevive configuration changes
 * - Compose remember → Se pierde en configuration changes
 * - Compose rememberSaveable → Sobrevive configuration changes
 * - Navigation state → Se preserva automáticamente
 */

/**
 * 🧭 NAVIGATION BEST PRACTICES:
 * 
 * ✅ DO:
 * - Usa rutas descriptivas y consistentes
 * - Centraliza las rutas en constantes/sealed classes
 * - Configura saveState/restoreState para mejor UX
 * - Usa launchSingleTop para prevenir duplicados
 * - Maneja back button apropiadamente
 * 
 * ❌ DON'T:
 * - No hardcodees rutas en múltiples lugares
 * - No ignores el back stack management
 * - No olvides pasar parámetros necesarios
 * - No crees navigation loops infinitos
 * - No abuses de popUpTo sin entender el comportamiento
 * 
 * 🎯 NAVIGATION PATTERNS:
 * - Bottom navigation: 3-5 secciones principales
 * - Top navigation: Dentro de una sección
 * - Modal navigation: Dialogs, bottom sheets
 * - Deep linking: URLs que abren pantallas específicas
 */

/**
 * 🏗️ MVVM + UDF ARCHITECTURE SUMMARY:
 * 
 * 📊 MODEL (Data Layer):
 * - UiState: Qué mostrar en pantalla
 * - UiEvent: Qué puede hacer el usuario
 * - UiEffect: Efectos laterales one-shot
 * - Domain models: TaskDetail, etc.
 * 
 * 🎭 VIEW (UI Layer):
 * - Composables: Presentación reactiva
 * - Screens: Pantallas completas
 * - Components: Elementos reutilizables
 * - Navigation: Flujo entre pantallas
 * 
 * 🧠 VIEWMODEL (Presentation Layer):
 * - State management: StateFlow para estado
 * - Event handling: Procesa acciones de UI
 * - Business logic: Validaciones, transformaciones
 * - Effect emission: Envía efectos one-shot
 * 
 * 🔄 DATA FLOW:
 * UI → UiEvent → ViewModel → Business Logic → UiState → UI
 *                    ↓
 *               UiEffect → UI (one-shot actions)
 */