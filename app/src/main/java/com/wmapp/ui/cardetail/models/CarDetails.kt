package com.wmapp.ui.cardetail.models

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Model for representing car attributes.
 */
data class CarDetails (

    @JsonProperty("carId")
    val carId : Int,
    @JsonProperty("title")
    val title: String ,
    @JsonProperty("isClean")
    val isClean : Boolean,
    @JsonProperty("isDamaged")
    val isDamaged : Boolean,
    @JsonProperty("licencePlate")
    val licencePlate : String,
    @JsonProperty("fuelLevel")
    val fuelLevel : String,
    @JsonProperty("vehicleStateId")
    val vehicleStateId: Int,
    @JsonProperty("hardwareId")
    val hardwareId : String,
    @JsonProperty("vehicleTypeId")
    val vehicleTypeId : Int,
    @JsonProperty("pricingTime")
    val pricingTime : String,
    @JsonProperty("pricingParking")
    val pricingParking: String,
    @JsonProperty("isActivatedByHardware")
    val isActivatedByHardware : Boolean,
    @JsonProperty("locationId")
    val locationId : Int,
    @JsonProperty("address")
    val address : String,
    @JsonProperty("zipCode")
    val zipCode : String,
    @JsonProperty("city")
    val city: String,
    @JsonProperty("lat")
    val lat: Double,
    @JsonProperty("lon")
    val lon : Double,
    @JsonProperty("reservationState")
    val reservationState : Int,
    @JsonProperty("damageDescription")
    val damageDescription : String,
    @JsonProperty("vehicleTypeImageUrl")
    val vehicleTypeImageUrl : String

)