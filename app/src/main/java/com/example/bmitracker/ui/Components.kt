package com.example.bmitracker.ui

import android.widget.Button
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AppButton(text: String, onClick: ()-> Unit, modifier: Modifier = Modifier)
{
    Button(onClick= onClick, modifier =modifier.fillMaxWidth().height(52.dp)){
        Text(text)
    }
}
@Composable
fun ErrorText(text: String){
    if(text.isNotBlank()){
        Text(text = text, color = MaterialTheme.colorScheme.error,
            modifier = Modifier.padding(8.dp))
    }
}
@Composable
fun LoadingButton(text: String, loading: Boolean, onClick: () -> Unit){
    Button(onClick = onClick, enabled = !loading,
        modifier = Modifier.fillMaxWidth().height(52.dp)){
        if(loading){
            CircularProgressIndicator(
                modifier = Modifier.size(22.dp),
                strokeWidth = 2.dp
            )
        }else{
            Text(text)
        }
    }
}