package es.lasalle.videoconferencia.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

/**
 * 🔤 SISTEMA TIPOGRÁFICO LA SALLE - Material Design 3
 * 
 * 📖 CONCEPTOS PEDAGÓGICOS FUNDAMENTALES:
 * 
 * 🎯 MAPA MENTAL - JERARQUÍA TIPOGRÁFICA M3:
 * 
 *           Typography Scale
 *                 │
 *        ┌────────┼────────┐
 *        │        │        │
 *    Display   Headline   Title
 *    (Grandes)  (Títulos) (Subtítulos)
 *        │        │        │
 *     ┌──┼──┐  ┌──┼──┐  ┌──┼──┐
 *     L  M  S  L  M  S  L  M  S
 *
 *           Body      Label
 *        (Contenido) (Botones/UI)
 *           │           │
 *        ┌──┼──┐     ┌──┼──┐
 *        L  M  S     L  M  S
 * 
 * 🎨 ROLES SEMÁNTICOS:
 * 
 * 1️⃣ DISPLAY: Headlines muy grandes, splash screens
 *    • Uso: Títulos principales de landing pages
 *    • Tamaño: 45-57sp, peso Light
 * 
 * 2️⃣ HEADLINE: Títulos importantes de secciones
 *    • Uso: Headers de páginas, títulos de cards grandes
 *    • Tamaño: 24-32sp, peso SemiBold
 * 
 * 3️⃣ TITLE: Subtítulos y headers de subsecciones
 *    • Uso: Títulos de dialogs, headers de listas
 *    • Tamaño: 14-22sp, peso Medium-SemiBold
 * 
 * 4️⃣ BODY: Contenido principal de texto
 *    • Uso: Párrafos, descripciiones, contenido regular
 *    • Tamaño: 12-16sp, peso Normal
 * 
 * 5️⃣ LABEL: Elementos de UI interactivos
 *    • Uso: Botones, tabs, chips, inputs
 *    • Tamaño: 11-14sp, peso Medium
 * 
 * 📏 PROPIEDADES CLAVE:
 * - fontSize: Tamaño del texto en sp (scale-independent pixels)
 * - fontWeight: Grosor (Light, Normal, Medium, SemiBold, Bold)
 * - lineHeight: Espacio entre líneas (afecta legibilidad)
 * - letterSpacing: Espacio entre caracteres (tracking)
 * 
 * 💡 BUENAS PRÁCTICAS:
 * - Usar la escala apropiada según el contexto
 * - Mantener contraste suficiente con el fondo
 * - Considerar accesibilidad (tamaño mínimo 12sp)
 * - Usar letterSpacing para mejorar legibilidad
 */

