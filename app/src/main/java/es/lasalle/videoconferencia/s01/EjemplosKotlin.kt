package es.lasalle.videoconferencia.s01

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import es.lasalle.videoconferencia.ui.theme.Dimensions
import es.lasalle.videoconferencia.ui.theme.VideoconferenciaTheme
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

// =====================================
// 1. VARIABLES Y TIPOS BÁSICOS
// =====================================

fun variablesYTipos() {
    // val = inmutable (preferir siempre)
    val nombre = "Alice"
    val edad = 25
    val altura = 1.75
    val esActivo = true
    
    // var = mutable (usar solo cuando sea necesario)
    var contador = 0
    contador++ // Podemos cambiar var
    
    // String templates
    println("$nombre tiene $edad años y mide $altura metros")
    println("¿Está activo? $esActivo")
}

// =====================================
// 2. NULL SAFETY
// =====================================

fun nullSafety() {
    // Variable que puede ser null
    var textoNulo: String? = null
    var textoSeguro: String = "Nunca null"
    
    // Safe call operator (?.)
    println("Longitud segura: ${textoNulo?.length}") // null, no crash
    
    // Elvis operator (?:)
    val longitud = textoNulo?.length ?: 0
    println("Longitud con default: $longitud")
    
    // Smart cast después de verificar null
    if (textoNulo != null) {
        println("Ahora es seguro: ${textoNulo.length}")
        println("Ahora: $textoSeguro")
    }
}

// =====================================
// 3. DATA CLASSES Y SEALED CLASSES
// =====================================

// Data class - genera automáticamente equals, hashCode, toString, copy
data class Usuario(
    var id: Long,
    val nombre: String,
    val email: String,
    val esActivo: Boolean = true // Parámetro por defecto
)

fun a() {
    val usuario1 = Usuario(1, "Alice", "alice@test.com")
    val b = usuario1.id
}

// Sealed class - para modelar estados
sealed class EstadoRed {
    object Cargando : EstadoRed()
    data class Success(val datos: String) : EstadoRed()
    data class Error(val mensaje: String) : EstadoRed()
}


fun aja(estado: String): String = when (estado) {
    "a" -> "b"
    else -> "z"
}

fun manejarEstado(estado: EstadoRed): String = when (estado) {
    is EstadoRed.Cargando -> "Cargando..."
    is EstadoRed.Success -> "Datos: ${estado.datos}"
    is EstadoRed.Error -> "Error: ${estado.mensaje}"
    // No necesita else - sealed class es exhaustiva
}


// =====================================
// 4. FUNCIONES Y EXTENSIONES
// =====================================

// Función con parámetros por defecto
fun saludar(
    nombre: String,
    saludo: String = "Hola",
    signo: String = "!"
): String = "$saludo, $nombre$signo"

// Extension functions
fun String.esEmail(): Boolean = contains("@") && contains(".")

fun List<Int>.promedio(): Double = if (this.isEmpty()) 0.0 else sum().toDouble() / this.size

// Función infix (solo para DSLs)
infix fun Int.multiplicadoPor(otro: Int): Int = this * otro

fun ejemplosFunciones() {
    // Parámetros nombrados
    println(saludar("Alice"))
    println(saludar("Bob", saludo = "Hi"))
    println(saludar("Charlie", signo = "."))
    println(saludar("Eve", saludo = "Hello", signo = "?"))
    
    // Extension functions
    println("alice@test.com".esEmail()) // true
    println(listOf(1, 2, 3, 4, 5).promedio()) // 3.0
    
    // Infix
    val resultado = 5 multiplicadoPor 3 // 15
    println("5 multiplicado por 3 = $resultado")
}

// =====================================
// 5. COLLECTIONS Y OPERACIONES
// =====================================

data class Producto(
    val id: Long,
    val nombre: String,
    val categoria: String,
    val precio: Double,
    val enStock: Boolean
)

