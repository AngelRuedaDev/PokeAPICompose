package com.example.pokeapicompose.data.repository

import com.example.pokeapicompose.data.model.PokemonDetail
import com.example.pokeapicompose.data.model.PokemonListResponse
import com.example.pokeapicompose.data.remote.PokeApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PokemonRepository(private val pokeApiService: PokeApiService) {

    suspend fun getPokemonList(limit: Int = 20, offset: Int = 0): PokemonListResponse {
        return withContext(Dispatchers.IO) {
            pokeApiService.getPokemonList(limit, offset)
        }
    }

    suspend fun getPokemonDetail(name: String): PokemonDetail {
        return withContext(Dispatchers.IO) {
            pokeApiService.getPokemonDetail(name)
        }
    }
}