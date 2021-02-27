package com.kev95p.pruebakotlin.model

import android.content.Context
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.kev95p.pruebakotlin.interfaces.Login

class LoginModelImpl(private val presenter: Login.Presenter, private val ctx: Context): Login.Model {

    private val userRef = FirebaseDatabase.getInstance().getReference("users")

    override fun requestLogin(email: String, password: String) {

        val shared = ctx.applicationContext.getSharedPreferences("pref",0)
        userRef.addListenerForSingleValueEvent(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val result = snapshot.children.find {
                    ((it.child("email").value as String) == email )
                }
                if(result != null){
                    val editor = shared.edit()
                    editor.putString("currentUser",result.key)
                    editor.apply()
                }
                else{
                    val key = userRef.push().key.toString()
                    userRef.child(key).child("email").setValue(email)
                    val editor = shared.edit()
                    editor.putString("currentUser",key)
                    editor.apply()
                }
                presenter.loginSuccess()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

        presenter.loginSuccess()
    }
}