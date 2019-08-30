package com.wmapp.networking

import com.wmapp.ui.cardetail.models.BookCar
import com.wmapp.ui.cardetail.models.BookedResponse
import com.wmapp.ui.cardetail.models.CarDetails
import com.wmapp.ui.home.models.CarsFeed
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.*

/**
 * Retrofit service for different end points witn input data.
 */
interface NetworkService {
    @GET("wunderfleet-recruiting-dev/cars.json")
    suspend fun getAllCarsFeed(): Response<ArrayList<CarsFeed>>

    @GET("wunderfleet-recruiting-dev/cars/{carID}")
    suspend fun getCarsDetails( @Path("carID") carID: Int): Response<CarDetails>


    @POST
    suspend fun bookCarRequest(@Url newURL:String,
                               @Header("Authorization") authHeader:String,
                               @Body bookCar: BookCar) : Response<BookedResponse>
}