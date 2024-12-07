package com.app.assessment

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.app.assessment.model.MedicalRecord
import com.app.assessment.network.ApiResult
import com.app.assessment.repository.MedicineRepository
import com.app.assessment.room.MedicineDao
import com.app.assessment.room.MedicineEntity
import com.app.assessment.viewModel.MedicineViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.*
import org.mockito.Mockito.*

@ExperimentalCoroutinesApi
class MedicineViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val repository = mock(MedicineRepository::class.java)
    private val dao = mock(MedicineDao::class.java)
    private lateinit var viewModel: MedicineViewModel

    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = MedicineViewModel(repository, dao)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `fetchMedicines inserts data into Room`() = runTest {
        val drugs = listOf(
            MedicalRecord.Drug(
                id = 1, name = "Paracetamol", dose = "500mg", strength = "500mg",
                manufacturer = "MediCare", usage = "Pain relief", image = "url"
            )
        )
        val apiResult = ApiResult.Success(
            MedicalRecord(
                problems = listOf(
                    MedicalRecord.ProblemGroup(
                        diabetes = listOf(
                            MedicalRecord.DiabetesProblem(
                                medications = listOf(
                                    MedicalRecord.Medication(
                                        medicationsClasses = listOf(
                                            MedicalRecord.MedicationClass(
                                                className = listOf(
                                                    MedicalRecord.ClassDetail(associatedDrug = drugs)
                                                )
                                            )
                                        )
                                    )
                                )
                            )
                        )
                    )
                )
            )
        )
        `when`(repository.getMedicine()).thenReturn(apiResult)

        viewModel.fetchMedicines()

        verify(dao).insertAll(anyList())
        Assert.assertFalse(viewModel.isLoading.value ?: true)
    }

    @Test
    fun `fetchMedicineById retrieves correct medicine`() = runTest {
        val medicine = MedicineEntity(
            id = 1, name = "Paracetamol", dose = "500mg", strength = "500mg",
            manufacturer = "MediCare", usage = "Pain relief", image = "url"
        )
        `when`(dao.getMedicineById(1)).thenReturn(medicine)

        val observer = mock(Observer::class.java) as Observer<MedicineEntity?>
        viewModel.medicineById.observeForever(observer)

        viewModel.fetchMedicineById(1)

        verify(observer).onChanged(medicine)
        Assert.assertEquals(medicine, viewModel.medicineById.value)
    }

    @Test
    fun `fetchMedicines handles API error`() = runTest {
        val errorMessage = "Network error"
        val apiResult = ApiResult.Failure(errorMessage)
        `when`(repository.getMedicine()).thenReturn(apiResult)

        viewModel.fetchMedicines()

        Assert.assertEquals(errorMessage, viewModel.error.value)
        Assert.assertFalse(viewModel.isLoading.value ?: true)
    }
}
