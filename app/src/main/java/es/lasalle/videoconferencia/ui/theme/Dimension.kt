package es.lasalle.videoconferencia.ui.theme

import androidx.compose.ui.unit.dp

/**
 * 📏 SISTEMA DE DIMENSIONES LA SALLE - Espaciado y sizing consistente
 * 
 * 📖 CONCEPTOS PEDAGÓGICOS FUNDAMENTALES:
 * 
 * 🎯 MAPA MENTAL - SISTEMA DE SPACING:
 * 
 *         Design Tokens
 *              │
 *    ┌─────────┼─────────┐
 *    │         │         │
 *  Spacing   Sizing   Component
 *  (4dp base) (Accessibility) (Specific)
 *    │         │         │
 * ┌──┴──┐   ┌──┴──┐   ┌──┴──┐
 * XS S  M   Icons    Touch
 * L  XL     Buttons  Inputs
 * 
 * 🧮 BASE MATEMÁTICA - ESCALA 4DP:
 * 
 *   4dp → 8dp → 16dp → 24dp → 32dp → 48dp
 *   (Base) (2x) (4x)  (6x)  (8x)  (12x)
 * 
 * ¿Por qué 4dp?
 * - Divisible por 2 (diseño simétrico)
 * - Múltiplo de densidades de pantalla comunes
 * - Balance perfecto: no muy pequeño, no muy grande
 * - Estándar Material Design (Google research)
 * 
 * 🎨 PRINCIPIOS DE APLICACIÓN:
 * 
 * 1️⃣ JERARQUÍA VISUAL:
 *    - Más espacio = Mayor importancia
 *    - Elementos relacionados = Menos espacio entre ellos
 *    - Secciones diferentes = Más espacio entre ellas
 * 
 * 2️⃣ RESPIRACIÓN DEL DISEÑO:
 *    - spaceXSmall (4dp): Entre elementos muy relacionados
 *    - spaceSmall (8dp): Elementos del mismo grupo
 *    - spaceMedium (16dp): Entre grupos diferentes
 *    - spaceLarge (24dp): Entre secciones
 *    - spaceXLarge (32dp): Separación principal
 * 
 * 3️⃣ ACCESIBILIDAD INTEGRADA:
 *    - touchTargetMin (48dp): Tamaño mínimo para touch
 *    - Basado en investigación de ergonomía
 *    - Incluye usuarios con dificultades motoras
 */

// La Salle Dimension System - Consistent spacing and sizing
object Dimensions {
    /**
     * 🌬️ SPACING SYSTEM - La respiración del diseño
     * 
     * 🎯 GUÍA DE USO PRÁCTICA:
     * 
     * spaceXSmall (4dp):
     *   ✅ Entre icon y texto en botones
     *   ✅ Padding interno de chips pequeños
     *   ✅ Separación de elementos muy relacionados
     * 
     * spaceSmall (8dp):
     *   ✅ Padding interno de botones
     *   ✅ Entre elementos de una Row
     *   ✅ Margin pequeño entre componentes
     * 
     * spaceMedium (16dp):
     *   ✅ Padding principal de pantallas
     *   ✅ Entre elementos de diferentes grupos
     *   ✅ Margin estándar entre cards
     * 
     * spaceLarge (24dp):
     *   ✅ Entre secciones principales
     *   ✅ Padding de containers grandes
     *   ✅ Separación visual importante
     * 
     * spaceXLarge/XXLarge (32dp/48dp):
     *   ✅ Separación máxima entre secciones
     *   ✅ Padding de pantallas principales
     *   ✅ White space para emphasis
     * 
     * 💡 EJEMPLO DE APLICACIÓN:
     * ```kotlin
     * Column(
     *     modifier = Modifier.padding(Dimensions.spaceMedium),
     *     verticalArrangement = Arrangement.spacedBy(Dimensions.spaceSmall)
     * ) {
     *     // Elementos con spacing consistente
     * }
     * ```
     */
    // Spacing system - for padding, margins
    val spaceXSmall = 4.dp
    val spaceSmall = 8.dp
    val spaceMedium = 16.dp
    val spaceLarge = 24.dp
    val spaceXLarge = 32.dp
    val spaceXXLarge = 48.dp
    
