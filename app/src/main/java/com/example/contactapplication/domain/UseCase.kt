package com.example.contactapplication.domain

import com.example.contactapplication.domain.repository.ContactsRepository
import com.example.contactapplication.model.ContactsModel

class GetContactsUseCase(private val repository: ContactsRepository) {
    suspend operator fun invoke(): List<ContactsModel> {
        return repository.getAllContacts()
    }
}

class DeleteContactUseCase(private val repository: ContactsRepository) {
    suspend operator fun invoke(contact: ContactsModel) {
        repository.deleteContact(contact)
    }
}