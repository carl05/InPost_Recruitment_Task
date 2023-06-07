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
import org.mockito.kotlin.any
import org.mockito.kotlin.atLeastOnce
import org.mockito.kotlin.given
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import pl.inpost.recruitmenttask.presentation.shipmentList.OperationsNetworkVO
import pl.inpost.recruitmenttask.presentation.shipmentList.ShipmentListViewModel
import pl.inpost.recruitmenttask.presentation.shipmentList.ShipmentVO
import java.time.ZonedDateTime

@RunWith(MockitoJUnitRunner::class)
class ShipmentListViewModelTest {

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
    private lateinit var shipmentListViewModel: ShipmentListViewModel

    @Test
    fun `test refresh data return not empty list`() {
        //Arrange
        runBlocking {
            val list = mutableListOf<ShipmentVO>()
            list.add(mock())
            given(shipmentListViewModel.mutableViewState).willReturn(mock())
            given(shipmentListViewModel.mutableViewState.value).willReturn(list)
        }
        //Act
        runBlocking {
            launch(Dispatchers.Main) {
                shipmentListViewModel.refreshData()
            }
        }
        //Assert
        assertEquals(shipmentListViewModel.mutableViewState.value?.isNotEmpty(), true)
    }

    @Test
    fun `test refresh data return not list`() {
        //Arrange
        runBlocking {
            given(shipmentListViewModel.mutableViewState).willReturn(mock())
            given(shipmentListViewModel.mutableViewState.value).willReturn(mutableListOf())
        }
        //Act
        runBlocking {
            launch(Dispatchers.Main) {
                shipmentListViewModel.refreshData()
            }
        }
        //Assert
        assertEquals(shipmentListViewModel.mutableViewState.value?.isEmpty(), true)
    }

    /**
     * here we do not focus on test if sort method is working
     * this is already done by kotlin api
     * our focus is to have sure that whenever we load data
     * the sort method is called from viewmodel
     */
    @Test
    fun `test sort items`() {
        //Arrange
        val items = getIems()
        runBlocking {
            given(shipmentListViewModel.shipmentUseCase).willReturn(mock())
            given(shipmentListViewModel.shipmentUseCase.getShipments()).willReturn(items)
            given(shipmentListViewModel.mutableViewState).willReturn(mock())
            given(shipmentListViewModel.refreshData()).willCallRealMethod()
        }

        //Act
        runBlocking {
            shipmentListViewModel.refreshData()

        }

        //Assert
        verify(shipmentListViewModel, atLeastOnce()).sortItems(any())


    }

    private fun getIems(): MutableList<ShipmentVO> {
        val items = mutableListOf<ShipmentVO>()
        items.add(randomItem())
        items.add(randomItem())
        items.add(randomItem())
        items.add(randomItem())
        items.add(randomItem())
        return items
    }

    private fun randomItem(): ShipmentVO {
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


}