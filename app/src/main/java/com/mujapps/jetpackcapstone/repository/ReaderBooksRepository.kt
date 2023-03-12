package com.mujapps.jetpackcapstone.repository

import com.mujapps.jetpackcapstone.data.DataOrException
import com.mujapps.jetpackcapstone.model.BookItem
import com.mujapps.jetpackcapstone.network.ReaderApiService
import javax.inject.Inject

class ReaderBooksRepository @Inject constructor(
    val mBooksApi: ReaderApiService
) {
    private val dataOrException = DataOrException<List<BookItem>, Boolean, Exception>()
    private val bookInformationOrException = DataOrException<BookItem, Boolean, Exception>()

    suspend fun getAllBooksByQuery(searchQuery: String): DataOrException<List<BookItem>, Boolean, Exception> {

       try {
            dataOrException.loading = true
            dataOrException.data = mBooksApi.getAllBooks(searchQuery).items

            if (dataOrException.data!!.isNotEmpty()) dataOrException.loading = false
        } catch (e: Exception) {
            dataOrException.e = e
        }
        return dataOrException
    }

    suspend fun getBookInformation(bookId : String) : DataOrException<BookItem, Boolean, Exception> {
        try {
            bookInformationOrException.loading = true
            bookInformationOrException.data = mBooksApi.getABookDetails(bookId)

            if (bookInformationOrException.data.toString().isNotEmpty()) bookInformationOrException.loading = false
        } catch (e: Exception) {
            bookInformationOrException.e = e
        }
        return bookInformationOrException
    }
}