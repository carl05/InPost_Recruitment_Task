package pl.inpost.recruitmenttask

import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import net.bytebuddy.utility.RandomString
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.given
import pl.inpost.recruitmenttask.data.network.model.CustomerNetwork
import pl.inpost.recruitmenttask.data.network.model.EventLogNetwork
import pl.inpost.recruitmenttask.data.network.model.OperationsNetwork
import pl.inpost.recruitmenttask.data.network.model.ShipmentNetwork
import pl.inpost.recruitmenttask.domain.ShipmentsUseCase
import pl.inpost.recruitmenttask.domain.toVO
import pl.inpost.recruitmenttask.presentation.shipmentList.OperationsNetworkVO
import pl.inpost.recruitmenttask.presentation.shipmentList.ShipmentVO
import java.time.ZonedDateTime

@RunWith(MockitoJUnitRunner::class)
class ShipmentListUseCaseTest {

    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    @Before
    fun setUp() {
        Dispatchers.setMain(mainThreadSurrogate)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain() // reset the main dispatcher to the original Main dispatcher
        mainThreadSurrogate.close()
    }

    @Mock
    private lateinit var shipmentsUseCase: ShipmentsUseCase

    @Test
    fun `test getShipments is not empty`() {
        //Arrange
        runBlocking {
            given(shipmentsUseCase.getShipments()).willReturn(getVOItems())
        }
        runBlocking {
            launch(Dispatchers.Main) {
                //Act
                val voList = shipmentsUseCase.getShipments()

                //Assert
                assertEquals(voList.isNotEmpty(), true)
                assertEquals(voList.size, 5)
            }
        }
    }

    @Test
    fun `test getShipments is empty`() {
        //Arrange
        runBlocking {
            given(shipmentsUseCase.getShipments()).willReturn(emptyList())
        }
        runBlocking {
            launch(Dispatchers.Main) {
                //Act
                val voList = shipmentsUseCase.getShipments()

                //Assert
                assertEquals(voList.isEmpty(), true)
                assertEquals(voList.size, 0)
            }
        }
    }

    @Test
    fun `test tovo method`() {
        //Arrange
        runBlocking {
            given(shipmentsUseCase.getShipments()).willReturn(emptyList())
        }
        runBlocking {
            launch(Dispatchers.Main) {
                //Act
                val shipmentNetwork = randomNetworkItem()
                val voitem = shipmentNetwork.toVO()

                //Assert
                assertEquals(shipmentNetwork.status, voitem.status)
                assertEquals(shipmentNetwork.operations.highlight, voitem.operation.highlight)
                assertEquals(shipmentNetwork.eventLog.firstOrNull()?.name, voitem.eventLog.firstOrNull()?.name)
                assertEquals(shipmentNetwork.expiryDate, voitem.expiryDate)
                assertEquals(shipmentNetwork.pickUpDate, voitem.pickUpDate)
                assertEquals(shipmentNetwork.storedDate, voitem.storedDate)
            }
        }
    }

    private fun getVOItems(): MutableList<ShipmentVO> {
        val items = mutableListOf<ShipmentVO>()
        items.add(randomVOItem())
        items.add(randomVOItem())
        items.add(randomVOItem())
        items.add(randomVOItem())
        items.add(randomVOItem())
        return items
    }

    private fun randomVOItem(): ShipmentVO {
        return ShipmentVO(
            number = "123456",
            senderName = RandomString.make(5),
            senderEmail = RandomString.make(5),
            status = RandomString.make(5),
            expiryDate = ZonedDateTime.now(),
            storedDate = ZonedDateTime.now(),
            pickUpDate = ZonedDateTime.now(),
            highlight = true,
            operation = OperationsNetworkVO(
                true, true, true, true, true, true
            ),
            eventLog = mutableListOf(),
            shipmentType = RandomString.make(5),
        )
    }


    private fun randomNetworkItem(): ShipmentNetwork {
        return ShipmentNetwork(
            number = "123456",
            eventLog = emptyList<EventLogNetwork>(),
            sender = CustomerNetwork(email = RandomString.make(5), phoneNumber = RandomString.make(5), name = RandomString.make(5)) ,
            receiver = CustomerNetwork(email = RandomString.make(5), phoneNumber = RandomString.make(5), name = RandomString.make(5)) ,
            status = RandomString.make(5),
            expiryDate = ZonedDateTime.now(),
            storedDate = ZonedDateTime.now(),
            pickUpDate = ZonedDateTime.now(),
            operations = OperationsNetwork(
                true, true, true, true, true, true
            ),
            shipmentType = RandomString.make(5),
            openCode = RandomString.make(5)
        )
    }


}