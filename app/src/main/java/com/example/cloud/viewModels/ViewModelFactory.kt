package com.example.cloud.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.cloud.ui.createAccount.CreateAccountFragment
import com.example.cloud.viewModels.createAccountViewModel.CreateAccountViewModel
import com.example.cloud.viewModels.homeViewModel.HomeViewModel
import com.example.cloud.viewModels.loginViewModel.LoginViewModel

class ViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> LoginViewModel() as T
            modelClass.isAssignableFrom(CreateAccountViewModel::class.java) -> CreateAccountViewModel() as T
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> HomeViewModel() as T
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}