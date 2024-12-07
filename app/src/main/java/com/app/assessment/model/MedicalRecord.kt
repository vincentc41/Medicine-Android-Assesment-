package com.app.assessment.model

import com.google.gson.annotations.SerializedName


data class MedicalRecord(
    @SerializedName("problems")
    val problems: List<ProblemGroup>
) {
    data class ProblemGroup(
        @SerializedName("Diabetes")
        val diabetes: List<DiabetesProblem>? = null,
        @SerializedName("Asthma")
        val asthma: List<DiabetesProblem>? = null
    )

    data class DiabetesProblem(
        @SerializedName("medications")
        val medications: List<Medication>? = null,
        @SerializedName("labs")
        val labs: List<LabData>? = null
    )

    data class Medication(
        @SerializedName("medicationsClasses")
        val medicationsClasses: List<MedicationClass>? = null
    )

    data class MedicationClass(
        @SerializedName("className")
        val className: List<ClassDetail>? = null,
        @SerializedName("className2")
        val className2: List<ClassDetail>? = null
    )

    data class ClassDetail(
        @SerializedName("associatedDrug")
        val associatedDrug: List<Drug>? = null,
        @SerializedName("associatedDrug#2")
        val associatedDrug2: List<Drug>? = null
    )

    data class Drug(
        @SerializedName("id")
        val id: Int? = null,
        @SerializedName("name")
        val name: String? = null,
        @SerializedName("dose")
        val dose: String? = null,
        @SerializedName("strength")
        val strength: String? = null,
        @SerializedName("manufacturer")
        val manufacturer: String? = null,
        @SerializedName("usage")
        val usage: String? = null,
        @SerializedName("image")
        val image: String? = null
    )

    data class LabData(
        @SerializedName("missing_field")
        val missingField: String? = null
    )
}

