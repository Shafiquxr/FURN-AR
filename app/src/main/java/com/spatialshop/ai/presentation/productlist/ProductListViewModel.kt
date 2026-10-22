package com.spatialshop.ai.presentation.productlist

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spatialshop.ai.domain.model.Product
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

enum class SortOption {
    POPULAR,
    PRICE_LOW_HIGH,
    PRICE_HIGH_LOW,
    RATING
}

data class ProductListUiState(
    val categoryName: String = "All",
    val products: List<Product> = emptyList(),
    val filteredProducts: List<Product> = emptyList(),
    val selectedSort: SortOption = SortOption.POPULAR,
    val searchQuery: String = "",
    val isLoading: Boolean = false
)

@HiltViewModel
class ProductListViewModel @Inject constructor(
    private val repository: ProductRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val categoryArg: String = savedStateHandle["category"] ?: "All"

    private val _uiState = MutableStateFlow(ProductListUiState(categoryName = categoryArg))
    val uiState: StateFlow<ProductListUiState> = _uiState.asStateFlow()

    init {
        val flow = if (categoryArg.equals("All", ignoreCase = true)) {
            repository.getProducts()
        } else {
            repository.getProductsByCategory(categoryArg)
        }

        flow.onEach { prods ->
            _uiState.update { current ->
                val sorted = applyFilterAndSort(prods, current.searchQuery, current.selectedSort)
                current.copy(
                    products = prods,
                    filteredProducts = sorted
                )
            }
        }.launchIn(viewModelScope)
    }

    fun onSearchQueryChanged(query: String) {
        _uiState.update { current ->
            val sorted = applyFilterAndSort(current.products, query, current.selectedSort)
            current.copy(searchQuery = query, filteredProducts = sorted)
        }
    }

    fun onSortSelected(sortOption: SortOption) {
        _uiState.update { current ->
            val sorted = applyFilterAndSort(current.products, current.searchQuery, sortOption)
            current.copy(selectedSort = sortOption, filteredProducts = sorted)
        }
    }

    fun toggleFavorite(productId: String) {
        viewModelScope.launch {
            repository.toggleFavorite(productId)
        }
    }

    private fun applyFilterAndSort(
        items: List<Product>,
        query: String,
        sort: SortOption
    ): List<Product> {
        val filtered = if (query.isBlank()) {
            items
        } else {
            items.filter {
                it.name.contains(query, ignoreCase = true) ||
                it.description.contains(query, ignoreCase = true)
            }
        }

        return when (sort) {
            SortOption.POPULAR -> filtered
            SortOption.PRICE_LOW_HIGH -> filtered.sortedBy { it.price }
            SortOption.PRICE_HIGH_LOW -> filtered.sortedByDescending { it.price }
            SortOption.RATING -> filtered.sortedByDescending { it.rating }
        }
    }
}
