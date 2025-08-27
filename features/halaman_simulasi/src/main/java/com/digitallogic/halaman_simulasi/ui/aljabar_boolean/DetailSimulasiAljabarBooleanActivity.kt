package com.digitallogic.halaman_simulasi.ui.aljabar_boolean


import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.Switch
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.digitallogic.halaman_simulasi.R

class DetailSimulasiAljabarBooleanActivity : AppCompatActivity() {

    private lateinit var titleText: TextView
    private lateinit var descriptionText: TextView
    private lateinit var formulaText1: TextView
    private lateinit var formulaText2: TextView
    private lateinit var inputASwitch: Switch
    private lateinit var inputBSwitch: Switch
    private lateinit var inputCSwitch: Switch
    private lateinit var inputAValue: TextView
    private lateinit var inputBValue: TextView
    private lateinit var inputCValue: TextView
    private lateinit var inputALabel: TextView
    private lateinit var inputBLabel: TextView
    private lateinit var inputCLabel: TextView
    private lateinit var inputAContainer: LinearLayout
    private lateinit var inputBContainer: LinearLayout
    private lateinit var inputCContainer: LinearLayout
    private lateinit var leftResultText: TextView
    private lateinit var rightResultText: TextView
    private lateinit var truthTableLayout: TableLayout

