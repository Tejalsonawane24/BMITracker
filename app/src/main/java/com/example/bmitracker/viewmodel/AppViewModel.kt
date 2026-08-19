package com.example.bmitracker.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.bmitracker.data.FirebaseRepository
import com.example.bmitracker.data.UserProfile
import com.example.bmitracker.data.WeightEntry
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

data class AppUiState(
    val firebaseUser: FirebaseUser? = null,
    val profiles: List<UserProfile> = emptyList(),
    val selectedProfile: UserProfile? = null,
    val history: List<WeightEntry> = emptyList(),
    val loading: Boolean = false,
    val message: String = "",
    val error: String = ""
)

class AppViewModel : ViewModel() {

    private val repository = FirebaseRepository()

    private val _uiState = MutableStateFlow(AppUiState())
    val uiState = _uiState.asStateFlow()

    private var preferences: android.content.SharedPreferences? = null

    fun initialize(context: Context) {

        preferences =
            context.getSharedPreferences(
                "bmi_preferences",
                Context.MODE_PRIVATE
            )

        val user = repository.getCurrentUser()

        _uiState.value =
            _uiState.value.copy(firebaseUser = user)

        if (user != null) {
            loadProfiles()
        }
    }

    fun login(
        email: String,
        password: String
    ) {

        if (email.isBlank() || password.isBlank()) {
            showError("Please enter email and password")
            return
        }

        setLoading(true)

        repository.signIn(
            email = email,
            password = password,

            onSuccess = {
                setLoading(false)
                loadProfiles()
            },

            onError = {
                setLoading(false)
                showError(it)
            }
        )
    }

    fun register(
        email: String,
        password: String
    ) {

        if (!android.util.Patterns.EMAIL_ADDRESS
                .matcher(email)
                .matches()
        ) {
            showError("Enter a valid email address")
            return
        }

        if (password.length < 6) {
            showError("Password must contain at least 6 characters")
            return
        }

        setLoading(true)

        repository.register(
            email,
            password,

            onSuccess = {
                setLoading(false)

                _uiState.value =
                    _uiState.value.copy(
                        firebaseUser = repository.getCurrentUser()
                    )
            },

            onError = {
                setLoading(false)
                showError(it)
            }
        )
    }

    fun loginWithGoogleSuccess() {

        _uiState.value =
            _uiState.value.copy(
                firebaseUser = repository.getCurrentUser(),
                error = ""
            )

        loadProfiles()
    }

    fun resetPassword(email: String) {

        if (!android.util.Patterns.EMAIL_ADDRESS
                .matcher(email)
                .matches()
        ) {
            showError("Enter a valid email address")
            return
        }

        repository.resetPassword(
            email,

            onSuccess = {
                showMessage("Password reset email sent")
            },

            onError = {
                showError(it)
            }
        )
    }

    fun saveProfile(profile: UserProfile) {

        setLoading(true)

        repository.saveProfile(
            profile,

            onSuccess = {
                setLoading(false)
                loadProfiles()
                showMessage("Profile saved successfully")
            },

            onError = {
                setLoading(false)
                showError(it)
            }
        )
    }

    fun loadProfiles() {

        if (repository.getCurrentUser() == null) {
            return
        }

        repository.getProfiles(

            onSuccess = { profiles ->

                val savedId =
                    preferences
                        ?.getString("selected_profile_id", null)

                val selected =
                    profiles.find {
                        it.id == savedId
                    } ?: profiles.firstOrNull()

                selected?.let {
                    preferences
                        ?.edit()
                        ?.putString(
                            "selected_profile_id",
                            it.id
                        )
                        ?.apply()
                }

                _uiState.value =
                    _uiState.value.copy(
                        firebaseUser =
                            repository.getCurrentUser(),
                        profiles = profiles,
                        selectedProfile = selected
                    )

                selected?.let {
                    loadHistory(it.id)
                }
            },

            onError = {
                showError(it)
            }
        )
    }

    fun selectProfile(profile: UserProfile) {

        preferences
            ?.edit()
            ?.putString(
                "selected_profile_id",
                profile.id
            )
            ?.apply()

        _uiState.value =
            _uiState.value.copy(
                selectedProfile = profile
            )

        loadHistory(profile.id)
    }

    fun loadHistory(profileId: String) {

        repository.getHistory(
            profileId,

            onSuccess = {
                _uiState.value =
                    _uiState.value.copy(
                        history = it
                    )
            },

            onError = {
                showError(it)
            }
        )
    }

    fun deleteProfile(profileId: String) {

        repository.deleteProfile(
            profileId,

            onSuccess = {
                loadProfiles()
                showMessage("Profile deleted")
            },

            onError = {
                showError(it)
            }
        )
    }

    fun signOut() {

        repository.signOut()

        preferences
            ?.edit()
            ?.remove("selected_profile_id")
            ?.apply()

        _uiState.value =
            AppUiState()
    }

    fun clearMessage() {

        _uiState.value =
            _uiState.value.copy(
                message = "",
                error = ""
            )
    }

    private fun setLoading(value: Boolean) {

        _uiState.value =
            _uiState.value.copy(
                loading = value
            )
    }

    private fun showMessage(value: String) {

        _uiState.value =
            _uiState.value.copy(
                message = value,
                error = ""
            )
    }

    private fun showError(value: String) {

        _uiState.value =
            _uiState.value.copy(
                error = value,
                message = ""
            )
    }
}