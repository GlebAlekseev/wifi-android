package com.example.wifitrilateration.di

import com.example.wifitrilateration.di.module.ViewModelModule
import com.example.wifitrilateration.ui.fragment.MapsFragment
import com.example.wifitrilateration.ui.fragment.RoutersConfigFragment
import com.example.wifitrilateration.ui.fragment.RoutersNowFragment
import dagger.Subcomponent

@Subcomponent(modules = [ViewModelModule::class])
interface RoutersNowFragmentSubcomponent {
    fun inject(routersNowFragment: RoutersNowFragment)
}