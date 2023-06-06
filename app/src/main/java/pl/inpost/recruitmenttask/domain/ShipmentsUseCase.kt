package pl.inpost.recruitmenttask.domain

import pl.inpost.recruitmenttask.data.network.api.ShipmentApi
import pl.inpost.recruitmenttask.data.network.model.ShipmentNetwork
import javax.inject.Inject

class ShipmentsUseCase @Inject constructor(private val shipmentApi: ShipmentApi) {
    suspend fun getShipments(): List<ShipmentNetwork> {
        return shipmentApi.getShipments()
    }

}