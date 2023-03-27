package com.mujapps.jetpackcapstone.screens.search

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.mujapps.jetpackcapstone.components.BookImage
import com.mujapps.jetpackcapstone.components.InputField
import com.mujapps.jetpackcapstone.components.ReaderAppBar
import com.mujapps.jetpackcapstone.model.BookItem
import com.mujapps.jetpackcapstone.navigation.ReaderScreens

@Composable
fun BookSearchScreen(
    navController: NavHostController,
    searchViewModel: BooksSearchViewModel = hiltViewModel()
) {
    Scaffold(topBar = {
        ReaderAppBar(
            tittle = "Search Books",
            icon = Icons.Default.ArrowBack,
            navController = navController,
            showProfile = false
        ) {
            navController.navigate(ReaderScreens.ReaderHomeScreen.name)
        }
    }) {
        it.calculateBottomPadding()
        Surface() {
            Column {
                SearchForm(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    viewModel = searchViewModel
                ) { query ->
                    Log.d("TAG", query)
                    searchViewModel.searchBooks(query)
                }

                Spacer(modifier = Modifier.height(12.dp))

                BookList(navController, searchViewModel)
            }
        }
    }
}

@Composable
fun BookList(
    navController: NavHostController,
    searchViewModel: BooksSearchViewModel = hiltViewModel()
) {

    val mBooks = searchViewModel.mBooksList
    if (searchViewModel.mIsLoading) {
        LinearProgressIndicator()
    } else {
        LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(16.dp)) {
            items(items = mBooks ?: emptyList()) { book ->
                BookRow(book, navController)
            }
        }
    }
}

@Composable
fun BookRow(book: BookItem, navController: NavController) {
    Card(
        modifier = Modifier
            .clickable {
                //Click Function Of the the row
                navController.navigate(ReaderScreens.DetailScreen.name + "/${book.id}")
            }
            .fillMaxWidth()
            .height(120.dp)
            .padding(4.dp),
        shape = RectangleShape,
        elevation = 8.dp
    ) {
        Row(modifier = Modifier.padding(4.dp), verticalAlignment = Alignment.Top) {
            val imageUrl: String = book.volumeInfo.imageLinks.smallThumbnail
            BookImage(imageUrl)

            Column() {
                Text(text = book.volumeInfo.title, overflow = TextOverflow.Ellipsis)
                Text(
                    text = "Authors :${book.volumeInfo.authors}",
                    overflow = TextOverflow.Clip,
                    fontStyle = FontStyle.Italic,
                    style = MaterialTheme.typography.caption
                )
                Text(
                    text = "Date :${book.volumeInfo.publishedDate}",
                    overflow = TextOverflow.Clip,
                    fontStyle = FontStyle.Italic,
                    style = MaterialTheme.typography.caption
                )
                Text(
                    text = "[${book.volumeInfo.categories}]",
                    overflow = TextOverflow.Clip,
                    fontStyle = FontStyle.Italic,
                    style = MaterialTheme.typography.caption
                )
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SearchForm(
    modifier: Modifier = Modifier, loading: Boolean = false,
    viewModel: BooksSearchViewModel,
    hint: String = "Search", onSearch: (String) -> Unit = {}
) {
    Column() {
        val searchQueryState = rememberSaveable {
            mutableStateOf("")
        }
        val keyboardController = LocalSoftwareKeyboardController.current
        val valid = remember(searchQueryState.value) {
            searchQueryState.value.trim().isNotEmpty()
        }

        InputField(
            valueState = searchQueryState,
            labelId = "Search",
            isEnabled = true,
            onAction = KeyboardActions {
                if (!valid) return@KeyboardActions
                onSearch(searchQueryState.value.trim())
                searchQueryState.value = ""
                keyboardController?.hide()
            })
    }
}