package com.app.assessment.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MedicineDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(medicines: List<MedicineEntity>)

    @Query("SELECT * FROM medicine_table WHERE id = :id")
    suspend fun getMedicineById(id: Int): MedicineEntity?
}
