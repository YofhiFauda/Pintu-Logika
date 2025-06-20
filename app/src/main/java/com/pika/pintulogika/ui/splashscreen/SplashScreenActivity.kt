package com.pika.pintulogika.ui.splashScreen

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.pika.pintulogika.ui.onboarding.OnboardingActivity
import com.pika.pintulogika.R
import com.pika.pintulogika.data.SessionManager
import com.pika.pintulogika.ui.role.RoleActivity

class SplashScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        // Android 12+ pakai SplashScreen API
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            installSplashScreen().apply {
                // Opsional: kontrol kondisi jika ingin splash lebih lama
                //contoh seperti Menunggu autentikasi selesai
                //Memuat data penting dari Firestore, Room DB, atau API
                //Menampilkan splash lebih lama dengan animasi atau branding
            }
        }

        super.onCreate(savedInstanceState)

        // Cek jika Android di bawah 12, gunakan layout manual
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
            setContentView(R.layout.activity_splash_screen)

            val motionLayout = findViewById<MotionLayout>(R.id.motionLayout)

            // Dengarkan saat transisi selesai ke splash7_end
            motionLayout.setTransitionListener(object : MotionLayout.TransitionListener {
                override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) {}

                override fun onTransitionChange(p0: MotionLayout?, p1: Int, p2: Int, p3: Float) {}

                override fun onTransitionCompleted(motionLayout: MotionLayout?, currentId: Int) {
                    if (currentId == R.id.splash7_end) {
                        Handler(Looper.getMainLooper()).postDelayed({
                            navigateToMain()
                        }, 2000) // delay 1 detik setelah splash7 selesai
                    }
                }

                override fun onTransitionTrigger(p0: MotionLayout?, p1: Int, p2: Boolean, p3: Float) {}
            })
        } else {
            // Jika Android 12+, langsung lanjutkan (karena splash-nya ditangani oleh system)
            Handler(Looper.getMainLooper()).postDelayed({
                navigateToMain()
            }, 2000) // Delay total 2 detik
        }
    }

    private fun navigateToMain() {
        val sessionManager = SessionManager(this)
        if (sessionManager.isFirstTimeLaunch()) {
            startActivity(Intent(this, OnboardingActivity::class.java))
        } else {
            startActivity(Intent(this, RoleActivity::class.java))
        }
        finish()
    }

}

