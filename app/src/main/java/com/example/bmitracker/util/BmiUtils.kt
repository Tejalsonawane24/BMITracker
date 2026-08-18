package com.example.bmitracker.util

object BmiUtils{
    fun weightToKg(weight: Double, unit: String): Double{
        return if(unit == "LB"){
            weight * 0.45359237
        }else{
            weight
        }
    }
    fun heightToMeters(height: Double, unit: String): Double{
        return if(unit == "IN"){
            height * 0.0254
        }else{
            height/100.0
        }
    }
    fun calculateBmi(
        weight: Double,
        weightUnit: String,
        height: Double,
        heightUnit: String
    ): Double{
        val kg= weightToKg(weight, weightUnit)
        val meters = heightToMeters(height, heightUnit)
        if(meters <= 0) return 0.0
        return kg/(meters*meters)
    }
    fun category(bmi : Double): String{
        return when{
            bmi < 18.5 -> "Underweight"
            bmi < 25.0 -> "Normal Weight"
            bmi < 30.0 -> "Overweight"
            else -> "Obese"
        }
    }
}