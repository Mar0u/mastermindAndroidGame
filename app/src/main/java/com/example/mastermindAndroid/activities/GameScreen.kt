package com.example.mastermindAndroid.activities

import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.getDrawable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.mastermindAndroid.R
import com.example.mastermindAndroid.data.OfflineUserRepository
import com.example.mastermindAndroid.data.ProfileViewModel
import com.example.mastermindAndroid.data.UserDatabase
import com.example.mastermindAndroid.nav.Screen
import com.example.mastermindAndroid.ui.theme.DarkColorScheme
import com.example.mastermindAndroid.ui.theme.MastermindAndroidTheme
import com.google.accompanist.drawablepainter.rememberDrawablePainter
import kotlinx.coroutines.delay

@Composable
fun CircularButton(onClick: () -> Unit, color: Color) {
    val animatedColor by animateColorAsState(
        targetValue = color,
        animationSpec = tween(durationMillis = 500),
        label = "ColorAnimation"
    )

    Button(
        onClick = onClick,
        modifier = Modifier
            .size(50.dp)
            .background(color = MaterialTheme.colorScheme.background),
        border = BorderStroke(2.dp, MaterialTheme.colorScheme.outline),
        colors = ButtonDefaults.buttonColors(
            containerColor = animatedColor,
            contentColor = MaterialTheme.colorScheme.onBackground
        )
    ) {}
}

@Composable
fun SmallCircle(color: Color) {
    Box(
        modifier = Modifier
            .clip(CircleShape)
            .size(20.dp)
            .background(color)
            .border(2.dp, MaterialTheme.colorScheme.outline, CircleShape)
    ) {
    }
}

@Composable
fun FeedbackCircles(colors: List<Color>) {

    val col0 by animateColorAsState(
        colors[0],
        animationSpec = tween(durationMillis = 100, delayMillis = 0),
        label = "col0"
    )
    val col1 by animateColorAsState(
        colors[1],
        animationSpec = tween(durationMillis = 100, delayMillis = 100),
        label = "col1"
    )
    val col2 by animateColorAsState(
        colors[2],
        animationSpec = tween(durationMillis = 100, delayMillis = 200),
        label = "col2"
    )
    val col3 by animateColorAsState(
        colors[3],
        animationSpec = tween(durationMillis = 100, delayMillis = 300),
        label = "col3",
    )
    Column {
        Row(
            modifier = Modifier.padding(bottom = 5.dp),
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            SmallCircle(color = col0)
            SmallCircle(color = col1)
        }
        Row(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
            SmallCircle(color = col2)
            SmallCircle(color = col3)
        }
    }
}

@Composable
fun SelectableColorsRow(
    colors: List<Color>,
    onClick: (button: Int) -> Unit,
    isEnabled: Boolean
) {
    val buttonColors = rememberSaveable(
        saver = listSaver(
            save = { list ->
                list.map { color ->
                    listOf(
                        color.red,
                        color.green,
                        color.blue,
                        color.alpha
                    )
                }
            },
            restore = { list ->
                list.map { rgba -> Color(rgba[0], rgba[1], rgba[2], rgba[3]) }.toMutableStateList()
            }
        )
    ) { mutableStateListOf(Color.White, Color.White, Color.White, Color.White) }

    val displayedColors = colors + List(4 - colors.size) { Color.White }

    Row(
        horizontalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        buttonColors.forEachIndexed { index, color ->
            CircularButton(
                onClick = {
                    if (isEnabled) {
                        buttonColors[index] = displayedColors.getOrElse(index) { Color.White }
                        onClick(index)
                    }
                },
                color = color
            )
        }
    }
}

