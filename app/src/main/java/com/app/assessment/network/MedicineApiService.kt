package com.app.assessment.network

import com.app.assessment.model.MedicalRecord
import retrofit2.http.GET

interface MedicineApiService {
    @GET("v3/fa1e0727-90f1-4398-9600-2d3ec1c15ff0")
    suspend fun getMedicines(): MedicalRecord
}
