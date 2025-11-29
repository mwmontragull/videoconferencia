package es.lasalle.videoconferencia.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

/**
 * ⭕ SISTEMA DE SHAPES LA SALLE - Material Design 3
 * 
 * 📖 CONCEPTOS PEDAGÓGICOS FUNDAMENTALES:
 * 
 * 🎯 MAPA MENTAL - JERARQUÍA DE FORMAS:
 * 
 *         Shape System
 *              │
 *    ┌─────────┼─────────┐
 *    │         │         │
 * Material   Custom   Component
 * (Standard) (La Salle) (Específico)
 *    │         │         │
 * ┌──┴──┐   ┌──┴──┐   ┌──┴──┐
 * XS S  M   Button   Dialog
 * L  XL     Card     FAB
 * 
 * 🔄 PRINCIPIOS DE CONSISTENCIA:
 * 
 * 1️⃣ PROGRESIÓN UNIFORME: 4dp → 8dp → 12dp → 16dp → 28dp
 *    • Cada nivel sube de forma predecible
 *    • Fácil de recordar y aplicar
 * 
 * 2️⃣ ROLES SEMÁNTICOS:
 *    • XS (4dp): Elementos muy pequeños (badges, dots)
 *    • S (8dp): Botones, chips pequeños
 *    • M (12dp): Cards, inputs estándar
 *    • L (16dp): Dialogs, componentes grandes
 *    • XL (28dp): Bottom sheets, elementos prominentes
 * 
 * 3️⃣ COHERENCIA VISUAL:
 *    • Misma familia de formas en toda la app
 *    • Balance entre suavidad y definición
 *    • Facilita reconocimiento de patrones
 * 
 * 💡 IMPACTO PSICOLÓGICO:
 * - Esquinas redondeadas → Amigable, moderno, accesible
 * - Esquinas cuadradas → Formal, técnico, tradicional
 * - Radius moderado → Balance perfecto para UI empresarial
 */

// La Salle Shape System - Custom rounded corners for different components
/**
 * 🏛️ SHAPES ESTÁNDAR MATERIAL 3 - Sistema base
 * 
 * 📏 ESCALA PROGRESIVA:
 * extraSmall → small → medium → large → extraLarge
 *    4dp    →  8dp  →  12dp   → 16dp  →   28dp
 * 
 * 🎯 HERENCIA AUTOMÁTICA:
 * Los componentes heredan automáticamente estos shapes:
 * - Button usa MaterialTheme.shapes.small (8dp)
 * - Card usa MaterialTheme.shapes.medium (12dp)
 * - Dialog usa MaterialTheme.shapes.large (16dp)
 */
val LaSalleShapes = Shapes(
    // Extra Small - for small elements like chips, badges
    extraSmall = RoundedCornerShape(4.dp),
    
    // Small - for buttons, cards with subtle rounding
    small = RoundedCornerShape(8.dp),
    
    // Medium - for cards, dialogs, standard components
    medium = RoundedCornerShape(12.dp),
    
    // Large - for bottom sheets, large cards
    large = RoundedCornerShape(16.dp),
    
    // Extra Large - for full-screen components
    extraLarge = RoundedCornerShape(28.dp)
)

/**
 * 🎨 CUSTOM SHAPES - Formas específicas de La Salle
 * 
 * 🔧 CUÁNDO USAR CUSTOM VS MATERIAL:
 * 
 * ✅ USA CustomShapes CUANDO:
 * - Necesitas override explícito del theme
 * - Quieres shapes asimétricas especiales
 * - Diseño requiere radius específico
 * 
 * ✅ USA MaterialTheme.shapes CUANDO:
 * - Quieres consistencia automática
 * - El componente hereda naturalmente
 * - Sigues convenciones estándar
 * 
 * 🎯 MAPA DE CASOS DE USO:
 * 
 *   button/card/dialog → Equivalentes a Material (explicit override)
 *   chip → Más redondeado (16dp vs 8dp) → Pill shape
 *   bottomSheet → Asimétrico → Solo top corners
 *   floatingActionButton → Menos redondeado que círculo completo
 * 
 * 💡 EJEMPLOS DE APLICACIÓN:
 * ```kotlin
 * // Uso automático (recomendado)
 * Card { } // Usa MaterialTheme.shapes.medium automáticamente
 * 
 * // Override explícito
 * Card(shape = CustomShapes.card) { } // Mismo resultado, más verboso
 * 
 * // Caso especial
 * Surface(shape = CustomShapes.chip) { } // Pill shape para chips
 * ```
 */
// Custom shapes for specific use cases
object CustomShapes {
    val button = RoundedCornerShape(8.dp)
    val card = RoundedCornerShape(12.dp)
    val chip = RoundedCornerShape(16.dp)
    val dialog = RoundedCornerShape(16.dp)
    val bottomSheet = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
    val floatingActionButton = RoundedCornerShape(16.dp)
    
    /**
     * 🔀 ASYMMETRIC SHAPES - Formas asimétricas especiales
     * 
     * 🎯 CASOS DE USO ESPECÍFICOS:
     * - topRounded: Headers pegados arriba, listas con dividers superiores
     * - bottomRounded: Footers, elementos al final de listas
     * - leftRounded: Navegación lateral, elementos alineados a la izquierda
     * - rightRounded: Paneles laterales, tooltips desde la derecha
     * 
     * 💡 EJEMPLO PRÁCTICO:
     * ```kotlin
     * // Card que solo redondea la parte superior
     * Card(shape = CustomShapes.topRounded) {
     *     // Contenido que conecta visualmente con elemento inferior
     * }
     * ```
     */
    // Asymmetric shapes for special components
    val topRounded = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)
    val bottomRounded = RoundedCornerShape(bottomStart = 12.dp, bottomEnd = 12.dp)
    val leftRounded = RoundedCornerShape(topStart = 12.dp, bottomStart = 12.dp)
    val rightRounded = RoundedCornerShape(topEnd = 12.dp, bottomEnd = 12.dp)
}