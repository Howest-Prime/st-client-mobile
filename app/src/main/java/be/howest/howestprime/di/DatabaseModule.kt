package be.howest.howestprime.di

import android.content.Context
import androidx.room.Room
import be.howest.howestprime.data.local.room.AppDatabase
import be.howest.howestprime.data.local.room.dao.SnackDao
import be.howest.howestprime.data.local.room.dao.TicketDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "howestprime.db").build()

    @Provides
    @Singleton
    fun provideSnackDao(db: AppDatabase): SnackDao = db.snackDao()

    @Provides
    @Singleton
    fun provideTicketDao(db: AppDatabase): TicketDao = db.ticketDao()
}
