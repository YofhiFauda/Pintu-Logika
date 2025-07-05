package com.pika.halaman_materi.data.modul_list

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pika.halaman_materi.R
import com.pika.halaman_materi.data.adapter.ModulAdapter
import com.pika.halaman_materi.data.model.Modul
import com.pika.halaman_materi.data.model.SubModul
import android.util.Log
import android.widget.ProgressBar
import android.widget.TextView
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.pika.halaman_materi.utils.OnProgressUpdateListener

class ModulListFragment : Fragment(R.layout.fragment_modul_list) {

    private lateinit var rvModulList: RecyclerView
    private val firestore = FirebaseFirestore.getInstance()
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
                    val modulId = namaModul.replace(" ", "_").lowercase() // ⬅️ gunakan ini, sesuai struktur Firestore

                    firestore.collection("materi_$materiId")
                        .document(modulId)
                        .collection("sub_modul")
                        .get()
                        .addOnSuccessListener { subSnapshot ->
                            val subModulList = subSnapshot.documents.mapNotNull { subDoc ->
                                val nama = subDoc.getString("nama_sub_modul") ?: return@mapNotNull null
                                val judul = subDoc.getString("judul_materi") ?: ""
                                val konten = subDoc.getString("konten") ?: ""
                                val tanggal = subDoc.getString("tanggal_upload") ?: ""
                                SubModul(nama, judul, konten, tanggal)
                            }

                            Log.d("ModulListFragment", "Modul: $namaModul | Submodul: ${subModulList.size}")
                            tempList.add(Modul(namaModul, false, subModulList))

                            completed++
                            if (completed == modulDocs.size) {
                                listModul.addAll(tempList)
                                adapter.notifyDataSetChanged()
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
