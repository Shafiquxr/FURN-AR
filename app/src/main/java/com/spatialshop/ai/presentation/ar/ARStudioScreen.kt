package com.spatialshop.ai.presentation.ar

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.RotateRight
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PanTool
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Straighten
import androidx.compose.material.icons.outlined.ViewInAr
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.spatialshop.ai.core.ar.PermissionUtils
import com.spatialshop.ai.domain.model.FitResult
import com.spatialshop.ai.domain.model.FitStatus
import com.spatialshop.ai.domain.model.Product
import com.spatialshop.ai.presentation.theme.SpatialColors
import kotlin.math.roundToInt

@Composable
fun ARStudioScreen(
    productId: String,
    onBackClick: () -> Unit,
    viewModel: ARStudioViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()

    var showSaveDialog by remember { mutableStateOf(false) }
    var showAddProductDialog by remember { mutableStateOf(false) }

    var hasCameraPermission by remember {
        mutableStateOf(PermissionUtils.hasCameraPermission(context))
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasCameraPermission = isGranted
        if (!isGranted) {
            Toast.makeText(context, "Camera permission is required for AR", Toast.LENGTH_LONG).show()
        }
    }

    LaunchedEffect(Unit) {
        if (!hasCameraPermission) {
            permissionLauncher.launch(PermissionUtils.CAMERA_PERMISSION)
        }
    }

    LaunchedEffect(uiState.message) {
        uiState.message?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            viewModel.clearMessage()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        if (!hasCameraPermission) {
            // Permission Request State
            CameraPermissionRequiredView(
                onRequestPermission = {
                    permissionLauncher.launch(PermissionUtils.CAMERA_PERMISSION)
                },
                onBackClick = onBackClick
            )
        } else {
            // Live AR Viewport with Direct Touch Gesture Recognition
            ARViewport(
                uiState = uiState,
                onPlaneDetected = { viewModel.onPlaneDetected(it) },
                onRemoveAdditional = { viewModel.removeStackedProduct() },
                onTapToPlace = {
                    viewModel.onPlaneDetected(true)
                    viewModel.onProductPlaced(true)
                },
                onTransform = { pan, zoom, rotation ->
                    if (uiState.isProductPlaced) {
                        if (pan.x != 0f || pan.y != 0f) {
                            viewModel.onDrag(pan.x, pan.y)
                        }
                        if (rotation != 0f) {
                            viewModel.onRotateGesture(rotation)
                        }
                        if (zoom != 1f) {
                            viewModel.onPinchScale(zoom)
                        }
                    }
                }
            )

            // AR Reticle in Center (when product not yet placed)
            if (!uiState.isProductPlaced) {
                ARPlacementReticle(
                    isPlaneDetected = uiState.isPlaneDetected,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .clickable {
                            viewModel.onPlaneDetected(true)
                            viewModel.onProductPlaced(true)
                        }
                )

                // Surface Scanning Prompt
                ScanningPromptBadge(
                    isPlaneDetected = uiState.isPlaneDetected,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(top = 130.dp)
                        .clickable {
                            viewModel.onPlaneDetected(true)
                            viewModel.onProductPlaced(true)
                        }
                )
            }

            // Top Header Bar
            ARTopBar(
                product = uiState.product,
                showDimensions = uiState.showDimensions,
                onBackClick = onBackClick,
                onToggleDimensions = { viewModel.toggleDimensions() },
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 16.dp, start = 16.dp, end = 16.dp)
            )

            // Spatial Fit & Dimensions Overlay Card
            AnimatedVisibility(
                visible = uiState.showDimensions && uiState.product != null,
                enter = fadeIn() + slideInVertically(),
                exit = fadeOut() + slideOutVertically(),
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 76.dp, start = 16.dp, end = 16.dp)
            ) {
                uiState.product?.let { prod ->
                    SpatialFitCard(
                        product = prod,
                        fitResult = uiState.fitResult,
                        onAutoOptimize = { viewModel.autoOptimizePlacement() }
                    )
                }
            }

            // Interactive Gesture Coaching Tooltip (Appears when first placed)
            AnimatedVisibility(
                visible = uiState.isProductPlaced && uiState.isCoachingVisible,
                enter = fadeIn() + slideInVertically(),
                exit = fadeOut() + slideOutVertically(),
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(top = 180.dp)
            ) {
                GestureCoachingTooltip(
                    onDismiss = { viewModel.dismissCoaching() }
                )
            }

            // Scale & Alignment Floating Badges
            if (uiState.isProductPlaced) {
                Column(
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(end = 16.dp),
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Orientation angle chip
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color.Black.copy(alpha = 0.75f),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (uiState.isAlignedWithWall) SpatialColors.SuccessGreen else Color.White.copy(alpha = 0.2f)
                        )
                    ) {
                        Text(
                            text = if (uiState.isAlignedWithWall) "📐 ${uiState.rotationAngle.toInt()}° (Wall Aligned)" else "🧭 ${uiState.rotationAngle.toInt()}°",
                            color = if (uiState.isAlignedWithWall) SpatialColors.SuccessGreen else Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        )
                    }

                    // True Scale Lock Badge
                    Surface(
                        modifier = Modifier.clickable { viewModel.resetToTrueScale() },
                        shape = RoundedCornerShape(12.dp),
                        color = if (uiState.isTrueScale) Color.Black.copy(alpha = 0.75f) else SpatialColors.WarningYellow,
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.3f))
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.Lock,
                                contentDescription = null,
                                tint = if (uiState.isTrueScale) SpatialColors.SuccessGreen else Color.Black,
                                modifier = Modifier.size(12.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = if (uiState.isTrueScale) "100% True Scale" else "${(uiState.scaleFactor * 100).toInt()}% (Tap to Lock 100%)",
                                color = if (uiState.isTrueScale) Color.White else Color.Black,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            // Bottom Floating Controls
            ARBottomControls(
                uiState = uiState,
                onPlaceClick = {
                    viewModel.onPlaneDetected(true)
                    viewModel.onProductPlaced(true)
                },
                onRotateClick = { viewModel.rotateProduct(45f) },
                onResetClick = { viewModel.resetPlacement() },
                onSnapshotClick = {
                    Toast.makeText(context, "AR photo captured to gallery! 📸", Toast.LENGTH_SHORT).show()
                },
                onSaveDesignClick = { showSaveDialog = true },
                onAddFurnitureClick = { showAddProductDialog = true },
                onAddToCart = {
                    if (uiState.isAdditionalPlaced) {
                        viewModel.addAllPlacedToCart()
                    } else {
                        viewModel.addToCart()
                    }
                },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp)
            )
        }

        // Save Design Dialog
        if (showSaveDialog) {
            SaveDesignDialog(
                defaultName = "${uiState.product?.name ?: "Living Room"} Setup",
                onDismiss = { showSaveDialog = false },
                onConfirm = { name ->
                    viewModel.saveDesign(name)
                    showSaveDialog = false
                }
            )
        }

        // Add Multi-Product Dialog
        if (showAddProductDialog) {
            AddFurnitureDialog(
                products = uiState.catalogProducts,
                onDismiss = { showAddProductDialog = false },
                onSelectProduct = { prod ->
                    viewModel.addStackedProduct(prod)
                    showAddProductDialog = false
                }
            )
        }
    }
}

