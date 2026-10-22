package com.spatialshop.ai.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.spatialshop.ai.domain.model.FurnitureCollection
import com.spatialshop.ai.domain.model.Product
import com.spatialshop.ai.presentation.theme.SpatialColors

/**
 * Home Screen - Main landing page matching the UI reference.
 * Features: Hero text, search bar, categories, deals banner,
 * best sellers, popular collections.
 */
@Composable
fun HomeScreen(
    onProductClick: (String) -> Unit,
    onCategoryClick: (String) -> Unit,
    onViewAllClick: (String) -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
            .padding(top = 16.dp)
    ) {
        // Offline Status Banner
        if (!uiState.isOnline) {
            Surface(
                color = SpatialColors.WarningYellow.copy(alpha = 0.2f),
                shape = RoundedCornerShape(10.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, SpatialColors.WarningYellow),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 4.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("📡", fontSize = 14.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "Offline Mode - Viewing cached catalog",
                        fontSize = 12.sp,
                        color = SpatialColors.TextPrimary,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }

        // Hero Title
        HeroTitle()

        Spacer(modifier = Modifier.height(16.dp))

        // Search Bar
        SearchBar(
            query = uiState.searchQuery,
            onQueryChanged = { viewModel.onSearchQueryChanged(it) }
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Categories
        SectionHeader(title = "Categories", actionText = "View All") {
            onViewAllClick("all")
        }
        Spacer(modifier = Modifier.height(8.dp))
        CategoryChips(
            categories = uiState.categories.map { it.name },
            selectedCategory = uiState.selectedCategory,
            onCategorySelected = { viewModel.onCategorySelected(it) }
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Deals Banner
        DealsBanner(
            onExploreClick = { onCategoryClick("Living Room") }
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Best Selling Items
        SectionHeader(title = "Best Selling Items", actionText = "View All") {
            onViewAllClick("bestsellers")
        }
        Spacer(modifier = Modifier.height(12.dp))
        BestSellersList(
            products = uiState.bestSellers,
            onProductClick = onProductClick,
            onToggleFavorite = { viewModel.toggleFavorite(it) }
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Popular Collections
        SectionHeader(title = "Popular Collections", actionText = "View All") {
            onViewAllClick("collections")
        }
        Spacer(modifier = Modifier.height(12.dp))
        CollectionsList(
            collections = uiState.collections,
            onCollectionClick = { onCategoryClick(it.name) }
        )

        Spacer(modifier = Modifier.height(24.dp))
    }
}

/**
 * Hero title: "Elegant & Modern Furniture For Your Dream Home"
 * "Modern" highlighted in mustard yellow.
 */
@Composable
fun HeroTitle() {
    val annotatedString = buildAnnotatedString {
        append("Elegant & ")
        withStyle(style = SpanStyle(color = SpatialColors.Primary)) {
            append("Modern")
        }
        append(" Furniture\nFor Your Dream Home")
    }

    Text(
        text = annotatedString,
        style = MaterialTheme.typography.headlineLarge.copy(
            fontSize = 26.sp,
            lineHeight = 34.sp
        ),
        fontWeight = FontWeight.Bold,
        color = SpatialColors.TextPrimary,
        modifier = Modifier.padding(horizontal = 20.dp)
    )
}

/**
 * Search bar with filter icon matching UI reference.
 */
@Composable
fun SearchBar(
    query: String,
    onQueryChanged: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = query,
            onValueChange = onQueryChanged,
            modifier = Modifier.weight(1f),
            placeholder = {
                Text(
                    "Search furniture, chairs, tables…",
                    color = SpatialColors.TextTertiary,
                    fontSize = 14.sp
                )
            },
            leadingIcon = {
                Icon(
                    Icons.Default.Search,
                    contentDescription = "Search",
                    tint = SpatialColors.TextSecondary
                )
            },
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = SpatialColors.Primary,
                unfocusedBorderColor = SpatialColors.Divider,
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White
            ),
            singleLine = true
        )
        Spacer(modifier = Modifier.width(10.dp))
        Surface(
            modifier = Modifier.size(52.dp),
            shape = RoundedCornerShape(12.dp),
            color = SpatialColors.TextPrimary,
            onClick = { /* Filter action */ }
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    Icons.Outlined.Tune,
                    contentDescription = "Filter",
                    tint = Color.White,
                    modifier = Modifier.size(22.dp)
                )
            }
        }
    }
}

/**
 * Horizontal scrolling category chips.
 * Selected chip is mustard yellow with white text.
 */
