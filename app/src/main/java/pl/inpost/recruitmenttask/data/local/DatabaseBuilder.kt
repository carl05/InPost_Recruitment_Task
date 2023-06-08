package pl.inpost.recruitmenttask.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import java.sql.SQLException

@Database(entities = [ShipmentEntity::class], version = 1, exportSchema = false)
abstract class ShipmentDatabase : RoomDatabase() {
    abstract fun shipmentDao(): ShipmentDao

    companion object {

        @Volatile
        private var INSTANCE: ShipmentDatabase? = null

        fun getShipmentDao(context: Context): ShipmentDao {
            checkInstance(context)
            return INSTANCE?.shipmentDao() ?: throw SQLException("Erro ao criar banco de dados")
        }

        private fun checkInstance(context: Context) {
            if (INSTANCE == null) {
                synchronized(this) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        ShipmentDatabase::class.java,
                        "shipments_database"
                    ).build()
                }
            }
        }
    }
}
