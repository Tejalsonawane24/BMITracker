package com.example.bmitracker.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class FirebaseRepository {

    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    private fun profilesCollection() =
        firestore
            .collection("users")
            .document(auth.currentUser!!.uid)
            .collection("profiles")

    private fun historyCollection(profileId: String) =
        profilesCollection()
            .document(profileId)
            .collection("weightHistory")

    fun signIn(
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener {
                onError(it.message ?: "Login failed")
            }
    }

    fun register(
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener {
                onError(it.message ?: "Registration failed")
            }
    }

    fun resetPassword(
        email: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        auth.sendPasswordResetEmail(email)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener {
                onError(it.message ?: "Could not send reset email")
            }
    }

    fun signOut() {
        auth.signOut()
    }

    fun getCurrentUser() = auth.currentUser

    fun saveProfile(
        profile: UserProfile,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {

        val profileId =
            if (profile.id.isBlank()) {
                profilesCollection().document().id
            } else {
                profile.id
            }

        val finalProfile = profile.copy(
            id = profileId,
            updatedAt = System.currentTimeMillis()
        )

        val data = hashMapOf(
            "id" to finalProfile.id,
            "name" to finalProfile.name,
            "dateOfBirth" to finalProfile.dateOfBirth,
            "gender" to finalProfile.gender,
            "weight" to finalProfile.weight,
            "weightUnit" to finalProfile.weightUnit,
            "height" to finalProfile.height,
            "heightUnit" to finalProfile.heightUnit,
            "bmi" to finalProfile.bmi,
            "updatedAt" to finalProfile.updatedAt
        )

        profilesCollection()
            .document(profileId)
            .set(data)
            .addOnSuccessListener {

                val historyData = hashMapOf(
                    "weightKg" to finalProfile.weightToKg(),
                    "date" to System.currentTimeMillis()
                )

                historyCollection(profileId)
                    .add(historyData)
                    .addOnSuccessListener {
                        onSuccess()
                    }
                    .addOnFailureListener {
                        onSuccess()
                    }
            }
            .addOnFailureListener {
                onError(it.message ?: "Could not save profile")
            }
    }

    fun getProfiles(
        onSuccess: (List<UserProfile>) -> Unit,
        onError: (String) -> Unit
    ) {

        profilesCollection()
            .get()
            .addOnSuccessListener { result ->

                val profiles = result.documents.map { doc ->

                    UserProfile(
                        id = doc.getString("id") ?: doc.id,
                        name = doc.getString("name") ?: "",
                        dateOfBirth = doc.getString("dateOfBirth") ?: "",
                        gender = doc.getString("gender") ?: "",
                        weight = doc.getDouble("weight") ?: 0.0,
                        weightUnit = doc.getString("weightUnit") ?: "KG",
                        height = doc.getDouble("height") ?: 0.0,
                        heightUnit = doc.getString("heightUnit") ?: "CM",
                        bmi = doc.getDouble("bmi") ?: 0.0,
                        updatedAt = doc.getLong("updatedAt") ?: 0L
                    )
                }

                onSuccess(profiles)
            }
            .addOnFailureListener {
                onError(it.message ?: "Could not load profiles")
            }
    }

    fun getHistory(
        profileId: String,
        onSuccess: (List<WeightEntry>) -> Unit,
        onError: (String) -> Unit
    ) {

        val sevenDaysAgo =
            System.currentTimeMillis() - (7L * 24L * 60L * 60L * 1000L)

        historyCollection(profileId)
            .whereGreaterThanOrEqualTo("date", sevenDaysAgo)
            .orderBy("date", Query.Direction.ASCENDING)
            .get()
            .addOnSuccessListener { result ->

                val history = result.documents.map { doc ->

                    WeightEntry(
                        id = doc.id,
                        weightKg = doc.getDouble("weightKg") ?: 0.0,
                        date = doc.getLong("date") ?: 0L
                    )
                }

                onSuccess(history)
            }
            .addOnFailureListener {
                onError(it.message ?: "Could not load history")
            }
    }

    fun deleteProfile(
        profileId: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {

        profilesCollection()
            .document(profileId)
            .delete()
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener {
                onError(it.message ?: "Could not delete profile")
            }
    }
}

private fun UserProfile.weightToKg(): Double {
    return if (weightUnit == "LB") {
        weight * 0.45359237
    } else {
        weight
    }
}