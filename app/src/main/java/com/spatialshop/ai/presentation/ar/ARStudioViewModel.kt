package com.spatialshop.ai.presentation.ar

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spatialshop.ai.core.fit.SpaceBounds
import com.spatialshop.ai.core.fit.SpatialFitEngine
import com.spatialshop.ai.domain.model.FitResult
import com.spatialshop.ai.domain.model.FitStatus
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
import kotlin.math.abs

data class ARStudioUiState(
    val product: Product? = null,
    val selectedColor: ProductColor? = null,
    val isPlaneDetected: Boolean = false,
    val isProductPlaced: Boolean = false,
    val showDimensions: Boolean = true,
    val fitStatus: FitStatus = FitStatus.FITS,
    val fitResult: FitResult = FitResult(
        status = FitStatus.FITS,
        clearanceLeft = 45f,
        clearanceRight = 38f,
        clearanceFront = 62f,
        clearanceBack = 15f,
        confidence = 0.94f,
        recommendations = listOf("Fits comfortably in current area", "Leaves 60cm walkway clearance")
    ),
    val rotationAngle: Float = 0f,
    val scaleFactor: Float = 1.0f,
    val isTrueScale: Boolean = true,
    val positionOffsetX: Float = 0f,
    val positionOffsetY: Float = 0f,
    val isCoachingVisible: Boolean = true,
    val isAlignedWithWall: Boolean = false,
    val catalogProducts: List<Product> = emptyList(),
    val additionalProduct: Product? = null,
    val isAdditionalPlaced: Boolean = false,
    val additionalPositionX: Float = 140f,
    val additionalPositionY: Float = 0f,
    val isAddedToCart: Boolean = false,
    val isDesignSaved: Boolean = false,
    val message: String? = null
)

