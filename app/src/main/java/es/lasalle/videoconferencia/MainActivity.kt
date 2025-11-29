package es.lasalle.videoconferencia

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import es.lasalle.videoconferencia.s01.EjemplosKotlin
import es.lasalle.videoconferencia.s02.S02Demos
import es.lasalle.videoconferencia.s03.S03Demo
import es.lasalle.videoconferencia.s04.ui.S04Demo
import es.lasalle.videoconferencia.s05.S05Demo
import es.lasalle.videoconferencia.ui.theme.Dimensions
import es.lasalle.videoconferencia.ui.theme.VideoconferenciaTheme

// =====================================
// 🏠 MAIN ACTIVITY - PUNTO DE ENTRADA DE LA APP EDUCATIVA
// =====================================

/**
 * 🎯 MainActivity - Activity principal de la aplicación educativa
 *
 * 📖 SINGLE ACTIVITY + COMPOSE NAVIGATION:
 * Esta aplicación demuestra la arquitectura moderna de Android:
 * - Una sola Activity que contiene toda la navegación
 * - Jetpack Compose para toda la UI
 * - Navigation Compose para el routing
 * - Material Design 3 para el theming
 *
 * 🧭 ESTRUCTURA DE NAVEGACIÓN:
 * - MainMenu: Pantalla principal con acceso a todas las demos
 * - S01: Ejemplos fundamentales de Kotlin
 * - S02: Demos de Jetpack Compose y Material Design
 * - S03: Navegación + MVVM + UDF completo
 * - S04: Retrofit + API + Repository Pattern
 *
 * 🎨 DISEÑO EDUCATIVO:
 * Cada sección está diseñada para enseñar conceptos específicos
 * de forma progresiva, desde lo básico hasta lo avanzado.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VideoconferenciaTheme {
                MainNavigationApp()
            }
        }
    }
}

// =====================================
// 🧭 NAVIGATION APP - Aplicación principal con navegación
// =====================================

/**
 * 🏗️ MainNavigationApp - Aplicación principal con navegación completa
 *
 * 📖 NAVIGATION SETUP:
 * Configura la navegación principal de la app con todas las secciones
 * educativas. Cada sección demuestra diferentes aspectos del desarrollo Android.
 */

sealed class Routes(val route: String) {
    object MainMenu : Routes("main_menu")
}

