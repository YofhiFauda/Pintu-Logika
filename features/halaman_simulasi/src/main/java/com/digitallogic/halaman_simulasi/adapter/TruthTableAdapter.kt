package com.digitallogic.halaman_simulasi.adapter

import android.graphics.Color
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.digitallogic.halaman_simulasi.R
import com.digitallogic.halaman_simulasi.data.TruthTableRow

// Adapter yang diperbaiki untuk menampilkan semua kolom
class TruthTableAdapter(
    private val rows: List<TruthTableRow>,
    private val columns: List<String>,
    private val activeA: Int,
    private val activeB: Int?,
    private val activeC: Int?
) : RecyclerView.Adapter<TruthTableAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val container: LinearLayout = view.findViewById(R.id.rowContainer)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_truth_table_row, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val row = rows[position]
        holder.container.removeAllViews()

        val isActiveRow = row.inputA == activeA.toString() &&
                (activeB == null || row.inputB == activeB.toString()) &&
                (activeC == null || row.inputC == activeC.toString())

        // Tampilkan kolom berdasarkan header
        columns.forEach { header ->
            val value = when (header) {
                "A" -> row.inputA
                "B" -> row.inputB
                "C" -> row.inputC
                "A'" -> row.inputAComplement
                "B'" -> row.inputBComplement
                "C'" -> row.inputCComplement
                else -> {
                    // Untuk kolom output, ambil dari outputLeft atau outputRight
                    if (columns.indexOf(header) == columns.size - 2) row.outputLeft
                    else if (columns.indexOf(header) == columns.size - 1) row.outputRight
                    else ""
                }
            }

            if (value.isNotEmpty()) {
                addCell(holder.container, value, false, isActiveRow)
            }
        }
    }

    private fun addCell(
        container: LinearLayout,
        value: String,
        highlight: Boolean,
        activeRow: Boolean
    ) {
        val context = container.context
        val textView = TextView(context).apply {
            text = value
            layoutParams =
                LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
            textAlignment = View.TEXT_ALIGNMENT_CENTER
            setPadding(16, 16, 16, 16)
            setTextColor(Color.BLACK)
            setTypeface(typeface, Typeface.BOLD)

            when {
                activeRow -> setBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        android.R.color.holo_blue_light
                    )
                )

                highlight && value == "1" -> setBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        android.R.color.holo_green_light
                    )
                )

                highlight && value == "0" -> setBackgroundColor(Color.LTGRAY)
            }
        }
        container.addView(textView)
    }

    override fun getItemCount() = rows.size
}
