package com.pika.kelola_materi.tambah_materi

import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.cardview.widget.CardView
import androidx.lifecycle.lifecycleScope
import com.digitallogic.core_data.model.materi.ContentItem
import com.digitallogic.core_data.model.materi.ContentType
import com.digitallogic.core_data.remote.retrofit.RetrofitClient
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.pika.kelola_materi.R
import com.pika.kelola_materi.databinding.FragmentTambahMateriBinding
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.util.*

class TambahMateriFragment : Fragment() {

    private lateinit var binding: FragmentTambahMateriBinding
    private lateinit var firestore: FirebaseFirestore
    private lateinit var storage: FirebaseStorage


    private val materiIdList = mutableListOf<String>()
    private val materiNamaList = mutableListOf<String>()
    private val modulNamaList = mutableListOf<String>()

    private var selectedMateriId: String? = null
    private var selectedMateriNama: String? = null
    private var selectedModulNama: String? = null

    // Dynamic Content Management
    private val contentItems = mutableListOf<ContentItem>()
    private var currentEditingType: ContentType? = null
    private var selectedImageUri: Uri? = null

    // Image picker
    private val imagePickerLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            selectedImageUri = it
            binding.ivPreview.setImageURI(it)
            binding.ivPreview.visibility = View.VISIBLE
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentTambahMateriBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firestore = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance()

        setupInitialData()
        setupDynamicContentButtons()
        setupSpinners()

        val rootView = requireActivity().findViewById<View>(android.R.id.content)