    /**
     * 🎨 ICON SIZING - Iconos consistentes
     * 
     * 📐 ESCALA DE ICONOS:
     * 
     * iconSmall (16dp):
     *   ✅ Iconos en inputs pequeños
     *   ✅ Indicadores de estado
     *   ✅ Iconos decorativos
     * 
     * iconMedium (24dp):
     *   ✅ Iconos estándar en botones
     *   ✅ Navigation icons
     *   ✅ List item icons (default)
     * 
     * iconLarge (32dp):
     *   ✅ Iconos principales en headers
     *   ✅ Feature icons prominentes
     *   ✅ Avatar placeholders pequeños
     * 
     * iconXLarge (48dp):
     *   ✅ Iconos de splash screen
     *   ✅ Empty state illustrations
     *   ✅ Large avatar sizes
     * 
     * 💡 CONSISTENCIA:
     * Usar siempre estos tamaños predefinidos
     * en lugar de valores arbitrarios.
     */
    // Component sizes
    val iconSmall = 16.dp
    val iconMedium = 24.dp
    val iconLarge = 32.dp
    val iconXLarge = 48.dp
    
    /**
     * 🔘 BUTTON DIMENSIONS - Botones accesibles
     * 
     * 📏 ALTURAS ESTANDARIZADAS:
     * 
     * buttonHeightSmall (36dp):
     *   ✅ Botones secundarios en toolbars
     *   ✅ Inline actions en listas densas
     *   ✅ Chips interactivos
     * 
     * buttonHeight (48dp):
     *   ✅ Botones principales (DEFAULT)
     *   ✅ FABs estándar
     *   ✅ Touch target mínimo accesible
     * 
     * buttonHeightLarge (56dp):
     *   ✅ CTAs (Call-to-Action) prominentes
     *   ✅ Botones principales en formularios
     *   ✅ Extra prominence cuando se necesita
     * 
     * buttonMinWidth (64dp):
     *   ✅ Ancho mínimo para evitar botones muy estrechos
     *   ✅ Mantiene proporción visual adecuada
     *   ✅ Mejora usabilidad en touch
     */
    // Button dimensions
    val buttonHeight = 48.dp
    val buttonHeightSmall = 36.dp
    val buttonHeightLarge = 56.dp
    val buttonMinWidth = 64.dp
    
    /**
     * 📝 INPUT DIMENSIONS - Campos de entrada
     * 
     * 📋 ALTURAS OPTIMIZADAS:
     * 
     * inputHeight (56dp):
     *   ✅ TextFields estándar (Material 3)
     *   ✅ Dropdowns y selectors
     *   ✅ Altura cómoda para typing
     * 
     * inputHeightSmall (40dp):
     *   ✅ Inputs en formularios densos
     *   ✅ Search bars compactos
     *   ✅ Filters y opciones secundarias
     * 
     * 💡 ACCESIBILIDAD:
     * Nunca usar altura menor a 40dp para
     * mantener touch targets accesibles.
     */
    // Input field dimensions
    val inputHeight = 56.dp
    val inputHeightSmall = 40.dp
    
    /**
     * 📇 CARD ELEVATIONS - Profundidad visual
     * 
     * 🎭 ESTADOS DE ELEVACIÓN:
     * 
     * cardElevation (4dp):
     *   ✅ Estado normal de cards
     *   ✅ Sutil pero visible profundidad
     *   ✅ No distrae del contenido
     * 
     * cardElevationHovered (8dp):
     *   ✅ Hover state (desktop)
     *   ✅ Feedback visual de interactividad
     *   ✅ Indica clickeable
     * 
     * cardElevationPressed (1dp):
     *   ✅ Press state (mobile)
     *   ✅ Simula "hundir" el elemento
     *   ✅ Feedback táctil visual
     * 
     * 🎨 PROGRESIÓN LÓGICA:
     * Normal → Hover (aumenta) → Press (disminuye)
     * Simula física real de objetos.
     */
    // Card dimensions
    val cardElevation = 4.dp
    val cardElevationHovered = 8.dp
    val cardElevationPressed = 1.dp
    
