package com.pika.halaman_materi.ui

import android.graphics.Paint
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.pika.halaman_materi.R
import com.pika.halaman_materi.data.modul_list.ModulListFragment
import com.pika.halaman_materi.data.session.ProgressPreferences
import com.pika.halaman_materi.databinding.ActivityMateriPembelajaranBinding
import kotlinx.coroutines.launch

class MateriPembelajaranActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMateriPembelajaranBinding
    private val firestore = FirebaseFirestore.getInstance()
    private val subModulList = mutableListOf<SubModulDynamic>()
    private var currentIndex = 0

    private lateinit var materiId: String
    private lateinit var modulId: String
    private var judulSubmodul: String = ""

    private val statusManager by lazy { ProgressPreferences(this) }
    private val FRAGMENT_TAG = "ModulListFragment"

    // Data class untuk dynamic content
    data class ContentItem(
        val type: String,
        val content: String,
        val style: String = "",
        val alignment: String = "left",
        val imageUrl: String = "",
        val caption: String = "",
        val linkText: String = "",
        val linkUrl: String = ""
    )

    // Updated SubModul with dynamic content support
    data class SubModulDynamic(
        val nama: String = "",
        val judul: String = "",
        val tanggalUpload: String = "",
        val dynamicContent: List<ContentItem> = listOf(),
        val isSelesai: Boolean = false
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMateriPembelajaranBinding.inflate(layoutInflater)
        setContentView(binding.root)

        materiId = intent.getStringExtra("materi_id") ?: run {
            Toast.makeText(this, "materi_id tidak ditemukan", Toast.LENGTH_SHORT).show()
            finish(); return
        }

        modulId = intent.getStringExtra("modul_id") ?: run {
            Toast.makeText(this, "modul_id tidak ditemukan", Toast.LENGTH_SHORT).show()
            finish(); return
        }

        judulSubmodul = intent.getStringExtra("judul_submodul") ?: ""
        currentIndex = subModulList.indexOfFirst { it.nama == judulSubmodul }.coerceAtLeast(0)

        setupToolbar()
        setupEdgeToEdge()
        setupBottomNavigation()
        setupScrollBehavior()

        fetchSubModulFromFirestore()

        binding.btnDaftarMenu.setOnClickListener {
            Log.d("Activity", "materiId yang dikirim: $materiId")
            val existingFragment = supportFragmentManager.findFragmentByTag(FRAGMENT_TAG)
            if (existingFragment != null && existingFragment.isVisible) {
                supportFragmentManager.popBackStack()
            } else {
                val fragment = ModulListFragment.newInstance(materiId)
                supportFragmentManager.beginTransaction()
                    .setCustomAnimations(
                        com.pika.core_ui.R.anim.slide_in_right,
                        com.pika.core_ui.R.anim.slide_out_right,
                        com.pika.core_ui.R.anim.slide_in_right,
                        com.pika.core_ui.R.anim.slide_out_right
                    )
                    .add(R.id.fragment_container, fragment, FRAGMENT_TAG)
                    .addToBackStack(null)
                    .commit()
            }
        }
    }

    private fun fetchSubModulFromFirestore() {
        val path = "modul/$materiId/list_modul"

        firestore.collection(path)
            .orderBy("timestamp")
            .get()
            .addOnSuccessListener { modulDocs ->
                val allFetchTasks = mutableListOf<com.google.android.gms.tasks.Task<QuerySnapshot>>()
                subModulList.clear()

                for (modulDoc in modulDocs) {
                    val namaModul = modulDoc.getString("nama_modul") ?: continue
                    val modulId = namaModul.replace(" ", "_").lowercase()

                    val task = firestore.collection("materi_$materiId")
                        .document(modulId)
                        .collection("sub_modul")
                        .get()

                    allFetchTasks.add(task)
                }

                com.google.android.gms.tasks.Tasks.whenAllSuccess<QuerySnapshot>(allFetchTasks)
                    .addOnSuccessListener { allResults ->
                        for (result in allResults) {
                            for (doc in result.documents) {
                                val nama = doc.getString("nama_sub_modul") ?: continue
                                val judul = doc.getString("judul_materi") ?: ""
                                val tanggal = doc.getString("tanggal_upload") ?: ""

                                // Parse dynamic content
                                val dynamicContentList = mutableListOf<ContentItem>()
                                val dynamicContentData = doc.get("dynamic_content") as? List<Map<String, Any>>

                                dynamicContentData?.forEach { contentMap ->
                                    val contentItem = ContentItem(
                                        type = contentMap["type"] as? String ?: "",
                                        content = contentMap["content"] as? String ?: "",
                                        style = contentMap["style"] as? String ?: "",
                                        alignment = contentMap["alignment"] as? String ?: "left",
                                        imageUrl = contentMap["imageUrl"] as? String ?: "",
                                        caption = contentMap["caption"] as? String ?: "",
                                        linkText = contentMap["linkText"] as? String ?: "",
                                        linkUrl = contentMap["linkUrl"] as? String ?: ""
                                    )
                                    dynamicContentList.add(contentItem)
                                }

                                subModulList.add(
                                    SubModulDynamic(
                                        nama = nama,
                                        judul = judul,
                                        tanggalUpload = tanggal,
                                        dynamicContent = dynamicContentList
                                    )
                                )
                            }
                        }

                        // Sort by created_at if available, otherwise by name
                        subModulList.sortBy { it.nama }

                        if (judulSubmodul.isNotEmpty()) {
                            currentIndex = subModulList.indexOfFirst { it.nama == judulSubmodul }
                                .coerceAtLeast(0)
                        } else {
                            currentIndex = 0
                        }

                        if (subModulList.isNotEmpty()) {
                            updateContent(subModulList[currentIndex])
                            updateNavigationButtons()
                        } else {
                            Toast.makeText(this, "Tidak ada submodul ditemukan", Toast.LENGTH_SHORT).show()
                        }
                    }
                    .addOnFailureListener { exception ->
                        Log.e("MateriActivity", "Error fetching submodules", exception)
                        Toast.makeText(this, "Gagal memuat submodul: ${exception.message}", Toast.LENGTH_SHORT).show()
                    }
            }
            .addOnFailureListener { exception ->
                Log.e("MateriActivity", "Error fetching modules", exception)
                Toast.makeText(this, "Gagal memuat modul utama: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun updateContent(subModul: SubModulDynamic) {
        binding.contentTitle.text = subModul.judul
        binding.bottomTitle.text = subModul.judul

        // Clear existing content
        binding.contentContainer.removeAllViews()

        // Render dynamic content
        renderDynamicContent(subModul.dynamicContent)

        binding.nestedScrollView.smoothScrollTo(0, 0)

        // Save that submodule has been read
        lifecycleScope.launch {
            statusManager.markAsRead(subModul.nama)
        }
    }

    private fun renderDynamicContent(contentItems: List<ContentItem>) {
        if (contentItems.isEmpty()) {
            val emptyText = TextView(this).apply {
                text = "Konten tidak tersedia"
                textSize = 14f
                setTextColor(resources.getColor(com.pika.core_ui.R.color.gray, null))
                gravity = Gravity.CENTER
                setPadding(32, 32, 32, 32)
            }
            binding.contentContainer.addView(emptyText)
            return
        }

        contentItems.forEach { item ->
            when (item.type) {
                "TEXT" -> addTextContent(item)
                "IMAGE" -> addImageContent(item)
                "LINK" -> addLinkContent(item)
            }
        }
    }

    private fun addTextContent(item: ContentItem) {
        val textView = TextView(this).apply {
            text = item.content

            // Apply alignment
            gravity = when (item.alignment) {
                "center" -> Gravity.CENTER
                "right" -> Gravity.END
                else -> Gravity.START
            }

            // Apply text style
            when (item.style) {
                "Heading 1" -> {
                    textSize = 24f
                    setTypeface(typeface, Typeface.BOLD)
                    setTextColor(resources.getColor(com.pika.core_ui.R.color.black, null))
                }
                "Heading 2" -> {
                    textSize = 20f
                    setTypeface(typeface, Typeface.BOLD)
                    setTextColor(resources.getColor(com.pika.core_ui.R.color.black, null))
                }
                "Heading 3" -> {
                    textSize = 18f
                    setTypeface(typeface, Typeface.BOLD)
                    setTextColor(resources.getColor(com.pika.core_ui.R.color.black, null))
                }
                "Bold" -> {
                    textSize = 16f
                    setTypeface(typeface, Typeface.BOLD)
                    setTextColor(resources.getColor(com.pika.core_ui.R.color.black, null))
                }
                "Italic" -> {
                    textSize = 16f
                    setTypeface(typeface, Typeface.ITALIC)
                    setTextColor(resources.getColor(com.pika.core_ui.R.color.black, null))
                }
                else -> {
                    textSize = 16f
                    setTextColor(resources.getColor(com.pika.core_ui.R.color.black, null))
                }
            }

            val layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(0, 16, 0, 16)
            }
            this.layoutParams = layoutParams
            setPadding(24, 12, 24, 12)
        }

        binding.contentContainer.addView(textView)
    }

    private fun addImageContent(item: ContentItem) {
        val container = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(0, 24, 0, 24)
            }

            // Apply alignment to container
            gravity = when (item.alignment) {
                "center" -> Gravity.CENTER
                "right" -> Gravity.END
                else -> Gravity.START
            }
        }

        val imageView = ImageView(this).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                dpToPx(200) // Fixed height
            ).apply {
                setMargins(24, 0, 24, 0)
            }
            scaleType = ImageView.ScaleType.CENTER_CROP

            // Load image from ImageKit URL
            val imageUrl = if (item.imageUrl.isNotEmpty()) item.imageUrl else item.content

            Glide.with(this@MateriPembelajaranActivity)
                .load(imageUrl)
                .apply(RequestOptions().transform(RoundedCorners(16)))
                .placeholder(com.pika.core_ui.R.color.light_gray)
                .error(com.pika.core_ui.R.drawable.ic_error_24) // You need to add this drawable
                .into(this)
        }

        container.addView(imageView)

        // Add caption if available
        if (item.caption.isNotEmpty()) {
            val captionView = TextView(this).apply {
                text = item.caption
                textSize = 14f
                setTextColor(resources.getColor(com.pika.core_ui.R.color.gray, null))
                gravity = when (item.alignment) {
                    "center" -> Gravity.CENTER
                    "right" -> Gravity.END
                    else -> Gravity.START
                }
                setPadding(24, 8, 24, 0)
                setTypeface(typeface, Typeface.ITALIC)
            }
            container.addView(captionView)
        }

        binding.contentContainer.addView(container)
    }

    private fun addLinkContent(item: ContentItem) {
        val linkView = TextView(this).apply {
            text = item.linkText.ifEmpty { item.linkUrl }
            textSize = 16f
            setTextColor(resources.getColor(com.pika.core_ui.R.color.blue_medium, null))
            paintFlags = paintFlags or Paint.UNDERLINE_TEXT_FLAG

            val layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(0, 12, 0, 12)
            }
            this.layoutParams = layoutParams
            setPadding(24, 16, 24, 16)

            // Make it clickable
            isClickable = true
            isFocusable = true

            setOnClickListener {
                try {
                    val intent = android.content.Intent(android.content.Intent.ACTION_VIEW, Uri.parse(item.linkUrl))
                    context.startActivity(intent)
                } catch (e: Exception) {
                    Toast.makeText(context, "Tidak dapat membuka link", Toast.LENGTH_SHORT).show()
                }
            }

            // Add ripple effect
            val typedValue = android.util.TypedValue()
            context.theme.resolveAttribute(android.R.attr.selectableItemBackground, typedValue, true)
            setBackgroundResource(typedValue.resourceId)
        }

        binding.contentContainer.addView(linkView)
    }

    private fun dpToPx(dp: Int): Int {
        val density = resources.displayMetrics.density
        return (dp * density).toInt()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(false)
            setDisplayShowHomeEnabled(false)
            setDisplayShowTitleEnabled(false)
        }

        binding.btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.btnSearch.setOnClickListener {
            Toast.makeText(this, "Tombol Pencarian DiKlik", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupBottomNavigation() {
        binding.btnPrevious.setOnClickListener {
            if (currentIndex > 0) {
                currentIndex--
                updateContent(subModulList[currentIndex])
                updateNavigationButtons()

                lifecycleScope.launch {
                    statusManager.markAsRead(subModulList[currentIndex].nama)
                }
            } else {
                Toast.makeText(this, "Ini adalah konten pertama", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnNext.setOnClickListener {
            if (currentIndex < subModulList.size - 1) {
                currentIndex++
                updateContent(subModulList[currentIndex])
                updateNavigationButtons()

                lifecycleScope.launch {
                    statusManager.markAsRead(subModulList[currentIndex].nama)
                }
            } else {
                Toast.makeText(this, "Ini adalah konten terakhir", Toast.LENGTH_SHORT).show()
            }
        }

        updateNavigationButtons()
    }

    private fun updateNavigationButtons() {
        binding.btnPrevious.apply {
            alpha = if (currentIndex > 0) 1.0f else 0.5f
            isEnabled = currentIndex > 0
        }
        binding.btnNext.apply {
            alpha = if (currentIndex < subModulList.size - 1) 1.0f else 0.5f
            isEnabled = currentIndex < subModulList.size - 1
        }
    }

    private fun setupScrollBehavior() {
        binding.nestedScrollView.setOnScrollChangeListener { _: NestedScrollView?, _: Int, scrollY: Int, _: Int, _: Int ->
            val scrollRange = binding.nestedScrollView.getChildAt(0).height - binding.nestedScrollView.height
            if (scrollRange > 0) {
                val scrollProgress = (scrollY.toFloat() / scrollRange * 100).toInt()
                // Could be used for progress indicator
            }
        }
    }

    @Suppress("DEPRECATION")
    private fun setupEdgeToEdge() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { _, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            val navigationBars = insets.getInsets(WindowInsetsCompat.Type.navigationBars())
            binding.bottomNavigationContainer.updatePadding(bottom = navigationBars.bottom)
            binding.root.updatePadding(top = systemBars.top)
            insets
        }

        window.statusBarColor = android.graphics.Color.TRANSPARENT
        window.navigationBarColor = android.graphics.Color.TRANSPARENT
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
            window.setDecorFitsSystemWindows(false)
        } else {
            window.decorView.systemUiVisibility = (
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                            View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    )
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}