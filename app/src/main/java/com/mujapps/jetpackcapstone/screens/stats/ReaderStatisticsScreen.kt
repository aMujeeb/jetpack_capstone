package com.mujapps.jetpackcapstone.screens.stats

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.sharp.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import com.mujapps.jetpackcapstone.components.BookImage
import com.mujapps.jetpackcapstone.components.ReaderAppBar
import com.mujapps.jetpackcapstone.model.BookItem
import com.mujapps.jetpackcapstone.model.MBook
import com.mujapps.jetpackcapstone.navigation.ReaderScreens
import com.mujapps.jetpackcapstone.screens.home.HomeScreenViewModel
import com.mujapps.jetpackcapstone.screens.search.BookRow
import java.util.*

@Composable
fun ReaderStatisticsScreen(
    navController: NavHostController,
    viewModel: HomeScreenViewModel = hiltViewModel()
) {

    var books: List<MBook>
    val currentUser = FirebaseAuth.getInstance().currentUser

    Scaffold(topBar = {
        ReaderAppBar(
            tittle = "Book Stats",
            icon = Icons.Default.ArrowBack,
            showProfile = false,
            navController = navController
        ) {
            navController.popBackStack()
        }
    }) {
        it.calculateBottomPadding()
        //Only want to show books have already read
        books = if (!viewModel.retrievedBooksData.value.data.isNullOrEmpty()) {
            viewModel.retrievedBooksData.value.data!!.filter { mBook ->
                mBook.userId == currentUser?.uid
            }
        } else {
            emptyList()
        }

        Column {
            Row {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .padding(2.dp)
                ) {
                    Icon(imageVector = Icons.Sharp.Person, contentDescription = "Person")
                }
                Text(
                    text = "Hi, ${
                        currentUser?.email.toString().split("@")[0].uppercase(Locale.getDefault())
                    }"
                )
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                shape = CircleShape,
                elevation = 8.dp
            ) {
                val readBookList: List<MBook> =
                    if (!viewModel.retrievedBooksData.value.data.isNullOrEmpty()) {
                        books.filter { mBook ->
                            (mBook.userId == currentUser?.uid) && (mBook.finishedReading != null)
                        }
                    } else {
                        emptyList()
                    }

                val readingBookList: List<MBook> =
                    if (!viewModel.retrievedBooksData.value.data.isNullOrEmpty()) {
                        books.filter { mBook ->
                            (mBook.startedReading != null) && (mBook.finishedReading == null)
                        }
                    } else {
                        emptyList()
                    }

                Column(
                    modifier = Modifier.padding(start = 8.dp, top = 8.dp, bottom = 4.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(text = "Your Stats", style = MaterialTheme.typography.h4)
                    Divider()
                    Text(text = "You are reading : ${readingBookList.size}")
                    Text(text = "You have read : ${readBookList.size}")
                }
            }

            if (viewModel.retrievedBooksData.value.loading == true) {
                LinearProgressIndicator()
            } else {
                Divider()
                LazyColumn(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(), contentPadding = PaddingValues(16.dp)
                ) {
                    //Filter books
                    val readBookList: List<MBook> =
                        if (!viewModel.retrievedBooksData.value.data.isNullOrEmpty()) {
                            books.filter { mBook ->
                                (mBook.userId == currentUser?.uid) && (mBook.finishedReading != null)
                            }
                        } else {
                            emptyList()
                        }
                    items(items = readBookList) { book ->
                        BookStatsRow(book = book)
                    }
                }
            }
        }
    }
}

@Composable
fun BookStatsRow(book: MBook) {
    Card(
        modifier = Modifier
            .clickable {
                //Click Function Of the the row
            }
            .fillMaxWidth()
            .height(120.dp)
            .padding(4.dp),
        shape = RectangleShape,
        elevation = 8.dp
    ) {
        Row(modifier = Modifier.padding(4.dp), verticalAlignment = Alignment.Top) {
            val imageUrl: String = book.photoUrl ?: ""
            BookImage(imageUrl)

            Column() {

                Row(horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(text = book.title ?: "", overflow = TextOverflow.Ellipsis, softWrap = true)
                    if (book.rating!! >= 4) {
                        Spacer(modifier = Modifier.fillMaxWidth(0.8f))
                        Icon(
                            imageVector = Icons.Default.ThumbUp,
                            contentDescription = "Thumbs Up",
                            tint = Color.Green.copy(alpha = 0.5f)
                        )
                    }
                }

                Text(
                    text = "Authors :${book.authors}",
                    overflow = TextOverflow.Clip,
                    fontStyle = FontStyle.Italic,
                    style = MaterialTheme.typography.caption
                )
                Text(
                    text = "Date :${book.publishedDate}",
                    overflow = TextOverflow.Clip,
                    fontStyle = FontStyle.Italic,
                    style = MaterialTheme.typography.caption
                )
                Text(
                    text = "[${book.categories}]",
                    overflow = TextOverflow.Clip,
                    fontStyle = FontStyle.Italic,
                    style = MaterialTheme.typography.caption
                )
            }
        }
    }
}