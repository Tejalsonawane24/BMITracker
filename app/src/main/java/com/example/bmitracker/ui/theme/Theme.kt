package com.example.bmitracker.ui.theme

import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.material3.MaterialTheme

private val AppColors = lightColorScheme()
@Composable
fun BmiTheme(content: @Composable () -> Unit){
    MaterialTheme(colorScheme = AppColors, content = content)
}