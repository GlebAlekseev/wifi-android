package com.example.wifitrilateration.di

import com.example.wifitrilateration.di.module.ViewModelModule
import com.example.wifitrilateration.ui.activity.MainActivity
import dagger.Subcomponent

@Subcomponent(modules = [ViewModelModule::class])
interface MainActivitySubcomponent {
    fun inject(activity: MainActivity)
}

