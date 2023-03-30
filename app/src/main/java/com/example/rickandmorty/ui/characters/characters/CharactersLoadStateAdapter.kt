package com.example.rickandmorty.ui.characters.characters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.rickandmorty.databinding.ItemCharacterLoadStateFooterBinding

class CharactersLoadStateAdapter(private val retry: () -> Unit) :
    LoadStateAdapter<CharactersLoadStateAdapter.LoadStateViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadStateViewHolder {
        val binding = ItemCharacterLoadStateFooterBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return LoadStateViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    inner class LoadStateViewHolder(private val binding: ItemCharacterLoadStateFooterBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.footerButtonRetry.setOnClickListener {
                retry.invoke()
            }
        }

        fun bind(loadState: LoadState) {
            binding.apply {
                footerProgressBar.isVisible = loadState is LoadState.Loading
                footerButtonRetry.isVisible = loadState !is LoadState.Loading
                footerTextViewError.isVisible = loadState !is LoadState.Loading
            }
        }
    }
}
