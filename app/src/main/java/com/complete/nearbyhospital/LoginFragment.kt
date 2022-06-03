package com.complete.nearbyhospital


import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.complete.nearbyhospital.Constants.Companion.firebaseAuth
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_login.*


class LoginFragment : Fragment(R.layout.fragment_login) {


    private lateinit var mAuth: FirebaseAuth

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
       mAuth =firebaseAuth
        login_to_signUp.setOnClickListener {
           val intent=Intent(activity,SignUpFragment::class.java)
            activity?.finish()
            startActivity(intent)
        }
        loginPhone.setOnClickListener {
            val intent=Intent(activity,LoginViaPhoneFragment::class.java)
            activity?.finish()
            startActivity(intent)
        }
        login.setOnClickListener {
            if (!TextUtils.isEmpty(email.text.toString()) && !TextUtils.isEmpty(password.text.toString())) {

                verifyEmail(email.text.toString(), password.text.toString())
            }else {
                Toast.makeText(activity, "Please Enter Something", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun login(email: String, password: String) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(requireActivity()) { task ->
            if (task.isSuccessful) {
                val intent = Intent(activity, MainActivity::class.java)
                activity?.finish()
                startActivity(intent)

            } else {
                Toast.makeText(activity, " Authentication Failed:"+task.exception, Toast.LENGTH_SHORT).show()

                Log.d("error",task.exception.toString())
            }
        }
    }
    private fun verifyEmail(email: String, password: String) {
        mAuth.signInWithEmailAndPassword(email, password).addOnSuccessListener {
            if (it.user!!.isEmailVerified) {
                login(email, password)
            } else {

                Toast.makeText(activity, "Please Verify", Toast.LENGTH_SHORT).show()
            }
            mAuth.signOut()
        }.addOnFailureListener {

            Toast.makeText(activity, " Authentication Failed! May be User doesn't exist", Toast.LENGTH_SHORT).show()
            Log.d("error",it.toString())
        }
    }

    override fun onStart() {
        super.onStart()
        val mAuth1 = firebaseAuth.currentUser
        if (mAuth1 != null) {
            startActivity(Intent(activity, MainActivity::class.java))
        } else {
            super.onStart()
        }
    }
}