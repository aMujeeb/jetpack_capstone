package com.mujapps.jetpackcapstone.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mujapps.jetpackcapstone.screens.create.CreateAccountScreen
import com.mujapps.jetpackcapstone.screens.details.BookDetailsScreen
import com.mujapps.jetpackcapstone.screens.home.HomeScreen
import com.mujapps.jetpackcapstone.screens.login.LoginScreen
import com.mujapps.jetpackcapstone.screens.search.BookSearchScreen
import com.mujapps.jetpackcapstone.screens.search.BooksSearchViewModel
import com.mujapps.jetpackcapstone.screens.splash.ReaderSplashScreen
import com.mujapps.jetpackcapstone.screens.stats.ReaderStatisticsScreen
import com.mujapps.jetpackcapstone.screens.update.BookUpdateScreen

@Composable
fun ReaderNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = ReaderScreens.SplashScreen.name) {
        composable(ReaderScreens.SplashScreen.name) {
            ReaderSplashScreen(navController = navController)
        }

        composable(ReaderScreens.ReaderHomeScreen.name) {
            HomeScreen(navController = navController)
        }

        composable(ReaderScreens.LoginScreen.name) {
            LoginScreen(navController = navController)
        }

        composable(ReaderScreens.CreateAccountScreen.name) {
            CreateAccountScreen(navController = navController)
        }

        composable(ReaderScreens.SearchScreen.name) {
            val mSearchViewModel = hiltViewModel<BooksSearchViewModel>()
            BookSearchScreen(navController = navController, searchViewModel = mSearchViewModel)
        }

        composable(ReaderScreens.DetailScreen.name) {
            BookDetailsScreen(navController = navController)
        }

        composable(ReaderScreens.UpdateScreen.name) {
            BookUpdateScreen(navController = navController)
        }

        composable(ReaderScreens.ReaderStatsScreen.name) {
            ReaderStatisticsScreen(navController = navController)
        }
    }
}