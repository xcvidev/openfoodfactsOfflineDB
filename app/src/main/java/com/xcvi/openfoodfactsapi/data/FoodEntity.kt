package com.xcvi.openfoodfactsapi.data

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.jetbrains.annotations.NotNull

@Serializable
data class ScanResponse(
    val product: ProductDTO? = null,
    val status: String = "",
)

@Serializable
data class ProductDTO(
    @SerialName("_id")
    val barcode: String = "",
    @SerialName("product_name")
    val name: String = "",
    @SerialName("brands")
    val brand: String = "",
    @SerialName("nutriments")
    val nutriments: NutrimentsDTO? = null,
)

@Serializable
data class NutrimentsDTO(
    @SerialName("energy-kcal_100g")
    val calories: Double = -1.0,
)

@Entity
class FoodEntity(
    @PrimaryKey
    val barcode: String = "",
    val name: String = "",
    val brand: String = "",

    var calories: Double = -1.0,
)

@Entity
class OfflineFoodEntity(
    @PrimaryKey
    val barcode: String = "",
    @NonNull
    val name: String = "",
    @NonNull
    val brand: String = ""
)