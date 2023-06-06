package pl.inpost.recruitmenttask.presentation.shipmentList

import androidx.lifecycle.LiveData
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
class ShipmentListViewModel @Inject constructor(
    private val shipmentUseCase: ShipmentsUseCase
) : ViewModel() {

    private val mutableViewState = MutableLiveData<List<ShipmentNetworkVO>>(emptyList())
    val viewState: LiveData<List<ShipmentNetworkVO>> = mutableViewState

    @OptIn(DelicateCoroutinesApi::class)
    fun refreshData() {
        GlobalScope.launch(Dispatchers.Main) {
            val shipments = shipmentUseCase.getShipments()
            mutableViewState.setState { shipments }
        }
    }

    fun sortItems(items: MutableList<ShipmentNetworkVO>) {
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
