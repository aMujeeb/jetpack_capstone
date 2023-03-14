package com.mujapps.jetpackcapstone.screens.search

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mujapps.jetpackcapstone.data.DataOrException
import com.mujapps.jetpackcapstone.model.BookItem
import com.mujapps.jetpackcapstone.repository.ReaderBooksRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BooksSearchViewModel @Inject constructor(private val mBooksRepository: ReaderBooksRepository) :
    ViewModel() {

    val listOfBooks: MutableState<DataOrException<List<BookItem>, Boolean, Exception>> =
        mutableStateOf(
            DataOrException(null, true, Exception(""))
        )

    init {
        //searchBooks("Android")
    }

    fun searchBooks(query: String) {
        viewModelScope.launch {
            if(query.isEmpty()) return@launch

            listOfBooks.value.loading = true
            listOfBooks.value = mBooksRepository.getAllBooksByQuery(query)
            Log.d("TAG", "xx"+listOfBooks.value.data.toString())
            if(listOfBooks.value.data.toString().isNotEmpty()) listOfBooks.value.loading = false
        }
    }
}