fun operacionesColecciones() {
    val productos = listOf(
        Producto(1, "Laptop", "Electrónicos", 999.99, true),
        Producto(2, "Café", "Comida", 4.99, true),
        Producto(3, "Mesa", "Muebles", 199.99, false),
        Producto(4, "Ratón", "Electrónicos", 29.99, true),
        Producto(5, "Té", "Comida", 3.99, true)
    )
    
    // ==============================================
    // OPERACIONES BÁSICAS - Transforman colecciones
    // ==============================================
    
    // filter: Mantiene solo elementos que cumplan condición
    // Entrada: [Laptop(true), Café(true), Mesa(false), Ratón(true), Té(true)]
    // Salida: [Laptop, Café, Ratón, Té] - solo los que están en stock
    val enStock = productos.filter { it.enStock }
    
    // map: Transforma cada elemento a algo diferente
    // Entrada: [Laptop, Café, Mesa, Ratón, Té] (objetos Producto)
    // Salida: ["Laptop", "Café", "Mesa", "Ratón", "Té"] (solo nombres)
    val nombres = productos.map { it.nombre }
    
    // sumOf: Suma una propiedad de todos los elementos
    // Entrada: [999.99, 4.99, 199.99, 29.99, 3.99] (precios)
    // Salida: 1238.95 (suma total)
    val valorTotal = productos.sumOf { it.precio }
    
    // ==============================================
    // AGRUPACIÓN - Organizan datos por claves
    // ==============================================
    
    // groupBy: Agrupa elementos por una clave, crea Map<Clave, List<Elementos>>
    // Entrada: [Laptop(Electrónicos), Café(Comida), Mesa(Muebles), Ratón(Electrónicos), Té(Comida)]
    // Salida: {Electrónicos=[Laptop, Ratón], Comida=[Café, Té], Muebles=[Mesa]}
    val porCategoria = productos.groupBy { it.categoria }
    
    // associateBy: Crea Map<Clave, Elemento> usando una clave única
    // Entrada: [Producto(id=1), Producto(id=2), Producto(id=3), ...]
    // Salida: {1=Laptop, 2=Café, 3=Mesa, 4=Ratón, 5=Té}
    val idAProducto = productos.associateBy { it.id }
    
    // ==============================================
    // OPERACIONES COMPLEJAS - Cadena de transformaciones
    // ==============================================
    
    // Cadena: filter → filter → sortedByDescending → map
    // Paso 1: [Laptop, Ratón] (solo Electrónicos)
    // Paso 2: [Laptop] (solo > 50€)
    // Paso 3: [Laptop] (ordenado por precio desc)
    // Paso 4: ["Laptop"] (solo nombres)
    val electronicosCaros = productos
        .filter { it.categoria == "Electrónicos" }  // Solo electrónicos
        .filter { it.precio > 50.0 }                // Solo caros (>50€)
        .sortedByDescending { it.precio }           // Más caro primero
        .map { it.nombre }                          // Solo nombres
    
    // ==============================================
    // RESULTADOS PARA COMPARAR
    // ==============================================

    println("Productos originales: ${productos.size}")
    println("Productos en stock: ${enStock.size} de ${productos.size}")
    println("Nombres extraídos: $nombres")
    println("Valor total: ${"%.2f".format(valorTotal)}€")
    println("Electrónicos caros: $electronicosCaros")
    println("Agrupados por categoría:")
    porCategoria.forEach { (categoria, prods) ->
        println("  $categoria: ${prods.map { it.nombre }}")
    }
    println("Búsqueda por ID - Producto 3: ${idAProducto[3]?.nombre}")
}

// =====================================
// 6. SCOPE FUNCTIONS
// =====================================

data class Configuracion(
    var apiUrl: String = "",
    var timeout: Int = 30,
    var reintentos: Int = 3,
    var debug: Boolean = false
)

