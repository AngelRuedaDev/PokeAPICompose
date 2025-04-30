package com.example.pokeapicompose.data.model

data class TypeListResponse(
    val results: List<Type>
)

data class Type(
    val name: String,
    val url: String
)

