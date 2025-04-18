package com.example.pokeapicompose.ui.views

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.pokeapicompose.viewmodel.PokemonDetailViewModel
import androidx.compose.ui.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

@Composable
fun PokemonDetailScreen(id: Int, viewModel: PokemonDetailViewModel) {
    val pokemon by viewModel.pokemon.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    LaunchedEffect(id) {
        viewModel.fetchPokemon(id)
    }

    Box(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        when {
            isLoading -> CircularProgressIndicator(Modifier.align(Alignment.Center))
            error != null -> Text(error ?: "Error", Modifier.align(Alignment.Center))
            pokemon != null -> {
                Column {
                    Text("Name: ${pokemon!!.name}", fontSize = 24.sp)
                    Text("Weight: ${pokemon!!.weight}")
                    Text("Types: ${pokemon!!.types.joinToString(", ") { it.type.name }}")
                    AsyncImage(
                        model = pokemon!!.spriteUrl,
                        contentDescription = pokemon!!.name
                    )

                    AsyncImage(
                        model = pokemon!!.spriteShinyUrl,
                        contentDescription = "Pokemon Image"
                    )
                }
            }
        }
    }
}