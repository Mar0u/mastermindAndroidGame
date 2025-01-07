package com.example.mastermindAndroid.activities

import com.example.mastermindAndroid.ui.theme.DarkColorScheme
import android.annotation.SuppressLint
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.mastermindAndroid.data.ProfileViewModel
import com.example.mastermindAndroid.nav.Screen

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("SuspiciousIndentation")
@Composable
fun ScoreScreenInitial(
    navController: NavController,
    viewModel: ProfileViewModel = hiltViewModel<ProfileViewModel>(),
    imageUri: Uri? = null
) {
    viewModel.loadUsers()
    val list = viewModel.users.collectAsState(initial = emptyList()).value
        .sortedByDescending { it.points }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        topBar = {
            Column(
                Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Leaderboard",
                    fontSize = TextUnit(40F, TextUnitType.Sp),
                    style = MaterialTheme.typography.displayLarge,
                    modifier = Modifier.padding(10.dp)
                )
            }
        },
        bottomBar = {
            Column(
                Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    onClick = {
                        val uriString = imageUri?.toString()
                        val route = if (uriString != null) {
                            "${Screen.Profile.route}?imageUri=$uriString"
                        } else {
                            Screen.Profile.route
                        }
                        navController.navigate(route)
                              },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(30),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = DarkColorScheme.primary,
                        contentColor = DarkColorScheme.surface
                    )
                ) {
                    Text("Go back")
                }
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            itemsIndexed(list) { idx, user ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp, horizontal = 5.dp)
                        .background(
                            color = if (idx == 0) Color(0xFFFFD700) else if (idx == 1) Color(
                                0xFF838383
                            ) else if (idx == 2) Color(0xFFCD7F32) else Color(0xFFD6D6D6),
                            shape = RoundedCornerShape(15.dp)
                        )
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "#${idx + 1}",
                        fontWeight = FontWeight.Bold,
                        fontSize = TextUnit(20F, TextUnitType.Sp),
                        modifier = Modifier
                            .width(50.dp),
                        textAlign = TextAlign.Center
                    )

                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 10.dp)
                    ) {
                        Text(
                            text = user.name,
                            fontWeight = FontWeight.Bold,
                            fontSize = TextUnit(18F, TextUnitType.Sp),
                            textAlign = TextAlign.Start,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        Text(
                            text = "Games: ${user.games}\nWins: ${user.wins}\nPoints: ${user.points}",
                            fontSize = TextUnit(14F, TextUnitType.Sp),
                            textAlign = TextAlign.Start,
                            lineHeight = TextUnit(18F, TextUnitType.Sp)
                        )
                    }
                }
            }
        }
    }
}
