package com.kev95p.pruebakotlin.presenter

import android.content.Context
import com.kev95p.pruebakotlin.interfaces.Login
import com.kev95p.pruebakotlin.model.LoginModelImpl

class LoginPresenterImpl(private val view: Login.View, ctx:Context): Login.Presenter {

    private val model: Login.Model = LoginModelImpl(this,ctx)

    override fun requestLogin(email: String, password: String) {
       model.requestLogin(email,password)
    }

    override fun loginSuccess() {
        view.loginSuccess()
    }
}