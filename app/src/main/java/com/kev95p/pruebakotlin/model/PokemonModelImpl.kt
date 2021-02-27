package com.kev95p.pruebakotlin.model

import android.annotation.SuppressLint
import android.content.Context
import com.google.firebase.database.FirebaseDatabase
import com.kev95p.pruebakotlin.data.api.PokemonApi
import com.kev95p.pruebakotlin.data.dblocal.AppDatabase
import com.kev95p.pruebakotlin.interfaces.PokemonModel
import com.kev95p.pruebakotlin.interfaces.PokemonPresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class PokemonModelImpl(private val presenter: PokemonPresenter,ctx:Context) : PokemonModel {

    private var disposable: Disposable? = null
    private val pokemonService = PokemonApi.getInstance(ctx)


    @SuppressLint("CheckResult")
    override fun fetchData(limit: Int, offset: Int) {
        pokemonService.getAllPokemon(limit,offset)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { data ->
                println("Observer: ${Thread.currentThread().name}")
                presenter.showData(data)
            }
    }

    override fun getDisposable() {
        disposable?.dispose()
    }
}