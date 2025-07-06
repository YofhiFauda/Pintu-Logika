package com.pika.pintulogika.ui.features.simulasi

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
import com.digitallogic.halaman_simulasi.adapter.LogicSimulasiAdapter
import com.digitallogic.halaman_simulasi.ui.SimulasiDetailActivity
import com.pika.pintulogika.databinding.FragmentSimulasiBinding

class SimulasiFragment : Fragment() {

    private var _binding: FragmentSimulasiBinding? = null
    private val binding get() = _binding!!

    val logicList = listOf(
        LogicSimulasi("LOGIKA AND", "Simulasi gerbang logika AND", com.pika.core_ui.R.drawable.ic_gate_and),
        LogicSimulasi("LOGIKA OR", "Simulasi gerbang logika OR", com.pika.core_ui.R.drawable.ic_gate_or),
        LogicSimulasi("LOGIKA NOT", "Simulasi gerbang logika NOT", com.pika.core_ui.R.drawable.ic_gate_not),
        LogicSimulasi("LOGIKA NAND", "Simulasi gerbang logika NAND", com.pika.core_ui.R.drawable.ic_gate_nand),
        LogicSimulasi("LOGIKA NOR", "Simulasi gerbang logika NOR", com.pika.core_ui.R.drawable.ic_gate_nor),
        LogicSimulasi("LOGIKA XOR", "Simulasi gerbang logika XOR", com.pika.core_ui.R.drawable.ic_gate_xor),
        LogicSimulasi("LOGIKA XNOR", "Simulasi gerbang logika XNOR", com.pika.core_ui.R.drawable.ic_gate_xnor),

    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSimulasiBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupWindowInsets()

    }

    private fun setupRecyclerView() {
        val adapter = LogicSimulasiAdapter(logicList) { selectedItem ->
            val intent = Intent(requireContext(), SimulasiDetailActivity::class.java)
            intent.putExtra("GATE_TYPE", selectedItem.title)
            startActivity(intent)
        }

        binding.rvLogicSimulasi.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvLogicSimulasi.adapter = adapter
    }

    private fun setupWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())

            // Set padding untuk container utama
            view.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                0 // Tidak set bottom padding di sini
            )


            // Set padding untuk RecyclerView
            binding.rvLogicSimulasi.setPadding(
                16, // left
                16, // top
                16, // right
                systemBars.bottom + 16 // bottom (untuk BottomNavigation)
            )
            insets
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}