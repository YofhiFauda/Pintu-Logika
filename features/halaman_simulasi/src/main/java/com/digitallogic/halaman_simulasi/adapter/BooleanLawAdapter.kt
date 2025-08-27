package com.digitallogic.halaman_simulasi.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.digitallogic.halaman_simulasi.data.BooleanLaw
import com.digitallogic.halaman_simulasi.R

class BooleanLawAdapter(
    private val laws: List<BooleanLaw>,
    private val onItemClick: (BooleanLaw) -> Unit
) : RecyclerView.Adapter<BooleanLawAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titleText: TextView = view.findViewById(R.id.lawTitle)
        val descriptionText: TextView = view.findViewById(R.id.lawDescription)
        val formulaText: TextView = view.findViewById(R.id.lawFormula)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_boolean_law, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val law = laws[position]
        holder.titleText.text = law.title
        holder.descriptionText.text = law.description
        holder.formulaText.text = law.formula1
        val formulaDisplay = if (!law.formula2.isNullOrEmpty()) {
            "${law.formula1} , ${law.formula2}"
        } else {
            law.formula1
        }
        holder.formulaText.text = formulaDisplay


        holder.itemView.setOnClickListener {
            onItemClick(law)
        }
    }

    override fun getItemCount() = laws.size
}