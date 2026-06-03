package be.howest.howestprime.data.remote

import be.howest.howestprime.data.remote.api.SnackApi
import be.howest.howestprime.data.remote.dto.SnackDto
import retrofit2.http.GET
import javax.inject.Inject

class SnackApiClient  @Inject constructor(
    private val snackApi: SnackApi
){
    suspend fun getSnacks(): List<SnackDto> {
        return snackApi.getSnacks()
    }
}
