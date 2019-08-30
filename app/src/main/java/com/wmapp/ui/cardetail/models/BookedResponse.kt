package com.wmapp.ui.cardetail.models

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Model for verifying booked response
 */
data class BookedResponse(

    @JsonProperty("reservationId")
    val reservationId: Int,
    @JsonProperty("carId")
    val carId: Int,
    @JsonProperty("cost")
    val cost: Int,
    @JsonProperty("drivenDistance")
    val drivenDistance: Int,
    @JsonProperty("licencePlate")
    val licencePlate: String,
    @JsonProperty("startAddress")
    val startAddress: String,
    @JsonProperty("userId")
    val userId: Int,
    @JsonProperty("isParkModeEnabled")
    val isParkModeEnabled: Boolean,
    @JsonProperty("damageDescription")
    val damageDescription: String,
    @JsonProperty("fuelCardPin")
    val fuelCardPin: String,
    @JsonProperty("endTime")
    val endTime: Int,
    @JsonProperty("startTime")
    val startTime: Int
)