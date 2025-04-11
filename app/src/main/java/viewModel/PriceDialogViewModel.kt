package viewModel

import android.util.Log
import androidx.compose.ui.platform.LocalGraphicsContext
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import data.models.Brand
import data.models.Model
import data.models.Year
import data.models.Price
import data.repository.CarRepository
import PriceDialog
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PriceDialogViewModel : ViewModel() {
    private val repository = CarRepository()

    private val _brands = MutableStateFlow<List<Brand>>(emptyList())
    val brands: StateFlow<List<Brand>> = _brands

    private val _models = MutableStateFlow<List<Model>>(emptyList())
    val models: StateFlow<List<Model>> = _models

    private val _years = MutableStateFlow<List<Year>>(emptyList())
    val years: StateFlow<List<Year>> = _years

    private val _price = MutableStateFlow<Price?>(null)
    val price: StateFlow<Price?> = _price

    private var brandJob: Job? = null
    private var modelJob: Job? = null
    private var yearJob: Job? = null
    private var priceJob: Job? = null

    fun loadBrands() {
        brandJob?.cancel()
        brandJob = viewModelScope.launch {
            runCatching {
                delay(3000)
                repository.getBrands()
            }.onSuccess {
                _brands.value = it
            }.onFailure { exception ->
                Log.e("fetch", "Error fetching brands", exception)
            }
        }
    }

    fun loadModels(brandId: Int) {
        modelJob?.cancel()
        modelJob = viewModelScope.launch {
            runCatching {
                delay(3000)
                repository.getModels(brandId)
            }.onSuccess {
                _models.value = it
            }.onFailure { exception ->
                Log.e("fetch", "Error fetching models", exception)
            }
        }
    }

    fun loadYears(brandId: Int, modelId: Int) {
        yearJob?.cancel()
        yearJob = viewModelScope.launch {
            runCatching {
                delay(3000)

                repository.getYears(brandId, modelId)
            }.onSuccess {
                _years.value = it
            }.onFailure { exception ->
                Log.e("fetch", "Error fetching years", exception)
            }
        }
    }

    fun loadPrice(brandId: Int, modelId: Int, year: String) {
        priceJob?.cancel()
        priceJob = viewModelScope.launch {
            runCatching {
                delay(3000)

                repository.getPrice(brandId, modelId, year)
            }.onSuccess {
                _price.value = it
            }.onFailure { exception ->
                Log.e("fetch", "Error fetching price", exception)
            }
        }
    }

    fun cancelJobs() {
        brandJob?.cancel()
        modelJob?.cancel()
        yearJob?.cancel()
        priceJob?.cancel()
        Log.d("cancel", "cancel")
    }
    fun clearModels() {
        _models.value = emptyList()
    }

    fun clearYears() {
        _years.value = emptyList()
    }
    fun clearPrice() {
        _price.value = null
    }
}