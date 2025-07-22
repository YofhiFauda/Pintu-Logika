package com.digitallogic.halaman_kuis.aljabar_boolean


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

    }

    private fun setupStage(stage: Int) {
        val stageData = GameStageManager.getStage(stage)
        binding.tvScore.text = "Score: $score"
        binding.tvLogic.text = renderDeskripsiDenganWarna(stageData.description)
        binding.textHasil.text = "Pilih Jawaban Sesuai Kotak"
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

    private fun dpToPx(dp: Int): Int {
        val scale = resources.displayMetrics.density
        return (dp * scale + 0.5f).toInt()
    }

}

