package com.example.contactapplication.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.contactapplication.model.ContactsModel
import com.example.contactapplication.R
import com.example.contactapplication.databinding.ViewholderContactsBinding

class ContactsAdapter(
    private val items: List<ContactsModel>,
    private val clickListener: (ContactsModel) -> Unit
) : RecyclerView.Adapter<ContactsAdapter.Viewholder>() {
    private lateinit var context: Context

    class Viewholder(val binding: ViewholderContactsBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Viewholder {
        context = parent.context
        val binding = ViewholderContactsBinding.inflate(
            LayoutInflater.from(context),
            parent,
            false
        )
        return Viewholder(binding)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: Viewholder, position: Int) {
        val item = items[position]
        holder.binding.nameTxt.text = item.name

        if (item.imageUri.isNotBlank()) {
            Glide.with(context)
                .load(item.imageUri)
                .placeholder(R.drawable.baseline_people_alt_24)
                .error(R.drawable.baseline_people_alt_24)
                .into(holder.binding.pic)
        } else {
            holder.binding.pic.setImageResource(R.drawable.baseline_people_alt_24)
        }

        holder.itemView.setOnClickListener {
            clickListener(item)
        }
    }
}