@HiltViewModel
class ARStudioViewModel @Inject constructor(
    private val repository: ProductRepository,
    private val fitEngine: SpatialFitEngine,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val productId: String = checkNotNull(savedStateHandle["productId"])

    private var baseLeftClearance = 45f
    private var baseRightClearance = 38f
    private var baseFrontClearance = 62f
    private var baseBackClearance = 15f

    private var currentSpace = SpaceBounds(
        availableWidthCm = 180f,
        availableDepthCm = 160f,
        availableHeightCm = 240f,
        distanceToLeftObstacleCm = baseLeftClearance,
        distanceToRightObstacleCm = baseRightClearance,
        distanceToFrontObstacleCm = baseFrontClearance,
        distanceToBackObstacleCm = baseBackClearance
    )

    private val _uiState = MutableStateFlow(ARStudioUiState())
    val uiState: StateFlow<ARStudioUiState> = _uiState.asStateFlow()

    init {
        repository.getProductById(productId).onEach { prod ->
            _uiState.update { current ->
                val result = prod?.let { fitEngine.analyzeFit(it, currentSpace, current.rotationAngle) }
                    ?: current.fitResult
                current.copy(
                    product = prod,
                    selectedColor = current.selectedColor ?: prod?.colors?.firstOrNull(),
                    fitResult = result,
                    fitStatus = result.status
                )
            }
        }.launchIn(viewModelScope)

        repository.getProducts().onEach { prods ->
            _uiState.update { it.copy(catalogProducts = prods.filter { p -> p.id != productId }) }
        }.launchIn(viewModelScope)
    }

    fun onPlaneDetected(detected: Boolean) {
        _uiState.update { it.copy(isPlaneDetected = detected) }
    }

    fun onProductPlaced(placed: Boolean) {
        val prod = _uiState.value.product
        val result = if (prod != null) {
            fitEngine.analyzeFit(prod, currentSpace, _uiState.value.rotationAngle)
        } else _uiState.value.fitResult

        _uiState.update {
            it.copy(
                isProductPlaced = placed,
                fitResult = result,
                fitStatus = result.status,
                isCoachingVisible = placed,
                message = if (placed) "Product placed! Drag to reposition, twist to rotate" else null
            )
        }
    }

    fun onDrag(deltaX: Float, deltaY: Float) {
        val sensitivity = 0.25f
        val newX = _uiState.value.positionOffsetX + deltaX * sensitivity
        val newY = _uiState.value.positionOffsetY + deltaY * sensitivity

        // Dynamically adjust room clearances relative to movement
        val adjustedLeft = (baseLeftClearance + newX * 0.5f).coerceAtLeast(0f)
        val adjustedRight = (baseRightClearance - newX * 0.5f).coerceAtLeast(0f)
        val adjustedFront = (baseFrontClearance - newY * 0.5f).coerceAtLeast(0f)
        val adjustedBack = (baseBackClearance + newY * 0.5f).coerceAtLeast(0f)

        currentSpace = currentSpace.copy(
            distanceToLeftObstacleCm = adjustedLeft,
            distanceToRightObstacleCm = adjustedRight,
            distanceToFrontObstacleCm = adjustedFront,
            distanceToBackObstacleCm = adjustedBack
        )

        val prod = _uiState.value.product
        val result = if (prod != null) {
            fitEngine.analyzeFit(prod, currentSpace, _uiState.value.rotationAngle)
        } else _uiState.value.fitResult

        _uiState.update {
            it.copy(
                positionOffsetX = newX,
                positionOffsetY = newY,
                fitResult = result,
                fitStatus = result.status,
                isCoachingVisible = false
            )
        }
    }

    fun onRotateGesture(deltaDegrees: Float) {
        var angle = (_uiState.value.rotationAngle + deltaDegrees) % 360f
        if (angle < 0f) angle += 360f

        // Magnetic wall alignment snapping (within 4 degrees of cardinal walls)
        var isAligned = false
        val cardinalAngles = listOf(0f, 90f, 180f, 270f, 360f)
        for (cardinal in cardinalAngles) {
            if (abs(angle - cardinal) < 4f) {
                angle = cardinal % 360f
                isAligned = true
                break
            }
        }

        val prod = _uiState.value.product
        val result = if (prod != null) {
            fitEngine.analyzeFit(prod, currentSpace, angle)
        } else _uiState.value.fitResult

        _uiState.update {
            it.copy(
                rotationAngle = angle,
                isAlignedWithWall = isAligned,
                fitResult = result,
                fitStatus = result.status,
                isCoachingVisible = false
            )
        }
    }

    fun onPinchScale(zoomRatio: Float) {
        val newScale = (_uiState.value.scaleFactor * zoomRatio).coerceIn(0.5f, 1.5f)
        val isTrue = abs(newScale - 1.0f) < 0.05f

        _uiState.update {
            it.copy(
                scaleFactor = if (isTrue) 1.0f else newScale,
                isTrueScale = isTrue,
                isCoachingVisible = false
            )
        }
    }

    fun resetToTrueScale() {
        _uiState.update {
            it.copy(
                scaleFactor = 1.0f,
                isTrueScale = true,
                message = "Locked to 100% True Physical Scale 🔒"
            )
        }
    }

    fun dismissCoaching() {
        _uiState.update { it.copy(isCoachingVisible = false) }
    }

    fun toggleDimensions() {
        _uiState.update { it.copy(showDimensions = !it.showDimensions) }
    }

    fun rotateProduct(degrees: Float = 45f) {
        onRotateGesture(degrees)
    }

    fun autoOptimizePlacement() {
        val prod = _uiState.value.product ?: return
        val optimalAngle = fitEngine.findOptimalRotation(prod, currentSpace)
        val result = fitEngine.analyzeFit(prod, currentSpace, optimalAngle)
        _uiState.update {
            it.copy(
                rotationAngle = optimalAngle,
                fitResult = result,
                fitStatus = result.status,
                isCoachingVisible = false,
                message = "Optimized to ${optimalAngle.toInt()}° for maximum walkway clearance! ✨"
            )
        }
    }

    fun saveDesign(name: String) {
        val prod = _uiState.value.product ?: return
        val state = _uiState.value
        viewModelScope.launch {
            repository.saveDesign(
                name = name.ifBlank { "${prod.name} Layout" },
                productId = prod.id,
                rotationY = state.rotationAngle,
                fitStatus = state.fitStatus.name,
                clearanceFront = state.fitResult.clearanceFront ?: 60f,
                thumbnailPath = prod.images.firstOrNull()
            )
            _uiState.update {
                it.copy(
                    isDesignSaved = true,
                    message = "Design '${name.ifBlank { prod.name }}' saved to your profile! 💾"
                )
            }
        }
    }

    fun resetPlacement() {
        currentSpace = currentSpace.copy(
            distanceToLeftObstacleCm = baseLeftClearance,
            distanceToRightObstacleCm = baseRightClearance,
            distanceToFrontObstacleCm = baseFrontClearance,
            distanceToBackObstacleCm = baseBackClearance
        )
        _uiState.update {
            it.copy(
                isProductPlaced = false,
                rotationAngle = 0f,
                scaleFactor = 1.0f,
                isTrueScale = true,
                positionOffsetX = 0f,
                positionOffsetY = 0f,
                isDesignSaved = false,
                message = "Placement reset. Scan a surface to place again"
            )
        }
    }

    fun selectColor(color: ProductColor) {
        _uiState.update { it.copy(selectedColor = color) }
    }

    fun addToCart() {
        val prod = _uiState.value.product ?: return
        val color = _uiState.value.selectedColor
        viewModelScope.launch {
            repository.addToCart(prod, 1, color)
            _uiState.update { it.copy(isAddedToCart = true, message = "Added to cart!") }
        }
    }

    fun addStackedProduct(product: Product) {
        _uiState.update {
            it.copy(
                additionalProduct = product,
                isAdditionalPlaced = true,
                message = "Added ${product.name} to room scene! 🛋️"
            )
        }
    }

    fun removeStackedProduct() {
        _uiState.update {
            it.copy(
                additionalProduct = null,
                isAdditionalPlaced = false,
                message = "Removed second item from scene"
            )
        }
    }

    fun addAllPlacedToCart() {
        val prod1 = _uiState.value.product
        val prod2 = _uiState.value.additionalProduct
        viewModelScope.launch {
            if (prod1 != null) {
                repository.addToCart(prod1, 1, _uiState.value.selectedColor)
            }
            if (prod2 != null && _uiState.value.isAdditionalPlaced) {
                repository.addToCart(prod2, 1, prod2.colors.firstOrNull())
            }
            _uiState.update {
                it.copy(
                    isAddedToCart = true,
                    message = "All placed room items added to cart! 🛒"
                )
            }
        }
    }

    fun clearMessage() {
        _uiState.update { it.copy(message = null) }
    }
}
