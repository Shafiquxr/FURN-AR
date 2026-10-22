package com.spatialshop.ai.data.remote.api

import com.spatialshop.ai.data.remote.dto.InventoryCheckDto
import com.spatialshop.ai.data.remote.dto.OrderRequestDto
import com.spatialshop.ai.data.remote.dto.OrderResponseDto
import com.spatialshop.ai.data.remote.dto.ProductDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface SpatialShopApi {

    @GET("api/v1/products")
    suspend fun getProducts(
        @Query("category") category: String? = null,
        @Query("query") query: String? = null
    ): Response<List<ProductDto>>

    @GET("api/v1/products/{id}")
    suspend fun getProductById(
        @Path("id") id: String
    ): Response<ProductDto>

    @GET("api/v1/products/{id}/inventory")
    suspend fun checkInventory(
        @Path("id") id: String
    ): Response<InventoryCheckDto>

    @POST("api/v1/orders")
    suspend fun placeOrder(
        @Body orderRequest: OrderRequestDto
    ): Response<OrderResponseDto>
}