    /**
     * 👆 TOUCH TARGETS - Accesibilidad universal
     * 
     * 🎯 CIENCIA DETRÁS DE 48DP:
     * 
     * ¿Por qué exactamente 48dp?
     * • Investigación de Google en usabilidad
     * • Promedio del tamaño de dedo humano: ~44dp
     * • 48dp incluye margen de error
     * • Funciona para todas las edades
     * • Cumple WCAG 2.1 AA guidelines
     * 
     * 🔬 RESPALDO CIENTÍFICO:
     * - MIT Touch Lab research
     * - Estudios de ergonomía digital
     * - Análisis de usuarios con discapacidades motoras
     * - Testing en múltiples dispositivos
     * 
     * ⚖️ REQUISITO LEGAL:
     * Muchas jurisdicciones requieren touch targets
     * mínimos para cumplir leyes de accesibilidad.
     * 
     * 💡 APLICACIÓN CORRECTA:
     * ```kotlin
     * IconButton(
     *     modifier = Modifier.sizeIn(
     *         minWidth = Dimensions.touchTargetMin,
     *         minHeight = Dimensions.touchTargetMin
     *     )
     * )
     * ```
     */
    // Touch targets (accessibility)
    val touchTargetMin = 48.dp
    
    /**
     * 📱 NAVIGATION COMPONENTS - Elementos de navegación
     * 
     * 🏗️ ARQUITECTURA DE NAVEGACIÓN:
     * 
     * appBarHeight (64dp):
     *   ✅ TopAppBar principal en tablets/desktop
     *   ✅ Headers principales con más contenido
     *   ✅ Mejor para pantallas grandes
     * 
     * appBarHeightSmall (56dp):
     *   ✅ TopAppBar estándar en móviles
     *   ✅ Conserva espacio vertical
     *   ✅ Default Material 3
     * 
     * bottomNavHeight (80dp):
     *   ✅ BottomNavigationBar estándar
     *   ✅ Incluye espacio para labels
     *   ✅ Touch targets cómodos
     */
    // App bar dimensions
    val appBarHeight = 64.dp
    val appBarHeightSmall = 56.dp
    
    // Bottom navigation
    val bottomNavHeight = 80.dp
    
    /**
     * 🔴 FAB SIZING - Floating Action Buttons
     * 
     * 🎯 PROMINENCIA PROGRESIVA:
     * 
     * fabSizeSmall (40dp):
     *   ✅ Mini FAB para acciones secundarias
     *   ✅ FABs múltiples en pantalla
     *   ✅ Contextos donde el FAB no es principal
     * 
     * fabSize (56dp):
     *   ✅ FAB estándar (DEFAULT)
     *   ✅ Acción principal de la pantalla
     *   ✅ Balance perfecto de prominencia
     * 
     * fabSizeLarge (96dp):
     *   ✅ Extended FAB con texto
     *   ✅ Extra prominence cuando se necesita
     *   ✅ Pantallas con mucho white space
     */
    // FAB dimensions
    val fabSize = 56.dp
    val fabSizeSmall = 40.dp
    val fabSizeLarge = 96.dp
    
    /**
     * ➖ DIVIDERS & BORDERS - Elementos de separación
     * 
     * 📏 GROSOR SEMÁNTICO:
     * 
     * dividerThickness (1dp):
     *   ✅ Separadores sutiles entre elementos
     *   ✅ No distrae del contenido
     *   ✅ Estándar Material Design
     * 
     * borderThin (1dp):
     *   ✅ Bordes de inputs sin focus
     *   ✅ Outlines sutiles
     *   ✅ Separadores mínimos
     * 
     * borderMedium (2dp):
     *   ✅ Bordes de focus en inputs
     *   ✅ Estados activos/selected
     *   ✅ Emphasis moderado
     * 
     * borderThick (4dp):
     *   ✅ Bordes de error/warning
     *   ✅ Estados de alta importancia
     *   ✅ Máximo emphasis sin ser agresivo
     * 
     * 💡 REGLA DE ORO:
     * Usar siempre el grosor mínimo que logre
     * el objetivo visual deseado.
     */
    // Divider
    val dividerThickness = 1.dp
    
    // Border widths
    val borderThin = 1.dp
    val borderMedium = 2.dp
    val borderThick = 4.dp
}