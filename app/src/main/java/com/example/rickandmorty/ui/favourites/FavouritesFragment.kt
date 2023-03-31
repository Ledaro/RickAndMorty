package com.example.rickandmorty.ui.favourites

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rickandmorty.R
import com.example.rickandmorty.databinding.FragmentFavouritesBinding
import com.example.rickandmorty.model.Character
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavouritesFragment : Fragment(R.layout.fragment_favourites),
    FavouriteCharactersAdapter.OnItemClickListener {

    private var _binding: FragmentFavouritesBinding? = null
    private val binding get() = _binding!!
    private lateinit var favouriteCharactersAdapter: FavouriteCharactersAdapter
    private val viewModel: FavouritesViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentFavouritesBinding.bind(view)
        favouriteCharactersAdapter = FavouriteCharactersAdapter(this)

        setupRecyclerView()

        viewModel.savedCharacters.observe(viewLifecycleOwner) {
            favouriteCharactersAdapter.submitList(it)
        }
    }

    private fun setupRecyclerView() {
        binding.apply {
            favouritesRecyclerView.adapter = favouriteCharactersAdapter
            favouritesRecyclerView.layoutManager = LinearLayoutManager(activity)
            favouritesRecyclerView.setHasFixedSize(true)
        }
    }

    override fun onItemClick(character: Character) {
        val action =
            FavouritesFragmentDirections.actionFavouritesFragmentToCharacterDetailFragment(character)
        findNavController().navigate(action)
    }
}
