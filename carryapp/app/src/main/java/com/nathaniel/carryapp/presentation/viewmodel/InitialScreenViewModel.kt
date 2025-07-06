package com.nathaniel.carryapp.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class InitialScreenViewModel @Inject constructor() : ViewModel() {

    fun onGetStartedClicked() {
        Log.d("TST", "Get started clicked.")
    }
}