package com.example.bmitracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.bmitracker.ui.BmiApp
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.bmitracker.ui.theme.BMITrackerTheme
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {
    private val viewModel: AppViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.initialize(this)
        setContent {
          BmiTheme {
              BmiApp(
                  viewModel = viewModel,
                  onGoogleLogin ={
                      lifecycleScope.launch {
                          try {
                              val idToken = GoogleAuthHelper.signIn(this@MainActivity)
                              val credential = com.google.firebase.auth.GoogleAuthProvider.getCredential(idToken,null)
                              FirebaseAuth.getInstance().signInWithCredential(credential).addOnSuccessListener{
                                  viewModel.loginWithGoogleSuccess()
                              }.addOnFailureListener{}
                          }catch (e: Exception){
                              viewModel.clearMessage()
                          }
                      }
                  }
              )
          }
        }
    }
}
