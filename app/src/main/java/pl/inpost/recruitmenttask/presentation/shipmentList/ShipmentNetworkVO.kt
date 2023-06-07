package pl.inpost.recruitmenttask.presentation.shipmentList

import java.time.ZonedDateTime


data class ShipmentNetworkVO(
    val number: String,
    val senderName: String?,
    val senderEmail: String?,
    val status: String,
    val expiryDate: ZonedDateTime?,
    val storedDate: ZonedDateTime?,
    val pickUpDate: ZonedDateTime?,
    val highlight: Boolean,
    val operation: OperationsNetworkVO,
    val eventLog: MutableList<EventLogNetworkVO>,
)

/**
 * @param manualArchive - shipment can be manually (gesture) archived
 * @param delete - shipment can be manually deleted
 * @param collect - shipment can be remotely collected
 * @param highlight - shipment is ready to pick up - grouping
 * @param expandAvizo - shipment time to pick up can be expanded - show button
 * @param endOfWeekCollection - shipment will be available to pick up over the weekend - change colors
 */
data class OperationsNetworkVO(
    val manualArchive: Boolean,
    val delete: Boolean,
    val collect: Boolean,
    val highlight: Boolean,
    val expandAvizo: Boolean,
    val endOfWeekCollection: Boolean
)

data class EventLogNetworkVO(
    val name: String,
    val date: ZonedDateTime
)

