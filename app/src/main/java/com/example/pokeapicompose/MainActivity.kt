package com.example.pokeapicompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.ViewModelProvider
import com.example.pokeapicompose.di.AppContainer
import com.example.pokeapicompose.ui.theme.PokeApiComposeTheme
import com.example.pokeapicompose.ui.views.PokemonListScreen
import com.example.pokeapicompose.viewmodel.PokemonListViewModel
import com.example.pokeapicompose.di.PokemonViewModelFactory

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
                PokemonListScreen(viewModel)
            }
        }
    }
}
