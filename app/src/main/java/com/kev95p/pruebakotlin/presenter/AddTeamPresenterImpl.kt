package com.kev95p.pruebakotlin.presenter

import android.content.Context
import android.util.Log
import com.kev95p.pruebakotlin.data.dto.PokemonDto
import com.kev95p.pruebakotlin.data.dto.TeamDto
import com.kev95p.pruebakotlin.interfaces.AddTeam
import com.kev95p.pruebakotlin.model.AddTeamModelImpl

class AddTeamPresenterImpl(private val view:AddTeam.View, ctx: Context): AddTeam.Presenter {

    private val model: AddTeam.Model = AddTeamModelImpl(this,ctx)

    override fun getLocalPokemon() {
        model.getLocalPokemon()
    }

    override fun getTeam(key: String) {
       model.getTeamFromRdb(key)
    }

    override fun saveTeam(team: TeamDto) {
        Log.d("AddTeamModel","saveTeamOnRdb")
        model.saveTeamOnRdb(team)
    }

    override fun receiveLocalPokemon(data: ArrayList<PokemonDto?>) {
        view.receiveLocalPokemon(data)
    }

    override fun receiveTeam(team: TeamDto) {
        view.receiveTeam(team)
    }

    override fun teamSaved() {
        view.teamSaved()
    }
}