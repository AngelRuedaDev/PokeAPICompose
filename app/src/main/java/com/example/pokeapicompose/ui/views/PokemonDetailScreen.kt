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
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.pokeapicompose.data.model.PokemonItem
import com.example.pokeapicompose.ui.theme.SoftBlue

/**
 * Displays the detail screen for a specific Pokémon.
 * It shows a loading indicator, error message, or the full Pokémon detail depending on the current UI state.
 * Triggers data fetching when the screen is first displayed.
 */
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
            isLoading -> PokemonLoadingDetail()
            error != null -> Text(error ?: stringResource(id = R.string.unknown_error), Modifier.align(Alignment.Center))
            pokemon != null -> {
                Column {
                    PokemonDetail(pokemon!!, evolutionItems)
                }
            }
        }
    }
}

/**
 * Displays the full detail view of a Pokémon, including its name, images, type info, and evolution chain.
 * Evolutions are shown only if more than one evolution stage is available.
 */
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

        // Only show evolutions if there is more than 1 pokémon on the list
        if (evolutionItems.size > 1) {
            Evolutions(evolutionItems, pokemon)
        }
    }
}

/**
 * Displays a list of evolution stages for the current Pokémon.
 * Highlights the current Pokémon among the evolution list.
 */
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

/**
 * Displays the name and ID number of the Pokémon at the top of the detail view.
 * The name is capitalized and the ID is shown in a 3-digit format.
 */
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

/**
 * Displays the regular and shiny sprite images of the Pokémon side by side.
 * Each image is shown inside a styled surface card with a label.
 */
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

/**
 * Displays general information about the Pokémon including its types and physical stats (weight and height).
 * Information is displayed inside a styled card container.
 */
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

/**
 * Displays the Pokémon's types in a responsive row layout using pill-shaped colored labels.
 */
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

/**
 * Displays weight and height information for the Pokémon.
 * Values are shown in metric units (kg for weight, cm for height).
 */
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

/**
 * Displays a single pill-shaped label for a Pokémon type with background color depending on the type.
 */
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

/**
 * Converts a value from hectograms to kilograms.
 * This is used because Pokémon API weight is provided in hectograms.
 *
 * @param hectograms Weight in hectograms.
 * @return Equivalent weight in kilograms.
 */
private fun hectogramsToKilograms(hectograms: Int): Float {
    return hectograms / 10f
}

/**
 * Converts a value from decimeters to centimeters.
 * This is used because Pokémon API height is provided in decimeters.
 */
private fun decimetersToCentimeters(decimeters: Int): Int {
    return decimeters * 10
}

/**
 * Displays a single Pokémon evolution item with its sprite and name.
 * If the current item corresponds to the currently viewed Pokémon, a border is added for emphasis.
 */
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

@Composable
fun PokemonLoadingDetail() {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.pokeball_loading))
    val progress by animateLottieCompositionAsState(composition, iterations = LottieConstants.IterateForever)
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        LottieAnimation(
            composition = composition,
            progress = { progress },
            modifier = Modifier.size(150.dp)
        )
    }
}