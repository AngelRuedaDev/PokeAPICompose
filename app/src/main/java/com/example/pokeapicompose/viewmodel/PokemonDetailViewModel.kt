package com.example.pokeapicompose.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokeapicompose.data.model.Chain
import com.example.pokeapicompose.data.model.EvolutionChain
import com.example.pokeapicompose.data.model.PokemonDetail
import com.example.pokeapicompose.data.model.PokemonItem
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

    private val _evolutionUrl = MutableStateFlow<String?>(null)
    val evolutionUrl: StateFlow<String?> = _evolutionUrl

    private val _evolutionChain = MutableStateFlow<EvolutionChain?>(null)
    val evolutionChain: StateFlow<EvolutionChain?> = _evolutionChain

    private val _evolutionNames = MutableStateFlow<List<String>>(emptyList())
    val evolutionNames: StateFlow<List<String>> = _evolutionNames

    private val _evolutionItems = MutableStateFlow<List<PokemonItem>>(emptyList())
    val evolutionItems: StateFlow<List<PokemonItem>> = _evolutionItems


    fun fetchPokemon(id: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = repository.getPokemonDetail(id)
                _pokemon.value = response

                // Paso 1: obtener la URL de la cadena evolutiva
                val evolutionUrl = repository.getPokemonEvolutionUrl(id)
                _evolutionUrl.value = evolutionUrl

                // Paso 2: extraer el ID de la URL (por ejemplo "https://pokeapi.co/api/v2/evolution-chain/2/" → 2)
                val chainId = extractEvolutionChainId(evolutionUrl)

                // Paso 3: llamar a la API y guardar el resultado
                val chainResponse = repository.getPokemonEvolutionChain(chainId)
                _evolutionChain.value = chainResponse

                val names = getAllEvolutions(chainResponse.chain)
                _evolutionNames.value = names

                val items = names.map { name ->
                    val detail = repository.getPokemonByName(name)
                    PokemonItem(name = name, url = "https://pokeapi.co/api/v2/pokemon/${detail.id}")
                }

                _evolutionItems.value = items

            } catch (e: Exception) {
                _error.value = e.localizedMessage ?: "Unknown error"
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Función recursiva privada
    private fun getAllEvolutions(chain: Chain): List<String> {
        val evolutions = mutableListOf<String>()
        evolutions.add(chain.species.name)
        for (evolution in chain.evolvesTo) {
            evolutions.addAll(getAllEvolutions(evolution))
        }
        return evolutions
    }

    private fun extractEvolutionChainId(url: String): Int {
        // Ejemplo: https://pokeapi.co/api/v2/evolution-chain/2/
        return url.trimEnd('/').split("/").last().toInt()
    }
}