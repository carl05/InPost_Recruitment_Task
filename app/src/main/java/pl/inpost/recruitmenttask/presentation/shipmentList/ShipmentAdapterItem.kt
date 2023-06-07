package pl.inpost.recruitmenttask.presentation.shipmentList


sealed class ShipmentAdapterItem

data class CategoryItem(val name: String) : ShipmentAdapterItem()

data class ShipmentItem(val shipmentVO: ShipmentVO) : ShipmentAdapterItem()
