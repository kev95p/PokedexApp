package com.kev95p.pruebakotlin.interfaces

import com.kev95p.pruebakotlin.data.dto.PokemonDto

interface PokemonView {
    fun showData(data: ArrayList<PokemonDto?>);
}