package com.example.pokeapicompose.data.model

data class PokemonListResponse(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<PokemonItem>
)

data class PokemonItem(
    val name: String,
    val url: String
){
    val id: Int
        get() = url.trimEnd('/').split("/").last().toInt()

    val spriteUrl: String
        get() = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/home/${id}.png"
}