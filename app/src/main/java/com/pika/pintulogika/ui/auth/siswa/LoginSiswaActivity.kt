package com.pika.pintulogika.ui.auth.siswa

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.pika.pintulogika.databinding.ActivityLoginSiswaBinding
import com.pika.pintulogika.data.session.SessionManager
import androidx.lifecycle.lifecycleScope
import com.pika.pintulogika.MainActivity
import com.pika.pintulogika.ui.preauth.role.RoleActivity
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.first


class LoginSiswaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginSiswaBinding
    private lateinit var firestore: FirebaseFirestore
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
        sessionManager = SessionManager(applicationContext)

        // Cek apakah siswa sudah login
        lifecycleScope.launch {
            val session = sessionManager.sessionState.first()
            if (session.isLoggedIn && session.role == "siswa") {
                // Sudah login → langsung ke dashboard
                redirectDashboard()
            } else {
                // Belum login → tampilkan form login
                binding = ActivityLoginSiswaBinding.inflate(layoutInflater)
                setContentView(binding.root)
                setupLogin()
                setupExit()
            }
        }
    }

    private fun setupLogin() = with(binding) {
        btnSimpan.setOnClickListener {
            val nama  = etNama.text.toString().uppercase()
            val kelas = etKelas.text.toString().uppercase()

            when {
                nama.isEmpty()  -> etNama.error  = "Nama tidak boleh kosong"
                kelas.isEmpty() -> etKelas.error = "Kelas tidak boleh kosong"
                else -> {
                    // Login anonymous Firebase dulu
                    firebaseAuth.signInAnonymously()
                        .addOnSuccessListener { authResult ->
                            val uid = authResult.user?.uid ?: return@addOnSuccessListener

                            val userId = "${nama}_${kelas}"
                            val userMap = hashMapOf(
                                "nama" to nama,
                                "kelas" to kelas,
                                "role" to "siswa",
                                "uid" to uid
                            )

                            firestore.collection("users")
                                .document("kelas:$kelas")
                                .collection(nama)
                                .document(userId)
                                .set(userMap)
                                .addOnSuccessListener {
                                    lifecycleScope.launch {
                                        sessionManager.saveSiswaSession(nama, kelas, userId)
                                        Toast.makeText(
                                            this@LoginSiswaActivity,
                                            "Login berhasil sebagai anonymous",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        redirectDashboard()
                                    }
                                }
                                .addOnFailureListener {
                                    Toast.makeText(
                                        this@LoginSiswaActivity,
                                        "Gagal menyimpan data: ${it.message}",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                        }
                        .addOnFailureListener {
                            Toast.makeText(
                                this@LoginSiswaActivity,
                                "Gagal login anonymous: ${it.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                }
            }
        }
    }



    private fun redirectDashboard() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun setupExit(){
        binding.btnExit.setOnClickListener {
            val intent = Intent(this, RoleActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}