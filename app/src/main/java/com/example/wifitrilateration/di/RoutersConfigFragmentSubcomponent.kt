package com.example.wifitrilateration.di

import com.example.wifitrilateration.di.module.ViewModelModule
import com.example.wifitrilateration.ui.fragment.MapsFragment
import com.example.wifitrilateration.ui.fragment.RoutersConfigFragment
import dagger.Subcomponent

@Subcomponent(modules = [ViewModelModule::class])
interface RoutersConfigFragmentSubcomponent {
    fun inject(routersConfigFragment: RoutersConfigFragment)
}