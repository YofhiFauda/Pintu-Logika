package com.pika.pintulogika.ui.auth.guru

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.pika.pintulogika.databinding.ActivityLoginGuruBinding
import com.pika.pintulogika.data.session.SessionManager
import com.pika.pintulogika.ui.preauth.role.RoleActivity
import androidx.lifecycle.lifecycleScope
import com.pika.pintulogika.MainActivity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class LoginGuruActivity : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var binding: ActivityLoginGuruBinding
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
        sessionManager = SessionManager(applicationContext)

        // Cek apakah siswa sudah login
        lifecycleScope.launch {
            val session = sessionManager.sessionState.first()
            if (session.isLoggedIn && session.role == "guru") {
                // Sudah login → langsung ke dashboard
                startMainActivity()
            } else {
                // Belum login → tampilkan form login
                binding = ActivityLoginGuruBinding.inflate(layoutInflater)
                setContentView(binding.root)
                setupLogin()
                setupAction()
            }
        }
    }

    private fun setupLogin() = with(binding) {
        btnMasukLoginAdmin.setOnClickListener {
            val email    = etEmailLoginAdmin.text.toString().trim()
            val password = etPasswordAdmin.text.toString().trim()

            when {
                email.isEmpty()    -> etEmailLoginAdmin.error   = "Email tidak boleh kosong"
                password.isEmpty() -> etPasswordAdmin.error     = "Password tidak boleh kosong"
                else -> {
                    lifecycleScope.launch {
                        try {
                            firebaseAuth.signInWithEmailAndPassword(email, password).await()
                            // ── Jika sukses, simpan session
                            val uid = firebaseAuth.currentUser?.uid ?: ""
                            sessionManager.saveGuruSession(uid, email)

                            Toast.makeText(this@LoginGuruActivity,
                                "Login sukses", Toast.LENGTH_SHORT).show()

                            startMainActivity()
                        } catch (e: Exception) {
                            Toast.makeText(
                                this@LoginGuruActivity,
                                "Login gagal: ${e.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }
    }

    private fun startMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun setupAction() {
        binding.btnBackLoginGuru.setOnClickListener {
            startActivity(Intent(this, RoleActivity::class.java))
            finish()
        }
    }
}