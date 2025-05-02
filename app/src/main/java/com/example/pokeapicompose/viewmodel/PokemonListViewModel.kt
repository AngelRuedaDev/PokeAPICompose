package com.example.pokeapicompose.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokeapicompose.data.model.PokemonItem
import com.example.pokeapicompose.data.model.PokemonListResponse
import com.example.pokeapicompose.data.model.TypeListResponse
import com.example.pokeapicompose.data.repository.PokemonRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PokemonListViewModel(private val repository: PokemonRepository) : ViewModel() {
    private val _pokemonList = MutableStateFlow<PokemonListResponse?>(null)
    private val pokemonList: StateFlow<PokemonListResponse?> = _pokemonList

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _typesList = MutableStateFlow<TypeListResponse?>(null)
    val typesList: StateFlow<TypeListResponse?> = _typesList

    private val _selectedType = MutableStateFlow<String?>(null)
    val selectedType: StateFlow<String?> = _selectedType.asStateFlow()

    // Combines the Pokémon list and search query to produce a filtered list
    val pokemonFilteredList: StateFlow<PokemonListResponse> = combine(
        pokemonList,
        searchQuery
    ) { list, query ->
        filterPokemonList(list, query)
    }.stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        PokemonListResponse(0, null, null, emptyList())
    )

    // Initial data loading
    init {
        fetchPokemonList()
        fetchTypesList()
    }

    // Fetches the full list of Pokémon from the repository
    private fun fetchPokemonList() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = repository.getPokemonList(limit = 100000)
                val filteredList = filterToNotShow(response.results)
                _pokemonList.value = PokemonListResponse(response.count, response.next, response.previous, filteredList)
            } catch (e: Exception) {
                _error.value = e.localizedMessage ?: "Unknown error"
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Fetches the list of Pokémon types, excluding unknown, stellar, and shadow
    private fun fetchTypesList() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = repository.getTypesList()
                _typesList.value = TypeListResponse(response.results.dropLast(3)) //delete unknown, stellar and shadow types
            } catch (e: Exception) {
                _error.value = e.localizedMessage ?: "Unknown error"
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Fetches a list of Pokémon filtered by type
    private fun searchPokemonByType(pokemonType: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = repository.getPokemonsByType(pokemonType)
                val pokemonItems = response.pokemon.map { it.pokemon }
                _pokemonList.value = PokemonListResponse(
                    count = pokemonItems.size,
                    next = null,
                    previous = null,
                    results = pokemonItems
                )
            } catch (e: Exception) {
                _error.value = e.localizedMessage ?: "Unknown error"
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Updates the search query based on user input
    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    // Handles the logic when a type is selected or cleared
    fun onTypeSelected(type: String?) {
        _selectedType.value = type
        if (type == null) {
            fetchPokemonList()
        } else {
            searchPokemonByType(type)
        }
    }

    // Filters Pokémon list based on search query
    private fun filterPokemonList(list: PokemonListResponse?, query: String): PokemonListResponse {
        if (list == null) return PokemonListResponse(0, null, null, emptyList())
        if (query.isBlank()) return list

        val filtered = list.results.filter {
            it.name.contains(query.trim(), ignoreCase = true)
        }

        return list.copy(results = filtered)
    }

    // Filters out Pokémon names containing "="
    private fun filterToNotShow(pokemonList: List<PokemonItem>): List<PokemonItem> {
        return pokemonList.filterNot { it.name.contains("-", ignoreCase = true) }
    }
}