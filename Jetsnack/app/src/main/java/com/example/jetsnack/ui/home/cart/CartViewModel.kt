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

package com.example.jetsnack.ui.home.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.jetsnack.R
import com.example.jetsnack.model.OrderLine
import com.example.jetsnack.model.SnackRepo
import com.example.jetsnack.model.SnackbarManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Holds the contents of the cart and allows changes to it.
 *
 * TODO: Move data to Repository so it can be displayed and changed consistently throughout the app.
 */
class CartViewModel(private val snackbarManager: SnackbarManager, private val snackRepository: SnackRepo) : ViewModel() {

    val orderLines: StateFlow<List<OrderLine>> = snackRepository.getCartFlow()

    private val _checkoutState = MutableStateFlow(CheckoutState.None)
    val checkoutState: StateFlow<CheckoutState> = _checkoutState.asStateFlow()

    // Logic to show errors every few requests
    private var requestCount = 0
    private fun shouldRandomlyFail(): Boolean = ++requestCount % 5 == 0

    fun increaseSnackCount(snackId: Long) {
        if (!shouldRandomlyFail()) {
            snackRepository.increaseSnackCount(snackId)
        } else {
            snackbarManager.showMessage(R.string.cart_increase_error)
        }
    }

    fun decreaseSnackCount(snackId: Long) {
        if (!shouldRandomlyFail()) {
            snackRepository.decreaseSnackCount(snackId)
        } else {
            snackbarManager.showMessage(R.string.cart_decrease_error)
        }
    }

    fun removeSnack(snackId: Long) {
        snackRepository.removeSnack(snackId)
    }

    fun onCheckoutClick() {
        viewModelScope.launch {
            _checkoutState.value = CheckoutState.Loading
            delay(10000L) // Wait for 10 seconds
            _checkoutState.value = CheckoutState.Success
        }
    }

    fun onDismissCheckout() {
        if (_checkoutState.value == CheckoutState.Success) {
            snackRepository.checkout()
        }
        _checkoutState.value = CheckoutState.None
    }

    enum class CheckoutState {
        None, Loading, Success
    }

    /**
     * Factory for CartViewModel that takes SnackbarManager as a dependency
     */
    companion object {
        fun provideFactory(
            snackbarManager: SnackbarManager = SnackbarManager,
            snackRepository: SnackRepo = SnackRepo,
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return CartViewModel(snackbarManager, snackRepository) as T
            }
        }
    }
}
