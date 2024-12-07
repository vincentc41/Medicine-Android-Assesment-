package com.app.assessment.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "medicine_table")
data class MedicineEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val dose: String,
    val strength: String,
    val manufacturer: String,
    val usage: String,
    val image: String
)
