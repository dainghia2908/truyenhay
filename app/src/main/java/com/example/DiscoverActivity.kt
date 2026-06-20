package com.example

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.ComponentActivity
import com.example.ImageLoader.loadImage

class DiscoverActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_discover)

        // Setup common tab and navigation controls
        setupGlobalTabControls("discover")
        
        // Define screen headers titles
        findViewById<TextView>(R.id.app_title)?.text = "Discover Feed"

        // category tags clicks opens Genre Activity
        findViewById<View>(R.id.btn_category_fantasy)?.setOnClickListener { openGenre() }
        findViewById<View>(R.id.btn_category_mystery)?.setOnClickListener { openGenre() }
        findViewById<View>(R.id.btn_category_romance)?.setOnClickListener { openGenre() }
        findViewById<View>(R.id.btn_category_scifi)?.setOnClickListener { openGenre() }
        findViewById<View>(R.id.btn_category_all)?.setOnClickListener { openGenre() }

        // Load visual representations
        findViewById<ImageView>(R.id.img_cat_fantasy)?.loadImage("https://lh3.googleusercontent.com/aida-public/AB6AXuDCbN_JvNiFWg5NEfCmCBADiR5jp4HXFvRWvuChxNc2neRcwhh4coD8UZmOAbd0QDLvU6bGpKejIM1ecZeIkXGPG4y5AEI-yIhe7TTSCXm85CZlbPdpmz5Q9NBMuefIW0hH3TMcGgZLBxuZloqO5o4t4Vh1psxvdmYP_Z9ZmBLra6A3io8mR6U8eKO9HeVm2mn41LE0MGdAb6jT6OHMEFmTnNA_xfxwuUAb-ACcIT1D9mJVY0YCFiOlk-bqprr4a8AL26-MMcaqq0k")
        findViewById<ImageView>(R.id.img_cat_mystery)?.loadImage("https://lh3.googleusercontent.com/aida-public/AB6AXuDcJTSYQsK7jvXXg04XV2hAs5W8PyuOEpeVTSMIYbljH6f9iviuIEDS-geQXGoGyNt7bJveSjfnPCbQhKdv6UbGboDkFzpVkrr6zHgvRL889tVNTGHFHs7injN2peumI6W5cT_OKYRvQfsCRkcC_STPVso_6-yXgflb0RgrY-aGWGf-g_Zo9xbtI04Td-nwKBOhKVDnQn3yvNPS34F3yvjcNYybANQA2AoI7SCgfqLL5hJHDdTAPmp_aMOxxsAbfjB-mV3PBUzVU7Y")
        findViewById<ImageView>(R.id.img_cat_romance)?.loadImage("https://lh3.googleusercontent.com/aida-public/AB6AXuBhDz3IFLS87jWLomNPT-tH1Cw1-TuyppuWnviYjc_zb52yms9QI_UZOiyK-4FSySXj6nVDMQCCZ-Ed3XBV7KR96jbdaSaQ7D0Y4tdR1D86hXJBxCW166ulAsuvrEWT4Si5ZwyJ_ciW8EQtrxvBJvYsrDFothQhKmmGoLI1orpF9VFFUkwbX83wEpUxfbZa-Oa9mBV4dIANPF8rKlSf5QplcFtbBOniMdTrvtojI8o1UWZsKxbYIrVIjpRNKG6D-7NsdLu898qIBB8")
        findViewById<ImageView>(R.id.img_cat_scifi)?.loadImage("https://lh3.googleusercontent.com/aida-public/AB6AXuAFrAza6LXmdGuoRM0eYY3hfIc9XWCDLyHjNn497qKSDos-8TYPBj7Wx4Wy9UsieHpXMfrOXQKl4i2NMHJ62QM4SKoszrd0b66g7RSNr9YMI72BB-_A_3qeeAyPR1rO10vl4mrlqf2SwM2iJ-IIHGotFMcdAMPsJ1OI6qwCHwBtbFH3Jzu6ADsfYtq776HUXSKW4gEdOttazBPUjT4xBakgBddRETBL5aGJkKr18kx-vmEcJJDrCuFaeOjjRBGlx8TYZ9_QLnzNDN8")

        // Hot tag entries redirects
        findViewById<View>(R.id.tag_trending_1)?.setOnClickListener { openBookDetail("1") }
        findViewById<View>(R.id.tag_trending_2)?.setOnClickListener { openBookDetail("4") }
        findViewById<View>(R.id.tag_trending_3)?.setOnClickListener { openGenre() }
        findViewById<View>(R.id.tag_trending_4)?.setOnClickListener { openGenre() }
    }

    private fun openGenre() {
        val intent = Intent(this, GenreActivity::class.java)
        startActivity(intent)
    }

    private fun openBookDetail(bookId: String) {
        BookRepository.selectedBookId = bookId
        BookRepository.previousScreen = "discover"
        val intent = Intent(this, BookDetailActivity::class.java)
        startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        ImageLoader.cancelAll()
    }
}
