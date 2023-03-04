package com.mujapps.jetpackcapstone.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material.icons.rounded.StarBorder
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.google.firebase.auth.FirebaseAuth
import com.mujapps.jetpackcapstone.components.*
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
    val mBooks = listOf(
        MBook(id = "b1", title = "Hello Again 1", authors = "All of Us 1", notes = null),
        MBook(id = "b2", title = "Hello Again 2", authors = "All of Us 2", notes = null),
        MBook(id = "b3", title = "Hello Again 3", authors = "All of Us 3", notes = null),
        MBook(id = "b4", title = "Hello Again 4", authors = "All of Us 4", notes = null),
        MBook(id = "b5", title = "Hello Again 5", authors = "All of Us 5", notes = null),
        MBook(id = "b6", title = "Hello Again 6", authors = "All of Us 6", notes = null)
    )

    val currentUserEmail = FirebaseAuth.getInstance().currentUser?.email
    val currentUserName =
        if (!FirebaseAuth.getInstance().currentUser?.email.isNullOrEmpty()) FirebaseAuth.getInstance().currentUser?.email?.split(
            "@"
        )?.get(0) else "N/A"
    Column(Modifier.padding(4.dp), verticalArrangement = Arrangement.Top) {
        Row(modifier = Modifier.align(alignment = Alignment.Start)) {
            TitleSection(label = "Your Reading \n" + " activity right now")

            Spacer(modifier = Modifier.fillMaxWidth(0.7f))

            Column {
                Icon(imageVector = Icons.Filled.AccountCircle,
                    contentDescription = "Profile",
                    modifier = Modifier
                        .clickable {
                            navController.navigate(ReaderScreens.ReaderStatsScreen.name)
                        }
                        .size(40.dp),
                    tint = MaterialTheme.colors.secondaryVariant)

                Text(
                    text = currentUserName ?: "N/A",
                    modifier = Modifier.padding(2.dp),
                    style = MaterialTheme.typography.overline,
                    color = Color.Red,
                    fontSize = 12.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Clip
                )
                Divider()
            }
        }
        ReadingRightNowArea(books = listOf(), navController = navController)

        TitleSection(label = "Reading List")

        BookListArea(listOfBooks = mBooks, navController = navController)
    }
}

@Composable
fun ReadingRightNowArea(books: List<MBook>, navController: NavController) {
    ListCard()
}

@Composable
fun BookListArea(listOfBooks: List<MBook>, navController: NavHostController) {
    HorizonTalScrollableComponent(listOfBooks) {
        //To do OnClick card -> Navigate to details
    }
}

@Composable
fun HorizonTalScrollableComponent(listOfBooks: List<MBook>, onCardPressed: (String) -> Unit) {
    val scrollState = rememberScrollState()
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(280.dp)
            .horizontalScroll(scrollState)
    ) {
        for (book in listOfBooks) {
            ListCard(book) {
                onCardPressed(it)
            }
        }
    }
}
