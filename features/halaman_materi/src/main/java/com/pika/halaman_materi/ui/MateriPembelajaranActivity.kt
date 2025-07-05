package com.pika.halaman_materi.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.lifecycleScope
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.pika.halaman_materi.R
import com.pika.halaman_materi.data.model.SubModul
import com.pika.halaman_materi.data.modul_list.ModulListFragment
import com.pika.halaman_materi.data.session.ProgressPreferences
import com.pika.halaman_materi.databinding.ActivityMateriPembelajaranBinding
import kotlinx.coroutines.launch

class MateriPembelajaranActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMateriPembelajaranBinding
    private val firestore = FirebaseFirestore.getInstance()
    private val subModulList = mutableListOf<SubModul>()
    private var currentIndex = 0

    private lateinit var materiId: String
    private lateinit var modulId: String
    private var judulSubmodul: String = ""

    private val statusManager by lazy { ProgressPreferences(this) }

    private val FRAGMENT_TAG = "ModulListFragment"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMateriPembelajaranBinding.inflate(layoutInflater)
        setContentView(binding.root)

        materiId = intent.getStringExtra("materi_id") ?: run {
            Toast.makeText(this, "materi_id tidak ditemukan", Toast.LENGTH_SHORT).show()
            finish(); return
        }

        modulId = intent.getStringExtra("modul_id") ?: run {
            Toast.makeText(this, "modul_id tidak ditemukan", Toast.LENGTH_SHORT).show()
            finish(); return
        }

        judulSubmodul = intent.getStringExtra("judul_submodul") ?: ""
        currentIndex = subModulList.indexOfFirst { it.nama == judulSubmodul }.coerceAtLeast(0)

        setupToolbar()
        setupEdgeToEdge()
        setupBottomNavigation()
        setupScrollBehavior()

        fetchSubModulFromFirestore()

        binding.btnDaftarMenu.setOnClickListener {
            Log.d("Activity", "materiId yang dikirim: $materiId") // Tambahkan ini
            val existingFragment = supportFragmentManager.findFragmentByTag(FRAGMENT_TAG)
            if (existingFragment != null && existingFragment.isVisible) {
                supportFragmentManager.popBackStack()
            } else {
                val fragment = ModulListFragment.newInstance(materiId)
                supportFragmentManager.beginTransaction()
                    .setCustomAnimations(
                        com.pika.core_ui.R.anim.slide_in_right,
                        com.pika.core_ui.R.anim.slide_out_right,
                        com.pika.core_ui.R.anim.slide_in_right,
                        com.pika.core_ui.R.anim.slide_out_right
                    )
                    .add(R.id.fragment_container, fragment, FRAGMENT_TAG)
                    .addToBackStack(null)
                    .commit()
            }
        }
    }

    private fun fetchSubModulFromFirestore() {
        val path = "modul/$materiId/list_modul"

        firestore.collection(path)
            .orderBy("timestamp")
            .get()
            .addOnSuccessListener { modulDocs ->
                val allFetchTasks =
                    mutableListOf<com.google.android.gms.tasks.Task<QuerySnapshot>>()

                subModulList.clear()

                for (modulDoc in modulDocs) {
                    val namaModul = modulDoc.getString("nama_modul") ?: continue
                    val modulId = namaModul.replace(" ", "_").lowercase()

                    val task = firestore.collection("materi_$materiId")
                        .document(modulId)
                        .collection("sub_modul")
                        .get()

                    allFetchTasks.add(task)
                }

                com.google.android.gms.tasks.Tasks.whenAllSuccess<QuerySnapshot>(allFetchTasks)
                    .addOnSuccessListener { allResults ->
                        for (result in allResults) {
                            for (doc in result.documents) {
                                val nama = doc.getString("nama_sub_modul") ?: continue
                                val judul = doc.getString("judul_materi") ?: ""
                                val konten = doc.getString("konten") ?: ""
                                val tanggal = doc.getString("tanggal_upload") ?: ""

                                subModulList.add(SubModul(nama, judul, konten, tanggal))
                            }
                        }

                        // Urutkan secara opsional berdasarkan tanggal atau alfabet
                        if (judulSubmodul.isNotEmpty()) {
                            currentIndex = subModulList.indexOfFirst { it.nama == judulSubmodul }
                                .coerceAtLeast(0)
                        } else {
                            currentIndex = 0
                        }

                        if (subModulList.isNotEmpty()) {
                            updateContent(subModulList[currentIndex])
                            updateNavigationButtons()
                        } else {
                            Toast.makeText(this, "Tidak ada submodul ditemukan", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Gagal memuat submodul", Toast.LENGTH_SHORT).show()
                    }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Gagal memuat modul utama", Toast.LENGTH_SHORT).show()
            }
    }


    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(false)
            setDisplayShowHomeEnabled(false)
            setDisplayShowTitleEnabled(false)
        }

        binding.btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.btnSearch.setOnClickListener {
            Toast.makeText(this, "Tombol Pencarian DiKlik", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateContent(subModul: SubModul) {
        binding.contentTitle.text = subModul.judul
        binding.contentText.text = subModul.konten
        binding.bottomTitle.text = subModul.judul
        binding.nestedScrollView.smoothScrollTo(0, 0)

        // Simpan bahwa submodul sudah dibaca
        lifecycleScope.launch {
            statusManager.markAsRead(subModul.nama) // gunakan subModul.nama atau ID unik
        }
    }


    private fun setupBottomNavigation() {
        binding.btnPrevious.setOnClickListener {
            if (currentIndex > 0) {
                currentIndex--
                updateContent(subModulList[currentIndex])
                updateNavigationButtons()

                // ✅ Tandai juga sebagai sudah dibaca
                lifecycleScope.launch {
                    statusManager.markAsRead(subModulList[currentIndex].nama)
                }
            } else {
                Toast.makeText(this, "Ini adalah konten pertama", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnNext.setOnClickListener {
            if (currentIndex < subModulList.size - 1) {
                currentIndex++
                updateContent(subModulList[currentIndex])
                updateNavigationButtons()

                lifecycleScope.launch {
                    statusManager.markAsRead(subModulList[currentIndex].nama)
                }
            } else {
                Toast.makeText(this, "Ini adalah konten terakhir", Toast.LENGTH_SHORT).show()
            }
        }

        updateNavigationButtons()
    }

    private fun updateNavigationButtons() {
        binding.btnPrevious.apply {
            alpha = if (currentIndex > 0) 1.0f else 0.5f
            isEnabled = currentIndex > 0
        }
        binding.btnNext.apply {
            alpha = if (currentIndex < subModulList.size - 1) 1.0f else 0.5f
            isEnabled = currentIndex < subModulList.size - 1
        }
    }

    private fun setupScrollBehavior() {
        binding.nestedScrollView.setOnScrollChangeListener { _: NestedScrollView?, _: Int, scrollY: Int, _: Int, _: Int ->
            val scrollRange =
                binding.nestedScrollView.getChildAt(0).height - binding.nestedScrollView.height
            if (scrollRange > 0) {
                val scrollProgress = (scrollY.toFloat() / scrollRange * 100).toInt()
                // Bisa digunakan untuk indikator progress
            }
        }
    }

    private fun setupEdgeToEdge() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { _, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            val navigationBars = insets.getInsets(WindowInsetsCompat.Type.navigationBars())
            binding.bottomNavigationContainer.updatePadding(bottom = navigationBars.bottom)
            binding.root.updatePadding(top = systemBars.top)
            insets
        }

        window.statusBarColor = android.graphics.Color.TRANSPARENT
        window.navigationBarColor = android.graphics.Color.TRANSPARENT
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
            window.setDecorFitsSystemWindows(false)
        } else {
            @Suppress("DEPRECATION")
            window.decorView.systemUiVisibility = (
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                            View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    )
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}