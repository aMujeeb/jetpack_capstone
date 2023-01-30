package com.mujapps.jetpackcapstone.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.google.firebase.auth.ktx.actionCodeSettings

@Composable
fun HomeScreen(navController: NavHostController) {
    Scaffold(topBar = {
        ReaderAppBar(tittle = "A. Reader", navController = navController)
    },
        floatingActionButton = {
            FabContent(onTap = {

            })
        }) {
        it.calculateBottomPadding()
        //Content
        Surface(modifier = Modifier.fillMaxSize()) {

        }
    }
}

@Composable
fun FabContent(onTap: () -> Unit) {
    FloatingActionButton(onClick = {
        onTap()
    }, shape = RoundedCornerShape(48.dp), backgroundColor = Color(0xFF92CBDF)) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "Add a Book",
            tint = MaterialTheme.colors.onSecondary
        )
    }
}


@Composable
fun ReaderAppBar(tittle: String, showProfile: Boolean = true, navController: NavController) {
    TopAppBar(title = {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (showProfile) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "Logo Icon",
                    modifier = Modifier
                        .clip(
                            RoundedCornerShape(16.dp)
                        )
                        .scale(0.6f)
                )
            }
            Text(
                text = tittle,
                color = Color.Red.copy(alpha = 0.7f),
                style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 14.sp)
            )
        }
    }, actions = {}, backgroundColor = Color.Transparent, elevation = 0.dp)
}