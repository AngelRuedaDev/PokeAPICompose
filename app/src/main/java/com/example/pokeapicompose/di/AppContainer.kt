package com.example.pokeapicompose.di

import com.example.pokeapicompose.data.remote.PokeApiService
import com.example.pokeapicompose.data.repository.PokemonRepository
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * AppContainer is a class responsible for setting up and providing the necessary components
 * for the application, such as the Retrofit instance and the repository.
 *
 * It initializes Retrofit with a base URL for the Pokémon API and a Gson converter to parse the API responses.
 */
class AppContainer {
    // Initialize Retrofit with the base URL and the Gson converter to handle JSON responses.
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://pokeapi.co/api/v2/") // Base URL of the Pokémon API.
        .addConverterFactory(GsonConverterFactory.create()) // Adds Gson converter to Retrofit.
        .build() // Build the Retrofit instance.

    // Create an instance of the PokeApiService using Retrofit.
    private val apiService = retrofit.create(PokeApiService::class.java)

    // Instantiate the PokemonRepository, passing in the apiService to fetch data from the Pokémon API.
    val pokemonRepository = PokemonRepository(apiService)
}