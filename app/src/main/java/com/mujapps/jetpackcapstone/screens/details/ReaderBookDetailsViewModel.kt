package com.mujapps.jetpackcapstone.screens.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mujapps.jetpackcapstone.data.Resource
import com.mujapps.jetpackcapstone.model.BookItem
import com.mujapps.jetpackcapstone.repository.ReaderBooksRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReaderBookDetailsViewModel @Inject constructor(private val mBooksRepository: ReaderBooksRepository) :
    ViewModel() {

        suspend fun getBookInformation(bookId : String) : Resource<BookItem> {
            return mBooksRepository.getBooksDetails(bookId = bookId)
        }
}