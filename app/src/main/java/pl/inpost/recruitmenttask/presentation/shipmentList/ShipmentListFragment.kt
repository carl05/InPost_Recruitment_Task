package pl.inpost.recruitmenttask.presentation.shipmentList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import pl.inpost.recruitmenttask.R
import pl.inpost.recruitmenttask.databinding.FragmentShipmentListBinding


@AndroidEntryPoint
class ShipmentListFragment : Fragment() {

    private val viewModel: ShipmentListViewModel by viewModels()
    private var binding: FragmentShipmentListBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentShipmentListBinding.inflate(inflater, container, false)
        return requireNotNull(binding).root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.mutableViewState.observe(requireActivity()) { shipments ->
            val (items, flaggedItems) = fillAdapterList(shipments)
            val adapter = ShipmentSectionedAdapter(items + flaggedItems)
            binding?.recycleViewShipments?.adapter = adapter
            binding?.swipeRefresh?.isRefreshing = false
        }
        binding?.swipeRefresh?.setOnRefreshListener {
            viewModel.refreshData()
        }
        viewModel.refreshData()
    }

    private fun fillAdapterList(shipments: MutableList<ShipmentVO>): Pair<MutableList<ShipmentAdapterItem>, MutableList<ShipmentAdapterItem>> {
        val items = mutableListOf<ShipmentAdapterItem>()
        val flaggedItems = mutableListOf<ShipmentAdapterItem>()

        shipments.forEach { shipmentNetworkVO ->
            if (shipmentNetworkVO.highlight) {
                flaggedItems.add(ShipmentItem(shipmentNetworkVO))
            } else {
                items.add(ShipmentItem(shipmentNetworkVO))
            }
        }
        //add the categories in begining only
        //if have at least one item
        takeIf { items.size > 0 }?.run {  items.add(0,CategoryItem(getString(R.string.status_ready_to_pickup))) }
        takeIf { flaggedItems.size > 0 }?.run { flaggedItems.add(0,CategoryItem(getString(R.string.status_other))) }

        return Pair(items, flaggedItems)
    }



    companion object {
        fun newInstance() = ShipmentListFragment()
    }
}
