package com.pika.halaman_materi.data.modul_list

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pika.halaman_materi.R
import com.pika.halaman_materi.data.adapter.ModulAdapter
import com.digitallogic.core_data.model.materi.Modul
import android.util.Log
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import com.digitallogic.core_data.model.materi.ContentItem
import com.digitallogic.core_data.model.materi.ContentType
import com.digitallogic.core_data.model.materi.SubModulDynamic
import com.google.firebase.firestore.FirebaseFirestore
import com.pika.halaman_materi.data.session.ProgressPreferences
import com.pika.halaman_materi.utils.OnProgressUpdateListener
import kotlinx.coroutines.launch

class ModulListFragment : Fragment(R.layout.fragment_modul_list) {

    private lateinit var rvModulList: RecyclerView
    private val firestore = FirebaseFirestore.getInstance()
    private lateinit var progressPreferences: ProgressPreferences
    private val listModul = mutableListOf<Modul>()
    private lateinit var adapter: ModulAdapter
    private var materiId: String? = null

    private lateinit var progressBar: ProgressBar
    private lateinit var tvProgressPercent: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        materiId = arguments?.getString("materi_id")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvModulList = view.findViewById(R.id.rvModulList)
        progressBar = view.findViewById(R.id.progressBar)
        tvProgressPercent = view.findViewById(R.id.tvProgressPercent)

        rvModulList.layoutManager = LinearLayoutManager(requireContext())
        progressPreferences = ProgressPreferences(requireContext())


        if (materiId != null) {
            adapter = ModulAdapter(listModul, materiId!!, progressListener)
            rvModulList.adapter = adapter
            fetchModulAndSubmodul(materiId!!)
        } else {
            Log.e("ModulListFragment", "materiId belum diset")
        }
    }

    private val progressListener = object : OnProgressUpdateListener {
        override fun onProgressUpdate(total: Int, completed: Int) {
            val percentage = if (total == 0) 0 else (completed * 100 / total)
            progressBar.progress = percentage
            tvProgressPercent.text = "$percentage% Selesai"
        }
    }


    private fun fetchModulAndSubmodul(materiId: String) {
        firestore.collection("modul")
            .document(materiId)
            .collection("list_modul")
            .orderBy("timestamp")
            .get()
            .addOnSuccessListener { modulSnapshot ->
                listModul.clear()
                val modulDocs = modulSnapshot.documents

                if (modulDocs.isEmpty()) {
                    adapter.notifyDataSetChanged()
                    return@addOnSuccessListener
                }

                var completed = 0
                val tempList = mutableListOf<Modul>()

                for (modulDoc in modulDocs) {
                    val namaModul = modulDoc.getString("nama_modul") ?: continue
                    val modulId = namaModul.replace(" ", "_").lowercase() // sesuai struktur koleksi

                    firestore.collection("materi_$materiId")
                        .document(modulId)
                        .collection("sub_modul")
                        .get()
                        .addOnSuccessListener { subSnapshot ->
                            val subModulList = subSnapshot.documents.mapNotNull { subDoc ->
                                val nama = subDoc.getString("nama_sub_modul") ?: return@mapNotNull null
                                val judul = subDoc.getString("judul_materi") ?: ""
                                val tanggal = subDoc.getString("tanggal_upload") ?: ""
                                val isSelesai = subDoc.getBoolean("is_selesai") ?: false

                                val contentList = mutableListOf<ContentItem>()

                                val rawContent = subDoc.get("dynamic_content") as? List<*>
                                rawContent?.forEach { item ->
                                    val map = item as? Map<*, *> ?: return@forEach
                                    val typeString = map["type"] as? String ?: "TEXT"

                                    val type = try {
                                        ContentType.valueOf(typeString.uppercase())
                                    } catch (e: IllegalArgumentException) {
                                        ContentType.TEXT
                                    }

                                    val contentItem = ContentItem(
                                        type = type,
                                        content = map["content"] as? String ?: "",
                                        style = map["style"] as? String ?: "",
                                        alignment = map["alignment"] as? String ?: "left",
                                        imageUrl = map["imageUrl"] as? String ?: "",
                                        caption = map["caption"] as? String ?: "",
                                        linkText = map["linkText"] as? String ?: "",
                                        linkUrl = map["linkUrl"] as? String ?: ""
                                    )
                                    contentList.add(contentItem)
                                }

                                SubModulDynamic(
                                    nama = nama,
                                    judul = judul,
                                    tanggalUpload = tanggal,
                                    dynamicContent = contentList,
                                    isSelesai = isSelesai
                                )
                            }

                            Log.d("ModulListFragment", "Modul: $namaModul | Submodul: ${subModulList.size}")
                            tempList.add(Modul(namaModul, false, subModulList))

                            completed++
                            if (completed == modulDocs.size) {
                                lifecycleScope.launch {
                                    val readSubmoduls = getReadSubmodules()
                                    val updatedList = tempList.map { modul ->
                                        val updatedSubs = modul.subModul.map { sub ->
                                            sub.copy(isSelesai = readSubmoduls.contains(sub.nama))
                                        }
                                        modul.copy(subModul = updatedSubs)
                                    }

                                    listModul.addAll(updatedList)
                                    adapter.notifyDataSetChanged()

                                    // Hitung progress
                                    val total = updatedList.sumOf { it.subModul.size }
                                    val completedCount = updatedList.sumOf { it.subModul.count { it.isSelesai } }
                                    progressListener.onProgressUpdate(total, completedCount)
                                }

                                Log.d("ModulListFragment", "Jumlah modul: ${listModul.size}")
                            }
                        }
                        .addOnFailureListener {
                            Log.e("Firestore", "Gagal ambil submodul: ${it.message}")
                            completed++
                            if (completed == modulDocs.size) {
                                listModul.addAll(tempList)
                                adapter.notifyDataSetChanged()
                            }
                        }
                }
            }
            .addOnFailureListener {
                Log.e("Firestore", "Gagal ambil modul utama: ${it.message}")
            }
    }

    private suspend fun getReadSubmodules(): Set<String> {
        return context?.let { ProgressPreferences(it).getReadSubmodules() } ?: emptySet()
    }


    override fun onResume() {
        super.onResume()
        adapter.notifyDataSetChanged() // untuk memicu progress hitung ulang
    }


    companion object {
        fun newInstance(materiId: String): ModulListFragment {
            val fragment = ModulListFragment()
            val args = Bundle()
            args.putString("materi_id", materiId)
            fragment.arguments = args
            return fragment
        }
    }
}
