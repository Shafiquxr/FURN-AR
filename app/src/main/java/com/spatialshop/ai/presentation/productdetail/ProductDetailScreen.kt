package com.spatialshop.ai.presentation.productdetail

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Height
import androidx.compose.material.icons.outlined.OpenInFull
import androidx.compose.material.icons.outlined.SwitchLeft
import androidx.compose.material.icons.outlined.ViewInAr
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import com.spatialshop.ai.data.repository.SampleData
import com.spatialshop.ai.domain.model.Product
import com.spatialshop.ai.domain.model.ProductColor
import com.spatialshop.ai.presentation.theme.SpatialColors

/**
 * Product Detail Screen matching the UI reference.
 * Shows image gallery, color picker, dimensions, description,
 * and "View in My Room" CTA button.
 */
@Composable
fun ProductDetailScreen(
    productId: String,
    onBackClick: () -> Unit,
    onViewInRoom: (String) -> Unit,
    viewModel: ProductDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val product = uiState.product ?: SampleData.getProductById(productId)

    if (product == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Product not found", color = SpatialColors.TextSecondary)
        }
        return
    }

    var selectedColorIndex by remember { mutableIntStateOf(0) }
    var isDescriptionExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Scrollable content
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            // Top bar with back, favorite, share
            TopBar(
                isFavorite = uiState.isFavorite,
                onBackClick = onBackClick,
                onToggleFavorite = { viewModel.toggleFavorite() }
            )

            // Image Gallery
            ImageGallery(product = product)

            Spacer(modifier = Modifier.height(16.dp))

            // Product Info
            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                // Product Name
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = SpatialColors.TextPrimary
                )

                Spacer(modifier = Modifier.height(6.dp))

                // Category + Rating
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = product.category,
                        style = MaterialTheme.typography.bodyMedium,
                        color = SpatialColors.TextSecondary
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Icon(
                        Icons.Filled.Star,
                        contentDescription = null,
                        tint = SpatialColors.StarYellow,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = " ${product.rating}",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = SpatialColors.TextPrimary
                    )
                    Text(
                        text = " (${product.reviewCount} Reviews)",
                        style = MaterialTheme.typography.bodySmall,
                        color = SpatialColors.TextSecondary
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Price Row
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = product.formattedPrice,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = SpatialColors.TextPrimary
                    )
                    if (product.formattedOriginalPrice != null) {
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = product.formattedOriginalPrice!!,
                            style = MaterialTheme.typography.bodyLarge,
                            color = SpatialColors.TextTertiary,
                            textDecoration = TextDecoration.LineThrough
                        )
                    }
                    if (product.discountLabel != null) {
                        Spacer(modifier = Modifier.width(10.dp))
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = SpatialColors.DiscountRed.copy(alpha = 0.1f)
                        ) {
                            Text(
                                text = product.discountLabel!!,
                                color = SpatialColors.DiscountRed,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Color Picker
                if (product.colors.isNotEmpty()) {
                    ColorPicker(
                        colors = product.colors,
                        selectedIndex = selectedColorIndex,
                        onColorSelected = { selectedColorIndex = it }
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                }

                // Dimensions Row
                DimensionsRow(product = product)

                Spacer(modifier = Modifier.height(20.dp))

                // Description
                DescriptionSection(
                    description = product.description,
                    isExpanded = isDescriptionExpanded,
                    onToggle = { isDescriptionExpanded = !isDescriptionExpanded }
                )

                Spacer(modifier = Modifier.height(20.dp))
            }
        }

        // Bottom Action Bar
        BottomActionBar(
            isAddedToCart = uiState.isAddedToCart,
            onAddToCart = { viewModel.addToCart() },
            onViewInRoom = { onViewInRoom(product.id) }
        )
    }
}

@Composable
private fun TopBar(
    isFavorite: Boolean,
    onBackClick: () -> Unit,
    onToggleFavorite: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onBackClick) {
            Icon(
                Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = SpatialColors.TextPrimary
            )
        }
        Row {
            IconButton(onClick = onToggleFavorite) {
                Icon(
                    if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = "Favorite",
                    tint = if (isFavorite) SpatialColors.DiscountRed else SpatialColors.TextPrimary
                )
            }
            IconButton(onClick = { /* Share */ }) {
                Icon(
                    Icons.Default.Share,
                    contentDescription = "Share",
                    tint = SpatialColors.TextPrimary
                )
            }
        }
    }
}

/**
 * Image gallery with main image and thumbnail strip on the right.
 */
