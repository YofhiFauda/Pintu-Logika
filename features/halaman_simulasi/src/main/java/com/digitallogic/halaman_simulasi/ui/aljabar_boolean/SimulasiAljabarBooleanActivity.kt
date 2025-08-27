package com.digitallogic.halaman_simulasi.ui.aljabar_boolean

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.digitallogic.halaman_simulasi.R
import com.digitallogic.halaman_simulasi.adapter.BooleanLawAdapter
import com.digitallogic.halaman_simulasi.data.BooleanLaw

class SimulasiAljabarBooleanActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: BooleanLawAdapter

    private val booleanLaws = listOf(
        BooleanLaw(
            1,
            "Hukum Identitas",
            "Operasi dengan elemen identitas tidak mengubah nilai",
            "A + 0 = A",
            "A · 1 = A"
        ),
        BooleanLaw(
            2,
            "Hukum Komplemen",
            "Operasi dengan komplemen menghasilkan nilai tetap",
            "A + A' = 1",
            "A · A' = 0"
        ),
        BooleanLaw(
            3,
            "Hukum Dominasi",
            "Elemen dominan menentukan hasil operasi",
            "A + 1 = 1",
            "A · 0 = 0"
        ),
        BooleanLaw(
            4,
            "Hukum Idempoten",
            "Operasi dengan diri sendiri menghasilkan nilai yang sama",
            "A + A = A",
            "A · A = A"
        ),
        BooleanLaw(
            5,
            "Hukum Involusi",
            "Komplemen ganda mengembalikan nilai asli",
            "(A')' = A"
        ),
        BooleanLaw(
            6,
            "Hukum Distributif",
            "Distribusi operasi terhadap operasi lain",
            "A · (B + C) = (A · B) + (A · C)",
            "A + (B · C) = (A + B) · (A + C)"
        ),
        BooleanLaw(
            7,
            "Hukum Komutatif",
            "Urutan operand tidak mempengaruhi hasil operasi",
            "A + B = B + A",
            "A · B = B · A"
        ),
        BooleanLaw(
            8,
            "Hukum De Morgan",
            "Komplemen dari operasi sama dengan operasi komplemen",
            "(A + B)' = A' · B'",
            "(A · B)' = A' + B'"
        )
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_simulasi_aljabar_boolean)

        setupRecyclerView()
    }
    private fun setupRecyclerView() {
        recyclerView = findViewById(R.id.recyclerViewLaws)
        adapter = BooleanLawAdapter(booleanLaws) { law ->
            openSimulation(law)
        }

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }


    private fun openSimulation(law: BooleanLaw) {
        val intent = Intent(this, DetailSimulasiAljabarBooleanActivity::class.java)
        intent.putExtra("LAW_ID", law.id)
        intent.putExtra("LAW_TITLE", law.title)
        intent.putExtra("LAW_DESCRIPTION", law.description)
        intent.putExtra("LAW_FORMULA1", law.formula1)
        law.formula2?.let { intent.putExtra("LAW_FORMULA2", it) }
        startActivity(intent)
    }
}