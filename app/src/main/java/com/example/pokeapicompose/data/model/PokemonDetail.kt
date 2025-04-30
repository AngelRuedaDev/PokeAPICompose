package com.example.pokeapicompose.data.model

data class PokemonDetail(
    val id: Int,
    val name: String,
    val types: List<PokemonType>,
    val weight: String,
    val height: String,
    val species: Species
){
    val spriteUrl: String
        get() = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/home/${id}.png"

    val spriteShinyUrl: String
        get() = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/home/shiny/${id}.png"
}

data class PokemonType(
    val slot: Int,
    val type: PokemonTypeInfo
)

data class PokemonTypeInfo(
    val name: String,
    val url: String
)

data class Species(
    val name: String,
    val url: String
)