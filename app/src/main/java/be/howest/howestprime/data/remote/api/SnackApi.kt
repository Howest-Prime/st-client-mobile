package be.howest.howestprime.data.remote.api

import be.howest.howestprime.data.remote.dto.SnackDto
import retrofit2.http.GET

interface SnackApi {
    @GET("snacks.json")
    suspend fun getSnacks(): List<SnackDto>
}