package com.xcvi.openfoodfactsapi.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.Constraints
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.xcvi.openfoodfactsapi.data.FoodEntity
import com.xcvi.openfoodfactsapi.data.FoodRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FoodViewModel @Inject constructor(
    private val foodRepository: FoodRepository,
): ViewModel()  {

    val foods: MutableStateFlow<List<FoodEntity>> = MutableStateFlow(emptyList())
    val size: MutableStateFlow<Int> = MutableStateFlow(0)

    init {
        viewModelScope.launch {
            foodRepository.observeSize().collect {
                size.value = it
            }
        }
    }

    fun scan(barcode: String) {
        foods.value = emptyList()
        viewModelScope.launch {
            val res = foodRepository.scanFood(barcode)
            foods.value = foods.value + res
        }
    }

    fun search(query: String) {
        foods.value = emptyList()
        viewModelScope.launch {
            val res = foodRepository.searchFood(query)
            foods.value = foods.value + res
        }

    }
}