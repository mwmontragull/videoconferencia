package es.lasalle.videoconferencia.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

/**
 * 🎨 SISTEMA DE THEMING LA SALLE - Material Design 3 Implementation
 * 
 * 📖 CONCEPTOS PEDAGÓGICOS FUNDAMENTALES:
 * 
 * 🎯 MAPA MENTAL - ARQUITECTURA DE THEMING:
 * 
 *         MaterialTheme
 *              │
 *    ┌─────────┼─────────┐
 *    │         │         │
 * ColorScheme Typography Shapes
 * (Paleta)   (Texto)   (Formas)
 *    │         │         │
 * ┌──┴──┐   ┌──┴──┐   ┌──┴──┐
 * Light Dark Scale   Standard
 * Mode  Mode Hierarchy Custom
 * 
 * 🔄 SISTEMA DE TRES PILARES:
 * 
 * 1️⃣ COLORSCHEME: Define toda la paleta visual
 *    • Primary, Secondary, Tertiary + variants
 *    • Surface, Background, Error + on-colors
 *    • Automáticamente contrasta para legibilidad
 * 
 * 2️⃣ TYPOGRAPHY: Sistema jerárquico de texto
 *    • Display, Headline, Title, Body, Label
 *    • Cada uno con Large, Medium, Small
 *    • Optimizado para legibilidad y jerarquía
 * 
 * 3️⃣ SHAPES: Sistema consistente de formas
 *    • ExtraSmall → ExtraLarge (4dp → 28dp)
 *    • Herencia automática por componentes
 *    • Consistencia visual en toda la app
 * 
 * 🌓 DARK/LIGHT MODE AUTOMÁTICO:
 * - isSystemInDarkTheme(): Detecta preferencia del sistema
 * - Esquemas de color adaptativos automáticamente
 * - Status bar se ajusta dinámicamente
 * - Sin intervención manual del desarrollador
 * 
 * 🎨 DYNAMIC COLOR (ANDROID 12+):
 * - Extrae colores del wallpaper del usuario
 * - Personalización automática por device
 * - Fallback a La Salle colors si no está disponible
 * - Configurableactivado/desactivado por parámetro
 */

/**
 * 🌞 LA SALLE LIGHT COLOR SCHEME - Modo claro
 * 
 * 📖 QUÉ REPRESENTA:
 * ColorScheme completo para modo claro usando colores La Salle
 * 
 * 🎨 CONSTRUCCIÓN DEL SCHEME:
 * - Importa todos los colores desde Color.kt
 * - Mapea semánticamente: primary, secondary, tertiary
 * - Incluye variants y on-colors para contraste automático
 * - Define surfaces, backgrounds y error states
 * 
 * 🔧 FUNCIONALIDAD CLAVE:
 * • lightColorScheme(): Factory function de Material 3
 * • Todos los roles semánticos definidos explícitamente
 * • Automáticamente calcula contrastes legibles
 * • Base para todo el theming en modo claro
 */
// La Salle Light Color Scheme
private val LaSalleLightColorScheme = lightColorScheme(
    primary = LightPrimary,
    onPrimary = LightOnPrimary,
    primaryContainer = LightPrimaryContainer,
    onPrimaryContainer = LightOnPrimaryContainer,
    
    secondary = LightSecondary,
    onSecondary = LightOnSecondary,
    secondaryContainer = LightSecondaryContainer,
    onSecondaryContainer = LightOnSecondaryContainer,
    
    tertiary = LightTertiary,
    onTertiary = LightOnTertiary,
    tertiaryContainer = LightTertiaryContainer,
    onTertiaryContainer = LightOnTertiaryContainer,
    
    error = LightError,
    onError = LightOnError,
    errorContainer = LightErrorContainer,
    onErrorContainer = LightOnErrorContainer,
    
    background = LightBackground,
    onBackground = LightOnBackground,
    surface = LightSurface,
    onSurface = LightOnSurface,
    surfaceVariant = LightSurfaceVariant,
    onSurfaceVariant = LightOnSurfaceVariant,
    outline = LightOutline,
    outlineVariant = LightOutlineVariant
)

/**
 * 🌙 LA SALLE DARK COLOR SCHEME - Modo oscuro
 * 
 * 📖 QUÉ REPRESENTA:
 * ColorScheme completo para modo oscuro con inversión inteligente
 * 
 * 🔄 TRANSFORMACIÓN ESTRATÉGICA:
 * - Colores primarios se vuelven más claros pero conservan identidad
 * - Backgrounds cambian a tonos oscuros (#121212 base)
 * - On-colors se ajustan para mantener contraste óptimo
 * - Surfaces usan overlays para simular elevación
 * 
 * ⚡ BENEFICIOS DEL DARK MODE:
 * • Reduce fatiga visual en condiciones de poca luz
 * • Ahorra batería en pantallas OLED/AMOLED
 * • Mejora legibilidad para algunos usuarios
 * • Experiencia moderna y elegante
 * 
 * 🎨 TÉCNICA DE INVERSIÓN:
 * No es simplemente invertir colores, sino adaptar
 * inteligentemente para mantener usabilidad y estética
 */
