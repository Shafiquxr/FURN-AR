package com.spatialshop.ai.presentation.checkout

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
import java.util.UUID
import javax.inject.Inject

enum class PaymentMethod(val displayName: String, val subtitle: String) {
    UPI("UPI", "Google Pay, PhonePe, Paytm, BHIM"),
    CARD("Credit / Debit Card", "Visa, Mastercard, RuPay"),
    NET_BANKING("Net Banking", "All Indian Banks"),
    COD("Cash on Delivery", "Pay when delivered to your door")
}

data class CheckoutUiState(
    val cartItems: List<CartItem> = emptyList(),
    val subtotal: Double = 0.0,
    val deliveryFee: Double = 0.0,
    val total: Double = 0.0,
    val fullName: String = "John Doe",
    val phone: String = "+91 98765 43210",
    val address: String = "Flat 402, Green Valley Apartments",
    val city: String = "Bengaluru",
    val state: String = "Karnataka",
    val pincode: String = "560001",
    val selectedPayment: PaymentMethod = PaymentMethod.UPI,
    val isPlacingOrder: Boolean = false,
    val orderId: String? = null
)

@HiltViewModel
class CheckoutViewModel @Inject constructor(
    private val repository: ProductRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CheckoutUiState())
    val uiState: StateFlow<CheckoutUiState> = _uiState.asStateFlow()

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
                    total = total
                )
            }
        }.launchIn(viewModelScope)
    }

    fun updateAddress(
        name: String,
        phone: String,
        address: String,
        city: String,
        state: String,
        pincode: String
    ) {
        _uiState.update {
            it.copy(
                fullName = name,
                phone = phone,
                address = address,
                city = city,
                state = state,
                pincode = pincode
            )
        }
    }

    fun selectPaymentMethod(method: PaymentMethod) {
        _uiState.update { it.copy(selectedPayment = method) }
    }

    fun placeOrder(onOrderPlaced: (String) -> Unit) {
        viewModelScope.launch {
            _uiState.update { it.copy(isPlacingOrder = true) }
            val orderId = "ORD-" + UUID.randomUUID().toString().substring(0, 8).uppercase()
            // Clear cart upon successful order
            repository.clearCart()
            _uiState.update { it.copy(isPlacingOrder = false, orderId = orderId) }
            onOrderPlaced(orderId)
        }
    }
}
