package com.example.pokeapicompose.ui.views

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.pokeapicompose.viewmodel.PokemonDetailViewModel
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.pokeapicompose.R
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.text.style.TextAlign
import com.example.pokeapicompose.data.model.PokemonDetail
import com.example.pokeapicompose.ui.theme.TypeColorProvider
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.ui.res.stringResource
import com.example.pokeapicompose.data.model.PokemonItem
import com.example.pokeapicompose.ui.theme.SoftBlue

@Composable
fun PokemonDetailScreen(id: Int, viewModel: PokemonDetailViewModel) {
    val pokemon by viewModel.pokemon.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val evolutionItems by viewModel.evolutionItems.collectAsState()

    LaunchedEffect(id) {
        viewModel.fetchPokemon(id)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(WindowInsets.systemBars.only(WindowInsetsSides.Top).asPaddingValues())
    ) {
        when {
            isLoading -> CircularProgressIndicator(Modifier.align(Alignment.Center))
            error != null -> Text(error ?: stringResource(id = R.string.unknown_error), Modifier.align(Alignment.Center))
            pokemon != null -> {
                Column {
                    PokemonDetail(pokemon!!, evolutionItems)
                }
            }
        }
    }
}

@Composable
fun PokemonDetail(pokemon: PokemonDetail, evolutionItems: List<PokemonItem>) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(bottom = 16.dp)
    ) {
        PokemonDetailTitle(pokemon)
        PokemonDetailImages(pokemon)
        PokemonDetailInformation(pokemon)

        // Only show evolutions if there is more than 1 evolution on the list
        if (evolutionItems.size > 1) {
            Evolutions(evolutionItems, pokemon)
        }
    }
}

@Composable
fun Evolutions(evolutionItems: List<PokemonItem>, currentPokemon: PokemonDetail) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Title(stringResource(id = R.string.evolutions_title))
        Spacer(modifier = Modifier.height(8.dp))
        evolutionItems.forEach { pokemon ->
            PokemonEvolutionItem(
                pokemon = pokemon,
                isCurrentPokemon = pokemon.id == currentPokemon.id // Compare IDs
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}


@SuppressLint("DefaultLocale")
@Composable
fun PokemonDetailTitle(pokemon: PokemonDetail) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 16.dp, horizontal = 18.dp)) {
        Text(
            pokemon.name.replaceFirstChar { it.uppercase() },
            modifier = Modifier.fillMaxWidth(),
            fontSize = 36.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            "#${String.format("%03d", pokemon.id)}",
            modifier = Modifier.fillMaxWidth(),
            fontSize = 32.sp,
            fontWeight = FontWeight.Normal,
            color = MaterialTheme.colorScheme.tertiary
        )
    }

}

@Composable
fun PokemonDetailImages(pokemon: PokemonDetail) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp, horizontal = 16.dp)
    ) {
        // --- Regular ---
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f),
                color = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface,
                shape = RoundedCornerShape(12.dp),
                shadowElevation = 5.dp
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    AsyncImage(
                        model = pokemon.spriteUrl,
                        contentDescription = pokemon.name,
                        modifier = Modifier.size(120.dp),
                        contentScale = ContentScale.Crop
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(stringResource(id = R.string.regular), fontWeight = FontWeight.Light, color = MaterialTheme.colorScheme.onBackground)
        }

        // --- Shiny ---
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f),
                color = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface,
                shape = RoundedCornerShape(12.dp),
                shadowElevation = 5.dp
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    AsyncImage(
                        model = pokemon.spriteShinyUrl,
                        contentDescription = "${pokemon.name} shiny",
                        modifier = Modifier.size(120.dp),
                        contentScale = ContentScale.Crop,
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(stringResource(id = R.string.shiny), fontWeight = FontWeight.Light, color = MaterialTheme.colorScheme.onBackground)
        }
    }
}

@Composable
fun PokemonDetailInformation(pokemon: PokemonDetail) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp),
        color = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface,
        shape = RoundedCornerShape(12.dp),
        shadowElevation = 5.dp
    ){
        Column {
            PokemonDetailTypes(pokemon)
            Information(pokemon)
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PokemonDetailTypes(pokemon: PokemonDetail){
    FlowRow(modifier = Modifier
        .fillMaxWidth()
        .padding(top = 16.dp, start = 16.dp, end = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
    ){
        pokemon.types.forEach { type->
            PillItem(type.type.name)
        }
    }
}

@Composable
fun Information(pokemon: PokemonDetail) {

    val weightInKg = hectogramsToKilograms(pokemon.weight.toInt())
    val heightInCm = decimetersToCentimeters(pokemon.height.toInt())

    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 20.dp, horizontal = 16.dp)) {


        Text(stringResource(id = R.string.weight), fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Text("$weightInKg kg", fontSize = 20.sp, fontWeight = FontWeight.Normal)
        Spacer(modifier = Modifier.fillMaxWidth().height(10.dp))
        Text(stringResource(id = R.string.height), fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Text("$heightInCm cm", fontSize = 20.sp, fontWeight = FontWeight.Normal)

    }
}

@Composable
fun PillItem(type: String) {
    val bgColor = TypeColorProvider.getColorForType(type)

    val borderColor = bgColor.copy(
        red = bgColor.red * 0.8f,
        green = bgColor.green * 0.8f,
        blue = bgColor.blue * 0.8f
    )

    Box(
        modifier = Modifier
            .background(
                color = bgColor,
                shape = RoundedCornerShape(50)
            )
            .border(
                width = 2.dp,
                color = borderColor,
                shape = RoundedCornerShape(50)
            )
            .padding(horizontal = 16.dp, vertical = 8.dp)


    ) {
        Text(
            text = type.replaceFirstChar { it.uppercase() },
            color = Color.Black,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            fontSize = 20.sp
        )
    }
}


private fun hectogramsToKilograms(hectograms: Int): Float {
    return hectograms / 10f
}

private fun decimetersToCentimeters(decimeters: Int): Int {
    return decimeters * 10
}

@Composable
fun PokemonEvolutionItem(
    pokemon: PokemonItem,
    isCurrentPokemon: Boolean
) {
    //If the pokemon is the one that we are currently looking add a border to show it to the user
    val borderModifier = if (isCurrentPokemon) {
        Modifier.border(width = 4.dp, color = SoftBlue, shape = RoundedCornerShape(8.dp))
    } else {
        Modifier //
    }
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .then(borderModifier),
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