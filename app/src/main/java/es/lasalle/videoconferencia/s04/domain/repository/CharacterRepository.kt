package es.lasalle.videoconferencia.s04.domain.repository

import es.lasalle.videoconferencia.s04.domain.models.Character

interface CharacterRepository {
    suspend fun getCharacters(): Result<List<Character>>
    suspend fun getCharacterById(id: Int): Result<Character?>
}