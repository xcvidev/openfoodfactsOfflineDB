package com.xcvi.openfoodfactsapi.data.utils
import android.content.Context
import com.xcvi.openfoodfactsapi.data.FoodDatabase
import com.xcvi.openfoodfactsapi.data.FoodEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.zip.ZipInputStream

private fun prepopulateDatabaseFromZippedCSV(context: Context, db: FoodDatabase) {
    CoroutineScope(Dispatchers.IO).launch {
        val batchSize = 1000
        val buffer = mutableListOf<FoodEntity>()

        try {
            val zipStream = ZipInputStream(context.assets.open("food_db.zip"))
            zipStream.use { zip ->
                var entry = zip.nextEntry
                while (entry != null) {
                    if (entry.name.endsWith(".csv")) {
                        BufferedReader(InputStreamReader(zip)).useLines { lines ->
                            val iterator = lines.iterator()

                            if (iterator.hasNext()) iterator.next() // skip header

                            while (iterator.hasNext()) {
                                val line = iterator.next()
                                val tokens = line.split(",")

                                if (tokens.size >= 3) {
                                    val barcode = tokens[0]
                                    val name = tokens[1]
                                    val brand = tokens[2]
                                    buffer.add(
                                        FoodEntity(
                                            name = name,
                                            brand = brand,
                                            barcode = barcode
                                        )
                                    )
                                    println("Added Food: $line")

                                } else {
                                    println("Malformed line: $line")
                                }

                                if (buffer.size >= batchSize) {
                                    db.dao.insertFoods(buffer)
                                    println("Inserted batch of ${buffer.size}")
                                    buffer.clear()
                                }
                            }

                            // Final batch
                            if (buffer.isNotEmpty()) {
                                db.dao.insertFoods(buffer)
                                println("Inserted final batch of ${buffer.size}")
                                buffer.clear()
                            }
                        }
                        break
                    }
                    entry = zip.nextEntry
                }
            }
        } catch (e: Exception) {
            println("Failed to prepopulate DB: ${e.message}")
        }
    }
}

fun prepopulateDatabaseFromCSV(csvName: String, context: Context, db: FoodDatabase) {
    CoroutineScope(Dispatchers.IO).launch {
        context.assets.open(csvName).bufferedReader().useLines { lines ->
            val iterator = lines.iterator()
            if (!iterator.hasNext()) return@useLines // skip empty file

            iterator.next() // skip header

            val buffer = mutableListOf<FoodEntity>()
            val batchSize = 1000

            while (iterator.hasNext()) {
                val tokens = iterator.next().split(",")
                if (tokens.size >= 3) {
                    val barcode = tokens[0]
                    val name = tokens[1]
                    val brand = tokens[2]
                    buffer.add(FoodEntity(
                        barcode = barcode,
                        name = name,
                        brand = brand
                    ))

                }

                if (buffer.size >= batchSize) {
                    db.dao.insertFoods(buffer)
                    buffer.clear()
                }
            }

            // Insert remaining
            if (buffer.isNotEmpty()) {
                db.dao.insertFoods(buffer)
            }
        }
    }
}