@Composable
fun MainNavigationApp() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.MainMenu.route
    ) {
        /**
         * 🏠 Main Menu - Menú principal
         */
        composable(Routes.MainMenu.route) {
            MainMenuScreen(navController = navController)
        }

        /**
         * 📚 S01 - Ejemplos de Kotlin
         */
        composable("s01_kotlin") {
            EjemplosKotlin(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        /**
         * 🎨 S02 - Compose + Material Design
         */
        composable("s02_compose") {
            S02Demos(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        /**
         * 🧭 S03 - Navegación + MVVM
         */
        composable("s03_navigation") {
            S03Demo(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        /**
         * 🌐 S04 - Retrofit + API
         */
        composable("s04_retrofit") {
            S04Demo(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        /**
         * 📱 S05 - Intent Demonstrations
         */
        composable("s05_intents") {
            S05Demo(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}

// =====================================
// 🏠 MAIN MENU SCREEN - Pantalla de menú principal
// =====================================

/**
 * 📱 MainMenuScreen - Pantalla principal de selección de demos
 *
 * 📖 MENU DESIGN:
 * Presenta todas las secciones educativas disponibles de forma
 * clara y organizada, explicando qué aprenderá el usuario en cada una.
 *
 * @param navController Controlador de navegación para cambiar de pantalla
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainMenuScreen(navController: NavHostController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "La Salle - Desarrollo Android",
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(Dimensions.spaceMedium)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(Dimensions.spaceMedium)
        ) {
            /**
             * 🎓 Header Section - Bienvenida y descripción
             */
            MainMenuHeader()

            /**
             * 📚 Demo Sections - Secciones de demos
             */
            DemoSection(
                title = "S01 - Fundamentos de Kotlin",
                description = "Conceptos básicos del lenguaje Kotlin: variables, funciones, clases, colecciones, corrutinas y Flow",
                icon = Icons.Default.Code,
                color = MaterialTheme.colorScheme.primary,
                onClick = { navController.navigate("s01_kotlin") }
            )

            DemoSection(
                title = "S02 - Jetpack Compose + Material Design",
                description = "Componentes de UI, layouts, estado, theming y navegación básica con Compose",
                icon = Icons.Default.Palette,
                color = MaterialTheme.colorScheme.secondary,
                onClick = { navController.navigate("s02_compose") }
            )

            DemoSection(
                title = "S03 - Navegación + MVVM + UDF",
                description = "Arquitectura completa: navegación avanzada, ViewModels, estados complejos y flujo unidireccional",
                icon = Icons.Default.Navigation,
                color = MaterialTheme.colorScheme.tertiary,
                onClick = { navController.navigate("s03_navigation") }
            )

            DemoSection(
                title = "S04 - Retrofit + API + Repository",
                description = "Networking con Retrofit, consumo de APIs REST, Repository pattern, caching y manejo de errores",
                icon = Icons.Default.Cloud,
                color = MaterialTheme.colorScheme.surfaceTint,
                onClick = { navController.navigate("s04_retrofit") }
            )

            DemoSection(
                title = "S05 - Intent Demonstrations",
                description = "Intents explícitos e implícitos: navegación interna y comunicación con aplicaciones externas",
                icon = Icons.Default.SendAndArchive,
                color = MaterialTheme.colorScheme.primary,
                onClick = { navController.navigate("s05_intents") }
            )

            Spacer(modifier = Modifier.height(Dimensions.spaceLarge))
        }
    }
}

/**
 * 🎓 MainMenuHeader - Encabezado del menú principal
 */
@Composable
private fun MainMenuHeader() {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier
                .padding(Dimensions.spaceLarge)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(Dimensions.spaceSmall)
        ) {
            Icon(
                imageVector = Icons.Default.School,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.primary
            )

            Text(
                text = "M07 - Android",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}

/**
 * 📱 DemoSection - Sección individual de demo
 *
 * @param title Título de la sección
 * @param description Descripción de lo que se aprende
 * @param icon Icono representativo
 * @param color Color del tema de la sección
 * @param onClick Acción al hacer click
 */
@Composable
private fun DemoSection(
    title: String,
    description: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: androidx.compose.ui.graphics.Color,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimensions.spaceMedium),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Dimensions.spaceMedium)
        ) {
            /**
             * 🎨 Icon Section
             */
            Card(
                modifier = Modifier.size(60.dp),
                colors = CardDefaults.cardColors(
                    containerColor = color.copy(alpha = 0.1f)
                )
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        modifier = Modifier.size(32.dp),
                        tint = color
                    )
                }
            }

            /**
             * 📝 Text Section
             */
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(Dimensions.spaceXSmall)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            /**
             * ➡️ Arrow Icon
             */
            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

/**
 * ℹ️ MainMenuFooter - Pie del menú principal
 */
@Composable
private fun MainMenuFooter() {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
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
                    text = "Sobre esta aplicación",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Text(
                text = "Esta aplicación es un recurso educativo completo para aprender desarrollo Android moderno. Cada sección incluye código fuente comentado, ejemplos prácticos y patrones de diseño recomendados.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Text(
                text = "• Kotlin: Sintaxis, paradigmas y mejores prácticas\n• Compose: UI declarativa y Material Design 3\n• Arquitectura: MVVM, UDF y gestión de estado\n• Navegación: Single Activity y rutas type-safe",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

// =====================================
// 🎨 PREVIEWS - Para desarrollo y testing
// =====================================

@PreviewLightDark
@PreviewFontScale
@Composable
private fun MainMenuDarkPreview() {
    VideoconferenciaTheme {
        MainMenuScreen(navController = rememberNavController())
    }
}