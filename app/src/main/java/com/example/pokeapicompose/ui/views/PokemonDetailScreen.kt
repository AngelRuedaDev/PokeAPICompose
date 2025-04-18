package com.example.pokeapicompose.ui.views

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
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
import com.example.pokeapicompose.data.model.PokemonDetail

@Composable
fun PokemonDetailScreen(id: Int, viewModel: PokemonDetailViewModel) {
    val pokemon by viewModel.pokemon.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

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
            error != null -> Text(error ?: "Error", Modifier.align(Alignment.Center))
            pokemon != null -> {
                Column {
                    /*Text("Name: ${pokemon!!.name}", fontSize = 24.sp)
                    Text("Weight: ${pokemon!!.weight}")
                    Text("Types: ${pokemon!!.types.joinToString(", ") { it.type.name }}")
                    AsyncImage(
                        model = pokemon!!.spriteUrl,
                        contentDescription = pokemon!!.name
                    )

                    AsyncImage(
                        model = pokemon!!.spriteShinyUrl,
                        contentDescription = "Pokemon Image"
                    )*/

                    PokemonDetail(pokemon!!)
                }
            }
        }
    }
}

@Composable
fun PokemonDetail(pokemon: PokemonDetail) {
    PokemonDetailTitle(pokemon)
    PokemonDetailImages(pokemon)
    PokemonDetailInformation(pokemon)
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
            color = MaterialTheme.colorScheme.secondary
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
            ElevatedCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f),
                elevation = CardDefaults.elevatedCardElevation(defaultElevation = 5.dp),
                colors = CardDefaults.elevatedCardColors(
                    containerColor = Color.White,
                    contentColor = MaterialTheme.colorScheme.secondary
                )
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
            Text("Regular", fontWeight = FontWeight.Light)
        }

        // --- Shiny ---
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ElevatedCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f),
                elevation = CardDefaults.elevatedCardElevation(defaultElevation = 5.dp),
                colors = CardDefaults.elevatedCardColors(
                    containerColor = Color.White,
                    contentColor = MaterialTheme.colorScheme.secondary
                )
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
            Text("Shiny", fontWeight = FontWeight.Light)
        }
    }
}

@Composable
fun PokemonDetailInformation(pokemon: PokemonDetail) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 5.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = Color.White,
            contentColor = MaterialTheme.colorScheme.secondary
        )
    ) {
        //Flexbox con tipos

        Information(pokemon)
    }
}

@Composable
fun Information(pokemon: PokemonDetail) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 20.dp, horizontal = 16.dp)) {

        Text("Weight", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Text("${pokemon.weight} kg", fontSize = 20.sp, fontWeight = FontWeight.Normal)

    }
}