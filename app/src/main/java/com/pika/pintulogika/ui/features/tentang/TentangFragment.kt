package com.pika.pintulogika.ui.features.tentang

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.pika.pintulogika.R
import com.pika.pintulogika.databinding.FragmentTentangBinding
import com.pika.pintulogika.ui.features.tentang.bantuan.BantuanActivity
import com.pika.pintulogika.ui.features.tentang.panduan.PanduanActivity


class TentangFragment : Fragment() {

    private var _binding: FragmentTentangBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTentangBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnPanduan.setOnClickListener {
            val intent = Intent(requireContext(), PanduanActivity::class.java)
            startActivity(intent)
        }

        binding.btnBantuan.setOnClickListener {
            val intent = Intent(requireContext(), BantuanActivity::class.java)
            startActivity(intent)
        }
    }
}