package com.kev95p.pruebakotlin.interfaces

import com.kev95p.pruebakotlin.data.dto.PokemonDto
import com.kev95p.pruebakotlin.data.dto.TeamDto

interface AddTeam {
    interface Model{
        fun getLocalPokemon()
        fun getTeamFromRdb(key:String)
        fun saveTeamOnRdb(team:TeamDto)
    }
    interface Presenter{
        fun getLocalPokemon()
        fun getTeam(key:String)
        fun saveTeam(team:TeamDto)
        fun receiveLocalPokemon(data: ArrayList<PokemonDto?>)
        fun receiveTeam(team:TeamDto)
        fun teamSaved()
    }
    interface View{
        fun receiveLocalPokemon(data: ArrayList<PokemonDto?>)
        fun receiveTeam(team:TeamDto)
        fun teamSaved()
    }
}