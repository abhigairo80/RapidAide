package com.complete.nearbyhospital

data class User(var name:String = "",
                var phone:String? = "",
                var email:String? = "",
                var uid :String? ="") {
    override fun toString(): String {
        return "User(name=$name, phone=$phone, email=$email, uid=$uid)"
    }
}