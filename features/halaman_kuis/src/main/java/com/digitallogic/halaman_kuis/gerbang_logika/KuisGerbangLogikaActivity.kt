package com.digitallogic.halaman_kuis.gerbang_logika

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.digitallogic.halaman_kuis.LevelLogicGates
import com.digitallogic.halaman_kuis.R
import com.digitallogic.halaman_kuis.SoundPlayer
import com.digitallogic.halaman_kuis.adapter.LevelAdapter
import com.digitallogic.halaman_kuis.databinding.ActivityKuisGerbangLogikaBinding

class KuisGerbangLogikaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityKuisGerbangLogikaBinding
    private lateinit var levelAdapter: LevelAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityKuisGerbangLogikaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Init Sound On/Off
        SoundPlayer.isSoundOn = loadSoundPreference()
        setSoundIcon(SoundPlayer.isSoundOn)

        setupRecyclerView()
        setupLevels()

        binding.btnBackSimulasi.setOnClickListener {
            SoundPlayer.playSound(this, com.pika.core_ui.R.raw.water_bubble)
            finish()
        }
        // Toggle sound
        binding.btnSound.setOnClickListener {
            Log.e("Sound", "Toggle Sound")
            val isOn = SoundPlayer.toggleSound()
            saveSoundPreference(isOn)
            setSoundIcon(isOn)
            SoundPlayer.playSound(this, com.pika.core_ui.R.raw.water_bubble)
        }

        binding.btnInfoKuisGerbangLogika.setOnClickListener {
            SoundPlayer.playSound(this, com.pika.core_ui.R.raw.water_bubble)
            showInfoDialog()
        }

    }

    override fun onResume() {
        super.onResume()
        setupLevels() // Refresh UI setiap kali aktivitas muncul
    }

    private fun setupRecyclerView() {
        binding.rvLevel.layoutManager = GridLayoutManager(this, 3)

        levelAdapter = LevelAdapter(this) { level ->
            startGameActivity(level)
        }
        binding.rvLevel.adapter = levelAdapter
    }

    private fun setupLevels() {
        val levels = (1..15).map { levelNumber ->
            LevelLogicGates(
                number = levelNumber,
                isUnlocked = levelNumber <= getCurrentUnlockedLevel(),
                stars = getStarsForLevel(levelNumber)
            )
        }
        levelAdapter.submitList(levels)
    }

    private fun startGameActivity(level: LevelLogicGates) {
        if (level.isUnlocked) {
            val intent = Intent(this, DetailKuisGerbangLogikaActivity::class.java)
            intent.putExtra("LEVEL_NUMBER", level.number)
            startActivity(intent)
        } else {
            Toast.makeText(this, "Level belum terbuka", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getCurrentUnlockedLevel(): Int {
        return getSharedPreferences("game_prefs", MODE_PRIVATE)
            .getInt("unlocked_level", 1)
    }

    private fun getStarsForLevel(levelNumber: Int): Int {
        return getSharedPreferences("game_prefs", MODE_PRIVATE)
            .getInt("stars_level_$levelNumber", 0)
    }

    private fun loadSoundPreference(): Boolean {
        return getSharedPreferences("prefs", MODE_PRIVATE)
            .getBoolean("sound_on", true)
    }

    private fun saveSoundPreference(isOn: Boolean) {
        getSharedPreferences("prefs", MODE_PRIVATE)
            .edit()
            .putBoolean("sound_on", isOn)
            .apply()
    }

    private fun setSoundIcon(isOn: Boolean) {
        binding.btnSound.setImageResource(
            if (isOn) com.pika.core_ui.R.drawable.ic_sound_on_24 else com.pika.core_ui.R.drawable.ic_sound_off_24
        )
    }

    private fun showInfoDialog(){
        val message = """
            🎮 CARA BERMAIN LOGIC GATE GAME
            
            🎯 Tujuan Utama:
            Atur input untuk menghasilkan output TRUE (1)
            
            🕹️ Kontrol:
            • Tap tombol input untuk toggle 0/1
            • Lihat alur data dari input ke output
            • Perhatikan jenis gerbang dan fungsinya
            
            📚 Jenis Gerbang:
            • AND: Output 1 jika SEMUA input 1
            • OR: Output 1 jika ADA SATU input 1  
            • NOT: Membalik nilai input
            • NAND: Kebalikan dari AND
            • NOR: Kebalikan dari OR
            • XOR: Output 1 jika input BERBEDA
            
            💡 Tips Umum:
            1. Mulai dari input, ikuti alur ke output
            2. Pahami fungsi setiap gerbang
            3. Gunakan trial and error untuk belajar
            4. Level makin tinggi makin kompleks!
            
            🏆 Selamat bermain dan berlatih logika digital!
        """.trimIndent()

        AlertDialog.Builder(this)
            .setTitle("Panduan Gamifikasi Gerbang Logika")
            .setMessage(message)
            .setPositiveButton("Tutup") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
}