package com.app.assessment

import androidx.room.Database
import androidx.room.RoomDatabase
import com.app.assessment.room.MedicineDao
import com.app.assessment.room.MedicineEntity

@Database(entities = [MedicineEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun medicineDao(): MedicineDao
}