@Composable
fun GameRow(
    selectedColors: List<Color>,
    feedbackColors: List<Color>,
    clickable: Boolean,
    isEnabled: Boolean,
    onSelectClickColor: (button: Int) -> Unit,
    onCheckClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        SelectableColorsRow(
            colors = selectedColors,
            onClick = onSelectClickColor,
            isEnabled = isEnabled
        )
        AnimatedVisibility(
            visible = clickable && isEnabled,
            enter = scaleIn(tween(500)),
            exit = scaleOut(tween(500))
        ) {
            IconButton(
                onClick = onCheckClick,
                modifier = Modifier
                    .clip(CircleShape)
                    .size(50.dp),
                colors = IconButtonDefaults.filledIconButtonColors(),
                enabled = clickable && isEnabled
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.background
                )
            }
        }
        FeedbackCircles(colors = feedbackColors)
    }
}

fun selectNextAvailableColor(
    availableColors: List<Color>,
    selectedColors: List<Color>,
    button: Int
): Color {
    val currentIndex = availableColors.indexOf(selectedColors[button])
    return availableColors[(currentIndex + 1) % availableColors.size]
}

fun selectRandomColors(availableColors: List<Color>): List<Color> {
    return availableColors.shuffled().take(4)
}

fun checkColors(
    selectedColors: List<Color>,
    trueColors: List<Color>,
    notFoundColor: Color
): List<Color> {
    val feedbackColors = mutableListOf<Color>()
    for (i in selectedColors.indices) {
        if (selectedColors[i] == trueColors[i]) {
            feedbackColors.add(Color.Red)
        } else if (trueColors.contains(selectedColors[i])) {
            feedbackColors.add(Color.Yellow)
        } else {
            feedbackColors.add(notFoundColor)
        }
    }
    return feedbackColors
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameScreenInitial(
    navController: NavController,
    viewModel: ProfileViewModel = hiltViewModel(),
    imageUri: Uri? = null
) {
    val allColors: List<Color> = listOf(
        Color.Gray,
        Color.Red,
        Color.Blue,
        Color.Cyan,
        Color.Black,
        Color.Green,
        Color.Magenta,
        Color.Yellow
    )
    val availableColors =
        rememberSaveable { allColors.shuffled().take(viewModel.guessColors.intValue) }

    val isGameActive = rememberSaveable { mutableStateOf(true) }
    val numberOfAttempts = rememberSaveable { mutableIntStateOf(1) }
    val colorsToGuess = rememberSaveable { mutableStateOf(selectRandomColors(availableColors)) }

    val selectedColors = rememberSaveable {
        MutableList(numberOfAttempts.intValue) {
            mutableStateListOf(
                availableColors[0],
                availableColors[0],
                availableColors[0],
                availableColors[0]
            )
        }
    }
    val feedbackColors = rememberSaveable {
        MutableList(numberOfAttempts.intValue) {
            mutableStateListOf(
                Color.White,
                Color.White,
                Color.White,
                Color.White
            )
        }
    }

    val confirmButton =
        rememberSaveable { MutableList(numberOfAttempts.intValue) { mutableStateOf(false) } }
    val isRowConfirmed =
        rememberSaveable { MutableList(numberOfAttempts.intValue) { mutableStateOf(false) } }
    val showVictoryGif = remember { mutableStateOf(false) }
    val points = remember { mutableStateOf(0) }

    Box(modifier = Modifier.fillMaxSize()) {
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
                        text = "Attempts: ${numberOfAttempts.intValue}",
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
                        Text("Quit")
                    }
                }
            }
        ) { innerPadding ->
            Box(modifier = Modifier.padding(innerPadding)) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    items((numberOfAttempts.intValue), key = { it }) { rowNumber ->
                        var rowVisible by remember { mutableStateOf(false) }
                        LaunchedEffect(Unit) { rowVisible = true }
                        AnimatedVisibility(
                            visible = rowVisible,
                            enter = expandVertically(tween(250), expandFrom = Alignment.Top)
                        ) {
                            GameRow(
                                selectedColors = selectedColors[rowNumber],
                                feedbackColors = feedbackColors[rowNumber],
                                clickable = confirmButton[rowNumber].value,
                                isEnabled = !isRowConfirmed[rowNumber].value,
                                onSelectClickColor = { buttonNumber ->
                                    selectedColors[rowNumber][buttonNumber] =
                                        selectNextAvailableColor(
                                            availableColors,
                                            selectedColors[rowNumber],
                                            buttonNumber
                                        )
                                    if (selectedColors[rowNumber].groupingBy { it }.eachCount()
                                            .filter { it.value > 1 }.isEmpty()
                                    )
                                        confirmButton[rowNumber].value = true
                                },
                                onCheckClick = {
                                    feedbackColors[rowNumber].clear()
                                    feedbackColors[rowNumber].addAll(
                                        checkColors(
                                            selectedColors[rowNumber],
                                            colorsToGuess.value,
                                            Color.White
                                        )
                                    )

                                    if (feedbackColors[rowNumber].all { it == Color.Red }) {
                                        isGameActive.value = false

                                        points.value = calculatePoints(
                                            attempts = numberOfAttempts.value,
                                            totalColors = colorsToGuess.value.size
                                        )
                                        viewModel.incrementUserWins(points.value)
                                        showVictoryGif.value = true
                                    } else {
                                        numberOfAttempts.intValue = numberOfAttempts.intValue + 1
                                    }

                                    confirmButton[rowNumber].value = false
                                    isRowConfirmed[rowNumber].value = true

                                    selectedColors += mutableStateListOf(
                                        availableColors[0],
                                        availableColors[0],
                                        availableColors[0],
                                        availableColors[0]
                                    )
                                    feedbackColors += mutableStateListOf(
                                        Color.White,
                                        Color.White,
                                        Color.White,
                                        Color.White
                                    )
                                    confirmButton += mutableStateOf(false)
                                    isRowConfirmed += mutableStateOf(false)
                                }
                            )
                        }
                    }
                }
            }
        }

        VictoryGifOverlay(
            showGif = showVictoryGif.value,
            onGifEnd = {
                showVictoryGif.value = false; navController.navigate(Screen.Profile.route)
            },
            points = points.value
        )
    }
}

