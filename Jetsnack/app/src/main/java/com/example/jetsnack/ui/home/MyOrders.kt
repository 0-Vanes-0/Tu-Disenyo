package com.example.jetsnack.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.jetsnack.model.Order
import com.example.jetsnack.model.OrderLine
import com.example.jetsnack.model.SnackRepo
import com.example.jetsnack.ui.components.Divider
import com.example.jetsnack.ui.components.SnackImage
import com.example.jetsnack.ui.theme.JetsnackTheme
import com.example.jetsnack.ui.utils.formatPrice
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun MyOrders(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val orders by SnackRepo.getOrderHistoryFlow().collectAsStateWithLifecycle(emptyList())

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(JetsnackTheme.colors.uiBackground)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) {
                Text(text = "←", fontSize = 24.sp, color = JetsnackTheme.colors.textPrimary)
            }
            Text(
                text = "Order History",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = JetsnackTheme.colors.textPrimary,
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        if (orders.isEmpty()) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center
            ) {
                Text(
                    text = "No orders yet",
                    style = MaterialTheme.typography.bodyLarge,
                    color = JetsnackTheme.colors.textSecondary
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(bottom = 24.dp)
            ) {
                items(orders) { order ->
                    OrderItem(order = order)
                }
            }
        }
    }
}

@Composable
private fun OrderItem(order: Order) {
    val dateFormat = remember { SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault()) }
    val dateString = remember(order.id) { dateFormat.format(Date(order.id)) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
    ) {
        Text(
            text = "Order # ${order.id} • $dateString",
            style = MaterialTheme.typography.titleSmall,
            color = JetsnackTheme.colors.brand,
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
        )
        
        order.items.forEach { line ->
            HistoryItem(orderLine = line)
        }
        
        val total = order.items.sumOf { it.snack.price * it.count }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Total",
                style = MaterialTheme.typography.titleMedium,
                color = JetsnackTheme.colors.textPrimary,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = formatPrice(total),
                style = MaterialTheme.typography.titleMedium,
                color = JetsnackTheme.colors.textPrimary
            )
        }
        Divider(Modifier.padding(top = 8.dp))
    }
}

@Composable
private fun HistoryItem(
    orderLine: OrderLine,
    modifier: Modifier = Modifier
) {
    val snack = orderLine.snack
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 8.dp),
    ) {
        SnackImage(
            imageRes = snack.imageRes,
            imageCustomPath = snack.imageCustomPath,
            contentDescription = null,
            modifier = Modifier.size(80.dp)
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 16.dp),
        ) {
            Text(
                text = snack.name,
                style = MaterialTheme.typography.titleMedium,
                color = JetsnackTheme.colors.textSecondary,
                modifier = Modifier.padding(top = 4.dp)
            )
            Text(
                text = "${orderLine.count} x ${formatPrice(snack.price)}",
                style = MaterialTheme.typography.bodyMedium,
                color = JetsnackTheme.colors.textHelp,
                modifier = Modifier.padding(top = 4.dp)
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = formatPrice(snack.price * orderLine.count),
                style = MaterialTheme.typography.titleSmall,
                color = JetsnackTheme.colors.textPrimary
            )
        }
    }
}
