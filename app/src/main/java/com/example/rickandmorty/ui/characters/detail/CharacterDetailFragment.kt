package com.example.rickandmorty.ui.characters.detail

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.rickandmorty.R
import com.example.rickandmorty.databinding.FragmentDetailBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CharacterDetailFragment : Fragment(R.layout.fragment_detail) {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!
    private val args by navArgs<CharacterDetailFragmentArgs>()
    private val viewModel: CharacterDetailViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentDetailBinding.bind(view)

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.characterDetailEvent.collect { event ->
                when (event) {
                    is CharacterDetailViewModel.CharacterDetailEvent.ShowSaveCharacterMessage -> {
                        Snackbar.make(requireView(), "Character saved successfully", Snackbar.LENGTH_LONG)
                            .show()
                    }
                }
            }
        }


        binding.apply {
            val character = args.character

            (requireActivity() as AppCompatActivity).supportActionBar?.title = character.name

            Glide.with(this@CharacterDetailFragment)
                .load(character.image)
                .error(R.drawable.ic_error)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        detailProgressBar.isVisible = false
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        detailProgressBar.isVisible = false
                        detailSaveCharacterButton.isVisible = true
                        return false
                    }
                })
                .into(detailImageView)

            detailSaveCharacterButton.setOnClickListener {
                viewModel.saveCharacter(character)
            }
        }
    }
}
