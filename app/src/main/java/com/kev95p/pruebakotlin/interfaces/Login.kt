package com.kev95p.pruebakotlin.interfaces

interface Login {
    interface Model{
        fun requestLogin(email:String,password:String)
    }
    interface Presenter{
        fun requestLogin(email:String,password:String)
        fun loginSuccess()
    }
    interface View{
        fun loginSuccess()
        fun loginFail()
    }
}