    private var lawId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_detail_simulasi_aljabar_boolean)

        initViews()
        setupFromIntent()
        setupInputListeners()

        updateSimulation()
    }

    private fun initViews() {
        titleText = findViewById(R.id.lawTitle)
        descriptionText = findViewById(R.id.lawDescription)
        formulaText1 = findViewById(R.id.lawFormula1)
        formulaText2 = findViewById(R.id.lawFormula2)
        inputASwitch = findViewById(R.id.inputASwitch)
        inputBSwitch = findViewById(R.id.inputBSwitch)
        inputCSwitch = findViewById(R.id.inputCSwitch)
        inputAValue = findViewById(R.id.inputAValue)
        inputBValue = findViewById(R.id.inputBValue)
        inputCValue = findViewById(R.id.inputCValue)
        inputALabel = findViewById(R.id.inputALabel)
        inputBLabel = findViewById(R.id.inputBLabel)
        inputCLabel = findViewById(R.id.inputCLabel)
        inputAContainer = findViewById(R.id.inputAContainer)
        inputBContainer = findViewById(R.id.inputBContainer)
        inputCContainer = findViewById(R.id.inputCContainer)
        leftResultText = findViewById(R.id.leftResult)
        rightResultText = findViewById(R.id.rightResult)
        truthTableLayout = findViewById(R.id.tableTruthAljabar)
    }

    private fun setupFromIntent() {
        lawId = intent.getIntExtra("LAW_ID", 1)
        titleText.text = intent.getStringExtra("LAW_TITLE")
        descriptionText.text = intent.getStringExtra("LAW_DESCRIPTION")
        formulaText1.text = intent.getStringExtra("LAW_FORMULA1")

        val secondFormula = intent.getStringExtra("LAW_FORMULA2")
        if (!secondFormula.isNullOrEmpty()) {
            formulaText2.visibility = View.VISIBLE
            formulaText2.text = secondFormula
        }

        Log.e("DetailSimulasiAljabarBooleanActivity", "LAW_FORMULA1: $formulaText1")
        Log.e("DetailSimulasiAljabarBooleanActivity", "LAW_FORMULA2: $secondFormula")

        // Configure inputs based on law
        when (lawId) {
            1, 2, 3, 4, 5 -> {
                // Single input laws
                inputBContainer.visibility = View.GONE
                inputCContainer.visibility = View.GONE
            }

            6 -> {
                // Distributive law needs 3 inputs
                inputBContainer.visibility = View.VISIBLE
                inputCContainer.visibility = View.VISIBLE
            }

            else -> {
                // Two input laws
                inputBContainer.visibility = View.VISIBLE
                inputCContainer.visibility = View.GONE
            }
        }
    }

    private fun setupInputListeners() {
        inputASwitch.setOnCheckedChangeListener { _, isChecked ->
            updateInputDisplay()
            updateSimulation()
        }
        inputBSwitch.setOnCheckedChangeListener { _, isChecked ->
            updateInputDisplay()
            updateSimulation()
        }
        inputCSwitch.setOnCheckedChangeListener { _, isChecked ->
            updateInputDisplay()
            updateSimulation()
        }
    }

    private fun updateInputDisplay() {
        inputAValue.text = if (inputASwitch.isChecked) "1" else "0"
        inputBValue.text = if (inputBSwitch.isChecked) "1" else "0"
        inputCValue.text = if (inputCSwitch.isChecked) "1" else "0"

        inputALabel.text = if (inputASwitch.isChecked) "ON" else "OFF"
        inputBLabel.text = if (inputBSwitch.isChecked) "ON" else "OFF"
        inputCLabel.text = if (inputCSwitch.isChecked) "ON" else "OFF"
    }

    private fun updateSimulation() {
        updateInputDisplay()

        val a = inputASwitch.isChecked
        val b = inputBSwitch.isChecked
        val c = inputCSwitch.isChecked

        when (lawId) {
            1 -> simulateIdentityLaw(a)
            2 -> simulateComplementLaw(a)
            3 -> simulateDominanceLaw(a)
            4 -> simulateIdempotentLaw(a)
            5 -> simulateInvolutionLaw(a)
            6 -> simulateDistributiveLaw(a, b, c)
            7 -> simulateCommutativeLaw(a, b)
            8 -> simulateDeMorganLaw(a, b)
        }

        generateTruthTable()
    }

    private fun simulateIdentityLaw(a: Boolean) {
        // A + 0 = A, A · 1 = A
        val leftResult1 = a || false  // A + 0
        val rightResult1 = a          // A
        val leftResult2 = a && true   // A · 1
        val rightResult2 = a          // A

        leftResultText.text =
            "A + 0 = ${if (leftResult1) 1 else 0} | A · 1 = ${if (leftResult2) 1 else 0}"
        rightResultText.text =
            "A = ${if (rightResult1) 1 else 0} | A = ${if (rightResult2) 1 else 0}"
    }

    private fun simulateComplementLaw(a: Boolean) {
        // A + A' = 1, A · A' = 0
        val leftResult1 = a || !a     // A + A'
        val rightResult1 = true       // 1
        val leftResult2 = a && !a     // A · A'
        val rightResult2 = false      // 0

        leftResultText.text =
            "A + A' = ${if (leftResult1) 1 else 0} | A · A' = ${if (leftResult2) 1 else 0}"
        rightResultText.text =
            "1 = ${if (rightResult1) 1 else 0} | 0 = ${if (rightResult2) 1 else 0}"
    }

    private fun simulateDominanceLaw(a: Boolean) {
        // A + 1 = 1, A · 0 = 0
        val leftResult1 = a || true   // A + 1
        val rightResult1 = true       // 1
        val leftResult2 = a && false  // A · 0
        val rightResult2 = false      // 0

        leftResultText.text =
            "A + 1 = ${if (leftResult1) 1 else 0} | A · 0 = ${if (leftResult2) 1 else 0}"
        rightResultText.text =
            "1 = ${if (rightResult1) 1 else 0} | 0 = ${if (rightResult2) 1 else 0}"
    }

    private fun simulateIdempotentLaw(a: Boolean) {
        // A + A = A, A · A = A
        val leftResult1 = a || a      // A + A
        val rightResult1 = a          // A
        val leftResult2 = a && a      // A · A
        val rightResult2 = a          // A

        leftResultText.text =
            "A + A = ${if (leftResult1) 1 else 0} | A · A = ${if (leftResult2) 1 else 0}"
        rightResultText.text =
            "A = ${if (rightResult1) 1 else 0} | A = ${if (rightResult2) 1 else 0}"
    }

    private fun simulateInvolutionLaw(a: Boolean) {
        // (A')' = A
        val leftResult = !(!a)        // (A')'
        val rightResult = a           // A

        leftResultText.text = "(A')' = ${if (leftResult) 1 else 0}"
        rightResultText.text = "A = ${if (rightResult) 1 else 0}"
    }

    private fun simulateDistributiveLaw(a: Boolean, b: Boolean, c: Boolean) {
        // A · (B + C) = (A · B) + (A · C)
        val leftResult = a && (b || c)           // A · (B + C)
        val rightResult = (a && b) || (a && c)   // (A · B) + (A · C)

        leftResultText.text = "A · (B + C) = ${if (leftResult) 1 else 0}"
        rightResultText.text = "(A · B) + (A · C) = ${if (rightResult) 1 else 0}"
    }

    private fun simulateCommutativeLaw(a: Boolean, b: Boolean) {
        // A + B = B + A, A · B = B · A
        val leftResult1 = a || b      // A + B
        val rightResult1 = b || a     // B + A
        val leftResult2 = a && b      // A · B
        val rightResult2 = b && a     // B · A

        leftResultText.text =
            "A + B = ${if (leftResult1) 1 else 0} | A · B = ${if (leftResult2) 1 else 0}"
        rightResultText.text =
            "B + A = ${if (rightResult1) 1 else 0} | B · A = ${if (rightResult2) 1 else 0}"
    }

    private fun simulateDeMorganLaw(a: Boolean, b: Boolean) {
        // (A + B)' = A' · B', (A · B)' = A' + B'
        val leftResult1 = !(a || b)              // (A + B)'
        val rightResult1 = (!a) && (!b)          // A' · B'
        val leftResult2 = !(a && b)              // (A · B)'
        val rightResult2 = (!a) || (!b)          // A' + B'

        leftResultText.text =
            "(A + B)' = ${if (leftResult1) 1 else 0} | (A · B)' = ${if (leftResult2) 1 else 0}"
        rightResultText.text =
            "A' · B' = ${if (rightResult1) 1 else 0} | A' + B' = ${if (rightResult2) 1 else 0}"
    }

    private fun generateTruthTable() {
        truthTableLayout.removeAllViews()

        val a = if (inputASwitch.isChecked) 1 else 0
        val b = if (inputBContainer.visibility == View.VISIBLE) {
            if (inputBSwitch.isChecked) 1 else 0
        } else null
        val c = if (inputCContainer.visibility == View.VISIBLE) {
            if (inputCSwitch.isChecked) 1 else 0
        } else null

        val rows = when (lawId) {
            1 -> generateIdentityLaw()
            2 -> generateComplementLaw()
            3 -> generateDominanceLaw()
            4 -> generateIdempotentLaw()
            5 -> generateInvolutionLaw()
            6 -> generateDistributiveLaw()
            7 -> generateCommutativeLaw()
            8 -> generateDeMorganLaw()
            else -> emptyList()
        }

        val headers = getColumnHeaders()
        addTableRow(headers, isHeader = true)

        for (row in rows) {
            val values = mutableListOf<String>()
            if (row.inputA.isNotEmpty()) values.add(row.inputA)
            if (row.inputB.isNotEmpty()) values.add(row.inputB)
            if (row.inputC.isNotEmpty()) values.add(row.inputC)
            values.add(row.outputLeft)
            values.add(row.outputRight)

            val isActiveRow =
                row.inputA == a.toString() &&
                        (b == null || row.inputB == b.toString()) &&
                        (c == null || row.inputC == c.toString())

            addTableRow(values, isActiveRow = isActiveRow, isEqual = row.isEqual)
        }
    }

    private fun addTableRow(values: List<String>, isHeader: Boolean = false, isActiveRow: Boolean = false, isEqual: Boolean = false) {
        val row = TableRow(this)
        for (value in values) {
            val textView = TextView(this).apply {
                text = value
                setPadding(16, 16, 16, 16)
                textAlignment = View.TEXT_ALIGNMENT_CENTER
                setTypeface(null, if (isHeader) Typeface.BOLD else Typeface.NORMAL)
                setTextColor(Color.BLACK)
                setBackgroundColor(
                    when {

                        isHeader -> Color.LTGRAY // Warna header: Abu-abu
                        isActiveRow -> ContextCompat.getColor(context, com.pika.core_ui.R.color.orange_lighter) // Baris aktif
                        else -> ContextCompat.getColor(context, com.pika.core_ui.R.color.light_gray) // Latar default: Light Gray
                    }
                )
            }
            row.addView(textView)
        }
        truthTableLayout.addView(row)
    }

    private fun getColumnHeaders(): List<String> {
        return when (lawId) {
            1, 2, 3, 4, 5 -> listOf("A", "A . 1", "A")
            6 -> listOf("A", "B", "C", "A.(B+C)", "A.B + A.C")
            7 -> listOf("A", "B", "A.B", "B.A")
            8 -> listOf("A", "B", "!(A + B)", "!A.!B")
            else -> listOf("A", "B", "Output1", "Output2")
        }
    }

    data class TruthTableRow(
        val inputA: String = "",
        val inputB: String = "",
        val inputC: String = "",
        val outputLeft: String,
        val outputRight: String,
        val isEqual: Boolean
    )

    private fun and(a: Int, b: Int): Int = a * b
    private fun or(a: Int, b: Int): Int = if (a + b > 0) 1 else 0
    private fun not(a: Int): Int = if (a == 1) 0 else 1

    private fun generateIdentityLaw(): List<TruthTableRow> {
        return listOf(
            TruthTableRow("0", outputLeft = and(0, 1).toString(), outputRight = "0", isEqual = and(0, 1) == 0),
            TruthTableRow("1", outputLeft = and(1, 1).toString(), outputRight = "1", isEqual = and(1, 1) == 1),
        )
    }

    private fun generateComplementLaw(): List<TruthTableRow> {
        return listOf(
            TruthTableRow("0", outputLeft = and(0, not(0)).toString(), outputRight = "0", isEqual = and(0, not(0)) == 0),
            TruthTableRow("1", outputLeft = and(1, not(1)).toString(), outputRight = "0", isEqual = and(1, not(1)) == 0),
        )
    }

    private fun generateDominanceLaw(): List<TruthTableRow> {
        return listOf(
            TruthTableRow("0", outputLeft = or(0, 1).toString(), outputRight = "1", isEqual = or(0, 1) == 1),
            TruthTableRow("1", outputLeft = or(1, 1).toString(), outputRight = "1", isEqual = or(1, 1) == 1),
        )
    }

    private fun generateIdempotentLaw(): List<TruthTableRow> {
        return listOf(
            TruthTableRow("0", outputLeft = and(0, 0).toString(), outputRight = "0", isEqual = and(0, 0) == 0),
            TruthTableRow("1", outputLeft = and(1, 1).toString(), outputRight = "1", isEqual = and(1, 1) == 1),
        )
    }

    private fun generateInvolutionLaw(): List<TruthTableRow> {
        return listOf(
            TruthTableRow("0", outputLeft = not(not(0)).toString(), outputRight = "0", isEqual = not(not(0)) == 0),
            TruthTableRow("1", outputLeft = not(not(1)).toString(), outputRight = "1", isEqual = not(not(1)) == 1),
        )
    }

    private fun generateDistributiveLaw(): List<TruthTableRow> {
        val result = mutableListOf<TruthTableRow>()
        for (a in listOf(0, 1)) {
            for (b in listOf(0, 1)) {
                for (c in listOf(0, 1)) {
                    val left = and(a, or(b, c))
                    val right = or(and(a, b), and(a, c))
                    result.add(TruthTableRow(a.toString(), b.toString(), c.toString(), left.toString(), right.toString(), left == right))
                }
            }
        }
        return result
    }

    private fun generateCommutativeLaw(): List<TruthTableRow> {
        val result = mutableListOf<TruthTableRow>()
        for (a in listOf(0, 1)) {
            for (b in listOf(0, 1)) {
                val left = and(a, b)
                val right = and(b, a)
                result.add(TruthTableRow(a.toString(), b.toString(), outputLeft = left.toString(), outputRight = right.toString(), isEqual = left == right))
            }
        }
        return result
    }

    private fun generateDeMorganLaw(): List<TruthTableRow> {
        val result = mutableListOf<TruthTableRow>()
        for (a in listOf(0, 1)) {
            for (b in listOf(0, 1)) {
                val left = not(or(a, b))
                val right = and(not(a), not(b))
                result.add(TruthTableRow(a.toString(), b.toString(), outputLeft = left.toString(), outputRight = right.toString(), isEqual = left == right))
            }
        }
        return result
    }
}