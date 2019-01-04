package com.kshitijchauhan.haroldadmin.moviedb.ui.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kshitijchauhan.haroldadmin.moviedb.MovieDBApplication
import com.kshitijchauhan.haroldadmin.moviedb.ui.UIState
import com.kshitijchauhan.haroldadmin.moviedb.utils.Constants
import com.kshitijchauhan.haroldadmin.moviedb.utils.SharedPreferencesDelegate
import com.kshitijchauhan.haroldadmin.moviedb.utils.SingleLiveEvent
import com.kshitijchauhan.haroldadmin.moviedb.utils.extensions.log

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val _state = SingleLiveEvent<UIState>()
    private val _snackbar = SingleLiveEvent<String>()
    private val _bottomNavSelectedIconId = SingleLiveEvent<Int>()
    private val _clearBackStack = SingleLiveEvent<Unit>()
    private val _toolbarTitle = SingleLiveEvent<String>()
//    TODO: Change this implementation from a boolean to a stack so that progress bar only hides when the loading stack is empty
    private val _progressBar = MutableLiveData<Boolean>()
    private var _isAuthenticated by SharedPreferencesDelegate(application, Constants.KEY_IS_AUTHENTICATED, false)
    private var _sessionId by SharedPreferencesDelegate(application, Constants.KEY_SESSION_ID, "")
    private var _accountId by SharedPreferencesDelegate(application, Constants.KEY_ACCOUNT_ID, -1)

    val state: LiveData<UIState>
        get() = _state

    val isAuthenticated: Boolean
        get() = _isAuthenticated

    val sessionId: String
        get() = _sessionId

    val accountId: Int
        get() = _accountId

    val snackbar: LiveData<String>
        get() = _snackbar

    val bottomNavSelectedItemId: LiveData<Int>
        get() = _bottomNavSelectedIconId

    val progressBar: LiveData<Boolean>
        get() = _progressBar

    val clearBackStack: LiveData<Unit>
        get() = _clearBackStack

    val toolbarTitle: LiveData<String>
        get() = _toolbarTitle

    fun <T : UIState> updateStateTo(state: T) {
        log("Updating view to: ${state::class.java.simpleName}")
        _state.postValue(state)
    }

    fun showSnackbar(message: String) {
        _snackbar.postValue(message)
    }

    fun setAuthenticationStatus(status: Boolean) {
        _isAuthenticated = status
    }

    fun setSessionId(sessionId: String) {
        _sessionId = sessionId
    }

    fun setBottomNavSelectedItemId(id: Int) {
        _bottomNavSelectedIconId.value = id
    }

    fun setProgressBarVisible(status: Boolean) {
        _progressBar.value = status
    }

    fun peekState() = _state.value

    fun rebuildAppComponent() = getApplication<MovieDBApplication>().rebuildAppComponent()

    fun signalClearBackstack() = _clearBackStack.call()

    fun updateToolbarTitle(title: String) {
        _toolbarTitle.value = title
    }

    fun setAccountId(newId: Int) {
        _accountId = newId
    }
}