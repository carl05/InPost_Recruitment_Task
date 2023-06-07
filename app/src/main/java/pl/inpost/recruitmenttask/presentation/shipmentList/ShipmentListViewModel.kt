package pl.inpost.recruitmenttask.presentation.shipmentList

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import pl.inpost.recruitmenttask.data.network.model.ShipmentStatus
import pl.inpost.recruitmenttask.domain.ShipmentsUseCase
import pl.inpost.recruitmenttask.util.setState
import javax.inject.Inject

@HiltViewModel
open class ShipmentListViewModel @Inject constructor(
    val shipmentUseCase: ShipmentsUseCase
) : ViewModel() {

    val mutableViewState = MutableLiveData<MutableList<ShipmentVO>>(mutableListOf())

    @OptIn(DelicateCoroutinesApi::class)
    fun refreshData() {
        GlobalScope.launch(Dispatchers.Main) {
            val shipments = shipmentUseCase.getShipments().toMutableList()
            sortItems(shipments)
            mutableViewState.setState { shipments }
        }
    }

    fun sortItems(items: MutableList<ShipmentVO>) {
        items.sortWith(compareBy(
            { shipment ->
                if (ShipmentStatus.values().map { it.name }
                        .contains(shipment.status)) ShipmentStatus.valueOf(shipment.status)
                else ShipmentStatus.OTHER
            },
            { it.pickUpDate },
            { it.expiryDate },
            { it.storedDate },
            { it.number }
        ))
    }
}
