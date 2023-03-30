package com.example.rickandmorty.ui.characters.detail

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.rickandmorty.R
import com.example.rickandmorty.databinding.FragmentDetailBinding

class CharacterDetailFragment : Fragment(R.layout.fragment_detail) {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentDetailBinding.bind(view)
    }
}
