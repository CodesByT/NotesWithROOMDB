package com.example.notes.di

import android.app.Application
import androidx.room.Room
import com.example.notes.data.repository.MyRepository
import com.example.notes.data.repository.Repository
import com.example.notes.data.room.MyDao
import com.example.notes.data.room.MyDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    @Provides
    @Singleton
    fun provideMyDatabase(app: Application):MyDatabase{
        return Room.databaseBuilder(
            app,
            MyDatabase::class.java,
            "MyDatabase"
        ).build()
    }

    @Provides
    @Singleton
    fun provideMyDao(database: MyDatabase): MyDao {
        return database.dao
    }

    @Provides
    @Singleton
    fun provideMyRepository(mydb: MyDatabase):Repository{
        return MyRepository(mydb.dao)
    }

}