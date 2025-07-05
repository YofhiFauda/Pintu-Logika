package com.pika.pintulogika.ui.features.materi

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import com.pika.pintulogika.R
import com.pika.pintulogika.databinding.FragmentMateriBinding
import kotlinx.coroutines.launch
import androidx.lifecycle.lifecycleScope
import com.pika.halaman_materi.ui.MateriPembelajaranActivity
import com.pika.kelola_materi.KelolaMateriActivity
import com.pika.pintulogika.data.session.SessionManager
import com.pika.pintulogika.ui.preauth.role.RoleActivity


class MateriFragment : Fragment() {

    private var _binding: FragmentMateriBinding? = null
    private val binding get() = _binding!!
    private lateinit var sessionManager: SessionManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMateriBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sessionManager = SessionManager(requireContext())

        setupToolButton()
        setupMateriGerbangLogika()
    }

    private fun setupMateriGerbangLogika() {
        binding.cardMateriGerbangLogika.setOnClickListener {
            val intent = Intent(requireContext(), MateriPembelajaranActivity::class.java)
            intent.putExtra("materi_id", "gerbang_logika")
            intent.putExtra("modul_id", "pendahuluan") // modul default, bisa diubah nanti
            startActivity(intent)
        }

        binding.cardMateriAljabarBoolean.setOnClickListener {
            val intent = Intent(requireContext(), MateriPembelajaranActivity::class.java)
            intent.putExtra("materi_id", "aljabar_boolean")
            intent.putExtra("modul_id", "pendahuluan") // modul default, bisa juga "pengenalan"
            startActivity(intent)
        }
    }


    private fun setupToolButton() {
        viewLifecycleOwner.lifecycleScope.launch {
            sessionManager.sessionState.collect { session ->
                when (session.role) {
                    "siswa" -> {
                        binding.btnKeluar.visibility = View.VISIBLE
                        binding.btnMore.visibility = View.GONE

                        buttonKeluarOnClick()
                    }
                    "guru" -> {
                        binding.btnKeluar.visibility = View.GONE
                        binding.btnMore.visibility = View.VISIBLE

                        buttonMoreOnClick()
                    }
                    else -> {
                        binding.btnKeluar.visibility = View.GONE
                        binding.btnMore.visibility = View.GONE
                    }
                }
            }
        }
    }

    private fun buttonKeluarOnClick(){
        binding.btnKeluar.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                sessionManager.clearSession()
                //FirebaseAuth.getInstance().signOut()
                val intent = Intent(requireContext(), RoleActivity::class.java)
                startActivity(intent)
                requireActivity().finish()
            }
        }
    }

    private fun buttonMoreOnClick(){
        binding.btnMore.setOnClickListener {
            val popup = PopupMenu(requireContext(), it)
            popup.menuInflater.inflate(R.menu.more_menu, popup.menu)

            popup.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.menu_kelolaMateri -> {
                        val intent = Intent(requireContext(), KelolaMateriActivity::class.java)
                        startActivity(intent)

                        true
                    }
                    R.id.menu_kuisMateri -> {
                        Toast.makeText(requireContext(), "Menu Kelola Kuis Materi Berhasil", Toast.LENGTH_SHORT).show()
                        true
                    }
                    R.id.menu_tampilanMateri -> {
                        Toast.makeText(requireContext(), "Menu Tampilkan Materi Berhasil", Toast.LENGTH_SHORT).show()
                        true
                    }
                    R.id.menu_keluar -> {
                        viewLifecycleOwner.lifecycleScope.launch {
                            sessionManager.logout()
                            //FirebaseAuth.getInstance().signOut()
                            val intent = Intent(requireContext(), RoleActivity::class.java)
                            startActivity(intent)
                            requireActivity().finish()
                        }
                        true
                    }
                    else -> false
                }
            }

            popup.show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}