package es.lasalle.videoconferencia.s04.data.remote

import es.lasalle.videoconferencia.s04.data.remote.models.ApiCharacter
import es.lasalle.videoconferencia.s04.data.remote.models.ApiResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RickAndMortyApi {
    
    @GET("character")
    suspend fun getCharacters(
        @Query("page") page: Int = 1
    ): Response<ApiResponse>
    
    @GET("character/{id}")
    suspend fun getCharacterById(
        @Path("id") id: Int
    ): Response<ApiCharacter>
}

object ApiConstants {
    const val BASE_URL = "https://rickandmortyapi.com/api/"
}