package com.example

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.example.ImageLoader.loadImage

class ProfileActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        setupGlobalTabControls("profile")
        findViewById<TextView>(R.id.app_title)?.text = "Trang Cá Nhân"

        // Setup dynamic profile elements and premium flows
        findViewById<ImageView>(R.id.img_profile_avatar)?.loadImage("https://lh3.googleusercontent.com/aida-public/AB6AXuBX2u8kDL863R7YHqG71RHaEQHnsTxUQQjeH-n0blS0-Q4LG5BpO_rowO7TUuUEPlbvDTH1-acopfI1Vssq0s_4zRyqs8jI4VwkRPSfWQeoqHD60lLvi1a6iWQiE32WutC-PPdEy0OsXf3-tQ_RnVxDRtAUzBPkDML1Snkcru6xmg_czbonOJ8AHhKSA8GuMqbqqvud_iFfWIVpTqvXKenOwm5Xm4uaZK9cBxgMJAlB_kRCLNmwh6MzR_KBGWkFdNDAQMqmPZjPvvM")

        findViewById<View>(R.id.btn_profile_vip_upgrade)?.setOnClickListener {
            Toast.makeText(this, "Cảm ơn bạn đã lựa chọn nạp VIP ủng hộ Aura Books!", Toast.LENGTH_LONG).show()
        }
        findViewById<View>(R.id.btn_setting_profile_info)?.setOnClickListener {
            Toast.makeText(this, "Hồ sơ: Thanh Nguyen - Đăng ký: 2026", Toast.LENGTH_SHORT).show()
        }
        findViewById<View>(R.id.btn_setting_password)?.setOnClickListener {
            Toast.makeText(this, "Hệ thống đổi mật khẩu an toàn", Toast.LENGTH_SHORT).show()
        }
        findViewById<View>(R.id.btn_setting_transactions)?.setOnClickListener {
            Toast.makeText(this, "Lịch sử nạp: Có 1 hóa đơn VIP dồi dào!", Toast.LENGTH_SHORT).show()
        }
        findViewById<View>(R.id.btn_logout)?.setOnClickListener {
            Toast.makeText(this, "Đã đăng xuất tài khoản", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
            startActivity(intent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        ImageLoader.cancelAll()
    }
}
