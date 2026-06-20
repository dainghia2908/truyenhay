package com.example

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.example.ImageLoader.loadImage

class AdminActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)

        val booksList = BookRepository.booksList

        // Pre-fill cover thumbnails
        findViewById<ImageView>(R.id.img_admin_book_1)?.loadImage(booksList[5].coverUrl)
        findViewById<ImageView>(R.id.img_admin_book_2)?.loadImage(booksList[3].coverUrl)

        // Delete handlers
        findViewById<View>(R.id.btn_admin_delete_1)?.setOnClickListener {
            Toast.makeText(this, "Đã xóa truyện Biên Niên Sử khỏi danh sách", Toast.LENGTH_SHORT).show()
            (it.parent as View).visibility = View.GONE
        }
        findViewById<View>(R.id.btn_admin_delete_2)?.setOnClickListener {
            Toast.makeText(this, "Đã xóa truyện Bóng Đêm thức tỉnh", Toast.LENGTH_SHORT).show()
            (it.parent as View).visibility = View.GONE
        }

        // Form cancellation
        findViewById<View>(R.id.btn_admin_cancel)?.setOnClickListener {
            Toast.makeText(this, "Đã hủy thao tác", Toast.LENGTH_SHORT).show()
            finish()
        }

        // Save handler - appends inputted story details into memory repository list
        findViewById<View>(R.id.btn_admin_save)?.setOnClickListener {
            val editTitle = findViewById<EditText>(R.id.edit_admin_title).text.toString()
            val editAuthor = findViewById<EditText>(R.id.edit_admin_author).text.toString()
            val editTags = findViewById<EditText>(R.id.edit_admin_tags).text.toString()

            if (editTitle.isEmpty() || editAuthor.isEmpty()) {
                Toast.makeText(this, "Mời bạn nhập đầy đủ Tên truyện và Tác giả!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val newBook = BookItem(
                id = (booksList.size + 1).toString(),
                title = editTitle,
                author = editAuthor,
                genre = if (editTags.isNotEmpty()) editTags else "Epic Fantasy",
                chaptersCount = 1,
                rating = 5.0,
                views = "1k",
                coverUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuACZp3PlfIVuzg97iZN2HljKuGlB_QFnTzGswvhub-3WthXIh4HeNu66pNKNKVdnCmnOC99wzJwKOgkcnrqWdsAkd8tqi2ipqQ6L1wLMBa3S-VdIMt-nKRQsDRvjoterPyMfOY-2k4OumEOYS8EyJwEbw3xOf6Yr3TrXBLNpCGz-FYUF-0xqv61T_LIxj_5oA96w8a1rxh0Q2soK4-wL9a2p63Rh0hYP9GfEkxa7BTgAuXl8HufbXAu0jpV9uuX10YMPAKQfBK3llQ",
                progress = 0,
                isVip = false,
                isCompleted = false,
                summary = "Truyện mới dồi dào nội dung kịch tính vừa được viết bởi tác giả $editAuthor",
                dateAdded = "Vừa xong"
            )

            booksList.add(0, newBook)
            Toast.makeText(this, "Đã thêm thành công cuốn sách: $editTitle!", Toast.LENGTH_LONG).show()

            // Navigate back to MainActivity (Home) to refresh list
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
            startActivity(intent)
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        ImageLoader.cancelAll()
    }
}
