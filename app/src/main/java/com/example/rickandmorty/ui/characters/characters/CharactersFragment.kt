package com.example.rickandmorty.ui.characters.characters

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
    private lateinit var searchView: SearchView


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentCharactersBinding.bind(view)
        charactersAdapter = CharactersAdapter(this)
        charactersAdapter.stateRestorationPolicy =
            RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY

        setupRecyclerView()

        viewModel.characters.observe(viewLifecycleOwner) {
            charactersAdapter.submitData(viewLifecycleOwner.lifecycle, it)
        }

        charactersAdapter.addLoadStateListener { loadState ->
            handleLoadStateListener(loadState)
        }

        (requireActivity() as MenuHost).addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_characters, menu)

                val searchItem = menu.findItem(R.id.action_search)
                searchView = searchItem.actionView as SearchView

                val pendingQuery = viewModel.searchQuery.value
                if (pendingQuery != null && pendingQuery.isNotEmpty()) {
                    searchItem.expandActionView()
                    searchView.setQuery(pendingQuery, false)
                }

                searchView.onQueryTextChanged {
                    if (it.isNotEmpty()) {
                        binding.charactersRecyclerView.scrollToPosition(0)
                        viewModel.searchQuery.value = it
                    }
                }

                val menuItem = menu.findItem(R.id.action_search)
                menuItem.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
                    override fun onMenuItemActionExpand(item: MenuItem?): Boolean {
                        return true
                    }

                    override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
                        binding.charactersRecyclerView.scrollToPosition(0)
                        viewModel.searchQuery.value = ""
                        viewModel.characterStatus.value = CharacterStatus.ALL
                        return true
                    }
                })
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.action_toggle_alive -> {
                        menuItem.isChecked = !menuItem.isChecked
                        viewModel.characterStatus.value = CharacterStatus.ALIVE
                        binding.charactersRecyclerView.scrollToPosition(0)
                        true
                    }
                    R.id.action_toggle_dead -> {
                        menuItem.isChecked = !menuItem.isChecked
                        viewModel.characterStatus.value = CharacterStatus.DEAD
                        binding.charactersRecyclerView.scrollToPosition(0)
                        true
                    }
                    R.id.action_toggle_all -> {
                        menuItem.isChecked = !menuItem.isChecked
                        viewModel.characterStatus.value = CharacterStatus.ALL
                        binding.charactersRecyclerView.scrollToPosition(0)
                        true
                    }
                    else -> return false
                }
            }
        }, viewLifecycleOwner)
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
            charactersRecyclerView.addItemDecoration(
                DividerItemDecoration(
                    context,
                    DividerItemDecoration.VERTICAL
                )
            )
            charactersButtonRetry.setOnClickListener {
                charactersAdapter.retry()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        searchView.setOnQueryTextListener(null)
    }
}
