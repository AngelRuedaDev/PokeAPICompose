package com.example.pokeapicompose.data.model

data class PokemonDetail(
    val id: Int,
    val name: String,
    val types: List<PokemonType>,
    val weight: String
){
    val spriteUrl: String
        get() = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/home/${id}.png"

    val spriteShinyUrl: String
        get() = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/home/shiny/${id}.png"
}

data class PokemonType(
    val slot: Int,
    val type: TypeInfo
)

data class TypeInfo(
    val name: String,
    val url: String
)