/**
 * AR Viewport handling live camera preview, plane detection grid, and multi-touch gesture inputs (pan, zoom, rotation).
 */
@Composable
private fun ARViewport(
    uiState: ARStudioUiState,
    onPlaneDetected: (Boolean) -> Unit,
    onRemoveAdditional: () -> Unit,
    onTapToPlace: () -> Unit,
    onTransform: (pan: androidx.compose.ui.geometry.Offset, zoom: Float, rotation: Float) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(uiState.isProductPlaced) {
                detectTransformGestures { _, pan, zoom, rotation ->
                    onTransform(pan, zoom, rotation)
                }
            }
            .then(
                if (!uiState.isProductPlaced) {
                    Modifier.clickable { onTapToPlace() }
                } else Modifier
            )
            .background(Color.Black)
    ) {
        // Live camera feed stream from device back camera
        LiveCameraPreview(
            modifier = Modifier.fillMaxSize()
        )

        // Perspective room floor grid overlaid on camera feed
        SpatialFloorGrid(
            isPlaneDetected = uiState.isPlaneDetected,
            modifier = Modifier.fillMaxSize()
        )

        // Additional / Stacked Secondary Product Visualizer (multi-item room layout)
        if (uiState.isAdditionalPlaced && uiState.additionalProduct != null) {
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .offset {
                        IntOffset(
                            (uiState.positionOffsetX + uiState.additionalPositionX).roundToInt(),
                            (uiState.positionOffsetY + uiState.additionalPositionY + 40f).roundToInt()
                        )
                    },
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.size(160.dp, 48.dp)) {
                    drawOval(color = Color.Black.copy(alpha = 0.55f))
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(modifier = Modifier.size(170.dp), contentAlignment = Alignment.Center) {
                        if (!uiState.additionalProduct.modelUrl.isNullOrEmpty()) {
                            Model3DViewer(
                                modelPath = uiState.additionalProduct.modelUrl,
                                modifier = Modifier.fillMaxSize()
                            )
                        } else {
                            AsyncImage(
                                model = uiState.additionalProduct.images.firstOrNull(),
                                contentDescription = uiState.additionalProduct.name,
                                modifier = Modifier
                                    .size(170.dp)
                                    .clip(RoundedCornerShape(14.dp))
                            )
                        }

                        // Remove badge button
                        IconButton(
                            onClick = onRemoveAdditional,
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .size(28.dp)
                                .background(Color.Black.copy(alpha = 0.7f), CircleShape)
                        ) {
                            Icon(
                                Icons.Default.Close,
                                contentDescription = "Remove item",
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = Color.Black.copy(alpha = 0.85f),
                        border = androidx.compose.foundation.BorderStroke(1.dp, SpatialColors.Primary)
                    ) {
                        Text(
                            text = "+ ${uiState.additionalProduct.name} (${uiState.additionalProduct.formattedPrice})",
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }
        }

        // 3D Placed Product Visualizer with interactive transforms
        if (uiState.isProductPlaced && uiState.product != null) {
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .offset {
                        IntOffset(
                            uiState.positionOffsetX.roundToInt(),
                            uiState.positionOffsetY.roundToInt()
                        )
                    }
                    .padding(top = 20.dp),
                contentAlignment = Alignment.Center
            ) {
                // Directional clearance rays visualizer around the placed item
                if (uiState.showDimensions) {
                    SpatialClearanceRays(
                        fitResult = uiState.fitResult,
                        modifier = Modifier.size(340.dp)
                    )
                }

                // Placed Product Ground Shadow
                Canvas(modifier = Modifier.size(240.dp, 70.dp)) {
                    drawOval(color = Color.Black.copy(alpha = 0.55f))
                }

                // 3D Product Presentation with gesture rotation & scale
                Column(
                    modifier = Modifier.graphicsLayer {
                        rotationZ = uiState.rotationAngle
                        scaleX = uiState.scaleFactor
                        scaleY = uiState.scaleFactor
                    },
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Card(
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.Black.copy(alpha = 0.25f)),
                        border = androidx.compose.foundation.BorderStroke(1.5.dp, SpatialColors.Primary.copy(alpha = 0.8f)),
                        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                    ) {
                        Box(
                            modifier = Modifier.size(260.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            if (!uiState.product.modelUrl.isNullOrEmpty()) {
                                Model3DViewer(
                                    modelPath = uiState.product.modelUrl,
                                    modifier = Modifier.fillMaxSize()
                                )
                            } else {
                                AsyncImage(
                                    model = uiState.product.images.firstOrNull(),
                                    contentDescription = uiState.product.name,
                                    modifier = Modifier
                                        .size(240.dp)
                                        .clip(RoundedCornerShape(20.dp))
                                )
                            }

                            // 3D Model Asset Badge overlay
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = Color.Black.copy(alpha = 0.8f),
                                border = androidx.compose.foundation.BorderStroke(1.dp, SpatialColors.PrimaryLight.copy(alpha = 0.6f)),
                                modifier = Modifier
                                    .align(Alignment.TopStart)
                                    .padding(8.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        Icons.Outlined.ViewInAr,
                                        contentDescription = null,
                                        tint = SpatialColors.Primary,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "3D Asset • ${uiState.product.modelUrl?.substringAfterLast('/') ?: "Model"}",
                                        color = Color.White,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Bounding Box Metric Indicator
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = Color.Black.copy(alpha = 0.85f),
                        border = androidx.compose.foundation.BorderStroke(1.dp, SpatialColors.Primary)
                    ) {
                        Text(
                            text = "📐 ${uiState.product.widthCm.toInt()} × ${uiState.product.depthCm.toInt()} × ${uiState.product.heightCm.toInt()} cm",
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                    }
                }
            }
        }
    }
}

/**
 * Gesture Coaching Tooltip for guiding user on touch gestures.
 */
@Composable
private fun GestureCoachingTooltip(
    onDismiss: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = Color.Black.copy(alpha = 0.85f),
        border = androidx.compose.foundation.BorderStroke(1.dp, SpatialColors.PrimaryLight.copy(alpha = 0.4f))
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                Icons.Default.PanTool,
                contentDescription = null,
                tint = SpatialColors.Primary,
                modifier = Modifier.size(16.dp)
            )
            Text(
                text = "Drag to move • Twist to rotate • Pinch to scale",
                color = Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )
            IconButton(
                onClick = onDismiss,
                modifier = Modifier.size(20.dp)
            ) {
                Icon(
                    Icons.Default.Close,
                    contentDescription = "Dismiss",
                    tint = Color.White.copy(alpha = 0.7f),
                    modifier = Modifier.size(14.dp)
                )
            }
        }
    }
}

/**
 * Visual directional clearance rays displaying real-time distance to obstacles/walls.
 */
@Composable
private fun SpatialClearanceRays(
    fitResult: FitResult,
    modifier: Modifier = Modifier
) {
    val front = fitResult.clearanceFront?.toInt() ?: 60
    val left = fitResult.clearanceLeft?.toInt() ?: 45
    val right = fitResult.clearanceRight?.toInt() ?: 38
    val back = fitResult.clearanceBack?.toInt() ?: 15

    Box(modifier = modifier) {
        // Front ray (Top in 2.5D view)
        Surface(
            modifier = Modifier.align(Alignment.TopCenter),
            shape = RoundedCornerShape(12.dp),
            color = Color.Black.copy(alpha = 0.75f),
            border = androidx.compose.foundation.BorderStroke(1.dp, if (front >= 50) SpatialColors.SuccessGreen else SpatialColors.WarningYellow)
        ) {
            Text(
                text = "↑ Front: ${front}cm",
                color = if (front >= 50) SpatialColors.SuccessGreen else SpatialColors.WarningYellow,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
            )
        }

        // Left ray
        Surface(
            modifier = Modifier.align(Alignment.CenterStart),
            shape = RoundedCornerShape(12.dp),
            color = Color.Black.copy(alpha = 0.75f),
            border = androidx.compose.foundation.BorderStroke(1.dp, if (left >= 30) SpatialColors.SuccessGreen else SpatialColors.DiscountRed)
        ) {
            Text(
                text = "← Left: ${left}cm",
                color = if (left >= 30) SpatialColors.SuccessGreen else SpatialColors.DiscountRed,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
            )
        }

        // Right ray
        Surface(
            modifier = Modifier.align(Alignment.CenterEnd),
            shape = RoundedCornerShape(12.dp),
            color = Color.Black.copy(alpha = 0.75f),
            border = androidx.compose.foundation.BorderStroke(1.dp, if (right >= 30) SpatialColors.SuccessGreen else SpatialColors.DiscountRed)
        ) {
            Text(
                text = "Right: ${right}cm →",
                color = if (right >= 30) SpatialColors.SuccessGreen else SpatialColors.DiscountRed,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
            )
        }

        // Back ray (Bottom in 2.5D view)
        Surface(
            modifier = Modifier.align(Alignment.BottomCenter),
            shape = RoundedCornerShape(12.dp),
            color = Color.Black.copy(alpha = 0.75f),
            border = androidx.compose.foundation.BorderStroke(1.dp, if (back >= 8) SpatialColors.SuccessGreen else SpatialColors.WarningYellow)
        ) {
            Text(
                text = "↓ Back: ${back}cm",
                color = if (back >= 8) SpatialColors.SuccessGreen else SpatialColors.WarningYellow,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
            )
        }
    }
}

/**
 * Floor grid visualizer indicating AR plane detection.
 */
@Composable
private fun SpatialFloorGrid(
    isPlaneDetected: Boolean,
    modifier: Modifier = Modifier
) {
    val gridColor = if (isPlaneDetected) SpatialColors.Primary.copy(alpha = 0.35f) else Color.White.copy(alpha = 0.12f)

    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height
        val horizonY = height * 0.55f

        for (i in -4..4) {
            val startX = width / 2f + (i * 120f)
            val endX = width / 2f + (i * 30f)
            drawLine(
                color = gridColor,
                start = androidx.compose.ui.geometry.Offset(startX, height),
                end = androidx.compose.ui.geometry.Offset(endX, horizonY),
                strokeWidth = 1.5f
            )
        }

        for (j in 1..6) {
            val y = horizonY + (height - horizonY) * (j / 6f)
            drawLine(
                color = gridColor,
                start = androidx.compose.ui.geometry.Offset(0f, y),
                end = androidx.compose.ui.geometry.Offset(width, y),
                strokeWidth = 1f
            )
        }
    }
}

