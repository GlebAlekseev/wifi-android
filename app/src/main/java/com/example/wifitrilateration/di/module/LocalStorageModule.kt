package com.example.wifitrilateration.di.module

import android.content.Context
import androidx.room.Room
import com.example.wifitrilateration.data.local.room.AppDatabase
import com.example.wifitrilateration.data.mapper.RouterMapperImpl
import com.example.wifitrilateration.domain.entity.Router
import com.glebalekseevjk.yasmrhomework.data.local.dao.RouterDao
import com.glebalekseevjk.yasmrhomework.data.local.model.RouterDbModel
import com.glebalekseevjk.yasmrhomework.domain.mapper.Mapper
import dagger.Binds
import dagger.Module
import dagger.Provides


@Module
interface LocalStorageModule {
    @Binds
    fun provideMapperTodoItem(todoItemMapperImpl: RouterMapperImpl): Mapper<Router, RouterDbModel>

    companion object {
        @Provides
        fun provideAppDataBase(context: Context): AppDatabase {
            return Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                AppDatabase.DATABASE_NAME
            ).build()
        }

        @Provides
        fun provideTodoItemDao(appDatabase: AppDatabase): RouterDao {
            return appDatabase.routerDao()
        }
    }
}
