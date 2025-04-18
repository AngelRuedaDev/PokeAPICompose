package com.example.pokeapicompose.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.pokeapicompose.data.repository.PokemonRepository
import com.example.pokeapicompose.viewmodel.PokemonDetailViewModel
import com.example.pokeapicompose.viewmodel.PokemonListViewModel

class PokemonDetailViewModelFactory (
    private val repository: PokemonRepository
) : ViewModelProvider.Factory{

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PokemonDetailViewModel::class.java)) {
            return PokemonDetailViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}