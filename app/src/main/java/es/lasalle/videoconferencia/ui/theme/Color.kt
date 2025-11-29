package es.lasalle.videoconferencia.ui.theme

import androidx.compose.ui.graphics.Color

/**
 * 🎨 SISTEMA DE COLORES LA SALLE - Material Design 3
 * 
 * 📖 CONCEPTOS PEDAGÓGICOS FUNDAMENTALES:
 * 
 * 🌈 MAPA MENTAL - JERARQUÍA DE COLORES M3:
 * 
 *         Brand Colors (La Salle)
 *              │
 *    ┌─────────┼─────────┐
 *    │         │         │
 * Primary   Secondary  Tertiary
 *    │         │         │
 * ┌──┴──┐   ┌──┴──┐   ┌──┴──┐
 * Light Dark Light Dark Light Dark
 * 
 * 🎯 ROLES SEMÁNTICOS:
 * 
 * 1️⃣ PRIMARY: Acciones principales, FABs, botones importantes
 *    • Ejemplo: Botón "Guardar", enlaces principales
 * 
 * 2️⃣ SECONDARY: Acciones secundarias, filtros, tabs
 *    • Ejemplo: Botón "Cancelar", chips de filtro
 * 
 * 3️⃣ TERTIARY: Acentos, elementos destacados
 *    • Ejemplo: Badges, elementos decorativos
 * 
 * 🔧 AUTOMATIC CONTRAST:
 * - onPrimary, onSecondary, onTertiary
 * - Garantizan legibilidad automática
 * - Cambian según light/dark mode
 * 
 * 💡 CONTAINERS:
 * - primaryContainer: Fondos suaves con primary
 * - secondaryContainer: Fondos suaves con secondary
 * - tertiaryContainer: Fondos suaves con tertiary
 */

// Brand Colors - La Salle Theme
val LaSallePrimary = Color(0xFF1976D2) // Deep Blue
val LaSallePrimaryVariant = Color(0xFF0D47A1) // Darker Blue
val LaSalleSecondary = Color(0xFF388E3C) // Green
val LaSalleSecondaryVariant = Color(0xFF1B5E20) // Dark Green
val LaSalleAccent = Color(0xFFFF5722) // Orange Red

/**
 * 🌞 LIGHT THEME - Colores para modo claro
 * 
 * 📋 PATRÓN DE NOMENCLATURA:
 * - Color base: LightPrimary, LightSecondary, etc.
 * - Texto sobre color: LightOnPrimary, LightOnSecondary
 * - Container: LightPrimaryContainer (fondo suave)
 * - Texto sobre container: LightOnPrimaryContainer
 * 
 * 🎨 MAPA DE CONTRASTE:
 * 
 *   Primary (azul intenso)     →  OnPrimary (blanco)
 *      ↓
 *   PrimaryContainer (azul claro) → OnPrimaryContainer (azul oscuro)
 * 
 * 💡 CUÁNDO USAR CADA UNO:
 * - Primary: Botones principales, FAB, enlaces activos
 * - PrimaryContainer: Fondos de chips, cards destacadas
 * - OnPrimary: Texto/iconos sobre Primary
 * - OnPrimaryContainer: Texto/iconos sobre PrimaryContainer
 */
// Light Theme Colors
val LightPrimary = LaSallePrimary
val LightOnPrimary = Color.White
val LightPrimaryContainer = Color(0xFFE3F2FD) // Light Blue
val LightOnPrimaryContainer = Color(0xFF0D47A1)

val LightSecondary = LaSalleSecondary
val LightOnSecondary = Color.White
val LightSecondaryContainer = Color(0xFFE8F5E8) // Light Green
val LightOnSecondaryContainer = Color(0xFF1B5E20)

val LightTertiary = LaSalleAccent
val LightOnTertiary = Color.White
val LightTertiaryContainer = Color(0xFFFFE8E0) // Light Orange
val LightOnTertiaryContainer = Color(0xFFBF360C)

val LightError = Color(0xFFD32F2F)
val LightOnError = Color.White
val LightErrorContainer = Color(0xFFFFCDD2)
val LightOnErrorContainer = Color(0xFFB71C1C)

/**
 * 🏢 SURFACE COLORS - Fondos y superficies
 * 
 * 📐 JERARQUÍA DE SUPERFICIES:
 * 
 *   Background (más profundo)
 *        ↑
 *   Surface (nivel base)
 *        ↑
 *   SurfaceVariant (nivel elevado)
 * 
 * 🎯 USOS COMUNES:
 * - Background: Fondo principal de pantallas
 * - Surface: Cards, dialogs, bottom sheets
 * - SurfaceVariant: Headers, dividers, elementos elevados
 */
