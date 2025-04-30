package com.example.pokeapicompose.data.model

data class TypeInfo(
    val id: Int,
    val name: String,
    val pokemon: List<TypePokemon>
)

data class TypePokemon(
    val pokemon: PokemonItem
)
