package com.example.pokeapicompose.data.model

import com.google.gson.annotations.SerializedName

data class EvolutionChain(
    @SerializedName("id") val id: Int,
    val baby_trigger_item: Any?,
    @SerializedName("chain") val chain: Chain
)

data class Chain(
    val is_baby: Boolean,
    @SerializedName("species") val species: Species,
    val evolution_details: List<EvolutionDetail>,
    @SerializedName("evolves_to") val evolvesTo: List<Chain>
)

data class EvolutionDetail(
    val gender: Int?,
    val held_item: NamedAPIResource?,
    val item: NamedAPIResource?,
    val known_move: NamedAPIResource?,
    val known_move_type: NamedAPIResource?,
    val location: NamedAPIResource?,
    val min_affection: Int?,
    val min_beauty: Int?,
    val min_happiness: Int?,
    val min_level: Int?,
    val needs_overworld_rain: Boolean,
    val party_species: NamedAPIResource?,
    val party_type: NamedAPIResource?,
    val relative_physical_stats: Int?,
    val time_of_day: String,
    val trade_species: NamedAPIResource?,
    val trigger: NamedAPIResource,
    val turn_upside_down: Boolean
)

data class NamedAPIResource(
    val name: String,
    val url: String
)