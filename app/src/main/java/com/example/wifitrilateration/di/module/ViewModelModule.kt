package com.example.wifitrilateration.di.module

import androidx.lifecycle.ViewModel
import com.example.wifitrilateration.di.FromViewModelFactory
import com.example.wifitrilateration.di.ViewModelKey
import com.example.wifitrilateration.ui.viewmodel.*
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

@Module
interface ViewModelModule {
    @Binds
    @[IntoMap ViewModelKey(MapsViewModel::class)]
    fun bindMapsViewModel(mapsViewModel: MapsViewModel): ViewModel

    @Binds
    @[IntoMap ViewModelKey(MainViewModel::class)]
    fun bindMainViewModel(mainViewModel: MainViewModel): ViewModel

    @Binds
    @[IntoMap ViewModelKey(RoutersConfigViewModel::class)]
    fun bindRoutersConfigViewModel(mainViewModel: RoutersConfigViewModel): ViewModel

    @Binds
    @[IntoMap ViewModelKey(RoutersNowViewModel::class)]
    fun bindRoutersNowViewModel(mainViewModel: RoutersNowViewModel): ViewModel

    companion object {
        @FromViewModelFactory
        @Provides
        fun provideMapsViewModel(viewModelFactory: ViewModelFactory): MapsViewModel {
            return viewModelFactory.create(MapsViewModel::class.java)
        }

        @FromViewModelFactory
        @Provides
        fun provideMainViewModel(viewModelFactory: ViewModelFactory): MainViewModel {
            return viewModelFactory.create(MainViewModel::class.java)
        }

        @FromViewModelFactory
        @Provides
        fun provideRoutersNowViewModel(viewModelFactory: ViewModelFactory): RoutersNowViewModel {
            return viewModelFactory.create(RoutersNowViewModel::class.java)
        }

        @FromViewModelFactory
        @Provides
        fun provideRoutersConfigViewModel(viewModelFactory: ViewModelFactory): RoutersConfigViewModel {
            return viewModelFactory.create(RoutersConfigViewModel::class.java)
        }

    }
}