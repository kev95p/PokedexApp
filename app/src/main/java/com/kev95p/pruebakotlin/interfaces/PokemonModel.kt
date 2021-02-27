package com.kev95p.pruebakotlin.interfaces


interface PokemonModel {
    fun fetchData(limit:Int,offset:Int)
    fun getDisposable()
}