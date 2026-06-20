package com.example

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.example.ImageLoader.loadImage

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Setup bottom tab panel and top app bar redirects
        setupGlobalTabControls("home")

        // Define top title
        findViewById<TextView>(R.id.app_title)?.text = "Aura Books"

        // Setup genre tags navigation
        findViewById<View>(R.id.genre_chip_action)?.setOnClickListener { openGenre() }
        findViewById<View>(R.id.genre_chip_romance)?.setOnClickListener { openGenre() }
        findViewById<View>(R.id.genre_chip_fantasy)?.setOnClickListener { openGenre() }
        findViewById<View>(R.id.genre_chip_scifi)?.setOnClickListener { openGenre() }
        findViewById<View>(R.id.genre_chip_thriller)?.setOnClickListener { openGenre() }
        findViewById<View>(R.id.btn_home_view_all)?.setOnClickListener { openGenre() }

        findViewById<View>(R.id.btn_view_full_chart)?.setOnClickListener {
            val intent = Intent(this, DiscoverActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
            startActivity(intent)
        }

        // Ads settings
        findViewById<ImageView>(R.id.img_banner_ad)?.loadImage("https://lh3.googleusercontent.com/aida-public/AB6AXuAEHeigwbdFGmXWs42OMC87EecSTbdNOXjknrWy2sT-ILbYJFmaVfVhK7kt-6S0Rb57i2ggrPV0Hco5Qwa87vZX0GL2MXGlxqcB-3P-ZVqVFo7_shWazhrZeiAPNFNEQw84bmLTeoZQ1cx23Q1_3m5RlZ6AQpmx1sH9bTagQX41mgdfSIZJBmWz5LpnAlb6zzeJI4iNsv5M_uIXfmRao7mKPNWN5btXI2wVu3j7nenIPKJPEIbWJ141Icf_cQAIpx0GimVKwlO83Pk")
        findViewById<View>(R.id.card_premium_banner)?.setOnClickListener {
            Toast.makeText(this, "Nạp gói VIP dạt dào tiện ích offline", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onResume() {
        super.onResume()
        bindHomeContent()
    }

    private fun bindHomeContent() {
        val booksList = BookRepository.booksList
        if (booksList.isEmpty()) return

        // Featured card
        val featuredBook = booksList[0]
        findViewById<TextView>(R.id.txt_featured_title)?.text = featuredBook.title
        findViewById<ImageView>(R.id.img_featured)?.loadImage(featuredBook.coverUrl)
        findViewById<View>(R.id.card_featured)?.setOnClickListener {
            openBookDetail(featuredBook.id)
        }

        // Dynamic three-row covers setup
        val book1 = booksList.find { it.id == "3" } ?: booksList[2]
        val book2 = booksList.find { it.id == "4" } ?: booksList[3]
        val book3 = booksList.find { it.id == "5" } ?: booksList[4]

        findViewById<TextView>(R.id.lbl_book_1_title)?.text = book1.title
        findViewById<TextView>(R.id.lbl_book_1_meta)?.text = "Ch. ${book1.chaptersCount} • ${book1.dateAdded}"
        findViewById<ImageView>(R.id.img_book_1)?.loadImage(book1.coverUrl)
        findViewById<View>(R.id.card_book_1)?.setOnClickListener { openBookDetail(book1.id) }

        findViewById<TextView>(R.id.lbl_book_2_title)?.text = book2.title
        findViewById<TextView>(R.id.lbl_book_2_meta)?.text = "Ch. ${book2.chaptersCount} • ${book2.dateAdded}"
        findViewById<ImageView>(R.id.img_book_2)?.loadImage(book2.coverUrl)
        findViewById<View>(R.id.card_book_2)?.setOnClickListener { openBookDetail(book2.id) }

        findViewById<TextView>(R.id.lbl_book_3_title)?.text = book3.title
        findViewById<TextView>(R.id.lbl_book_3_meta)?.text = "Ch. ${book3.chaptersCount} • ${book3.dateAdded}"
        findViewById<ImageView>(R.id.img_book_3)?.loadImage(book3.coverUrl)
        findViewById<View>(R.id.card_book_3)?.setOnClickListener { openBookDetail(book3.id) }

        // Top trending rankings
        val bRank1 = booksList.find { it.id == "2" } ?: booksList[1]
        val bRank2 = booksList.find { it.id == "3" } ?: booksList[2]
        val bRank3 = booksList.find { it.id == "5" } ?: booksList[4]

        findViewById<TextView>(R.id.txt_rank_1_title)?.text = bRank1.title
        findViewById<ImageView>(R.id.img_rank_1)?.loadImage(bRank1.coverUrl)
        findViewById<View>(R.id.rank_row_1)?.setOnClickListener { openBookDetail(bRank1.id) }

        findViewById<TextView>(R.id.txt_rank_2_title)?.text = "Coffee & Rain"
        findViewById<ImageView>(R.id.img_rank_2)?.loadImage(bRank2.coverUrl)
        findViewById<View>(R.id.rank_row_2)?.setOnClickListener {
            Toast.makeText(this, "Mở truyện: Coffee & Rain", Toast.LENGTH_SHORT).show()
        }

        findViewById<TextView>(R.id.txt_rank_3_title)?.text = "The Silent Observer"
        findViewById<ImageView>(R.id.img_rank_3)?.loadImage(bRank3.coverUrl)
        findViewById<View>(R.id.rank_row_3)?.setOnClickListener {
            Toast.makeText(this, "Mở truyện: The Silent Observer", Toast.LENGTH_SHORT).show()
        }
    }

    private fun openGenre() {
        val intent = Intent(this, GenreActivity::class.java)
        startActivity(intent)
    }

    private fun openBookDetail(bookId: String) {
        BookRepository.selectedBookId = bookId
        BookRepository.previousScreen = "home"
        val intent = Intent(this, BookDetailActivity::class.java)
        startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        ImageLoader.cancelAll()
    }
}
