package com.example.mastermindAndroid.activities

import android.annotation.SuppressLint
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.mastermindAndroid.R
import com.example.mastermindAndroid.data.ProfileViewModel
import com.example.mastermindAndroid.data.User
import com.example.mastermindAndroid.nav.Screen
import com.example.mastermindAndroid.ui.theme.DarkColorScheme
import kotlinx.coroutines.launch

@Composable
private fun ProfileImageWithPicker(profileImageUri: Uri?, selectImageOnClick: () -> Unit) {
    IconButton(
        modifier = Modifier.then(Modifier.size(100.dp)),
        onClick = selectImageOnClick,
    ) {
        Box {
            if (profileImageUri != null) {
                AsyncImage(
                    model = profileImageUri,
                    contentDescription = "Loaded image",
                    modifier = Modifier.size(100.dp)
                )
            } else {
                Image(
                    painter = painterResource(
                        id = R.drawable.ic_baseline_question_mark_24
                    ),
                    contentDescription = "Profile photo",
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape),
                )
            }
            Image(
                painter = painterResource(
                    id = R.drawable.baseline_image_search_24
                ),
                contentDescription = "Profile photo",
                modifier = Modifier
                    .size(20.dp)
                    .clip(CircleShape)
                    .align(Alignment.TopEnd),
            )
        }
    }
}

@SuppressLint("Recycle")
@Composable
fun LoginScreenInitial(
    navController: NavController,
    viewModel: ProfileViewModel = hiltViewModel<ProfileViewModel>()
) {
    val playerName = rememberSaveable { mutableStateOf("") }
    val playerEmail = rememberSaveable { mutableStateOf("") }
    val colorsToGuess = rememberSaveable { mutableStateOf("5") }
    val imageUri = rememberSaveable { mutableStateOf<Uri?>(null) }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    var wasNameTouched by rememberSaveable { mutableStateOf(false) }
    var wasEmailTouched by rememberSaveable { mutableStateOf(false) }

    val nameError = playerName.value.isBlank()
    val emailError = !android.util.Patterns.EMAIL_ADDRESS.matcher(playerEmail.value).matches()

    val nameErrorAndTouched = nameError && wasNameTouched
    val emailErrorAndTouched = emailError && wasEmailTouched

    val isButtonEnabled = !nameError && !emailError

    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { selectedUri ->
            if (selectedUri != null) {
                imageUri.value = selectedUri
            }
        })

    val infiniteTransition = rememberInfiniteTransition(label = "infiniteTransition")
    val scale by infiniteTransition.animateFloat(
        initialValue = 0.8F,
        targetValue = 1.2F,
        animationSpec = infiniteRepeatable(tween(400), RepeatMode.Reverse),
        label = "scaleInfiniteTransition"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "MasterAnd",
            color = DarkColorScheme.primary,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.displayLarge,
            modifier = Modifier
                .padding(bottom = 48.dp)
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                    transformOrigin = TransformOrigin.Center
                },
        )

        Box {
            ProfileImageWithPicker(profileImageUri = imageUri.value, selectImageOnClick = {
                imagePicker.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                )
            })
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextFieldWithError(
            label = "Name",
            value = playerName.value,
            onValueChange = {
                playerName.value = it
                if (it.isNotEmpty()) {
                    wasNameTouched = true
                }
            },
            isError = nameErrorAndTouched,
            errorMessage = "Name can't be empty",
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            modifier = Modifier.onFocusChanged { focusState ->
                if (focusState.isFocused) {
                    wasNameTouched = true
                }
            }
        )

        OutlinedTextFieldWithError(
            label = "E-mail",
            value = playerEmail.value,
            onValueChange = {
                playerEmail.value = it
                if (it.isNotEmpty()) {
                    wasEmailTouched = true
                }
            },
            isError = emailErrorAndTouched,
            errorMessage = "Enter a valid email",
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier.onFocusChanged { focusState ->
                if (focusState.isFocused) {
                    wasEmailTouched = true
                }
            }
        )

        Slider(
            value = colorsToGuess.value.toFloat(),
            onValueChange = { colorsToGuess.value = it.toInt().toString() },
            valueRange = 5f..8f,
            steps = 2,
            colors = SliderDefaults.colors(
                thumbColor = DarkColorScheme.secondary,
                activeTrackColor = DarkColorScheme.secondary,
                inactiveTrackColor = DarkColorScheme.secondaryContainer
            ),
            modifier = Modifier.fillMaxWidth()
        )
        Text(text = "Colors: ${colorsToGuess.value}")

        Button(
            onClick = {
                coroutineScope.launch {
                    Log.println(Log.INFO, "POTUSER", "START")
                    Log.println(Log.INFO, "VIEWMODEL", (viewModel.user).toString())
                    viewModel.getUser(playerEmail.value)
                    Log.println(Log.INFO, "VIEWMODEL", (viewModel.user).toString())
                    Log.println(Log.INFO, "POTUSER", "END")
                    if (viewModel.user.value.email != playerEmail.value) {
                        if (imageUri.value != null) {
                            val user = (
                                    User(
                                        name = playerName.value,
                                        email = playerEmail.value,
                                        imageData = context.contentResolver.openInputStream(imageUri.value!!)
                                            ?.readBytes()!!
                                    )
                                    )
                            viewModel.user.value = user
                            viewModel.insertUser(user)

                        } else {
                            val user = (
                                    User(
                                        name = playerName.value,
                                        email = playerEmail.value,
                                        imageData = null
                                    )
                                    )
                            viewModel.user.value = user
                            viewModel.insertUser(user)
                        }
                    }
                    viewModel.guessColors.intValue = colorsToGuess.value.toInt()
                    val uriString = imageUri.value?.toString()
                    navController.navigate(Screen.Profile.route + if (uriString != null) "?imageUri=$uriString" else "")
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 14.dp),
            enabled = isButtonEnabled,
            shape = RoundedCornerShape(30),

            colors = ButtonDefaults.buttonColors(
                containerColor = DarkColorScheme.primary,
                contentColor = DarkColorScheme.surface
            )

        ) {
            Text(text = "Login")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OutlinedTextFieldWithError(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    isError: Boolean,
    errorMessage: String,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    modifier: Modifier = Modifier
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(text = label) },
            modifier = modifier.then(Modifier.fillMaxWidth()),
            isError = isError,
            keyboardOptions = keyboardOptions,
            trailingIcon = {
                if (isError) {
                    Icon(
                        imageVector = Icons.Default.Error,
                        contentDescription = "Error",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            },
        )
        if (isError) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
            )
        }
    }
}
