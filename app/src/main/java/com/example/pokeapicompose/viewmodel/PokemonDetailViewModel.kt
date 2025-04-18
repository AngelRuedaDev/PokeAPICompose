package com.example.pokeapicompose.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokeapicompose.data.model.PokemonDetail
import com.example.pokeapicompose.data.model.PokemonListResponse
import com.example.pokeapicompose.data.repository.PokemonRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PokemonDetailViewModel(private val repository: PokemonRepository): ViewModel() {
    private val _pokemon = MutableStateFlow<PokemonDetail?>(null)
    val pokemon: StateFlow<PokemonDetail?> = _pokemon

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun fetchPokemon(id: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = repository.getPokemonDetail(id)
                _pokemon.value = response
            } catch (e: Exception) {
                _error.value = e.localizedMessage ?: "Unknown error"
            } finally {
                _isLoading.value = false
            }
        }
    }
}