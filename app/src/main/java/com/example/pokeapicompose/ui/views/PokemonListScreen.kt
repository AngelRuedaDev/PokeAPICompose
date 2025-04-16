package com.example.pokeapicompose.ui.views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.pokeapicompose.data.model.PokemonItem
import com.example.pokeapicompose.data.model.PokemonListResponse
import com.example.pokeapicompose.viewmodel.PokemonListViewModel

@Composable
fun PokemonListScreen(viewModel : PokemonListViewModel) {
    val pokemonListState = viewModel.pokemonList.collectAsState()
    val isLoading = viewModel.isLoading.collectAsState()
    val error = viewModel.error.collectAsState()

    Box(modifier = Modifier.fillMaxSize().padding(WindowInsets.systemBars.only(WindowInsetsSides.Top).asPaddingValues())) {

        when {
            isLoading.value -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }

            error.value != null -> {
                Text(
                    text = error.value ?: "Error desconocido",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            pokemonListState.value != null -> {
                PokemonList(pokemonListState.value!!)
            }
        }
    }
}

@Composable
fun PokemonList(pokemonListResponse: PokemonListResponse) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(pokemonListResponse.results) { pokemon ->
            PokemonListItem(pokemon = pokemon) {
                // navegar a la pantalla de detalle
            }
        }
    }
}


@Composable
fun PokemonListItem(pokemon: PokemonItem, onClick: () -> Unit) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable { onClick() },
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(16.dp)
        ) {
            AsyncImage(
                model = pokemon.spriteUrl,
                contentDescription = pokemon.name,
                modifier = Modifier
                    .size(90.dp)
                    .padding(end = 16.dp)
            )

            Text(
                text = pokemon.name.replaceFirstChar { it.uppercase() },
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start
            )
        }
    }
}

