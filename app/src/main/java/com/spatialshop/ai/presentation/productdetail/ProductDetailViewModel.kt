package com.spatialshop.ai.presentation.productdetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spatialshop.ai.domain.model.Product
import com.spatialshop.ai.domain.model.ProductColor
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

data class ProductDetailUiState(
    val product: Product? = null,
    val selectedColor: ProductColor? = null,
    val selectedImageIndex: Int = 0,
    val isFavorite: Boolean = false,
    val isAddedToCart: Boolean = false,
    val isLoading: Boolean = true
)

@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    private val repository: ProductRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val productId: String = checkNotNull(savedStateHandle["productId"])

    private val _uiState = MutableStateFlow(ProductDetailUiState())
    val uiState: StateFlow<ProductDetailUiState> = _uiState.asStateFlow()

    init {
        repository.getProductById(productId).onEach { prod ->
            _uiState.update { current ->
                current.copy(
                    product = prod,
                    selectedColor = current.selectedColor ?: prod?.colors?.firstOrNull(),
                    isFavorite = prod?.isFavorite ?: false,
                    isLoading = false
                )
            }
        }.launchIn(viewModelScope)
    }

    fun selectColor(color: ProductColor) {
        _uiState.update { it.copy(selectedColor = color) }
    }

    fun selectImage(index: Int) {
        _uiState.update { it.copy(selectedImageIndex = index) }
    }

    fun toggleFavorite() {
        viewModelScope.launch {
            repository.toggleFavorite(productId)
        }
    }

    fun addToCart() {
        val prod = _uiState.value.product ?: return
        val color = _uiState.value.selectedColor
        viewModelScope.launch {
            repository.addToCart(prod, 1, color)
            _uiState.update { it.copy(isAddedToCart = true) }
        }
    }
}
