package es.lasalle.videoconferencia.s05

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.Launch
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.automirrored.filled.SendAndArchive
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import es.lasalle.videoconferencia.s05.ui.ExplicitIntentScreen
import es.lasalle.videoconferencia.s05.ui.ImplicitIntentScreen
import es.lasalle.videoconferencia.s05.ui.IntentReceiverScreen
import es.lasalle.videoconferencia.ui.theme.Dimensions

sealed class S05Routes(val route: String) {
    object Menu : S05Routes("s05_menu")
    object ExplicitIntents : S05Routes("explicit_intents")
    object ImplicitIntents : S05Routes("implicit_intents")
    object IntentReceivers : S05Routes("intent_receivers")
}

@Composable
fun S05Demo(onNavigateBack: () -> Unit) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = S05Routes.Menu.route
    ) {
        composable(S05Routes.Menu.route) {
            S05MenuScreen(
                onNavigateBack = onNavigateBack,
                onNavigateToExplicit = { navController.navigate(S05Routes.ExplicitIntents.route) },
                onNavigateToImplicit = { navController.navigate(S05Routes.ImplicitIntents.route) },
                onNavigateToReceivers = { navController.navigate(S05Routes.IntentReceivers.route) }
            )
        }

        composable(S05Routes.ExplicitIntents.route) {
            ExplicitIntentScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(S05Routes.ImplicitIntents.route) {
            ImplicitIntentScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(S05Routes.IntentReceivers.route) {
            IntentReceiverScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun S05MenuScreen(
    onNavigateBack: () -> Unit,
    onNavigateToExplicit: () -> Unit,
    onNavigateToImplicit: () -> Unit,
    onNavigateToReceivers: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("S05 - Intent Demonstrations") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer
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
            S05Header()

            DemoOption(
                title = "Explicit Intents",
                description = "Navegación entre Activities de nuestra propia aplicación con intercambio de datos",
                icon = Icons.AutoMirrored.Filled.Launch,
                color = MaterialTheme.colorScheme.primary,
                onClick = onNavigateToExplicit
            )

            DemoOption(
                title = "Implicit Intents",
                description = "Interacción con aplicaciones externas: cámara, contactos, maps, web, etc.",
                icon = Icons.AutoMirrored.Filled.OpenInNew,
                color = MaterialTheme.colorScheme.secondary,
                onClick = onNavigateToImplicit
            )

            DemoOption(
                title = "Intent Receivers",
                description = "Configura tu app para recibir intents de otras aplicaciones usando Intent Filters",
                icon = Icons.Default.CallReceived,
                color = MaterialTheme.colorScheme.tertiary,
                onClick = onNavigateToReceivers
            )

            Spacer(modifier = Modifier.height(Dimensions.spaceLarge))
        }
    }
}

@Composable
private fun S05Header() {
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
                imageVector = Icons.AutoMirrored.Filled.SendAndArchive,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.primary
            )

            Text(
                text = "Intent Demonstrations",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )

            Text(
                text = "Aprende a utilizar Intents explícitos e implícitos para navegar y comunicarte con otras aplicaciones",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun DemoOption(
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

            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}