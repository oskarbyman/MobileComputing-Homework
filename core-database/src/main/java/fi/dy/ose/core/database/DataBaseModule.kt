package fi.dy.ose.core.database

import android.content.Context
import androidx.room.ProvidedAutoMigrationSpec
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import fi.dy.ose.core.database.dao.ReminderDao
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class DataBaseModule {

    @Provides
    fun provideReminderDao(appDatabase: AppDatabase) : ReminderDao {
        return appDatabase.reminderDao()
    }

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context) : AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "appDB"
        ).allowMainThreadQueries().fallbackToDestructiveMigration().build()
    }
}