package com.spatialshop.ai.presentation.checkout

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.spatialshop.ai.presentation.theme.SpatialColors

@Composable
fun CheckoutScreen(
    onBackClick: () -> Unit,
    onOrderSuccess: (String) -> Unit,
    viewModel: CheckoutViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Top Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = SpatialColors.TextPrimary
                )
            }
            Text(
                text = "Checkout",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = SpatialColors.TextPrimary
            )
        }

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Delivery Address Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = SpatialColors.SurfaceVariant)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Default.LocationOn,
                                contentDescription = null,
                                tint = SpatialColors.Primary,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Delivery Address",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = SpatialColors.TextPrimary
                            )
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "${uiState.fullName} • ${uiState.phone}",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp,
                            color = SpatialColors.TextPrimary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "${uiState.address}, ${uiState.city}, ${uiState.state} - ${uiState.pincode}",
                            fontSize = 13.sp,
                            color = SpatialColors.TextSecondary,
                            lineHeight = 18.sp
                        )
                    }
                }
            }

            // Items Summary Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = SpatialColors.SurfaceVariant)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Order Items (${uiState.cartItems.size})",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = SpatialColors.TextPrimary
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        uiState.cartItems.forEach { item ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                AsyncImage(
                                    model = item.product.images.firstOrNull(),
                                    contentDescription = item.product.name,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .size(48.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = item.product.name,
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.SemiBold,
                                        maxLines = 1
                                    )
                                    Text(
                                        text = "Qty: ${item.quantity}",
                                        fontSize = 12.sp,
                                        color = SpatialColors.TextSecondary
                                    )
                                }
                                Text(
                                    text = item.formattedTotalPrice,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                            }
                        }
                    }
                }
            }

            // Payment Methods Selection
            item {
                Text(
                    text = "Payment Method",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = SpatialColors.TextPrimary,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    PaymentMethodRow(
                        icon = Icons.Default.PhoneAndroid,
                        method = PaymentMethod.UPI,
                        isSelected = uiState.selectedPayment == PaymentMethod.UPI,
                        onClick = { viewModel.selectPaymentMethod(PaymentMethod.UPI) }
                    )
                    PaymentMethodRow(
                        icon = Icons.Default.CreditCard,
                        method = PaymentMethod.CARD,
                        isSelected = uiState.selectedPayment == PaymentMethod.CARD,
                        onClick = { viewModel.selectPaymentMethod(PaymentMethod.CARD) }
                    )
                    PaymentMethodRow(
                        icon = Icons.Default.AccountBalance,
                        method = PaymentMethod.NET_BANKING,
                        isSelected = uiState.selectedPayment == PaymentMethod.NET_BANKING,
                        onClick = { viewModel.selectPaymentMethod(PaymentMethod.NET_BANKING) }
                    )
                    PaymentMethodRow(
                        icon = Icons.Default.LocalShipping,
                        method = PaymentMethod.COD,
                        isSelected = uiState.selectedPayment == PaymentMethod.COD,
                        onClick = { viewModel.selectPaymentMethod(PaymentMethod.COD) }
                    )
                }
            }

            // Price Details Breakdown
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = SpatialColors.SurfaceVariant)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Price Details",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Price (${uiState.cartItems.size} items)", color = SpatialColors.TextSecondary)
                            Text("₹ ${String.format("%,.0f", uiState.subtotal)}", fontWeight = FontWeight.SemiBold)
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Delivery Charges", color = SpatialColors.TextSecondary)
                            Text(
                                if (uiState.deliveryFee == 0.0) "FREE" else "₹ ${String.format("%,.0f", uiState.deliveryFee)}",
                                color = if (uiState.deliveryFee == 0.0) SpatialColors.SuccessGreen else SpatialColors.TextPrimary,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Total Payable", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                            Text(
                                "₹ ${String.format("%,.0f", uiState.total)}",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = SpatialColors.PrimaryDark
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }

        // Bottom CTA Bar
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shadowElevation = 10.dp,
            color = Color.White
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("Total Amount", fontSize = 12.sp, color = SpatialColors.TextSecondary)
                    Text(
                        "₹ ${String.format("%,.0f", uiState.total)}",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = SpatialColors.TextPrimary
                    )
                }

                Button(
                    onClick = { viewModel.placeOrder(onOrderSuccess) },
                    enabled = !uiState.isPlacingOrder && uiState.cartItems.isNotEmpty(),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = SpatialColors.Primary),
                    modifier = Modifier.height(50.dp)
                ) {
                    if (uiState.isPlacingOrder) {
                        CircularProgressIndicator(
                            color = Color.White,
                            modifier = Modifier.size(24.dp),
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text(
                            "Place Order",
                            color = Color.White,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun PaymentMethodRow(
    icon: ImageVector,
    method: PaymentMethod,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) SpatialColors.PrimaryLight.copy(alpha = 0.3f) else SpatialColors.SurfaceVariant
        ),
        border = if (isSelected) androidx.compose.foundation.BorderStroke(1.5.dp, SpatialColors.Primary) else null
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                icon,
                contentDescription = null,
                tint = if (isSelected) SpatialColors.PrimaryDark else SpatialColors.TextPrimary,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = method.displayName,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    color = SpatialColors.TextPrimary
                )
                Text(
                    text = method.subtitle,
                    fontSize = 12.sp,
                    color = SpatialColors.TextSecondary
                )
            }
            if (isSelected) {
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .background(SpatialColors.Primary, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.Check,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }
        }
    }
}
