package com.xcvi.openfoodfactsapi.data

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.Upsert
import androidx.sqlite.db.SupportSQLiteDatabase
import com.xcvi.openfoodfactsapi.data.utils.prepopulateDatabaseFromZippedCSV
import kotlinx.coroutines.flow.Flow

@Database(
    entities = [FoodEntity::class, OfflineFoodEntity::class],
    version = 1,
    //exportSchema = false
)
abstract class FoodDatabase : RoomDatabase() {
    abstract val dao: FoodDao

    companion object {

        @Volatile
        private var INSTANCE: FoodDatabase? = null

        fun getInstance(context: Context): FoodDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }


        private fun buildDatabase(context: Context): FoodDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                FoodDatabase::class.java,
                "food_db.db"
            ).createFromAsset("food_db.db").build()
                /*
                .addCallback(
                object : Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        prepopulateDatabaseFromZippedCSV(context = context, db = getInstance(context))
                    }
                }
            )*/

        }
    }

}

@Dao
interface FoodDao {
    @Upsert
    suspend fun insertFood(food: FoodEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertFoods(foods: List<FoodEntity>)

    @Query("SELECT * FROM foodentity WHERE barcode = :barcode")
    suspend fun getFoodByBarcode(barcode: String): FoodEntity?

    @Query("SELECT * FROM foodentity")
    fun observeFoods(): Flow<List<FoodEntity>>

    @Query("SELECT * FROM foodentity WHERE name LIKE '%' || :name || '%'")
    suspend fun searchFood(name: String): List<FoodEntity>

    @Delete
    suspend fun deleteFood(food: FoodEntity)
}