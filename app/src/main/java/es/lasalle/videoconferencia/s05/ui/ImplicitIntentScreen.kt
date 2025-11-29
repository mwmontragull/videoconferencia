package es.lasalle.videoconferencia.s05.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.ContactsContract
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import es.lasalle.videoconferencia.ui.theme.Dimensions
import androidx.core.net.toUri

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImplicitIntentScreen(onNavigateBack: () -> Unit) {
    val context = LocalContext.current
    var resultMessage by remember { mutableStateOf("No se ha realizado ninguna acción aún") }

    var urlToOpen by remember { mutableStateOf("https://www.lasalle.es") }
    var phoneNumber by remember { mutableStateOf("123456789") }
    var emailAddress by remember { mutableStateOf("info@lasalle.es") }
    var emailSubject by remember { mutableStateOf("Consulta desde la app") }
    var emailBody by remember { mutableStateOf("Hola, escribo desde la aplicación móvil...") }
    var shareText by remember { mutableStateOf("¡Echa un vistazo a esta increíble app!") }
    var mapLocation by remember { mutableStateOf("La Salle Barcelona") }

    var hasCameraPermission by remember {
        mutableStateOf(ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)
    }
    
    var hasContactsPermission by remember {
        mutableStateOf(ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED)
    }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasCameraPermission = isGranted
        if (isGranted) {
            resultMessage = "Permiso de cámara concedido"
        } else {
            resultMessage = "Permiso de cámara denegado. No se puede usar la cámara."
        }
    }

    val contactsPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasContactsPermission = isGranted
        if (isGranted) {
            resultMessage = "Permiso de contactos concedido"
        } else {
            resultMessage = "Permiso de contactos denegado. No se puede acceder a los contactos."
        }
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        if (bitmap != null) {
            resultMessage = "Foto capturada correctamente (${bitmap.width}x${bitmap.height})"
        } else {
            resultMessage = "Captura de foto cancelada"
        }
    }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            resultMessage = "Imagen seleccionada: $uri"
        } else {
            resultMessage = "Selección de imagen cancelada"
        }
    }

    val contactLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickContact()
    ) { uri ->
        if (uri != null) {
            resultMessage = "Contacto seleccionado: $uri"
        } else {
            resultMessage = "Selección de contacto cancelada"
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Implicit Intents") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    navigationIconContentColor = MaterialTheme.colorScheme.onSecondaryContainer
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
            ImplicitIntentHeader()

            IntentCard(
                title = "Navegador Web",
                icon = Icons.Default.Language,
                color = MaterialTheme.colorScheme.primary,
                content = {
                    OutlinedTextField(
                        value = urlToOpen,
                        onValueChange = { urlToOpen = it },
                        label = { Text("URL") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            ) {
                try {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(urlToOpen))
                    context.startActivity(intent)
                    resultMessage = "Abriendo navegador: $urlToOpen"
                } catch (e: Exception) {
                    resultMessage = "Error al abrir navegador: ${e.message}"
                }
            }

            IntentCard(
                title = "Llamada Telefónica",
                icon = Icons.Default.Phone,
                color = MaterialTheme.colorScheme.secondary,
                content = {
                    OutlinedTextField(
                        value = phoneNumber,
                        onValueChange = { phoneNumber = it },
                        label = { Text("Número de teléfono") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            ) {
                try {
                    val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phoneNumber"))
                    context.startActivity(intent)
                    resultMessage = "Abriendo marcador para: $phoneNumber"
                } catch (e: Exception) {
                    resultMessage = "Error al abrir marcador: ${e.message}"
                }
            }

            IntentCard(
                title = "Enviar Email",
                icon = Icons.Default.Email,
                color = MaterialTheme.colorScheme.tertiary,
                content = {
                    OutlinedTextField(
                        value = emailAddress,
                        onValueChange = { emailAddress = it },
                        label = { Text("Email destino") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = emailSubject,
                        onValueChange = { emailSubject = it },
                        label = { Text("Asunto") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = emailBody,
                        onValueChange = { emailBody = it },
                        label = { Text("Mensaje") },
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 3
                    )
                }
            ) {
                try {
                    val intent = Intent(Intent.ACTION_SENDTO).apply {
                        data = "mailto:".toUri()
                        putExtra(Intent.EXTRA_EMAIL, arrayOf(emailAddress))
                        putExtra(Intent.EXTRA_SUBJECT, emailSubject)
                        putExtra(Intent.EXTRA_TEXT, emailBody)
                    }
                    context.startActivity(intent)
                    resultMessage = "Abriendo cliente de email"
                } catch (e: Exception) {
                    resultMessage = "Error al abrir email: ${e.message}"
                }
            }

            IntentCard(
                title = "Google Maps",
                icon = Icons.Default.Map,
                color = MaterialTheme.colorScheme.primary,
                content = {
                    OutlinedTextField(
                        value = mapLocation,
                        onValueChange = { mapLocation = it },
                        label = { Text("Ubicación o dirección") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            ) {
                try {
                    val intent = Intent(Intent.ACTION_VIEW, "geo:0,0?q=$mapLocation".toUri())
                    context.startActivity(intent)
                    resultMessage = "Abriendo Maps para: $mapLocation"
                } catch (e: Exception) {
                    resultMessage = "Error al abrir Maps: ${e.message}"
                }
            }

            IntentCard(
                title = "Compartir Texto",
                icon = Icons.Default.Share,
                color = MaterialTheme.colorScheme.secondary,
                content = {
                    OutlinedTextField(
                        value = shareText,
                        onValueChange = { shareText = it },
                        label = { Text("Texto a compartir") },
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 3
                    )
                }
            ) {
                try {
                    val intent = Intent(Intent.ACTION_SEND).apply {
                        type = "text/plain"
                        putExtra(Intent.EXTRA_TEXT, shareText)
                    }
                    val chooser = Intent.createChooser(intent, "Compartir texto")
                    context.startActivity(chooser)
                    resultMessage = "Abriendo selector para compartir"
                } catch (e: Exception) {
                    resultMessage = "Error al compartir: ${e.message}"
                }
            }

            PermissionSection(
                title = "Gestión de Permisos",
                icon = Icons.Default.Security
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(Dimensions.spaceSmall)
            ) {
                PermissionButton(
                    text = "Cámara",
                    icon = Icons.Default.Camera,
                    hasPermission = hasCameraPermission,
                    onRequestPermission = { cameraPermissionLauncher.launch(Manifest.permission.CAMERA) },
                    onExecuteAction = {
                        cameraLauncher.launch(null)
                        resultMessage = "Abriendo cámara..."
                    },
                    modifier = Modifier.weight(1f)
                )

                Button(
                    onClick = {
                        galleryLauncher.launch("image/*")
                        resultMessage = "Abriendo galería..."
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.Photo, contentDescription = null)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Galería")
                }

                PermissionButton(
                    text = "Contactos",
                    icon = Icons.Default.Contacts,
                    hasPermission = hasContactsPermission,
                    onRequestPermission = { contactsPermissionLauncher.launch(Manifest.permission.READ_CONTACTS) },
                    onExecuteAction = {
                        contactLauncher.launch(null)
                        resultMessage = "Abriendo contactos..."
                    },
                    modifier = Modifier.weight(1f)
                )
            }

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
                            tint = MaterialTheme.colorScheme.tertiary
                        )
                        Text(
                            text = "Resultado de la acción",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Text(
                        text = resultMessage,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(Dimensions.spaceLarge))
        }
    }
}

@Composable
private fun ImplicitIntentHeader() {
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
                imageVector = Icons.Default.OpenInNew,
                contentDescription = null,
                modifier = Modifier.size(48.dp),
                tint = MaterialTheme.colorScheme.secondary
            )

            Text(
                text = "Implicit Intents Demo",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Text(
                text = "Interactúa con aplicaciones externas instaladas en el dispositivo",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun IntentCard(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: androidx.compose.ui.graphics.Color,
    content: @Composable ColumnScope.() -> Unit,
    onAction: () -> Unit
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

            content()

            Button(
                onClick = onAction,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.Launch, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Ejecutar")
            }
        }
    }
}

@Composable
private fun PermissionSection(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer
        )
    ) {
        Row(
            modifier = Modifier
                .padding(Dimensions.spaceMedium)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Dimensions.spaceSmall)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.tertiary
            )
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onTertiaryContainer
            )
        }
    }
}

@Composable
private fun PermissionButton(
    text: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    hasPermission: Boolean,
    onRequestPermission: () -> Unit,
    onExecuteAction: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (hasPermission) {
        Button(
            onClick = onExecuteAction,
            modifier = modifier
        ) {
            Icon(icon, contentDescription = null)
            Spacer(modifier = Modifier.width(4.dp))
            Text(text)
        }
    } else {
        OutlinedButton(
            onClick = onRequestPermission,
            modifier = modifier,
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = MaterialTheme.colorScheme.error
            )
        ) {
            Icon(Icons.Default.Warning, contentDescription = null)
            Spacer(modifier = Modifier.width(4.dp))
            Text("Pedir\n$text")
        }
    }
}