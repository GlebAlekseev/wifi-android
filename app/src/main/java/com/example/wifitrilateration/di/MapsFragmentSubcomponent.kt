package com.example.wifitrilateration.di

import com.example.wifitrilateration.di.module.ViewModelModule
import com.example.wifitrilateration.ui.fragment.MapsFragment
import dagger.Subcomponent

@Subcomponent(modules = [ViewModelModule::class])
interface MapsFragmentSubcomponent {
    fun inject(mapsFragment: MapsFragment)
}