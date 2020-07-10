package com.zercey.paint.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.zercey.paint.R
import com.zercey.paint.databinding.ActivityMainBinding
import java.util.concurrent.TimeUnit

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var loginId = ""
    private lateinit var mAuth: FirebaseAuth
    private var number = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,
            R.layout.activity_main
        )

        mAuth = FirebaseAuth.getInstance()

        binding.loginButton.setOnClickListener {

            number = binding.contactInput.text.toString()
            if (number.length == 10) {
                verifyCredential("+91$number")
            } else {
                binding.contactInput.error = "Enter valid Mobile Number"
            }
        }

        binding.otpInput.setOtpCompletionListener { otp ->
            val credential = PhoneAuthProvider.getCredential(loginId, otp)
            login(credential)
        }
    }

    private fun verifyCredential(contact: String) {
        val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                login(credential)
            }

            override fun onVerificationFailed(e: FirebaseException) {
                Log.w("TAG", "onVerificationFailed", e)
                binding.otpInput.visibility = View.GONE
                binding.contactInputLayout.visibility = View.VISIBLE
                binding.loginButton.visibility = View.VISIBLE
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                binding.contactInputLayout.visibility = View.GONE
                binding.loginButton.visibility = View.GONE
                binding.otpInput.visibility = View.VISIBLE
                loginId = verificationId

            }
        }

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            contact,
            60,
            TimeUnit.SECONDS,
            this,
            callbacks
        )
    }

    private fun login(credential: PhoneAuthCredential) {
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    startActivity(Intent(this@LoginActivity, SplashActivity::class.java))
                    finish()
                } else {
                    Log.w("TAG", "signInWithCredential:failure", task.exception)

                    binding.otpInput.visibility = View.GONE
                    binding.contactInputLayout.visibility = View.VISIBLE
                    binding.loginButton.visibility = View.VISIBLE
                }
            }
    }
}