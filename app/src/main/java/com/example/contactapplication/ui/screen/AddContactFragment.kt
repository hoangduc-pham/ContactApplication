package com.example.contactapplication.ui.screen

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.contactapplication.data.local.ContactDatabase
import com.example.contactapplication.databinding.FragmentAddContactBinding
import com.example.contactapplication.model.ContactsModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddContactFragment : Fragment() {
    private var _binding: FragmentAddContactBinding? = null
    private val binding get() = _binding!!
    private val REQUEST_CODE_PICK_IMAGE = 1001
    private lateinit var currentImageUri: Uri

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddContactBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
    }

    private fun init() {
        binding.backBtn.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
        binding.imageButton.setOnClickListener {
            openImagePicker()
        }
        binding.buttonSave.setOnClickListener {
            saveContact()
        }
    }

    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            val imageUri: Uri? = data?.data
            imageUri?.let {
                currentImageUri = it
                Glide.with(this)
                    .load(it)
                    .apply(RequestOptions.circleCropTransform())
                    .into(binding.imageButton)
            }
        }
    }

    private fun saveContact() {
        val contactDatabase = ContactDatabase.getDatabase(requireContext())

        val name = binding.editTextName.text.toString()
        val phone = binding.editTextPhone.text.toString()
        val email = binding.editTextEmail.text.toString()

        if (name.isNotBlank() && phone.isNotBlank() && email.isNotBlank()) {
            if (!::currentImageUri.isInitialized) {
                Toast.makeText(requireContext(), "Please select an image", Toast.LENGTH_SHORT).show()
                return
            }

            val contact = ContactsModel(
                name = name,
                phoneNumber = phone,
                email = email,
                imageUri = currentImageUri.toString()
            )


            CoroutineScope(Dispatchers.IO).launch {
                contactDatabase.contactDao().insertContact(contact)
                requireActivity().runOnUiThread {
                    Toast.makeText(requireContext(), "Contact saved!", Toast.LENGTH_SHORT).show()
                    requireActivity().supportFragmentManager.popBackStack()
                }
            }
        } else {
            Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
