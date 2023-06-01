package com.example.graphchallenge.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.graphchallenge.data.model.Item

@Dao
interface ItemDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(item: Item)

    @Query("Select * from item")
    fun getAll(): LiveData<List<Item>>

    @Query("SELECT * FROM item ORDER BY rowIndex DESC LIMIT 5")
    fun getLastFive(): LiveData<List<Item>>

    @Query("SELECT * FROM item ORDER BY total DESC LIMIT 5")
    fun getFiveTop(): LiveData<List<Item>>

    @Query("SELECT * FROM item ORDER BY total ASC LIMIT 5")
    fun getFiveDown(): LiveData<List<Item>>

    @Query("SELECT SUM(total) FROM item")
    fun getTotal(): LiveData<Float>

    @Query("SELECT AVG(total) FROM item")
    fun getAverage(): LiveData<Float>

    @Query("SELECT total FROM item ORDER BY total DESC LIMIT 1")
    fun getMax(): LiveData<Float>

    @Query("SELECT total FROM item ORDER BY total DESC LIMIT 1")
    fun getMin(): LiveData<Float>
}