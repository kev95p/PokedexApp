package com.kev95p.pruebakotlin.data.dto

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class TeamDto(
    var name:String? = null,
    var pokemon: List<String>?=null,
    @Exclude var key: String?=null,
    @Exclude var pokemonList: List<PokemonDto?>?=null,
)
