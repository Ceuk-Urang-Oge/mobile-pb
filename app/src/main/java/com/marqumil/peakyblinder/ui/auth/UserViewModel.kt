package com.marqumil.peakyblinder.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.marqumil.peakyblinder.local.SharedPrefs
import com.marqumil.peakyblinder.local.SharedPrefs.Companion.KEY_LOGIN
import com.marqumil.peakyblinder.remote.ResultState
import com.marqumil.peakyblinder.remote.post.LoginBody
import com.marqumil.peakyblinder.remote.response.LoginResponse
import com.marqumil.peakyblinder.remote.response.UserResponse
import com.marqumil.peakyblinder.remote.retrofit.ApiConfig
import com.orhanobut.hawk.Hawk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserViewModel : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val currentUserLiveData = MutableLiveData<FirebaseUser?>()

    private val token = Hawk.get<LoginResponse?>(SharedPrefs.KEY_LOGIN)?.loginToken

    private val _emailError = MutableLiveData<String>()
    val emailError : LiveData<String> = _emailError

    private val _passwordError = MutableLiveData<String>()
    val passwordError : LiveData<String> = _passwordError

    private val _loginState = MutableLiveData<ResultState<Boolean>>()
    val loginState : LiveData<ResultState<Boolean>> = _loginState

    private val _userState = MutableLiveData<ResultState<UserResponse>>()
    val userState : LiveData<ResultState<UserResponse>> = _userState

    private val _logOutState = MutableLiveData<ResultState<Boolean>>()
    val logOutState : LiveData<ResultState<Boolean>> = _logOutState

    init {
        currentUserLiveData.value = auth.currentUser
        // Set up an AuthStateListener to update the LiveData when the user's authentication state changes.
        auth.addAuthStateListener { firebaseAuth ->
            currentUserLiveData.value = firebaseAuth.currentUser
        }
    }

    fun login(email: String, password: String) {
        if (email == "" || email.isEmpty()) {
            _emailError.value = "Silakan masukkan Email terlebih dahulu!"
        } else if (password == "" || password.isEmpty()) {
            _passwordError.value = "Silakan masukkan password terlebih dahulu!"
        } else {
            pushLogin(email, password)
        }
    }

    private fun pushLogin(nik: String, password: String) {
        _loginState.value = ResultState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            val loginBody = LoginBody(nik, password)
            try {
                val apiClient = ApiConfig.getApiService().postLogin(loginBody)
                val data = apiClient.data
                if (data != null) {
                    Hawk.put(KEY_LOGIN, data)
                    _loginState.postValue(ResultState.Success(true))
                }
                else {
                    _loginState.postValue(ResultState.Failure(Exception(apiClient.message ?: "Unknown Error")))
                }
            }
            catch (e : Exception) {
                _loginState.postValue(ResultState.Failure(e))
            }
        }
    }

    fun getUser() {
        viewModelScope.launch {
            try {
                _userState.value = ResultState.Loading
                val response = ApiConfig.getApiService().getUser()
                val user = response?.data
                if (user != null) {
                    _userState.postValue(ResultState.Success(user))
                } else {
                    _userState.postValue(ResultState.Failure(Throwable("Data Kosong")))
                }
            } catch (e: Exception) {
                _userState.postValue(ResultState.Failure(e))
            }
        }
    }


    fun logOut() {
        viewModelScope.launch {
            try {
                _logOutState.value = ResultState.Loading
                val response = ApiConfig.getApiService().getLogout()
                val user = response?.data
                if (user != null) {
                    _logOutState.postValue(ResultState.Success(true))
                } else {
                    _logOutState.postValue(ResultState.Failure(Throwable("Data Kosong")))
                }
            } catch (e: Exception) {
                _logOutState.postValue(ResultState.Failure(e))
            }
        }
    }


    // LiveData to observe changes in the user's authentication state
    fun getCurrentUserLiveData(): LiveData<FirebaseUser?> = currentUserLiveData

    // Method to sign out the current user
    fun signOut() {
        auth.signOut()
    }

    // Method to check if the user is signed in
    fun isUserSignedIn(): Boolean {
        return auth.currentUser != null
    }

    fun getEmail(): String? {
        if (!isUserSignedIn()) {
            return null
        }
        return auth.currentUser?.email
    }

    fun getUsername(): String? {
        if (!isUserSignedIn()) {
            return null
        }
        return auth.currentUser?.displayName
    }
}