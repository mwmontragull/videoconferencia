package es.lasalle.videoconferencia.s04.data.remote.models

// =====================================
// 📄 API RESPONSE - RESPUESTA DE LA API CON PAGINACIÓN
// =====================================

/**
 * 📦 ApiResponse - Wrapper para respuestas paginadas de la API de Rick and Morty
 * 
 * 📖 ESTRUCTURA DE LA API:
 * La API de Rick and Morty envuelve todas las respuestas de listas
 * en un objeto que contiene metadatos de paginación y los resultados.
 * 
 * 🔢 EJEMPLO DE RESPUESTA JSON:
 * {
 *   "info": {
 *     "count": 826,
 *     "pages": 42,
 *     "next": "https://rickandmortyapi.com/api/character/?page=2",
 *     "prev": null
 *   },
 *   "results": [
 *     { /* Character 1 */ },
 *     { /* Character 2 */ },
 *     // ... hasta 20 personajes por página
 *   ]
 * }
 * 
 * 🧠 VENTAJAS DEL WRAPPER:
 * - Información de paginación junto con los datos
 * - Estructura consistente en toda la API
 * - Facilita implementación de infinite scroll o paginación
 * 
 * 💡 GENERIC TYPE:
 * Se usa como ApiResponse<ApiCharacter> para personajes,
 * pero puede reutilizarse para otros endpoints (locations, episodes)
 * 
 * @param info Metadatos de paginación
 * @param results Lista de resultados en la página actual
 */
data class ApiResponse(
    val info: ApiInfo,
    val results: List<ApiCharacter>
)

// =====================================
// ℹ️ API INFO - METADATOS DE PAGINACIÓN
// =====================================

/**
 * 📊 ApiInfo - Metadatos de paginación de la API
 * 
 * 📖 INFORMACIÓN DE PAGINACIÓN:
 * Contiene toda la información necesaria para implementar
 * navegación por páginas o infinite scrolling.
 * 
 * 🔢 ESTRUCTURA JSON:
 * {
 *   "count": 826,
 *   "pages": 42,
 *   "next": "https://rickandmortyapi.com/api/character/?page=2",
 *   "prev": null
 * }
 * 
 * 🔧 CAMPOS EXPLICADOS:
 * - count: Total de personajes en toda la API (826)
 * - pages: Total de páginas disponibles (42) 
 * - next: URL de la siguiente página (null si es la última)
 * - prev: URL de la página anterior (null si es la primera)
 * 
 * 💡 USO EN LA APP:
 * - count: Para mostrar "X de Y personajes"
 * - pages: Para progress indicators
 * - next/prev: Para navegación o detección de más páginas
 * 
 * @param count Total de elementos en toda la colección
 * @param pages Total de páginas disponibles
 * @param next URL de la siguiente página (null si no hay)
 * @param prev URL de la página anterior (null si no hay)
 */
data class ApiInfo(
    val count: Int,
    val pages: Int,
    val next: String?,
    val prev: String?
)