fun scopeFunctions() {
    // ========================================================
    // SCOPE FUNCTIONS - Funciones que operan en un contexto
    // ========================================================
    
    // ==============================================
    // LET - Para null safety y transformaciones
    // ==============================================
    // Uso: objeto?.let { } - solo ejecuta si NO es null
    // Retorna: el resultado del bloque
    // Parámetro: 'it' (el objeto)
    
    val configNula: Configuracion? = obtenerConfig()
    val resultado = configNula?.let { config ->
        // Solo se ejecuta si configNula NO es null
        // 'config' es el objeto sin null (smart cast)
        "API: ${config.apiUrl}, Timeout: ${config.timeout}s"
    } ?: "Config no disponible" // Si es null, usa este valor

    println("🔍 LET - Resultado: $resultado")
    
    // ==============================================
    // APPLY - Para configurar objetos
    // ==============================================
    // Uso: objeto.apply { } - configura propiedades
    // Retorna: el mismo objeto configurado
    // Contexto: 'this' (puedes omitir this.)
    
    val config = Configuracion().apply {
        // Dentro del bloque, 'this' es el objeto Configuracion
        // No necesitas escribir 'this.apiUrl', solo 'apiUrl'
        apiUrl = "https://api.ejemplo.com"  // this.apiUrl = ...
        timeout = 60                        // this.timeout = ...
        debug = true                        // this.debug = ...
        // apply RETORNA el objeto configurado
    }

    println("🔧 APPLY - Config creada: ${config.apiUrl}")
    
    // ==============================================
    // ALSO - Para efectos secundarios
    // ==============================================
    // Uso: objeto.also { } - haz algo extra, pero mantén el objeto
    // Retorna: el mismo objeto original
    // Parámetro: 'it' (el objeto)
    
    val configGuardada = config.also { cfg ->
        // Haz algo extra con 'cfg' (logging, guardado, etc.)
        println("🗂️ ALSO - Guardando config: ${cfg.apiUrl}")
        guardarEnArchivo(cfg)
        // also RETORNA el objeto original (config)
    }
    // configGuardada es exactamente igual a config
    
    // ==============================================
    // RUN - Para calcular algo del receptor
    // ==============================================
    // Uso: objeto.run { } - calcula algo usando el objeto
    // Retorna: el resultado del cálculo
    // Contexto: 'this' (el objeto)
    
    val esConfigValida = config.run {
        // 'this' es config, puedes acceder a sus propiedades directamente
        apiUrl.isNotEmpty() && timeout > 0  // this.apiUrl && this.timeout
        // run RETORNA el resultado del bloque (Boolean)
    }

    println("✅ RUN - Config válida: $esConfigValida")
    
    // ==============================================
    // WITH - Para operar en un objeto no-null
    // ==============================================
    // Uso: with(objeto) { } - opera en un objeto que sabes que no es null
    // Retorna: el resultado del bloque
    // Contexto: 'this' (el objeto pasado como parámetro)
    
    val resumen = with(config) {
        // 'this' es config
        "URL: $apiUrl, Timeout: ${timeout}s, Debug: $debug"
        // with RETORNA el resultado del bloque (String)
    }

    println("📋 WITH - Resumen: $resumen")
    
    // ==============================================
    // REGLAS DE USO - Cuándo usar cada una
    // ==============================================

    println("\n📚 REGLAS DE USO:")
    println("• let    → Para null safety y transformaciones")
    println("• apply  → Para configurar objetos (builder pattern)")
    println("• also   → Para logging, debugging, efectos secundarios")
    println("• run    → Para calcular algo usando el objeto")
    println("• with   → Para operar en objetos que sabes que no son null")
    
    // ==============================================
    // COMPARACIÓN PRÁCTICA
    // ==============================================
    
    val usuario: Usuario? = Usuario(1, "Alice", "alice@test.com")
    
    // let - transformación segura
    val mensaje = usuario?.let { "Hola ${it.nombre}" } ?: "No hay usuario"
    
    // apply - configuración
    val nuevoUsuario = Usuario(2, "", "").apply {
        // nombre = "Bob"  // Error: nombre es val, no var
        // email = "bob@test.com"  // Error: email es val, no var
    }
    
    // also - logging
    val usuarioLogged = usuario?.also { println("Usuario cargado: ${it.nombre}") }
    
    // run - validación
    val esValido = usuario?.run { nombre.isNotEmpty() && email.contains("@") } ?: false

    println("\n🔄 EJEMPLOS PRÁCTICOS:")
    println("Mensaje: $mensaje")
    println("Usuario válido: $esValido")
}

private fun obtenerConfig(): Configuracion? = Configuracion()
private fun guardarEnArchivo(config: Configuracion) { /* Lógica de guardado */ }

// =====================================
// 7. CORRUTINAS BÁSICAS
// =====================================

// Función suspend básica
suspend fun obtenerUsuario(id: Long): Usuario {
    delay(1000) // Simula llamada de red
    return Usuario(id, "Usuario $id", "usuario$id@ejemplo.com")
}

suspend fun obtenerPublicaciones(usuarioId: Long): List<String> {
    delay(800)
    return listOf("Post 1 de $usuarioId", "Post 2 de $usuarioId")
}

