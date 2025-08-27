package com.pika.pintulogika.ui.features.tentang.panduan

import android.graphics.Typeface
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.pika.pintulogika.R
import com.pika.pintulogika.databinding.ActivityBantuanBinding
import com.pika.pintulogika.databinding.ActivityPanduanBinding

class PanduanActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPanduanBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityPanduanBinding.inflate(layoutInflater)
        setContentView(binding.root)


        setSupportActionBar(binding.toolbarPanduan)
        supportActionBar?.apply {
            title = "Panduan Penggunaan"
            setDisplayHomeAsUpEnabled(true)
        }
        addContent()
    }

    private fun addContent() {
        val container = binding.contentContainer

        container.addView(createTextView("Panduan Penggunaan Aplikasi Pintu Logika", 24f, true))

        container.addView(createTextView("Daftar Isi", 20f, true))
        container.addView(createTextView(
            "1. Pengenalan Aplikasi\n" +
                    "2. Fitur Materi Pembelajaran\n" +
                    "3. Fitur Simulasi Gerbang Logika\n" +
                    "4. Fitur Kuis Gerbang Logika\n" +
                    "5. Fitur Kuis Aljabar Boolean", 16f, false))

        // Pengenalan Aplikasi
        container.addView(createTextView("Pengenalan Aplikasi", 20f, true))
        container.addView(createTextView(
            "Pintu Logika adalah aplikasi pembelajaran interaktif yang membantu Anda memahami konsep gerbang logika digital. Aplikasi ini menyediakan empat fitur utama:\n\n" +
                    "- Materi Pembelajaran: Konten edukatif dengan sistem progress tracking\n" +
                    "- Simulasi Gerbang Logika: Simulasi interaktif untuk memahami cara kerja berbagai gerbang logika\n" +
                    "- Kuis Gerbang Logika: Tantangan puzzle logika dengan 15 level kompleks\n" +
                    "- Kuis Aljabar Boolean: Game logika dengan 30 stage berbeda tingkat kesulitan", 16f, false))

        // Fitur Materi Pembelajaran
        container.addView(createTextView("Fitur Materi Pembelajaran", 20f, true))
        container.addView(createTextView("Mengakses Materi", 18f, true))
        container.addView(createTextView(
            "1. Membuka Daftar Modul\n" +
                    "- Buka aplikasi dan pilih materi yang ingin dipelajari\n" +
                    "- Tap tombol \"Daftar Menu\" (ikon 3 garis) di toolbar untuk melihat semua modul\n" +
                    "- Daftar modul akan muncul dalam bentuk fragment overlay\n\n" +
                    "2. Navigasi Modul\n" +
                    "- Setiap modul memiliki ikon expand/collapse untuk menampilkan/menyembunyikan submodul\n" +
                    "- Tap pada nama modul untuk membuka/tutup daftar submodul\n" +
                    "- Submodul yang sudah dibaca ditandai dengan ikon centang berwarna hijau\n" +
                    "- Submodul yang belum dibaca ditandai dengan ikon centang berwarna abu-abu", 16f, false))

        container.addView(createTextView("Membaca Konten", 18f, true))
        container.addView(createTextView(
            "1. Memilih Submodul\n" +
                    "- Tap pada nama submodul untuk membuka halaman pembelajaran\n" +
                    "- Halaman akan menampilkan konten dinamis (teks, gambar, link)\n" +
                    "- Konten otomatis disimpan sebagai \"sudah dibaca\"\n\n" +
                    "2. Navigasi Konten\n" +
                    "- Gunakan tombol Previous (←) dan Next (→) di bagian bawah untuk navigasi antar submodul\n" +
                    "- Judul submodul ditampilkan di bagian tengah bottom navigation\n" +
                    "- Scroll konten menggunakan gesture swipe vertikal\n\n" +
                    "3. Fitur Toolbar\n" +
                    "- Tombol Back: Kembali ke halaman sebelumnya\n" +
                    "- Tombol Daftar Menu: Membuka overlay daftar semua modul\n" +
                    "- Tombol Search: Fitur pencarian (dalam pengembangan)", 16f, false))

        // Fitur Simulasi Gerbang Logika
        container.addView(createTextView("Fitur Simulasi Gerbang Logika", 20f, true))
        container.addView(createTextView("Penggunaan Simulasi", 18f, true))
        container.addView(createTextView(
            "- Pilih menu \"Simulasi\" dari halaman utama aplikasi.\n" +
                    "- Anda akan diarahkan ke halaman simulasi yang menampilkan berbagai jenis gerbang logika seperti AND, OR, NOT, XOR, NAND, dan NOR.\n" +
                    "- Gunakan tombol switch input (0/1) di bawah masing-masing input untuk mengubah nilai logika.\n" +
                    "- Output akan langsung berubah berdasarkan input dan jenis gerbang logika yang dipilih.\n" +
                    "- Beberapa gerbang mendukung lebih dari dua input, yang bisa ditambah atau dikurangi menggunakan tombol (+) dan (–).", 16f, false))

        container.addView(createTextView("Interaktivitas Visual", 18f, true))
        container.addView(createTextView(
            "- Animasi jalur logika ditampilkan saat input berubah, memberikan visualisasi aliran data.\n" +
                    "- Output dinamis berubah dengan efek visual sehingga memudahkan pemahaman hubungan logika antar elemen.", 16f, false))

        // Fitur Kuis Gerbang Logika
        container.addView(createTextView("Fitur Kuis Gerbang Logika", 20f, true))
        container.addView(createTextView("Mekanisme Permainan", 18f, true))
        container.addView(createTextView(
            "- Terdapat 15 level kuis yang menantang pemahaman tentang gerbang logika.\n" +
                    "- Setiap level menampilkan puzzle logika berupa input dan output yang harus dicocokkan dengan gerbang yang benar.\n" +
                    "- Pemain harus menyusun konfigurasi logika yang tepat untuk menyelesaikan level tersebut.\n" +
                    "- Jika jawaban benar, pemain dapat melanjutkan ke level berikutnya.", 16f, false))

        container.addView(createTextView("Navigasi dan Interaksi", 18f, true))
        container.addView(createTextView(
            "- Gunakan gesture drag-and-drop untuk menyusun komponen logika di area permainan.\n" +
                    "- Tap tombol “Cek Jawaban” untuk mengetahui apakah rangkaian logika yang dibuat sudah benar.", 16f, false))

        // Fitur Kuis Aljabar Boolean
        container.addView(createTextView("Fitur Kuis Aljabar Boolean", 20f, true))
        container.addView(createTextView("Struktur Permainan", 18f, true))
        container.addView(createTextView(
            "- Tersedia 30 stage dengan tingkat kesulitan yang meningkat secara bertahap.\n" +
                    "- Setiap stage menampilkan ekspresi aljabar boolean yang harus disederhanakan atau dicocokkan dengan tabel kebenaran.\n" +
                    "- Pemain memilih jawaban dari beberapa opsi atau mengetikkan ekspresi hasil penyederhanaan.", 16f, false))

        container.addView(createTextView("Evaluasi dan Feedback", 18f, true))
        container.addView(createTextView(
            "- Sistem akan memberikan feedback langsung atas jawaban yang dipilih.\n" +
                    "- Jika salah, akan muncul penjelasan atau petunjuk untuk membantu memahami kesalahan.\n" +
                    "- Tersedia fitur 'Coba Lagi' agar pemain dapat memperbaiki jawabannya.", 16f, false))
    }


    private fun createTextView(text: String, textSize: Float, isBold: Boolean = false): TextView {
        return TextView(this).apply {
            this.text = text
            this.textSize = textSize
            setPadding(0, 16, 0, 8)
            if (isBold) setTypeface(null, Typeface.BOLD)
        }
    }

}