@Composable
fun CategoryChips(
    categories: List<String>,
    selectedCategory: String,
    onCategorySelected: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        categories.forEach { category ->
            val isSelected = category == selectedCategory
            Surface(
                modifier = Modifier.height(40.dp),
                shape = RoundedCornerShape(24.dp),
                color = if (isSelected) SpatialColors.ChipSelectedBg else SpatialColors.ChipUnselectedBg,
                onClick = { onCategorySelected(category) }
            ) {
                Box(
                    modifier = Modifier.padding(horizontal = 20.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = category,
                        color = if (isSelected) SpatialColors.ChipSelectedText else SpatialColors.ChipUnselectedText,
                        fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}

/**
 * Deals banner card matching the UI reference.
 * Shows "50% OFF" badge, promotional text, and arrow button.
 */
@Composable
fun DealsBanner(onExploreClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .height(180.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = SpatialColors.BannerBackground),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Discount badge
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = SpatialColors.DiscountRed
                ) {
                    Text(
                        text = "50% OFF",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }

                Column {
                    Text(
                        text = "Best Deals on\nModern Chairs",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = SpatialColors.TextPrimary,
                        lineHeight = 24.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Upgrade your space with\nstylish & comfortable chairs.",
                        style = MaterialTheme.typography.bodySmall,
                        color = SpatialColors.TextSecondary,
                        lineHeight = 18.sp
                    )
                }

                // Arrow button
                Surface(
                    modifier = Modifier.size(40.dp),
                    shape = CircleShape,
                    color = SpatialColors.Primary,
                    onClick = onExploreClick
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = "Explore deals",
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }
}

/**
 * Section header with title and "View All >" action.
 */
@Composable
fun SectionHeader(
    title: String,
    actionText: String = "View All",
    onActionClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = SpatialColors.TextPrimary
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable { onActionClick() }
        ) {
            Text(
                text = actionText,
                style = MaterialTheme.typography.bodyMedium,
                color = SpatialColors.TextSecondary,
                fontWeight = FontWeight.Medium
            )
            Icon(
                Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = null,
                tint = SpatialColors.TextSecondary,
                modifier = Modifier
                    .size(16.dp)
                    .padding(start = 2.dp)
            )
        }
    }
}

/**
 * Horizontal list of best-selling product cards.
 */
@Composable
fun BestSellersList(
    products: List<Product>,
    onProductClick: (String) -> Unit,
    onToggleFavorite: (String) -> Unit
) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        items(products, key = { it.id }) { product ->
            ProductCard(
                product = product,
                onClick = { onProductClick(product.id) },
                onToggleFavorite = { onToggleFavorite(product.id) }
            )
        }
    }
}

/**
 * Product card matching the UI reference.
 * Shows image, favorite icon, category, name, rating, and price.
 */
@Composable
fun ProductCard(
    product: Product,
    onClick: () -> Unit,
    onToggleFavorite: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(160.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            // Product image with favorite icon
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .background(SpatialColors.SurfaceVariant)
            ) {
                coil.compose.AsyncImage(
                    model = product.images.firstOrNull(),
                    contentDescription = product.name,
                    contentScale = androidx.compose.ui.layout.ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                // Favorite icon
                IconButton(
                    onClick = onToggleFavorite,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(4.dp)
                        .size(32.dp)
                        .background(Color.White.copy(alpha = 0.85f), androidx.compose.foundation.shape.CircleShape)
                ) {
                    Icon(
                        imageVector = if (product.isFavorite) Icons.Filled.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Favorite",
                        tint = if (product.isFavorite) SpatialColors.DiscountRed else SpatialColors.TextSecondary,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            // Product info
            Column(
                modifier = Modifier.padding(12.dp)
            ) {
                // Category
                Text(
                    text = product.category,
                    style = MaterialTheme.typography.labelSmall,
                    color = SpatialColors.TextSecondary
                )

                Spacer(modifier = Modifier.height(2.dp))

                // Product name
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = SpatialColors.TextPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Rating
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Filled.Star,
                        contentDescription = null,
                        tint = SpatialColors.StarYellow,
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        text = " ${product.rating}",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = SpatialColors.TextPrimary
                    )
                    Text(
                        text = " (${product.reviewCount})",
                        style = MaterialTheme.typography.labelSmall,
                        color = SpatialColors.TextSecondary
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Price
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = product.formattedPrice,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = SpatialColors.TextPrimary
                    )
                    if (product.formattedOriginalPrice != null) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = product.formattedOriginalPrice!!,
                            style = MaterialTheme.typography.labelSmall,
                            color = SpatialColors.TextTertiary,
                            textDecoration = TextDecoration.LineThrough
                        )
                    }
                }
            }
        }
    }
}

/**
 * Popular collections horizontal list.
 */
@Composable
fun CollectionsList(
    collections: List<FurnitureCollection>,
    onCollectionClick: (FurnitureCollection) -> Unit
) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        items(collections) { collection ->
            CollectionCard(
                collection = collection,
                onClick = { onCollectionClick(collection) }
            )
        }
    }
}

/**
 * Collection card with image, name, item count, and arrow.
 */
@Composable
fun CollectionCard(
    collection: FurnitureCollection,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(200.dp)
            .height(100.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SpatialColors.SurfaceVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = collection.name,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = SpatialColors.TextPrimary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${collection.itemCount}+ Items",
                    style = MaterialTheme.typography.labelSmall,
                    color = SpatialColors.TextSecondary
                )
            }
            Surface(
                modifier = Modifier.size(32.dp),
                shape = CircleShape,
                color = SpatialColors.Primary
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "View collection",
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}
