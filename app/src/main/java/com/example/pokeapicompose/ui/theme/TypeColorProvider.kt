package com.example.pokeapicompose.ui.theme

import androidx.compose.ui.graphics.Color

object TypeColorProvider {

    private val typeColors = mapOf(
        "fire" to Color(0xFFFFA756),
        "water" to Color(0xFF58ABF6),
        "grass" to Color(0xFF8BBE8A),
        "electric" to Color(0xFFF2CB55),
        "psychic" to Color(0xFFFB6C6C),
        "normal" to Color(0xFFB5B9C4),
        "flying" to Color(0xFF83A2E3),
        "poison" to Color(0xFF9F6E97),
        "ground" to Color(0xFFF78551),
        "rock" to Color(0xFFD4C294),
        "bug" to Color(0xFF8BD674),
        "ghost" to Color(0xFF8571BE),
        "steel" to Color(0xFF4C91B2),
        "ice" to Color(0xFF91D8DF),
        "dragon" to Color(0xFF7383B9),
        "dark" to Color(0xFF6F6E78),
        "fairy" to Color(0xFFEBA8C3),
        "fighting" to Color(0xFFEB4971)
    )

    fun getColorForType(type: String): Color {
        return typeColors[type] ?: Color.Gray
    }
}