fun calculatePoints(attempts: Int, totalColors: Int): Int {
    val basePoints = 1000
    val difficultyMultiplier = totalColors * 2

    val points = (basePoints - (attempts * 50)) * difficultyMultiplier

    return maxOf(points, 0)
}

@Composable
fun VictoryGifOverlay(showGif: Boolean, onGifEnd: () -> Unit, points: Int) {
    if (showGif) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Image(
                modifier = Modifier.fillMaxSize(),
                painter = rememberDrawablePainter(
                    drawable = getDrawable(
                        LocalContext.current,
                        R.drawable.confetti
                    )
                ),
                contentDescription = "You won",
                contentScale = ContentScale.Crop,
            )
            Text(
                text = "YOU WON\n",
                style = TextStyle(
                    fontSize = 50.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                ),
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .align(Alignment.Center)

            )
            Text(
                text = "Points: $points",
                style = TextStyle(
                    fontSize = 30.sp,
                    color = Color.Black
                ),
                modifier = Modifier
                    .padding(bottom = 10.dp)
                    .padding(top = 10.dp)
                    .align(Alignment.Center)

            )
            Image(
                modifier = Modifier
                    .align(Alignment.BottomCenter),
                painter = rememberDrawablePainter(
                    drawable = getDrawable(
                        LocalContext.current,
                        R.drawable.cat_boom
                    )
                ),
                contentDescription = "You won",
                contentScale = ContentScale.FillWidth,
            )
        }

        LaunchedEffect(Unit) {
            delay(3000)
            onGifEnd()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun Prev() {
    val context = LocalContext.current
    val userDatabase = UserDatabase.getDatabase(context)
    val userDao = userDatabase.userDao()
    val offlineUserRepository = OfflineUserRepository(userDao)
    val viewModel = ProfileViewModel(offlineUserRepository)

    MastermindAndroidTheme {
        GameScreenInitial(navController = NavController(context), viewModel = viewModel)
    }
}

