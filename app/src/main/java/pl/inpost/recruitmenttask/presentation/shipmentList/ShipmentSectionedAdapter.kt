package pl.inpost.recruitmenttask.presentation.shipmentList

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import pl.inpost.recruitmenttask.databinding.ItemCategoryBinding
import pl.inpost.recruitmenttask.databinding.ShipmentItemBinding

class ShipmentSectionedAdapter(private val itemList: List<ShipmentAdapterItem>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        companion object {
            private const val VIEW_TYPE_CATEGORY = 0
            private const val VIEW_TYPE_ITEM = 1
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return when (viewType) {
                VIEW_TYPE_CATEGORY -> {
                    val view = ItemCategoryBinding.inflate(LayoutInflater.from(parent.context))
                    CategoryViewHolder(view)
                }
                VIEW_TYPE_ITEM -> {
                    val view = ShipmentItemBinding.inflate(LayoutInflater.from(parent.context))
                    ItemViewHolder(view)
                }
                else -> throw IllegalArgumentException("Invalid view type")
            }
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val item = itemList[position]
            when (holder) {
                is CategoryViewHolder -> {
                    holder.bind(item as CategoryItem)
                }
                is ItemViewHolder -> {
                    holder.bind(item as ShipmentItem)
                }
            }
        }

        override fun getItemCount(): Int {
            return itemList.size
        }

        override fun getItemViewType(position: Int): Int {
            return when (itemList[position]) {
                is CategoryItem -> VIEW_TYPE_CATEGORY
                is ShipmentItem -> VIEW_TYPE_ITEM
            }
        }

        // ViewHolders

        inner class CategoryViewHolder(val binding: ItemCategoryBinding) : RecyclerView.ViewHolder(binding.root) {
            fun bind(categoryItem: CategoryItem) {
                binding.tvCategoryName.text = categoryItem.name
            }
        }

        inner class ItemViewHolder(val binding: ShipmentItemBinding) : RecyclerView.ViewHolder(binding.root) {
            fun bind(shipmentItem: ShipmentItem) {
                binding.shipmentNumber.text = shipmentItem.shipmentNetwork.number
                binding.status.text = shipmentItem.shipmentNetwork.status
            }
        }
    }

