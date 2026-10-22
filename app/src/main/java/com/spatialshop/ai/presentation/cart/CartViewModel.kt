package com.spatialshop.ai.presentation.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spatialshop.ai.domain.model.CartItem
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

data class CartUiState(
    val cartItems: List<CartItem> = emptyList(),
    val subtotal: Double = 0.0,
    val deliveryFee: Double = 0.0,
    val total: Double = 0.0,
    val formattedTotal: String = "₹ 0",
    val isLoading: Boolean = false
)

@HiltViewModel
class CartViewModel @Inject constructor(
    private val repository: ProductRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CartUiState())
    val uiState: StateFlow<CartUiState> = _uiState.asStateFlow()

    init {
        repository.getCartItems().onEach { items ->
            val subtotal = items.sumOf { it.totalPrice }
            val deliveryFee = if (subtotal > 0 && subtotal < 10000) 499.0 else 0.0
            val total = subtotal + deliveryFee
            _uiState.update {
                it.copy(
                    cartItems = items,
                    subtotal = subtotal,
                    deliveryFee = deliveryFee,
                    total = total,
                    formattedTotal = "₹ ${String.format("%,.0f", total)}"
                )
            }
        }.launchIn(viewModelScope)
    }

    fun updateQuantity(cartItemId: Long, quantity: Int) {
        viewModelScope.launch {
            repository.updateCartQuantity(cartItemId, quantity)
        }
    }

    fun removeItem(cartItemId: Long) {
        viewModelScope.launch {
            repository.removeFromCart(cartItemId)
        }
    }

    fun clearCart() {
        viewModelScope.launch {
            repository.clearCart()
        }
    }
}
