package com.example.bmitracker

import android.app.Activity
import android.credentials.*
import com.google.android.libraries.identity.googleid.*
object GoogleAuthHelper {
    suspend fun signIn(
        activity: Activity
    ): String{
        val credentialManager= CredentialManager.create(activity)
        val googleIdOption = GetGoogleIdOption.Builder().setServerClientId(
            activity.getString(
                com.example.bmitracker.R.string.default_web_client_id
            )
        ).setFilterByAuthorizedAccounts(false).build()
        val request = GetCredentialRequest.Builder().addCredentialOption(
            googleIdOption
        ).build()
        val result = credentialManager.getCredential(activity, request)
        val credential = result.credential
        if(credential is CustomCredential &&
            credential.type ==
            GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
            ){
            val googleCredential = GoogleIdTokenCredential.createFrom(
                credential.data
            )
            return googleCredential.idToken
        }
        throw IllegalStateException("Invalid Google credential")
    }
}