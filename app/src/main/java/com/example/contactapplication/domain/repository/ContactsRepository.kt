package com.example.contactapplication.domain.repository

import com.example.contactapplication.model.ContactsModel

interface ContactsRepository {
    suspend fun getAllContacts(): List<ContactsModel>
    suspend fun deleteContact(contact: ContactsModel)
}