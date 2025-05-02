package com.example.pokeapicompose.ui.views

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.pokeapicompose.R
import com.example.pokeapicompose.data.model.PokemonItem
import com.example.pokeapicompose.data.model.PokemonListResponse
import com.example.pokeapicompose.data.model.TypeListResponse
import com.example.pokeapicompose.data.navigation.AppScreens
import com.example.pokeapicompose.viewmodel.PokemonListViewModel
import androidx.compose.runtime.remember as remember

/**
 * Displays the main screen for listing Pokémon.
 * It handles search input, type filtering, and different UI states (loading, error, success).
 */
@Composable
fun PokemonListScreen(viewModel: PokemonListViewModel, navController: NavController) {
    val pokemonFiltered = viewModel.pokemonFilteredList.collectAsState()
    val isLoading = viewModel.isLoading.collectAsState()
    val error = viewModel.error.collectAsState()
    val searchQuery = viewModel.searchQuery.collectAsState()
    val typesList = viewModel.typesList.collectAsState()
    val selectedType = viewModel.selectedType.collectAsState()

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
                    text = error.value ?: stringResource(id = R.string.unknown_error),
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            else -> {
                Column {
                    SearchSection(
                        searchQuery.value,
                        { viewModel.onSearchQueryChanged(it) },
                        typesList.value,
                        selectedType.value,
                        onTypeSelected = { viewModel.onTypeSelected(it) })

                    PokemonList(pokemonFiltered.value, navController, selectedType.value)
                }
            }
        }
    }
}

/**
 * Displays a large bold title.
 */
@Composable
fun Title(title: String) {
    Text(
        title,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 18.dp, top = 18.dp, bottom = 10.dp),
        color = MaterialTheme.colorScheme.onBackground,
        fontSize = 36.sp,
        fontWeight = FontWeight.Bold
    )
}

/**
 * Displays a list of Pokémon in a scrollable column.
 * Shows a dynamic title based on the selected Pokémon type.
 * Navigates to the detail screen when a Pokémon is clicked.
 */
@Composable
fun PokemonList(
    pokemonListResponse: PokemonListResponse,
    navController: NavController,
    selectedType: String?
) {
    Column{
        if (selectedType.isNullOrEmpty()) {
            Title(stringResource(id = R.string.pokemon_list_title))
        } else {
            Title("${selectedType.replaceFirstChar { it.uppercase() }} Type")
        }

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(pokemonListResponse.results) { pokemon ->
                PokemonListItem(pokemon = pokemon) {
                    // Go to the detail Screen
                    navController.navigate(route = AppScreens.PokemonDetailScreen.route + "/${pokemon.id}")
                }
            }
        }
    }

}

/**
 * Displays a single Pokémon item in the list.
 * Shows the Pokémon's image and name, and handles click interaction.
 */
@Composable
fun PokemonListItem(
    pokemon: PokemonItem,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable { onClick() },
        color = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface,
        shape = RoundedCornerShape(12.dp),
        shadowElevation = 5.dp
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

/**
 * Displays the search section with a search bar and filter button.
 * Allows users to filter Pokémon by name or type through a dialog.
 */
@Composable
fun SearchSection(
    searchQuery: String,
    onQueryChanged: (String) -> Unit,
    pokemonTypesList: TypeListResponse?,
    selectedType: String?,
    onTypeSelected: (String?) -> Unit
) {
    val openDialog = remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, top = 18.dp, bottom = 8.dp, end = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        SearchBar(
            query = searchQuery,
            onQueryChanged = { onQueryChanged(it) },
            modifier = Modifier
                .weight(1f)
                .shadow(5.dp, shape = RoundedCornerShape(50.dp))
                .background(Color.Transparent)
        )
        FilterButton { openDialog.value = true }

        //Open the type dialog
        if (openDialog.value) {

            TypesDialog(
                onDismissRequest = {
                    openDialog.value = false
                    onTypeSelected(null) // clean the filter
                },
                onTypeSelected = { type ->
                    openDialog.value = false
                    onTypeSelected(type)
                },
                pokemonTypesList = pokemonTypesList,
                selectedType = selectedType
            )
        }

    }
}

