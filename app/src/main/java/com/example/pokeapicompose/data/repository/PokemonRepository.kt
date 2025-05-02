package com.example.pokeapicompose.data.repository


import com.example.pokeapicompose.data.model.EvolutionChain
import com.example.pokeapicompose.data.model.PokemonDetail
import com.example.pokeapicompose.data.model.PokemonListResponse
import com.example.pokeapicompose.data.model.TypeInfo
import com.example.pokeapicompose.data.model.TypeListResponse
import com.example.pokeapicompose.data.remote.PokeApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Repository class responsible for handling the data operations related to Pokémon.
 * It interacts with the PokeApiService to fetch data from the API.
 */
class PokemonRepository(private val pokeApiService: PokeApiService) {

    /**
     * Fetches a list of Pokémon from the API with a limit and offset for pagination.
     */
    suspend fun getPokemonList(limit: Int = 20, offset: Int = 0): PokemonListResponse {
        return withContext(Dispatchers.IO) {
            pokeApiService.getPokemonList(limit, offset)
        }
    }

    /**
     * Fetches detailed information about a specific Pokémon by its ID.
     */
    suspend fun getPokemonDetail(id: Int): PokemonDetail {
        return withContext(Dispatchers.IO) {
            pokeApiService.getPokemonDetail(id)
        }
    }

    /**
     * Fetches detailed information about a Pokémon by its name.
     */
    suspend fun getPokemonByName(name: String): PokemonDetail {
        return withContext(Dispatchers.IO) {
            pokeApiService.getPokemonByName(name)
        }
    }

    /**
     * Fetches the URL of the Pokémon's evolution chain using its ID.
     */
    suspend fun getPokemonEvolutionUrl(id: Int): String {
        return withContext(Dispatchers.IO) {
            pokeApiService.getPokemonEvolutionUrl(id).evolutionChain.url
        }
    }

    /**
     * Fetches the evolution chain details for a Pokémon using the chain ID.
     */
    suspend fun getPokemonEvolutionChain(chainId: Int): EvolutionChain {
        return withContext(Dispatchers.IO) {
            pokeApiService.getEvolutionChain(chainId)
        }
    }

    /**
     * Fetches a list of Pokémon types from the API with a limit and offset for pagination.
     */
    suspend fun getTypesList(limit: Int = 50, offset: Int = 0): TypeListResponse {
        return withContext(Dispatchers.IO) {
            pokeApiService.getTypesList(limit, offset)
        }
    }

    /**
     * Fetches detailed information about Pokémon of a specific type.
     */
    suspend fun getPokemonsByType(typeName: String): TypeInfo {
        return withContext(Dispatchers.IO) {
            pokeApiService.getTypeDetail(typeName)
        }
    }
}