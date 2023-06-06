package pl.inpost.recruitmenttask.data.network.api

import pl.inpost.recruitmenttask.data.network.model.ShipmentNetwork

interface ShipmentApi {
    suspend fun getShipments(): List<ShipmentNetwork>
}
