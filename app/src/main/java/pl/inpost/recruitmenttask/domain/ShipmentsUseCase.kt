package pl.inpost.recruitmenttask.domain

import android.os.Build
import androidx.annotation.RequiresApi
import pl.inpost.recruitmenttask.data.local.ShipmentRepository
import pl.inpost.recruitmenttask.data.network.model.ShipmentNetwork
import pl.inpost.recruitmenttask.presentation.shipmentList.EventLogNetworkVO
import pl.inpost.recruitmenttask.presentation.shipmentList.OperationsNetworkVO
import pl.inpost.recruitmenttask.presentation.shipmentList.ShipmentVO
import javax.inject.Inject

class ShipmentsUseCase @Inject constructor(private val shipmentRepository: ShipmentRepository) {
    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun getShipments(): List<ShipmentVO> {
        return shipmentRepository.getShipments().map { it.toVO() }
    }

}

fun ShipmentNetwork.toVO() = ShipmentVO(
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
