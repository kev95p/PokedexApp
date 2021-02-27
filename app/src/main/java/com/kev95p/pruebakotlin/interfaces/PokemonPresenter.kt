package com.kev95p.pruebakotlin.interfaces

import com.kev95p.pruebakotlin.data.dto.PokemonDto

interface PokemonPresenter {
    fun getData(limit:Int,offset:Int)
    fun showData(data: ArrayList<PokemonDto?>)
    fun disposeService()
}