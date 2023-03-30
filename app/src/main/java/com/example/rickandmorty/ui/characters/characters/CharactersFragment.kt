package com.example.rickandmorty.ui.characters.characters

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rickandmorty.R
import com.example.rickandmorty.databinding.FragmentCharactersBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CharactersFragment : Fragment(R.layout.fragment_characters) {

    private var _binding: FragmentCharactersBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CharactersViewModel by viewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentCharactersBinding.bind(view)

        val adapter = CharactersAdapter()

        binding.apply {
            charactersRecyclerView.setHasFixedSize(true)
            charactersRecyclerView.adapter = adapter
            charactersRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        }

        viewModel.characters.observe(viewLifecycleOwner) {
            adapter.submitData(viewLifecycleOwner.lifecycle, it)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
