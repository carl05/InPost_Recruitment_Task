package pl.inpost.recruitmenttask.domain

import pl.inpost.recruitmenttask.data.network.api.ShipmentApi
import pl.inpost.recruitmenttask.data.network.model.ShipmentNetwork
import pl.inpost.recruitmenttask.presentation.shipmentList.ShipmentNetworkVO
import javax.inject.Inject

class ShipmentsUseCase @Inject constructor(private val shipmentApi: ShipmentApi) {
    suspend fun getShipments(): List<ShipmentNetworkVO> {
        return shipmentApi.getShipments().map { it.toVO() }
    }

}

private fun ShipmentNetwork.toVO() = ShipmentNetworkVO(
    number = this.number,
    nameSender = this.sender?.name,
    status = this.status,
    expiryDate = this.expiryDate,
    storedDate = this.storedDate,
    pickUpDate = this.pickUpDate,
    highlight = this.operations.highlight
)
