package com.digitallogic.halaman_simulasi.ui.gerbang_logika

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.digitallogic.core_data.model.LogicSimulasi
import com.digitallogic.halaman_simulasi.databinding.ActivitySimulasiGerbangLogikaBinding
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.core.view.ViewCompat
import com.digitallogic.halaman_simulasi.adapter.LogicSimulasiAdapter

class SimulasiGerbangLogikaActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySimulasiGerbangLogikaBinding

    private val logicList = listOf(
        LogicSimulasi("LOGIKA AND", "Simulasi gerbang logika AND", com.pika.core_ui.R.drawable.ic_gate_and),
        LogicSimulasi("LOGIKA OR", "Simulasi gerbang logika OR", com.pika.core_ui.R.drawable.ic_gate_or),
        LogicSimulasi("LOGIKA NOT", "Simulasi gerbang logika NOT", com.pika.core_ui.R.drawable.ic_gate_not),
        LogicSimulasi("LOGIKA NAND", "Simulasi gerbang logika NAND", com.pika.core_ui.R.drawable.ic_gate_nand),
        LogicSimulasi("LOGIKA NOR", "Simulasi gerbang logika NOR", com.pika.core_ui.R.drawable.ic_gate_nor),
        LogicSimulasi("LOGIKA XOR", "Simulasi gerbang logika XOR", com.pika.core_ui.R.drawable.ic_gate_xor),
        LogicSimulasi("LOGIKA XNOR", "Simulasi gerbang logika XNOR", com.pika.core_ui.R.drawable.ic_gate_xnor)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySimulasiGerbangLogikaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnInfoSimulasi.setOnClickListener {
            showInfoDialog()
        }

        setupRecyclerView()
        setupWindowInsets()
    }

    private fun showInfoDialog() {
        val message = """
            Panduan Penggunaan Simulasi Gerbang Logika
            
            Pengenalan
            Aplikasi simulasi ini membantu Anda memahami cara kerja gerbang logika digital dengan cara yang interaktif dan mudah dipahami. Setiap gerbang memiliki aturan sendiri dalam memproses sinyal input menjadi output.

            Cara Menggunakan Simulasi
            1. Memilih Gerbang Logika
            Pilih jenis gerbang logika yang ingin Anda pelajari dari menu utama aplikasi.

            2. Memahami Interface Simulasi
            Setelah masuk ke halaman simulasi, Anda akan melihat:
            - Switch (Saklar): Tombol merah dan hijau untuk mengatur input A dan B
            - Gambar Gerbang: Simbol gerbang logika yang sedang dipelajari
            - Lampu: Menunjukkan output (nyala = 1, mati = 0)
            - Tabel Kebenaran: Menampilkan semua kemungkinan kombinasi input-output

            3. Cara Mengoperasikan
            - Atur Input: Geser switch ke kiri (OFF/0) atau kanan (ON/1)
            - Lihat Output: Perhatikan apakah lampu menyala atau mati
            - Cek Tabel: Baris yang sesuai dengan input Anda akan menyala kuning
            - Baca Penjelasan: Lihat rumus logika di bagian atas layar

            Tips Belajar Efektif
            1. Mulai dari Dasar
               Pelajari AND, OR, dan NOT terlebih dahulu
               Pahami konsep 0 (OFF/False) dan 1 (ON/True)

            2. Praktek Interaktif
               Coba semua kombinasi switch untuk setiap gerbang
               Perhatikan pola dalam tabel kebenaran

            3. Gunakan Analogi
               Hubungkan dengan kehidupan sehari-hari
               Bayangkan sebagai saklar lampu atau pintu

            4. Pahami Pola
               NAND = kebalikan AND
               NOR = kebalikan OR
               XNOR = kebalikan XOR

            5. Latihan Berulang
               Ulangi simulasi beberapa kali
               Coba prediksi output sebelum mengubah switch

            Troubleshooting
            Jika Simulasi Tidak Berfungsi:
            - Pastikan switch dapat digeser dengan lancar
            - Periksa apakah tabel kebenaran ter-highlight dengan benar
            - Restart aplikasi jika diperlukan

            Jika Bingung dengan Hasil:
            - Lihat kembali tabel kebenaran
            - Bandingkan dengan rumus logika yang ditampilkan
            - Coba analogi sederhana untuk setiap gerbang
        """.trimIndent()

        AlertDialog.Builder(this)
            .setTitle("Panduan Penggunaan Simulasi")
            .setMessage(message)
            .setPositiveButton("Tutup") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun setupRecyclerView() {
        val adapter = LogicSimulasiAdapter(logicList) { selectedItem ->
            val intent = Intent(this, SimulasiDetailActivity::class.java)
            intent.putExtra("GATE_TYPE", selectedItem.title)
            startActivity(intent)
        }

        binding.rvLogicSimulasi.layoutManager = GridLayoutManager(this, 2)
        binding.rvLogicSimulasi.adapter = adapter
    }

    private fun setupWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())

            view.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                0
            )

            binding.rvLogicSimulasi.setPadding(
                16,
                16,
                16,
                systemBars.bottom + 16
            )
            insets
        }
    }
}