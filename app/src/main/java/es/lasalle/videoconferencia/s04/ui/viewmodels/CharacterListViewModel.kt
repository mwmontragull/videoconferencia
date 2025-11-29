package es.lasalle.videoconferencia.s04.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import es.lasalle.videoconferencia.s04.data.repository.CharacterRepositoryImpl
import es.lasalle.videoconferencia.s04.domain.usecases.GetCharactersUseCase
import es.lasalle.videoconferencia.s04.ui.models.CharacterListUiEvent
import es.lasalle.videoconferencia.s04.ui.models.CharacterListUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CharacterListViewModel(application: Application) : AndroidViewModel(application) {
    
    private val repository = CharacterRepositoryImpl(application)
    private val getCharactersUseCase = GetCharactersUseCase(repository)
    
    private val _uiState = MutableStateFlow<CharacterListUiState>(CharacterListUiState.Loading)
    val uiState: StateFlow<CharacterListUiState> = _uiState.asStateFlow()
    
    init {
        loadCharacters()
    }
    
    fun onEvent(event: CharacterListUiEvent) {
        when (event) {
            CharacterListUiEvent.LoadCharacters -> loadCharacters()
            CharacterListUiEvent.RefreshCharacters -> refreshCharacters()
            is CharacterListUiEvent.CharacterClicked -> {
                // Navigation will be handled by the composable
            }
        }
    }
    
    private fun loadCharacters() {
        viewModelScope.launch {
            _uiState.value = CharacterListUiState.Loading
            
            getCharactersUseCase().fold(
                onSuccess = { characters ->
                    _uiState.value = CharacterListUiState.Success(characters)
                },
                onFailure = { error ->
                    _uiState.value = CharacterListUiState.Error(
                        error.message ?: "Error desconocido"
                    )
                }
            )
        }
    }
    
    private fun refreshCharacters() {
        loadCharacters()
    }
}