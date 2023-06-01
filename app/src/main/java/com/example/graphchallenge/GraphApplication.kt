package com.example.graphchallenge

import android.app.Application
import androidx.room.Room
import com.example.graphchallenge.data.AppDataBase

class GraphApplication: Application() {

    private lateinit var dataBase: AppDataBase

    override fun onCreate() {
        super.onCreate()

        dataBase = Room.databaseBuilder(
            applicationContext,
            AppDataBase::class.java, "graph-database"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    fun getAppDataBase(): AppDataBase {
        return dataBase
    }
}