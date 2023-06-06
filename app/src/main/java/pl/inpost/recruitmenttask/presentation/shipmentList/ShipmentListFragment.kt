package pl.inpost.recruitmenttask.presentation.shipmentList

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import pl.inpost.recruitmenttask.R
import pl.inpost.recruitmenttask.databinding.FragmentShipmentListBinding

@AndroidEntryPoint
class ShipmentListFragment : Fragment() {

    private val viewModel: ShipmentListViewModel by viewModels()
    private var binding: FragmentShipmentListBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.shipment_list_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
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
        viewModel.viewState.observe(requireActivity()) { shipments ->
            val data = shipments.toMutableList()
            val (items, flaggedItems) = fillAdapterList(data)
            val adapter = ShipmentSectionedAdapter(items + flaggedItems)
            val linearLayoutManager = LinearLayoutManager(requireContext())
            linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
            binding?.recycleViewShipments?.layoutManager = linearLayoutManager
            binding?.recycleViewShipments?.adapter = adapter
//            binding?.swipeRefresh?.isRefreshing = false
        }
//        binding?.swipeRefresh?.setOnRefreshListener {
//            viewModel.refreshData()
//        }
        viewModel.refreshData()
        viewModel.refreshData()
    }

    private fun fillAdapterList(shipments: MutableList<ShipmentNetworkVO>): Pair<MutableList<ShipmentAdapterItem>, MutableList<ShipmentAdapterItem>> {
        val items = mutableListOf<ShipmentAdapterItem>()
        val flaggedItems = mutableListOf<ShipmentAdapterItem>()

        viewModel.sortItems(shipments)

        shipments.forEach { shipmentNetworkVO ->
            if (shipmentNetworkVO.highlight) {
                flaggedItems.add(ShipmentItem(shipmentNetworkVO))
            } else {
                items.add(ShipmentItem(shipmentNetworkVO))
            }
        }
        //add the categories in begining only
        // if have at least one item
//        takeIf { items.size > 0 }?.run {  items.add(CategoryItem(getString(R.string.status_ready_to_pickup))) }
//        takeIf { flaggedItems.size > 0 }?.run { flaggedItems.add(CategoryItem(getString(R.string.status_other))) }

        return Pair(items, flaggedItems)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    companion object {
        fun newInstance() = ShipmentListFragment()
    }
}