val LightBackground = Color(0xFFFFFBFE)
val LightOnBackground = Color(0xFF1C1B1F)
val LightSurface = Color(0xFFFFFBFE)
val LightOnSurface = Color(0xFF1C1B1F)
val LightSurfaceVariant = Color(0xFFF3F4F6)
val LightOnSurfaceVariant = Color(0xFF6B7280)
val LightOutline = Color(0xFFD1D5DB)
val LightOutlineVariant = Color(0xFFE5E7EB)

/**
 * 🌙 DARK THEME - Colores para modo oscuro
 * 
 * 🔄 INVERSIÓN ESTRATÉGICA:
 * En dark mode, los colores se invierten inteligentemente:
 * - Primarios se vuelven más claros pero mantienen identidad
 * - Containers más oscuros pero visibles
 * - OnColors se ajustan para máximo contraste
 * 
 * 🎨 TRANSFORMACIÓN DE COLORES:
 * 
 *   LIGHT MODE          →    DARK MODE
 *   Primary (oscuro)    →    Primary (claro)
 *   Container (claro)   →    Container (medio)
 *   OnPrimary (claro)   →    OnPrimary (oscuro)
 * 
 * ⚡ BENEFICIOS:
 * - Reduce fatiga visual en entornos oscuros
 * - Ahorra batería en pantallas OLED
 * - Mejora legibilidad nocturna
 */
// Dark Theme Colors
val DarkPrimary = Color(0xFF90CAF9) // Light Blue
val DarkOnPrimary = Color(0xFF0D47A1)
val DarkPrimaryContainer = Color(0xFF1565C0)
val DarkOnPrimaryContainer = Color(0xFFE3F2FD)

val DarkSecondary = Color(0xFFA5D6A7) // Light Green
val DarkOnSecondary = Color(0xFF1B5E20)
val DarkSecondaryContainer = Color(0xFF2E7D32)
val DarkOnSecondaryContainer = Color(0xFFE8F5E8)

val DarkTertiary = Color(0xFFFFAB91) // Light Orange
val DarkOnTertiary = Color(0xFFBF360C)
val DarkTertiaryContainer = Color(0xFFE64A19)
val DarkOnTertiaryContainer = Color(0xFFFFE8E0)

val DarkError = Color(0xFFEF5350)
val DarkOnError = Color(0xFFB71C1C)
val DarkErrorContainer = Color(0xFFC62828)
val DarkOnErrorContainer = Color(0xFFFFCDD2)

/**
 * 🌃 DARK SURFACES - Fondos oscuros con profundidad
 * 
 * 🎭 ELEVACIÓN VISUAL:
 * - Background: Negro profundo (#121212)
 * - Surface: Gris oscuro (#1E1E1E) - ligeramente elevado
 * - SurfaceVariant: Gris medio (#2A2A2A) - más elevado
 * 
 * 🔧 TÉCNICA DE OVERLAYS:
 * Cada nivel de elevación añade un overlay blanco semi-transparente
 * sobre el negro base, creando la sensación de profundidad.
 */
val DarkBackground = Color(0xFF121212)
val DarkOnBackground = Color(0xFFE3E3E3)
val DarkSurface = Color(0xFF1E1E1E)
val DarkOnSurface = Color(0xFFE3E3E3)
val DarkSurfaceVariant = Color(0xFF2A2A2A)
val DarkOnSurfaceVariant = Color(0xFFB0B0B0)
val DarkOutline = Color(0xFF404040)
val DarkOutlineVariant = Color(0xFF2A2A2A)

/**
 * 🎯 COLORES SEMÁNTICOS PERSONALIZADOS
 * 
 * 📊 SISTEMA DE FEEDBACK VISUAL:
 * 
 *   SUCCESS (Verde) → Confirmación, éxito, completado
 *   WARNING (Naranja) → Advertencia, precaución, atención
 *   INFO (Azul) → Información, consejos, ayuda
 * 
 * 💡 EJEMPLO DE USO:
 * ```kotlin
 * Text(
 *     "Operation successful!", 
 *     color = if (isSystemInDarkTheme()) SuccessDark else SuccessLight
 * )
 * ```
 * 
 * 🎨 CONSISTENCIA CON M3:
 * Estos colores complementan el sistema principal pero no lo reemplazan.
 * Para estados críticos, usa siempre MaterialTheme.colorScheme.error
 */
// Custom semantic colors for demo purposes
val SuccessLight = Color(0xFF4CAF50)
val SuccessDark = Color(0xFF81C784)
val WarningLight = Color(0xFFFF9800)
val WarningDark = Color(0xFFFFB74D)
val InfoLight = Color(0xFF2196F3)
val InfoDark = Color(0xFF64B5F6)