        rootView.viewTreeObserver.addOnGlobalLayoutListener {
            val rect = android.graphics.Rect()
            rootView.getWindowVisibleDisplayFrame(rect)

            val screenHeight = rootView.rootView.height
            val keypadHeight = screenHeight - rect.bottom

            if (keypadHeight > screenHeight * 0.10) {
                // Keyboard is visible
                binding.scrollContainer.setPadding(0, 0, 0, keypadHeight)
            } else {
                // Keyboard is hidden
                binding.scrollContainer.setPadding(0, 0, 0, 0)
            }
        }

    }

    private fun setupInitialData() {
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

        binding.scrollContainer.post {
            binding.scrollContainer.fullScroll(View.FOCUS_DOWN)
        }

    }

    private fun setupDynamicContentButtons() {
        binding.btnAddText.setOnClickListener {
            showContentEditor(ContentType.TEXT)
        }

        binding.btnAddImage.setOnClickListener {
            showContentEditor(ContentType.IMAGE)
        }

        binding.btnAddLink.setOnClickListener {
            showContentEditor(ContentType.LINK)
        }

        binding.btnSelectImage.setOnClickListener {
            imagePickerLauncher.launch("image/*")
        }

        binding.btnAddContent.setOnClickListener {
            addContentToList()
        }

        binding.btnCancelEdit.setOnClickListener {
            hideContentEditor()
        }
    }

    private fun setupSpinners() {
        // Text Style Spinner
        val textStyles = arrayOf("Normal", "Heading 1", "Heading 2", "Heading 3", "Bold", "Italic")
        val textStyleAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, textStyles)
        textStyleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerTextStyle.adapter = textStyleAdapter

        // Text Alignment Spinner
        val alignments = arrayOf("Kiri", "Tengah", "Kanan")
        val alignmentAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, alignments)
        alignmentAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerTextAlign.adapter = alignmentAdapter

        // Image Alignment Spinner
        val imageAlignmentAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, alignments)
        imageAlignmentAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerImageAlign.adapter = imageAlignmentAdapter
    }

    private fun showContentEditor(type: ContentType) {
        currentEditingType = type
        binding.editorCard.visibility = View.VISIBLE

        // Hide all editors first
        binding.textEditor.visibility = View.GONE
        binding.imageEditor.visibility = View.GONE
        binding.linkEditor.visibility = View.GONE

        when (type) {
            ContentType.TEXT -> {
                binding.editorTitle.text = "Tambah Teks"
                binding.textEditor.visibility = View.VISIBLE
            }
            ContentType.IMAGE -> {
                binding.editorTitle.text = "Tambah Gambar"
                binding.imageEditor.visibility = View.VISIBLE
                selectedImageUri = null
                binding.ivPreview.visibility = View.GONE
            }
            ContentType.LINK -> {
                binding.editorTitle.text = "Tambah Link"
                binding.linkEditor.visibility = View.VISIBLE
            }
        }
    }

    private fun hideContentEditor() {
        binding.editorCard.visibility = View.GONE
        clearEditorFields()
    }

    private fun clearEditorFields() {
        binding.etTextContent.text?.clear()
        binding.etImageCaption.text?.clear()
        binding.etLinkText.text?.clear()
        binding.etLinkUrl.text?.clear()
        binding.ivPreview.visibility = View.GONE
        selectedImageUri = null
    }

    private fun addContentToList() {
        when (currentEditingType) {
            ContentType.TEXT -> {
                val content = binding.etTextContent.text.toString().trim()
                if (content.isNotEmpty()) {
                    val style = binding.spinnerTextStyle.selectedItem.toString()
                    val alignment = getAlignmentValue(binding.spinnerTextAlign.selectedItem.toString())

                    contentItems.add(
                        ContentItem(
                            type = ContentType.TEXT,
                            content = content,
                            style = style,
                            alignment = alignment
                        )
                    )
                    updateContentPreview()
                    hideContentEditor()
                } else {
                    Toast.makeText(requireContext(), "Masukan teks terlebih dahulu", Toast.LENGTH_SHORT).show()
                }
            }

            ContentType.IMAGE -> {
                if (selectedImageUri != null) {
                    val caption = binding.etImageCaption.text.toString().trim()
                    val alignment = getAlignmentValue(binding.spinnerImageAlign.selectedItem.toString())

                    contentItems.add(
                        ContentItem(
                            type = ContentType.IMAGE,
                            content = selectedImageUri.toString(),
                            alignment = alignment,
                            caption = caption
                        )
                    )
                    updateContentPreview()
                    hideContentEditor()
                } else {
                    Toast.makeText(requireContext(), "Pilih gambar terlebih dahulu", Toast.LENGTH_SHORT).show()
                }
            }

            ContentType.LINK -> {
                val linkText = binding.etLinkText.text.toString().trim()
                val linkUrl = binding.etLinkUrl.text.toString().trim()

                if (linkText.isNotEmpty() && linkUrl.isNotEmpty()) {
                    contentItems.add(
                        ContentItem(
                            type = ContentType.LINK,
                            content = linkText,
                            linkText = linkText,
                            linkUrl = linkUrl
                        )
                    )
                    updateContentPreview()
                    hideContentEditor()
                } else {
                    Toast.makeText(requireContext(), "Lengkapi teks dan URL link", Toast.LENGTH_SHORT).show()
                }
            }

            null -> {
                Toast.makeText(requireContext(), "Error: Tipe konten tidak valid", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getAlignmentValue(alignment: String): String {
        return when (alignment) {
            "Kiri" -> "left"
            "Tengah" -> "center"
            "Kanan" -> "right"
            else -> "left"
        }
    }

    private fun updateContentPreview() {
        binding.contentPreview.removeAllViews()

        if (contentItems.isEmpty()) {
            val emptyText = TextView(requireContext()).apply {
                text = "Preview Konten"
                textSize = 14f
                setTextColor(resources.getColor(com.pika.core_ui.R.color.gray, null))
                gravity = android.view.Gravity.CENTER
            }
            binding.contentPreview.addView(emptyText)
            return
        }

        contentItems.forEachIndexed { index, item ->
            val itemContainer = LinearLayout(requireContext()).apply {
                orientation = LinearLayout.VERTICAL
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    setMargins(0, 8, 0, 8)
                }
                setPadding(16, 12, 16, 12)
                setBackgroundResource(com.pika.core_ui.R.drawable.content_item_background) // You'll need to create this drawable
            }

            when (item.type) {
                ContentType.TEXT -> {
                    val textView = TextView(requireContext()).apply {
                        text = item.content
                        gravity = when (item.alignment) {
                            "center" -> android.view.Gravity.CENTER
                            "right" -> android.view.Gravity.END
                            else -> android.view.Gravity.START
                        }

                        when (item.style) {
                            "Heading 1" -> {
                                textSize = 24f
                                setTypeface(typeface, android.graphics.Typeface.BOLD)
                            }
                            "Heading 2" -> {
                                textSize = 20f
                                setTypeface(typeface, android.graphics.Typeface.BOLD)
                            }
                            "Heading 3" -> {
                                textSize = 18f
                                setTypeface(typeface, android.graphics.Typeface.BOLD)
                            }
                            "Bold" -> {
                                setTypeface(typeface, android.graphics.Typeface.BOLD)
                            }
                            "Italic" -> {
                                setTypeface(typeface, android.graphics.Typeface.ITALIC)
                            }
                            else -> {
                                textSize = 14f
                            }
                        }
                        setTextColor(resources.getColor(com.pika.core_ui.R.color.black, null))
                    }
                    itemContainer.addView(textView)
                }

                ContentType.IMAGE -> {
                    val imageView = ImageView(requireContext()).apply {
                        layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            400
                        ).apply {
                            gravity = when (item.alignment) {
                                "center" -> android.view.Gravity.CENTER
                                "right" -> android.view.Gravity.END
                                else -> android.view.Gravity.START
                            }
                        }
                        scaleType = ImageView.ScaleType.CENTER_CROP
                        setImageURI(Uri.parse(item.content))
                        setBackgroundResource(com.pika.core_ui.R.color.light_gray)
                    }
                    itemContainer.addView(imageView)

                    if (item.caption.isNotEmpty()) {
                        val captionView = TextView(requireContext()).apply {
                            text = item.caption
                            textSize = 12f
                            setTextColor(resources.getColor(com.pika.core_ui.R.color.gray, null))
                            gravity = when (item.alignment) {
                                "center" -> android.view.Gravity.CENTER
                                "right" -> android.view.Gravity.END
                                else -> android.view.Gravity.START
                            }
                        }
                        itemContainer.addView(captionView)
                    }
                }

                ContentType.LINK -> {
                    val linkView = TextView(requireContext()).apply {
                        text = item.linkText
                        textSize = 14f
                        setTextColor(resources.getColor(com.pika.core_ui.R.color.blue_medium, null))
                        paintFlags = paintFlags or android.graphics.Paint.UNDERLINE_TEXT_FLAG
                        isClickable = true
                        setOnClickListener {
                            // Handle link click - you can open in browser or show dialog
                            Toast.makeText(context, "Link: ${item.linkUrl}", Toast.LENGTH_SHORT).show()
                        }
                    }
                    itemContainer.addView(linkView)
                }
            }

            // Add delete button
            val deleteButton = Button(requireContext()).apply {
                text = "Hapus"
                textSize = 10f
                setBackgroundColor(resources.getColor(com.pika.core_ui.R.color.red_500, null))
                setTextColor(resources.getColor(com.pika.core_ui.R.color.white, null))
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    gravity = android.view.Gravity.END
                }
                setOnClickListener {
                    contentItems.removeAt(index)
                    updateContentPreview()
                }
            }
            itemContainer.addView(deleteButton)

            binding.contentPreview.addView(itemContainer)
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
            .orderBy("timestamp")
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

        if (selectedMateriId == null || selectedModulNama.isNullOrEmpty() || subModulNama.isEmpty() ||
            judulMateri.isEmpty() || tanggalUpload.isEmpty() || contentItems.isEmpty()
        ) {
            Toast.makeText(requireContext(), "Lengkapi semua field dan tambahkan minimal satu konten", Toast.LENGTH_SHORT).show()
            return
        }

        // Show loading
        binding.btnSimpan.isEnabled = false
        binding.btnSimpan.text = "Menyimpan..."

        // Upload images first, then save content
        uploadImagesAndSaveContent(subModulNama, judulMateri, tanggalUpload)
    }

    private fun uriToMultipart(uri: Uri, name: String): MultipartBody.Part {
        val context = requireContext()
        val contentResolver = context.contentResolver
        val inputStream = contentResolver.openInputStream(uri) ?: throw IllegalStateException("Cannot open URI")
        val fileBytes = inputStream.readBytes()
        val requestFile = fileBytes.toRequestBody("image/*".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData("file", name, requestFile)
    }


    private fun uploadImagesAndSaveContent(
        subModulNama: String,
        judulMateri: String,
        tanggalUpload: String
    ) {
        val imageItems = contentItems.filter { it.type == ContentType.IMAGE }

        if (imageItems.isEmpty()) {
            saveContentToFirestore(subModulNama, judulMateri, tanggalUpload, contentItems)
            return
        }

        val updatedContentItems = contentItems.toMutableList()
        var uploadedCount = 0

        imageItems.forEachIndexed { index, item ->
            val imageUri = Uri.parse(item.content)
            val fileName = "materi_image_${System.currentTimeMillis()}_$index.jpg"

            val part = try {
                uriToMultipart(imageUri, fileName)
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Gagal membaca file gambar", Toast.LENGTH_SHORT).show()
                return
            }

            val fileNameBody = fileName.toRequestBody("text/plain".toMediaTypeOrNull())

            // 🔁 Upload ke ImageKit
            lifecycleScope.launch {
                try {
                    val response = RetrofitClient.instance.uploadImage(
                        file = part,
                        fileName = fileNameBody
                    )

                    if (response.isSuccessful && response.body() != null) {
                        val imageUrl = response.body()!!.url

                        val itemIndex = updatedContentItems.indexOfFirst {
                            it.type == ContentType.IMAGE && it.content == item.content
                        }

                        if (itemIndex != -1) {
                            updatedContentItems[itemIndex] = updatedContentItems[itemIndex].copy(
                                content = imageUrl,
                                imageUrl = imageUrl
                            )
                        }

                        uploadedCount++
                        if (uploadedCount == imageItems.size) {
                            saveContentToFirestore(subModulNama, judulMateri, tanggalUpload, updatedContentItems)
                        }
                    } else {
                        Toast.makeText(requireContext(), "Upload gagal: ${response.code()}", Toast.LENGTH_SHORT).show()
                        binding.btnSimpan.isEnabled = true
                        binding.btnSimpan.text = "Simpan Materi"
                    }
                } catch (e: Exception) {
                    Toast.makeText(requireContext(), "Gagal upload ke ImageKit: ${e.message}", Toast.LENGTH_SHORT).show()
                    binding.btnSimpan.isEnabled = true
                    binding.btnSimpan.text = "Simpan Materi"
                }
            }
        }
    }


    private fun saveContentToFirestore(subModulNama: String, judulMateri: String, tanggalUpload: String, finalContentItems: List<ContentItem>) {
        val collectionPath = "materi_${selectedMateriId}"
        val modulDocId = selectedModulNama!!.replace(" ", "_").lowercase()

        // Convert content items to serializable format
        val contentData = finalContentItems.map { item ->
            mapOf(
                "type" to item.type.name,
                "content" to item.content,
                "style" to item.style,
                "alignment" to item.alignment,
                "imageUrl" to item.imageUrl,
                "caption" to item.caption,
                "linkText" to item.linkText,
                "linkUrl" to item.linkUrl
            )
        }

        val data = mapOf(
            "nama_sub_modul" to subModulNama,
            "judul_materi" to judulMateri,
            "tanggal_upload" to tanggalUpload,
            "dynamic_content" to contentData,
            "created_at" to System.currentTimeMillis()
        )

        firestore.collection(collectionPath)
            .document(modulDocId)
            .collection("sub_modul")
            .add(data)
            .addOnSuccessListener {
                Toast.makeText(requireContext(), "Materi berhasil disimpan", Toast.LENGTH_SHORT).show()
                clearAllInput()
                binding.btnSimpan.isEnabled = true
                binding.btnSimpan.text = "Simpan Materi"
            }
            .addOnFailureListener { exception ->
                Toast.makeText(requireContext(), "Gagal menyimpan materi: ${exception.message}", Toast.LENGTH_SHORT).show()
                binding.btnSimpan.isEnabled = true
                binding.btnSimpan.text = "Simpan Materi"
            }
    }

    private fun clearAllInput() {
        binding.etNamaSubmodul.text?.clear()
        binding.etJudulMateri.text?.clear()
        binding.etTanggalUpload.text?.clear()
        contentItems.clear()
        updateContentPreview()
        hideContentEditor()
    }
}