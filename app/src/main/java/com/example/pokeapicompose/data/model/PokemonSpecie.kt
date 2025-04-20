package com.example.pokeapicompose.data.model

import com.google.gson.annotations.SerializedName

data class PokemonSpecie(
    @SerializedName("evolution_chain")
    val evolutionChain: EvolutionChainUrl
)

data class EvolutionChainUrl(
    val url: String
)