package com.example.pokeapicompose.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.pokeapicompose.data.repository.PokemonRepository
import com.example.pokeapicompose.viewmodel.PokemonDetailViewModel

/**
 * Factory class to create an instance of the PokemonDetailViewModel.
 *
 * This class is responsible for providing the necessary dependencies (such as the repository)
 * to the ViewModel when it is created. The factory pattern is used here to allow the
 * creation of the ViewModel with parameters.
 */
class PokemonDetailViewModelFactory (
    private val repository: PokemonRepository
) : ViewModelProvider.Factory{

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        // Check if the requested ViewModel class is PokemonDetailViewModel
        if (modelClass.isAssignableFrom(PokemonDetailViewModel::class.java)) {
            // If it is, create and return the ViewModel with the repository injected.
            return PokemonDetailViewModel(repository) as T
        }
        // If the ViewModel class is not recognized, throw an exception.
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}