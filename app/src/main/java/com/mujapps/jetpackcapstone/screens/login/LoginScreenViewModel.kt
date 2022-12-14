package com.mujapps.jetpackcapstone.screens.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginScreenViewModel : ViewModel() {
    //val loadingState = MutableStateFlow(LoadingState.IDLE)

    private val  auth : FirebaseAuth= Firebase.auth
    private val _loading = MutableLiveData<Boolean>()
}