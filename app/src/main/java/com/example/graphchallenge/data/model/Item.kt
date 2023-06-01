package com.example.graphchallenge.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Item(
    @PrimaryKey
    val id: String,
    val total: Float,
    val date: String,
    val rowIndex: Int
)
