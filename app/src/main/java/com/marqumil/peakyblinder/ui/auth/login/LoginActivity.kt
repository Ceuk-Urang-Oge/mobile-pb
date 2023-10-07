package com.marqumil.peakyblinder.ui.auth.login

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.marqumil.peakyblinder.MainActivity
import com.marqumil.peakyblinder.R
import com.marqumil.peakyblinder.databinding.ActivityLoginBinding
import com.marqumil.peakyblinder.remote.ResultState
import com.marqumil.peakyblinder.ui.auth.UserViewModel
import com.marqumil.peakyblinder.ui.auth.register.RegisterActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var auth: FirebaseAuth
    private var isPasswordVisible = false
    private val viewModel: UserViewModel by viewModels()
    private lateinit var siEmail: String
    private lateinit var siPassword: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        setupObserver()
        logIn()
        intentToSignUp()
//        passwordState()

//        // Configure Google Sign In
//        val gso = GoogleSignInOptions
//            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//            .requestIdToken(getString(R.string.default_web_client_id))
//            .requestEmail()
//            .build()
//        googleSignInClient = GoogleSignIn.getClient(this, gso)
//        // Initialize Firebase Auth
//        auth = Firebase.auth
//
//        binding.btnLogin.setOnClickListener {
//            val email = binding.etEmail.text.toString()
//            val password = binding.etPassword.text.toString()
//
//            // check if email and password is not empty
//            if (email.isEmpty() || password.isEmpty()) {
//                return@setOnClickListener
//            }
//
//            Log.d("LoginActivityy", "debug: $email $password")
//
//            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
//                .addOnCompleteListener(this@LoginActivity) { task ->
//                    if (task.isSuccessful) {
//                        // Sign in success, navigate to the next activity
//                        Log.d("LoginActivityy", "signInWithEmail:success")
//                        val user = auth.currentUser
//                        Toast.makeText(
//                            this@LoginActivity,
//                            "Login success.",
//                            Toast.LENGTH_SHORT
//                        ).show()
//                        updateUI(user)
//                    } else {
//                        // If sign in fails, display a message to the user.
//                        Log.w("LoginActivityy", "signInWithEmail:failure", task.exception)
//                        Toast.makeText(
//                            this@LoginActivity,
//                            "Login failed.",
//                            Toast.LENGTH_SHORT
//                        ).show()
//                    }
//                }
//        }

//        binding.signInButton.setOnClickListener {
//            signIn()
//        }
//
//        binding.tvRegister.setOnClickListener {
//            // intent to google web register
//            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
//            startActivity(intent)
//        }
    }

//    private fun signIn() {
//        val signInIntent = googleSignInClient.signInIntent
//        Log.d(TAG, "test")
//        resultLauncher.launch(signInIntent)
//    }
//
//
//    private var resultLauncher = registerForActivityResult(
//        ActivityResultContracts.StartActivityForResult()
//    ) { result ->
//        Log.d(TAG, "test: ${result}")
//        if (result.resultCode == Activity.RESULT_OK) {
//            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
//            // test the google sign in
//            Log.d(TAG, "test: ${task}")
//
//            try {
//                // Google Sign In was successful, authenticate with Firebase
//                val account = task.getResult(ApiException::class.java)!!
//                Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)
//                firebaseAuthWithGoogle(account.idToken!!)
//            } catch (e: ApiException) {
//                // Google Sign In failed, update UI appropriately
//                Log.w(TAG, "Google sign in failed", e)
//            }
//        }
//    }
//
//    private fun firebaseAuthWithGoogle(idToken: String) {
//        val credential = GoogleAuthProvider.getCredential(idToken, null)
//        auth.signInWithCredential(credential)
//            .addOnCompleteListener(this) { task ->
//                if (task.isSuccessful) {
//                    // Sign in success, update UI with the signed-in user's information
//                    Log.d(TAG, "signInWithCredential:success")
//                    val user = auth.currentUser
//                    updateUI(user)
//                } else {
//                    // If sign in fails, display a message to the user.
//                    Log.w(TAG, "signInWithCredential:failure", task.exception)
//                    updateUI(null)
//                }
//            }
//    }
//
//    private fun updateUI(currentUser: FirebaseUser?) {
//        if (currentUser != null){
//            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
//            finish()
//        }
//    }
//
//    override fun onStart() {
//        super.onStart()
//        // Check if user is signed in (non-null) and update UI accordingly.
//        val currentUser = auth.currentUser
//        updateUI(currentUser)
//    }

    private fun setupObserver() {
        binding.apply {
            viewModel.apply {
                emailError.observe(this@LoginActivity) {
                    if (it.isNotEmpty()) {
                        etEmail.error = it
                        etEmail.requestFocus()
                    }
                    else {
                        etEmail.error = null
                    }
                }

                passwordError.observe(this@LoginActivity) {
                    if (it.isNotEmpty()) {
                        etPassword.error = it
                        etPassword.requestFocus()
                    }
                    else {
                        etPassword.error = null
                    }
                }

                loginState.observe(this@LoginActivity) {
                    when (it) {
                        is ResultState.Success<*> -> {
                            binding.progressBar.visibility = android.view.View.GONE
                            pushLogin()
//                            val successDialog = SuccessDialog()
//                            successDialog.textStringId = "login Berhasil"
//                            successDialog.textStringIdJudul = "Selamat datang"
//                            successDialog.show(supportFragmentManager, "success")
//                            successDialog.okCallback = {
//                                if (it.value as Boolean) pushLogin()
//                            }
                        }
                        is ResultState.Failure -> {
                            binding.progressBar.visibility = android.view.View.GONE
                            btnLogin.setText(R.string.enter)
                            Toast.makeText(this@LoginActivity, "${it.throwable.message} : Username atau password salah ", Toast.LENGTH_SHORT).show()
                        }
                        is ResultState.Loading -> {
                            binding.progressBar.visibility = android.view.View.VISIBLE
                            btnLogin.setText(R.string.please_wait)
                        }
                    }
                }
            }
        }
    }

    private fun intentToSignUp() {
        binding.apply {
            tvRegister.setOnClickListener {
                val intentSignUp = Intent(this@LoginActivity, RegisterActivity::class.java)
                startActivity(intentSignUp)
            }
        }
    }

    private fun logIn() {
        binding.apply {
            btnLogin.setOnClickListener {
                if (btnLogin.text != getString(R.string.please_wait)) {
                    siEmail = etEmail.text.toString()
                    siPassword = etPassword.text.toString()

                    viewModel.login(siEmail, siPassword)
                }
            }
        }
    }

    private fun pushLogin() {
        val intentSignIn = Intent(this@LoginActivity, MainActivity::class.java)
        startActivity(intentSignIn)
        finishAffinity()
    }

    companion object {
        private const val TAG = "LoginActivityLog"
    }
}