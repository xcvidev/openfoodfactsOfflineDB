package com.xcvi.openfoodfactsapi.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FoodRepository(
    private val api: FoodApi,
    private val db: FoodDatabase,
) {

    suspend fun observeSize(): Flow<Int> {
        return db.dao.observeFoods().map { it
            it.size
        }
    }

    suspend fun scanFood(barcode: String): FoodEntity {
        val localFood = db.dao.getFoodByBarcode(barcode)
        if (localFood != null && localFood.calories > -1.0) {
            return localFood
        } else {
            val res = api.scanFood(barcode)
            return if (res.product != null) {
                val food = FoodEntity(
                    name = res.product.name,
                    brand = res.product.brand,
                    barcode = res.product.barcode,
                    calories = res.product.nutriments?.calories ?: 0.0
                )
                db.dao.insertFood(food)
                food
            } else {
                FoodEntity(name = "Error: Product Not Found")
            }
        }
    }

    suspend fun searchFood(query: String): List<FoodEntity> {
        return db.dao.searchFood(query)
    }

}