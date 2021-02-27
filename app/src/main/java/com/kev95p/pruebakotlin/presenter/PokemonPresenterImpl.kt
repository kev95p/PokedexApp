package com.kev95p.pruebakotlin.presenter

import android.content.Context
import com.kev95p.pruebakotlin.data.dto.PokemonDto
import com.kev95p.pruebakotlin.interfaces.PokemonModel
import com.kev95p.pruebakotlin.interfaces.PokemonPresenter
import com.kev95p.pruebakotlin.interfaces.PokemonView
import com.kev95p.pruebakotlin.model.PokemonModelImpl

class PokemonPresenterImpl(private val view: PokemonView,private val ctx: Context): PokemonPresenter {

    private val pokemonModel: PokemonModel = PokemonModelImpl(this,ctx)

    override fun getData(limit:Int,offset:Int) {
        pokemonModel.fetchData(limit,offset)
    }

    override fun showData(data: ArrayList<PokemonDto?>) {
        view.showData(data);
    }

    override fun disposeService() {
        pokemonModel.getDisposable()
    }
}