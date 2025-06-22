package com.pika.pintulogika.ui.preauth.onboarding

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.pika.pintulogika.R

class OnboardingAdapter(
    private val items: List<OnboardingItem>,
    private val onSkipClick: () -> Unit // ← Tambahkan callback
) : RecyclerView.Adapter<OnboardingAdapter.OnboardingViewHolder>() {

    inner class OnboardingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val img = itemView.findViewById<ImageView>(R.id.imageViewOnboarding)
        private val skip = itemView.findViewById<TextView>(R.id.tv_skipOnboarding)
        private val title = itemView.findViewById<TextView>(R.id.tv_titleOnboarding)
        private val desc = itemView.findViewById<TextView>(R.id.tv_subtitleOnboarding)

        fun bind(item: OnboardingItem) {
            img.setImageResource(item.imageResId)
            skip.text = item.skip
            title.text = item.title
            desc.text = item.description

            // Tangani klik skip
            skip.setOnClickListener {
                onSkipClick() // ← Kirim callback ke Activity
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OnboardingViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_onboarding, parent, false)
        return OnboardingViewHolder(view)
    }

    override fun onBindViewHolder(holder: OnboardingViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}
