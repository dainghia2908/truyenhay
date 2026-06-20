package com.example

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.example.ImageLoader.loadImage

class BookDetailActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_detail)

        val selectedBookId = BookRepository.selectedBookId
        val booksList = BookRepository.booksList
        val book = booksList.find { it.id == selectedBookId } ?: booksList[1] // default Blade/Echoes

        findViewById<TextView>(R.id.txt_detail_title)?.text = book.title
        findViewById<TextView>(R.id.txt_detail_author)?.text = "bởi ${book.author}"
        
        val synopsisTxt = findViewById<TextView>(R.id.txt_detail_synopsis)
        synopsisTxt?.text = book.summary

        // Load visual representations
        findViewById<ImageView>(R.id.img_detail_cover)?.loadImage(book.coverUrl)
        findViewById<ImageView>(R.id.img_detail_banner)?.loadImage("https://lh3.googleusercontent.com/aida-public/AB6AXuA94Gfkx89K8o2YQsCG5HzzxCCjTZsP9duCyPUAbFJdYnqKIxWKxfzLKkJUE9cbgIrhw7dabEXkKrrCpMki2GMXrwFzMPBTPrc9zreW2_UaDtydroyQyTI_M4U-jzqG5OoJNIs80I4MxvKaoeI62dEG6JoSjclJVhdbANYP1er5FYT9zxn8XI54mzk9mrRYECltxs2SYo68FlGTGksRuipgxk1TYFvYZeI9UTgoYVoLY9ygNNBPBwvmQfbPZH6MSf_pu14xOvJsM50")
        findViewById<ImageView>(R.id.img_commenter_avatar)?.loadImage("https://lh3.googleusercontent.com/aida-public/AB6AXuDOxpuQbaIXfBwTl0bUGZtnU2XRcM2nc0O7gxYXKFz1__EHWQmvNizL13Ve2geIAWJ8Gwf6Wfh3KoMrW0vttA6ckhCxJg4nvIpQiSrd2xidwe3mRlH9U1Lw0EqQt4x-TZ-HdaXwlE_1mDZEThQYMcKVNlI31Wy1mrbnQIy4-gd1slPeb3lj_SVVZwaeLca_OnvD_Zk1tcv9a0AXq3x5rSYDQRKNHsMs44SUU_W0VjuL7UQE0B5Leeo1fH_8n_17ktJzC-nFT5JcSFc")

        // Navigation clicks
        findViewById<View>(R.id.btn_detail_back)?.setOnClickListener {
            finish() // returns to previous Activity in stack
        }
        findViewById<View>(R.id.btn_detail_start_read)?.setOnClickListener {
            val intent = Intent(this, ReaderViewActivity::class.java)
            startActivity(intent)
        }
        findViewById<View>(R.id.btn_detail_bookmark)?.setOnClickListener {
            Toast.makeText(this, "Đã thêm vào tủ sách riêng!", Toast.LENGTH_SHORT).show()
        }

        // Expanded/collapsed summaries selector toggling
        val toggleBtn = findViewById<TextView>(R.id.btn_toggle_synopsis)
        var isExpanded = false
        toggleBtn?.setOnClickListener {
            if (isExpanded) {
                synopsisTxt?.maxLines = 3
                toggleBtn.text = "Xem thêm"
            } else {
                synopsisTxt?.maxLines = 100
                toggleBtn.text = "Thu gọn"
            }
            isExpanded = !isExpanded
        }

        findViewById<View>(R.id.btn_write_comment)?.setOnClickListener {
            Toast.makeText(this, "Tính năng viết đánh giá sẽ khả dụng ở phiên bản tiếp theo!", Toast.LENGTH_SHORT).show()
        }
        findViewById<View>(R.id.btn_see_all_comments)?.setOnClickListener {
            Toast.makeText(this, "Đang tải thêm 45 bình luận thành viên...", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        ImageLoader.cancelAll()
    }
}
