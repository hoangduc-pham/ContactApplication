package com.example.contactapplication.ui.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.example.contactapplication.domain.DeleteContactUseCase
import com.example.contactapplication.domain.GetContactsUseCase
import com.example.contactapplication.model.ContactsModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getContactsUseCase: GetContactsUseCase,
    private val deleteContactUseCase: DeleteContactUseCase
) : ViewModel() {

    private val _contacts = MutableLiveData<List<ContactsModel>>()
    val contacts: LiveData<List<ContactsModel>> get() = _contacts

    fun loadContacts() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val contactsList = getContactsUseCase().sortedBy { it.name }
                _contacts.postValue(contactsList)
            } catch (e: Exception) {
                 Log.e("MainViewModel", "Error loading contacts: ${e.message}")
            }
        }
    }


    fun deleteContact(position: Int) {
        try {
            val contactToDelete = _contacts.value?.get(position) ?:
            throw IllegalArgumentException("...")

            viewModelScope.launch(Dispatchers.IO) {
                deleteContactUseCase(contactToDelete)
                loadContacts()
            }
        } catch (e: Exception) {
             Log.e("MainViewModel", "Error deleting contact: ${e.message}")
        }
    }

}

