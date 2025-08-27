package com.pika.pintulogika.ui.features.simulasi

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.digitallogic.core_data.model.LogicSimulasi
import com.digitallogic.halaman_kuis.aljabar_boolean.KuisAljabarBooleanActivity
import com.digitallogic.halaman_kuis.gerbang_logika.KuisGerbangLogikaActivity
import com.digitallogic.halaman_simulasi.adapter.LogicSimulasiAdapter
import com.digitallogic.halaman_simulasi.ui.aljabar_boolean.SimulasiAljabarBooleanActivity
import com.digitallogic.halaman_simulasi.ui.gerbang_logika.SimulasiDetailActivity
import com.digitallogic.halaman_simulasi.ui.gerbang_logika.SimulasiGerbangLogikaActivity
import com.pika.pintulogika.databinding.FragmentSimulasiBinding

class SimulasiFragment : Fragment() {

    private var _binding: FragmentSimulasiBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSimulasiBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.cardKuisAljabarBoolean.setOnClickListener {
            val intent = Intent(requireContext(), SimulasiAljabarBooleanActivity::class.java)
            startActivity(intent)
        }

        binding.cardKuisGerbangLogika.setOnClickListener {
            val intent = Intent(requireContext(), SimulasiGerbangLogikaActivity::class.java)
            startActivity(intent)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}