package com.mujapps.jetpackcapstone.screens.home

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mujapps.jetpackcapstone.data.DataOrException
import com.mujapps.jetpackcapstone.model.MBook
import com.mujapps.jetpackcapstone.repository.FireRepository
import com.mujapps.jetpackcapstone.repository.ReaderBooksRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val mFireRepository: FireRepository
) : ViewModel() {

    var retrievedBooksData: MutableState<DataOrException<List<MBook>, Boolean, Exception>> = mutableStateOf(
        DataOrException(
            listOf(), true, Exception("")
        )
    )

    init {
        getAllBooksStored()
    }

    private fun getAllBooksStored() {
        viewModelScope.launch {
            retrievedBooksData.value.loading = true
            retrievedBooksData.value = mFireRepository.getAllBooksStored()
            if (retrievedBooksData.value.data.isNullOrEmpty().not()) retrievedBooksData.value.loading = false
        }
    }
}