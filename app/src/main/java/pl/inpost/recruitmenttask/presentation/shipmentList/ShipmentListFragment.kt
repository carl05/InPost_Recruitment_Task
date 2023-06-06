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
            val items = mutableListOf<ShipmentAdapterItem>()
            val flaggedItems = mutableListOf<ShipmentAdapterItem>()

            items.add(CategoryItem("Gotowe do odbioru"))
            flaggedItems.add(CategoryItem("Pozostate"))
            shipments.forEach { shipmentNetwork ->
                if(shipmentNetwork.operations.highlight){
                    flaggedItems.add(ShipmentItem(shipmentNetwork))
                }else{
                    items.add(ShipmentItem(shipmentNetwork))
                }
//                val shipmentItemBinding = ShipmentItemBinding.inflate(layoutInflater).apply {
//                    shipmentNumber.text = shipmentNetwork.number
//                    status.text = shipmentNetwork.status
//                }
//                binding?.recycleViewShipments?.addView(shipmentItemBinding.root)
            }
            val adapter = ShipmentSectionedAdapter(items + flaggedItems)

            binding?.recycleViewShipments?.layoutManager = LinearLayoutManager(requireContext())
            binding?.recycleViewShipments?.adapter = adapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    companion object {
        fun newInstance() = ShipmentListFragment()
    }
}
