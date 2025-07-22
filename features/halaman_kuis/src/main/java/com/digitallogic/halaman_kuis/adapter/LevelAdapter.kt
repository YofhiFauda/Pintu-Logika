package com.digitallogic.halaman_kuis.adapter

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.digitallogic.halaman_kuis.R
import android.view.LayoutInflater
import android.view.View
import androidx.core.content.ContextCompat
import android.widget.TextView
import com.pika.core_ui.R as CoreUiR
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.digitallogic.halaman_kuis.LevelLogicGates
import com.digitallogic.halaman_kuis.SoundPlayer

class LevelAdapter(
    private val context: Context,
    private val onLevelClick: (LevelLogicGates) -> Unit
) : ListAdapter<LevelLogicGates, LevelAdapter.LevelViewHolder>(LevelDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LevelViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_level_gerbang_logika, parent, false)
        return LevelViewHolder(view)
    }

    override fun onBindViewHolder(holder: LevelViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class LevelViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val levelNumber: TextView = itemView.findViewById(R.id.levelNumber)
        private val star1: View = itemView.findViewById(R.id.dot_indikator1)
        private val star2: View = itemView.findViewById(R.id.dot_indikator2)
        private val star3: View = itemView.findViewById(R.id.dot_indikator3)
        private val levelContainer: View = itemView.findViewById(R.id.levelContainer)

        fun bind(level: LevelLogicGates) {
            levelNumber.text = level.number.toString()


            // Set stars
            setDotColor(star1, level.stars >= 1)
            setDotColor(star2, level.stars >= 2)
            setDotColor(star3, level.stars >= 3)

            levelContainer.setOnClickListener {
                SoundPlayer.playSound(context, com.pika.core_ui.R.raw.water_bubble)
                onLevelClick(level)
            }
        }

        private fun setDotColor(dot: View, isActive: Boolean) {
            dot.setBackgroundResource(
                if (isActive) CoreUiR.drawable.green_dot_indicator_stroke
                else CoreUiR.drawable.red_dot_indicator_stroke // Buat resource baru untuk dot non-aktif
            )
        }
    }
}

class LevelDiffCallback : DiffUtil.ItemCallback<LevelLogicGates>() {
    override fun areItemsTheSame(oldItem: LevelLogicGates, newItem: LevelLogicGates): Boolean {
        return oldItem.number == newItem.number
    }

    override fun areContentsTheSame(oldItem: LevelLogicGates, newItem: LevelLogicGates): Boolean {
        return oldItem == newItem
    }
}




