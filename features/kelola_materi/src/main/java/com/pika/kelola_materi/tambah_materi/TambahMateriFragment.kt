package com.pika.kelola_materi.tambah_materi

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import com.pika.kelola_materi.R
import com.pika.kelola_materi.databinding.FragmentTambahMateriBinding
import com.pika.kelola_materi.databinding.FragmentTambahModulBinding
import java.util.Calendar

class TambahMateriFragment : Fragment() {

    private lateinit var binding: FragmentTambahMateriBinding
    private lateinit var firestore: FirebaseFirestore

    private val materiIdList = mutableListOf<String>()
    private val materiNamaList = mutableListOf<String>()
    private val modulNamaList = mutableListOf<String>()

    private var selectedMateriId: String? = null
    private var selectedMateriNama: String? = null
    private var selectedModulNama: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentTambahMateriBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        firestore = FirebaseFirestore.getInstance()

        loadMateriPembelajaran()

        binding.spinnerMateriPembelajaran.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
                selectedMateriId = materiIdList[pos]
                selectedMateriNama = materiNamaList[pos]
                loadModulDariMateri(selectedMateriId!!)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        binding.spinnerModul.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>, p1: View?, position: Int, p3: Long) {
                selectedModulNama = modulNamaList[position]
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }

        binding.etTanggalUpload.setOnClickListener {
            showDatePicker()
        }

        binding.btnSimpan.setOnClickListener {
            simpanMateri()
        }
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        DatePickerDialog(requireContext(), { _, year, month, day ->
            val formatted = String.format("%04d-%02d-%02d", year, month + 1, day)
            binding.etTanggalUpload.setText(formatted)
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
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
                binding.spinnerMateriPembelajaran.adapter = adapter
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Gagal memuat materi", Toast.LENGTH_SHORT).show()
            }
    }

    private fun loadModulDariMateri(materiId: String) {
        firestore.collection("modul")
            .document(materiId)
            .collection("list_modul")
            .orderBy("timestamp") // ASCENDING secara default
            .addSnapshotListener { snapshot, error ->
                if (error != null || snapshot == null) {
                    Toast.makeText(requireContext(), "Gagal memuat modul", Toast.LENGTH_SHORT).show()
                    return@addSnapshotListener
                }

                modulNamaList.clear()
                for (doc in snapshot) {
                    val namaModul = doc.getString("nama_modul")
                    if (!namaModul.isNullOrEmpty()) {
                        modulNamaList.add(namaModul)
                    }
                }

                val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, modulNamaList)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.spinnerModul.adapter = adapter
            }
    }


    private fun simpanMateri() {
        val subModulNama = binding.etNamaSubmodul.text.toString().trim()
        val judulMateri = binding.etJudulMateri.text.toString().trim()
        val tanggalUpload = binding.etTanggalUpload.text.toString().trim()
        val konten = binding.etKonten.text.toString().trim()

        if (selectedMateriId == null || selectedModulNama.isNullOrEmpty() || subModulNama.isEmpty() ||
            judulMateri.isEmpty() || tanggalUpload.isEmpty() || konten.isEmpty()
        ) {
            Toast.makeText(requireContext(), "Lengkapi semua field", Toast.LENGTH_SHORT).show()
            return
        }

        // Gunakan koleksi: materi_<materiId> / <modulId> / sub_modul
        val collectionPath = "materi_${selectedMateriId}"
        val modulDocId = selectedModulNama!!.replace(" ", "_").lowercase() // supaya konsisten

        val data = mapOf(
            "nama_sub_modul" to subModulNama,
            "judul_materi" to judulMateri,
            "tanggal_upload" to tanggalUpload,
            "konten" to konten
        )

        firestore.collection(collectionPath)
            .document(modulDocId)
            .collection("sub_modul")
            .add(data)
            .addOnSuccessListener {
                Toast.makeText(requireContext(), "Materi berhasil disimpan", Toast.LENGTH_SHORT).show()
                clearInput()
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Gagal menyimpan materi", Toast.LENGTH_SHORT).show()
            }
    }




    private fun clearInput() {
        binding.etNamaSubmodul.text?.clear()
        binding.etJudulMateri.text?.clear()
        binding.etTanggalUpload.text?.clear()
        binding.etKonten.text?.clear()
    }
}

