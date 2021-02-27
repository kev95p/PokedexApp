package com.kev95p.pruebakotlin.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kev95p.pruebakotlin.data.dto.PokemonDto

@Dao
interface PokemonDao {
    @Query("SELECT * FROM PokemonDto")
    fun getAll(): List<PokemonDto?>

    @Query("SELECT * FROM PokemonDto WHERE id = :id")
    fun findById(id:Int): PokemonDto

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(pokemon: PokemonDto)

}