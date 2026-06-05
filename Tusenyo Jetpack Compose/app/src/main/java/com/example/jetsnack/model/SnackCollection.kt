/*
 * Copyright 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.jetsnack.model

import androidx.compose.runtime.Immutable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.random.Random

@Immutable
data class SnackCollection(val id: Long, val name: String, val snacks: List<Snack>, val type: CollectionType = CollectionType.Normal)

enum class CollectionType { Normal, Highlight }

/**
 * A fake repo
 */
object SnackRepo {
    fun getSnacks(): List<SnackCollection> = snackCollections
    fun getSnack(snackId: Long) = snacks.find { it.id == snackId }!!
    fun getRelated(@Suppress("UNUSED_PARAMETER") snackId: Long) = related
    fun getInspiredByCart() = inspiredByCart
    fun getFilters() = filters
    fun getPriceFilters() = priceFilters
    fun getCart(): List<OrderLine> = _cart.value
    fun getCartFlow(): StateFlow<List<OrderLine>> = _cart.asStateFlow()
    fun getSortFilters() = sortFilters
    fun getCategoryFilters() = categoryFilters
    fun getSortDefault() = sortDefault
    fun getLifeStyleFilters() = lifeStyleFilters

    private val _cart = MutableStateFlow(
        listOf<OrderLine>()
    )

    fun addSnackToCart(snack: Snack) {
        val currentCart = _cart.value.toMutableList()
        val existingIndex = currentCart.indexOfFirst { it.snack.id == snack.id }
        if (existingIndex != -1) {
            val existingItem = currentCart[existingIndex]
            currentCart[existingIndex] = existingItem.copy(count = existingItem.count + 1)
        } else {
            currentCart.add(OrderLine(snack, 1))
        }
        _cart.value = currentCart
    }

    fun increaseSnackCount(snackId: Long) {
        val currentCart = _cart.value.toMutableList()
        val index = currentCart.indexOfFirst { it.snack.id == snackId }
        if (index != -1) {
            val item = currentCart[index]
            currentCart[index] = item.copy(count = item.count + 1)
            _cart.value = currentCart
        }
    }

    fun decreaseSnackCount(snackId: Long) {
        val currentCart = _cart.value.toMutableList()
        val index = currentCart.indexOfFirst { it.snack.id == snackId }
        if (index != -1) {
            val item = currentCart[index]
            if (item.count > 1) {
                currentCart[index] = item.copy(count = item.count - 1)
            } else {
                currentCart.removeAt(index)
            }
            _cart.value = currentCart
        }
    }

    fun removeSnack(snackId: Long) {
        _cart.value = _cart.value.filter { it.snack.id != snackId }
    }

    fun clearCart() {
        _cart.value = emptyList()
    }

    private val _orders = MutableStateFlow<List<Order>>(emptyList())
    fun getOrderHistoryFlow(): StateFlow<List<Order>> = _orders.asStateFlow()

    fun checkout() {
        if (_cart.value.isNotEmpty()) {
            val newOrder = Order(
                id = System.currentTimeMillis(),
                items = _cart.value
            )
            _orders.value = listOf(newOrder) + _orders.value
            clearCart()
        }
    }
}

/**
 * Static data
 */

private val tastyTreats = SnackCollection(
    id = 1L,
    name = "Design Library",
    type = CollectionType.Highlight,
    snacks = snacks.subList(0, 15),
)

private val popular = SnackCollection(
    id = Random.nextLong(),
    name = "Popular Designs",
    snacks = snacks.subList(16, 25),
)

private val wfhFavs = tastyTreats.copy(
    id = Random.nextLong(),
    name = "Urban Collection",
)

private val newlyAdded = popular.copy(
    id = Random.nextLong(),
    name = "Newly Added",
)

private val exclusive = tastyTreats.copy(
    id = Random.nextLong(),
    name = "Exclusive Prints",
)

private val also = tastyTreats.copy(
    id = Random.nextLong(),
    name = "Customers also bought",
)

private val inspiredByCart = tastyTreats.copy(
    id = Random.nextLong(),
    name = "Inspired by your cart",
)

private val snackCollections = listOf(
    tastyTreats,
    popular,
    wfhFavs,
    newlyAdded,
    exclusive,
)

private val related = listOf(
    also.copy(id = Random.nextLong()),
    popular.copy(id = Random.nextLong()),
)

@Immutable
data class OrderLine(val snack: Snack, val count: Int)

@Immutable
data class Order(val id: Long, val items: List<OrderLine>)
