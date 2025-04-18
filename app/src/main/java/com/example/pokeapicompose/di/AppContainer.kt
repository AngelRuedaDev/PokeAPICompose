package com.example.pokeapicompose.di

import com.example.pokeapicompose.data.remote.PokeApiService
import com.example.pokeapicompose.data.repository.PokemonRepository
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AppContainer {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://pokeapi.co/api/v2/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val apiService = retrofit.create(PokeApiService::class.java)

    val pokemonRepository = PokemonRepository(apiService)
}