package com.laomuji1999.compose.core.logic.model.response

import kotlinx.serialization.Serializable

@Serializable
data class ProductResponse(
    val total: Int,
    val products: List<Product>,
) {
    @Serializable
    class Product(
        val id: Int,
        val title: String,
        val price: Double,
        val description: String,
    )
}