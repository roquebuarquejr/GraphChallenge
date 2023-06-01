package com.example.graphchallenge.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.graphchallenge.data.model.Item

@Database(entities = [Item::class], version = 4)
abstract class AppDataBase : RoomDatabase() {

    abstract fun itemDao(): ItemDao
}
