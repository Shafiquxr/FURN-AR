package com.spatialshop.ai.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Navigation routes for Spatial Shopping AI.
 */
sealed class Screen(val route: String) {
    // Bottom nav screens
    data object Home : Screen("home")
    data object Categories : Screen("categories")
    data object Favorites : Screen("favorites")
    data object Cart : Screen("cart")
    data object Profile : Screen("profile")

    // Detail screens
    data object ProductList : Screen("product_list/{category}") {
        fun createRoute(category: String) = "product_list/$category"
    }
    data object ProductDetail : Screen("product_detail/{productId}") {
        fun createRoute(productId: String) = "product_detail/$productId"
    }

    // AR screens
    data object ARStudio : Screen("ar_studio/{productId}") {
        fun createRoute(productId: String) = "ar_studio/$productId"
    }
    data object FitAnalysis : Screen("fit_analysis")

    // Checkout flow
    data object Checkout : Screen("checkout")
    data object OrderSuccess : Screen("order_success/{orderId}") {
        fun createRoute(orderId: String) = "order_success/$orderId"
    }

    // Other screens
    data object SavedDesigns : Screen("saved_designs")
    data object Settings : Screen("settings")
}

/**
 * Bottom navigation bar items.
 */
data class BottomNavItem(
    val screen: Screen,
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val badgeCount: Int = 0
)

/**
 * List of bottom navigation items matching the UI reference.
 */
val bottomNavItems = listOf(
    BottomNavItem(
        screen = Screen.Home,
        label = "Home",
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home
    ),
    BottomNavItem(
        screen = Screen.Categories,
        label = "Categories",
        selectedIcon = Icons.Filled.Home, // Will use custom grid icon
        unselectedIcon = Icons.Outlined.Home
    ),
    BottomNavItem(
        screen = Screen.Favorites,
        label = "Favorites",
        selectedIcon = Icons.Filled.Favorite,
        unselectedIcon = Icons.Outlined.FavoriteBorder
    ),
    BottomNavItem(
        screen = Screen.Cart,
        label = "Cart",
        selectedIcon = Icons.Filled.ShoppingCart,
        unselectedIcon = Icons.Outlined.ShoppingCart,
        badgeCount = 0 // Updated dynamically
    ),
    BottomNavItem(
        screen = Screen.Profile,
        label = "Profile",
        selectedIcon = Icons.Filled.Person,
        unselectedIcon = Icons.Outlined.Person
    )
)