// La Salle Typography System - Based on Material 3 with custom adjustments
val LaSalleTypography = Typography(
    /**
     * 🏆 DISPLAY STYLES - Para headlines gigantes
     * 
     * 🎯 CUÁNDO USAR:
     * - Splash screens, landing pages
     * - Títulos principales muy destacados
     * - Números grandes (estadísticas, precios)
     * 
     * 📐 CARACTERÍSTICAS:
     * - FontWeight.Light: Elegante, no abruma
     * - Negative letterSpacing: Compacta el texto grande
     * - LineHeight generoso: Evita que se vea apretado
     * 
     * 💡 EJEMPLO DE USO:
     * Text("Bienvenido", style = MaterialTheme.typography.displayLarge)
     */
    // Display styles - for large headlines
    displayLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Light,
        fontSize = 57.sp,
        lineHeight = 64.sp,
        letterSpacing = (-0.25).sp
    ),
    displayMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Light,
        fontSize = 45.sp,
        lineHeight = 52.sp,
        letterSpacing = 0.sp
    ),
    displaySmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 36.sp,
        lineHeight = 44.sp,
        letterSpacing = 0.sp
    ),
    
    /**
     * 📰 HEADLINE STYLES - Títulos principales
     * 
     * 🎯 CUÁNDO USAR:
     * - Headers de páginas principales
     * - Títulos de secciones importantes
     * - Nombres de pantallas en TopAppBar
     * 
     * 📐 CARACTERÍSTICAS:
     * - FontWeight.SemiBold: Balance entre elegancia y presencia
     * - Sin letterSpacing: Lectura natural
     * - LineHeight calculado: Ratio 1.25 (32sp/40sp lineHeight)
     * 
     * 💡 JERARQUÍA DE USO:
     * headlineLarge → Título principal de la app/página
     * headlineMedium → Headers de secciones principales  
     * headlineSmall → Subtítulos importantes
     */
    // Headline styles - for titles and important text
    headlineLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.SemiBold,
        fontSize = 32.sp,
        lineHeight = 40.sp,
        letterSpacing = 0.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.SemiBold,
        fontSize = 28.sp,
        lineHeight = 36.sp,
        letterSpacing = 0.sp
    ),
    headlineSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.SemiBold,
        fontSize = 24.sp,
        lineHeight = 32.sp,
        letterSpacing = 0.sp
    ),
    
    /**
     * 🏷️ TITLE STYLES - Subtítulos y headers de sección
     * 
     * 🎯 CUÁNDO USAR:
     * - Títulos de cards y componentes
     * - Headers de listas y grupos
     * - Subtítulos en dialogs
     * 
     * 📐 EVOLUCIÓN DE PESO:
     * titleLarge → SemiBold (más prominente)
     * titleMedium/Small → Medium (más sutil)
     * 
     * 🔤 LETTERSPACING PROGRESIVO:
     * - titleLarge: 0.sp (sin espaciado)
     * - titleMedium: 0.15.sp (ligeramente espaciado)
     * - titleSmall: 0.1.sp (espaciado sutil)
     */
    // Title styles - for subtitles and section headers
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.SemiBold,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    titleMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.15.sp
    ),
    titleSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp
    ),
    
    /**
     * 📄 BODY STYLES - Contenido regular y párrafos
     * 
     * 🎯 EL CORAZÓN DEL CONTENIDO:
     * - bodyLarge: Párrafos principales, contenido importante
     * - bodyMedium: Texto regular, descripciones
     * - bodySmall: Texto secundario, captions, metadatos
     * 
     * 📐 OPTIMIZADO PARA LECTURA:
     * - FontWeight.Normal: No cansa la vista
     * - LineHeight 1.5 ratio: Espaciado cómodo para lectura
     * - LetterSpacing aumenta en tamaños pequeños
     * 
     * 💡 ACCESIBILIDAD:
     * bodySmall (12sp) es el tamaño mínimo recomendado
     * para mantener legibilidad en todas las edades
     */
    // Body styles - for regular content
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.25.sp
    ),
    bodySmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.4.sp
    ),
    
    /**
     * 🏷️ LABEL STYLES - Elementos interactivos de UI
     * 
     * 🎯 PARA COMPONENTES FUNCIONALES:
     * - labelLarge: Botones principales, tabs activos
     * - labelMedium: Botones secundarios, chips, badges
     * - labelSmall: Campos de texto, labels pequeños
     * 
     * 📐 DISEÑADO PARA INTERACCIÓN:
     * - FontWeight.Medium: Destaca sin ser agresivo
     * - LetterSpacing generoso: Mejora legibilidad en elementos pequeños
     * - LineHeight compacto: Conserva espacio en UI densa
     * 
     * 🎨 CONTEXTOS DE USO:
     * ```kotlin
     * Button { Text("Action", style = MaterialTheme.typography.labelLarge) }
     * Chip { Text("Filter", style = MaterialTheme.typography.labelMedium) }
     * TextField(label = { Text("Email", style = MaterialTheme.typography.labelSmall) })
     * ```
     */
    // Label styles - for buttons, inputs, captions
    labelLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp
    ),
    labelMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
)

