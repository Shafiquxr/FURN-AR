package com.spatialshop.ai.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spatialshop.ai.core.network.NetworkMonitor
import com.spatialshop.ai.data.repository.SampleData
import com.spatialshop.ai.domain.model.Category
import com.spatialshop.ai.domain.model.FurnitureCollection
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

data class HomeUiState(
    val categories: List<Category> = SampleData.categories,
    val selectedCategory: String = "All",
    val bestSellers: List<Product> = emptyList(),
    val deals: List<Product> = emptyList(),
    val collections: List<FurnitureCollection> = emptyList(),
    val searchQuery: String = "",
    val isLoading: Boolean = false,
    val isOnline: Boolean = true
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: ProductRepository,
    private val networkMonitor: NetworkMonitor
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadData()
        observeNetwork()
        syncCatalog()
    }

    private fun observeNetwork() {
        networkMonitor.isOnline.onEach { online ->
            _uiState.update { it.copy(isOnline = online) }
            if (online) {
                syncCatalog()
            }
        }.launchIn(viewModelScope)
    }

    fun syncCatalog() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            repository.syncProducts()
            _uiState.update { it.copy(isLoading = false) }
        }
    }

    private fun loadData() {
        repository.getCategories().onEach { cats ->
            _uiState.update { it.copy(categories = cats) }
        }.launchIn(viewModelScope)

        repository.getBestSellers().onEach { products ->
            _uiState.update { it.copy(bestSellers = products) }
        }.launchIn(viewModelScope)

        repository.getDeals().onEach { deals ->
            _uiState.update { it.copy(deals = deals) }
        }.launchIn(viewModelScope)

        repository.getCollections().onEach { cols ->
            _uiState.update { it.copy(collections = cols) }
        }.launchIn(viewModelScope)
    }

    fun onCategorySelected(category: String) {
        _uiState.update { it.copy(selectedCategory = category) }
    }

    fun onSearchQueryChanged(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
    }

    fun toggleFavorite(productId: String) {
        viewModelScope.launch {
            repository.toggleFavorite(productId)
        }
    }
}
