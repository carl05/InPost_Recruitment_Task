package pl.inpost.recruitmenttask.domain

import pl.inpost.recruitmenttask.data.network.api.ShipmentApi
import pl.inpost.recruitmenttask.data.network.model.ShipmentNetwork
import pl.inpost.recruitmenttask.presentation.shipmentList.EventLogNetworkVO
import pl.inpost.recruitmenttask.presentation.shipmentList.OperationsNetworkVO
import pl.inpost.recruitmenttask.presentation.shipmentList.ShipmentVO
import javax.inject.Inject

class ShipmentsUseCase @Inject constructor(private val shipmentApi: ShipmentApi) {
    suspend fun getShipments(): List<ShipmentVO> {
        return shipmentApi.getShipments().map { it.toVO() }
    }

}

private fun ShipmentNetwork.toVO() = ShipmentVO(
    number = this.number,
    senderName = this.sender?.name,
    status = this.status,
    expiryDate = this.expiryDate,
    storedDate = this.storedDate,
    pickUpDate = this.pickUpDate,
    highlight = this.operations.highlight,
    senderEmail = this.sender?.email,
    shipmentType = this.shipmentType,
    eventLog = this.eventLog.map { EventLogNetworkVO(name = it.name, date = it.date) }.toMutableList(),
    operation = OperationsNetworkVO(
        manualArchive = this.operations.manualArchive,
        delete = this.operations.delete,
        collect = this.operations.collect,
        highlight = this.operations.highlight,
        expandAvizo = this.operations.expandAvizo,
        endOfWeekCollection = this.operations.endOfWeekCollection
    )
)
