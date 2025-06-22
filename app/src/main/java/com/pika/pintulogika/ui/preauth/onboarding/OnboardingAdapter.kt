package com.pika.pintulogika.ui.preauth.onboarding

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.pika.pintulogika.R
import com.pika.pintulogika.databinding.ItemOnboardingBinding

class OnboardingAdapter(
    private val items: List<OnboardingItem>,
    private val onSkipClicked: () -> Unit
) : RecyclerView.Adapter<OnboardingAdapter.OnboardingViewHolder>() {

    inner class OnboardingViewHolder(private val binding: ItemOnboardingBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: OnboardingItem) {
            binding.tvTitleOnboarding.text = item.title
            binding.tvSubtitleOnboarding.text = item.description
            binding.imageViewOnboarding.setImageResource(item.imageResId)

            // Tombol "Lewati"
            binding.tvSkipOnboarding.setOnClickListener {
                onSkipClicked()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OnboardingViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemOnboardingBinding.inflate(inflater, parent, false)
        return OnboardingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OnboardingViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}
