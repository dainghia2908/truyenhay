package com.example

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.example.ImageLoader.loadImage

class GenreActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_genre)

        // Setup top bar back navigation dynamically
        val backBtn = findViewById<TextView>(R.id.btn_top_menu)
        backBtn?.text = "←"
        backBtn?.setTextSize(android.util.TypedValue.COMPLEX_UNIT_SP, 24f)
        backBtn?.setOnClickListener {
            finish()
        }

        // Title setup
        findViewById<TextView>(R.id.app_title)?.text = "Epic Fantasy"

        // Setup other standard top buttons we want to keep
        findViewById<View>(R.id.btn_top_admin)?.setOnClickListener {
            val intent = Intent(this, AdminActivity::class.java)
            startActivity(intent)
        }
        findViewById<View>(R.id.btn_top_search)?.setOnClickListener {
            val intent = Intent(this, DiscoverActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
            startActivity(intent)
        }

        val booksList = BookRepository.booksList
        val bg1 = booksList.find { it.id == "10" } ?: booksList[9]
        val bg2 = booksList.find { it.id == "2" } ?: booksList[1]
        val bg3 = booksList.find { it.id == "11" } ?: booksList[10]
        val bg4 = booksList.find { it.id == "12" } ?: booksList[11]
        val bg5 = booksList.find { it.id == "13" } ?: booksList[12]

        // Load covers
        findViewById<ImageView>(R.id.img_genre_book_1)?.loadImage(bg1.coverUrl)
        findViewById<ImageView>(R.id.img_genre_book_2)?.loadImage(bg2.coverUrl)
        findViewById<ImageView>(R.id.img_genre_book_3)?.loadImage(bg3.coverUrl)
        findViewById<ImageView>(R.id.img_genre_book_4)?.loadImage(bg4.coverUrl)
        findViewById<ImageView>(R.id.img_genre_book_5)?.loadImage(bg5.coverUrl)

        // Book detail redirects
        findViewById<View>(R.id.card_genre_book_1)?.setOnClickListener { openBookDetail(bg1.id) }
        findViewById<View>(R.id.card_genre_book_2)?.setOnClickListener { openBookDetail(bg2.id) }
        findViewById<View>(R.id.card_genre_book_3)?.setOnClickListener { openBookDetail(bg3.id) }
        findViewById<View>(R.id.card_genre_book_4)?.setOnClickListener { openBookDetail(bg4.id) }
        findViewById<View>(R.id.card_genre_book_5)?.setOnClickListener { openBookDetail(bg5.id) }

        // Filter button styles toggles
        val pBtn = findViewById<TextView>(R.id.btn_genre_popular)
        val nBtn = findViewById<TextView>(R.id.btn_genre_newest)
        val cBtn = findViewById<TextView>(R.id.btn_genre_completed)
        val oBtn = findViewById<TextView>(R.id.btn_genre_ongoing)

        fun resetGenreFilterBtns() {
            pBtn?.setBackgroundResource(R.drawable.bg_chip)
            nBtn?.setBackgroundResource(R.drawable.bg_chip)
            cBtn?.setBackgroundResource(R.drawable.bg_chip)
            oBtn?.setBackgroundResource(R.drawable.bg_chip)

            pBtn?.setTextColor(getColor(R.color.colorOnBackground))
            nBtn?.setTextColor(getColor(R.color.colorOnBackground))
            cBtn?.setTextColor(getColor(R.color.colorOnBackground))
            oBtn?.setTextColor(getColor(R.color.colorOnBackground))
        }

        pBtn?.setOnClickListener {
            resetGenreFilterBtns()
            pBtn.setBackgroundResource(R.drawable.bg_pill_accent)
            pBtn.setTextColor(getColor(R.color.colorOnPrimary))
            Toast.makeText(this, "Lọc thể loại: Phổ biến nhất", Toast.LENGTH_SHORT).show()
        }
        nBtn?.setOnClickListener {
            resetGenreFilterBtns()
            nBtn.setBackgroundResource(R.drawable.bg_pill_accent)
            nBtn.setTextColor(getColor(R.color.colorOnPrimary))
            Toast.makeText(this, "Lọc thể loại: Mới nhất", Toast.LENGTH_SHORT).show()
        }
        cBtn?.setOnClickListener {
            resetGenreFilterBtns()
            cBtn.setBackgroundResource(R.drawable.bg_pill_accent)
            cBtn.setTextColor(getColor(R.color.colorOnPrimary))
            Toast.makeText(this, "Lọc thể loại: Đã Hoàn Thành", Toast.LENGTH_SHORT).show()
        }
        oBtn?.setOnClickListener {
            resetGenreFilterBtns()
            oBtn.setBackgroundResource(R.drawable.bg_pill_accent)
            oBtn.setTextColor(getColor(R.color.colorOnPrimary))
            Toast.makeText(this, "Lọc thể loại: Đang Cập Nhật", Toast.LENGTH_SHORT).show()
        }
    }

    private fun openBookDetail(bookId: String) {
        BookRepository.selectedBookId = bookId
        BookRepository.previousScreen = "genre"
        val intent = Intent(this, BookDetailActivity::class.java)
        startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        ImageLoader.cancelAll()
    }
}
