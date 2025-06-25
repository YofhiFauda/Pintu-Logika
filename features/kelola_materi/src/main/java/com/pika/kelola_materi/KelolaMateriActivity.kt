package com.pika.kelola_materi

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.pika.kelola_materi.databinding.ActivityKelolaMateriBinding

class KelolaMateriActivity : AppCompatActivity() {
    private lateinit var binding: ActivityKelolaMateriBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityKelolaMateriBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.apply {
            val adapter = KelolaMateriPagerAdapter(this@KelolaMateriActivity)
            viewPager.adapter = adapter

            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                tab.text = if (position == 0) "Tambah Modul" else "Tambah Materi"
            }.attach()

            binding.btnBackToDashboard.setOnClickListener {
                finish()
            }
        }
    }
}
