package com.mujapps.jetpackcapstone.screens.details

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.text.HtmlCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.mujapps.jetpackcapstone.components.ReaderAppBar
import com.mujapps.jetpackcapstone.components.RoundedButton
import com.mujapps.jetpackcapstone.data.Resource
import com.mujapps.jetpackcapstone.model.BookItem
import com.mujapps.jetpackcapstone.model.MBook
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
        style = MaterialTheme.typography.subtitle1,
        overflow = TextOverflow.Ellipsis,
        maxLines = 8
    )
    Text(
        text = "Published : ${bookData?.publishedDate.toString()}",
        style = MaterialTheme.typography.subtitle1
    )
    Spacer(modifier = Modifier.height(16.dp))

    //Cleaning unwanted characters of description
    val desc = HtmlCompat.fromHtml(bookData?.description ?: "", HtmlCompat.FROM_HTML_MODE_LEGACY)

    //Getting relative height by available space
    val localDims = LocalContext.current.resources.displayMetrics

    Surface(
        modifier = Modifier
            .height(localDims.heightPixels.dp.times(0.09f))
            .padding(4.dp), shape = RectangleShape, border = BorderStroke(1.dp, Color.DarkGray)
    ) {
        LazyColumn(modifier = Modifier.padding(4.dp)) {
            item {
                Text(text = desc.toString())
            }
        }
    }

    //Buttons
    Row(modifier = Modifier.padding(top = 8.dp), horizontalArrangement = Arrangement.SpaceAround) {
        RoundedButton(label = "Save") {
            //Save data in Firebase
            val mBook = MBook(
                title = bookData?.title ?: "",
                authors = (bookData?.authors ?: "").toString(),
                description = bookData?.description,
                categories = (bookData?.categories ?: "").toString(),
                notes = "",
                photoUrl = bookData?.imageLinks?.thumbnail,
                publishedDate = bookData?.publishedDate,
                pageCount = bookData?.pageCount.toString(),
                rating = 0.0,
                googleBookId = googleBookId,
                userId = FirebaseAuth.getInstance().currentUser?.uid.toString()
            )
            saveToFireBase(mBook, navController)
        }

        Spacer(modifier = Modifier.width(24.dp))

        RoundedButton(label = "Cancel") {
            navController.popBackStack()
        }
    }
}

fun saveToFireBase(book: MBook, navController: NavController) {
    val db = FirebaseFirestore.getInstance()
    val dbCollection = db.collection("books")
    if (book.toString().isNotEmpty()) {
        dbCollection.add(book).addOnSuccessListener { documentReference ->
            val docId =
                documentReference.id //Getting the saved ID- This generated by FireStore while saving. Auto generated ID
            dbCollection.document(docId).update(hashMapOf("id" to docId) as Map<String, Any>)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) navController.popBackStack()
                }.addOnFailureListener {
                    Log.d("TAG", "Error Updating Doc")
                }
        }
    }
}