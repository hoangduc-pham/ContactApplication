package com.example.contactapplication.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.contactapplication.model.ContactsModel

@Dao
interface ContactDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertContact(contact: ContactsModel)

    @Query("SELECT * FROM contacts")
    suspend fun getAllContacts(): List<ContactsModel>

    @Delete
    suspend fun deleteContact(contact: ContactsModel)

    @Update
    suspend fun updateContact(contact: ContactsModel)
}