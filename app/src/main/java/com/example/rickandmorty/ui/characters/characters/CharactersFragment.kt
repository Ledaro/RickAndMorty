package com.example.rickandmorty.ui.characters.characters

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rickandmorty.R
import com.example.rickandmorty.data.models.Character
import com.example.rickandmorty.databinding.FragmentCharactersBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CharactersFragment : Fragment(R.layout.fragment_characters),
    CharactersAdapter.OnItemClickListener {

    private var _binding: FragmentCharactersBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CharactersViewModel by viewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentCharactersBinding.bind(view)

        val adapter = CharactersAdapter(this)

        binding.apply {
            charactersRecyclerView.setHasFixedSize(true)
            charactersRecyclerView.adapter = adapter.withLoadStateHeaderAndFooter(
                header = CharactersLoadStateAdapter { adapter.retry() },
                footer = CharactersLoadStateAdapter { adapter.retry() },
            )
            charactersRecyclerView.layoutManager = LinearLayoutManager(requireContext())
            charactersRecyclerView.itemAnimator = null
            charactersButtonRetry.setOnClickListener {
                adapter.retry()
            }
        }

        viewModel.characters.observe(viewLifecycleOwner) {
            adapter.submitData(viewLifecycleOwner.lifecycle, it)
        }

        adapter.addLoadStateListener { loadState ->
            binding.apply {
                charactersProgressBar.isVisible = loadState.source.refresh is LoadState.Loading
                charactersRecyclerView.isVisible = loadState.source.refresh is LoadState.NotLoading
                charactersButtonRetry.isVisible = loadState.source.refresh is LoadState.Error
                charactersTextViewError.isVisible = loadState.source.refresh is LoadState.Error

                if (loadState.source.refresh is LoadState.NotLoading && loadState.append.endOfPaginationReached && adapter.itemCount < 1) {
                    charactersRecyclerView.isVisible = false
                    charactersTextViewEmpty.isVisible = true
                } else {
                    charactersTextViewEmpty.isVisible = false
                }

            }
        }

        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        inflater.inflate(R.menu.menu_characters, menu)

        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    binding.charactersRecyclerView.scrollToPosition(0)
                    viewModel.searchCharacters(query)
                    searchView.clearFocus()
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onItemClick(character: Character) {
        val action =
            CharactersFragmentDirections.actionCharactersFragmentToCharacterDetailFragment(character)
        findNavController().navigate(action)
    }
}
