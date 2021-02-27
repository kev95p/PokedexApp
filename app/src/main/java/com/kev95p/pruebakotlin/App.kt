package com.kev95p.pruebakotlin

import android.app.Application
import android.util.Log
import androidx.room.Room
import androidx.room.RoomDatabase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.kev95p.pruebakotlin.data.dblocal.AppDatabase
import com.kev95p.pruebakotlin.data.dto.TeamDto
import com.kev95p.pruebakotlin.data.firebasedb.RDatabase


class App : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}