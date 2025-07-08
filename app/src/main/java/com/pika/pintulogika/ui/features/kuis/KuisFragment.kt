package com.pika.pintulogika.ui.features.kuis

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.digitallogic.halaman_kuis.aljabar_boolean.KuisAljabarBooleanActivity
import com.digitallogic.halaman_kuis.gerbang_logika.KuisGerbangLogikaActivity
import com.pika.halaman_materi.ui.MateriPembelajaranActivity
import com.pika.pintulogika.R
import com.pika.pintulogika.databinding.FragmentKuisBinding
import com.pika.pintulogika.databinding.FragmentMateriBinding

class KuisFragment : Fragment() {

    private lateinit var _binding: FragmentKuisBinding
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentKuisBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.cardKuisAljabarBoolean.setOnClickListener {
            val intent = Intent(requireContext(), KuisAljabarBooleanActivity::class.java)
            startActivity(intent)
        }

        binding.cardKuisGerbangLogika.setOnClickListener {
            val intent = Intent(requireContext(), KuisGerbangLogikaActivity::class.java)
            startActivity(intent)
        }
    }
}