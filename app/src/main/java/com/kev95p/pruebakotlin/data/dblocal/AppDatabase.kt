package com.kev95p.pruebakotlin.data.dblocal

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.kev95p.pruebakotlin.data.dao.PokemonDao
import com.kev95p.pruebakotlin.data.dto.PokemonDto

@Database(entities = [PokemonDto::class],version = 1)
abstract class AppDatabase:RoomDatabase() {
    abstract fun pokemonDao(): PokemonDao
    companion object{
        @Volatile
        private var INSTANCE: AppDatabase? = null
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "pk.db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}