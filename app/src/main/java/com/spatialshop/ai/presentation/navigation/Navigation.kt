package com.spatialshop.ai.presentation.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.spatialshop.ai.presentation.cart.CartScreen
import com.spatialshop.ai.presentation.categories.CategoriesScreen
import com.spatialshop.ai.presentation.favorites.FavoritesScreen
import com.spatialshop.ai.presentation.home.HomeScreen
import com.spatialshop.ai.presentation.productdetail.ProductDetailScreen
import com.spatialshop.ai.presentation.productlist.ProductListScreen
import com.spatialshop.ai.presentation.profile.ProfileScreen
import com.spatialshop.ai.presentation.theme.SpatialColors

/**
 * Main navigation composable with bottom navigation bar.
 * Hosts all screens and handles navigation between them.
 */
@Composable
fun SpatialShopNavigation() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Screens that show the bottom navigation bar
    val bottomNavScreens = listOf(
        Screen.Home.route,
        Screen.Categories.route,
        Screen.Favorites.route,
        Screen.Cart.route,
        Screen.Profile.route
    )
    val showBottomNav = currentRoute in bottomNavScreens

    Scaffold(
        bottomBar = {
            if (showBottomNav) {
                SpatialBottomNavBar(
                    currentRoute = currentRoute,
                    onNavigate = { screen ->
                        navController.navigate(screen.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        },
        containerColor = Color.White
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding),
            enterTransition = {
                fadeIn(animationSpec = tween(300))
            },
            exitTransition = {
                fadeOut(animationSpec = tween(300))
            }
        ) {
            // Bottom nav screens
            composable(Screen.Home.route) {
                HomeScreen(
                    onProductClick = { productId ->
                        navController.navigate(Screen.ProductDetail.createRoute(productId))
                    },
                    onCategoryClick = { category ->
                        navController.navigate(Screen.ProductList.createRoute(category))
                    },
                    onViewAllClick = { category ->
                        navController.navigate(Screen.ProductList.createRoute(category))
                    }
                )
            }

            composable(Screen.Categories.route) {
                CategoriesScreen(
                    onCategoryClick = { category ->
                        navController.navigate(Screen.ProductList.createRoute(category))
                    }
                )
            }

            composable(Screen.Favorites.route) {
                FavoritesScreen(
                    onProductClick = { productId ->
                        navController.navigate(Screen.ProductDetail.createRoute(productId))
                    }
                )
            }

            composable(Screen.Cart.route) {
                CartScreen(
                    onProductClick = { productId ->
                        navController.navigate(Screen.ProductDetail.createRoute(productId))
                    },
                    onCheckoutClick = {
                        navController.navigate(Screen.Checkout.route)
                    }
                )
            }

            composable(Screen.Profile.route) {
                ProfileScreen()
            }

            // Checkout Screen (Phase 6)
            composable(Screen.Checkout.route) {
                com.spatialshop.ai.presentation.checkout.CheckoutScreen(
                    onBackClick = { navController.popBackStack() },
                    onOrderSuccess = { orderId ->
                        navController.navigate(Screen.OrderSuccess.createRoute(orderId)) {
                            popUpTo(Screen.Cart.route) { inclusive = true }
                        }
                    }
                )
            }

            // Order Success Screen (Phase 6)
            composable(
                route = Screen.OrderSuccess.route,
                arguments = listOf(navArgument("orderId") { type = NavType.StringType })
            ) { backStackEntry ->
                val orderId = backStackEntry.arguments?.getString("orderId") ?: ""
                com.spatialshop.ai.presentation.checkout.OrderSuccessScreen(
                    orderId = orderId,
                    onContinueShopping = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Home.route) { inclusive = true }
                        }
                    }
                )
            }

            // Product listing by category
            composable(
                route = Screen.ProductList.route,
                arguments = listOf(navArgument("category") { type = NavType.StringType })
            ) { backStackEntry ->
                val category = backStackEntry.arguments?.getString("category") ?: ""
                ProductListScreen(
                    category = category,
                    onProductClick = { productId ->
                        navController.navigate(Screen.ProductDetail.createRoute(productId))
                    },
                    onBackClick = { navController.popBackStack() }
                )
            }

            // Product detail
            composable(
                route = Screen.ProductDetail.route,
                arguments = listOf(navArgument("productId") { type = NavType.StringType })
            ) { backStackEntry ->
                val productId = backStackEntry.arguments?.getString("productId") ?: ""
                ProductDetailScreen(
                    productId = productId,
                    onBackClick = { navController.popBackStack() },
                    onViewInRoom = { id ->
                        navController.navigate(Screen.ARStudio.createRoute(id))
                    }
                )
            }

            // AR Studio (Phase 3-5)
            composable(
                route = Screen.ARStudio.route,
                arguments = listOf(navArgument("productId") { type = NavType.StringType })
            ) { backStackEntry ->
                val productId = backStackEntry.arguments?.getString("productId") ?: ""
                com.spatialshop.ai.presentation.ar.ARStudioScreen(
                    productId = productId,
                    onBackClick = { navController.popBackStack() }
                )
            }
        }
    }
}

/**
 * Bottom navigation bar matching the UI reference.
 * 5 tabs: Home, Categories, Favorites, Cart (with badge), Profile
 */
@Composable
fun SpatialBottomNavBar(
    currentRoute: String?,
    onNavigate: (Screen) -> Unit
) {
    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 8.dp
    ) {
        bottomNavItems.forEach { item ->
            val isSelected = currentRoute == item.screen.route
            NavigationBarItem(
                selected = isSelected,
                onClick = { onNavigate(item.screen) },
                icon = {
                    BadgedBox(
                        badge = {
                            if (item.badgeCount > 0) {
                                Badge(
                                    containerColor = SpatialColors.DiscountRed,
                                    contentColor = Color.White
                                ) {
                                    Text(item.badgeCount.toString())
                                }
                            }
                        }
                    ) {
                        Icon(
                            imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                            contentDescription = item.label
                        )
                    }
                },
                label = {
                    Text(
                        text = item.label,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = SpatialColors.Primary,
                    selectedTextColor = SpatialColors.Primary,
                    unselectedIconColor = SpatialColors.NavUnselected,
                    unselectedTextColor = SpatialColors.NavUnselected,
                    indicatorColor = SpatialColors.PrimaryLight
                )
            )
        }
    }
}

/**
 * Placeholder screen for features not yet implemented.
 */
@Composable
fun PlaceholderScreen(
    title: String,
    message: String,
    onBack: () -> Unit = {}
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = androidx.compose.ui.Alignment.Center
    ) {
        Column(
            horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                style = androidx.compose.material3.MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = message,
                style = androidx.compose.material3.MaterialTheme.typography.bodyMedium,
                color = SpatialColors.TextSecondary
            )
        }
    }
}
