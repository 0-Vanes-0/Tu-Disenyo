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

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Immutable
import com.example.jetsnack.R
import kotlin.random.Random

@Immutable
data class Snack(
    val id: Long,
    val name: String,
    @DrawableRes
    val imageRes: Int,
    val price: Long,
    val tagline: String = "",
    val tags: Set<String> = emptySet(),
)

/**
 * Static data
 */

val snacks = listOf(
    Snack(
        id = 1L,
        name = "Hoodie Print",
        tagline = "Premium Quality",
        imageRes = R.drawable.hoodieprint,
        price = 2999,
    ),
    Snack(
        id = Random.nextLong(),
        name = "T-shirt Print",
        tagline = "Cotton 100%",
        imageRes = R.drawable.tshirtprint1,
        price = 1499,
    ),
    Snack(
        id = Random.nextLong(),
        name = "Cap Print",
        tagline = "Stylish accessories",
        imageRes = R.drawable.kepkaprint1,
        price = 999,
    ),
    Snack(
        id = Random.nextLong(),
        name = "Hoodie Print",
        tagline = "Urban Style",
        imageRes = R.drawable.hoodieprint2,
        price = 2999,
    ),
    Snack(
        id = Random.nextLong(),
        name = "T-shirt Print",
        tagline = "Daily wear",
        imageRes = R.drawable.tshirtprint2,
        price = 1499,
    ),
    Snack(
        id = Random.nextLong(),
        name = "Cap Print",
        tagline = "Street fashion",
        imageRes = R.drawable.kepkaprint2,
        price = 999,
    ),
    Snack(
        id = Random.nextLong(),
        name = "Hoodie Print",
        tagline = "Creative design",
        imageRes = R.drawable.hoodieprint3,
        price = 3299,
    ),
    Snack(
        id = Random.nextLong(),
        name = "T-shirt Print",
        tagline = "Limited edition",
        imageRes = R.drawable.tshirtprint3,
        price = 1799,
    ),
    Snack(
        id = Random.nextLong(),
        name = "Cap Print",
        tagline = "Trendy look",
        imageRes = R.drawable.kepkaprint3,
        price = 1199,
    ),
    Snack(
        id = Random.nextLong(),
        name = "Hoodie Print",
        tagline = "Urban Explorer",
        imageRes = R.drawable.hoodieprint4,
        price = 2999,
    ),
    Snack(
        id = Random.nextLong(),
        name = "T-shirt Print",
        tagline = "Minimalist",
        imageRes = R.drawable.tshirtprint4,
        price = 1499,
    ),
    Snack(
        id = Random.nextLong(),
        name = "Cap Print",
        tagline = "Classic cap",
        imageRes = R.drawable.kepkaprint4,
        price = 999,
    ),
    Snack(
        id = Random.nextLong(),
        name = "Hoodie Print",
        tagline = "Street Art",
        imageRes = R.drawable.hoodieprint5,
        price = 3299,
    ),
    Snack(
        id = Random.nextLong(),
        name = "Cap Print",
        tagline = "Modern vibe",
        imageRes = R.drawable.kepkaprint5,
        price = 1199,
    ),
    Snack(
        id = Random.nextLong(),
        name = "Hoodie Print",
        tagline = "Comfort First",
        imageRes = R.drawable.hoodieprint6,
        price = 2999,
    ),
    Snack(
        id = Random.nextLong(),
        name = "T-shirt Print",
        tagline = "Modern Art",
        imageRes = R.drawable.tshirtprint13,
        price = 1599,
    ),
    Snack(
        id = Random.nextLong(),
        name = "T-shirt Print",
        tagline = "Bold Design",
        imageRes = R.drawable.tshirtprint14,
        price = 1599,
    ),
    Snack(
        id = Random.nextLong(),
        name = "T-shirt Print",
        tagline = "Vintage Style",
        imageRes = R.drawable.tshirtprint15,
        price = 1699,
    ),
    Snack(
        id = Random.nextLong(),
        name = "T-shirt Print",
        tagline = "Creative Vibe",
        imageRes = R.drawable.tshirtprint18,
        price = 1699,
    ),
    Snack(
        id = Random.nextLong(),
        name = "Hoodie Print",
        tagline = "Relaxed Fit",
        imageRes = R.drawable.hoodieprint7,
        price = 3099,
    ),
    Snack(
        id = Random.nextLong(),
        name = "Hoodie Print",
        tagline = "Premium Heavyweight",
        imageRes = R.drawable.hoodieprint8,
        price = 3499,
    ),
    Snack(
        id = Random.nextLong(),
        name = "Hoodie Print",
        tagline = "Limited Release",
        imageRes = R.drawable.hoodieprint9,
        price = 3499,
    ),
    Snack(
        id = Random.nextLong(),
        name = "Hoodie Print",
        tagline = "New Collection",
        imageRes = R.drawable.hoodieprint231,
        price = 3299,
    ),
    Snack(
        id = Random.nextLong(),
        name = "T-shirt Print",
        tagline = "Graphic Tee",
        imageRes = R.drawable.tshirtprint122,
        price = 1899,
    ),
    Snack(
        id = Random.nextLong(),
        name = "T-shirt Print",
        tagline = "Special Edition",
        imageRes = R.drawable.tshirtprint1222,
        price = 1999,
    ),
)
