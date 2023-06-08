package pl.inpost.recruitmenttask.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "shipment")
data class ShipmentEntity(
    @PrimaryKey
    @ColumnInfo(name = "number") val number: String,
    @ColumnInfo(name = "shipmentNetwork") val shipmentNetwork: String,

)
