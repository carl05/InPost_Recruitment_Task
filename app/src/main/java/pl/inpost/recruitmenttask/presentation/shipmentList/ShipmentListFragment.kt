package pl.inpost.recruitmenttask.presentation.shipmentList

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import pl.inpost.recruitmenttask.R
import pl.inpost.recruitmenttask.data.network.model.ShipmentNetwork
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
            val (items, flaggedItems) = fillAdapterList(shipments)
            val adapter = ShipmentSectionedAdapter(items + flaggedItems)
            binding?.recycleViewShipments?.layoutManager = LinearLayoutManager(requireContext())
            binding?.recycleViewShipments?.adapter = adapter
        }
        viewModel.refreshData()
    }

    private fun fillAdapterList(shipments: List<ShipmentNetwork>): Pair<MutableList<ShipmentAdapterItem>, MutableList<ShipmentAdapterItem>> {
        val items = mutableListOf<ShipmentAdapterItem>()
        val flaggedItems = mutableListOf<ShipmentAdapterItem>()

        items.add(CategoryItem(getString(R.string.ready_pick_up)))
        flaggedItems.add(CategoryItem(getString(R.string.other)))
        shipments.forEach { shipmentNetwork ->
            if (shipmentNetwork.operations.highlight) {
                flaggedItems.add(ShipmentItem(shipmentNetwork))
            } else {
                items.add(ShipmentItem(shipmentNetwork))
            }
        }
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
