package com.example.rickandmorty.ui.characters.characters

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rickandmorty.R
import com.example.rickandmorty.databinding.FragmentCharactersBinding
import com.example.rickandmorty.model.Character
import com.example.rickandmorty.util.Constants.Companion.GRID_SPAN_COUNT
import com.example.rickandmorty.util.onQueryTextChanged
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

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

        val toolbar = binding.charactersToolbar
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)
        (requireActivity() as MenuHost).addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.characters_menu, menu)

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

                viewLifecycleOwner.lifecycleScope.launch {
                    viewModel.preferencesFlow.first().let { preferences ->
                        menu.findItem(R.id.action_toggle_alive).isChecked = preferences.isAlive
                        menu.findItem(R.id.action_toggle_dead).isChecked = preferences.isDead
                    }
                }
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.action_toggle_alive -> {
                        handleAliveToggle(menuItem)
                        true
                    }
                    R.id.action_toggle_dead -> {
                        handleDeadToggle(menuItem)
                        true
                    }
                    else -> return false
                }
            }
        }, viewLifecycleOwner)
    }

    private fun handleAliveToggle(menuItem: MenuItem): Boolean {
        menuItem.isChecked = !menuItem.isChecked
        viewModel.onAliveToggle(menuItem.isChecked)
        binding.charactersRecyclerView.scrollToPosition(0)
        return true
    }

    private fun handleDeadToggle(menuItem: MenuItem): Boolean {
        menuItem.isChecked = !menuItem.isChecked
        viewModel.onDeadToggle(menuItem.isChecked)
        binding.charactersRecyclerView.scrollToPosition(0)
        return true
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
            charactersRecyclerView.layoutManager =
                GridLayoutManager(requireContext(), GRID_SPAN_COUNT)
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
