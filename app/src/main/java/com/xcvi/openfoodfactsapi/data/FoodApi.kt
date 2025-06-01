package com.xcvi.openfoodfactsapi.data

import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.features.HttpTimeout
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.request.get
import kotlinx.serialization.json.Json

class FoodApiImpl(private val client: HttpClient) : FoodApi {

    private val url = "https://world.openfoodfacts.org/api/v3/product"

    override suspend fun scanFood(barcode: String): ScanResponse {
        return try {

            val res: ScanResponse = client.get("$url/$barcode")
            if(res.product == null){
                ScanResponse(
                    status = "Error: Product Not Found",
                )
            } else {
                ScanResponse(
                    status = "Success",
                    product = res.product
                )
            }
        } catch (e: Exception) {
            ScanResponse(
                status = "Connection Error: ${e.message}",
            )
        }
    }

}

interface FoodApi {
    suspend fun scanFood(barcode: String): ScanResponse

    companion object {
        fun create(): FoodApi {
            return FoodApiImpl(
                client = HttpClient(Android) {

                    install(HttpTimeout) {
                        requestTimeoutMillis = 10000
                        connectTimeoutMillis = 8000
                        socketTimeoutMillis = 8000
                    }


                    install(JsonFeature) {
                        serializer = KotlinxSerializer(Json {
                            isLenient = true
                            ignoreUnknownKeys = true
                        })
                    }


                }
            )
        }
    }

}

