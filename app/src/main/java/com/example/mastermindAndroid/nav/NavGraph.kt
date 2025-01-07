package com.example.mastermindAndroid.nav

import android.net.Uri
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.mastermindAndroid.activities.GameScreenInitial
import com.example.mastermindAndroid.activities.LoginScreenInitial
import com.example.mastermindAndroid.activities.ProfileScreenInitial
import com.example.mastermindAndroid.activities.ScoreScreenInitial
import com.example.mastermindAndroid.data.ProfileViewModel

@Composable
fun SetupNavGraph(navController: NavHostController) {
    val viewModel: ProfileViewModel = viewModel(factory = com.example.mastermindAndroid.AppViewModelProvider.Factory)
    NavHost(
        navController = navController,
        startDestination = Screen.Login.route,
        enterTransition = {
            fadeIn(
                tween(durationMillis = 300, easing = EaseIn)
            ) + slideIntoContainer(
                animationSpec = tween(durationMillis = 300, easing = EaseIn),
                towards = AnimatedContentTransitionScope.SlideDirection.Start
            )
        },
        exitTransition = {
            fadeOut(
                tween(durationMillis = 300, easing = EaseOut)
            ) + slideOutOfContainer(
                animationSpec = tween(durationMillis = 300, easing = EaseOut),
                towards = AnimatedContentTransitionScope.SlideDirection.End
            )
        }) {

        composable(
            route = Screen.Login.route
        ) {
            LoginScreenInitial(navController = navController, viewModel = viewModel)
        }

        composable(
            route = Screen.Profile.route + "?imageUri={imageUri}",
            arguments = listOf(navArgument("imageUri") { nullable = true; type = NavType.StringType })
        ) { backStackEntry ->
            val imageUri = backStackEntry.arguments?.getString("imageUri")?.let { Uri.parse(it) }
            ProfileScreenInitial(navController = navController, viewModel = viewModel, imageUri = imageUri)
        }

        composable(
            route = Screen.Game.route + "?imageUri={imageUri}",
            arguments = listOf(navArgument("imageUri") { nullable = true; type = NavType.StringType })
        ) { backStackEntry ->
            val imageUri = backStackEntry.arguments?.getString("imageUri")?.let { Uri.parse(it) }
            GameScreenInitial(navController = navController, viewModel = viewModel, imageUri = imageUri)
        }

        composable(
            route = Screen.Score.route + "?imageUri={imageUri}",
            arguments = listOf(navArgument("imageUri") { nullable = true; type = NavType.StringType })
        ) { backStackEntry ->
            val imageUri = backStackEntry.arguments?.getString("imageUri")?.let { Uri.parse(it) }
            ScoreScreenInitial(navController = navController, viewModel = viewModel, imageUri = imageUri)
        }
    }
}
