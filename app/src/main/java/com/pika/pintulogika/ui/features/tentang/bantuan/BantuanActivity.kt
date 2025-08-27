package com.pika.pintulogika.ui.features.tentang.bantuan

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.pika.halaman_materi.databinding.ActivityMateriPembelajaranBinding
import com.pika.pintulogika.R
import com.pika.pintulogika.databinding.ActivityBantuanBinding

class BantuanActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBantuanBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityBantuanBinding.inflate(layoutInflater)
        setContentView(binding.root)


        setSupportActionBar(binding.toolbarPanduan)
        supportActionBar?.apply {
            title = "Bantuan & FAQ"
            setDisplayHomeAsUpEnabled(true)
        }

        binding.tvBantuanIsi.text = getFaqText()
    }

    private fun getFaqText(): CharSequence {
        return buildString {
            appendLine("📘  FAQ - Frequently Asked Questions\n")
            appendLine("Aplikasi Pintu Logika\n")
            appendLine("━━━━━━━━━━━━━━━━━━━━\n")
            appendLine("📌 Tips dan Strategi Pembelajaran")
            appendLine("Q: Bagaimana urutan belajar yang optimal di aplikasi ini?")
            appendLine("A: ")
            appendLine("- Mulai dengan Materi Pembelajaran")
            appendLine("- Lanjut ke Simulasi")
            appendLine("- Kuis Aljabar Boolean")
            appendLine("- Kuis Gerbang Logika\n")

            appendLine("Q: Bagaimana cara memaksimalkan pembelajaran dari fitur Materi?")
            appendLine("A:")
            appendLine("- Baca materi secara berurutan")
            appendLine("- Gunakan progress bar")
            appendLine("- Review materi sebelumnya")
            appendLine("- Navigasi via tombol Previous/Next")
            appendLine("- Tap link & gambar untuk info tambahan\n")

            appendLine("📌 Troubleshooting Umum")
            appendLine("Q: Konten tidak muncul?")
            appendLine("A:")
            appendLine("- Periksa koneksi internet")
            appendLine("- Scroll ulang atau restart aplikasi\n")

            appendLine("Q: Simulasi tidak responsif?")
            appendLine("A:")
            appendLine("- Tunggu animasi selesai")
            appendLine("- Restart level\n")

            appendLine("📌 Tips Performa & Optimasi")
            appendLine("Q: Aplikasi terasa lambat?")
            appendLine("A:")
            appendLine("- Tutup background apps")
            appendLine("- Restart aplikasi")
            appendLine("- Clear cache dan update app\n")

            appendLine("📌 Fitur Aksesibilitas")
            appendLine("Q: Apa saja fitur aksesibilitas?")
            appendLine("A:")
            appendLine("- Responsive design")
            appendLine("- Audio feedback")
            appendLine("- TalkBack support")
            appendLine("- High contrast mode\n")

            appendLine("❓ Pertanyaan Umum")
            appendLine("Q: Bisakah digunakan offline?")
            appendLine("A: Bisa, kecuali gambar online\n")

            appendLine("Q: Apakah progress hilang saat update?")
            appendLine("A: Tidak, selama tidak uninstall aplikasi\n")

            appendLine("Q: Bagaimana memberi feedback?")
            appendLine("A: Gunakan menu feedback atau hubungi via Google Play\n")

            appendLine("💡 Tips Akhir:")
            appendLine("Belajar dengan konsisten, jangan terburu-buru. Nikmati proses dan ulangi materi jika perlu. Semangat belajar logika digital! 🚀")
        }
    }
}