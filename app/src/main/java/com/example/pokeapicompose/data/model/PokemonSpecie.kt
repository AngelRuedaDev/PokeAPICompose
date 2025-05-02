package com.example.pokeapicompose.data.model

import com.google.gson.annotations.SerializedName

data class PokemonSpecie(
    @SerializedName("evolution_chain") // Annotates the field to map the JSON key to the variable name.
    val evolutionChain: EvolutionChainUrl
)

data class EvolutionChainUrl(
    val url: String
)