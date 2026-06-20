package com.example

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.example.ImageLoader.loadImage

class LibraryActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_library)

        setupGlobalTabControls("library")
        findViewById<TextView>(R.id.app_title)?.text = "Kệ Sách Của Tôi"

        // Load dynamic covers
        val booksList = BookRepository.booksList
        val bLib1 = booksList.find { it.id == "6" } ?: booksList[5]
        val bLib2 = booksList.find { it.id == "7" } ?: booksList[6]
        val bLib3 = booksList.find { it.id == "8" } ?: booksList[7]
        val bLib4 = booksList.find { it.id == "9" } ?: booksList[8]

        findViewById<ImageView>(R.id.img_lib_book_1)?.loadImage(bLib1.coverUrl)
        findViewById<ImageView>(R.id.img_lib_book_2)?.loadImage(bLib2.coverUrl)
        findViewById<ImageView>(R.id.img_lib_book_3)?.loadImage(bLib3.coverUrl)
        findViewById<ImageView>(R.id.img_lib_book_4)?.loadImage(bLib4.coverUrl)

        findViewById<View>(R.id.card_lib_book_1)?.setOnClickListener { openBookDetail(bLib1.id) }
        findViewById<View>(R.id.card_lib_book_2)?.setOnClickListener { openBookDetail(bLib2.id) }
        findViewById<View>(R.id.card_lib_book_3)?.setOnClickListener {
            if (bLib3.isVip) {
                Toast.makeText(this, "Nội dung VIP! Bạn hãy nâng cấp VIP trong Profile nhé", Toast.LENGTH_SHORT).show()
            }
            openBookDetail(bLib3.id)
        }
        findViewById<View>(R.id.card_lib_book_4)?.setOnClickListener { openBookDetail(bLib4.id) }

        // Toggle subtabs styling on click
        val btnTabFollow = findViewById<TextView>(R.id.btn_lib_tab_following)
        val btnTabHist = findViewById<TextView>(R.id.btn_lib_tab_history)

        btnTabFollow?.setOnClickListener {
            btnTabFollow.setBackgroundResource(R.drawable.bg_card_rounded)
            btnTabFollow.setTextColor(getColor(R.color.colorPrimary))
            btnTabHist?.setBackgroundResource(0)
            btnTabHist?.setTextColor(getColor(R.color.colorOutline))
            Toast.makeText(this, "Đang xem tác phẩm Đang Theo Dõi", Toast.LENGTH_SHORT).show()
        }
        btnTabHist?.setOnClickListener {
            btnTabHist.setBackgroundResource(R.drawable.bg_card_rounded)
            btnTabHist.setTextColor(getColor(R.color.colorPrimary))
            btnTabFollow?.setBackgroundResource(0)
            btnTabFollow?.setTextColor(getColor(R.color.colorOutline))
            Toast.makeText(this, "Đang xem Lịch Sử Đọc của bạn!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun openBookDetail(bookId: String) {
        BookRepository.selectedBookId = bookId
        BookRepository.previousScreen = "library"
        val intent = Intent(this, BookDetailActivity::class.java)
        startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        ImageLoader.cancelAll()
    }
}
