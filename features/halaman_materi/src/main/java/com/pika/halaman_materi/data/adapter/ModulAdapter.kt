package com.pika.halaman_materi.data.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.digitallogic.core_data.model.materi.Modul
import com.pika.halaman_materi.R
import android.content.Intent
import com.pika.halaman_materi.data.session.ProgressPreferences
import com.pika.halaman_materi.ui.MateriPembelajaranActivity
import com.pika.halaman_materi.utils.OnProgressUpdateListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class ModulAdapter(
    private val modulList: List<Modul>,
    private val materiId: String,
    private val progressListener: OnProgressUpdateListener
) : RecyclerView.Adapter<ModulAdapter.ModulViewHolder>() {

    inner class ModulViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvModul: TextView = view.findViewById(R.id.tvModul)
        val ivExpand: ImageView = view.findViewById(R.id.ivExpand)
        val ivChecklist: ImageView = view.findViewById(R.id.ivChecklist)
        val layoutSubModul: LinearLayout = view.findViewById(R.id.layoutSubModul)
        val btnExpand: LinearLayout = view.findViewById(R.id.btn_expand)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ModulViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_modul, parent, false)
        return ModulViewHolder(view)
    }

    override fun onBindViewHolder(holder: ModulViewHolder, position: Int) {
        val modul = modulList[position]
        val statusManager = ProgressPreferences(holder.itemView.context)

        var completedCount = 0
        val totalCount = modul.subModul.size


        holder.tvModul.text = modul.nama
        holder.ivChecklist.visibility = if (modul.isSelesai) View.VISIBLE else View.INVISIBLE
        holder.ivExpand.setImageResource(
            if (modul.isExpanded) com.pika.core_ui.R.drawable.ic_expand_less_24 else com.pika.core_ui.R.drawable.ic_expand_more_24
        )

        holder.btnExpand.setOnClickListener {
            modul.isExpanded = !modul.isExpanded
            notifyItemChanged(position)
        }

        holder.layoutSubModul.removeAllViews()
        if (modul.isExpanded) {
            holder.layoutSubModul.visibility = View.VISIBLE
            for (sub in modul.subModul) {
                val subView = LayoutInflater.from(holder.itemView.context)
                    .inflate(R.layout.item_submodul, holder.layoutSubModul, false)
                val tvSub = subView.findViewById<TextView>(R.id.tvSubModul)
                val ivStatus = subView.findViewById<ImageView>(R.id.ivStatus)

                tvSub.text = sub.nama

                CoroutineScope(Dispatchers.Main).launch {
                    statusManager.isRead(sub.nama).collect { isRead ->
                        val color = if (isRead) com.pika.core_ui.R.color.green else com.pika.core_ui.R.color.gray
                        ivStatus.setColorFilter(ContextCompat.getColor(holder.itemView.context, color))

                        // Hitung selesai
                        if (isRead) completedCount++

                        // Callback progres update ke Fragment
                        if (completedCount + 1 == totalCount) {
                            val allSubmodul = modulList.flatMap { it.subModul }
                            val readCount = allSubmodul.count { it.isSelesai } // optional backup
                            progressListener.onProgressUpdate(allSubmodul.size, readCount)
                        }
                    }
                }

                subView.setOnClickListener {
                    val context = holder.itemView.context
                    val intent = Intent(context, MateriPembelajaranActivity::class.java).apply {
                        putExtra("materi_id", materiId)
                        putExtra("modul_id", modul.nama.replace(" ", "_").lowercase())
                        putExtra("judul_submodul", sub.nama)
                    }
                    context.startActivity(intent)
                }

                holder.layoutSubModul.addView(subView)
            }
        } else {
            holder.layoutSubModul.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int = modulList.size
}




