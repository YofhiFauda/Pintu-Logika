package com.digitallogic.halaman_simulasi.ui

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.Switch
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import com.digitallogic.halaman_simulasi.R
import com.digitallogic.halaman_simulasi.databinding.ActivitySimulasiDetailBinding
import androidx.core.view.updatePadding

class SimulasiDetailActivity : AppCompatActivity() {


    private lateinit var binding: ActivitySimulasiDetailBinding
    private lateinit var gateType: String
    private val tableRows = mutableListOf<TableRow>() // simpan baris untuk referensi animasi

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySimulasiDetailBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        val window = this.window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)


        gateType = intent.getStringExtra("GATE_TYPE") ?: "LOGIKA AND"
        binding.tvJudul.text = "SIMULASI $gateType"
        binding.imgGerbang.setImageResource(getGateDrawable(gateType))


        binding.btnBackSimulasi.setOnClickListener {
            finish()
        }

        // Atur visibilitas switch untuk LOGIKA NOT
        if (gateType == "LOGIKA NOT") {
            binding.switch2.visibility = View.GONE
        }


        val red = ContextCompat.getColor(this, com.pika.core_ui.R.color.red_500)
        val green = ContextCompat.getColor(this, com.pika.core_ui.R.color.green_500)
        val gray = ContextCompat.getColor(this, com.pika.core_ui.R.color.gray_500)

        // Warna awal switch
        updateSwitchTrack(binding.switch1, binding.switch1.isChecked, red, gray)
        updateSwitchTrack(binding.switch2, binding.switch2.isChecked, green, gray)

        binding.switch1.setOnCheckedChangeListener { _, isChecked ->
            updateSwitchTrack(binding.switch1, isChecked, red, gray)
            updateLamp()
        }

        binding.switch2.setOnCheckedChangeListener { _, isChecked ->
            updateSwitchTrack(binding.switch2, isChecked, green, gray)
            updateLamp()
        }



        updateLamp()
        setupTruthTable(gateType)
    }

    private fun updateSwitchTrack(
        switch: Switch,
        isChecked: Boolean,
        activeColor: Int,
        inactiveColor: Int
    ) {
        switch.trackTintList = ColorStateList.valueOf(
            if (isChecked) activeColor else inactiveColor
        )
    }

    private fun getGateDrawable(gate: String): Int {
        return when (gate.uppercase()) {
            "LOGIKA AND" -> com.pika.core_ui.R.drawable.ic_gate_and
            "LOGIKA OR" -> com.pika.core_ui.R.drawable.ic_gate_or
            "LOGIKA NOT" -> com.pika.core_ui.R.drawable.ic_gate_not
            "LOGIKA NAND" -> com.pika.core_ui.R.drawable.ic_gate_nand
            "LOGIKA NOR" -> com.pika.core_ui.R.drawable.ic_gate_nor
            "LOGIKA XOR" -> com.pika.core_ui.R.drawable.ic_gate_xor
            "LOGIKA XNOR" -> com.pika.core_ui.R.drawable.ic_gate_xnor
            else -> com.pika.core_ui.R.drawable.ic_gate_and // default
        }
    }


    private fun updateLamp() {
        val a = binding.switch1.isChecked
        val b = binding.switch2.isChecked

        val result = calculateLogicOutput(gateType, a, b)

        if (result) {
            binding.imgLamp.setImageResource(com.pika.core_ui.R.drawable.ic_lamp_on)

            // Tambahkan animasi
            val anim = AnimationUtils.loadAnimation(this, com.pika.core_ui.R.anim.lamp_pop)
            binding.imgLamp.startAnimation(anim)
        } else {
            binding.imgLamp.setImageResource(com.pika.core_ui.R.drawable.ic_lamp_off)
            binding.imgLamp.clearAnimation()
        }
        highlightMatchingRow(a, b)
        displayLogicExplanation(a, b)
    }


    private fun calculateLogicOutput(gate: String, a: Boolean, b: Boolean = false): Boolean {
        return when (gate.uppercase()) {
            "LOGIKA AND" -> a && b
            "LOGIKA OR" -> a || b
            "LOGIKA NOT" -> !a
            "LOGIKA NAND" -> !(a && b)
            "LOGIKA NOR" -> !(a || b)
            "LOGIKA XOR" -> a xor b
            "LOGIKA XNOR" -> !(a xor b)
            else -> false
        }
    }


    private fun logicFormat(name: String, a: Boolean, b: Boolean, result: Boolean): String {
        val aVal = if (a) "1" else "0"
        val bVal = if (b) "1" else "0"
        val res = if (result) "1" else "0"
        return "$aVal $name $bVal → Output: $res"
    }


    private fun displayLogicExplanation(a: Boolean, b: Boolean) {
        val result = calculateLogicOutput(gateType, a, b)

        val logicText = when (gateType.uppercase()) {
            "LOGIKA NOT" -> {
                val aVal = if (a) "1" else "0"
                val res = if (result) "1" else "0"
                "NOT $aVal → Output: $res"
            }

            "LOGIKA AND" -> logicFormat("AND", a, b, result)
            "LOGIKA OR" -> logicFormat("OR", a, b, result)
            "LOGIKA NAND" -> logicFormat("NAND", a, b, result)
            "LOGIKA NOR" -> logicFormat("NOR", a, b, result)
            "LOGIKA XOR" -> logicFormat("XOR", a, b, result)
            "LOGIKA XNOR" -> logicFormat("XNOR", a, b, result)
            else -> ""
        }

        binding.tvLogicExplanation.text = logicText
    }


    private fun setupTruthTable(gate: String) {
        val tableLayout = binding.tableTruth
        tableLayout.removeAllViews()
        tableRows.clear()

        // Header
        val headerRow = TableRow(this)
        headerRow.addView(makeCell("A", true))
        if (gate != "LOGIKA NOT") {
            headerRow.addView(makeCell("B", true))
        }
        headerRow.addView(makeCell("Output", true))
        tableLayout.addView(headerRow)

        val rows = when (gate.uppercase()) {
            "LOGIKA AND" -> listOf(
                Triple(false, false, false),
                Triple(false, true, false),
                Triple(true, false, false),
                Triple(true, true, true)
            )
            "LOGIKA OR" -> listOf(
                Triple(false, false, false),
                Triple(false, true, true),
                Triple(true, false, true),
                Triple(true, true, true)
            )
            "LOGIKA NOT" -> listOf(
                Triple(false, false, true),
                Triple(true, false, false)
            )
            "LOGIKA NAND" -> listOf(
                Triple(false, false, true),
                Triple(false, true, true),
                Triple(true, false, true),
                Triple(true, true, false)
            )
            "LOGIKA NOR" -> listOf(
                Triple(false, false, true),
                Triple(false, true, false),
                Triple(true, false, false),
                Triple(true, true, false)
            )
            "LOGIKA XOR" -> listOf(
                Triple(false, false, false),
                Triple(false, true, true),
                Triple(true, false, true),
                Triple(true, true, false)
            )
            "LOGIKA XNOR" -> listOf(
                Triple(false, false, true),
                Triple(false, true, false),
                Triple(true, false, false),
                Triple(true, true, true)
            )
            else -> emptyList()
        }

        for (row in rows) {
            val tableRow = TableRow(this)
            tableRow.tag = Triple(row.first, row.second, row.third)
            tableRow.addView(makeCell(if (row.first) "1" else "0"))
            if (gate != "LOGIKA NOT") {
                tableRow.addView(makeCell(if (row.second) "1" else "0"))
            }
            tableRow.addView(makeCell(if (row.third) "1" else "0"))
            tableLayout.addView(tableRow)
            tableRows.add(tableRow)
        }
    }

    private fun highlightMatchingRow(a: Boolean, b: Boolean) {
        val target = Triple(a, b, calculateLogicOutput(gateType, a, b))

        for (row in tableRows) {
            val tag = row.tag as? Triple<*, *, *> ?: continue
            val rowTriple = Triple(tag.first as Boolean, tag.second as Boolean, tag.third as Boolean)

            if (rowTriple == target) {
                val anim = AnimationUtils.loadAnimation(this, com.pika.core_ui.R.anim.row_highlight)
                row.setBackgroundColor(ContextCompat.getColor(this, com.pika.core_ui.R.color.yellow_200))
                row.startAnimation(anim)

                // Tooltip jika output = true
                if (rowTriple.third == true) {
                    Toast.makeText(this, "Logika benar: Output = 1", Toast.LENGTH_SHORT).show()
                    }
            } else {
                row.setBackgroundColor(Color.TRANSPARENT)
            }
        }
    }


    private fun makeCell(text: String, isHeader: Boolean = false): TextView {
        return TextView(this).apply {
            this.text = text
            this.setPadding(16, 8, 16, 8)
            this.setTextColor(Color.BLACK)
            this.textSize = if (isHeader) 16f else 14f
            this.setTypeface(null, if (isHeader) Typeface.BOLD else Typeface.NORMAL)
            this.setBackgroundColor(if (isHeader) Color.LTGRAY else Color.TRANSPARENT)
        }
    }
}