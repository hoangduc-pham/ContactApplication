package com.example.contactapplication.data.di

import android.content.Context
import com.example.contactapplication.data.local.ContactDao
import com.example.contactapplication.data.local.ContactDatabase
import com.example.contactapplication.domain.repository.ContactsRepository
import com.example.contactapplication.data.ContactsRepositoryImpl
import com.example.contactapplication.domain.DeleteContactUseCase
import com.example.contactapplication.domain.GetContactsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): ContactDatabase {
        return ContactDatabase.getDatabase(context)
    }

    @Provides
    @Singleton
    fun provideContactDao(database: ContactDatabase): ContactDao {
        return database.contactDao()
    }

    @Provides
    @Singleton
    fun provideContactRepository(contactDao: ContactDao): ContactsRepository {
        return ContactsRepositoryImpl(contactDao)
    }

    @Provides
    @Singleton
    fun provideGetContactsUseCase(repository: ContactsRepository): GetContactsUseCase {
        return GetContactsUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideDeleteContactUseCase(repository: ContactsRepository): DeleteContactUseCase {
        return DeleteContactUseCase(repository)
    }
}