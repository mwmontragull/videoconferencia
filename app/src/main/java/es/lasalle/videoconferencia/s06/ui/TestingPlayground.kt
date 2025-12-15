package es.lasalle.videoconferencia.s06.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import es.lasalle.videoconferencia.s02.CounterDemo
import es.lasalle.videoconferencia.s02.DemoChip
import es.lasalle.videoconferencia.s02.TaskCard
import es.lasalle.videoconferencia.ui.theme.Dimensions

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TestingPlayground(
    onNavigateBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Compose UI Testing") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(Dimensions.spaceMedium)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(Dimensions.spaceLarge)
        ) {
            TestingIntroSection()
            TestableComponentsSection()
        }
    }
}

@Composable
private fun TestingIntroSection() {
    Card(
        modifier = Modifier.fillMaxWidth()
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
                    text = "¿Qué es UI Testing?",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }

            Text(
                text = "Los tests de UI verifican que tu aplicación funciona correctamente desde la perspectiva del usuario. " +
                        "Simulan clicks, escritura de texto y verifican que los elementos aparezcan como esperamos.",
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                text = "Componentes testeables:",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Medium
            )

            Text(
                text = "• Botones y clicks\n• Campos de texto\n• Navegación\n• Estados dinámicos\n• Listas y elementos",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun TestableComponentsSection() {
    var counter by remember { mutableIntStateOf(0) }
    var taskShared by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(Dimensions.spaceMedium),
            verticalArrangement = Arrangement.spacedBy(Dimensions.spaceMedium)
        ) {
            Text(
                text = "Componentes para Testear",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Estos componentes incluyen testTag para facilitar los tests:",
                style = MaterialTheme.typography.bodyMedium
            )

            TestableSection(
                title = "DemoChip Component",
                description = "Chip simple con click"
            ) {
                DemoChip(
                    text = "Test Chip",
                    modifier = Modifier.testTag("demo_chip")
                )
            }

            TestableSection(
                title = "Counter Component",
                description = "Botones con estado que cambia"
            ) {
                CounterDemo(
                    quantity1 = counter,
                    onValueChange = { counter = it.coerceAtLeast(0) }
                )
            }

            TestableSection(
                title = "TaskCard Component",
                description = "Card con acciones (compartir/eliminar)"
            ) {
                TaskCard(
                    title = "Tarea de ejemplo",
                    onRemove = { /* Test remove action */ },
                    onShare = { taskShared = true },
                    modifier = Modifier.testTag("task_card")
                )

                if (taskShared) {
                    Text(
                        text = "✅ Tarea compartida",
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.testTag("share_confirmation")
                    )
                }
            }

            TestInfoCard()
        }
    }
}

@Composable
private fun TestableSection(
    title: String,
    description: String,
    content: @Composable () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(Dimensions.spaceSmall)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Medium
        )
        Text(
            text = description,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        content()
    }
}

@Composable
private fun TestInfoCard() {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
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
                    imageVector = Icons.Default.Code,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Text(
                    text = "Tests Implementados",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    fontWeight = FontWeight.Bold
                )
            }

            Text(
                text = "Revisa la carpeta androidTest/s06/ para ver ejemplos de tests reales que verifican estos componentes.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )

            Text(
                text = "Ejecuta: ./gradlew connectedAndroidTest",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}