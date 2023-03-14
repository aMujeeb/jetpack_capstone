package com.mujapps.jetpackcapstone.screens.search

import android.util.Log
import androidx.compose.foundation.Image
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.mujapps.jetpackcapstone.components.InputField
import com.mujapps.jetpackcapstone.components.ReaderAppBar
import com.mujapps.jetpackcapstone.model.MBook
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
fun BookList(navController: NavHostController, searchViewModel: BooksSearchViewModel) {

    Log.d("TAG", "xxx "+searchViewModel.listOfBooks.value.data.toString())

    if(searchViewModel.listOfBooks.value.loading == true){
        Log.d("TAG", "LOADING")
        CircularProgressIndicator()
    } else {
        Log.d("TAG", "xxx "+searchViewModel.listOfBooks.value.data.toString())
    }
    val mBooks = listOf(
        MBook(id = "b1", title = "Hello Again 1", authors = "All of Us 1", notes = null),
        MBook(id = "b2", title = "Hello Again 2", authors = "All of Us 2", notes = null),
        MBook(id = "b3", title = "Hello Again 3", authors = "All of Us 3", notes = null),
        MBook(id = "b4", title = "Hello Again 4", authors = "All of Us 4", notes = null),
        MBook(id = "b5", title = "Hello Again 5", authors = "All of Us 5", notes = null),
        MBook(id = "b6", title = "Hello Again 6", authors = "All of Us 6", notes = null)
    )

    LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(16.dp)) {
        items(items = mBooks) { book ->

        }
    }
}

@Composable
fun BookRow(book: MBook, navController: NavController) {
    Card(
        modifier = Modifier
            .clickable { }
            .fillMaxWidth()
            .height(100.dp)
            .padding(4.dp),
        shape = RectangleShape,
        elevation = 8.dp
    ) {
        Row(modifier = Modifier.padding(4.dp), verticalAlignment = Alignment.Top) {
            //Coil new version uses different implementation
            Image(
                painter = rememberImagePainter(data = ""),
                contentDescription = "Book Image",
                modifier = Modifier
                    .width(80.dp)
                    .fillMaxHeight()
                    .padding(4.dp)
            )

            Column() {
                Text(text = book.title.toString(), overflow = TextOverflow.Ellipsis)
                Text(
                    text = "Authors :${book.authors}",
                    overflow = TextOverflow.Clip,
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