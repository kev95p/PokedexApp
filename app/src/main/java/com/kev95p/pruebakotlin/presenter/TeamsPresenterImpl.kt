package com.kev95p.pruebakotlin.presenter

import android.content.Context
import com.kev95p.pruebakotlin.data.dto.TeamDto
import com.kev95p.pruebakotlin.interfaces.Teams
import com.kev95p.pruebakotlin.model.TeamsModelImpl

class TeamsPresenterImpl(private val view: Teams.View,ctx: Context): Teams.Presenter{

    private val pokemonModel: TeamsModelImpl = TeamsModelImpl(this,ctx)


    override fun getTeams() {
       pokemonModel.fetchTeamsRdb()
    }

    override fun saveTeam(team: TeamDto) {
        pokemonModel.saveTeamRdb(team)
    }

    override fun deleteTeam(team: TeamDto) {
        pokemonModel.deleteTeamRdb(team)
    }

    override fun receiveTeams(data: ArrayList<TeamDto?>) {
        view.receiveTeams(data)
    }

}