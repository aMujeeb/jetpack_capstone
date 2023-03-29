package com.mujapps.jetpackcapstone.screens.details

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.mujapps.jetpackcapstone.components.ReaderAppBar
import com.mujapps.jetpackcapstone.data.Resource
import com.mujapps.jetpackcapstone.model.BookItem
import com.mujapps.jetpackcapstone.navigation.ReaderScreens

@Composable
fun BookDetailsScreen(
    navController: NavHostController,
    bookId: String,
    mViewModel: ReaderBookDetailsViewModel = hiltViewModel()
) {
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

                val bookInfo = produceState<Resource<BookItem>>(initialValue = Resource.Loading()) {
                    value = mViewModel.getBookInformation(bookId)
                }.value
                if (bookInfo.data == null) {
                    LinearProgressIndicator()
                    Text(text = "Loading..")
                } else {
                    //Text(text = "Book : ${bookInfo.data?.volumeInfo?.title}")
                    ShowBookDetails(bookInfo, navController)
                }
            }
        }
    }
}

@Composable
fun ShowBookDetails(bookInfo: Resource<BookItem>, navController: NavController) {
    val bookData = bookInfo.data?.volumeInfo
    val googleBookId = bookInfo.data?.id

    Card(modifier = Modifier.padding(32.dp), shape = CircleShape, elevation = 4.dp) {
        Image(
            painter = rememberImagePainter(data = bookData!!.imageLinks.thumbnail),
            contentDescription = "Book Image",
            modifier = Modifier
                .width(80.dp)
                .height(80.dp)
                .padding(2.dp)
        )
    }

    Text(
        text = bookData?.title ?: "",
        style = MaterialTheme.typography.h5,
        overflow = TextOverflow.Ellipsis,
        maxLines = 18
    )
    Text(text = "Authors : ${bookData?.authors.toString()}")
    Text(text = "Page Count : ${bookData?.pageCount.toString()}")
    Text(
        text = "Categories : ${bookData?.categories.toString()}",
        style = MaterialTheme.typography.subtitle1
    )
    Text(
        text = "Published : ${bookData?.publishedDate.toString()}",
        style = MaterialTheme.typography.subtitle1
    )
    Spacer(modifier = Modifier.height(16.dp))

}