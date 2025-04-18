package com.example.pokeapicompose.data.remote

import com.example.pokeapicompose.data.model.PokemonDetail
import com.example.pokeapicompose.data.model.PokemonListResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokeApiService {

    // Llama a: https://pokeapi.co/api/v2/pokemon?limit=20
    @GET("pokemon")
    suspend fun getPokemonList(
        @Query("limit") limit: Int = 20,
        @Query("offset") offset: Int = 0
    ): PokemonListResponse

    // Llama a: https://pokeapi.co/api/v2/pokemon/{id}
    @GET("pokemon/{id}")
    suspend fun getPokemonDetail(
        @Path("id") id: Int
    ): PokemonDetail
}