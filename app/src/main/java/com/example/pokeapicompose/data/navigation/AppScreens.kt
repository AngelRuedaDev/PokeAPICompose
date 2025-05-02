package com.example.pokeapicompose.data.navigation

/**
 * A sealed class that represents different screens in the app.
 * Each screen is associated with a unique route that can be used for navigation.
 */
sealed class AppScreens(val route: String) {
    data object PokemonListScreen: AppScreens("pokemon_list_screen")
    data object PokemonDetailScreen: AppScreens("pokemon_detail_screen")
}