package data.api

import ModelosResponse
import data.models.Brand
import data.models.Model
import data.models.Price
import data.models.Year
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @GET("marcas/")
    suspend fun getBrands(): List<Brand>

    @GET("marcas/{id}/modelos")
    suspend fun getModels(@Path("id") id: Int): ModelosResponse

    @GET("marcas/{id}/modelos/{modelId}/anos")
    suspend fun getYears(@Path("id") id: Int, @Path("modelId") modelId : Int) : List<Year>

    @GET("marcas/{id}/modelos/{modelId}/anos/{year}")
    suspend fun getPrice(@Path("id") id: Int, @Path("modelId") modelId : Int, @Path("year") year: String) : Price

}