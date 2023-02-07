package com.mujapps.jetpackcapstone.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Logout
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.actionCodeSettings
import com.mujapps.jetpackcapstone.model.MBook
import com.mujapps.jetpackcapstone.navigation.ReaderScreens

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
            //Home Content
            HomeContent(navController)
        }
    }
}

@Composable
fun HomeContent(navController: NavHostController) {
    Column(Modifier.padding(4.dp), verticalArrangement = Arrangement.SpaceEvenly) {
        Row(modifier = Modifier.align(alignment = Alignment.Start)) {
            TitleSection(label = "Your Reading \n" + " activity right now")
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
            Spacer(modifier = Modifier.width(152.dp))

        }
    }, actions = {
        IconButton(onClick = {
            FirebaseAuth.getInstance().signOut().run {
                navController.navigate(ReaderScreens.LoginScreen.name)
            }
        }) {
            Icon(
                imageVector = Icons.Filled.Logout,
                contentDescription = "Log Out",
                tint = Color.Green.copy(alpha = 0.4f)
            )
        }
    }, backgroundColor = Color.Transparent, elevation = 0.dp)
}

@Composable
fun TitleSection(modifier: Modifier = Modifier, label: String) {
    Surface(modifier = modifier.padding(start = 8.dp, top = 2.dp)) {
        Text(
            text = label,
            fontSize = 18.sp,
            fontStyle = FontStyle.Normal,
            textAlign = TextAlign.Left
        )
    }
}

@Composable
fun ReadingRightNowArea(books: List<MBook>, navController: NavController) {

}