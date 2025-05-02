package com.example.pokeapicompose.data.remote

import com.example.pokeapicompose.data.model.EvolutionChain
import com.example.pokeapicompose.data.model.PokemonDetail
import com.example.pokeapicompose.data.model.PokemonListResponse
import com.example.pokeapicompose.data.model.PokemonSpecie
import com.example.pokeapicompose.data.model.TypeInfo
import com.example.pokeapicompose.data.model.TypeListResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Interface representing the PokeAPI service.
 * This interface defines the network calls to fetch data from the Pokémon API.
 */
interface PokeApiService {

    // Calls: https://pokeapi.co/api/v2/pokemon?limit=20
    @GET("pokemon")
    suspend fun getPokemonList(
        @Query("limit") limit: Int = 20,
        @Query("offset") offset: Int = 0
    ): PokemonListResponse

    // Calls: https://pokeapi.co/api/v2/pokemon/{id}
    @GET("pokemon/{id}")
    suspend fun getPokemonDetail(
        @Path("id") id: Int
    ): PokemonDetail

    // Calls: https://pokeapi.co/api/v2/pokemon/{name}
    @GET("pokemon/{name}")
    suspend fun getPokemonByName(
        @Path("name") name: String
    ): PokemonDetail

    // Calls: https://pokeapi.co/api/v2/pokemon-species/{id}
    @GET("pokemon-species/{id}")
    suspend fun getPokemonEvolutionUrl(
        @Path("id") id: Int
    ): PokemonSpecie

    // Calls: https://pokeapi.co/api/v2/evolution-chain/{id}
    @GET("evolution-chain/{id}/")
    suspend fun getEvolutionChain(@Path("id") id: Int): EvolutionChain

    // Calls: https://pokeapi.co/api/v2/type?limit=50
    @GET("type")
    suspend fun getTypesList(
        @Query("limit") limit: Int = 50,
        @Query("offset") offset: Int = 0
    ): TypeListResponse

    // Calls: https://pokeapi.co/api/v2/type/{name}
    @GET("type/{typeName}")
    suspend fun getTypeDetail(@Path("typeName") typeName: String): TypeInfo
}