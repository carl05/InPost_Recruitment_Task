package pl.inpost.recruitmenttask.presentation.shipmentList

import java.time.ZonedDateTime


data class ShipmentNetworkVO(
    val number: String,
    val nameSender: String?,
    val status: String,
    val expiryDate: ZonedDateTime?,
    val storedDate: ZonedDateTime?,
    val pickUpDate: ZonedDateTime?,
    val highlight: Boolean
)

