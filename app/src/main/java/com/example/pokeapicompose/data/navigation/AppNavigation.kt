package com.example.pokeapicompose.data.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
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

private val appContainer = AppContainer()

@Composable
fun AppNavigation(){

    //Se encarga de gestionar el estado de navegacion de las pantallas
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
            //Usar la ID y si es null por defecto usar 1
            val id = backStackEntry.arguments?.getInt("id") ?: 1

            val detailViewModel: PokemonDetailViewModel = viewModel(
                factory = PokemonDetailViewModelFactory(appContainer.pokemonRepository)
            )

            PokemonDetailScreen(id = id, viewModel = detailViewModel)
        }

    }

}