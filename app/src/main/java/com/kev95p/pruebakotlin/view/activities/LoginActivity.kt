package com.kev95p.pruebakotlin.view.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.kev95p.pruebakotlin.R
import com.kev95p.pruebakotlin.interfaces.Login
import com.kev95p.pruebakotlin.presenter.LoginPresenterImpl

class LoginActivity : AppCompatActivity(), Login.View {

    private lateinit var presenter: Login.Presenter
    private lateinit var txtEmail: TextView
    private lateinit var txtPassword: TextView
    private lateinit var btnLogin:  Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login2)

        presenter = LoginPresenterImpl(this,this)
        txtEmail = findViewById(R.id.txtEmail)
        txtPassword = findViewById(R.id.txtPassword)
        btnLogin = findViewById(R.id.btnLogin)

        btnLogin.setOnClickListener { view ->
            if(validateForm()){
                presenter.requestLogin(txtEmail.text.toString(),txtPassword.text.toString())
            }
        }
    }

    fun validateForm(): Boolean{
        if(txtEmail.text.isEmpty()){
            txtEmail.error = "Email is required"
            return false
        }
        if(txtPassword.text.isEmpty()){
            txtPassword.error = "Password is required"
            return false
        }
        return true
    }

    override fun loginSuccess() {
        val newIntent = Intent(this,MainActivity::class.java)
        newIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(newIntent)
    }

    override fun loginFail() {
        TODO("Not yet implemented")
    }
}