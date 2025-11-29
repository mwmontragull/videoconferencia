package es.lasalle.videoconferencia.s04.data.repository

import android.content.Context
import es.lasalle.videoconferencia.s04.data.local.LocalDataSource
import es.lasalle.videoconferencia.s04.data.mappers.toDomainModel
import es.lasalle.videoconferencia.s04.data.mappers.toDomainModels
import es.lasalle.videoconferencia.s04.data.remote.RemoteDataSource
import es.lasalle.videoconferencia.s04.domain.models.Character
import es.lasalle.videoconferencia.s04.domain.repository.CharacterRepository
import kotlinx.coroutines.flow.first

class CharacterRepositoryImpl(context: Context) : CharacterRepository {
    
    private val remoteDataSource = RemoteDataSource()
    private val localDataSource = LocalDataSource(context)
    
    override suspend fun getCharacters(): Result<List<Character>> {
        return try {
            val localCharacters = localDataSource.getAllCharactersFlow().first()
            
            if (localCharacters.isEmpty()) {
                val remoteResult = remoteDataSource.getCharacters(page = 1)
                remoteResult.fold(
                    onSuccess = { apiResponse ->
                        localDataSource.saveCharacters(apiResponse.results, page = 1)
                        Result.success(apiResponse.results.toDomainModels())
                    },
                    onFailure = { exception ->
                        Result.failure(exception)
                    }
                )
            } else {
                Result.success(localCharacters.toDomainModels())
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun getCharacterById(id: Int): Result<Character?> {
        return try {
            val localCharacter = localDataSource.getCharacterByIdFlow(id).first()
            
            if (localCharacter != null) {
                Result.success(localCharacter.toDomainModel())
            } else {
                val remoteResult = remoteDataSource.getCharacterById(id)
                remoteResult.fold(
                    onSuccess = { apiCharacter ->
                        localDataSource.saveCharacter(apiCharacter)
                        Result.success(apiCharacter.toDomainModel())
                    },
                    onFailure = { exception ->
                        Result.failure(exception)
                    }
                )
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}