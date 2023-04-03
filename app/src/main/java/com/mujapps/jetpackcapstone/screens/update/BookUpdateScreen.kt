package com.mujapps.jetpackcapstone.screens.update

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.mujapps.jetpackcapstone.components.InputField
import com.mujapps.jetpackcapstone.components.RatingBar
import com.mujapps.jetpackcapstone.components.ReaderAppBar
import com.mujapps.jetpackcapstone.components.RoundedButton
import com.mujapps.jetpackcapstone.data.DataOrException
import com.mujapps.jetpackcapstone.model.MBook
import com.mujapps.jetpackcapstone.screens.home.HomeScreenViewModel

@Composable
fun BookUpdateScreen(
    navController: NavHostController,
    mBookId: String,
    viewModel: HomeScreenViewModel = hiltViewModel()
) {
    Scaffold(topBar = {
        ReaderAppBar(
            tittle = "Update Book",
            navController = navController,
            showProfile = false,
            icon = Icons.Default.ArrowBack
        ) {
            navController.popBackStack()
        }
    }) {
        it.calculateBottomPadding()

        val bookInfo = produceState<DataOrException<List<MBook>, Boolean, Exception>>(
            initialValue = DataOrException(
                data = emptyList(),
                true,
                Exception("")
            )
        ) {
            value = viewModel.retrievedBooksData.value
        }.value

        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(4.dp)
        ) {
            Column(
                modifier = Modifier.padding(top = 4.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                if (bookInfo.loading == true) {
                    LinearProgressIndicator()
                    bookInfo.loading = false
                } else {
                    //Text(text = viewModel.retrievedBooksData.value.data?.get(0)?.title.toString())
                    Surface(
                        modifier = Modifier
                            .padding(4.dp)
                            .fillMaxWidth(), shape = CircleShape, elevation = 4.dp
                    ) {
                        ShowBookUpdate(
                            bookInfo = viewModel.retrievedBooksData.value,
                            bookId = mBookId
                        )
                    }

                    ShowSimpleForm(book = viewModel.retrievedBooksData.value.data?.first { book ->
                        book.googleBookId == mBookId
                    }, navController)
                }
            }
        }
    }
}

@Composable
fun ShowBookUpdate(bookInfo: DataOrException<List<MBook>, Boolean, Exception>, bookId: String) {
    Row() {
        Spacer(modifier = Modifier.width(48.dp))
        if (bookInfo.data != null) {
            Column(modifier = Modifier.padding(4.dp), verticalArrangement = Arrangement.Center) {
                CardListItem(book = bookInfo.data!!.first {
                    it.googleBookId == bookId
                }, openPressDetails = {})
            }
        }
    }
}

