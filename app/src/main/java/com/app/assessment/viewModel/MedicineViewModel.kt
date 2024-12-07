package com.app.assessment.viewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.assessment.room.MedicineDao
import com.app.assessment.room.MedicineEntity
import com.app.assessment.model.MedicalRecord
import com.app.assessment.network.ApiResult
import com.app.assessment.repository.MedicineRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MedicineViewModel @Inject constructor(
    private val repository: MedicineRepository,
    private val medicineDao: MedicineDao
) : ViewModel() {

    private val _medications = MutableLiveData<List<MedicalRecord.Drug>>()
    val medications: LiveData<List<MedicalRecord.Drug>> get() = _medications
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    private val _medicineById = MutableLiveData<MedicineEntity?>()
    val medicineById: LiveData<MedicineEntity?> get() = _medicineById

    fun fetchMedicines() {
        _isLoading.value = true
        viewModelScope.launch {
            when (val result = repository.getMedicine()) {
                is ApiResult.Success -> {
                    fun extractDrugs(classDetails: List<MedicalRecord.ClassDetail>?): List<MedicalRecord.Drug> {
                        return classDetails.orEmpty().flatMap {
                            it.associatedDrug.orEmpty() + it.associatedDrug2.orEmpty()
                        }
                    }
                    val drugs = result.data.problems
                        .flatMap { it.diabetes.orEmpty() }
                        .flatMap { it.medications.orEmpty() }
                        .flatMap { it.medicationsClasses.orEmpty() }
                        .flatMap { medicationClass ->
                            extractDrugs(medicationClass.className) + extractDrugs(medicationClass.className2)
                        }

                    val medicineEntities = drugs.map { drug ->
                        MedicineEntity(
                            id = drug.id?:0,
                            name = drug.name.orEmpty(),
                            dose = drug.dose.orEmpty(),
                            strength = drug.strength.orEmpty(),
                            manufacturer = drug.manufacturer.orEmpty(),
                            usage = drug.usage.orEmpty(),
                            image = drug.image.orEmpty()
                        )
                    }

                    medicineDao.insertAll(medicineEntities)
                    _isLoading.value = false
                    _medications.postValue(drugs)
                }
                is ApiResult.Failure -> {
                    _isLoading.value = false
                    _error.value=result.message
                }
            }
        }
    }
    fun fetchMedicineById(id: Int) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val medicine = medicineDao.getMedicineById(id)
                _medicineById.postValue(medicine)
            } catch (e: Exception) {
                _error.postValue("Error fetching medicine by ID: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }
}

