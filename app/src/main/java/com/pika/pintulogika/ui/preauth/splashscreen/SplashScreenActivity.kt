package com.pika.pintulogika.ui.preauth.splashscreen

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.pika.pintulogika.R
import com.pika.pintulogika.MainActivity
import com.pika.pintulogika.ui.preauth.onboarding.OnboardingActivity
import com.pika.pintulogika.ui.preauth.role.RoleActivity
import com.digitallogic.core_data.session.SessionManager
import kotlinx.coroutines.launch

@Suppress("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        // Tampilkan splash screen system Android 12+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            installSplashScreen()
        }

        super.onCreate(savedInstanceState)

        // Tetap tampilkan layout animasi MotionLayout (semua versi)
        setContentView(R.layout.activity_splash_screen)

        val motionLayout = findViewById<MotionLayout>(R.id.motionLayout)

        motionLayout.setTransitionListener(object : MotionLayout.TransitionListener {
            override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) {}
            override fun onTransitionChange(p0: MotionLayout?, p1: Int, p2: Int, p3: Float) {}

            override fun onTransitionCompleted(p0: MotionLayout?, currentId: Int) {
                if (currentId == R.id.splash7_end) {
                    Handler(Looper.getMainLooper()).postDelayed({
                        navigateToNextScreen()
                    }, 1000) // Delay opsional setelah animasi selesai
                }
            }

            override fun onTransitionTrigger(p0: MotionLayout?, p1: Int, p2: Boolean, p3: Float) {}
        })
    }

    private fun navigateToNextScreen() {
        val sessionManager = SessionManager(this)

        lifecycleScope.launch {
            sessionManager.sessionState.collect { session ->
                val nextIntent = when {
                    session.isFirstTimeLaunch -> Intent(this@SplashScreenActivity, OnboardingActivity::class.java)
                    session.isLoggedIn -> Intent(this@SplashScreenActivity, MainActivity::class.java)
                    else -> Intent(this@SplashScreenActivity, RoleActivity::class.java)
                }

                startActivity(nextIntent)
                finish()
            }
        }
    }
}
