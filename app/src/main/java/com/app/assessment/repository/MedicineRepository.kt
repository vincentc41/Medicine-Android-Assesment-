package com.app.assessment.repository

import com.app.assessment.model.MedicalRecord
import com.app.assessment.network.ApiResult
import com.app.assessment.network.MedicineApiService
import javax.inject.Inject


class MedicineRepository @Inject constructor(
    private val apiService: MedicineApiService
) {
    suspend fun getMedicine(): ApiResult<MedicalRecord> {
        return try {
            val response = apiService.getMedicines()
            ApiResult.Success(response)
        } catch (e: Exception) {
            ApiResult.Failure("Failed to get Medicines: ${e.message}")
        }
    }
}