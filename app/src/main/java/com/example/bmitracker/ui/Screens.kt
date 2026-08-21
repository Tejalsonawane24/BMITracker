package com.example.bmitracker.ui

import android.app.DatePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bmitracker.data.UserProfile
import com.example.bmitracker.data.WeightEntry
import com.example.bmitracker.util.BmiUtils
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun LoginScreen(
    loading: Boolean,
    error: String,
    onLogin: (String, String) -> Unit,
    onGoogleLogin: () -> Unit,
    onRegister: () -> Unit,
    onForgotPassword: () -> Unit
) {

    var email by rememberSaveable {
        mutableStateOf("")
    }

    var password by rememberSaveable {
        mutableStateOf("")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(28.dp),
        verticalArrangement =
            Arrangement.Center
    ) {

        Text(
            text = "BMI Tracker",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "Track your health. Stay healthy.",
            modifier = Modifier.padding(
                top = 6.dp,
                bottom = 30.dp
            )
        )

        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
            },
            label = {
                Text("Email")
            },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(
            Modifier.height(12.dp)
        )

        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
            },
            label = {
                Text("Password")
            },
            visualTransformation =
                PasswordVisualTransformation(),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        TextButton(
            onClick = onForgotPassword,
            modifier = Modifier.align(
                Alignment.End
            )
        ) {
            Text("Forgot Password?")
        }

        ErrorText(error)

        LoadingButton(
            text = "Sign In",
            loading = loading,
            onClick = {
                onLogin(
                    email.trim(),
                    password
                )
            }
        )

        Spacer(
            Modifier.height(12.dp)
        )

        OutlinedButton(
            onClick = onGoogleLogin,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
        ) {
            Text("Continue with Google")
        }

        Spacer(
            Modifier.height(16.dp)
        )

        TextButton(
            onClick = onRegister,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Create new account")
        }
    }
}

@Composable
fun RegisterScreen(
    loading: Boolean,
    error: String,
    onRegister: (String, String) -> Unit,
    onBack: () -> Unit
) {

    var email by rememberSaveable {
        mutableStateOf("")
    }

    var password by rememberSaveable {
        mutableStateOf("")
    }

    var confirmPassword by rememberSaveable {
        mutableStateOf("")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(28.dp),
        verticalArrangement =
            Arrangement.Center
    ) {

        Text(
            "Create Account",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(
            Modifier.height(24.dp)
        )

        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
            },
            label = {
                Text("Email")
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(
            Modifier.height(12.dp)
        )

        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
            },
            label = {
                Text("Password")
            },
            visualTransformation =
                PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(
            Modifier.height(12.dp)
        )

        OutlinedTextField(
            value = confirmPassword,
            onValueChange = {
                confirmPassword = it
            },
            label = {
                Text("Confirm Password")
            },
            visualTransformation =
                PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        ErrorText(error)

        LoadingButton(
            text = "Create Account",
            loading = loading,
            onClick = {

                if (password != confirmPassword) {
                    return@LoadingButton
                }

                onRegister(
                    email.trim(),
                    password
                )
            }
        )

        TextButton(
            onClick = onBack,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Back to Login")
        }
    }
}

@Composable
fun ForgotPasswordScreen(
    message: String,
    error: String,
    onReset: (String) -> Unit,
    onBack: () -> Unit
) {

    var email by rememberSaveable {
        mutableStateOf("")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(28.dp),
        verticalArrangement =
            Arrangement.Center
    ) {

        Text(
            "Reset Password",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(
            Modifier.height(12.dp)
        )

        Text(
            "Enter your registered email address."
        )

        Spacer(
            Modifier.height(24.dp)
        )

        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
            },
            label = {
                Text("Email")
            },
            modifier = Modifier.fillMaxWidth()
        )

        ErrorText(error)

        if (message.isNotBlank()) {

            Text(
                text = message,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(8.dp)
            )
        }

        AppButton(
            text = "Send Reset Email",
            onClick = {
                onReset(email.trim())
            }
        )

        TextButton(
            onClick = onBack,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Back to Login")
        }
    }
}

