package com.digitallogic.halaman_kuis.aljabar_boolean


import android.app.AlertDialog
import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.widget.Button
import android.widget.GridLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.digitallogic.halaman_kuis.ColorType
import com.digitallogic.halaman_kuis.GameStageManager
import com.digitallogic.halaman_kuis.GridItem
import com.digitallogic.halaman_kuis.databinding.ActivityKuisAljabarBooleanBinding
import com.pika.core_ui.R
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ImageSpan
import android.graphics.drawable.Drawable
import androidx.core.content.res.ResourcesCompat


class KuisAljabarBooleanActivity : AppCompatActivity() {

    private lateinit var binding: ActivityKuisAljabarBooleanBinding
    private var currentStage = 1
    private var score = 0 // <-- skor awal
    private val gridItems = mutableListOf<MutableList<GridItem>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityKuisAljabarBooleanBinding.inflate(layoutInflater)
        setContentView(binding.root)


        setupStage(currentStage)

        binding.btnBackSimulasi.setOnClickListener {
            finish()
        }

        binding.btnInfoKuisAljabarBoolean.setOnClickListener {
            showInfoDialog()
        }

    }

    private fun setupStage(stage: Int) {
        val stageData = GameStageManager.getStage(stage)
        binding.tvScore.text = "Score: $score"
        binding.tvLogic.text = renderDeskripsiDenganWarna(stageData.description)
        binding.textHasil.text = stageData.explanation
        binding.gridLayout.removeAllViews()
        gridItems.clear()

        // Buat grid yang memastikan setiap warna memiliki angka 1,2,3 masing-masing sekali
        val gridData = mutableListOf<Pair<Int, ColorType>>()

        // Untuk setiap warna, tambahkan angka 1,2,3
        ColorType.values().forEach { color ->
            gridData.add(Pair(1, color))
            gridData.add(Pair(2, color))
            gridData.add(Pair(3, color))
        }

        // Acak urutan untuk variasi posisi
        gridData.shuffle()

        var index = 0
        for (i in 0..2) {
            val row = mutableListOf<GridItem>()
            for (j in 0..2) {
                val (number, color) = gridData[index]
                index++

                val item = GridItem(number, color)
                row.add(item)

                val button = Button(this).apply {
                    text = number.toString()
                    textSize = 24f
                    setTypeface(null, Typeface.BOLD)
                    background = ContextCompat.getDrawable(context, getSelectorDrawable(color))
                    setTextColor(
                        ContextCompat.getColorStateList(
                            context,
                            getSelectorTextColor(color)
                        )
                    ) // ← ini pakai selector!
                    gravity = Gravity.CENTER

                    setOnClickListener {
                        item.isSelected = !item.isSelected
                        isSelected = item.isSelected // trigger selector state


                        checkAnswer()
                    }
                }


                val params = GridLayout.LayoutParams().apply {
                    width = 300
                    height = 300
                    setMargins(8, 8, 8, 8)
                }

                binding.gridLayout.addView(button, params)
            }
            gridItems.add(row)
        }
    }

    private fun getSelectorTextColor(color: ColorType): Int {
        return when (color) {
            ColorType.MERAH -> R.color.text_color_red_selector
            ColorType.HIJAU -> R.color.text_color_green_selector
            ColorType.ORANYE -> R.color.text_color_orange_selector
        }
    }


    private fun getSelectorDrawable(type: ColorType): Int {
        return when (type) {
            ColorType.MERAH -> R.drawable.bg_red_selector
            ColorType.HIJAU -> R.drawable.bg_green_selector
            ColorType.ORANYE -> R.drawable.bg_orange_selector
        }
    }


    private fun checkAnswer() {
        val stageData = GameStageManager.getStage(currentStage)

        for (i in 0..2) {
            for (j in 0..2) {
                val item = gridItems[i][j]
                val shouldBeSelected = stageData.logicFunction(item)

                if (item.isSelected && !shouldBeSelected) {
                    binding.textHasil.text = "Jawaban Anda Salah!"

                    // ⏳ Delay sebelum reset
                    lifecycleScope.launch {
                        delay(1500)
                        currentStage = 1
                        score = 0
                        setupStage(currentStage)
                    }
                    return
                }
            }
        }

        val isAllCorrect = gridItems.flatten().all {
            val shouldBeSelected = stageData.logicFunction(it)
            !shouldBeSelected || (shouldBeSelected && it.isSelected)
        }

        if (isAllCorrect) {
            binding.textHasil.text = "Jawaban Anda Benar"
            score += 5
            currentStage++

            lifecycleScope.launch {
                delay(1500)
                if (currentStage <= GameStageManager.totalStages) {
                    setupStage(currentStage)
                } else {
                    Toast.makeText(
                        this@KuisAljabarBooleanActivity,
                        "Permainan Selesai!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun renderDeskripsiDenganWarna(rawText: String): SpannableStringBuilder {
        val builder = SpannableStringBuilder(rawText)

        val colorMap = mapOf(
            "merah" to R.drawable.ic_dot_red,
            "hijau" to R.drawable.ic_dot_green,
            "oranye" to R.drawable.ic_dot_orange
        )

        for ((colorWord, drawableRes) in colorMap) {
            var index = builder.indexOf(colorWord)
            while (index >= 0) {
                val drawable = ResourcesCompat.getDrawable(resources, drawableRes, null)?.apply {
                    setBounds(0, 0, dpToPx(20), dpToPx(20)) // ukuran ikon 12px (setara 5dp kira-kira)
                }

                if (drawable != null) {
                    val imageSpan = ImageSpan(drawable, ImageSpan.ALIGN_BOTTOM)
                    builder.setSpan(imageSpan, index, index + colorWord.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                }

                index = builder.indexOf(colorWord, index + 1)
            }
        }

        return builder
    }

    private fun showInfoDialog(){
        val message = """
            Apa itu Game Aljabar Boolean?
            Game ini adalah permainan logika dimana pemain harus memilih kotak-kotak berwarna berdasarkan aturan logika Boolean. Mirip seperti puzzle matematika, tapi menggunakan warna dan angka sebagai elemen permainan.
            
            Komponen Utama Game
            
            1. Grid Permainan (9 Kotak)
              - Terdiri dari 9 kotak yang disusun dalam bentuk 3x3
              - Setiap kotak memiliki:
                - Angka: 1, 2, atau 3
                - Warna: Merah 🔴, Hijau 🟢, atau Oranye 🟠
              - Pemain bisa mengklik kotak untuk memilih/tidak memilih
            
            2. Sistem Skor
              - Skor awal: 0
              - Setiap jawaban benar: +5 poin
              - Jika salah: skor kembali ke 0 dan mulai dari stage 1
            
            3. Tahapan Permainan (Stages)
              - Game memiliki beberapa level/stage
              - Setiap stage memiliki aturan logika berbeda
              - Semakin tinggi stage, semakin sulit
            
            Cara Bermain
            
            Langkah 1: Pahami Aturan
            Di bagian atas layar, akan muncul instruksi logika, contoh:
            "Pilih semua kotak merah ATAU kotak dengan angka 1"
            
            Langkah 2: Analisis Grid
            Lihat 9 kotak yang tersedia dan identifikasi:
              - Warna setiap kotak
              - Angka setiap kotak
              - Kotak mana yang sesuai dengan aturan
            
            Langkah 3: Pilih Kotak
              - Klik kotak yang sesuai dengan aturan logika
              - Kotak yang dipilih akan berubah tampilan (highlight)
              - Bisa memilih lebih dari satu kotak
            
            Langkah 4: Sistem Penilaian Otomatis
            Game akan otomatis mengecek jawaban setiap kali Anda mengklik kotak:
              - Benar: Lanjut ke stage berikutnya dengan bonus +5 poin
              - Salah: Kembali ke stage 1 dengan skor 0
            
            Fitur Game
            
            1. Tampilan Visual
              - Warna-warni: Setiap warna (merah, hijau, oranye) punya tampilan khusus
              - Feedback Visual: Kotak berubah warna saat dipilih
              - Ikon Warna: Instruksi menggunakan ikon dot berwarna untuk memperjelas
            
            2. Interaksi Responsif
              - Klik untuk memilih/batal memilih kotak
              - Animasi dan perubahan warna saat interaksi
              - Toast message untuk feedback
            
            3. Navigasi
              - Tombol "Back" untuk keluar dari game
              - Pergantian stage otomatis setelah jawaban benar
            
            
            Logika Boolean dalam Game
            
            Konsep Dasar:
              1. AND: Kedua kondisi harus benar
                - Contoh: "Pilih kotak merah AND angka 1" → hanya kotak yang merah DAN berangka 1
                
              2. OR: Salah satu kondisi benar sudah cukup
                - Contoh: "Pilih kotak merah OR angka 1" → kotak merah atau berangka 1 (atau keduanya)
            
              3. NOT: Kebalikan dari kondisi
                - Contoh: "Pilih NOT merah" → pilih semua kecuali yang merah
            
            
            Contoh Aturan Game:
              - "Pilih semua kotak hijau"
              - "Pilih kotak dengan angka 2 atau 3"
              - "Pilih kotak yang BUKAN oranye"
              - "Pilih kotak merah DAN angka 1"
            
            Strategi Menang
            1. Baca Instruksi dengan Teliti
            Pahami setiap kata dalam instruksi logika, terutama kata kunci:
            
            "ATAU" / "OR"
            "DAN" / "AND"
            "TIDAK" / "NOT"
            "SEMUA"
            
            2. Analisis Sistematis
            
            Buat daftar mental kotak yang memenuhi syarat
            Periksa satu per satu sebelum mengklik
            Jangan terburu-buru
            
            3. Pahami Konsekuensi
            
            Satu kesalahan = mulai dari awal
            Lebih baik lambat tapi benar
            Perhatikan perubahan aturan setiap stage
            
            
            Tips untuk Pemula
            
            Mulai dengan Stage Mudah: Pahami pola dasar dulu
            Visualisasikan: Gambar mental kotak mana yang harus dipilih
            Double Check: Periksa kembali sebelum mengklik kotak terakhir
            Sabar: Jangan panik jika harus mengulang dari awal
            Pelajari Pola: Setiap stage punya pola logika tertentu
            
            
            Manfaat Bermain Game Ini
            
            Untuk Pembelajaran:
            Logika Berpikir: Meningkatkan kemampuan analisis
            Pemecahan Masalah: Belajar pendekatan sistematis
            Matematika Boolean: Pemahaman konsep AND, OR, NOT
            Konsentrasi: Melatih fokus dan ketelitian
            
            
            Troubleshooting
            Jika Game Tidak Merespons:
            Pastikan mengklik tepat di tengah kotak
            Tunggu sejenak antara klik
            
            Jika Bingung dengan Aturan:
            Baca instruksi pelan-pelan
            Coba identifikasi kata kunci logika
            Mulai dengan kotak yang paling jelas memenuhi syarat
            
            Jika Sering Salah:
            Jangan terburu-buru
            Periksa setiap kotak sebelum diklik
            Pahami perbedaan AND dan OR
        """.trimIndent()

        AlertDialog.Builder(this)
            .setTitle("Panduan Gamifikasi Aljabar Boolean")
            .setMessage(message)
            .setPositiveButton("Tutup") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun dpToPx(dp: Int): Int {
        val scale = resources.displayMetrics.density
        return (dp * scale + 0.5f).toInt()
    }

}

