package com.complete.nearbyhospital

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View

import android.widget.Toast
import com.complete.nearbyhospital.databinding.DialogViewBinding
import androidx.appcompat.app.AlertDialog
import com.complete.nearbyhospital.Constants.Companion.dbReferenceUsers
import com.complete.nearbyhospital.Constants.Companion.firebaseAuth

import com.complete.nearbyhospital.databinding.FragmentLoginViaPhoneBinding
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider

import kotlinx.android.synthetic.main.fragment_login_via_phone.*
import java.lang.Exception
import java.util.concurrent.TimeUnit
class LoginViaPhoneFragment  : Fragment(R.layout.fragment_login_via_phone) {

   // private var _binding : FragmentLoginViaPhoneBinding? = null

    lateinit var storedVerificationId: String
    private lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
   // private lateinit var mProgressDialog: Dialog

    private lateinit var name :String


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hide()
        otp.isEnabled = false
        verify.isEnabled = false
        resend.isEnabled = false
        loginBtn.setOnClickListener {
            verify.isEnabled = true
            otp.isEnabled = true
            loginBtn.isEnabled = false
            phoneNumber.isEnabled = false
            resend.isEnabled = true
            show()
            login()
        }
        resend.setOnClickListener {
            verify.isEnabled = false
            otp.isEnabled = false
            loginBtn.isEnabled = true
            phoneNumber.isEnabled = true
            resend.isEnabled = false
        }
        verify.setOnClickListener{
            show()
            val otp=otp.text.toString().trim()
            if(otp.isNotEmpty()){
                val credential : PhoneAuthCredential = PhoneAuthProvider.getCredential(
                    storedVerificationId, otp)
                signInWithPhoneAuthCredential(credential)
            }else{
                hide()
                Toast.makeText(activity,"Enter OTP", Toast.LENGTH_SHORT).show()
            }
        }

        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            }

            override fun onVerificationFailed(e: FirebaseException) {
                verify.isEnabled = false
                otp.isEnabled = false
                loginBtn.isEnabled = true
                phoneNumber.isEnabled = true
                resend.isEnabled = true
                hide()
                Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show()
                Log.d("tagetfailed",e.toString())
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                hide()
                Log.d("TAG", "onCodeSent:$verificationId")
                storedVerificationId = verificationId
                resendToken = token
                Toast.makeText(activity,"Code Sent",Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    hide()
                    val phone = phoneNumber.text.toString().trim()
                    try{
                        val b=DialogViewBinding.inflate(layoutInflater)
                        val mBuilder = AlertDialog.Builder(requireContext())
                            .setView(b.root)
                            .setTitle("Login Form")
                            .setNegativeButton("Cancel"
                            ) { dialog, _ -> dialog?.cancel()
                                firebaseAuth.signOut()
                                verify.isEnabled = false
                                otp.isEnabled = false
                                loginBtn.isEnabled = true
                                phoneNumber.isEnabled = true
                                resend.isEnabled = false
                                Toast.makeText(activity,"This OTP is not valid now!", Toast.LENGTH_LONG).show()}
                            .setPositiveButton("Continue") { dialog, which ->
                                name = b.yourname.text.toString().trim()
                                addUserToDB(name, phone, firebaseAuth.currentUser?.uid)
                                val intent = Intent(activity, MainActivity::class.java)
                                startActivity(intent)
                                Toast.makeText(activity, "Welcome $name", Toast.LENGTH_SHORT).show()
                                dialog?.dismiss()
                            }
                        mBuilder.show()
                        hide()
                    }catch (e: Exception){
                        hide()
                        Log.d("taget",e.toString())
                    }



                } else {
                    hide()
                    Log.d("taget",task.exception.toString())
                    Toast.makeText(
                        activity, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()

                }
            }
    }

    private fun login() {
        var number = phoneNumber.text.toString().trim()

        if (number.isNotEmpty()) {
            number = "+91$number"
            sendVerificationCode(number)


        } else {
            Toast.makeText(activity, "Enter mobile number", Toast.LENGTH_SHORT).show()
            verify.isEnabled = false
            otp.isEnabled = false
            loginBtn.isEnabled = true
            phoneNumber.isEnabled = true
            resend.isEnabled = false

        }
    }

    private fun sendVerificationCode(number: String) {
        val options = PhoneAuthOptions.newBuilder(firebaseAuth)
            .setPhoneNumber(number) // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(requireActivity()) // Activity (for callback binding)
            .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }
    private fun addUserToDB(name: String, phone: String, uid: String?) {
        val u = User(name,phone,null,uid)
        dbReferenceUsers.child(uid!!).setValue(u).addOnSuccessListener {
            Log.d("taget",u.toString())
        }
        Log.d("tag",u.toString())

    }
    private fun show(){
        cl.visibility = View.VISIBLE
    }
    fun hide(){
        cl.visibility = View.INVISIBLE
    }

}