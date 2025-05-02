package com.example.pokeapicompose.data.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.pokeapicompose.di.AppContainer
import com.example.pokeapicompose.di.PokemonDetailViewModelFactory
import com.example.pokeapicompose.di.PokemonViewModelFactory
import com.example.pokeapicompose.ui.views.PokemonDetailScreen
import com.example.pokeapicompose.ui.views.PokemonListScreen
import com.example.pokeapicompose.viewmodel.PokemonDetailViewModel
import com.example.pokeapicompose.viewmodel.PokemonListViewModel

// Create an instance of the AppContainer to access the repository
private val appContainer = AppContainer()

/**
 * Manages the navigation between screens in the app.
 * The NavHost is set up to navigate between the PokémonList and detail screens.
 */
@Composable
fun AppNavigation(){

    // Manages the navigation state of the screens.
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = AppScreens.PokemonListScreen.route) {
        composable(route = AppScreens.PokemonListScreen.route) {
            val viewModel: PokemonListViewModel = viewModel(
                factory = PokemonViewModelFactory(appContainer.pokemonRepository)
            )
            PokemonListScreen(viewModel, navController)
        }
        composable(route = AppScreens.PokemonDetailScreen.route +"/{id}",
            arguments = listOf(navArgument(name = "id") {type = NavType.IntType}))
        { backStackEntry ->
            // Retrieves the 'id' argument from the back stack entry. Defaults to 1 if null.
            val id = backStackEntry.arguments?.getInt("id") ?: 1

            val detailViewModel: PokemonDetailViewModel = viewModel(
                factory = PokemonDetailViewModelFactory(appContainer.pokemonRepository)
            )

            PokemonDetailScreen(id = id, viewModel = detailViewModel)
        }

    }

}