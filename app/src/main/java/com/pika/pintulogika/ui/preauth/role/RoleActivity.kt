package com.pika.pintulogika.ui.preauth.role

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.pika.pintulogika.databinding.ActivityRoleBinding
import com.pika.pintulogika.ui.auth.guru.LoginGuruActivity
import com.pika.pintulogika.ui.auth.siswa.LoginSiswaActivity

class RoleActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRoleBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRoleBinding.inflate(layoutInflater)
        setContentView(binding.root)


        setupView()
        setupAction()
    }

    @Suppress("DEPRECATION")
    private fun setupView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun setupAction() {
        binding.btnPengguna.setOnClickListener {

            startActivity(Intent(this, LoginSiswaActivity::class.java))
            finish()
        }

        binding.btnAdmin.setOnClickListener {
            startActivity(Intent(this, LoginGuruActivity::class.java))
            finish()
        }
    }
}