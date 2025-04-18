package com.example.pokeapicompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.rememberNavController
import com.example.pokeapicompose.di.AppContainer
import com.example.pokeapicompose.ui.theme.PokeApiComposeTheme
import com.example.pokeapicompose.ui.views.PokemonListScreen
import com.example.pokeapicompose.viewmodel.PokemonListViewModel
import com.example.pokeapicompose.viewmodel.PokemonDetailViewModel
import com.example.pokeapicompose.di.PokemonViewModelFactory
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.pokeapicompose.di.PokemonDetailViewModelFactory
import com.example.pokeapicompose.ui.views.PokemonDetailScreen

class MainActivity : ComponentActivity() {
    private val appContainer = AppContainer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val viewModelFactory = PokemonViewModelFactory(appContainer.pokemonRepository)
        val viewModel = ViewModelProvider(this, viewModelFactory)
            .get(PokemonListViewModel::class.java)



        setContent {
            PokeApiComposeTheme {
                AppNavigation(viewModel)
            }
        }
    }

    @Composable
    fun AppNavigation(viewModel: PokemonListViewModel){
        val navController = rememberNavController()

        NavHost(navController = navController, startDestination = "pokemon_list") {
            composable("pokemon_list") {
                PokemonListScreen(viewModel, navController)
            }

            composable(
                "pokemon_detail/{id}",
                arguments = listOf(navArgument("id") { type = NavType.IntType })
            ) { backStackEntry ->
                val id = backStackEntry.arguments?.getInt("id") ?: 1

                val detailViewModelFactory = PokemonDetailViewModelFactory(appContainer.pokemonRepository)
                val detailViewModel = ViewModelProvider(this@MainActivity, detailViewModelFactory)
                    .get("pokemon_detail_$id", PokemonDetailViewModel::class.java)

                // Nota: esto usa una "key" para no compartir instancia entre Pokémon

                PokemonDetailScreen(id = id, viewModel = detailViewModel)
            }
        }
    }
}

