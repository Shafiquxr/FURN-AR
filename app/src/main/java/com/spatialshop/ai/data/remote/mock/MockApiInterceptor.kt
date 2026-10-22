package com.spatialshop.ai.data.remote.mock

import com.google.gson.Gson
import com.spatialshop.ai.data.repository.SampleData
import com.spatialshop.ai.data.remote.dto.InventoryCheckDto
import com.spatialshop.ai.data.remote.dto.OrderResponseDto
import com.spatialshop.ai.data.remote.dto.ProductColorDto
import com.spatialshop.ai.data.remote.dto.ProductDto
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MockApiInterceptor @Inject constructor(
    private val gson: Gson
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val url = request.url.toString()

        // Handle mocked endpoints
        return when {
            url.contains("api/v1/products/") && url.contains("/inventory") -> {
                val productId = request.url.pathSegments.let { it[it.size - 2] }
                val inventory = InventoryCheckDto(
                    productId = productId,
                    inStock = true,
                    availableUnits = 24,
                    warehouseLocation = "Mumbai Fulfillment Center"
                )
                createJsonResponse(request, 200, gson.toJson(inventory))
            }

            url.contains("api/v1/products/") -> {
                val productId = request.url.pathSegments.last()
                val product = SampleData.products.firstOrNull { it.id == productId }
                if (product != null) {
                    val dto = ProductDto(
                        id = product.id,
                        name = product.name,
                        category = product.category,
                        subcategory = product.subcategory,
                        price = product.price,
                        originalPrice = product.originalPrice,
                        discountPercent = product.discountPercent,
                        currency = product.currency,
                        description = product.description,
                        images = product.images,
                        colors = product.colors.map { ProductColorDto(it.name, it.hexCode, it.imageUrl) },
                        widthCm = product.widthCm,
                        depthCm = product.depthCm,
                        heightCm = product.heightCm,
                        rating = product.rating,
                        reviewCount = product.reviewCount,
                        merchantName = product.merchantName,
                        productUrl = product.productUrl,
                        modelUrl = product.modelUrl,
                        modelScale = product.modelScale,
                        tags = product.tags
                    )
                    createJsonResponse(request, 200, gson.toJson(dto))
                } else {
                    createJsonResponse(request, 404, """{"error": "Product not found"}""")
                }
            }

            url.contains("api/v1/products") -> {
                val dtos = SampleData.products.map { product ->
                    ProductDto(
                        id = product.id,
                        name = product.name,
                        category = product.category,
                        subcategory = product.subcategory,
                        price = product.price,
                        originalPrice = product.originalPrice,
                        discountPercent = product.discountPercent,
                        currency = product.currency,
                        description = product.description,
                        images = product.images,
                        colors = product.colors.map { ProductColorDto(it.name, it.hexCode, it.imageUrl) },
                        widthCm = product.widthCm,
                        depthCm = product.depthCm,
                        heightCm = product.heightCm,
                        rating = product.rating,
                        reviewCount = product.reviewCount,
                        merchantName = product.merchantName,
                        productUrl = product.productUrl,
                        modelUrl = product.modelUrl,
                        modelScale = product.modelScale,
                        tags = product.tags
                    )
                }
                createJsonResponse(request, 200, gson.toJson(dtos))
            }

            url.contains("api/v1/orders") && request.method == "POST" -> {
                val orderId = "ORD-" + UUID.randomUUID().toString().take(8).uppercase()
                val orderResponse = OrderResponseDto(
                    orderId = orderId,
                    status = "CONFIRMED",
                    estimatedDelivery = "3-5 Business Days",
                    message = "Order successfully synchronized with backend commerce system"
                )
                createJsonResponse(request, 200, gson.toJson(orderResponse))
            }

            else -> {
                // If not matched by mock handler, proceed with standard network chain
                try {
                    chain.proceed(request)
                } catch (e: Exception) {
                    createJsonResponse(request, 503, """{"error": "Network unreachable", "message": "${e.message}"}""")
                }
            }
        }
    }

    private fun createJsonResponse(request: okhttp3.Request, code: Int, json: String): Response {
        val mediaType = "application/json; charset=utf-8".toMediaTypeOrNull()
        val responseBody = json.toResponseBody(mediaType)

        return Response.Builder()
            .request(request)
            .protocol(Protocol.HTTP_1_1)
            .code(code)
            .message(if (code == 200) "OK" else "Error")
            .body(responseBody)
            .addHeader("content-type", "application/json")
            .build()
    }
}
