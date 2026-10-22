package com.spatialshop.ai.data.remote.dto

import com.google.gson.annotations.SerializedName

data class OrderRequestDto(
    @SerializedName("items") val items: List<OrderItemDto>,
    @SerializedName("shipping_address") val shippingAddress: String,
    @SerializedName("payment_method") val paymentMethod: String,
    @SerializedName("total_amount") val totalAmount: Double,
    @SerializedName("notes") val notes: String = ""
)

data class OrderItemDto(
    @SerializedName("product_id") val productId: String,
    @SerializedName("quantity") val quantity: Int,
    @SerializedName("price") val price: Double,
    @SerializedName("color_hex") val colorHex: String? = null
)

data class OrderResponseDto(
    @SerializedName("order_id") val orderId: String,
    @SerializedName("status") val status: String,
    @SerializedName("estimated_delivery") val estimatedDelivery: String,
    @SerializedName("message") val message: String
)

data class InventoryCheckDto(
    @SerializedName("product_id") val productId: String,
    @SerializedName("in_stock") val inStock: Boolean,
    @SerializedName("available_units") val availableUnits: Int,
    @SerializedName("warehouse_location") val warehouseLocation: String
)
