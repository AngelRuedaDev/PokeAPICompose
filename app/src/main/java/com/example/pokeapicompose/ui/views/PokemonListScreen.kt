package com.example.pokeapicompose.ui.views

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.pokeapicompose.R
import com.example.pokeapicompose.data.model.PokemonItem
import com.example.pokeapicompose.data.model.PokemonListResponse
import com.example.pokeapicompose.data.model.TypeListResponse
import com.example.pokeapicompose.data.navigation.AppScreens
import com.example.pokeapicompose.viewmodel.PokemonListViewModel
import androidx.compose.runtime.remember as remember

@Composable
fun PokemonListScreen(viewModel: PokemonListViewModel, navController: NavController) {
    val pokemonFiltered = viewModel.pokemonFilteredList.collectAsState()
    val isLoading = viewModel.isLoading.collectAsState()
    val error = viewModel.error.collectAsState()
    val searchQuery = viewModel.searchQuery.collectAsState()
    val typesList = viewModel.typesList.collectAsState()

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

            else -> {
                Column {
                    /*SearchBar(
                        query = searchQuery.value,
                        onQueryChanged = { viewModel.onSearchQueryChanged(it) }
                    )*/

                    SearchSection(
                        searchQuery.value,
                        { viewModel.onSearchQueryChanged(it) },
                        typesList.value,
                        onTypeSelected = { selectedType ->
                            viewModel.searchPokemonByType(selectedType)
                        })

                    PokemonList(pokemonFiltered.value, navController)
                }
            }
        }
    }
}

@Composable
fun Title(title: String) {
    Text(
        title,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 18.dp, top = 18.dp, bottom = 10.dp),
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
fun SearchSection(
    searchQuery: String,
    onQueryChanged: (String) -> Unit,
    pokemonTypesList: TypeListResponse?,
    onTypeSelected: (String) -> Unit
) {
    val openDialog = remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, top = 8.dp, bottom = 8.dp, end = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        SearchBar(
            query = searchQuery,
            onQueryChanged = { onQueryChanged(it) },
            modifier = Modifier.weight(1f) //ocupa el espacio disponible
        )
        FilterButton { openDialog.value = true }

        // Aquí mostramos el dialog si openDialog es true
        if (openDialog.value) {

            TypesDialog(
                onDismissRequest = { openDialog.value = false },
                onTypeSelected = { selectedType ->
                    openDialog.value = false
                    //Log.d("TYPE", selectedType)
                    onTypeSelected(selectedType)
                },
                pokemonTypesList = pokemonTypesList
            )
        }

    }
}

@Composable
fun FilterButton(onClick: () -> Unit) {
    IconButton(onClick = onClick) {
        Icon(
            painter = painterResource(R.drawable.ic_filter),
            contentDescription = "Filter Icon",
            tint = MaterialTheme.colorScheme.secondary
        )
    }
}

@Composable
fun TypesDialog(
    onDismissRequest: () -> Unit,
    onTypeSelected: (String) -> Unit,
    pokemonTypesList: TypeListResponse?,

    ) {
    // Si la lista es nula o vacía, no mostramos nada
    if (pokemonTypesList == null || pokemonTypesList.results.isEmpty()) {
        return
    }

    val selectedType = remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = { onDismissRequest() },
        title = { Text(text = "Selecciona un tipo") },
        text = {
            LazyColumn {
                items(pokemonTypesList.results) { type ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { selectedType.value = type.name }
                            .padding(vertical = 4.dp, horizontal = 8.dp)
                    ) {
                        RadioButton(
                            selected = (type.name == selectedType.value),
                            onClick = { selectedType.value = type.name }
                        )
                        Text(
                            text = type.name.replaceFirstChar { it.uppercase() },
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = { onTypeSelected(selectedType.value) }
            ) {
                Text("Confirmar")
            }
        },
        dismissButton = {
            TextButton(
                onClick = { onDismissRequest() }
            ) {
                Text("Cancelar")
            }
        }
    )
}

@Composable
fun SearchBar(query: String, onQueryChanged: (String) -> Unit, modifier: Modifier = Modifier) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChanged,
        label = { Text("Search Pokémon") },
        modifier = modifier,
        singleLine = true
    )
}