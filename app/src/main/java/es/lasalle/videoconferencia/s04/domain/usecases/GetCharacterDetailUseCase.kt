package es.lasalle.videoconferencia.s04.domain.usecases

import es.lasalle.videoconferencia.s04.domain.models.Character
import es.lasalle.videoconferencia.s04.domain.repository.CharacterRepository

class GetCharacterDetailUseCase(
    private val repository: CharacterRepository
) {
    suspend operator fun invoke(id: Int): Result<Character?> {
        return repository.getCharacterById(id)
    }
}