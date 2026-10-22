package com.spatialshop.ai.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spatialshop.ai.domain.model.SavedDesign
import com.spatialshop.ai.domain.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProfileUiState(
    val savedDesigns: List<SavedDesign> = emptyList(),
    val userName: String = "Spatial Shopper",
    val userEmail: String = "shopper@spatialshop.ai"
)

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: ProductRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        repository.getSavedDesigns().onEach { designs ->
            _uiState.update { it.copy(savedDesigns = designs) }
        }.launchIn(viewModelScope)
    }

    fun deleteDesign(id: String) {
        viewModelScope.launch {
            repository.deleteSavedDesign(id)
        }
    }
}
