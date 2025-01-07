package com.example.mastermindAndroid.activities

import android.annotation.SuppressLint
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardDefaults.cardColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.mastermindAndroid.R
import com.example.mastermindAndroid.data.ProfileViewModel
import com.example.mastermindAndroid.data.User
import com.example.mastermindAndroid.nav.Screen
import com.example.mastermindAndroid.ui.theme.DarkColorScheme
import com.google.accompanist.drawablepainter.rememberDrawablePainter
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("SuspiciousIndentation")
@Composable
fun ProfileScreenInitial(
    navController: NavController,
    viewModel: ProfileViewModel = hiltViewModel<ProfileViewModel>(),
    imageUri: Uri? = null
) {
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        bottomBar = {
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    onClick = {
                        viewModel.user.value = User()
                        navController.navigate(route = Screen.Login.route)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(30),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = DarkColorScheme.primary,
                        contentColor = DarkColorScheme.surface
                    )
                ) {
                    Text("Logout", color = Color.White)
                }
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
                    colors = cardColors(
                        containerColor = Color(0xFFDED0FF)
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            if (imageUri == null) {
                                Image(
                                    painter = painterResource(id = R.drawable.ic_baseline_question_mark_24),
                                    contentDescription = "Profile photo",
                                    modifier = Modifier
                                        .size(100.dp)
                                        .clip(CircleShape)
                                        .border(2.dp, Color.Gray, CircleShape),
                                    contentScale = ContentScale.Crop
                                )
                            } else {
                                Image(
                                    painter = rememberAsyncImagePainter(imageUri),
                                    contentDescription = "Profile image",
                                    modifier = Modifier
                                        .size(100.dp)
                                        .clip(CircleShape)
                                        .border(2.dp, Color.Gray, CircleShape),
                                    contentScale = ContentScale.Crop
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text(
                                text = viewModel.user.value.name,
                                style = MaterialTheme.typography.headlineLarge,
                                color = Color.Black
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Games: ${viewModel.user.value.games}",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.Black
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Won: ${viewModel.user.value.wins}",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.Black
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Points: ${viewModel.user.value.points}",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.Black
                            )
                        }
                    }
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Button(
                        onClick = {
                            Log.d("ProfileScreen", "Navigating to ScoreScreen with imageUri: $imageUri")
                            navController.navigate(Screen.Score.route + if (imageUri != null) "?imageUri=$imageUri" else "")
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = DarkColorScheme.primary,
                            contentColor = DarkColorScheme.surface
                        )
                    ) {
                        Text("Leaderboard", color = Color.White)
                    }
                }

                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        modifier = Modifier
                            .padding(top = 80.dp)
                            .align(Alignment.BottomCenter)
                            .clickable {
                                coroutineScope.launch {
                                    viewModel.user.value.games += 1
                                    viewModel.updateUser()
                                }
                                val uriString = imageUri?.toString()
                                val route = if (uriString != null) {
                                    "${Screen.Game.route}?imageUri=$uriString"
                                } else {
                                    Screen.Game.route
                                }
                                navController.navigate(route)
                            },
                        painter = rememberDrawablePainter(
                            drawable = ContextCompat.getDrawable(
                                LocalContext.current,
                                R.drawable.letsplay
                            )
                        ),
                        contentDescription = "Play",
                        contentScale = ContentScale.FillWidth,
                    )
                }
            }
        }
    }
}
