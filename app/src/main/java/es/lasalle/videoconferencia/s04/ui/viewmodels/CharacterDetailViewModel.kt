package es.lasalle.videoconferencia.s04.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import es.lasalle.videoconferencia.s04.data.repository.CharacterRepositoryImpl
import es.lasalle.videoconferencia.s04.domain.usecases.GetCharacterDetailUseCase
import es.lasalle.videoconferencia.s04.ui.models.CharacterDetailUiEvent
import es.lasalle.videoconferencia.s04.ui.models.CharacterDetailUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CharacterDetailViewModel(application: Application) : AndroidViewModel(application) {
    
    private val repository = CharacterRepositoryImpl(application)
    private val getCharacterDetailUseCase = GetCharacterDetailUseCase(repository)
    
    private val _uiState = MutableStateFlow<CharacterDetailUiState>(CharacterDetailUiState.Loading)
    val uiState: StateFlow<CharacterDetailUiState> = _uiState.asStateFlow()
    
    fun onEvent(event: CharacterDetailUiEvent) {
        when (event) {
            is CharacterDetailUiEvent.LoadCharacter -> {
                loadCharacter(event.characterId)
            }
        }
    }
    
    private fun loadCharacter(characterId: Int) {
        viewModelScope.launch {
            _uiState.value = CharacterDetailUiState.Loading
            
            getCharacterDetailUseCase(characterId).fold(
                onSuccess = { character ->
                    if (character != null) {
                        _uiState.value = CharacterDetailUiState.Success(character)
                    } else {
                        _uiState.value = CharacterDetailUiState.Error("Personaje no encontrado")
                    }
                },
                onFailure = { error ->
                    _uiState.value = CharacterDetailUiState.Error(
                        error.message ?: "Error desconocido"
                    )
                }
            )
        }
    }
}