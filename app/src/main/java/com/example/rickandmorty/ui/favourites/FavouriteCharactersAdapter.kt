package com.example.rickandmorty.ui.favourites

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.rickandmorty.R
import com.example.rickandmorty.databinding.ItemCharacterBinding
import com.example.rickandmorty.model.Character

class FavouriteCharactersAdapter(private val listener: OnItemClickListener) :
    ListAdapter<Character, FavouriteCharactersAdapter.FavouriteCharactersViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FavouriteCharactersViewHolder {
        val binding =
            ItemCharacterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavouriteCharactersViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavouriteCharactersViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }

    inner class FavouriteCharactersViewHolder(private val binding: ItemCharacterBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val item = getItem(position)
                    if (item != null) {
                        listener.onItemClick(item)
                    }
                }
            }
        }

        fun bind(character: Character) {
            binding.apply {
                Glide.with(itemView)
                    .load(character.image)
                    .circleCrop()
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .error(R.drawable.ic_error)
                    .into(itemCharacterImageView)

                itemCharacterTextView.text = character.name
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(character: Character)
    }

    class DiffCallback : DiffUtil.ItemCallback<Character>() {
        override fun areItemsTheSame(oldItem: Character, newItem: Character): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Character, newItem: Character): Boolean {
            return oldItem == newItem
        }
    }
}
