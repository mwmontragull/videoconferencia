package es.lasalle.videoconferencia

import android.app.Application
import coil3.ImageLoader
import coil3.SingletonImageLoader
import coil3.network.okhttp.OkHttpNetworkFetcherFactory

// =====================================
// 📱 APPLICATION CLASS - INICIALIZACIÓN DE LA APP
// =====================================

/**
 * 🎯 VideoconferenciaApplication - Clase Application personalizada
 * 
 * 📖 CONCEPTOS PEDAGÓGICOS:
 * 
 * 🏗️ APPLICATION CLASS:
 * - Entry point de la aplicación Android
 * - Se ejecuta antes que cualquier Activity
 * - Perfecto para inicializaciones globales
 * - Singleton durante toda la vida de la app
 * 
 * 🖼️ COIL INITIALIZATION:
 * - Coil 3.x requiere inicialización explícita
 * - SingletonImageLoader: Una instancia global
 * - OkHttpClient: Cliente HTTP configurado
 * - NetworkFetcherFactory: Para descargas de red
 * 
 * 💡 ¿POR QUÉ ES NECESARIO?:
 * - SubcomposeAsyncImage necesita ImageLoader inicializado
 * - Sin inicialización → Loading infinito
 * - Con inicialización → Imágenes se cargan correctamente
 * 
 * 🔧 CONFIGURACIÓN INCLUIDA:
 * - Timeouts razonables para conexiones
 * - OkHttp integration para networking
 * - Error handling automático
 * - Cache automática de imágenes
 */
class VideoconferenciaApplication : Application() {
    
    override fun onCreate() {
        super.onCreate()
        
        // 🖼️ Inicializar Coil para carga de imágenes
        initializeCoil()
    }
    
    /**
     * 🚀 initializeCoil - Configuración de Coil ImageLoader
     * 
     * 📖 CONFIGURACIÓN SIMPLIFICADA PARA COIL 3.X:
     * 
     * 🎨 IMAGE LOADING:
     * - ImageLoader por defecto con configuración estándar
     * - Cache en memoria automática
     * - Error handling automático
     * - Soporte nativo para HTTPS (Rick and Morty API)
     * 
     * 💡 EDUCATIONAL NOTE:
     * Esta inicialización básica soluciona el problema de que SubcomposeAsyncImage
     * se quedaba en loading infinito porque Coil no estaba inicializado.
     * 
     * Coil 3.x simplifica mucho la configuración - la configuración por defecto
     * es suficiente para la mayoría de casos de uso.
     */
    private fun initializeCoil() {
        // 🖼️ Establecer factory para ImageLoader global con NetworkFetcher
        SingletonImageLoader.setSafe { context ->
            ImageLoader.Builder(context)
                .components {
                    // ✅ CRÍTICO: Agregar soporte para HTTP/HTTPS
                    add(OkHttpNetworkFetcherFactory())
                }
                .build()
        }
    }
}