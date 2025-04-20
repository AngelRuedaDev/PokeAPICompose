package com.example.pokeapicompose.ui.views


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.pokeapicompose.data.model.PokemonItem
import com.example.pokeapicompose.data.model.PokemonListResponse
import com.example.pokeapicompose.data.navigation.AppScreens
import com.example.pokeapicompose.viewmodel.PokemonListViewModel

@Composable
fun PokemonListScreen(viewModel: PokemonListViewModel, navController: NavController) {
    val pokemonListState = viewModel.pokemonList.collectAsState()
    val isLoading = viewModel.isLoading.collectAsState()
    val error = viewModel.error.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(WindowInsets.systemBars.only(WindowInsetsSides.Top).asPaddingValues())
    ) {
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
                PokemonList(pokemonListState.value!!, navController)
            }
        }
    }
}

@Composable
fun Title(title: String) {
    Text(
        title,
        modifier = Modifier.fillMaxWidth().padding(start = 18.dp, top = 18.dp, bottom = 10.dp ),
        color = MaterialTheme.colorScheme.secondary,
        fontSize = 36.sp,
        fontWeight = FontWeight.Bold
    )
}

@Composable
fun PokemonList(pokemonListResponse: PokemonListResponse, navController: NavController) {
    Column() {
        Title("Pokemon List")
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(pokemonListResponse.results) { pokemon ->
                //Log.d("PKM", pokemon.url+" "+pokemon.name+" "+pokemon.id)
                PokemonListItem(pokemon = pokemon) {
                    // navegar a la pantalla de detalle
                    navController.navigate(route = AppScreens.PokemonDetailScreen.route + "/${pokemon.id}")
                }
            }
        }
    }

}


@Composable
fun PokemonListItem(
    pokemon: PokemonItem,
    onClick: () -> Unit
) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable { onClick() },
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 5.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = Color(0xFFFFFFFF),
            contentColor = MaterialTheme.colorScheme.secondary
        )
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
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start,
                fontSize = 20.sp,
                fontWeight = FontWeight.Normal
            )
        }
    }
}

@Composable
fun SearchBar(query: String, onQueryChanged: (String) -> Unit) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChanged,
        label = { Text("Search Pokémon") },
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        singleLine = true
    )
}

