package com.example.rickandmorty.ui.characters.characters

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rickandmorty.R
import com.example.rickandmorty.databinding.FragmentCharactersBinding
import com.example.rickandmorty.model.Character
import com.example.rickandmorty.util.onQueryTextChanged
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CharactersFragment : Fragment(R.layout.fragment_characters),
    CharactersAdapter.OnItemClickListener {

    private var _binding: FragmentCharactersBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CharactersViewModel by viewModels()
    private lateinit var charactersAdapter: CharactersAdapter


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentCharactersBinding.bind(view)
        charactersAdapter = CharactersAdapter(this)

        setupRecyclerView()

        viewModel.characters.observe(viewLifecycleOwner) {
            charactersAdapter.submitData(viewLifecycleOwner.lifecycle, it)
        }

        charactersAdapter.addLoadStateListener { loadState ->
            handleLoadStateListener(loadState)
        }

        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_characters, menu)

        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView

        searchView.onQueryTextChanged {
            viewModel.searchQuery.value = it
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_toggle_alive -> {
                item.isChecked = !item.isChecked
                viewModel.characterStatus.value = CharacterStatus.ALIVE
                true
            }
            R.id.action_toggle_dead -> {
                item.isChecked = !item.isChecked
                viewModel.characterStatus.value = CharacterStatus.DEAD
                true
            }
            R.id.action_toggle_all -> {
                item.isChecked = !item.isChecked
                viewModel.characterStatus.value = CharacterStatus.ALL
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    override fun onItemClick(character: Character) {
        val action =
            CharactersFragmentDirections.actionCharactersFragmentToCharacterDetailFragment(character)
        findNavController().navigate(action)
    }

    private fun handleLoadStateListener(loadState: CombinedLoadStates) {
        binding.apply {
            charactersProgressBar.isVisible = loadState.source.refresh is LoadState.Loading
            charactersRecyclerView.isVisible = loadState.source.refresh is LoadState.NotLoading
            charactersButtonRetry.isVisible = loadState.source.refresh is LoadState.Error
            charactersTextViewError.isVisible = loadState.source.refresh is LoadState.Error

            if (loadState.source.refresh is LoadState.NotLoading && loadState.append.endOfPaginationReached && charactersAdapter.itemCount < 1) {
                charactersRecyclerView.isVisible = false
                charactersTextViewEmpty.isVisible = true
            } else {
                charactersTextViewEmpty.isVisible = false
            }
        }
    }

    private fun setupRecyclerView() {
        binding.apply {
            charactersRecyclerView.setHasFixedSize(true)
            charactersRecyclerView.adapter = charactersAdapter.withLoadStateHeaderAndFooter(
                header = CharactersLoadStateAdapter { charactersAdapter.retry() },
                footer = CharactersLoadStateAdapter { charactersAdapter.retry() },
            )
            charactersRecyclerView.layoutManager = LinearLayoutManager(requireContext())
            charactersRecyclerView.itemAnimator = null
            charactersButtonRetry.setOnClickListener {
                charactersAdapter.retry()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
