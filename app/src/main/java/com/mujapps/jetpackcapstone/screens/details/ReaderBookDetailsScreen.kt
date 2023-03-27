package com.mujapps.jetpackcapstone.screens.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.mujapps.jetpackcapstone.components.ReaderAppBar
import com.mujapps.jetpackcapstone.navigation.ReaderScreens

@Composable
fun BookDetailsScreen(navController: NavHostController, bookId: String) {
    Scaffold(topBar = {
        ReaderAppBar(
            tittle = "Book Details",
            navController = navController,
            icon = Icons.Default.ArrowBack
        ) {
            navController.navigate(ReaderScreens.SearchScreen.name)
        }
    }) {
        it.calculateBottomPadding()
        Surface(
            modifier = Modifier
                .padding(4.dp)
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier.padding(top = 12.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Book Description : $bookId")
            }
        }
    }
}