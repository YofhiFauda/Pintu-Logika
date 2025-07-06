package com.digitallogic.halaman_simulasi.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.digitallogic.core_data.model.LogicSimulasi
import com.digitallogic.halaman_simulasi.databinding.ItemGerbangLogikaBinding

class LogicSimulasiAdapter(
    private val items: List<LogicSimulasi>,
    private val onClick: (LogicSimulasi) -> Unit
) : RecyclerView.Adapter<LogicSimulasiAdapter.LogicViewHolder>() {

    inner class LogicViewHolder(val binding: ItemGerbangLogikaBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LogicViewHolder {
        val binding = ItemGerbangLogikaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LogicViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LogicViewHolder, position: Int) {
        val item = items[position]
        with(holder.binding) {
            imgGerbangLogika.setImageResource(item.iconResId)
            namaGerbang.text = item.title
            deskripsiGerbang.text = item.description
            root.setOnClickListener { onClick(item) }
        }
    }

    override fun getItemCount(): Int = items.size
}