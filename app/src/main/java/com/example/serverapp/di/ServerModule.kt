package com.example.serverapp.di

//import android.content.Context
//import androidx.room.Room
//import com.example.server.data.AppDatabase
//import com.example.server.data.LogDao
//import com.example.serverapp.room.AppDatabase
//import com.example.serverapp.room.LogDao
//import dagger.Module
//import dagger.Provides
//import dagger.hilt.InstallIn
//import dagger.hilt.android.qualifiers.ApplicationContext
//import dagger.hilt.components.SingletonComponent
//import javax.inject.Singleton
//
//@Module
//@InstallIn(SingletonComponent::class)
//object ServerModule {
//
//    @Provides
//    @Singleton
//    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
//        return Room.databaseBuilder(
//            context,
//            AppDatabase::class.java,
//            "logs.db"
//        ).build()
//    }
//
//    @Provides
//    fun provideLogDao(database: AppDatabase): LogDao {
//        return database.logDao()
//    }
//}
