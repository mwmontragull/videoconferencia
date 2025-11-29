package es.lasalle.videoconferencia.s05.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import es.lasalle.videoconferencia.ui.theme.Dimensions
import es.lasalle.videoconferencia.ui.theme.VideoconferenciaTheme

class IntentReceiverActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val intentAction = intent.action
        val intentType = intent.type
        val intentData = intent.data
        val intentExtras = intent.extras

        var sharedText = ""
        var sharedSubject = ""
        var receivedUri: Uri? = null

        when (intentAction) {
            Intent.ACTION_SEND -> {
                if ("text/plain" == intentType) {
                    sharedText = intent.getStringExtra(Intent.EXTRA_TEXT) ?: ""
                    sharedSubject = intent.getStringExtra(Intent.EXTRA_SUBJECT) ?: ""
                }
            }
            Intent.ACTION_VIEW -> {
                receivedUri = intentData
            }
        }

        setContent {
            VideoconferenciaTheme {
                IntentReceiverScreen(
                    intentAction = intentAction,
                    intentType = intentType,
                    sharedText = sharedText,
                    sharedSubject = sharedSubject,
                    receivedUri = receivedUri,
                    onFinish = { finish() }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun IntentReceiverScreen(
    intentAction: String?,
    intentType: String?,
    sharedText: String,
    sharedSubject: String,
    receivedUri: Uri?,
    onFinish: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Intent Recibido") },
                navigationIcon = {
                    IconButton(onClick = onFinish) {
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
            IntentReceiverHeader()

            intentAction?.let { action ->
                IntentInfoCard(
                    title = "Acción del Intent",
                    value = action,
                    icon = Icons.Default.PlayArrow,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            intentType?.let { type ->
                IntentInfoCard(
                    title = "Tipo de Contenido",
                    value = type,
                    icon = Icons.Default.Category,
                    color = MaterialTheme.colorScheme.secondary
                )
            }

            if (sharedText.isNotEmpty()) {
                IntentInfoCard(
                    title = "Texto Compartido",
                    value = sharedText,
                    icon = Icons.Default.Share,
                    color = MaterialTheme.colorScheme.tertiary
                )
            }

            if (sharedSubject.isNotEmpty()) {
                IntentInfoCard(
                    title = "Asunto",
                    value = sharedSubject,
                    icon = Icons.Default.Subject,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            receivedUri?.let { uri ->
                IntentInfoCard(
                    title = "URI Recibida",
                    value = uri.toString(),
                    icon = Icons.Default.Link,
                    color = MaterialTheme.colorScheme.secondary
                )

                if (uri.host == "www.lasalle.es") {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.tertiaryContainer
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
                                    imageVector = Icons.Default.School,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.tertiary
                                )
                                Text(
                                    text = "¡Dominio La Salle detectado!",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.onTertiaryContainer
                                )
                            }
                            Text(
                                text = "Has abierto un enlace del dominio www.lasalle.es que está configurado para abrir con nuestra aplicación.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onTertiaryContainer
                            )
                        }
                    }
                }
            }

            if (intentAction == null && intentType == null && receivedUri == null) {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(Dimensions.spaceMedium),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(Dimensions.spaceSmall)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.error,
                            modifier = Modifier.size(48.dp)
                        )
                        Text(
                            text = "No se recibió ningún Intent",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onErrorContainer
                        )
                        Text(
                            text = "Esta Activity fue abierta directamente sin un Intent específico.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onErrorContainer
                        )
                    }
                }
            }

            Button(
                onClick = onFinish,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.Close, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Cerrar")
            }

            Spacer(modifier = Modifier.height(Dimensions.spaceLarge))
        }
    }
}

@Composable
private fun IntentReceiverHeader() {
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
                imageVector = Icons.Default.CallReceived,
                contentDescription = null,
                modifier = Modifier.size(48.dp),
                tint = MaterialTheme.colorScheme.primary
            )

            Text(
                text = "Intent Receiver Demo",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Text(
                text = "Nuestra app ha recibido un Intent desde otra aplicación",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun IntentInfoCard(
    title: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: androidx.compose.ui.graphics.Color
) {
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
                    imageVector = icon,
                    contentDescription = null,
                    tint = color
                )
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }

            SelectionContainer {
                Text(
                    text = value,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}