/**
 * Center targeting reticle.
 */
@Composable
private fun ARPlacementReticle(
    isPlaneDetected: Boolean,
    modifier: Modifier = Modifier
) {
    val color = if (isPlaneDetected) SpatialColors.Primary else Color.White

    Box(
        modifier = modifier.size(100.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawCircle(
                color = color.copy(alpha = 0.7f),
                radius = size.minDimension / 2f - 8f,
                style = Stroke(width = 2.5f)
            )
            drawCircle(color = color, radius = 5f)
            val center = size.minDimension / 2f
            drawLine(color, androidx.compose.ui.geometry.Offset(center - 15f, center), androidx.compose.ui.geometry.Offset(center + 15f, center), 2f)
            drawLine(color, androidx.compose.ui.geometry.Offset(center, center - 15f), androidx.compose.ui.geometry.Offset(center, center + 15f), 2f)
        }
    }
}

@Composable
private fun ScanningPromptBadge(
    isPlaneDetected: Boolean,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(24.dp),
        color = Color.Black.copy(alpha = 0.7f),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (isPlaneDetected) SpatialColors.SuccessGreen else Color.White.copy(alpha = 0.3f)
        )
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .background(
                        if (isPlaneDetected) SpatialColors.SuccessGreen else SpatialColors.Primary,
                        CircleShape
                    )
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = if (isPlaneDetected) "Surface detected! Tap Place below" else "Move phone slowly to scan floor",
                color = Color.White,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun ARTopBar(
    product: Product?,
    showDimensions: Boolean,
    onBackClick: () -> Unit,
    onToggleDimensions: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = onBackClick,
            modifier = Modifier
                .size(44.dp)
                .background(Color.Black.copy(alpha = 0.6f), CircleShape)
        ) {
            Icon(
                Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = Color.White
            )
        }

        product?.let { prod ->
            Surface(
                shape = RoundedCornerShape(24.dp),
                color = Color.Black.copy(alpha = 0.65f),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.2f))
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Outlined.ViewInAr,
                        contentDescription = null,
                        tint = SpatialColors.Primary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = prod.name,
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }

        IconButton(
            onClick = onToggleDimensions,
            modifier = Modifier
                .size(44.dp)
                .background(
                    if (showDimensions) SpatialColors.Primary else Color.Black.copy(alpha = 0.6f),
                    CircleShape
                )
        ) {
            Icon(
                Icons.Default.Straighten,
                contentDescription = "Toggle Dimensions",
                tint = Color.White,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
private fun SpatialFitCard(
    product: Product,
    fitResult: FitResult,
    onAutoOptimize: () -> Unit
) {
    val statusColor = when (fitResult.status) {
        FitStatus.FITS -> SpatialColors.SuccessGreen
        FitStatus.FITS_WITH_RESTRICTIONS -> SpatialColors.WarningYellow
        FitStatus.DOES_NOT_FIT -> SpatialColors.DiscountRed
        FitStatus.INSUFFICIENT_DATA -> SpatialColors.TextSecondary
    }

    val statusText = when (fitResult.status) {
        FitStatus.FITS -> "Fits Comfortably"
        FitStatus.FITS_WITH_RESTRICTIONS -> "Tight Fit Restriction"
        FitStatus.DOES_NOT_FIT -> "Exceeds Room Space"
        FitStatus.INSUFFICIENT_DATA -> "Scanning..."
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Black.copy(alpha = 0.8f)),
        border = androidx.compose.foundation.BorderStroke(1.dp, statusColor.copy(alpha = 0.5f))
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = CircleShape,
                        color = statusColor
                    ) {
                        Icon(
                            Icons.Default.Check,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(14.dp).padding(2.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = statusText,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }

                Text(
                    text = product.formattedPrice,
                    color = SpatialColors.Primary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }

            fitResult.recommendations.firstOrNull()?.let { hint ->
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "💡 $hint",
                        color = Color.White.copy(alpha = 0.85f),
                        fontSize = 12.sp,
                        modifier = Modifier.weight(1f)
                    )

                    Button(
                        onClick = onAutoOptimize,
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = SpatialColors.Primary.copy(alpha = 0.9f)),
                        modifier = Modifier.height(30.dp).padding(start = 8.dp)
                    ) {
                        Text("⚡ Optimize", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
private fun ARBottomControls(
    uiState: ARStudioUiState,
    onPlaceClick: () -> Unit,
    onRotateClick: () -> Unit,
    onResetClick: () -> Unit,
    onSnapshotClick: () -> Unit,
    onSaveDesignClick: () -> Unit,
    onAddFurnitureClick: () -> Unit,
    onAddToCart: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // Primary Placement Button (Full Width, clear and prominent)
        Button(
            onClick = onPlaceClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (uiState.isProductPlaced) SpatialColors.SuccessGreen else SpatialColors.Primary
            ),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp)
        ) {
            Icon(
                if (uiState.isProductPlaced) Icons.Default.Check else Icons.Outlined.ViewInAr,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(22.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = if (uiState.isProductPlaced) "✓ Placed in Room (Tap to Re-place)" else "Place in Room",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Tool Deck Row (Icons evenly spaced across the bottom)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Black.copy(alpha = 0.65f), RoundedCornerShape(24.dp))
                .padding(horizontal = 8.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Reset Button
            IconButton(
                onClick = onResetClick,
                modifier = Modifier.size(44.dp)
            ) {
                Icon(
                    Icons.Default.Refresh,
                    contentDescription = "Reset Placement",
                    tint = Color.White
                )
            }

            // Rotate Button
            IconButton(
                onClick = onRotateClick,
                modifier = Modifier.size(44.dp)
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.RotateRight,
                    contentDescription = "Rotate",
                    tint = Color.White
                )
            }

            // Add Stacked Furniture Button (+)
            IconButton(
                onClick = onAddFurnitureClick,
                modifier = Modifier
                    .size(44.dp)
                    .background(
                        if (uiState.isAdditionalPlaced) SpatialColors.Primary else Color.Transparent,
                        CircleShape
                    )
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = "Add Furniture to Room",
                    tint = Color.White
                )
            }

            // Save Design Button
            IconButton(
                onClick = onSaveDesignClick,
                modifier = Modifier
                    .size(44.dp)
                    .background(
                        if (uiState.isDesignSaved) SpatialColors.SuccessGreen else Color.Transparent,
                        CircleShape
                    )
            ) {
                Icon(
                    Icons.Default.Bookmark,
                    contentDescription = "Save Design",
                    tint = Color.White
                )
            }

            // Snapshot / Capture Button
            IconButton(
                onClick = onSnapshotClick,
                modifier = Modifier.size(44.dp)
            ) {
                Icon(
                    Icons.Default.CameraAlt,
                    contentDescription = "Capture Snapshot",
                    tint = Color.White
                )
            }

            // Add to Cart Button
            IconButton(
                onClick = onAddToCart,
                modifier = Modifier
                    .size(44.dp)
                    .background(
                        if (uiState.isAddedToCart) SpatialColors.SuccessGreen else SpatialColors.PrimaryDark,
                        CircleShape
                    )
            ) {
                Icon(
                    if (uiState.isAddedToCart) Icons.Default.Check else Icons.Default.ShoppingCart,
                    contentDescription = "Add to Cart",
                    tint = Color.White
                )
            }
        }
    }
}

@Composable
private fun AddFurnitureDialog(
    products: List<Product>,
    onDismiss: () -> Unit,
    onSelectProduct: (Product) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("Add Furniture to Room", fontWeight = FontWeight.Bold)
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(340.dp)
            ) {
                Text(
                    "Stack multiple items to preview your entire living space together in AR:",
                    fontSize = 13.sp,
                    color = SpatialColors.TextSecondary
                )
                Spacer(modifier = Modifier.height(12.dp))

                if (products.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("No other products available in catalog", color = SpatialColors.TextSecondary)
                    }
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(products) { prod ->
                            Card(
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { onSelectProduct(prod) }
                            ) {
                                Row(
                                    modifier = Modifier.padding(8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    AsyncImage(
                                        model = prod.images.firstOrNull(),
                                        contentDescription = prod.name,
                                        modifier = Modifier
                                            .size(56.dp)
                                            .clip(RoundedCornerShape(8.dp))
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = prod.name,
                                            fontWeight = FontWeight.SemiBold,
                                            fontSize = 14.sp,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                        Text(
                                            text = prod.formattedPrice,
                                            color = SpatialColors.Primary,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 13.sp
                                        )
                                        Text(
                                            text = "${prod.widthCm.toInt()}×${prod.depthCm.toInt()}×${prod.heightCm.toInt()} cm",
                                            color = SpatialColors.TextSecondary,
                                            fontSize = 11.sp
                                        )
                                    }
                                    Button(
                                        onClick = { onSelectProduct(prod) },
                                        shape = RoundedCornerShape(8.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = SpatialColors.Primary),
                                        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                                        modifier = Modifier.height(34.dp)
                                    ) {
                                        Text("+ Add", fontSize = 12.sp, color = Color.White)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Close")
            }
        }
    )
}

@Composable
private fun SaveDesignDialog(
    defaultName: String,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var name by remember { mutableStateOf(defaultName) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Save Spatial Design") },
        text = {
            Column {
                Text(
                    "Name this room layout to view or restore it later from your profile:",
                    fontSize = 14.sp,
                    color = SpatialColors.TextSecondary
                )
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Design Name") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(name) },
                colors = ButtonDefaults.buttonColors(containerColor = SpatialColors.Primary)
            ) {
                Text("Save", color = Color.White)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
private fun CameraPermissionRequiredView(
    onRequestPermission: () -> Unit,
    onBackClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            Icons.Default.CameraAlt,
            contentDescription = null,
            tint = SpatialColors.Primary,
            modifier = Modifier.size(72.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Camera Access Required",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Spatial Shopping AI uses your camera to scan your room and project 3D furniture to true physical scale.",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.White.copy(alpha = 0.8f),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = onRequestPermission,
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = SpatialColors.Primary)
        ) {
            Text("Enable Camera", color = Color.White, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(12.dp))
        Button(
            onClick = onBackClick,
            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
        ) {
            Text("Go Back", color = Color.White.copy(alpha = 0.7f))
        }
    }
}