// Repositorio con corrutinas
class RepositorioUsuarios {
    // Sequential: toma ~1.8 segundos
    suspend fun obtenerUsuarioConPosts(id: Long): UsuarioConPosts {
        val usuario = obtenerUsuario(id)
        val posts = obtenerPublicaciones(id)
        return UsuarioConPosts(usuario, posts)
    }
    
    // Concurrent: toma ~1 segundo
    suspend fun obtenerUsuarioConPostsConcurrente(id: Long): UsuarioConPosts {
        return coroutineScope {
            val usuarioDeferred = async { obtenerUsuario(id) }
            val postsDeferred = async { obtenerPublicaciones(id) }

            val usuario = usuarioDeferred.await()
            val posts = postsDeferred.await()

            UsuarioConPosts(usuario, posts)
        }
    }
}

data class UsuarioConPosts(val usuario: Usuario, val posts: List<String>)

// =====================================
// 8. FLOW BÁSICO
// =====================================

// Cold Flow - se inicia cuando se colecta
fun numerosFlow(): Flow<Int> = flow {
    println("Flow iniciado")
    for (i in 1..5) {
        delay(1000)
        emit(i)
    }
}

// Hot Flow - StateFlow para estado de UI
data class EstadoUI(
    val cargando: Boolean = false,
    val usuarios: List<Usuario> = emptyList(),
    val error: String? = null
)

class ViewModelUsuarios {
    private val _estadoUI = MutableStateFlow(EstadoUI())
    val estadoUI: StateFlow<EstadoUI> = _estadoUI.asStateFlow()
    
    private val repositorio = RepositorioUsuarios()
    
    fun cargarUsuarios() {
        _estadoUI.value = _estadoUI.value.copy(cargando = true, error = null)
        
        // En una app real, usarías viewModelScope
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val usuarios = listOf(
                    obtenerUsuario(1),
                    obtenerUsuario(2),
                    obtenerUsuario(3)
                )
                _estadoUI.value = EstadoUI(cargando = false, usuarios = usuarios)
            } catch (e: Exception) {
                _estadoUI.value = EstadoUI(cargando = false, error = e.message)
            }
        }
    }
}

// =====================================
// 9. OPERADORES DE FLOW
// =====================================

suspend fun operadoresFlow() {
    // Operadores básicos
    val doblados = numerosFlow()
        .map { it * 2 }
        .filter { it > 4 }

    println("=== Números doblados y filtrados ===")
    doblados.collect { println("Doblado: $it") }
    
    // Operadores de tiempo
    val busqueda = flow {
        emit("a")
        delay(100)
        emit("ab")
        delay(100)
        emit("abc")
        delay(2000) // Delay largo
        emit("abcd")
    }

    println("\n=== Búsqueda con debounce ===")
    busqueda
        .debounce(500) // Espera 500ms de inactividad
        .distinctUntilChanged()
        .collect { consulta ->
            println("Buscando: $consulta")
        }
}

// =====================================
// 10. FUNCIÓN MAIN PARA PROBAR TODO
// =====================================

fun main() = runBlocking {
    println("=== 1. Variables y Tipos ===")
    variablesYTipos()

    println("\n=== 2. Null Safety ===")
    nullSafety()

    println("\n=== 3. Manejo de Estados ===")
    println(manejarEstado(EstadoRed.Cargando))
    println(manejarEstado(EstadoRed.Success("Datos importantes")))
    println(manejarEstado(EstadoRed.Error("Sin conexión")))

    println("\n=== 4. Funciones ===")
    ejemplosFunciones()

    println("\n=== 5. Collections ===")
    operacionesColecciones()

    println("\n=== 6. Scope Functions ===")
    scopeFunctions()

    println("\n=== 7. Corrutinas ===")
    val repo = RepositorioUsuarios()
    val inicio = System.currentTimeMillis()
    val resultado = repo.obtenerUsuarioConPostsConcurrente(1)
    val tiempo = System.currentTimeMillis() - inicio
    println("Resultado en ${tiempo}ms: ${resultado.usuario.nombre}")

    println("\n=== 8. Flow ===")
    val viewModel = ViewModelUsuarios()

    // Observar cambios de estado
    launch {
        viewModel.estadoUI.collect { estado ->
            println("Estado UI: cargando=${estado.cargando}, usuarios=${estado.usuarios.size}, error=${estado.error}")
        }
    }

    delay(100) // Dar tiempo al collector
    viewModel.cargarUsuarios()
    delay(4000) // Esperar a que termine

    println("\n=== 9. Operadores de Flow ===")
    operadoresFlow()
}

