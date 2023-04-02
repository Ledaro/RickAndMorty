package com.example.rickandmorty.ui.favourites

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.rickandmorty.R
import com.example.rickandmorty.databinding.FragmentFavouritesBinding
import com.example.rickandmorty.model.Character
import com.example.rickandmorty.util.Constants
import com.google.android.material.snackbar.Snackbar
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

        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.favouritesCharactersEvent.collect { event ->
                when (event) {
                    is FavouritesViewModel.FavouritesCharactersEvent.ShowUndoDeleteCharacterMessage -> {
                        Snackbar.make(
                            requireView(),
                            getString(R.string.favourites_snackbar_delete_character_text),
                            Snackbar.LENGTH_SHORT
                        )
                            .setAction(getString(R.string.favourites_snackbar_action_undo_text)) {
                                viewModel.undoSwipeDeleteCharacter(event.character)
                            }.show()
                    }
                }
            }
        }

        viewModel.savedCharacters.observe(viewLifecycleOwner) {
            favouriteCharactersAdapter.submitList(it)
        }
    }

    private fun setupRecyclerView() {
        binding.apply {
            favouritesRecyclerView.adapter = favouriteCharactersAdapter
            favouritesRecyclerView.layoutManager =
                GridLayoutManager(requireContext(), Constants.GRID_SPAN_COUNT)
            favouritesRecyclerView.addItemDecoration(
                DividerItemDecoration(
                    context,
                    DividerItemDecoration.VERTICAL
                )
            )
            favouritesRecyclerView.setHasFixedSize(true)

            ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
                0,
                ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
            ) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val character =
                        favouriteCharactersAdapter.currentList[viewHolder.bindingAdapterPosition]
                    viewModel.swipeDeleteCharacter(character)
                }
            }).attachToRecyclerView(favouritesRecyclerView)
        }
    }

    override fun onItemClick(character: Character) {
        val action =
            FavouritesFragmentDirections.actionFavouritesFragmentToCharacterDetailFragment(character)
        findNavController().navigate(action)
    }
}
