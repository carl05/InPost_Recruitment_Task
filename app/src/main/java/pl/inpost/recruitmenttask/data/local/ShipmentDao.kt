package pl.inpost.recruitmenttask.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface ShipmentDao {

    @Query("SELECT * FROM shipment")
    fun getAllShipments(): List<ShipmentEntity>

    @Update
    fun updateShipment(shipment: ShipmentEntity)

    @Insert
    fun insertShipment(shipment: ShipmentEntity)

    @Insert
    fun saveAll(shipments: List<ShipmentEntity>)

    @Query("Delete from shipment")
    fun deleteAll()
}