package com.kev95p.pruebakotlin.data.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PokemonDto(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "name") var name:String? = null,
    @ColumnInfo(name = "image_url") var imageUrl:String? = null,
    @ColumnInfo(name = "region") var region: String? = null,
    @ColumnInfo(name = "type1") var type1: String? = null,
    @ColumnInfo(name = "type2") var type2: String? = null,
    @ColumnInfo(name = "sprite") var imageSpriteUrl:String? = null,
    @ColumnInfo(name = "hp") var hp:Int? = null,
    @ColumnInfo(name = "atk") var atk:Int? = null,
    @ColumnInfo(name = "def") var def:Int? = null,
    @ColumnInfo(name = "sp_atk") var spAtk:Int? = null,
    @ColumnInfo(name = "sp_def") var spDef:Int? = null,
    @ColumnInfo(name = "speed") var speed:Int? = null,
)
