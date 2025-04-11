package data.repository

import android.util.Log
import data.api.RetrofitClient
import data.models.Brand
import data.models.Model
import data.models.Price
import data.models.Year

class CarRepository {
    private val apiService = RetrofitClient.instance

    suspend fun getBrands() : List<Brand> {
        return apiService.getBrands()
    }

    suspend fun getModels(id: Int) : List<Model> {
        val response = apiService.getModels(id)
        Log.d("API_RESPONSE", response.toString())
        return response.modelos
    }

    suspend fun getYears(id: Int, modelId: Int): List<Year>{
        return apiService.getYears(id, modelId)
    }

    suspend fun getPrice(id: Int, modelId: Int, year: String) : Price {
        return apiService.getPrice(id, modelId, year)
    }


}

