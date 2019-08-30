package com.wmapp.ui.home.models

import com.fasterxml.jackson.annotation.JsonProperty

data class CarsFeed (

    @JsonProperty("carId")
    val carId : Int,
    @JsonProperty("title")
    val title : String,
    @JsonProperty("lat")
    val lat : Double,
    @JsonProperty("lon")
    val lon : Double,
    @JsonProperty("licencePlate")
    val licencePlate : String,
    @JsonProperty("fuelLevel")
    val fuelLevel : Int,
    @JsonProperty("vehicleStateId")
    val vehicleStateId : Int,
    @JsonProperty("vehicleTypeId")
    val vehicleTypeId : Int,
    @JsonProperty("pricingTime")
    val pricingTime : String,
    @JsonProperty("pricingParking")
    val pricingParking : String,
    @JsonProperty("reservationState")
    val reservationState : Int,
    @JsonProperty("isClean")
    val isClean : Boolean,
    @JsonProperty("isDamaged")
    val isDamaged : Boolean,
    @JsonProperty("distance")
    val distance : String,
    @JsonProperty("address")
    val address : String,
    @JsonProperty("zipCode")
    val zipCode : String,
    @JsonProperty("city")
    val city : String,
    @JsonProperty("locationId")
    val locationId : Int

)