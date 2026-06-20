package com.example

import android.app.Activity
import android.content.Intent
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

object ImageLoader {
    private val imageJobs = HashMap<View, Job>()

    fun ImageView.loadImage(url: String) {
        // Cancel old concurrent loading jobs targeting this exact view
        imageJobs[this]?.cancel()

        val task = CoroutineScope(Dispatchers.Main).launch {
            try {
                val bitmap = withContext(Dispatchers.IO) {
                    val conn = URL(url).openConnection() as HttpURLConnection
                    conn.connectTimeout = 5000
                    conn.readTimeout = 5000
                    conn.doInput = true
                    conn.connect()
                    
                    val input: InputStream = conn.inputStream
                    android.graphics.BitmapFactory.decodeStream(input)
                }
                if (bitmap != null) {
                    this@loadImage.setImageBitmap(bitmap)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        imageJobs[this] = task
    }

    fun cancelAll() {
        for (job in imageJobs.values) {
            job.cancel()
        }
        imageJobs.clear()
    }
}

fun Activity.setupGlobalTabControls(activeTab: String) {
    // Highlight active nav indicator tab
    updateBottomNavPills(activeTab)

    // Tab clicks delegation logic
    findViewById<View>(R.id.btn_nav_home)?.setOnClickListener {
        if (activeTab != "home") {
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
            startActivity(intent)
        }
    }
    findViewById<View>(R.id.btn_nav_discover)?.setOnClickListener {
        if (activeTab != "discover") {
            val intent = Intent(this, DiscoverActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
            startActivity(intent)
        }
    }
    findViewById<View>(R.id.btn_nav_library)?.setOnClickListener {
        if (activeTab != "library") {
            val intent = Intent(this, LibraryActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
            startActivity(intent)
        }
    }
    findViewById<View>(R.id.btn_nav_profile)?.setOnClickListener {
        if (activeTab != "profile") {
            val intent = Intent(this, ProfileActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
            startActivity(intent)
        }
    }

    // Top App Bar controls
    findViewById<View>(R.id.btn_top_menu)?.setOnClickListener {
        Toast.makeText(this, "Aura Books Sidebar drawer mở nhẹ nhàng", Toast.LENGTH_SHORT).show()
    }
    findViewById<View>(R.id.btn_top_admin)?.setOnClickListener {
        val intent = Intent(this, AdminActivity::class.java)
        startActivity(intent)
    }
    findViewById<View>(R.id.btn_top_search)?.setOnClickListener {
        if (activeTab != "discover") {
            val intent = Intent(this, DiscoverActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
            startActivity(intent)
        }
    }
    findViewById<View>(R.id.btn_top_notifications)?.setOnClickListener {
        Toast.makeText(this, "Hộp thư thông báo: Có 2 chương truyện VIP mới!", Toast.LENGTH_SHORT).show()
    }
}

private fun Activity.updateBottomNavPills(screenId: String) {
    val rootHome = findViewById<View>(R.id.layout_indicator_home) ?: return
    val rootDisc = findViewById<View>(R.id.layout_indicator_discover) ?: return
    val rootLib = findViewById<View>(R.id.layout_indicator_library) ?: return
    val rootProf = findViewById<View>(R.id.layout_indicator_profile) ?: return

    val txtHome = findViewById<TextView>(R.id.txt_home) ?: return
    val txtDisc = findViewById<TextView>(R.id.txt_discover) ?: return
    val txtLib = findViewById<TextView>(R.id.txt_library) ?: return
    val txtProf = findViewById<TextView>(R.id.txt_profile) ?: return

    // Clear backgrounds
    rootHome.setBackgroundResource(0)
    rootDisc.setBackgroundResource(0)
    rootLib.setBackgroundResource(0)
    rootProf.setBackgroundResource(0)

    // Soft gray defaults for labels
    txtHome.setTextColor(getColor(R.color.colorOutline))
    txtDisc.setTextColor(getColor(R.color.colorOutline))
    txtLib.setTextColor(getColor(R.color.colorOutline))
    txtProf.setTextColor(getColor(R.color.colorOutline))

    // Set indicator pill inside selected tab
    when (screenId) {
        "home" -> {
            rootHome.setBackgroundResource(R.drawable.bg_active_nav)
            txtHome.setTextColor(getColor(R.color.colorOnBackground))
        }
        "discover" -> {
            rootDisc.setBackgroundResource(R.drawable.bg_active_nav)
            txtDisc.setTextColor(getColor(R.color.colorOnBackground))
        }
        "library" -> {
            rootLib.setBackgroundResource(R.drawable.bg_active_nav)
            txtLib.setTextColor(getColor(R.color.colorOnBackground))
        }
        "profile" -> {
            rootProf.setBackgroundResource(R.drawable.bg_active_nav)
            txtProf.setTextColor(getColor(R.color.colorOnBackground))
        }
    }
}
