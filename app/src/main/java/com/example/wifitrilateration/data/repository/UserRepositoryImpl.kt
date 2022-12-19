package com.example.wifitrilateration.data.repository

import android.content.Context
import com.example.wifitrilateration.data.preferences.SharedPreferencesUserStorage
import com.example.wifitrilateration.domain.repository.UserRepository
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(context: Context) :
    SharedPreferencesUserStorage(context),
    UserRepository
