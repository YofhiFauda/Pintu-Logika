package com.digitallogic.halaman_kuis.gerbang_logika

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.app.AlertDialog
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
import android.widget.ImageView
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

        findViewById<ImageView>(R.id.btnInfoDetailKuisGerbangLogika).setOnClickListener {
            SoundPlayer.playSound(this, CoreUiR.raw.water_bubble)
            showInfoDialog()
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

    private fun showInfoDialog(){
        val message = when (currentLevel) {
            1 -> """
                🎯 LEVEL 1 - Pengenalan AND Gate
                
                📝 Tujuan: Pahami cara kerja gerbang AND
                
                🔧 Komponen:
                • 4 Input (INPUT1, INPUT2, INPUT3, INPUT4)
                • 3 Gerbang AND (AND1, AND2, AND3)
                
                📖 Cara Bermain:
                1. Atur nilai input (0 atau 1) dengan menekan tombol input
                2. Gerbang AND menghasilkan output 1 hanya jika SEMUA input bernilai 1
                3. AND1 menggabungkan INPUT1 dan INPUT2
                4. AND2 menggabungkan INPUT3 dan INPUT4
                5. AND3 menggabungkan hasil AND1 dan AND2
                
                💡 Tips: Untuk mendapat output TRUE, semua 4 input harus bernilai 1!                
            """.trimIndent()
            2 -> """
                🎯 LEVEL 2 - Kombinasi OR dan AND
                
                📝 Tujuan: Pelajari kombinasi gerbang OR dan AND
                
                🔧 Komponen:
                • 4 Input (INPUT1, INPUT2, INPUT3, INPUT4)
                • 2 Gerbang OR (OR1, OR2)
                • 1 Gerbang AND (AND1)
                
                📖 Cara Bermain:
                1. OR1 menggabungkan INPUT1 dan INPUT2
                2. OR2 menggabungkan INPUT3 dan INPUT4
                3. AND1 menggabungkan hasil OR1 dan OR2
                4. Gerbang OR menghasilkan 1 jika SALAH SATU input bernilai 1
                
                💡 Tips: Pastikan minimal ada 1 input aktif di setiap pasangan OR!                
            """.trimIndent()
            3 -> """
                🎯 LEVEL 3 - AND ke OR
                
                📝 Tujuan: Pahami pola AND-OR
                
                🔧 Komponen:
                • 4 Input (INPUT1, INPUT2, INPUT3, INPUT4)
                • 2 Gerbang AND (AND1, AND2)
                • 1 Gerbang OR (OR1)
                
                📖 Cara Bermain:
                1. AND1 dan AND2 memproses pasangan input
                2. OR1 menggabungkan hasil kedua AND
                3. Output TRUE jika salah satu AND menghasilkan TRUE
                
                💡 Tips: Aktifkan kedua input di salah satu pasangan AND!                
            """.trimIndent()
            4 -> """
                🎯 LEVEL 4 - Pengenalan NOT Gate
                
                📝 Tujuan: Pelajari gerbang NOT (inverter)
                
                🔧 Komponen:
                • 4 Input + kombinasi AND, OR, NOT
                
                📖 Cara Bermain:
                1. NOT gate membalik nilai (0→1, 1→0)
                2. Rangkaian: AND1, OR1 → OR2 → NOT1
                3. Output akhir adalah kebalikan dari OR2
                
                💡 Tips: Pikirkan kapan OR2 menghasilkan FALSE untuk mendapat output TRUE!                
            """.trimIndent()
            5 ->"""
                🎯 LEVEL 5 - Rangkaian Kompleks
                
                📝 Tujuan: Kuasai kombinasi berbagai gerbang
                
                🔧 Komponen:
                • 6 Gerbang: OR1, NOT1, AND1, NOT2, NOT3, AND2
                
                📖 Cara Bermain:
                1. Jalur 1: INPUT1,2 → OR1 → NOT2
                2. Jalur 2: INPUT3 → NOT1 → AND1 ← INPUT4 → NOT3
                3. AND2 menggabungkan hasil NOT2 dan NOT3
                
                💡 Tips: Perhatikan jalur NOT yang berlipat ganda!                
            """.trimIndent()
            6 ->"""
                🎯 LEVEL 6 - NAND Gate
                
                📝 Tujuan: Pelajari gerbang NAND
                
                🔧 Komponen:
                • NAND, OR, NOT gates
                
                📖 Cara Bermain:
                1. NAND = NOT + AND (kebalikan dari AND)
                2. NAND menghasilkan 0 hanya jika semua input 1
                3. Kombinasi dengan OR dan NOT gate
                
                💡 Tips: NAND adalah gerbang universal - sangat powerful!              
            """.trimIndent()
            7 ->"""
                🎯 LEVEL 7 - Rangkaian 6 Input
                
                📝 Tujuan: Kelola rangkaian dengan banyak input
                
                🔧 Komponen:
                • 6 Input dengan kombinasi AND, NOR, NOT
                
                📖 Cara Bermain:
                1. Tiga jalur paralel diproses berbeda
                2. NOR gate = NOT + OR
                3. Hasil digabungkan dengan AND gates bertingkat
                
                💡 Tips: Analisis setiap jalur secara terpisah dulu!              
            """.trimIndent()
            8 ->"""
                🎯 LEVEL 8 - Rangkaian Bertingkat
                
                📝 Tujuan: Kuasai alur data bertingkat
                
                🔧 Komponen:
                • NOR, AND, NOT, OR gates dalam susunan kompleks
                
                📖 Cara Bermain:
                1. Multiple stage processing
                2. Perhatikan alur NOT gates yang berlapis
                3. Hasil akhir melalui OR3
                
                💡 Tips: Ikuti alur data step by step dari input ke output!              
            """.trimIndent()
            9 ->"""
                🎯 LEVEL 9 - NOR Gate Finale
                
                📝 Tujuan: Selesaikan dengan NOR gate
                
                🔧 Komponen:
                • Kombinasi semua jenis gate berakhir di NOR
                
                📖 Cara Bermain:
                1. NOR menghasilkan 1 hanya jika semua input 0
                2. Proses dua jalur AND yang kompleks
                3. NOR1 sebagai gerbang final
                
                💡 Tips: Untuk output TRUE, buat kedua input NOR1 menjadi FALSE!              
            """.trimIndent()
            10 ->"""
                🎯 LEVEL 10 - Paralelisme
                
                📝 Tujuan: Kelola multiple parallel paths
                
                🔧 Komponen:
                • 3 jalur input parallel dengan sharing connections
                
                📖 Cara Bermain:
                1. AND2 digunakan di dua jalur berbeda
                2. AND4 menggabungkan hasil AND3 dan OR2
                3. Optimalisasi resource dengan sharing
                
                💡 Tips: Perhatikan gate yang digunakan multiple paths!              
            """.trimIndent()
            11 ->"""
                🎯 LEVEL 11 - Mixed Operations
                
                📝 Tujuan: Campuran operasi kompleks
                
                🔧 Komponen:
                • NOR, OR, AND, NOT dalam kombinasi advanced
                
                📖 Cara Bermain:
                1. NOR dan OR bekerja parallel
                2. NOT gate di tengah processing
                3. Multiple AND gates untuk convergence
                
                💡 Tips: Balance antara true dan false paths!              
            """.trimIndent()
            12 ->"""
                🎯 LEVEL 12 - Advanced Architecture
                
                📝 Tujuan: Arsitektur rangkaian advanced
                
                🔧 Komponen:
                • NOR, NOT, multiple AND dan OR gates
                
                📖 Cara Bermain:
                1. Three-tier architecture
                2. Shared connections antara gates
                3. NOR1 dan NOT1 sebagai controller
                
                💡 Tips: Lihat pola three-tier: Input → Process → Combine!              
            """.trimIndent()
            13 ->"""
                🎯 LEVEL 13 - Expert Level
                
                📝 Tujuan: Tantangan untuk expert
                
                🔧 Komponen:
                • Multi-path dengan NOT gates strategis
                
                📖 Cara Bermain:
                1. NOT gates di posisi strategis
                2. Multiple OR dan AND stages  
                3. Final combination dengan AND3
                
                💡 Tips: Expert level - analisis setiap path dengan teliti!              
            """.trimIndent()
            14 ->"""
                🎯 LEVEL 14 - Master Challenge
                
                📝 Tujuan: Challenge untuk master level
                
                🔧 Komponen:
                • 5-tier architecture dengan 11 gates total
                
                📖 Cara Bermain:
                1. Arsitektur berlapis sangat kompleks
                2. Multiple convergence points
                3. NOT3 sebagai final inverter
                
                💡 Tips: Master level - gunakan strategi divide and conquer!              
            """.trimIndent()
            15 ->"""
                🎯 LEVEL 15 - XOR Challenge
                
                📝 Tujuan: Final boss dengan XOR gate!
                
                🔧 Komponen:
                • XOR gate sebagai final gate
                
                📖 Cara Bermain:
                1. XOR menghasilkan 1 jika input berbeda
                2. OR2 dan OR3 harus menghasilkan nilai berbeda
                3. Final boss level - gunakan semua pengetahuan!
                
                💡 Tips: XOR = eXclusive OR. TRUE jika input BERBEDA!
                
                🎉 Selamat! Ini adalah level terakhir!              
            """.trimIndent()

            else -> "Tidak ada informasi yang tersedia."
        }

        AlertDialog.Builder(this)
            .setTitle("Panduan Gamifikasi Gerbang Logika")
            .setMessage(message)
            .setPositiveButton("Tutup") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
}

