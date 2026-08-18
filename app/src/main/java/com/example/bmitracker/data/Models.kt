package com.example.bmitracker.data

data class UserProfile(
    val id: String = "",
    val name: String ="",
    val dateofBirth: String ="",
    val gender: String ="",
    val weight: Double=0.0,
    val weightUnit: String ="KG"
    val height: Double=0.0,
    val heightUnit: String ="CM",
    val bmi: Double=0.0,
    val updatedAt: Long = 0L
)

data class WeightEntry(
    val id: String= "",
    val weightkg: Double =0.0,
    val date: Long = 0L
)