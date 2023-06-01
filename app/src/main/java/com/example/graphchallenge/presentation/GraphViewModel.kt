package com.example.graphchallenge.presentation

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.example.graphchallenge.GraphApplication
import com.example.graphchallenge.R
import com.example.graphchallenge.data.ItemDao
import com.example.graphchallenge.data.RemoteDataSource
import com.example.graphchallenge.data.model.Item
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GraphViewModel(
    private val remoteDataSource: RemoteDataSource,
    private val itemDao: ItemDao,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {

    init {
        fetchItems()
    }

    val items: LiveData<List<Item>> = itemDao.getLastFive()
    val topFiveItems: LiveData<List<Item>> = itemDao.getFiveTop()
    val downFiveItems: LiveData<List<Item>> = itemDao.getFiveDown()

    val total: LiveData<Float> = itemDao.getTotal()
    val max: LiveData<Float> = itemDao.getMax()
    val min: LiveData<Float> = itemDao.getMin()
    val avg: LiveData<Float> = itemDao.getAverage()

    fun fetchItems() {
        viewModelScope.launch(dispatcher) {
            remoteDataSource.fetch()
                .map {
                    itemDao.insert(it)
                }
        }
    }

    companion object {

        fun create(application: Application): GraphViewModel {
            val credentials =
                GoogleCredential
                    .fromStream(application.resources.openRawResource(R.raw.graphchallenge))

            val remoteDataSource = RemoteDataSource(credentials)
            val itemDao = (application as GraphApplication).getAppDataBase().itemDao()
            return GraphViewModel(remoteDataSource, itemDao)
        }
    }
}