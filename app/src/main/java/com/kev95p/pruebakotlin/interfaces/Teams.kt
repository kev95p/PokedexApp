package com.kev95p.pruebakotlin.interfaces

import com.kev95p.pruebakotlin.data.dto.TeamDto

interface Teams {
    interface Model{
        fun fetchTeamsRdb()
        fun saveTeamRdb(team:TeamDto)
        fun deleteTeamRdb(team:TeamDto)
    }
    interface Presenter{
        fun getTeams()
        fun saveTeam(team:TeamDto)
        fun deleteTeam(team:TeamDto)
        fun receiveTeams(data: ArrayList<TeamDto?>)
    }
    interface View{
        fun receiveTeams(data: ArrayList<TeamDto?>)
    }
}