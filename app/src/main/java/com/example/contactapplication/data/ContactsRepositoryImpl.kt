package com.example.contactapplication.data


import com.example.contactapplication.domain.repository.ContactsRepository
import com.example.contactapplication.data.local.ContactDao
import com.example.contactapplication.model.ContactsModel

class ContactsRepositoryImpl(private val contactDao: ContactDao) : ContactsRepository {
    override suspend fun getAllContacts(): List<ContactsModel> {
        return contactDao.getAllContacts()
    }

    override suspend fun deleteContact(contact: ContactsModel) {
        contactDao.deleteContact(contact)
    }
}
