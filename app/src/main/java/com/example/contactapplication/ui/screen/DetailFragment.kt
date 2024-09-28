package com.example.contactapplication.ui.screen

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.contactapplication.data.local.ContactDatabase
import com.example.contactapplication.databinding.FragmentDetailBinding
import com.example.contactapplication.model.ContactsModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailFragment : Fragment() {
    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var database: ContactDatabase
    private var selectedImageUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initData()
        initButton()
    }

    private fun initButton() {
        binding.profileImageView.setOnClickListener {
            openGallery()
        }
        binding.backBtn.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
        binding.buttonEdit.setOnClickListener {
            saveContact()
        }
        binding.callButton.setOnClickListener {
            makeCall()
        }
    }

    private fun initData() {
        database = ContactDatabase.getDatabase(requireContext())

        val name = arguments?.getString("NAME")
        val phoneNumber = arguments?.getString("PHONE_NUMBER")
        val email = arguments?.getString("EMAIL")
        val imageUriString = arguments?.getString("IMAGE_URI")
        selectedImageUri = Uri.parse(imageUriString)

        binding.nameEditText.setText(name)
        binding.phoneEditText.setText(phoneNumber)
        binding.emailEditText.setText(email)

        Glide.with(this)
            .load(selectedImageUri)
            .apply(RequestOptions.circleCropTransform())
            .into(binding.profileImageView)
    }

    private fun makeCall() {
        val phoneNumber = binding.phoneEditText.text.toString()

        if (phoneNumber.isNotEmpty()) {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:$phoneNumber")
            startActivity(intent)
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            data?.data?.let { uri ->
                selectedImageUri = uri

                Glide.with(this)
                    .load(selectedImageUri)
                    .apply(RequestOptions.circleCropTransform())
                    .into(binding.profileImageView)
            }
        }
    }

    private fun saveContact() {
        val updatedName = binding.nameEditText.text.toString()
        val updatedPhone = binding.phoneEditText.text.toString()
        val updatedEmail = binding.emailEditText.text.toString()
        val contactId = arguments?.getLong("CONTACT_ID") ?: 0

        val updatedContact = ContactsModel(
            contactId,
            updatedName,
            updatedPhone,
            updatedEmail,
            selectedImageUri.toString()
        )

        lifecycleScope.launch {
            database.contactDao().updateContact(updatedContact)
            requireActivity().setResult(Activity.RESULT_OK)
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val REQUEST_CODE_PICK_IMAGE = 1001
    }
}
