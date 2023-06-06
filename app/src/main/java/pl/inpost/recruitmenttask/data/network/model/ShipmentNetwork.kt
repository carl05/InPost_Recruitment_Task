package pl.inpost.recruitmenttask.data.network.model

import java.time.ZonedDateTime

data class ShipmentNetwork(
    val number: String,
    val shipmentType: String,
    val status: String,
    val eventLog: List<EventLogNetwork>,
    val openCode: String?,
    val expiryDate: ZonedDateTime?,
    val storedDate: ZonedDateTime?,
    val pickUpDate: ZonedDateTime?,
    val receiver: CustomerNetwork?,
    val sender: CustomerNetwork?,
    val operations: OperationsNetwork
)

data class CustomerNetwork(
    val email: String?,
    val phoneNumber: String?,
    val name: String?
)

data class EventLogNetwork(
    val name: String,
    val date: ZonedDateTime
)

/**
 * @param manualArchive - shipment can be manually (gesture) archived
 * @param delete - shipment can be manually deleted
 * @param collect - shipment can be remotely collected
 * @param highlight - shipment is ready to pick up - grouping
 * @param expandAvizo - shipment time to pick up can be expanded - show button
 * @param endOfWeekCollection - shipment will be available to pick up over the weekend - change colors
 */
data class OperationsNetwork(
    val manualArchive: Boolean,
    val delete: Boolean,
    val collect: Boolean,
    val highlight: Boolean,
    val expandAvizo: Boolean,
    val endOfWeekCollection: Boolean
)