@Composable
private fun ImageGallery(product: Product) {
    var selectedImageIndex by remember { mutableIntStateOf(0) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(320.dp)
            .padding(horizontal = 20.dp)
    ) {
        // Main image
        Card(
            modifier = Modifier
                .weight(1f)
                .fillMaxSize(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = SpatialColors.SurfaceVariant),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                SpatialColors.PrimaryLight.copy(alpha = 0.4f),
                                SpatialColors.SurfaceVariant
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "🛋️", fontSize = 80.sp)

                // 3D icon overlay
                Surface(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(12.dp),
                    shape = RoundedCornerShape(8.dp),
                    color = SpatialColors.TextPrimary.copy(alpha = 0.7f)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Outlined.ViewInAr,
                            contentDescription = "3D View",
                            tint = Color.White,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("3D", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.width(10.dp))

        // Thumbnail strip
        Column(
            modifier = Modifier.fillMaxSize().width(60.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            product.images.take(4).forEachIndexed { index, _ ->
                Card(
                    modifier = Modifier
                        .size(60.dp)
                        .then(
                            if (index == selectedImageIndex)
                                Modifier.border(2.dp, SpatialColors.Primary, RoundedCornerShape(10.dp))
                            else Modifier
                        )
                        .clickable { selectedImageIndex = index },
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(containerColor = SpatialColors.SurfaceVariant)
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("🪑", fontSize = 24.sp)
                    }
                }
            }
        }
    }
}

/**
 * Color picker with circle swatches and selected color name.
 */
@Composable
private fun ColorPicker(
    colors: List<ProductColor>,
    selectedIndex: Int,
    onColorSelected: (Int) -> Unit
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Color",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = SpatialColors.TextPrimary
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = colors[selectedIndex].name,
                    style = MaterialTheme.typography.bodyMedium,
                    color = SpatialColors.TextSecondary
                )
                Icon(
                    Icons.Default.Share, // Chevron right
                    contentDescription = null,
                    tint = SpatialColors.TextSecondary,
                    modifier = Modifier.size(16.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            colors.forEachIndexed { index, color ->
                val colorValue = try {
                    Color(android.graphics.Color.parseColor(color.hexCode))
                } catch (e: Exception) {
                    SpatialColors.Primary
                }
                val isSelected = index == selectedIndex

                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .then(
                            if (isSelected) Modifier.border(2.dp, SpatialColors.TextPrimary, CircleShape)
                            else Modifier
                        )
                        .padding(3.dp)
                        .clip(CircleShape)
                        .background(colorValue)
                        .clickable { onColorSelected(index) }
                )
            }
        }
    }
}

/**
 * Product dimensions displayed in three cards.
 * Width | Depth | Height with icons.
 */
@Composable
private fun DimensionsRow(product: Product) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        DimensionCard(
            icon = Icons.Outlined.SwitchLeft,
            label = "Width",
            value = "${product.widthCm.toInt()} cm",
            modifier = Modifier.weight(1f)
        )
        DimensionCard(
            icon = Icons.Outlined.OpenInFull,
            label = "Depth",
            value = "${product.depthCm.toInt()} cm",
            modifier = Modifier.weight(1f)
        )
        DimensionCard(
            icon = Icons.Outlined.Height,
            label = "Height",
            value = "${product.heightCm.toInt()} cm",
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun DimensionCard(
    icon: ImageVector,
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = SpatialColors.SurfaceVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                icon,
                contentDescription = label,
                tint = SpatialColors.TextSecondary,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = SpatialColors.TextSecondary
            )
            Text(
                text = value,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = SpatialColors.TextPrimary
            )
        }
    }
}

/**
 * Expandable description section.
 */
@Composable
private fun DescriptionSection(
    description: String,
    isExpanded: Boolean,
    onToggle: () -> Unit
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Description",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = SpatialColors.TextPrimary
            )
            Text(
                text = if (isExpanded) "Show Less" else "Read More",
                style = MaterialTheme.typography.bodySmall,
                color = SpatialColors.TextSecondary,
                modifier = Modifier.clickable(onClick = onToggle)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = description,
            style = MaterialTheme.typography.bodyMedium,
            color = SpatialColors.TextSecondary,
            maxLines = if (isExpanded) Int.MAX_VALUE else 3,
            overflow = TextOverflow.Ellipsis,
            lineHeight = 22.sp
        )
    }
}

/**
 * Bottom action bar with "Add to Cart" and "View in My Room" buttons.
 */
@Composable
private fun BottomActionBar(
    isAddedToCart: Boolean = false,
    onAddToCart: () -> Unit,
    onViewInRoom: () -> Unit
) {
    HorizontalDivider(color = SpatialColors.Divider, thickness = 0.5.dp)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 20.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Add to Cart - Outlined
        OutlinedButton(
            onClick = onAddToCart,
            modifier = Modifier
                .weight(1f)
                .height(52.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = if (isAddedToCart) SpatialColors.SuccessGreen else SpatialColors.TextPrimary
            )
        ) {
            Icon(
                Icons.Default.ShoppingCart,
                contentDescription = null,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                if (isAddedToCart) "Added ✓" else "Add to Cart",
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp
            )
        }

        // View in My Room - Filled Yellow (Primary CTA)
        Button(
            onClick = onViewInRoom,
            modifier = Modifier
                .weight(1f)
                .height(52.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = SpatialColors.Primary,
                contentColor = Color.White
            ),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
        ) {
            Icon(
                Icons.Outlined.ViewInAr,
                contentDescription = null,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                "View in My Room",
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp
            )
        }
    }
}
