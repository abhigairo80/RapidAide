package com.complete.nearbyhospital

import android.app.Activity
import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.util.*

class Constants {
    companion object{


        var firebaseAuth = FirebaseAuth.getInstance()
        var dbReferenceUsers = FirebaseDatabase.getInstance("https://rapid-aide-351305-default-rtdb.asia-southeast1.firebasedatabase.app/").reference.child("users")
        var storageReference = FirebaseStorage.getInstance().reference

        val medicalSituations = arrayListOf<Situations>(
            Situations("Bleeding","You can start bleeding from even the smallest cut and bruises. But this is not something worth panicking about at that moment. The emergency case arises when it turns into deep cuts and severe bruises that require immediate attention. The condition in which you should seek emergency are:\n" +
                    "\n" +
                    "You cannot control the bleeding even with proper first aid treatment.\n" +
                    "An object that pierced through your kin, and you cannot get it out because it’s too deep.\n" +
                    "You can see your bone or tissue.\n" +
                    "If you delay the process, then the excess loss of blood may lead to dizziness, unwell feeling, pale face, and in some cases, you lose your consciousness. However, if this happens, it is a matter of urgency.")
        )

        fun getDate():String{
            val c = Calendar.getInstance()
            val date = c.get(Calendar.DATE)
            val month = c.get(Calendar.MONTH)+1
            val year = c.get(Calendar.YEAR)


            return "$date/$month/$year"
        }
        fun getTime():String{
            val c = Calendar.getInstance()
            var hour = c.get(Calendar.HOUR_OF_DAY)
            val minute = c.get(Calendar.MINUTE)
            val seconds = c.get(Calendar.SECOND)
            var timeStamp = ""
            var am = "am"
            if(minute<10 ){
                if(hour == 0){
                    hour = 12
                }
                if(hour>12){
                    hour -= 12
                    am = "pm"
                }
                timeStamp = "$hour:0$minute $am"
            }else {
                if (hour == 0) {
                    hour = 12
                }
                if (hour > 12) {
                    hour -= 12
                    am = "pm"
                }
                timeStamp = "$hour:$minute $am"
            }
            return timeStamp;
        }
    }
//    fun saveToSharedPrefrences(key:String,value:String,ctx:Activity){
//        ctx.getSharedPreferences(SHARED, Context.MODE_PRIVATE)?.edit()?.apply{
//            putString(key,value)
//            apply()
//        }
//    }
//    fun getFromSharedPrefrences(key:String,ctx:Activity){
//        ctx.getSharedPreferences(SHARED, Context.MODE_PRIVATE)?.getString(key,"")
//    }

}