// La Salle Dark Color Scheme
private val LaSalleDarkColorScheme = darkColorScheme(
    primary = DarkPrimary,
    onPrimary = DarkOnPrimary,
    primaryContainer = DarkPrimaryContainer,
    onPrimaryContainer = DarkOnPrimaryContainer,
    
    secondary = DarkSecondary,
    onSecondary = DarkOnSecondary,
    secondaryContainer = DarkSecondaryContainer,
    onSecondaryContainer = DarkOnSecondaryContainer,
    
    tertiary = DarkTertiary,
    onTertiary = DarkOnTertiary,
    tertiaryContainer = DarkTertiaryContainer,
    onTertiaryContainer = DarkOnTertiaryContainer,
    
    error = DarkError,
    onError = DarkOnError,
    errorContainer = DarkErrorContainer,
    onErrorContainer = DarkOnErrorContainer,
    
    background = DarkBackground,
    onBackground = DarkOnBackground,
    surface = DarkSurface,
    onSurface = DarkOnSurface,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = DarkOnSurfaceVariant,
    outline = DarkOutline,
    outlineVariant = DarkOutlineVariant
)

/**
 * 🎯 DEMOIS02THEME - Composable principal del sistema de theming
 * 
 * 📖 QUÉ HACE:
 * Configurador principal que orquesta todo el sistema de theming
 * 
 * 🧠 CONCEPTOS CLAVE - THEME PROVIDER:
 * - Actúa como "provider" de theme para toda la app
 * - Encapsula la lógica de selección dark/light
 * - Maneja dynamic colors de Android 12+
 * - Proporciona MaterialTheme completo a children
 * 
 * 🔧 PARÁMETROS INTELIGENTES:
 * 
 * darkTheme: Boolean = isSystemInDarkTheme()
 *   ✅ Detecta automáticamente preferencia del sistema
 *   ✅ Permite override manual si se necesita
 *   ✅ Respeta configuración de accesibilidad
 * 
 * dynamicColor: Boolean = false
 *   ✅ Disabled por defecto para mostrar tema La Salle
 *   ✅ Cuando true: usa colores del wallpaper (Android 12+)
 *   ✅ Fallback automático a La Salle en versiones anteriores
 * 
 * content: @Composable () -> Unit
 *   ✅ Children componentes que heredarán el theme
 *   ✅ Patrón estándar de Compose providers
 * 
 * 🎨 FLUJO DE DECISIÓN:
 * 1. ¿Dynamic color habilitado Y Android 12+? → Dynamic colors
 * 2. ¿Dark theme? → LaSalleDarkColorScheme
 * 3. Default → LaSalleLightColorScheme
 * 
 * 💡 PATRÓN DE USO:
 * ```kotlin
 * DemoS02Theme {
 *     // Toda la app hereda automáticamente colores, typography, shapes
 *     MyScreenContent()
 * }
 * ```
 */
@Composable
fun VideoconferenciaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false, // Disabled by default to showcase custom theme
    content: @Composable () -> Unit
) {
    /**
     * 🎨 LÓGICA DE SELECCIÓN DE COLOR SCHEME
     * 
     * 🔄 CASCADA DE DECISIONES:
     * 
     * 1️⃣ DYNAMIC COLOR PATH (Android 12+):
     *    - Requiere: dynamicColor = true AND Android S (API 31)+
     *    - dynamicDarkColorScheme()/dynamicLightColorScheme()
     *    - Extrae colores del wallpaper automáticamente
     *    - Material You personalización por usuario
     * 
     * 2️⃣ DARK THEME PATH:
     *    - Fallback cuando dynamic no disponible/deshabilitado
     *    - LaSalleDarkColorScheme con colores dark optimizados
     *    - Colores más claros, backgrounds oscuros
     * 
     * 3️⃣ LIGHT THEME PATH (Default):
     *    - Caso por defecto
     *    - LaSalleLightColorScheme con branding La Salle
     *    - Colores corporativos, máximo contrast
     */
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> LaSalleDarkColorScheme
        else -> LaSalleLightColorScheme
    }

    /**
     * 🏗️ MATERIALTHEME COMPOSITION - El corazón del theming
     * 
     * 📖 QUÉ SUCEDE AQUÍ:
     * MaterialTheme es el "provider" que inyecta theme en todo el árbol
     * 
     * 🎯 TRES PILARES UNIDOS:
     * 
     * colorScheme: ColorScheme calculado arriba
     *   ✅ Colores para todos los componentes Material
     *   ✅ Automáticamente aplicado a Button, Card, Surface, etc.
     *   ✅ Accesible vía MaterialTheme.colorScheme
     * 
     * typography: LaSalleTypography (desde Type.kt)
     *   ✅ Escala completa: Display, Headline, Title, Body, Label
     *   ✅ Cada estilo optimizado para su rol semántico
     *   ✅ Accesible vía MaterialTheme.typography
     * 
     * shapes: LaSalleShapes (desde Shape.kt)
     *   ✅ Sistema de corner radius: 4dp → 28dp
     *   ✅ Herencia automática por componentes
     *   ✅ Accesible vía MaterialTheme.shapes
     * 
     * content: @Composable () -> Unit
     *   ✅ Children que heredarán automáticamente todo el theme
     *   ✅ No necesitan configurar colores/typography manualmente
     * 
     * 💡 MAGIA DE LA HERENCIA:
     * Una vez configurado aquí, TODOS los componentes Material
     * en content{} heredan automáticamente estos valores:
     * 
     * ```kotlin
     * Button { } // Usa MaterialTheme.shapes.small automáticamente
     * Text(style = MaterialTheme.typography.bodyLarge) // Usa LaSalleTypography
     * Surface(color = MaterialTheme.colorScheme.surface) // Usa colorScheme seleccionado
     * ```
     */
    MaterialTheme(
        colorScheme = colorScheme,
        typography = LaSalleTypography,
        shapes = LaSalleShapes,
        content = content
    )
}