@Composable
fun ProfileScreen(
    existingProfile: UserProfile?,
    title: String,
    onSave: (UserProfile) -> Unit,
    onBack: () -> Unit
) {

    val context = LocalContext.current

    var name by rememberSaveable(
        existingProfile?.id
    ) {
        mutableStateOf(
            existingProfile?.name ?: ""
        )
    }

    var dob by rememberSaveable(
        existingProfile?.id
    ) {
        mutableStateOf(
            existingProfile?.dateOfBirth ?: ""
        )
    }

    var gender by rememberSaveable(
        existingProfile?.id
    ) {
        mutableStateOf(
            existingProfile?.gender ?: ""
        )
    }

    var weight by rememberSaveable(
        existingProfile?.id
    ) {
        mutableStateOf(
            if (existingProfile != null)
                existingProfile.weight.toString()
            else ""
        )
    }

    var weightUnit by rememberSaveable(
        existingProfile?.id
    ) {
        mutableStateOf(
            existingProfile?.weightUnit ?: "KG"
        )
    }

    var height by rememberSaveable(
        existingProfile?.id
    ) {
        mutableStateOf(
            if (existingProfile != null)
                existingProfile.height.toString()
            else ""
        )
    }

    var heightUnit by rememberSaveable(
        existingProfile?.id
    ) {
        mutableStateOf(
            existingProfile?.heightUnit ?: "CM"
        )
    }

    var error by remember {
        mutableStateOf("")
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {

        item {

            Text(
                title,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(
                Modifier.height(24.dp)
            )

            OutlinedTextField(
                value = name,
                onValueChange = {
                    name = it
                },
                label = {
                    Text("Name")
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(
                Modifier.height(12.dp)
            )

            OutlinedButton(
                onClick = {

                    val calendar =
                        Calendar.getInstance()

                    DatePickerDialog(
                        context,
                        { _, year, month, day ->

                            val selected =
                                Calendar.getInstance()

                            selected.set(
                                year,
                                month,
                                day
                            )

                            val formatter =
                                SimpleDateFormat(
                                    "yyyy-MM-dd",
                                    Locale.getDefault()
                                )

                            dob =
                                formatter.format(
                                    selected.time
                                )
                        },

                        calendar.get(
                            Calendar.YEAR
                        ),

                        calendar.get(
                            Calendar.MONTH
                        ),

                        calendar.get(
                            Calendar.DAY_OF_MONTH
                        )

                    ).show()
                },
                modifier = Modifier.fillMaxWidth()
            ) {

                Text(
                    if (dob.isBlank())
                        "Select Date of Birth"
                    else
                        dob
                )
            }

            Spacer(
                Modifier.height(12.dp)
            )

            Text(
                "Gender",
                fontWeight = FontWeight.Medium
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement =
                    Arrangement.spacedBy(8.dp)
            ) {

                listOf(
                    "Male",
                    "Female",
                    "Other"
                ).forEach {

                    FilterChip(
                        selected =
                            gender == it,
                        onClick = {
                            gender = it
                        },
                        label = {
                            Text(it)
                        }
                    )
                }
            }

            Spacer(
                Modifier.height(16.dp)
            )

            OutlinedTextField(
                value = weight,
                onValueChange = {
                    weight = it
                },
                label = {
                    Text("Weight")
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement =
                    Arrangement.spacedBy(8.dp)
            ) {

                FilterChip(
                    selected =
                        weightUnit == "KG",
                    onClick = {
                        weightUnit = "KG"
                    },
                    label = {
                        Text("KG")
                    }
                )

                FilterChip(
                    selected =
                        weightUnit == "LB",
                    onClick = {
                        weightUnit = "LB"
                    },
                    label = {
                        Text("LB")
                    }
                )
            }

            Spacer(
                Modifier.height(16.dp)
            )

            OutlinedTextField(
                value = height,
                onValueChange = {
                    height = it
                },
                label = {
                    Text("Height")
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement =
                    Arrangement.spacedBy(8.dp)
            ) {

                FilterChip(
                    selected =
                        heightUnit == "CM",
                    onClick = {
                        heightUnit = "CM"
                    },
                    label = {
                        Text("CM")
                    }
                )

                FilterChip(
                    selected =
                        heightUnit == "IN",
                    onClick = {
                        heightUnit = "IN"
                    },
                    label = {
                        Text("IN")
                    }
                )
            }

            Spacer(
                Modifier.height(20.dp)
            )

            ErrorText(error)

            AppButton(
                text = "Save",
                onClick = {

                    val weightValue =
                        weight.toDoubleOrNull()

                    val heightValue =
                        height.toDoubleOrNull()

                    when {

                        name.isBlank() ->
                            error =
                                "Name is required"

                        dob.isBlank() ->
                            error =
                                "Date of birth is required"

                        gender.isBlank() ->
                            error =
                                "Please select gender"

                        weightValue == null ||
                                weightValue <= 0 ->
                            error =
                                "Enter a valid weight"

                        heightValue == null ||
                                heightValue <= 0 ->
                            error =
                                "Enter a valid height"

                        else -> {

                            val bmi =
                                BmiUtils.calculateBmi(
                                    weightValue,
                                    weightUnit,
                                    heightValue,
                                    heightUnit
                                )

                            onSave(
                                UserProfile(
                                    id =
                                        existingProfile?.id
                                            ?: "",
                                    name = name.trim(),
                                    dateOfBirth = dob,
                                    gender = gender,
                                    weight =
                                        weightValue,
                                    weightUnit =
                                        weightUnit,
                                    height =
                                        heightValue,
                                    heightUnit =
                                        heightUnit,
                                    bmi =
                                        bmi
                                )
                            )
                        }
                    }
                }
            )

            Spacer(
                Modifier.height(8.dp)
            )

            if (existingProfile != null) {

                TextButton(
                    onClick = onBack,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Cancel")
                }
            }
        }
    }
}

@Composable
fun DashboardScreen(
    profile: UserProfile?,
    history: List<WeightEntry>,
    onSettings: () -> Unit,
    onUsers: () -> Unit,
    onLogout: () -> Unit
) {

    if (profile == null) {
        return
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {

        item {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement =
                    Arrangement.SpaceBetween,
                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                Column {

                    Text(
                        "Hello, ${profile.name}",
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        "Your health dashboard"
                    )
                }

                TextButton(
                    onClick = onLogout
                ) {
                    Text("Logout")
                }
            }

            Spacer(
                Modifier.height(24.dp)
            )

            BmiCard(profile)

            Spacer(
                Modifier.height(20.dp)
            )

            Text(
                "Weight History",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(
                Modifier.height(10.dp)
            )

            WeightChart(history)

            Spacer(
                Modifier.height(20.dp)
            )

            AppButton(
                text = "Manage Users",
                onClick = onUsers
            )

            Spacer(
                Modifier.height(10.dp)
            )

            OutlinedButton(
                onClick = onSettings,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Update Health Data")
            }
        }
    }
}

@Composable
private fun BmiCard(
    profile: UserProfile
) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp)
    ) {

        Column(
            modifier = Modifier.padding(24.dp)
        ) {

            Text(
                "Current BMI",
                fontSize = 18.sp
            )

            Text(
                String.format(
                    Locale.getDefault(),
                    "%.1f",
                    profile.bmi
                ),
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                BmiUtils.category(
                    profile.bmi
                ),
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(
                Modifier.height(12.dp)
            )

            Text(
                "Weight: ${profile.weight} ${profile.weightUnit}"
            )

            Text(
                "Height: ${profile.height} ${profile.heightUnit}"
            )
        }
    }
}

@Composable
fun UsersScreen(
    profiles: List<UserProfile>,
    selected: UserProfile?,
    onSelect: (UserProfile) -> Unit,
    onAdd: () -> Unit,
    onEdit: (UserProfile) -> Unit,
    onDelete: (String) -> Unit,
    onBack: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement =
                Arrangement.SpaceBetween,
            verticalAlignment =
                Alignment.CenterVertically
        ) {

            Text(
                "Manage Users",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )

            TextButton(
                onClick = onBack
            ) {
                Text("Back")
            }
        }

        Spacer(
            Modifier.height(16.dp)
        )

        AppButton(
            text = "+ Add User",
            onClick = onAdd
        )

        Spacer(
            Modifier.height(16.dp)
        )

        LazyColumn(
            verticalArrangement =
                Arrangement.spacedBy(12.dp)
        ) {

            items(profiles) { profile ->

                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {

                    Column(
                        modifier =
                            Modifier.padding(16.dp)
                    ) {

                        Text(
                            profile.name,
                            fontSize = 20.sp,
                            fontWeight =
                                FontWeight.Bold
                        )

                        Text(
                            "BMI: ${
                                String.format(
                                    Locale.getDefault(),
                                    "%.1f",
                                    profile.bmi
                                )
                            }"
                        )

                        Row(
                            horizontalArrangement =
                                Arrangement.spacedBy(8.dp)
                        ) {

                            Button(
                                onClick = {
                                    onSelect(profile)
                                }
                            ) {

                                Text(
                                    if (
                                        selected?.id ==
                                        profile.id
                                    ) {
                                        "Selected"
                                    } else {
                                        "Switch"
                                    }
                                )
                            }

                            OutlinedButton(
                                onClick = {
                                    onEdit(profile)
                                }
                            ) {
                                Text("Edit")
                            }

                            if (profiles.size > 1) {

                                OutlinedButton(
                                    onClick = {
                                        onDelete(
                                            profile.id
                                        )
                                    }
                                ) {
                                    Text("Delete")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}