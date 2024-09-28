package com.example.contactapplication.ui.screen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.contactapplication.R
import com.example.contactapplication.ui.adapter.ContactsAdapter
import com.example.contactapplication.databinding.FragmentHomeBinding
import com.example.contactapplication.model.ContactsModel
import com.example.contactapplication.ui.viewModel.MainViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by viewModels()
    private lateinit var contactsList: List<ContactsModel>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observeContacts()
        setupTouchForDelete()
        setupSearchView()
        setupClickListeners()
    }

    private fun setupRecyclerView() {
        binding.listContacts.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun observeContacts() {
        viewModel.contacts.observe(viewLifecycleOwner) { contacts ->
            contactsList = contacts
            binding.listContacts.adapter = ContactsAdapter(contactsList) { contact ->

                val bundle = Bundle().apply {
                    putLong("CONTACT_ID", contact.id)
                    putString("NAME", contact.name)
                    putString("IMAGE_URI", contact.imageUri)
                    putString("PHONE_NUMBER", contact.phoneNumber)
                    putString("EMAIL", contact.email)
                }
                findNavController().navigate(R.id.action_homeFragment_to_detailFragment, bundle)
            }
        }
        viewModel.loadContacts()
    }

    private fun setupTouchForDelete() {
        val itemTouch = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                viewModel.deleteContact(position)
            }
        }

        ItemTouchHelper(itemTouch).attachToRecyclerView(binding.listContacts)
    }

    private fun setupSearchView() {
        binding.toolbar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterContacts(newText)
                return true
            }
        })
    }

    private fun filterContacts(query: String?) {
        val filteredList = contactsList.filter { contact ->
            contact.name.contains(query ?: "", ignoreCase = true)
        }
        binding.listContacts.adapter = ContactsAdapter(filteredList) { contact ->

            val bundle = Bundle().apply {
                putLong("CONTACT_ID", contact.id)
                putString("NAME", contact.name)
                putString("IMAGE_URI", contact.imageUri)
                putString("PHONE_NUMBER", contact.phoneNumber)
                putString("EMAIL", contact.email)
            }
            findNavController().navigate(R.id.action_homeFragment_to_detailFragment, bundle)
        }
    }

    private fun setupClickListeners() {
        binding.floatingActionButton.setOnClickListener {
            val navController = findNavController()

            view?.findViewById<FloatingActionButton>(R.id.floatingActionButton)?.setOnClickListener {
                navController.navigate(R.id.action_homeFragment_to_addContactFragment)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}