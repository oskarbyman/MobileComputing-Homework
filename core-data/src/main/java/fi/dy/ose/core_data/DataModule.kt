package fi.dy.ose.core_data

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import fi.dy.ose.code.domain.repository.ReminderRepository
import fi.dy.ose.core_data.datasource.reminder.ReminderDataSource
import fi.dy.ose.core_data.datasource.reminder.ReminderDataSourceImpl
import fi.dy.ose.core_data.repository.ReminderRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

    @Singleton
    @Binds
    fun bindReminderDataSource(
        reminderDataSource: ReminderDataSourceImpl
    ): ReminderDataSource

    @Singleton
    @Binds
    fun bindReminderRepository(
        reminderDataSource: ReminderRepositoryImpl
    ): ReminderRepository
}