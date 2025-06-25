package com.pika.kelola_materi

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.pika.kelola_materi.tambah_materi.TambahMateriFragment
import com.pika.kelola_materi.tambah_modul.TambahModulFragment

class KelolaMateriPagerAdapter (activity: FragmentActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int = 2
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> TambahModulFragment()
            else -> TambahMateriFragment()
        }
    }
}