package com.mujapps.jetpackcapstone.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.google.firebase.auth.FirebaseAuth
import com.mujapps.jetpackcapstone.components.FabContent
import com.mujapps.jetpackcapstone.components.ReaderAppBar
import com.mujapps.jetpackcapstone.components.TitleSection
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
    }
}

@Composable
fun ListCard(
    book: MBook = MBook("asdf", "Runn Me", "Me And You", "Hello World"),
    onPressBook: (String) -> Unit = {}
) {
    val context = LocalContext.current
    val resources = context.resources
    val displayMetrics = resources.displayMetrics
    val screenWidth = displayMetrics.widthPixels / displayMetrics.density
    val spacing = 8.dp
    Card(
        shape = RoundedCornerShape(24.dp),
        backgroundColor = Color.White,
        elevation = 8.dp,
        modifier = Modifier
            .padding(16.dp)
            .height(220.dp)
            .clickable { onPressBook.invoke(book.title.toString()) }
    ) {
        Column(
            modifier = Modifier.width(width = screenWidth.dp - (spacing * 2)),
            horizontalAlignment = Alignment.Start
        ) {
            Row(horizontalArrangement = Arrangement.Center) {
                Image(
                    painter = rememberImagePainter(data = ""),
                    contentDescription = "Book Image",
                    modifier = Modifier
                        .height(132.dp)
                        .width(104.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column() {

                }
            }
        }
    }
}

@Composable
fun ReadingRightNowArea(books: List<MBook>, navController: NavController) {

}