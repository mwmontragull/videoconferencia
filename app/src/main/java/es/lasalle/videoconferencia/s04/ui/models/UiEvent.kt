package es.lasalle.videoconferencia.s04.ui.models

sealed class CharacterListUiEvent {
    object LoadCharacters : CharacterListUiEvent()
    object RefreshCharacters : CharacterListUiEvent()
    data class CharacterClicked(val characterId: Int) : CharacterListUiEvent()
}

sealed class CharacterDetailUiEvent {
    data class LoadCharacter(val characterId: Int) : CharacterDetailUiEvent()
}