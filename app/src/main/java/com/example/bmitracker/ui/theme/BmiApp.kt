package com.example.bmitracker.ui.theme

import androidx.compose.runtime.*
import com.example.bmitracker.viewmodel.AppViewModel

enum class Screen{
    LOGIN,
    REGISTER,
    FORGOT_PASSWORD,
    PROFILE,
    DASHBOARD,
    USERS,
    SETTINGS
}
@Composable
fun BmiApp(
    viewModel: AppViewModel,
    onGoogleLogin: () -> Unit
){
    val state by viewModel.uiState.collectAsState()
    var screen by rememberSaverable{
        mutableStateOf(Screen.LOGIN)
    }
    LaunchedEffect(state.firebaseUser, state.profiles.size){
        if(state.firebaseUser == null){
            screen = Screen.LOGIN
        }
        else if(state.profiles.isEmpty()){
            screen = Screen.PROFILE
        }
        else if(screen == Screen.LOGIN || screen == Screen.REGISTER || screen == Screen.FORGOT_PASSWORD){
            screen = Screen.DASHBOARD
        }
    }
    when(screen){
        Screen.LOGIN -> {
            LoginScreen(
                loading =state.loading,
                error = state.error,
                onLogin = { email, password ->
                    viewModel.login(email, password)
                },
                onGoogleLogin = onGoogleLogin,
                onRegister ={
                    screen = Screen.REGISTER
                },
                onForgotPassword = {
                    screen = Screen.FORGOT_PASSWORD
                }
            )
        }
        Screen.REGISTER -> {
            RegisterScreen(
                loading = state.loading,
                error = state.error,
                onRegister = { email, password ->
                    viewModel.register(email, password)
                },
               onBack = {
                   screen = Screen.LOGIN
               }
            )
        }
        Screen.FORGOT_PASSWORD -> {
            ForgotPasswordScreen(
               message =state.message,
                error = state.error,
                onReset ={
                    viewModel.resetPassword(it)
                },
                onBack = {
                    screen = Screen.LOGIN
                }
            )
        }
        Screen.PROFILE -> {
            ProfileScreen(
                existingProfile = state.selectedProfile,
                title =
                    if(state.selectedProfile == null){
                        "Create Profile"
                    }else{
                        "Edit Profile"
                    },
                onSave ={profile ->
                    viewModel.saveProfile(profile)
                    screen = Screen.DASHBOARD
                },
                onBack = {
                  if (state.profiles.isNotEmpty()){
                      screen = Screen.DASHBOARD
                  }
                }
            )
        }

        Screen.DASHBOARD -> {
            DashboardScreen(
                profile = state.selectedProfile,
                history = state.history,
                onSettings = {
                    screen = Screen.SETTINGS
                },
                onUsers ={
                    screen = Screen.USERS
                },
                onLogout ={
                  viewModel.signOut()
                }
            )
        }
        Screen.USERS -> {
            UsersScreen(
                profiles = state.profiles,
                selected = state.selectedProfile,
                onSelect ={
                    viewModel.selectProfile(it)
                    screen = Screen.DASHBOARD
                },
                onAdd ={
                    screen = Screen.PROFILE
                },
                onEdit= {
                    viewModel.selectProfile(it)
                    screen = Screen.PROFILE
                },
                onDelete={
                    viewModel.deleteProfile(it)
                },
                onBack = {
                    screen = Screen.DASHBOARD
                }
            )
        }
        Screen.SETTINGS -> {
            ProfileScreen(
                existingProfile = state.selectedProfile,
                title = "Update Health Data",
                onSave ={profile ->
                    viewModel.saveProfile(profile)
                    screen = Screen.DASHBOARD
                },
                onBack = {
                    screen = Screen.DASHBOARD
                }
            )
        }
    }
}