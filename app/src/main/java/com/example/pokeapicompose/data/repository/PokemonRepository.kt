package com.example.pokeapicompose.data.repository


import com.example.pokeapicompose.data.model.EvolutionChain
import com.example.pokeapicompose.data.model.PokemonDetail
import com.example.pokeapicompose.data.model.PokemonListResponse
import com.example.pokeapicompose.data.model.PokemonSpecie
import com.example.pokeapicompose.data.model.TypeInfo
import com.example.pokeapicompose.data.model.TypeListResponse
import com.example.pokeapicompose.data.remote.PokeApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PokemonRepository(private val pokeApiService: PokeApiService) {

    suspend fun getPokemonList(limit: Int = 20, offset: Int = 0): PokemonListResponse {
        return withContext(Dispatchers.IO) {
            pokeApiService.getPokemonList(limit, offset)
        }
    }

    suspend fun getPokemonDetail(id: Int): PokemonDetail {
        return withContext(Dispatchers.IO) {
            pokeApiService.getPokemonDetail(id)
        }
    }

    suspend fun getPokemonByName(name: String): PokemonDetail {
        return withContext(Dispatchers.IO) {
            pokeApiService.getPokemonByName(name)
        }
    }

    suspend fun getPokemonEvolutionUrl(id: Int): String {
        return withContext(Dispatchers.IO) {
            pokeApiService.getPokemonEvolutionUrl(id).evolutionChain.url
        }
    }

    suspend fun getPokemonEvolutionChain(chainId: Int): EvolutionChain {
        return withContext(Dispatchers.IO) {
            pokeApiService.getEvolutionChain(chainId)
        }
    }

    suspend fun getTypesList(limit: Int = 50, offset: Int = 0): TypeListResponse {
        return withContext(Dispatchers.IO) {
            pokeApiService.getTypesList(limit, offset)
        }
    }

    suspend fun getPokemonsByType(typeName: String): TypeInfo {
        return withContext(Dispatchers.IO) {
            pokeApiService.getTypeDetail(typeName)
        }
    }
}