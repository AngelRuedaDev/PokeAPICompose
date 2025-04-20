package com.example.pokeapicompose.data.navigation

sealed class AppScreens(val route: String) {
    object PokemonListScreen: AppScreens("pokemon_list_screen")
    object PokemonDetailScreen: AppScreens("pokemon_detail_screen")
}