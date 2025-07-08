package com.digitallogic.halaman_kuis.aljabar_boolean

import android.graphics.Color
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Button
import android.widget.GridLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.digitallogic.halaman_kuis.ColorType
import com.digitallogic.halaman_kuis.GameStageManager
import com.digitallogic.halaman_kuis.GridItem
import com.digitallogic.halaman_kuis.databinding.ActivityKuisAljabarBooleanBinding

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

        binding.btnSubmit.setOnClickListener {
            checkAnswer()
        }

        binding.btnBackSimulasi.setOnClickListener {
            finish()
        }

    }

    private fun setupStage(stage: Int) {
        val stageData = GameStageManager.getStage(stage)
        binding.tvScore.text = "Score: $score"
        binding.tvLogic.text = stageData.description
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
                    setBackgroundColor(getColorByType(color))
                    setOnClickListener {
                        item.isSelected = !item.isSelected
                        background.setTint(if (item.isSelected) Color.BLACK else getColorByType(color))
                    }
                }

                val params = GridLayout.LayoutParams().apply {
                    width = 200
                    height = 200
                    setMargins(8, 8, 8, 8)
                }

                binding.gridLayout.addView(button, params)
            }
            gridItems.add(row)
        }
    }

    private fun getColorByType(type: ColorType): Int {
        return when (type) {
            ColorType.MERAH -> Color.RED
            ColorType.HIJAU -> Color.GREEN
            ColorType.ORANYE -> Color.rgb(255, 165, 0)
        }
    }

    private fun checkAnswer() {
        val stageData = GameStageManager.getStage(currentStage)
        var isAllCorrect = true

        for (i in 0..2) {
            for (j in 0..2) {
                val item = gridItems[i][j]
                val shouldBeSelected = stageData.logicFunction(item)

                if (item.isSelected != shouldBeSelected) {
                    isAllCorrect = false
                    break
                }
            }
        }

        if (isAllCorrect) {
            score += 5
            currentStage++
            if (currentStage <= GameStageManager.totalStages) {
                setupStage(currentStage)
            } else {
                Toast.makeText(this, "Permainan Selesai!", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Jawaban salah!", Toast.LENGTH_SHORT).show()
        }
    }
}