@Composable
fun CardListItem(book: MBook, openPressDetails: () -> Unit) {
    Card(
        modifier = Modifier
            .padding(start = 4.dp, end = 4.dp, top = 4.dp, bottom = 8.dp)
            .clip(
                RoundedCornerShape(24.dp)
            )
            .clickable { }, elevation = 8.dp
    ) {
        Row(horizontalArrangement = Arrangement.Start) {
            Image(
                painter = rememberImagePainter(data = book.photoUrl.toString()),
                contentDescription = "Book Thumbnail",
                modifier = Modifier
                    .height(100.dp)
                    .width(120.dp)
                    .clip(
                        RoundedCornerShape(
                            topStart = 120.dp,
                            topEnd = 20.dp,
                            bottomEnd = 0.dp,
                            bottomStart = 0.dp
                        )
                    )
            )
            Column() {
                Text(
                    text = book.title.toString(),
                    style = MaterialTheme.typography.h6,
                    modifier = Modifier
                        .padding(start = 8.dp, end = 8.dp)
                        .width(120.dp), fontWeight = FontWeight.SemiBold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = book.authors.toString(),
                    style = MaterialTheme.typography.body2,
                    modifier = Modifier
                        .padding(start = 8.dp, end = 8.dp, top = 4.dp)
                        .width(120.dp), fontWeight = FontWeight.SemiBold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = book.publishedDate.toString(),
                    style = MaterialTheme.typography.body2,
                    modifier = Modifier
                        .padding(start = 8.dp, end = 8.dp, bottom = 8.dp)
                        .width(120.dp), fontWeight = FontWeight.SemiBold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ShowSimpleForm(book: MBook?, navController: NavHostController) {
    Spacer(modifier = Modifier.width(16.dp))
    val notesText = remember {
        mutableStateOf("")
    }

    val isStartedReading = remember {
        mutableStateOf(false)
    }

    val isFinishedReading = remember {
        mutableStateOf(false)
    }

    val ratingVal = remember {
        mutableStateOf(0)
    }

    SimpleForm(
        defaultValue = book?.notes.toString().ifEmpty { "No thoughts Available.." }) { note ->
        notesText.value = note
    }

    Row(
        modifier = Modifier.padding(4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        TextButton(
            onClick = { isStartedReading.value = true },
            enabled = book?.startedReading == null
        ) {
            if (book?.startedReading == null) {
                if (!isStartedReading.value) {
                    Text(text = "Start Reading")
                } else {
                    Text(
                        text = "Started Reading",
                        modifier = Modifier.alpha(0.6f),
                        color = Color.Red.copy(alpha = 0.5f)
                    )
                }
            } else {
                Text(text = "Started on :${book.startedReading}") //To do format Date
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        TextButton(
            onClick = { isFinishedReading.value = true },
            enabled = book?.finishedReading == null
        ) {
            if (book?.finishedReading == null) {
                if (!isFinishedReading.value) {
                    Text(text = "Mark as Read")
                } else {
                    Text(
                        text = "Finished Reading",
                        modifier = Modifier.alpha(0.6f),
                        color = Color.Red.copy(alpha = 0.5f)
                    )
                }
            } else {
                Text(text = "Finished on :${book.finishedReading}") //To do format Date
            }
        }
    }

    Text(text = "Raring", modifier = Modifier.padding(bottom = 4.dp))
    book?.rating?.toInt().let { it ->
        RatingBar(rating = it!!) { rating ->
            ratingVal.value = rating
        }
    }

    Spacer(modifier = Modifier.padding(bottom = 8.dp))

    Row() {
        RoundedButton(label = "Update") {
            //Update if only changes in data
            val changeNotes = book?.notes != notesText.value
            val changeRate = book?.rating?.toInt() != ratingVal.value
            val isFinishedTimeStamp =
                if (isFinishedReading.value) Timestamp.now() else book?.finishedReading
            val isStartedTimeStamp =
                if (isStartedReading.value) Timestamp.now() else book?.startedReading

            val bookUpdate =
                changeNotes || changeRate || isFinishedReading.value || isStartedReading.value
            val bookToUpdate = hashMapOf(
                "finished_reading" to isFinishedTimeStamp,
                "started_reading" to isStartedTimeStamp,
                "rating" to ratingVal.value,
                "notes" to notesText.value
            ).toMap()

            //Updating
            if(bookUpdate) {
                FirebaseFirestore.getInstance().collection("books").document(book?.id.toString()).update(bookToUpdate).addOnCompleteListener {
                    Log.d("TAG", "Update Success")
                }.addOnFailureListener {
                    Log.d("TAG", "Update Failure")
                }
            }

        }

        Spacer(modifier = Modifier.width(80.dp))

        RoundedButton(label = "Delete") {

        }
    }


}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SimpleForm(
    modifier: Modifier = Modifier,
    loading: Boolean = false,
    defaultValue: String = "Great Book",
    onSearch: (String) -> Unit
) {
    val textFieldValue = rememberSaveable {
        mutableStateOf(defaultValue)
    }
    val keyBoardController = LocalSoftwareKeyboardController.current
    val valid = remember(textFieldValue.value) {
        textFieldValue.value.trim().isNotEmpty()
    }

    InputField(
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp)
            .padding(4.dp)
            .background(Color.White, CircleShape)
            .padding(horizontal = 16.dp, vertical = 16.dp),
        valueState = textFieldValue,
        labelId = "Enter your thoughts",
        isEnabled = true,
        onAction = KeyboardActions {
            if (valid.not()) return@KeyboardActions
            onSearch(textFieldValue.value.trim())
            keyBoardController?.hide()
        })
}
