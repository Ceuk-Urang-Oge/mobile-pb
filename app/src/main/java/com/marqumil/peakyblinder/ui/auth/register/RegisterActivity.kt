package com.marqumil.peakyblinder.ui.auth.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.marqumil.peakyblinder.R
import com.marqumil.peakyblinder.databinding.ActivityRegisterBinding
import com.marqumil.peakyblinder.remote.ResultState
import com.marqumil.peakyblinder.ui.auth.login.LoginActivity

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private val viewModel: RegisterViewModel by viewModels()
    private lateinit var suEmail: String
    private lateinit var suPassword: String
    private lateinit var suUsername: String
    private lateinit var suConfirmPassword: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        setupObserver()
        signUp()
        intentToSignIn()

//        binding.btnRegister.setOnClickListener {
//            register()
//        }


    }


    private fun register() {
        val email = binding.etEmail.text.toString()
        val password = binding.etPassword.text.toString()

        // check if email and password is not empty
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(
                this@RegisterActivity,
                "Please enter text in email/pw",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        Log.d("RegisterActivityy", "debug: $email $password")

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this@RegisterActivity) { task ->
                if (task.isSuccessful) {
                    // Registration successful, navigate to the next activity
                    Toast.makeText(
                        this@RegisterActivity,
                        "Registration successful.",
                        Toast.LENGTH_SHORT
                    ).show()
                    val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                    startActivity(intent)
                } else {
                    // Registration failed, display an error message
                    Log.d("RegisterActivity", "onCreate: ${task.exception}")
                    // If the user already exists display a message to the user
                    if (task.exception?.message.toString() == "The email address is already in use by another account.") {
                        Toast.makeText(
                            this@RegisterActivity,
                            "User already exists.",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                            this@RegisterActivity,
                            "Registration failed.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                }
            }
    }

    private fun setupObserver() {
        binding.apply {
            viewModel.apply {
                emailError.observe(this@RegisterActivity) {
                    if (it.isNotEmpty()) {
                        etEmail.error = it
                        etEmail.requestFocus()
                    } else {
                        etEmail.error = null
                    }
                }

                passwordError.observe(this@RegisterActivity) {
                    if (it.isNotEmpty()) {
                        etPassword.error = it
                        etPassword.requestFocus()
                    } else {
                        etPassword.error = null
                    }
                }

                userNameError.observe(this@RegisterActivity) {
                    if (it.isNotEmpty()) {
                        etName.error = it
                        etName.requestFocus()
                    } else {
                        etName.error = null
                    }
                }


                passwordConfirmationError.observe(this@RegisterActivity) {
                    if (it.isNotEmpty()) {
                        etConfirmPassword.error = it
                        etConfirmPassword.requestFocus()
                    } else {
                        etConfirmPassword.error = null
                    }
                }
            }
        }
    }


    private fun intentToSignIn() {
        binding.apply {
            tvLogin.setOnClickListener {
                val intentSignUp = Intent(this@RegisterActivity, LoginActivity::class.java)
                startActivity(intentSignUp)
            }
        }
    }

    private fun signUp() {
        binding.apply {
            btnRegister.setOnClickListener {
                if (btnRegister.text != getString(R.string.please_wait)) {
                    suEmail = etEmail.text.toString()
                    suUsername = etName.text.toString()
                    suPassword = etPassword.text.toString()
                    // get from radio button
                    suConfirmPassword = etConfirmPassword.text.toString()

                    val builder =
                        AlertDialog.Builder(this@RegisterActivity, R.style.CustomAlertDialog).create()
                    val view = layoutInflater.inflate(R.layout.alert_dialog_confirmation, null)
                    val btnCancel = view.findViewById<Button>(R.id.btnCancel)
                    val btnNext = view.findViewById<Button>(R.id.btnNext)
                    builder.setView(view)
                    btnCancel.setOnClickListener {
                        builder.dismiss()
                    }
                    btnNext.setOnClickListener {
                        viewModel.register(
                            suEmail,
                            suUsername,
                            suPassword,
                            suConfirmPassword
                        )

                        builder.dismiss()
                    }
                    builder.setCanceledOnTouchOutside(false)
                    builder.show()

                    val builder2 =
                        AlertDialog.Builder(this@RegisterActivity, R.style.CustomAlertDialog).create()

                    viewModel.registerState.observe(this@RegisterActivity) {
                        Log.d("SignUpEmailActivity", "signUp: $it")
                        when (it) {
                            is ResultState.Success<*> -> {
                                binding.progressBar.visibility = android.view.View.GONE

                                val view2 = layoutInflater.inflate(R.layout.alert_dialog_signup_success, null)
                                val btnOk = view2.findViewById<Button>(R.id.btnLogin)
                                builder2.setView(view2)
                                btnOk.setOnClickListener {
                                    val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                                    startActivity(intent)
                                    builder2.dismiss()
                                }
                                builder2.setCanceledOnTouchOutside(false)
                                builder2.show()
                            }
                            is ResultState.Failure -> {
                                binding.progressBar.visibility = android.view.View.GONE
                                Toast.makeText(
                                    this@RegisterActivity, it.throwable.message , Toast.LENGTH_SHORT
                                ).show()
                                builder2.dismiss()
                            }
                            is ResultState.Loading -> {
                                binding.progressBar.visibility = android.view.View.VISIBLE
                                builder2.dismiss()
                            }
                        }
                    }
                }
            }
        }
    }
}