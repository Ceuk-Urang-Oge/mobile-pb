package com.marqumil.peakyblinder.ui.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.marqumil.peakyblinder.MainActivity
import com.marqumil.peakyblinder.R
import com.marqumil.peakyblinder.databinding.ActivitySplashScreenBinding
import com.marqumil.peakyblinder.local.SharedPrefs
import com.marqumil.peakyblinder.remote.response.LoginResponse
import com.marqumil.peakyblinder.ui.auth.login.LoginActivity
import com.orhanobut.hawk.Hawk
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SplashScreen : AppCompatActivity() {

    private lateinit var binding: ActivitySplashScreenBinding
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var auth: FirebaseAuth
    private val time = 3000L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        Handler(Looper.getMainLooper()).postDelayed({
            sessionChecker()
        }, time)

        withCoroutine(time)
    }

    private fun sessionChecker() {
        // sesion checeker using firebase and validation
        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }

    private fun updateUI(currentUser: FirebaseUser?) {
        if (currentUser != null){
            startActivity(Intent(this@SplashScreen, MainActivity::class.java))
            finishAffinity()
        } else {
            startActivity(Intent(this@SplashScreen, LoginActivity::class.java))
            finishAffinity()
        }
    }

    companion object {
        private const val TAG = "LoginActivity"
    }

    private fun withCoroutine(time: Long) {
        val mScope = CoroutineScope(Dispatchers.IO)
        mScope.launch {
            delay(time)
            withContext(Dispatchers.Main) {
                launchPostSplashActivity()
                mScope.cancel(null)
            }
        }
    }

    private fun launchPostSplashActivity() {
        val loginData : LoginResponse? = Hawk.get(SharedPrefs.KEY_LOGIN)
        val intent = if (loginData == null) {
            Intent(this, LoginActivity::class.java)
        }
        else {
            Intent(this, MainActivity::class.java)
        }
        startActivity(intent)
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}