// =====================================
// 🎨 COMPOSABLE WRAPPER - PARA INTEGRACIÓN CON UI
// =====================================

/**
 * 📱 EjemplosKotlin - Wrapper Composable para mostrar ejemplos en UI
 * 
 * Este Composable proporciona una interfaz visual para los ejemplos de Kotlin,
 * mostrando el código ejecutado y sus resultados en una pantalla interactiva.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EjemplosKotlin(
    onNavigateBack: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        text = "S01 - Fundamentos de Kotlin",
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
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(Dimensions.spaceMedium),
            verticalArrangement = Arrangement.spacedBy(Dimensions.spaceSmall)
        ) {
            item {
                Card {
                    Column(
                        modifier = Modifier.padding(Dimensions.spaceMedium)
                    ) {
                        Text(
                            text = "📚 Ejemplos de Kotlin",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold
                        )
                        
                        Spacer(modifier = Modifier.height(Dimensions.spaceSmall))
                        
                        Text(
                            text = "Esta sección contiene ejemplos prácticos de Kotlin que demuestran:",
                            style = MaterialTheme.typography.bodyLarge
                        )
                        
                        Spacer(modifier = Modifier.height(Dimensions.spaceSmall))
                        
                        val conceptos = listOf(
                            "Variables y tipos básicos",
                            "Null safety y smart casting",
                            "Data classes y sealed classes",
                            "Funciones de scope",
                            "Colecciones y operaciones",
                            "Funciones suspend y corrutinas",
                            "Flow para programación reactiva",
                            "Operadores de transformación"
                        )
                        
                        conceptos.forEach { concepto ->
                            Text(
                                text = "• $concepto",
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.padding(start = Dimensions.spaceSmall)
                            )
                        }
                    }
                }
            }
            
            item {
                Card {
                    Column(
                        modifier = Modifier.padding(Dimensions.spaceMedium)
                    ) {
                        Text(
                            text = "🚀 Ejecutar Ejemplos",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.SemiBold
                        )
                        
                        Spacer(modifier = Modifier.height(Dimensions.spaceSmall))
                        
                        Text(
                            text = "Los ejemplos se ejecutan automáticamente en segundo plano y muestran los conceptos fundamentales de Kotlin utilizados en el desarrollo Android moderno.",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        
                        Spacer(modifier = Modifier.height(Dimensions.spaceMedium))
                        
                        Button(
                            onClick = {
                                // En un ejemplo real, aquí ejecutarías los ejemplos
                                // y mostrarías los resultados en la UI
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Ver Código de Ejemplos")
                        }
                    }
                }
            }
            
            item {
                Card {
                    Column(
                        modifier = Modifier.padding(Dimensions.spaceMedium)
                    ) {
                        Text(
                            text = "💡 Conceptos Clave",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.SemiBold
                        )
                        
                        Spacer(modifier = Modifier.height(Dimensions.spaceSmall))
                        
                        Text(
                            text = "Inmutabilidad",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = "Preferir 'val' sobre 'var' para variables inmutables, mejorando la seguridad y predictibilidad del código.",
                            style = MaterialTheme.typography.bodyMedium,
                            fontFamily = FontFamily.Monospace
                        )
                        
                        Spacer(modifier = Modifier.height(Dimensions.spaceSmall))
                        
                        Text(
                            text = "Null Safety",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = "Sistema de tipos que previene NullPointerException en tiempo de compilación usando '?' y operadores seguros.",
                            style = MaterialTheme.typography.bodyMedium,
                            fontFamily = FontFamily.Monospace
                        )
                        
                        Spacer(modifier = Modifier.height(Dimensions.spaceSmall))
                        
                        Text(
                            text = "Corrutinas",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = "Programación asíncrona ligera que simplifica el manejo de operaciones concurrentes y de red.",
                            style = MaterialTheme.typography.bodyMedium,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }
            }
        }
    }
}

// =====================================
// 🎨 PREVIEWS
// =====================================

@Preview(name = "Ejemplos Kotlin")
@Composable
private fun EjemplosKotlinPreview() {
    VideoconferenciaTheme {
        EjemplosKotlin()
    }
}

@Preview(
    name = "Ejemplos Kotlin - Dark Mode",
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun EjemplosKotlinDarkPreview() {
    VideoconferenciaTheme {
        EjemplosKotlin()
    }
}