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
import com.example.pokeapicompose.data.navigation.AppNavigation
import com.example.pokeapicompose.di.PokemonDetailViewModelFactory
import com.example.pokeapicompose.ui.views.PokemonDetailScreen

class MainActivity : ComponentActivity() {
    //private val appContainer = AppContainer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        /*val viewModelFactory = PokemonViewModelFactory(appContainer.pokemonRepository)
        val viewModel = ViewModelProvider(this, viewModelFactory)
            .get(PokemonListViewModel::class.java)*/



        setContent {
            PokeApiComposeTheme {
                AppNavigation()
            }
        }
    }
}

