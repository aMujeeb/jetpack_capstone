package com.mujapps.jetpackcapstone.repository

import com.mujapps.jetpackcapstone.data.DataOrException
import com.mujapps.jetpackcapstone.data.Resource
import com.mujapps.jetpackcapstone.model.BookItem
import com.mujapps.jetpackcapstone.network.ReaderApiService
import javax.inject.Inject

class ReaderBooksRepository @Inject constructor(
    val mBooksApi: ReaderApiService
) {
    /*private val dataOrException = DataOrException<List<BookItem>, Boolean, Exception>()
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
    }*/

    suspend fun getBooksByQuery(searchQuery: String): Resource<List<BookItem>> {
        return try {
            Resource.Loading(data = true)
            val itemList = mBooksApi.getAllBooks(searchQuery).items
            if (itemList.isNotEmpty()) Resource.Loading(data = false)
            Resource.Success(data = itemList)
        } catch (e: Exception) {
            Resource.Error(message = e.message.toString())
        }
    }

    suspend fun getBooksDetails(bookId: String): Resource<BookItem> {
        val response = try {
            Resource.Loading(data = true)
            mBooksApi.getABookDetails(bookId)
        } catch (e: Exception) {
            return Resource.Error(message = e.message.toString())
        }
        return Resource.Success(data = response)
    }
}