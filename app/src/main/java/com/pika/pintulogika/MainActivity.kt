package com.pika.pintulogika

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.pika.pintulogika.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var firestore: FirebaseFirestore
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var navController: NavController

    @Suppress("DEPRECATION")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firestore = FirebaseFirestore.getInstance()
        firebaseAuth = FirebaseAuth.getInstance()
        bottomNavigationView = binding.bottomNavigation

        val window = this.window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)

        setupNavController()
        setupBottomNavigationView()
    }

    private fun setupNavController() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_fragment) as NavHostFragment
        navController = navHostFragment.navController
        navController.addOnDestinationChangedListener { _, destination, _ ->
            setupBottomNavigationView()
        }
    }

    private fun setupBottomNavigationView() {
        bottomNavigationView = binding.bottomNavigation
        bottomNavigationView.setOnItemSelectedListener  { item ->
            when (item.itemId) {
                R.id.item_materi -> {
                    navController.navigate(R.id.materiFragment)
                    true
                }
                R.id.item_simulasi -> {
                    navController.navigate(R.id.simulasiFragment)
                    true
                }
                R.id.item_kuis -> {
                    navController.navigate(R.id.kuisFragment)
                    true
                }
                R.id.item_tentang -> {
                    navController.navigate(R.id.tentangFragment)
                    true
                }
                else -> false
            }
        }
    }
}