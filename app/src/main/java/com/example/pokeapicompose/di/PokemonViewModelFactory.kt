package com.example.pokeapicompose.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.pokeapicompose.data.repository.PokemonRepository
import com.example.pokeapicompose.viewmodel.PokemonListViewModel

/**
 * Factory class to create an instance of the PokemonListViewModel.
 *
 * This class provides the necessary dependencies (such as the repository)
 * to the ViewModel when it is created, following the factory design pattern.
 */
class PokemonViewModelFactory(
    private val repository: PokemonRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        // Check if the requested ViewModel class is PokemonListViewModel.
        if (modelClass.isAssignableFrom(PokemonListViewModel::class.java)) {
            // If it is, create and return the ViewModel with the repository injected.
            return PokemonListViewModel(repository) as T
        }
        // If the ViewModel class is not recognized, throw an exception.
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}