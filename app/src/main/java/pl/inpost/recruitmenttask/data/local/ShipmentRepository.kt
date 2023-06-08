package pl.inpost.recruitmenttask.data.local

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.squareup.moshi.FromJson
import com.squareup.moshi.Moshi
import com.squareup.moshi.ToJson
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import pl.inpost.recruitmenttask.data.network.api.ShipmentApi
import pl.inpost.recruitmenttask.data.network.model.ShipmentNetwork
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class ShipmentRepository @Inject constructor(
    private val shipmentApi: ShipmentApi,
    @ApplicationContext appContext: Context
) {
    private val shipmentDao = ShipmentDatabase.getShipmentDao(appContext)

    private val moshi = Moshi.Builder()
        .add(ZonedDateTimeJsonAdapter)
        .add(KotlinJsonAdapterFactory())
        .build()

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun getShipments(): List<ShipmentNetwork> {
        return withContext(Dispatchers.IO) {
            val localShipments = shipmentDao.getAllShipments()
            return@withContext if (localShipments.isNotEmpty())
                convertToNetwork(localShipments)
            else {
                val resp = shipmentApi.getShipments()
                shipmentDao.deleteAll()
                shipmentDao.saveAll(convertToEntity(resp))
                resp
            }
        }
    }

    private fun convertToNetwork(localShipments: List<ShipmentEntity>): List<ShipmentNetwork> {
        val jsonAdapter = moshi.adapter(ShipmentNetwork::class.java)

        return localShipments.map {
            jsonAdapter.fromJson(it.shipmentNetwork) as ShipmentNetwork
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun convertToEntity(localShipments: List<ShipmentNetwork>): List<ShipmentEntity> {
        val jsonAdapter = moshi.adapter(ShipmentNetwork::class.java)

        val list = mutableListOf<ShipmentEntity>()
        localShipments.forEach {
            val entity = ShipmentEntity(
                number = it.number,
                shipmentNetwork = jsonAdapter.toJson(it)
            )
            list.add(entity)
        }
        return list
    }

    // ZonedDateTimeJsonAdapter
    object ZonedDateTimeJsonAdapter {
        @RequiresApi(Build.VERSION_CODES.O)
        @ToJson
        fun toJson(zonedDateTime: ZonedDateTime): String {
            return zonedDateTime.format(DateTimeFormatter.ISO_ZONED_DATE_TIME)
        }

        @RequiresApi(Build.VERSION_CODES.O)
        @FromJson
        fun fromJson(zonedDateTimeString: String): ZonedDateTime {
            return ZonedDateTime.parse(zonedDateTimeString, DateTimeFormatter.ISO_ZONED_DATE_TIME)
        }
    }
}
