package com.xcvi.openfoodfactsapi

import android.content.Context
import com.xcvi.openfoodfactsapi.data.FoodApi
import com.xcvi.openfoodfactsapi.data.FoodDatabase
import com.xcvi.openfoodfactsapi.data.FoodRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
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
    fun provideFoodDatabase(@ApplicationContext context: Context): FoodDatabase {
        return FoodDatabase.getInstance(context)
    }

    @Provides
    @Singleton
    fun provideFoodRepository(api: FoodApi, db: FoodDatabase): FoodRepository {
        return FoodRepository(api = api, db = db)
    }
}