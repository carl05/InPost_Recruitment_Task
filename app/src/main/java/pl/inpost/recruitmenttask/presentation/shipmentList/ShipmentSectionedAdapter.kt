package pl.inpost.recruitmenttask.presentation.shipmentList

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import pl.inpost.recruitmenttask.R
import pl.inpost.recruitmenttask.data.network.model.ShipmentStatus
import pl.inpost.recruitmenttask.data.network.model.ShipmentType
import pl.inpost.recruitmenttask.databinding.ItemCategoryBinding
import pl.inpost.recruitmenttask.databinding.ItemShipmentBinding

import pl.inpost.recruitmenttask.gone
import pl.inpost.recruitmenttask.visible
import java.time.format.DateTimeFormatter


class ShipmentSectionedAdapter(private val itemList: List<ShipmentAdapterItem>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_TYPE_CATEGORY = 0
        private const val VIEW_TYPE_ITEM = 1
    }
    val DATE_SCREEN_PATTERN = "E | dd.MM.yy | hh:mm a"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_CATEGORY -> {
                val view = ItemCategoryBinding.inflate(LayoutInflater.from(parent.context))
                CategoryViewHolder(view)
            }

            VIEW_TYPE_ITEM -> {
                val view = ItemShipmentBinding.inflate(LayoutInflater.from(parent.context))
                ItemViewHolder(view)
            }

            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    @RequiresApi(Build.VERSION_CODES.O) // thats a problem to be fixed Zoned date time pattern requires api
    // 26 or external library to parse
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = itemList[position]
        when (holder) {
            is CategoryViewHolder -> {
                holder.bind((item as CategoryItem).name)
            }

            is ItemViewHolder -> {
                holder.bind((item as ShipmentItem).shipmentVO)
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

    inner class CategoryViewHolder(val binding: ItemCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(categoryName: String) {
            binding.tvCategoryName.text = categoryName
        }
    }

    inner class ItemViewHolder(val binding: ItemShipmentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @RequiresApi(Build.VERSION_CODES.O) // thats a problem to be fixed Zoned date time pattern requires api
        // 26 or external library to parse
        fun bind(shipmentVO: ShipmentVO) {
            shipmentVO.run {
                binding.tvShipmentNumber.text = number
                binding.tvStatus.text = binding.root.context.getString(getResFromStatus(status))
                binding.tvShipmentNumber.text = number
                binding.tvUserId.text = senderEmail
                fillOperationStatus(this)
            }

        }


        @RequiresApi(Build.VERSION_CODES.O) // thats a problem to be fixed Zoned date time pattern requires api 26
        // or external library to parse
        private fun fillOperationStatus(shipmentVO: ShipmentVO) {
            shipmentVO.run {
                eventLog.sortByDescending { it.date }
                val firstLog = eventLog.firstOrNull()
                firstLog?.let { eventLogVO ->
                    fillStatusOperationLabel(eventLogVO)
                    fillStatusOperationDesciption(eventLogVO)
                } ?: showOperationStatus(false)
                fillTypeImage(shipmentType)
            }
        }

        private fun fillTypeImage(shipmentType: String) {
            if(shipmentType.lowercase() == ShipmentType.PARCEL_LOCKER.name.lowercase())
                binding.ivStatus.setImageDrawable(
                    AppCompatResources.getDrawable(binding.root.context, R.drawable.ic_figma_locker))
        }

        @RequiresApi(Build.VERSION_CODES.O) // thats a problem to be fixed Zoned date time pattern requires api 26 or external library to parse
        private fun fillStatusOperationDesciption(eventLogVO: EventLogNetworkVO) {
            val date = eventLogVO.date.toLocalDateTime()
            val formatter = DateTimeFormatter.ofPattern(DATE_SCREEN_PATTERN)
            val dateFormated = formatter.format(date)
            binding.tvDeliveryStatus.text = dateFormated
        }

        private fun fillStatusOperationLabel(eventLogVO: EventLogNetworkVO) {
            showOperationStatus(true)
            val status = getResFromStatus(eventLogVO.name)

            binding.tvLabelDeliveryStatus.text = binding.root.context.getString(status)
        }

        private fun getResFromStatus(name: String) =
            if (ShipmentStatus.values().map { it.name }
                    .contains(name)) ShipmentStatus.valueOf(name).nameRes
            else ShipmentStatus.OTHER.nameRes

        private fun showOperationStatus(show: Boolean) {
            if (show) {
                binding.tvDeliveryStatus.visible()
                binding.tvLabelDeliveryStatus.visible()
            } else {
                binding.tvDeliveryStatus.gone()
                binding.tvLabelDeliveryStatus.gone()
            }
        }
    }
}



