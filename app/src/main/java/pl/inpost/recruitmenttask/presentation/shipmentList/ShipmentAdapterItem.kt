package pl.inpost.recruitmenttask.presentation.shipmentList

import pl.inpost.recruitmenttask.data.network.model.ShipmentNetwork

sealed class ShipmentAdapterItem

data class CategoryItem(val name: String) : ShipmentAdapterItem()

data class ShipmentItem(val shipmentNetwork: ShipmentNetwork) : ShipmentAdapterItem()
