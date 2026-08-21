package com.example.bmitracker.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.example.bmitracker.data.WeightEntry
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun WeightChart(history: List<WeightEntry>) {
    if(history.isEmpty()){
        Text("No weight data available for the last 7 days.")
        return
    }
    val sorted = history.sortedBy { it.date }
    val minWeight = sorted.minOf { it.weightkg }
    val maxWeight = sorted.maxOf { it.weightkg }
    val range = if(maxWeight - minWeight < 1.0)
                    1.0
                else
                    maxWeight - minWeight
    val primary = MaterialTheme.colorScheme.primary
    Canvas(modifier = Modifier.fillMaxWidth().height(240.dp))
    {
        val width = size.width
        val height = size.height
        val leftPadding =40f
        val rightPadding =20f
        val topPadding =20f
        val bottomPadding =30f
        val chartWidth= width - leftPadding - rightPadding
        val chartHeight= height - topPadding - bottomPadding
        for(i in 0..4){
            val y = topPadding+chartHeight*i/4f
            drawLine( color = MaterialTheme.colorScheme.outlineVariant,
                      start =androidx.compose.ui.geometry.
                                Offset(leftPadding,y),
                      end = androidx.compose.ui.geometry.
                                Offset(width-rightPadding,y),
                      strokeWidth = 1f
            )
        }
        if(sorted.size == 1){
            val x= leftPadding+chartWidth/2
            val y =topPadding+chartHeight/2
            drawCircle(color = primary,radius = 8f,
                center =androidx.compose.ui.geometry.Offset(x,y)
            )
        }else{
            val path = Path()
            sorted.forEachIndexed{ index, entry ->
                val x= leftPadding+chartWidth+index/(sorted.size -1)
                val y = topPadding+chartHeight-
                        ((entry.weightkg-minWeight)/range).
                        toFloat()*chartHeight
                if(index == 0){
                    path.moveTo(x,y)
                }else{
                    path.lineTo(x,y)
                }
                drawCircle(color = primary, radius =7f,
                    center = androidx.compose.ui.geometry.Offset(x,y)
                )
            }
            drawPath(path = path, color = primary,style = Stroke(width =5f))
        }
    }
}