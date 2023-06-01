package com.example.graphchallenge

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.graphchallenge.data.ItemDao
import com.example.graphchallenge.data.RemoteDataSource
import com.example.graphchallenge.data.model.Item
import com.example.graphchallenge.presentation.GraphViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

@OptIn(ExperimentalCoroutinesApi::class)
class GraphViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    //https://medium.com/androiddevelopers/unit-testing-livedata-and-other-common-observability-problems-bb477262eb04
    private val remote = mock<RemoteDataSource>()
    private val dao = mock<ItemDao>()

    private lateinit var underTest: GraphViewModel

    @Test
    fun `test last five items`() {
        // GIVEN
        val expected =
            listOf(
                Item(
                    id = "id1",
                    total = 100f,
                    date = "01/01/2001",
                    rowIndex = 1
                ),
                Item(
                    id = "id2",
                    total = 200f,
                    date = "02/01/2001",
                    rowIndex = 2
                )
            )

        whenever(dao.getLastFive()).thenReturn(MutableLiveData(expected))

        // WHEN
        underTest = GraphViewModel(
            remote,
            dao,
            UnconfinedTestDispatcher()
        )

        // THEN
        val result = underTest.items.getOrAwaitValue()
        assert(expected == result)
    }

}


fun <T> LiveData<T>.getOrAwaitValue(
    time: Long = 2,
    timeUnit: TimeUnit = TimeUnit.SECONDS
): T {
    var data: T? = null
    val latch = CountDownLatch(1)
    val observer = object : Observer<T> {
        override fun onChanged(o: T?) {
            data = o
            latch.countDown()
            this@getOrAwaitValue.removeObserver(this)
        }
    }

    this.observeForever(observer)

    // Don't wait indefinitely if the LiveData is not set.
    if (!latch.await(time, timeUnit)) {
        throw TimeoutException("LiveData value was never set.")
    }

    @Suppress("UNCHECKED_CAST")
    return data as T
}