/**
 * Displays a button with a filter icon.
 * Triggers the provided callback when clicked to open the filter options.
 */
@Composable
fun FilterButton(onClick: () -> Unit) {
    IconButton(onClick = onClick) {
        Icon(
            painter = painterResource(R.drawable.ic_filter),
            contentDescription = "Filter Icon",
            tint = MaterialTheme.colorScheme.onBackground
        )
    }
}

/**
 * Displays a dialog for selecting a Pokémon type.
 * It shows a list of Pokémon types as radio buttons, allowing the user to select a type.
 * The selected type is passed back through the `onTypeSelected` callback when confirmed.
 * The dialog can be dismissed by the user via the dismiss button or by selecting a type.
 */
@Composable
fun TypesDialog(
    onDismissRequest: () -> Unit,
    onTypeSelected: (String) -> Unit,
    pokemonTypesList: TypeListResponse?,
    selectedType: String?
) {
    // If the list is null or empty do not show the dialog
    if (pokemonTypesList == null || pokemonTypesList.results.isEmpty()) {
        return
    }

    val currentSelectedType = remember { mutableStateOf("") }

    /* `LaunchedEffect` is used to launch a side-effect when the composable enters the composition.
    In this case, it initializes the `currentSelectedType` state with the selectedType value passed to the dialog.
    If `selectedType` is null, it sets the state to an empty string.*/
    LaunchedEffect(Unit) {
        currentSelectedType.value = selectedType ?: ""
    }

    AlertDialog(
        onDismissRequest = { onDismissRequest() },
        title = { Text(text = stringResource(id = R.string.select_type)) },
        text = {
            LazyColumn {
                items(pokemonTypesList.results) { type ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { currentSelectedType.value = type.name }
                            .padding(vertical = 4.dp, horizontal = 8.dp)
                    ) {
                        RadioButton(
                            selected = (type.name == currentSelectedType.value),
                            onClick = { currentSelectedType.value = type.name },
                            colors = RadioButtonDefaults.colors(
                                selectedColor = MaterialTheme.colorScheme.primary,
                                unselectedColor = MaterialTheme.colorScheme.onBackground
                            )

                        )
                        Text(
                            text = type.name.replaceFirstChar { it.uppercase() },
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(start = 8.dp),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if(currentSelectedType.value.isEmpty()){
                        onDismissRequest()
                    }else{
                        onTypeSelected(currentSelectedType.value)
                    }
                }
            ) {
                Text(text = stringResource(id = R.string.confirm), color = MaterialTheme.colorScheme.onBackground)
            }
        },
        dismissButton = {
            TextButton(
                onClick = { onDismissRequest() }
            ) {
                Text(text = stringResource(id = R.string.clear), color = MaterialTheme.colorScheme.onBackground)
            }
        }
    )
}

/**
 * Displays a text field for searching Pokémon.
 * It takes a query string and a function to update the query as parameters.
 */
@Composable
fun SearchBar(query: String, onQueryChanged: (String) -> Unit, modifier: Modifier = Modifier) {
    TextField(
        value = query,
        onValueChange = onQueryChanged,
        placeholder = { Text(stringResource(id = R.string.search_pokemon)) },
        modifier = modifier,
        shape = RoundedCornerShape(50.dp),
        singleLine = true,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.surface,
            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
            disabledContainerColor = MaterialTheme.colorScheme.surface,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            focusedPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
            unfocusedPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
            disabledPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant
        ),
        trailingIcon = {
            Icon(
                painter = painterResource(R.drawable.ic_search),
                contentDescription = "Search Icon",
                tint = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(end = 12.dp)
            )
        }

    )
}