package com.example.pokeapicompose.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokeapicompose.data.model.Chain
import com.example.pokeapicompose.data.model.EvolutionChain
import com.example.pokeapicompose.data.model.PokemonDetail
import com.example.pokeapicompose.data.model.PokemonItem
import com.example.pokeapicompose.data.repository.PokemonRepository
import kotlinx.coroutines.delay
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

                // Step 1: get the URL of the evolution chain
                val evolutionUrl = repository.getPokemonEvolutionUrl(id)
                _evolutionUrl.value = evolutionUrl

                // Step 2: extract the ID from the URL
                val chainId = extractEvolutionChainId(evolutionUrl)

                // Step 3: call the API and store the result
                val chainResponse = repository.getPokemonEvolutionChain(chainId)
                _evolutionChain.value = chainResponse

                // Step 4: get the names of the pokemon evolutions
                val names = getAllEvolutions(chainResponse.chain)
                _evolutionNames.value = names

                // Step 5: call the api to get the pokemon data an create the pokemon item
                val items = names.map { name ->
                    val detail = repository.getPokemonByName(name)
                    PokemonItem(name = name, url = "https://pokeapi.co/api/v2/pokemon/${detail.id}")
                }

                _evolutionItems.value = items

            } catch (e: Exception) {
                _error.value = e.localizedMessage ?: "Unknown error"
            } finally {
                delay(2000)
                _isLoading.value = false
            }
        }
    }

    /* This recursive function traverses the evolution chain of a Pokémon
    and collects the names of all Pokémon in the chain, including all evolution stages.*/
    private fun getAllEvolutions(chain: Chain): List<String> {
        val evolutions = mutableListOf<String>()
        evolutions.add(chain.species.name)
        for (evolution in chain.evolvesTo) {
            evolutions.addAll(getAllEvolutions(evolution))
        }
        return evolutions
    }

    /* This function extracts the numeric ID from an evolution chain URL.
     For example, given: "https://pokeapi.co/api/v2/evolution-chain/1/"
     it returns: 1*/
    private fun extractEvolutionChainId(url: String): Int {
        return url.trimEnd('/').split("/").last().toInt()
    }
}