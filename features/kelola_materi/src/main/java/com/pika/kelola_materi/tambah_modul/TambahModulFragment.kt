package com.pika.kelola_materi.tambah_modul

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.pika.kelola_materi.databinding.FragmentTambahModulBinding

class TambahModulFragment : Fragment() {

    private var _binding: FragmentTambahModulBinding? = null
    private val binding get() = _binding!!

    private lateinit var firestore: FirebaseFirestore
    private val materiIdList = mutableListOf<String>()
    private val materiNamaList = mutableListOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTambahModulBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        firestore = FirebaseFirestore.getInstance()
        loadMateriPembelajaran()

        binding.btnSimpanModul.setOnClickListener {
            val selectedIndex = binding.spinnerMateri.selectedItemPosition
            if (selectedIndex < 0 || materiIdList.isEmpty()) {
                Toast.makeText(requireContext(), "Pilih materi pembelajaran", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val selectedMateriId = materiIdList[selectedIndex]
            val namaModul = binding.etNamaModul.text.toString().trim()

            if (namaModul.isEmpty()) {
                Toast.makeText(requireContext(), "Nama modul tidak boleh kosong", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            simpanModulBaru(selectedMateriId, namaModul)
        }
    }

    private fun loadMateriPembelajaran() {
        firestore.collection("materi_pembelajaran").get()
            .addOnSuccessListener { snapshot ->
                materiIdList.clear()
                materiNamaList.clear()

                for (doc in snapshot) {
                    materiIdList.add(doc.id)
                    materiNamaList.add(doc.getString("nama") ?: "Tanpa Nama")
                }

                val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, materiNamaList)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.spinnerMateri.adapter = adapter
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Gagal memuat data", Toast.LENGTH_SHORT).show()
            }
    }

    private fun simpanModulBaru(materiId: String, namaModul: String) {
        val modulCollection = firestore.collection("modul").document(materiId).collection("list_modul")

        // Cek apakah modul sudah ada
        modulCollection
            .whereEqualTo("nama_modul", namaModul)
            .get()
            .addOnSuccessListener { snapshot ->
                if (!snapshot.isEmpty) {
                    Toast.makeText(requireContext(), "Modul sudah ada", Toast.LENGTH_SHORT).show()
                    return@addOnSuccessListener
                }

                val modulData = mapOf(
                    "nama_modul" to namaModul,
                    "timestamp" to FieldValue.serverTimestamp()
                )

                modulCollection
                    .add(modulData)
                    .addOnSuccessListener {
                        Toast.makeText(requireContext(), "Modul berhasil ditambahkan", Toast.LENGTH_SHORT).show()
                        binding.etNamaModul.text.clear()
                    }
                    .addOnFailureListener {
                        Toast.makeText(requireContext(), "Gagal menambahkan modul", Toast.LENGTH_SHORT).show()
                    }
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Gagal memeriksa modul", Toast.LENGTH_SHORT).show()
            }
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
