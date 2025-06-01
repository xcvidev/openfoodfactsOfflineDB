package com.xcvi.openfoodfactsapi

import android.content.Context
import androidx.lifecycle.ViewModelProvider.NewInstanceFactory.Companion.instance
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.RoomDatabase.Callback
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.work.WorkManager
import com.xcvi.openfoodfactsapi.data.FoodApi
import com.xcvi.openfoodfactsapi.data.FoodDatabase
import com.xcvi.openfoodfactsapi.data.FoodRepository
import com.xcvi.openfoodfactsapi.data.utils.prepopulateDatabaseFromZippedCSV
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FoodModule {

    @Provides
    @Singleton
    fun provideFoodApi(): FoodApi{
        return FoodApi.create()
    }

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): FoodDatabase {
        return FoodDatabase.getInstance(context)
    }


    @Provides
    @Singleton
    fun provideFoodRepository(api: FoodApi, db: FoodDatabase): FoodRepository {
        return FoodRepository(api = api, db = db)
    }
}