package com.example.rickandmorty.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.rickandmorty.databinding.ItemCharacterBinding
import com.example.rickandmorty.model.Character

class FavouriteCharactersAdapter :
    RecyclerView.Adapter<FavouriteCharactersAdapter.FavouriteCharactersViewHolder>() {

    inner class FavouriteCharactersViewHolder(val binding: ItemCharacterBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val differCallback = object : DiffUtil.ItemCallback<Character>() {
        override fun areItemsTheSame(oldItem: Character, newItem: Character): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: Character, newItem: Character): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FavouriteCharactersViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemCharacterBinding.inflate(layoutInflater, parent, false)
        return FavouriteCharactersViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavouriteCharactersViewHolder, position: Int) {
        val character = differ.currentList[position]
        holder.binding.apply {
            Glide.with(itemCharacterImageView).load(character.image).into(itemCharacterImageView)
            itemCharacterTextView.text = character.name
            root.setOnClickListener {
                onItemClickListener?.let { it(character) }
            }
        }
    }

    private var onItemClickListener: ((Character) -> Unit)? = null

    fun setOnItemClickListener(listener: (Character) -> Unit) {
        onItemClickListener = listener
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}
