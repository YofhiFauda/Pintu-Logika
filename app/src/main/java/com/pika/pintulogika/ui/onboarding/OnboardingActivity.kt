package com.pika.pintulogika.ui.onboarding

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.pika.pintulogika.R
import com.pika.pintulogika.data.SessionManager
import com.pika.pintulogika.databinding.ActivityOnboardingBinding
import com.pika.pintulogika.ui.role.RoleActivity
import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator

class OnboardingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOnboardingBinding
    private lateinit var viewPager: ViewPager2
    private lateinit var adapter: OnboardingAdapter
    private lateinit var btnNext: ImageButton
    private lateinit var btnGetStarted: Button
    private lateinit var dotsIndicator: WormDotsIndicator
    private lateinit var btnSkip: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val onboardingItems = listOf(
            OnboardingItem("SELAMAT DATANG\nDI PINTU LOGIKA", "Lewati","Pelajari Aljabar Boolean dan Gerbang Logika dengan cara yang lebih interaktif dan menyenangkan! Aplikasi ini dirancang untuk membantu memahami konsep dasar dengan simulasi, latihan, dan kuis interaktif.", R.drawable.scene_1),
            OnboardingItem("GERBANG LOGIKA\nDALAM DUNIA DIGITAL", "Lewati", "Kenali berbagai jenis gerbang logika seperti AND, OR, NOT, XOR, dan lainnya. Pahami bagaimana gerbang-gerbang ini digunakan dalam komponen elektronik dansistem digital!", R.drawable.scene_2),
            OnboardingItem("ASAH KEMAMPUAN\nLOGIKA KAMU", "Lewati","Uji pengetahuan Anda dengan latihan dan kuis interaktif. Tingkatkan keterampilan analisis dan pemahaman konsep aljabarboolean untuk menguasai penerapan gerbang logika dalam dunia digital.", R.drawable.scene_3),
        )

        viewPager = binding.viewPager
        btnNext = binding.btnNext
        dotsIndicator = binding.dotsIndicator
        btnGetStarted = binding.btnGetStarted
        btnSkip = findViewById(R.id.tv_skipOnboarding)

        adapter = OnboardingAdapter(onboardingItems) {
            val prefManager = SessionManager(this)
            prefManager.setFirstTimeLaunch(false)
            Log.d("PREF", "First time? ${prefManager.isFirstTimeLaunch()}")

            // Callback ketika tombol "Lewati" ditekan
            startActivity(Intent(this, RoleActivity::class.java))
            finish()
        }

        viewPager.adapter = adapter
        dotsIndicator.attachTo(viewPager)

        // Deteksi posisi halaman
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                if (position == adapter.itemCount - 1) {
                    // Halaman terakhir
                    btnNext.visibility = View.GONE
                    btnGetStarted.visibility = View.VISIBLE
                } else {
                    btnNext.visibility = View.VISIBLE
                    btnGetStarted.visibility = View.GONE
                }
            }
        })

        btnNext.setOnClickListener {

            viewPager.currentItem = viewPager.currentItem + 1
        }
        btnGetStarted.setOnClickListener {
            val prefManager = SessionManager(this)
            prefManager.setFirstTimeLaunch(false)
            Log.d("PREF", "First time? ${prefManager.isFirstTimeLaunch()}")


            startActivity(Intent(this, RoleActivity::class.java))
            finish()
        }

    }
}


