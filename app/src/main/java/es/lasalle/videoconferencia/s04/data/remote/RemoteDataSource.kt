package es.lasalle.videoconferencia.s04.data.remote

import es.lasalle.videoconferencia.s04.data.remote.models.ApiCharacter
import es.lasalle.videoconferencia.s04.data.remote.models.ApiResponse
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RemoteDataSource {
    
    private val api: RickAndMortyApi by lazy {
        Retrofit.Builder()
            .baseUrl(ApiConstants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RickAndMortyApi::class.java)
    }
    
    suspend fun getCharacters(page: Int): Result<ApiResponse> {
        return try {
            val response: Response<ApiResponse> = api.getCharacters(page)
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    Result.success(body)
                } else {
                    Result.failure(Exception("Response body is null"))
                }
            } else {
                Result.failure(Exception("HTTP ${response.code()}: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun getCharacterById(id: Int): Result<ApiCharacter> {
        return try {
            val response: Response<ApiCharacter> = api.getCharacterById(id)
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    Result.success(body)
                } else {
                    Result.failure(Exception("Character body is null"))
                }
            } else {
                Result.failure(Exception("HTTP ${response.code()}: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}