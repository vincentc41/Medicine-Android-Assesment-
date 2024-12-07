package com.app.assessment.room

import android.content.Context
import androidx.room.Room
import com.app.assessment.AppDatabase
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
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "medicine_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideMedicineDao(appDatabase: AppDatabase): MedicineDao {
        return appDatabase.medicineDao()
    }
}
