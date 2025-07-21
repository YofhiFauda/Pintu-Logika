package com.digitallogic.halaman_kuis.gerbang_logika

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.PointF
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.digitallogic.halaman_kuis.R
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.digitallogic.halaman_kuis.ConnectorView
import com.digitallogic.halaman_kuis.LevelConfig
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import com.digitallogic.halaman_kuis.SoundPlayer
import android.widget.Button
import android.widget.Toast
import androidx.core.view.setMargins
import com.digitallogic.halaman_kuis.GateConfig
import com.pika.core_ui.R as CoreUiR
import com.digitallogic.halaman_kuis.LevelManager
import kotlin.random.Random


class DetailKuisGerbangLogikaActivity : AppCompatActivity() {

    private var currentLevel = 1
    private lateinit var connectorView: ConnectorView
    private lateinit var manualLevelContainer: FrameLayout
    private lateinit var outputDots: List<View>
    private var hasUserInteracted = false
    private var isInitialRandomized = false
    private var moveCount = 0
    private lateinit var countMoveTextView: TextView


    private val inputViews = mutableMapOf<String, FrameLayout>()
    private val gateViews = mutableMapOf<String, View>()
    private val nodeValues = mutableMapOf<String, Boolean>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_kuis_gerbang_logika)

        currentLevel = intent.getIntExtra("LEVEL_NUMBER", 1)
        connectorView = findViewById(R.id.connectorLines)
        manualLevelContainer = findViewById(R.id.manual_level_container)

        outputDots = listOf(
            findViewById(R.id.output_dot1),
            findViewById(R.id.output_dot2),
            findViewById(R.id.output_dot3)
        )

        countMoveTextView = findViewById(R.id.countMove)
        resetMoveCounter()

        setupButtons()
        inflateLevelLayout()
    }

    private fun setupButtons() {
        findViewById<ImageButton>(R.id.btnHome).setOnClickListener {
            SoundPlayer.playSound(this, CoreUiR.raw.water_bubble)
            resetMoveCounter()
            finish()
        }
    }

    private fun inflateLevelLayout() {
        manualLevelContainer.removeAllViews()
        val config = LevelManager.getLevelConfig(currentLevel) ?: return

        val levelView = LayoutInflater.from(this).inflate(config.layoutResId, manualLevelContainer, false)
        manualLevelContainer.addView(levelView)

        for (i in 1..config.inputs) {
            val inputTag = "input$i"
            val view = levelView.findViewWithTag<FrameLayout>(inputTag)
            if (view != null) {
                inputViews[inputTag.uppercase()] = view
                nodeValues[inputTag.uppercase()] = false

                view.setOnClickListener {
                    toggleInput(inputTag.uppercase())
                }
            }
        }

        val allTaggedViews = getAllTaggedViews(levelView)
        for (v in allTaggedViews) {
            val tag = v.tag?.toString()?.uppercase() ?: continue
            if (tag.startsWith("AND") || tag.startsWith("OR") || tag.startsWith("NOT") ||
                tag.startsWith("NAND") || tag.startsWith("NOR") || tag.startsWith("XOR")) {
                gateViews[tag] = v
                nodeValues[tag] = false
            }
        }

        connectorView.post {
            // Delay randomization to ensure layout is ready
            Handler(Looper.getMainLooper()).postDelayed({
                if (!isInitialRandomized) {
                    randomizeInputs()
                    isInitialRandomized = true
                }
                evaluateLogic()
                drawConnections()
            }, 100)
        }
    }

    private fun incrementMoveCounter() {
        moveCount++
        countMoveTextView.text = moveCount.toString()
    }

    private fun resetMoveCounter() {
        moveCount = 0
        countMoveTextView.text = moveCount.toString()
    }


    // Fungsi untuk menghitung jumlah bintang berdasarkan moveCount dan level
    private fun calculateStars(): Int {
        return when (currentLevel) {
            1 -> when {
                moveCount <= 2 -> 3
                moveCount <= 4 -> 2
                else -> 1
            }
            2 -> when {
                moveCount <= 3 -> 3
                moveCount <= 5 -> 2
                else -> 1
            }
            3 -> when {
                moveCount <= 3 -> 3
                moveCount <= 5 -> 2
                else -> 1
            }
            4 -> when {
                moveCount <= 3 -> 3
                moveCount <= 5 -> 2
                else -> 1
            }
            5 -> when {
                moveCount <= 4 -> 3
                moveCount <= 6 -> 2
                else -> 1
            }
            6 -> when {
                moveCount <= 4 -> 3
                moveCount <= 6 -> 2
                else -> 1
            }
            7 -> when {
                moveCount <= 5 -> 3
                moveCount <= 7 -> 2
                else -> 1
            }
            8 -> when {
                moveCount <= 5 -> 3
                moveCount <= 7 -> 2
                else -> 1
            }
            9 -> when {
                moveCount <= 5 -> 3
                moveCount <= 7 -> 2
                else -> 1
            }
            10 -> when {
                moveCount <= 6 -> 3
                moveCount <= 8 -> 2
                else -> 1
            }
            11 -> when {
                moveCount <= 6 -> 3
                moveCount <= 8 -> 2
                else -> 1
            }
            12 -> when {
                moveCount <= 6 -> 3
                moveCount <= 8 -> 2
                else -> 1
            }
            13 -> when {
                moveCount <= 7 -> 3
                moveCount <= 9 -> 2
                else -> 1
            }
            14 -> when {
                moveCount <= 8 -> 3
                moveCount <= 10 -> 2
                else -> 1
            }
            15 -> when {
                moveCount <= 3 -> 3
                moveCount <= 5 -> 2
                else -> 1
            }
            else -> 1 // Default untuk level yang tidak terdefinisi
        }
    }


    // Fungsi baru untuk mengacak input dengan kondisi khusus
    private fun randomizeInputs() {
        val config = LevelManager.getLevelConfig(currentLevel) ?: return
        val tempNodeValues = mutableMapOf<String, Boolean>()
        var isValidCombination = false
        var attempts = 0
        val maxAttempts = 100

        // Cari kombinasi yang tidak menyelesaikan level
        while (!isValidCombination && attempts < maxAttempts) {
            attempts++
            tempNodeValues.clear()

            // Acak nilai untuk setiap input
            inputViews.keys.forEach { inputId ->
                tempNodeValues[inputId] = Random.nextBoolean()
            }

            // Simpan nilai asli dan gunakan nilai acak sementara
            val originalValues = nodeValues.toMutableMap()
            nodeValues.putAll(tempNodeValues)

            // Evaluasi logika tanpa pengecekan kemenangan
            evaluateLogic(checkWin = false)

            // Dapatkan output akhir
            val finalOutput = config.gates.find { it.isFinal }?.let { nodeValues[it.id.uppercase()] } ?: false

            // Kembalikan nilai asli
            nodeValues.putAll(originalValues)

            // Validasi kombinasi: tidak boleh menyelesaikan level
            isValidCombination = (finalOutput != config.expectedOutput)
        }

        // Jika tidak ditemukan kombinasi valid, gunakan semua false
        if (!isValidCombination) {
            inputViews.keys.forEach { inputId ->
                tempNodeValues[inputId] = false
            }
        }

        // Terapkan nilai acak yang valid
        tempNodeValues.forEach { (inputId, value) ->
            nodeValues[inputId] = value
            updateInputDots(inputId, value)
        }
    }

    // Fungsi untuk memperbarui tampilan dot input
    private fun updateInputDots(inputId: String, value: Boolean) {
        val dotView = inputViews[inputId]?.findViewById<View>(R.id.dot_indicator)
        dotView?.setBackgroundResource(
            if (value) CoreUiR.drawable.green_dot_indicator_stroke
            else CoreUiR.drawable.red_dot_indicator_stroke
        )
    }

    private fun toggleInput(id: String) {
        hasUserInteracted = true
        val updated = !(nodeValues[id] ?: false)
        nodeValues[id] = updated

        SoundPlayer.playSound(this, CoreUiR.raw.light_switch)

        val dotView = inputViews[id]?.findViewById<View>(R.id.dot_indicator)
        dotView?.setBackgroundResource(
            if (updated) CoreUiR.drawable.green_dot_indicator_stroke
            else CoreUiR.drawable.red_dot_indicator_stroke
        )

        incrementMoveCounter()
        evaluateLogic()
        drawConnections()
    }

    // Tambahkan parameter checkWin dengan default true
    private fun evaluateLogic(checkWin: Boolean = true) {
        val config = LevelManager.getLevelConfig(currentLevel) ?: return

        // Reset nilai node untuk gerbang logika
        gateViews.keys.forEach { nodeValues[it] = false }

        // Evaluasi gerbang secara topologi
        val evaluated = mutableSetOf<String>()
        var changed: Boolean
        do {
            changed = false
            for (gate in config.gates) {
                val gateId = gate.id.uppercase()
                if (evaluated.contains(gateId)) continue

                val inputsReady = gate.inputIds.all {
                    evaluated.contains(it.uppercase()) || inputViews.containsKey(it.uppercase())
                }

                if (inputsReady) {
                    val inputs = gate.inputIds.map { nodeValues[it.uppercase()] ?: false }
                    val output = calculateGateOutput(gate.type, inputs)
                    if (nodeValues[gateId] != output) {
                        nodeValues[gateId] = output
                        changed = true
                    }
                    evaluated.add(gateId)
                }
            }
        } while (changed)

        // Update indikator output
        val finalOutput = config.gates.find { it.isFinal }?.let { nodeValues[it.id.uppercase()] } ?: false
        updateOutputDots(finalOutput)

        // Hanya cek kemenangan jika diizinkan dan user sudah berinteraksi
        if (checkWin && hasUserInteracted && finalOutput == config.expectedOutput) {
            showLevelComplete()
        }
    }


    private fun calculateGateOutput(type: String, inputs: List<Boolean>): Boolean {
        return when (type.uppercase()) {
            "AND" -> inputs.all { it }
            "OR" -> inputs.any { it }
            "NOT" -> !inputs[0]
            "NAND" -> !inputs.all { it }
            "NOR" -> !inputs.any { it }
            "XOR" -> inputs.reduce { acc, b -> acc xor b }
            else -> false
        }
    }


    private fun updateOutputDots(active: Boolean) {
        outputDots.forEach {
            it.setBackgroundResource(
                if (active) CoreUiR.drawable.green_dot_indicator_stroke
                else CoreUiR.drawable.red_dot_indicator_stroke
            )
        }
    }

    private fun resetInputs() {
        hasUserInteracted = false
        resetMoveCounter()
        inputViews.forEach { (key, view) ->
            nodeValues[key] = false
            updateInputDots(key, false) // PERBAIKAN: gunakan updateInputDot
            val dotView = view.findViewById<View>(R.id.dot_indicator)
            dotView?.setBackgroundResource(CoreUiR.drawable.red_dot_indicator_stroke)

        }

        connectorView.clearLines() // ✅ Tambahkan ini jika ingin clear juga langsung

        evaluateLogic()
        drawConnections()
    }


    private fun showLevelComplete() {
        outputDots.forEachIndexed { index, dot ->
            ObjectAnimator.ofFloat(dot, View.ALPHA, 0.2f, 1f).apply {
                duration = 500
                startDelay = index * 100L
                repeatCount = ObjectAnimator.INFINITE
                repeatMode = ObjectAnimator.REVERSE
                start()
            }
        }

        val stars = calculateStars()
        saveProgress(stars)
        Handler(Looper.getMainLooper()).postDelayed({
            showLevelCompleteDialog(stars)
        }, 1500)
    }

    private fun showLevelCompleteDialog(stars: Int) {
        SoundPlayer.playSound(this, CoreUiR.raw.achievement)
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_level_complete, null)
        val dialog = Dialog(this, CoreUiR.style.FullScreenDialog)
        dialog.setContentView(dialogView)
        dialog.setCancelable(false)

        dialogView.findViewById<TextView>(R.id.tvLevel).text = "LEVEL $currentLevel"

        // Update dots berdasarkan jumlah stars yang didapat
        val dot1 = dialogView.findViewById<View>(R.id.dot1)
        val dot2 = dialogView.findViewById<View>(R.id.dot2)
        val dot3 = dialogView.findViewById<View>(R.id.dot3)

        // Reset semua dots ke merah dulu
        dot1.setBackgroundResource(CoreUiR.drawable.red_dot_indicator_stroke)
        dot2.setBackgroundResource(CoreUiR.drawable.red_dot_indicator_stroke)
        dot3.setBackgroundResource(CoreUiR.drawable.red_dot_indicator_stroke)

        // Set dots sesuai dengan jumlah stars
        when (stars) {
            3 -> {
                dot1.setBackgroundResource(CoreUiR.drawable.green_dot_indicator_stroke)
                dot2.setBackgroundResource(CoreUiR.drawable.green_dot_indicator_stroke)
                dot3.setBackgroundResource(CoreUiR.drawable.green_dot_indicator_stroke)
            }
            2 -> {
                dot1.setBackgroundResource(CoreUiR.drawable.green_dot_indicator_stroke)
                dot2.setBackgroundResource(CoreUiR.drawable.green_dot_indicator_stroke)
            }
            1 -> {
                dot1.setBackgroundResource(CoreUiR.drawable.green_dot_indicator_stroke)
            }
        }

        dialogView.findViewById<Button>(R.id.btnUlangi).setOnClickListener {
            SoundPlayer.playSound(this, CoreUiR.raw.water_bubble)
            dialog.dismiss()
            resetMoveCounter()
            resetInputs()
            val resetLevel = currentLevel
            val intent = Intent(this, DetailKuisGerbangLogikaActivity::class.java)
            intent.putExtra("LEVEL_NUMBER", resetLevel)
            startActivity(intent)
            finish()
        }

        dialogView.findViewById<Button>(R.id.btnKembali).setOnClickListener {
            SoundPlayer.playSound(this, CoreUiR.raw.water_bubble)
            dialog.dismiss()
            resetMoveCounter()
            finish()
        }

        dialogView.findViewById<Button>(R.id.btnSelanjutnya).setOnClickListener {
            SoundPlayer.playSound(this, CoreUiR.raw.water_bubble)
            dialog.dismiss()
            resetMoveCounter()
            val nextLevel = currentLevel + 1
            val intent = Intent(this, DetailKuisGerbangLogikaActivity::class.java)
            intent.putExtra("LEVEL_NUMBER", nextLevel)
            startActivity(intent)
            finish()
        }

        if (!isFinishing && !isDestroyed) dialog.show()
    }

    private fun saveProgress(stars: Int) {
        val prefs = getSharedPreferences("game_prefs", MODE_PRIVATE)
        val editor = prefs.edit()
        // Simpan bintang yang didapat untuk level ini
        val currentStars = prefs.getInt("stars_level_$currentLevel", 0)
        if (stars > currentStars) {
            editor.putInt("stars_level_$currentLevel", stars)
        }

        // Unlock level selanjutnya jika belum di-unlock
        if (currentLevel >= prefs.getInt("unlocked_level", 1)) {
            editor.putInt("unlocked_level", currentLevel + 1)
        }
        editor.apply()

        // Log untuk debugging
        Log.d("Progress", "Level $currentLevel completed with $stars stars. Move count: $moveCount")
    }

    @SuppressLint("DiscouragedApi")
    private fun drawConnections() {
        val config = LevelManager.getLevelConfig(currentLevel) ?: return
        val lines = mutableListOf<ConnectorView.Line>()

        config.connections.forEach { conn ->
            val fromParent = manualLevelContainer.findViewWithTag<View>(conn.fromId)
                ?: if (conn.fromId.startsWith("input")) inputViews[conn.fromId] else null

            // Special handling for output dots
            val toParent = if (conn.toId == "layout_Output") {
                findViewById<LinearLayout>(R.id.layout_Output)
            } else {
                manualLevelContainer.findViewWithTag<View>(conn.toId)
                    ?: if (conn.toId.startsWith("input")) inputViews[conn.toId] else null
            }

            val fromDot = fromParent?.findViewById<View>(
                resources.getIdentifier(conn.fromPortId, "id", packageName)
            )
            val toDot = toParent?.findViewById<View>(
                resources.getIdentifier(conn.toPortId, "id", packageName)
            )

            if (fromDot != null && toDot != null) {
                val start = getDotCenter(fromDot)
                val end = getDotCenter(toDot)
                val midY = (start.y + end.y) / 2

                val fromId = conn.fromId.uppercase()
                val color = if (nodeValues[fromId] == true) Color.GREEN else Color.RED

                lines.add(
                    ConnectorView.Line(
                        start = start,
                        middle1 = PointF(start.x, midY),
                        middle2 = PointF(end.x, midY),
                        end = end,
                        color = color
                    )
                )
            } else {
                Log.w("Connector", "Missing dot: from=${conn.fromId} -> ${conn.fromPortId}, to=${conn.toId} -> ${conn.toPortId}")
            }
        }

        connectorView.setLines(lines)
    }

    private fun getDotCenter(view: View): PointF {
        val location = IntArray(2)
        view.getLocationOnScreen(location)
        val parentLoc = IntArray(2)
        connectorView.getLocationOnScreen(parentLoc)

        return PointF(
            location[0] - parentLoc[0] + view.width / 2f,
            location[1] - parentLoc[1] + view.height / 2f
        )
    }

    private fun getAllTaggedViews(view: View): List<View> {
        val views = mutableListOf<View>()
        if (view.tag != null) {
            views.add(view)
        }
        if (view is ViewGroup) {
            for (i in 0 until view.childCount) {
                views.addAll(getAllTaggedViews(view.getChildAt(i)))
            }
        }
        return views
    }
}

