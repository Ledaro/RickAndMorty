package com.example.rickandmorty.ui.favourites

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rickandmorty.R
import com.example.rickandmorty.adapters.FavouriteCharactersAdapter
import com.example.rickandmorty.databinding.FragmentFavouritesBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavouritesFragment : Fragment(R.layout.fragment_favourites) {

    private var _binding: FragmentFavouritesBinding? = null
    private val binding get() = _binding!!
    lateinit var favouriteCharactersAdapter: FavouriteCharactersAdapter
    private val viewModel: FavouritesViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentFavouritesBinding.bind(view)

        setupRecyclerView()

        viewModel.getSavedCharacters().observe(viewLifecycleOwner, Observer { characters ->
            favouriteCharactersAdapter.differ.submitList(characters)
        })
    }

    private fun setupRecyclerView() {
        binding.apply {
            favouriteCharactersAdapter = FavouriteCharactersAdapter()
            favouritesRecyclerView.adapter = favouriteCharactersAdapter
            favouritesRecyclerView.layoutManager = LinearLayoutManager(activity)
            favouritesRecyclerView.setHasFixedSize(true)
        }
    }
}
