package es.lasalle.videoconferencia.s04.domain.usecases

import es.lasalle.videoconferencia.s04.domain.models.Character
import es.lasalle.videoconferencia.s04.domain.repository.CharacterRepository

class GetCharactersUseCase(
    private val repository: CharacterRepository
) {
    suspend operator fun invoke(): Result<List<Character>> {
        return repository.getCharacters()
    }
}