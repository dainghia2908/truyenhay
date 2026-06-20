package com.example

import android.graphics.Typeface
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

// Simple self-contained model for application books list
data class BookItem(
    val id: String,
    val title: String,
    val author: String,
    val genre: String,
    val chaptersCount: Int,
    val rating: Double,
    val views: String,
    val coverUrl: String,
    val progress: Int,
    val isVip: Boolean,
    val isCompleted: Boolean,
    val summary: String,
    val dateAdded: String
)

class MainActivity : ComponentActivity() {

    // Unique custom list of in-memory books to demonstrate complete responsiveness
    private lateinit var booksList: MutableList<BookItem>
    
    // Active navigation parameters
    private var currentScreen = "home"
    private var previousScreen = "home"
    private var selectedBookId = "1" // Default selected book: "The Sapphire Crown" or "The Echoes of Eternity"
    
    // Active configuration settings for story reader
    private var readerTheme = "white" // white, sepia, dark
    private var readerFontSize = 17f // default 17sp text size
    private var readerFontFamily = "literata" // literata, inter

    // Async jobs manager for clean garbage collection
    private val imageJobs = HashMap<View, Job>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Initialize dynamic database with the books represented in the visual wireframes
        initBooksDatabase()
        
        // Set root content wrapping all screens
        setContentView(R.layout.activity_main)

        // Setup bottom tab navigation click events
        setupGlobalControls()

        // Inflate the default screen "home"
        switchScreen("home")
    }

    private fun initBooksDatabase() {
        booksList = mutableListOf(
            BookItem(
                id = "1",
                title = "The Sapphire Crown",
                author = "E. R. Thorne",
                genre = "Epic Fantasy",
                chaptersCount = 142,
                rating = 4.9,
                views = "1.5M",
                coverUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuCTWchTks1X3VT91dtg_d9zkns8K-94tj-zUo150viTI3OKl5YnIkN1fXJrFURJFxFq8G3nHNp2TF4AXxqrL3p7NYSg0RX-7popxrsDkQt8fHk-Tsc5lWg1NwkWoBsbn3l-3zHE3pvsoBULsCQsUMQflH6-FbOnXpyVg-VP7wOS51lljDwQSzcn0ppglWwtG90ETeBfF4Pri19NkQRqTn91GEL2BCxaYU94-rI0Htb2qIabpmStnExtlhMxGpFH6ZDZ1Al3Hk8dGZU",
                progress = 0,
                isVip = false,
                isCompleted = false,
                summary = "Trong một thế giới đầy dẫy những thần thoại cổ xưa và bí mật chưa được khai quật, Vương Miện Ngọc Lam chính là chiếc chìa khóa tối cao đang mở ra một kỷ nguyên hỗn loạn mới. Những tàn tích trôi nổi rực sáng ánh hoàng hôn mang theo số phận của các bộ tộc...",
                dateAdded = "2 giờ trước"
            ),
            BookItem(
                id = "2",
                title = "The Echoes of Eternity",
                author = "Eleanor Vance",
                genre = "Epic Fantasy",
                chaptersCount = 124,
                rating = 4.8,
                views = "1.2M",
                coverUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuACW-LsEznWn0VvANRnKekKQg-GoE-nPfpZfyXQH53ZT_5DpWZvS4uhMLhmOJoxP5fF6MKSzJuSLUJJJOh3Rv3L0LKLc238UuO37k2Bc8pX-WT6fsYv9LHaxshWShBSdkEZd6qE_F6ACM2a5h4G9ZEzNSPzUvJtznm8Rt7TjDb6uGyZhDWFyulG1wi1b-llzD9onUDQ_bFkpO7H8ymZQ6ByfTgedsDYtrdwZcBCQe62i0eCX2m21hvymBTarbYNUB1KX2G0x4Xxlck",
                progress = 45,
                isVip = true,
                isCompleted = true,
                summary = "Trong một thế giới nơi lý ức có thể được lưu trữ và giao dịch như tiền tệ, Elara tìm thấy một mảnh ký ức không thuộc về bất kỳ ai. Nó chứa đựng bí mật về một nền văn minh đã mất, nơi công nghệ và ma thuật hòa quyện. Khi cô bắt đầu giải mã những thông điệp ẩn giấu, Elara bị cuốn vào một âm mưu nguy hiểm liên quan đến những gia tộc quyền lực nhất đang cố gắng kiểm soát thị trường ký ức. Cô phải lựa chọn giữa việc bảo vệ bí mật này hay tiết lộ nó cho toàn thế giới.",
                dateAdded = "12/10/2023"
            ),
            BookItem(
                id = "3",
                title = "Whispers in the Dark",
                author = "E. R. Thorne",
                genre = "Romance",
                chaptersCount = 84,
                rating = 4.9,
                views = "780k",
                coverUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuB-tatB2O1e1c4_F-9AVhXn72SA6YANviRSp7_1VcdLGPfmy9O__2XK6uj_8Z0Eox9JRa4X1upYsfU0i71cLbfWI0CfuF18t87Fx9hGm63KVERD96ajZlkf01PcujBgXHJtuC6GhmoJ-zB7QJya3RcxFsV74r7ePXovAXQ0xVA_CcekkFJw7i2Bsu_XU1E5Viw8ITpckTNs-uTYQoU9CknZJggK78AmbUTeG_B21hLPBWuYvA7Jzx811kSIqNzqnpPBp_CaYT_gA0o",
                progress = 50,
                isVip = false,
                isCompleted = false,
                summary = "Một bản tình ca mơ hồ diễn ra giữa màn đêm lung linh sương phủ. Câu chuyện đan xen tinh khôi và ủy mị giữa các tàn tích mờ ảo ngọt ngào.",
                dateAdded = "2 giờ trước"
            ),
            BookItem(
                id = "4",
                title = "Neon Overdrive",
                author = "Dạ Hành",
                genre = "Sci-Fi",
                chaptersCount = 12,
                rating = 4.6,
                views = "450k",
                coverUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuCR1VzQL9VGN62d54HPAnUiieo1EeVDOW6HdfbxPw8jgL95t2MLhpie7TPMzJdaCOcppoiil36zNTo9dpy3kg57cj2XBBah3ZGI2oWT3DDFLyM-3xdGmrc5OBbP2ewNvollVZ1L-ExSZZKT3Tt35EUOHu_jyr14XHEwrhb2eOeHVGupT1gKmKyKYPTOe9wugwcR-uYohoHs1kNQIb-mTT3pl1G9cIwcp62NFUG6MX-nEUJyByXu6YNlu6Zq3tgKga4ld2xxcChMKFA",
                progress = 24,
                isVip = false,
                isCompleted = false,
                summary = "Thế Giới Công Nghệ Tương Lai rực sáng ánh đèn neon xanh lam huyền ảo. Cuộc bứt tốc ngoạn mục phá vỡ hệ thống kiểm soát độc tài...",
                dateAdded = "5 giờ trước"
            ),
            BookItem(
                id = "5",
                title = "The Fog Street Murders",
                author = "M. Rivers",
                genre = "Mystery",
                chaptersCount = 45,
                rating = 4.5,
                views = "890k",
                coverUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuDFyqofeERfQItWdCtAv0M1ul5TF9DMUnEnQ4FFOPubBZ2KnaIuQEcjTj8_DUKe4zCLUH4t6R0vuyO7fhf2bVb49ib7R9MGmUhr8sQNDmUdxK_5vkQXSurujskseq3EUrqKOwJtNAFDqCajAHN8ERl1AzbHZBecsxOzGeEqzgDkwo3yrlJJazHvUDAMqBY1bb-n-uqQbgSxQKV7mWuPTztWAqI-XOIelYvvjop6AY2yfgZ5WX6rVlA72-CFQ69P8hHBzmq2SPFtpEo",
                progress = 100,
                isVip = false,
                isCompleted = true,
                summary = "Sương mù quấn quýt quẩn quanh con phố cổ dập dềnh bóng xe độc mã. Những vụ án bí hiểm mờ ảo và bất ngờ kích thích tối đa trí tò mò...",
                dateAdded = "1 ngày trước"
            ),
            BookItem(
                id = "6",
                title = "Rừng Hổ Phách",
                author = "Huyền Vĩ",
                genre = "Epic Fantasy",
                chaptersCount = 120,
                rating = 4.9,
                views = "1.3M",
                coverUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuA7GIqq_-fxzHzvIXkIYIGS4YjrzY64zj6PArf9t8xDKbew4OLXSuWDUXh70KWC4G67Y_lxMRswlJ9mhmqkQBQrRWX7YZnca1ovqqsyzgsxIXm03809lx5hdwRQHqnD1mVU-rOlHK1SFZz8IS4s6h93wBafSBOcYY6O-NmQFeRFevwB-MCE0yJwR1sUFF2Y4WzA6FIcrmkuEklwNPNt5b2qYwbWkGLBsn1SM3v79HCfMjp-KV1on1BVRHF3ieVjBhCjItYvM8YGKDE",
                progress = 37,
                isVip = true,
                isCompleted = false,
                summary = "Truyền thuyết hổ phách ngàn năm bảo vệ khu rừng thiêng dạt dào sương ấm. Elara bước sải dài mở ra cuộc phiêu lưu bất hủ...",
                dateAdded = "Vừa xong"
            ),
            BookItem(
                id = "7",
                title = "Kiến Trúc Tương Lai",
                author = "D. K. Vance",
                genre = "Tài liệu",
                chaptersCount = 50,
                rating = 4.4,
                views = "120k",
                coverUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuACZp3PlfIVuzg97iZN2HljKuGlB_QFnTzGswvhub-3WthXIh4HeNu66pNKNKVdnCmnOC99wzJwKOgkcnrqWdsAkd8tqi2ipqQ6L1wLMBa3S-VdIMt-nKRQsDRvjoterPyMfOY-2k4OumEOYS8EyJwEbw3xOf6Yr3TrXBLNpCGz-FYUF-0xqv61T_LIxj_5oA96w8a1rxh0Q2soK4-wL9a2p63Rh0hYP9GfEkxa7BTgAuXl8HufbXAu0jpV9uuX10YMPAKQfBK3llQ",
                progress = 24,
                isVip = false,
                isCompleted = false,
                summary = "Hình khối tối giản, kết cấu dồi dào ánh sáng tự nhiên và công nghệ tích hợp vững chắc định hình các siêu đô thị tương lai.",
                dateAdded = "3 ngày trước"
            ),
            BookItem(
                id = "8",
                title = "Dòng Chảy Thời Gian",
                author = "Eleanor Vance",
                genre = "Văn học",
                chaptersCount = 100,
                rating = 4.8,
                views = "600k",
                coverUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuDcLi0iip-YeLXNGgss94gj8-PLzaHQtp_YHTMRMwlI6fBWuDDonXKaMoXJequ6Bx2ITXY6Iyp9NXmJIlaJp-Fmgd35V7AbFAzMpi4jtk-_AjIYNEM-x7vaQjoOx68jQuTzlFkLKgydZ5QOp68dat19OZcyTOKXR0hpIL-m3asFlm12lniSiZx4a6AZb144VfjN73TmxM9Pvjxj6YKsEMwZfWQtfvdIPve_UHysCd3qCYsRUyvNjLs9y9L9B8_LpBFCi2hyfHJ95B8",
                progress = 0,
                isVip = true,
                isCompleted = false,
                summary = "Guồng máy cổ kính của chiếc đồng hồ cổ dường như nuốt chửng từng tíc tắc quý giá. Hành trình hồi tưởng đầy triết lý.",
                dateAdded = "10/11/2023"
            ),
            BookItem(
                id = "9",
                title = "Góc Nhỏ Của Thành Phố",
                author = "Thanh Nguyen",
                genre = "Truyện ngắn",
                chaptersCount = 30,
                rating = 4.7,
                views = "900k",
                coverUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuDekyyo03KgCnH65M6kmClgUrrT47-sPOu-hQgoEE8X9-_JVka4d8-9Hsc2U4F0pZO7PPQBNGR6bmXfjgTOgnHJaNVcA6WpqZjOKSnfPQXfYu7CSy7RM6wJ9SHt-1kQrC_i_LWEwRlf7SgUr1XFFeKfIwrLwSn-T9hiX0t1vsLxI4aCqNfOcHbJ1AED5-OnI3jA7qAFAbl19AjCEgbPJglnuDqZ2S73bhdvZon1Qi_qzc40bO7YXy2m9b3wfsUnyc5q9RaXe3eY3u8",
                progress = 100,
                isVip = false,
                isCompleted = true,
                summary = "Ngồi im lìm bên khung cửa sổ rộng lộng gió nhìn mưa bay trắng phố phường tấp nập. Bản nhạc say lòng người cô quạnh...",
                dateAdded = "4 ngày trước"
            ),
            BookItem(
                id = "10",
                title = "The Dragon's Legacy",
                author = "E. R. Thorne",
                genre = "Epic Fantasy",
                chaptersCount = 120,
                rating = 4.9,
                views = "2.1M",
                coverUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuAp_nOtbEe5YbihAEOuSypGX0pwfLjV41HjT8hCmpHGstHoY_6a5FExogUbkgR4VI5jMgIY_ZWc8P0y-NKA_YUS4cs40WnwCehoG-o1qdqApI7qvUTtvgPofh8-5uxnLGaMWFAdk7YiD4_fWovg3jHKYs0kmOyUKmtbyeDL3NQK_GHM5URiVajlHJcI5aD1c42DdPmmRVZtrB_JRr6fW2E7ylEDpUXYnELvwdFWV12bAFmHDPjfTPQGBwIZR5yjyHiOPpGawKZttkw",
                progress = 0,
                isVip = true,
                isCompleted = false,
                summary = "Hình bóng rồng thiêng sải cánh kiêu hãnh trên tháp cổ nghìn năm dầm sương dãi nắng rực cháy ánh hoàng hôn.",
                dateAdded = "Thứ hai"
            ),
            BookItem(
                id = "11",
                title = "Chronicles of Aethelgard",
                author = "S. L. Croft",
                genre = "Epic Fantasy",
                chaptersCount = 12,
                rating = 5.0,
                views = "400k",
                coverUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuAm9tVcHdodUIPbMoM995uq8fD-zt4uEnLMqCPOeLD14hAQh9QsGnXKyWCyrNV7sHi86Up3Aug8GpAl92nbiUN4vQWJPcQfzCzXeyodB3-E7GAF9zd1ftQO21vYF5r_KIUpXS06tEIgOkRuytzuL4sotreXHzucOStgqcPIQO9Y-JPcde37qBGsHYsoyTZVp_CaSeKR8dUN4a5ofg4o281qqBn7TmSBOYjUFdmG9cP_dpTBhjmUAjc5txfdyxtYyGABPEihopmS5UE",
                progress = 10,
                isVip = false,
                isCompleted = false,
                summary = "Chiến binh tinh linh áo giáp bạc lóng lánh đứng trầm mặc trước thành trì thủy tinh tuyệt tác nghìn năm.",
                dateAdded = "Thứ ba"
            ),
            BookItem(
                id = "12",
                title = "Sea of Stars and Shadows",
                author = "M. Rivers",
                genre = "Epic Fantasy",
                chaptersCount = 210,
                rating = 4.5,
                views = "3M",
                coverUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuDlv7Ktt1vPi6tlSytno8BKEwAVIM_LaKUedYU5XjTxa3zHmW2lwqtb92mxxbKjoVBz9XIe8yNv6r-_wA60SubPXifkRpMSStBGC2QCBkkJWsh8sy1415z4FxTdsW8SIWjmjo2eTUEz9QVnxId8iEOf1S-0eMkWwX8PMRhuNY3vtG-6IjdBDMkUA0akO6v08JKWxoIQ3dI6yr4qhF1WticXyHz5VB4ouQJuTeeTJ9L0RmGslRWvQbqUjf4xHcStCw7NBUXDy9q-Uug",
                progress = 0,
                isVip = false,
                isCompleted = false,
                summary = "Cánh buồm thẫm bồng bềnh phiêu dạt trên mặt biển lóng lánh tinh tú soi bóng siêu tinh vân vũ trụ rực rỡ ảo mộng.",
                dateAdded = "1 năm trước"
            ),
            BookItem(
                id = "13",
                title = "The Archivist's Secret",
                author = "H. G. Wells",
                genre = "Epic Fantasy",
                chaptersCount = 55,
                rating = 4.8,
                views = "850k",
                coverUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuBDcvVaJZuJDBYl8UAHvi1XQJpBWgRVCT0H2CYdn9ttatxK64QsB7v7rhW8NsmBWhHH9pmcu-xqy2ulKEwYSkONe0WtIcHSq_mN2WnTyAjBQis4LinYtlJGJ-5EZ7sVYtDmfV44cwqQbnqi1KVBp7sOopbXkm2CDS5VrFix_0ftzrGBi1dByGoyff8TTmL97hNEnDkhGRflZ0TEj7cHy1dcmnEJTSfZ_HKB5HsVr8QyQi5zYwZO_ppnaE1tW2itcmElFoehmrae6s0",
                progress = 0,
                isVip = true,
                isCompleted = false,
                summary = "Thư viện huyền bí rộng thênh thang dạt dào sương lạnh lơ lửng những thiên thể phát quang dẫn lối.",
                dateAdded = "Thứ sáu"
            )
        )
    }

    private fun setupGlobalControls() {
        // Tab clicks delegation logic
        findViewById<View>(R.id.btn_nav_home).setOnClickListener { switchScreen("home") }
        findViewById<View>(R.id.btn_nav_discover).setOnClickListener { switchScreen("discover") }
        findViewById<View>(R.id.btn_nav_library).setOnClickListener { switchScreen("library") }
        findViewById<View>(R.id.btn_nav_profile).setOnClickListener { switchScreen("profile") }

        // Top App Bar controls
        findViewById<View>(R.id.btn_top_menu).setOnClickListener {
            Toast.makeText(this, "Aura Books Sidebar drawer mở nhẹ nhàng", Toast.LENGTH_SHORT).show()
        }
        findViewById<View>(R.id.btn_top_admin).setOnClickListener {
            switchScreen("admin")
        }
        findViewById<View>(R.id.btn_top_search).setOnClickListener {
            switchScreen("discover")
        }
        findViewById<View>(R.id.btn_top_notifications).setOnClickListener {
            Toast.makeText(this, "Hộp thư thông báo: Có 2 chương truyện VIP mới!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun switchScreen(screenId: String) {
        previousScreen = currentScreen
        currentScreen = screenId

        // Handle structural layout state swaps
        val topAppBar = findViewById<View>(R.id.top_app_bar)
        val bottomNavBar = findViewById<View>(R.id.bottom_navigation_bar)
        
        // Hide standard bars inside Reader Screen to match immersive full-screen expectation
        if (screenId == "reader_view") {
            topAppBar.visibility = View.GONE
            bottomNavBar.visibility = View.GONE
        } else {
            topAppBar.visibility = View.VISIBLE
            bottomNavBar.visibility = View.VISIBLE
        }

        // Show different Page labels
        val appTitleText = findViewById<TextView>(R.id.app_title)
        when (screenId) {
            "home" -> appTitleText.text = "Aura Books"
            "discover" -> appTitleText.text = "Discover Feed"
            "library" -> appTitleText.text = "Kệ Sách Của Tôi"
            "profile" -> appTitleText.text = "Trang Cá Nhân"
            "admin" -> appTitleText.text = "Hệ thống Quản Trị"
            "genre" -> appTitleText.text = "Epic Fantasy"
            "book_detail" -> appTitleText.text = "Chi Tiết Truyện"
        }

        // Highlight selected nav indicator tab
        updateBottomNavPills(screenId)

        // Inject dynamic layout content view inside frame container
        val container = findViewById<ViewGroup>(R.id.screen_container)
        container.removeAllViews()

        val layoutRes = when (screenId) {
            "home" -> R.layout.screen_home
            "discover" -> R.layout.screen_discover
            "library" -> R.layout.screen_library
            "profile" -> R.layout.screen_profile
            "book_detail" -> R.layout.screen_book_detail
            "reader_view" -> R.layout.screen_reader_view
            "admin" -> R.layout.screen_admin
            "genre" -> R.layout.screen_genre
            else -> R.layout.screen_home
        }

        val inflatedView = layoutInflater.inflate(layoutRes, container, false)
        container.addView(inflatedView)

        // Set up interactions on active layout elements
        bindScreenWidgetsActions(screenId, inflatedView)
    }

    private fun updateBottomNavPills(screenId: String) {
        val rootHome = findViewById<View>(R.id.layout_indicator_home)
        val rootDisc = findViewById<View>(R.id.layout_indicator_discover)
        val rootLib = findViewById<View>(R.id.layout_indicator_library)
        val rootProf = findViewById<View>(R.id.layout_indicator_profile)

        val txtHome = findViewById<TextView>(R.id.txt_home)
        val txtDisc = findViewById<TextView>(R.id.txt_discover)
        val txtLib = findViewById<TextView>(R.id.txt_library)
        val txtProf = findViewById<TextView>(R.id.txt_profile)

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

    private fun bindScreenWidgetsActions(screenId: String, view: View) {
        when (screenId) {
            "home" -> {
                // Featured Sapphire Crown
                view.findViewById<View>(R.id.card_featured).setOnClickListener {
                    selectedBookId = "1"
                    switchScreen("book_detail")
                }
                view.findViewById<ImageView>(R.id.img_featured).loadImage(booksList[0].coverUrl)

                // Tags chips click opens Epic Fantasy Category Genre Screen
                view.findViewById<View>(R.id.genre_chip_action).setOnClickListener { switchScreen("genre") }
                view.findViewById<View>(R.id.genre_chip_romance).setOnClickListener { switchScreen("genre") }
                view.findViewById<View>(R.id.genre_chip_fantasy).setOnClickListener { switchScreen("genre") }
                view.findViewById<View>(R.id.genre_chip_scifi).setOnClickListener { switchScreen("genre") }
                view.findViewById<View>(R.id.genre_chip_thriller).setOnClickListener { switchScreen("genre") }
                view.findViewById<View>(R.id.btn_home_view_all).setOnClickListener { switchScreen("genre") }

                // Dynamic covers setup
                val book1 = booksList.find { it.id == "3" } ?: booksList[2] // Whispers in the Dark
                val book2 = booksList.find { it.id == "4" } ?: booksList[3] // Neon Overdrive
                val book3 = booksList.find { it.id == "5" } ?: booksList[4] // The Fog Street Murders
                
                view.findViewById<TextView>(R.id.lbl_book_1_title).text = book1.title
                view.findViewById<TextView>(R.id.lbl_book_1_meta).text = "Ch. ${book1.chaptersCount} • ${book1.dateAdded}"
                view.findViewById<ImageView>(R.id.img_book_1).loadImage(book1.coverUrl)
                view.findViewById<View>(R.id.card_book_1).setOnClickListener {
                    selectedBookId = book1.id
                    switchScreen("book_detail")
                }

                view.findViewById<TextView>(R.id.lbl_book_2_title).text = book2.title
                view.findViewById<TextView>(R.id.lbl_book_2_meta).text = "Ch. ${book2.chaptersCount} • ${book2.dateAdded}"
                view.findViewById<ImageView>(R.id.img_book_2).loadImage(book2.coverUrl)
                view.findViewById<View>(R.id.card_book_2).setOnClickListener {
                    selectedBookId = book2.id
                    switchScreen("book_detail")
                }

                view.findViewById<TextView>(R.id.lbl_book_3_title).text = book3.title
                view.findViewById<TextView>(R.id.lbl_book_3_meta).text = "Ch. ${book3.chaptersCount} • ${book3.dateAdded}"
                view.findViewById<ImageView>(R.id.img_book_3).loadImage(book3.coverUrl)
                view.findViewById<View>(R.id.card_book_3).setOnClickListener {
                    selectedBookId = book3.id
                    switchScreen("book_detail")
                }

                // Top trending rows loads
                val bRank1 = booksList.find { it.id == "2" } ?: booksList[1] // Blade of the Ancients
                val bRank2 = booksList.find { it.id == "3" } ?: booksList[2] // Coffee / Whispers
                val bRank3 = booksList.find { it.id == "5" } ?: booksList[4] // The Silent Observer / Fog
                
                view.findViewById<TextView>(R.id.txt_rank_1_title).text = bRank1.title
                view.findViewById<ImageView>(R.id.img_rank_1).loadImage(bRank1.coverUrl)
                view.findViewById<View>(R.id.rank_row_1).setOnClickListener {
                    selectedBookId = bRank1.id
                    switchScreen("book_detail")
                }

                view.findViewById<TextView>(R.id.txt_rank_2_title).text = "Coffee & Rain" // exact title
                view.findViewById<ImageView>(R.id.img_rank_2).loadImage(bRank2.coverUrl)
                view.findViewById<View>(R.id.rank_row_2).setOnClickListener {
                    Toast.makeText(this, "Mở truyện: Coffee & Rain", Toast.LENGTH_SHORT).show()
                }

                view.findViewById<TextView>(R.id.txt_rank_3_title).text = "The Silent Observer"
                view.findViewById<ImageView>(R.id.img_rank_3).loadImage(bRank3.coverUrl)
                view.findViewById<View>(R.id.rank_row_3).setOnClickListener {
                    Toast.makeText(this, "Mở truyện: The Silent Observer", Toast.LENGTH_SHORT).show()
                }

                view.findViewById<View>(R.id.btn_view_full_chart).setOnClickListener {
                    switchScreen("discover")
                }

                // Ad banner
                view.findViewById<ImageView>(R.id.img_banner_ad).loadImage("https://lh3.googleusercontent.com/aida-public/AB6AXuAEHeigwbdFGmXWs42OMC87EecSTbdNOXjknrWy2sT-ILbYJFmaVfVhK7kt-6S0Rb57i2ggrPV0Hco5Qwa87vZX0GL2MXGlxqcB-3P-ZVqVFo7_shWazhrZeiAPNFNEQw84bmLTeoZQ1cx23Q1_3m5RlZ6AQpmx1sH9bTagQX41mgdfSIZJBmWz5LpnAlb6zzeJI4iNsv5M_uIXfmRao7mKPNWN5btXI2wVu3j7nenIPKJPEIbWJ141Icf_cQAIpx0GimVKwlO83Pk")
                view.findViewById<View>(R.id.card_premium_banner).setOnClickListener {
                    Toast.makeText(this, "Nạp gói VIP dạt dào tiện ích offline", Toast.LENGTH_SHORT).show()
                }
            }
            "discover" -> {
                // bento cards clicks
                view.findViewById<View>(R.id.btn_category_fantasy).setOnClickListener { switchScreen("genre") }
                view.findViewById<View>(R.id.btn_category_mystery).setOnClickListener { switchScreen("genre") }
                view.findViewById<View>(R.id.btn_category_romance).setOnClickListener { switchScreen("genre") }
                view.findViewById<View>(R.id.btn_category_scifi).setOnClickListener { switchScreen("genre") }
                view.findViewById<View>(R.id.btn_category_all).setOnClickListener { switchScreen("genre") }

                view.findViewById<ImageView>(R.id.img_cat_fantasy).loadImage("https://lh3.googleusercontent.com/aida-public/AB6AXuDCbN_JvNiFWg5NEfCmCBADiR5jp4HXFvRWvuChxNc2neRcwhh4coD8UZmOAbd0QDLvU6bGpKejIM1ecZeIkXGPG4y5AEI-yIhe7TTSCXm85CZlbPdpmz5Q9NBMuefIW0hH3TMcGgZLBxuZloqO5o4t4Vh1psxvdmYP_Z9ZmBLra6A3io8mR6U8eKO9HeVm2mn41LE0MGdAb6jT6OHMEFmTnNA_xfxwuUAb-ACcIT1D9mJVY0YCFiOlk-bqprr4a8AL26-MMcaqq0k")
                view.findViewById<ImageView>(R.id.img_cat_mystery).loadImage("https://lh3.googleusercontent.com/aida-public/AB6AXuDcJTSYQsK7jvXXg04XV2hAs5W8PyuOEpeVTSMIYbljH6f9iviuIEDS-geQXGoGyNt7bJveSjfnPCbQhKdv6UbGboDkFzpVkrr6zHgvRL889tVNTGHFHs7injN2peumI6W5cT_OKYRvQfsCRkcC_STPVso_6-yXgflb0RgrY-aGWGf-g_Zo9xbtI04Td-nwKBOhKVDnQn3yvNPS34F3yvjcNYybANQA2AoI7SCgfqLL5hJHDdTAPmp_aMOxxsAbfjB-mV3PBUzVU7Y")
                view.findViewById<ImageView>(R.id.img_cat_romance).loadImage("https://lh3.googleusercontent.com/aida-public/AB6AXuBhDz3IFLS87jWLomNPT-tH1Cw1-TuyppuWnviYjc_zb52yms9QI_UZOiyK-4FSySXj6nVDMQCCZ-Ed3XBV7KR96jbdaSaQ7D0Y4tdR1D86hXJBxCW166ulAsuvrEWT4Si5ZwyJ_ciW8EQtrxvBJvYsrDFothQhKmmGoLI1orpF9VFFUkwbX83wEpUxfbZa-Oa9mBV4dIANPF8rKlSf5QplcFtbBOniMdTrvtojI8o1UWZsKxbYIrVIjpRNKG6D-7NsdLu898qIBB8")
                view.findViewById<ImageView>(R.id.img_cat_scifi).loadImage("https://lh3.googleusercontent.com/aida-public/AB6AXuAFrAza6LXmdGuoRM0eYY3hfIc9XWCDLyHjNn497qKSDos-8TYPBj7Wx4Wy9UsieHpXMfrOXQKl4i2NMHJ62QM4SKoszrd0b66g7RSNr9YMI72BB-_A_3qeeAyPR1rO10vl4mrlqf2SwM2iJ-IIHGotFMcdAMPsJ1OI6qwCHwBtbFH3Jzu6ADsfYtq776HUXSKW4gEdOttazBPUjT4xBakgBddRETBL5aGJkKr18kx-vmEcJJDrCuFaeOjjRBGlx8TYZ9_QLnzNDN8")

                // tag search queries redirects
                view.findViewById<View>(R.id.tag_trending_1).setOnClickListener {
                    selectedBookId = "1"
                    switchScreen("book_detail")
                }
                view.findViewById<View>(R.id.tag_trending_2).setOnClickListener {
                    selectedBookId = "4"
                    switchScreen("book_detail")
                }
                view.findViewById<View>(R.id.tag_trending_3).setOnClickListener { switchScreen("genre") }
                view.findViewById<View>(R.id.tag_trending_4).setOnClickListener { switchScreen("genre") }
            }
            "library" -> {
                // setup covers
                val bLib1 = booksList.find { it.id == "6" } ?: booksList[5] // Rừng Hổ Phách
                val bLib2 = booksList.find { it.id == "7" } ?: booksList[6] // Kiến trúc tương lai
                val bLib3 = booksList.find { it.id == "8" } ?: booksList[7] // Dòng chảy thời gian
                val bLib4 = booksList.find { it.id == "9" } ?: booksList[8] // Góc Nhỏ Của Thành Phố
                
                view.findViewById<ImageView>(R.id.img_lib_book_1).loadImage(bLib1.coverUrl)
                view.findViewById<ImageView>(R.id.img_lib_book_2).loadImage(bLib2.coverUrl)
                view.findViewById<ImageView>(R.id.img_lib_book_3).loadImage(bLib3.coverUrl)
                view.findViewById<ImageView>(R.id.img_lib_book_4).loadImage(bLib4.coverUrl)

                view.findViewById<View>(R.id.card_lib_book_1).setOnClickListener {
                    selectedBookId = bLib1.id
                    switchScreen("book_detail")
                }
                view.findViewById<View>(R.id.card_lib_book_2).setOnClickListener {
                    selectedBookId = bLib2.id
                    switchScreen("book_detail")
                }
                view.findViewById<View>(R.id.card_lib_book_3).setOnClickListener {
                    if (bLib3.isVip) {
                        Toast.makeText(this, "Nội dung VIP! Bạn hãy nâng cấp VIP trong Profile nhé", Toast.LENGTH_SHORT).show()
                    }
                    selectedBookId = bLib3.id
                    switchScreen("book_detail")
                }
                view.findViewById<View>(R.id.card_lib_book_4).setOnClickListener {
                    selectedBookId = bLib4.id
                    switchScreen("book_detail")
                }

                // Toggle tabs styling
                val btnTabFollow = view.findViewById<TextView>(R.id.btn_lib_tab_following)
                val btnTabHist = view.findViewById<TextView>(R.id.btn_lib_tab_history)

                btnTabFollow.setOnClickListener {
                    btnTabFollow.setBackgroundResource(R.drawable.bg_card_rounded)
                    btnTabFollow.setTextColor(getColor(R.color.colorPrimary))
                    btnTabHist.setBackgroundResource(0)
                    btnTabHist.setTextColor(getColor(R.color.colorOutline))
                    Toast.makeText(this, "Đang xem tác phẩm Đang Theo Dõi", Toast.LENGTH_SHORT).show()
                }
                btnTabHist.setOnClickListener {
                    btnTabHist.setBackgroundResource(R.drawable.bg_card_rounded)
                    btnTabHist.setTextColor(getColor(R.color.colorPrimary))
                    btnTabFollow.setBackgroundResource(0)
                    btnTabFollow.setTextColor(getColor(R.color.colorOutline))
                    Toast.makeText(this, "Đang xem Lịch Sử Đọc của bạn!", Toast.LENGTH_SHORT).show()
                }
            }
            "profile" -> {
                view.findViewById<ImageView>(R.id.img_profile_avatar).loadImage("https://lh3.googleusercontent.com/aida-public/AB6AXuBX2u8kDL863R7YHqG71RHaEQHnsTxUQQjeH-n0blS0-Q4LG5BpO_rowO7TUuUEPlbvDTH1-acopfI1Vssq0s_4zRyqs8jI4VwkRPSfWQeoqHD60lLvi1a6iWQiE32WutC-PPdEy0OsXf3-tQ_RnVxDRtAUzBPkDML1Snkcru6xmg_czbonOJ8AHhKSA8GuMqbqqvud_iFfWIVpTqvXKenOwm5Xm4uaZK9cBxgMJAlB_kRCLNmwh6MzR_KBGWkFdNDAQMqmPZjPvvM")
                
                view.findViewById<View>(R.id.btn_profile_vip_upgrade).setOnClickListener {
                    Toast.makeText(this, "Cảm ơn bạn đã lựa chọn nạp VIP ủng hộ Aura Books!", Toast.LENGTH_LONG).show()
                }
                view.findViewById<View>(R.id.btn_setting_profile_info).setOnClickListener {
                    Toast.makeText(this, "Hồ sơ: Thanh Nguyen - Đăng ký: 2026", Toast.LENGTH_SHORT).show()
                }
                view.findViewById<View>(R.id.btn_setting_password).setOnClickListener {
                    Toast.makeText(this, "Hệ thống đổi mật khẩu an toàn", Toast.LENGTH_SHORT).show()
                }
                view.findViewById<View>(R.id.btn_setting_transactions).setOnClickListener {
                    Toast.makeText(this, "Lịch sử nạp: Có 1 hóa đơn VIP dồi dào!", Toast.LENGTH_SHORT).show()
                }
                view.findViewById<View>(R.id.btn_logout).setOnClickListener {
                    Toast.makeText(this, "Đã đăng xuất tài khoản", Toast.LENGTH_SHORT).show()
                    switchScreen("home")
                }
            }
            "book_detail" -> {
                val book = booksList.find { it.id == selectedBookId } ?: booksList[1] // default Blade/Echoes
                
                view.findViewById<TextView>(R.id.txt_detail_title).text = book.title
                view.findViewById<TextView>(R.id.txt_detail_author).text = "bởi ${book.author}"
                view.findViewById<TextView>(R.id.txt_detail_synopsis).text = book.summary

                // load images
                view.findViewById<ImageView>(R.id.img_detail_cover).loadImage(book.coverUrl)
                view.findViewById<ImageView>(R.id.img_detail_banner).loadImage("https://lh3.googleusercontent.com/aida-public/AB6AXuA94Gfkx89K8o2YQsCG5HzzxCCjTZsP9duCyPUAbFJdYnqKIxWKxfzLKkJUE9cbgIrhw7dabEXkKrrCpMki2GMXrwFzMPBTPrc9zreW2_UaDtydroyQyTI_M4U-jzqG5OoJNIs80I4MxvKaoeI62dEG6JoSjclJVhdbANYP1er5FYT9zxn8XI54mzk9mrRYECltxs2SYo68FlGTGksRuipgxk1TYFvYZeI9UTgoYVoLY9ygNNBPBwvmQfbPZH6MSf_pu14xOvJsM50")
                view.findViewById<ImageView>(R.id.img_commenter_avatar).loadImage("https://lh3.googleusercontent.com/aida-public/AB6AXuDOxpuQbaIXfBwTl0bUGZtnU2XRcM2nc0O7gxYXKFz1__EHWQmvNizL13Ve2geIAWJ8Gwf6Wfh3KoMrW0vttA6ckhCxJg4nvIpQiSrd2xidwe3mRlH9U1Lw0EqQt4x-TZ-HdaXwlE_1mDZEThQYMcKVNlI31Wy1mrbnQIy4-gd1slPeb3lj_SVVZwaeLca_OnvD_Zk1tcv9a0AXq3x5rSYDQRKNHsMs44SUU_W0VjuL7UQE0B5Leeo1fH_8n_17ktJzC-nFT5JcSFc")

                // navigation clicks
                view.findViewById<View>(R.id.btn_detail_back).setOnClickListener {
                    switchScreen(previousScreen)
                }
                view.findViewById<View>(R.id.btn_detail_start_read).setOnClickListener {
                    switchScreen("reader_view")
                }
                view.findViewById<View>(R.id.btn_detail_bookmark).setOnClickListener {
                    Toast.makeText(this, "Đã thêm vào tủ sách riêng!", Toast.LENGTH_SHORT).show()
                }

                // toggle synopsis maxline logic
                val synopsisTxt = view.findViewById<TextView>(R.id.txt_detail_synopsis)
                val toggleBtn = view.findViewById<TextView>(R.id.btn_toggle_synopsis)
                var isExpanded = false
                toggleBtn.setOnClickListener {
                    if (isExpanded) {
                        synopsisTxt.maxLines = 3
                        toggleBtn.text = "Xem thêm"
                    } else {
                        synopsisTxt.maxLines = 100
                        toggleBtn.text = "Thu gọn"
                    }
                    isExpanded = !isExpanded
                }

                // Reviews interaction
                view.findViewById<View>(R.id.btn_write_comment).setOnClickListener {
                    Toast.makeText(this, "Tính năng viết đánh giá sẽ khả dụng ở phiên bản tiếp theo!", Toast.LENGTH_SHORT).show()
                }
                view.findViewById<View>(R.id.btn_see_all_comments).setOnClickListener {
                    Toast.makeText(this, "Đang tải thêm 45 bình luận thành viên...", Toast.LENGTH_SHORT).show()
                }
            }
            "reader_view" -> {
                // Header back navigation
                view.findViewById<View>(R.id.btn_reader_back).setOnClickListener {
                    switchScreen("book_detail")
                }

                // expandable bottom sheet settings controls
                val settingsSheet = view.findViewById<View>(R.id.card_reader_settings_sheet)
                val darkenOverlay = view.findViewById<View>(R.id.view_reader_darken_overlay)
                val btnTrigger = view.findViewById<View>(R.id.btn_reader_settings)

                // open sheet
                btnTrigger.setOnClickListener {
                    settingsSheet.visibility = View.VISIBLE
                    darkenOverlay.visibility = View.VISIBLE
                }

                // close sheet clicking darken overlay
                darkenOverlay.setOnClickListener {
                    settingsSheet.visibility = View.GONE
                    darkenOverlay.visibility = View.GONE
                }

                // Typography font size adjust slider dynamically
                val articleContainer = view.findViewById<ViewGroup>(R.id.layout_reader_article)
                val fontSizeSlider = view.findViewById<SeekBar>(R.id.slider_reader_font_size)
                
                // set current font progress
                fontSizeSlider.progress = (readerFontSize - 10).toInt()
                fontSizeSlider.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                        readerFontSize = 10f + progress
                        updateReaderTypography(articleContainer)
                    }
                    override fun onStartTrackingTouch(seekBar: SeekBar?) {}
                    override fun onStopTrackingTouch(seekBar: SeekBar?) {}
                })

                // Font Family swaps toggle
                val btnLiterata = view.findViewById<TextView>(R.id.btn_reader_font_literata)
                val btnInter = view.findViewById<TextView>(R.id.btn_reader_font_inter)

                btnLiterata.setOnClickListener {
                    readerFontFamily = "literata"
                    btnLiterata.setBackgroundResource(R.drawable.bg_card_rounded)
                    btnLiterata.setTextColor(getColor(R.color.colorPrimary))
                    btnInter.setBackgroundResource(R.drawable.bg_chip)
                    btnInter.setTextColor(getColor(R.color.colorOutline))
                    updateReaderTypography(articleContainer)
                }

                btnInter.setOnClickListener {
                    readerFontFamily = "inter"
                    btnInter.setBackgroundResource(R.drawable.bg_card_rounded)
                    btnInter.setTextColor(getColor(R.color.colorPrimary))
                    btnLiterata.setBackgroundResource(R.drawable.bg_chip)
                    btnLiterata.setTextColor(getColor(R.color.colorOutline))
                    updateReaderTypography(articleContainer)
                }

                // Background Theme switcher logic
                val btnWhite = view.findViewById<View>(R.id.btn_theme_white)
                val btnSepia = view.findViewById<View>(R.id.btn_theme_sepia)
                val btnDark = view.findViewById<View>(R.id.btn_theme_dark)

                val chkWhite = view.findViewById<TextView>(R.id.lbl_theme_white_checked)
                val chkSepia = view.findViewById<TextView>(R.id.lbl_theme_sepia_checked)
                val chkDark = view.findViewById<TextView>(R.id.lbl_theme_dark_checked)

                fun refreshCheckmarks() {
                    chkWhite.text = if (readerTheme == "white") "✔" else ""
                    chkSepia.text = if (readerTheme == "sepia") "✔" else ""
                    chkDark.text = if (readerTheme == "dark") "✔" else ""
                }

                val mainRootCanvas = view.findViewById<View>(R.id.scroll_reader_canvas).parent as View

                btnWhite.setOnClickListener {
                    readerTheme = "white"
                    refreshCheckmarks()
                    applyThemeColorsToReader(mainRootCanvas)
                }
                btnSepia.setOnClickListener {
                    readerTheme = "sepia"
                    refreshCheckmarks()
                    applyThemeColorsToReader(mainRootCanvas)
                }
                btnDark.setOnClickListener {
                    readerTheme = "dark"
                    refreshCheckmarks()
                    applyThemeColorsToReader(mainRootCanvas)
                }

                // Initial states run
                refreshCheckmarks()
                applyThemeColorsToReader(mainRootCanvas)

                // offline premium download trigger inside settings sheet
                view.findViewById<View>(R.id.btn_reader_offline_download).setOnClickListener {
                    Toast.makeText(this, "Đã lưu trữ ngoại tuyến thành công chương VIP này!", Toast.LENGTH_SHORT).show()
                }

                // Prev/Next indicators triggers
                view.findViewById<View>(R.id.btn_reader_prev).setOnClickListener {
                    Toast.makeText(this, "Bạn đã ở chương đầu tiên", Toast.LENGTH_SHORT).show()
                }
                view.findViewById<View>(R.id.btn_reader_next).setOnClickListener {
                    Toast.makeText(this, "Tính năng đọc chương kế cần VIP! Hãy nạp VIP nhé", Toast.LENGTH_SHORT).show()
                }
            }
            "admin" -> {
                // Pre-fill image cover miniatures
                view.findViewById<ImageView>(R.id.img_admin_book_1).loadImage(booksList[5].coverUrl) // Rừng hổ phách
                view.findViewById<ImageView>(R.id.img_admin_book_2).loadImage(booksList[3].coverUrl) // Neon

                // Delete handlers from database list
                view.findViewById<View>(R.id.btn_admin_delete_1).setOnClickListener {
                    Toast.makeText(this, "Đã xóa truyện Biên Niên Sử khỏi danh sách", Toast.LENGTH_SHORT).show()
                    (view.findViewById<View>(R.id.btn_admin_delete_1).parent as View).visibility = View.GONE
                }
                view.findViewById<View>(R.id.btn_admin_delete_2).setOnClickListener {
                    Toast.makeText(this, "Đã xóa truyện Bóng Đêm thức tỉnh", Toast.LENGTH_SHORT).show()
                    (view.findViewById<View>(R.id.btn_admin_delete_2).parent as View).visibility = View.GONE
                }

                // Form cancellation
                view.findViewById<View>(R.id.btn_admin_cancel).setOnClickListener {
                    Toast.makeText(this, "Đã hủy thao tác", Toast.LENGTH_SHORT).show()
                    switchScreen("home")
                }

                // Save button handler: dynamically adds our inputted cover elements into our memory data arrays
                view.findViewById<View>(R.id.btn_admin_save).setOnClickListener {
                    val editTitle = view.findViewById<EditText>(R.id.edit_admin_title).text.toString()
                    val editAuthor = view.findViewById<EditText>(R.id.edit_admin_author).text.toString()
                    val editTags = view.findViewById<EditText>(R.id.edit_admin_tags).text.toString()

                    if (editTitle.isEmpty() || editAuthor.isEmpty()) {
                        Toast.makeText(this, "Mời bạn nhập đầy đủ Tên truyện và Tác giả!", Toast.LENGTH_SHORT).show()
                        return@setOnClickListener
                    }

                    // add story dynamically
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
                    switchScreen("home") // return to home screen detailing our freshly prepended cover card!
                }
            }
            "genre" -> {
                // setup covers on grid
                val bg1 = booksList.find { it.id == "10" } ?: booksList[9] // Dragon's Legacy
                val bg2 = booksList.find { it.id == "2" } ?: booksList[1] // Blade of the Ancients
                val bg3 = booksList.find { it.id == "11" } ?: booksList[10] // Chronicles of Aethelgard
                val bg4 = booksList.find { it.id == "12" } ?: booksList[11] // Sea of stars
                val bg5 = booksList.find { it.id == "13" } ?: booksList[12] // Archivist

                view.findViewById<ImageView>(R.id.img_genre_book_1).loadImage(bg1.coverUrl)
                view.findViewById<ImageView>(R.id.img_genre_book_2).loadImage(bg2.coverUrl)
                view.findViewById<ImageView>(R.id.img_genre_book_3).loadImage(bg3.coverUrl)
                view.findViewById<ImageView>(R.id.img_genre_book_4).loadImage(bg4.coverUrl)
                view.findViewById<ImageView>(R.id.img_genre_book_5).loadImage(bg5.coverUrl)

                view.findViewById<View>(R.id.card_genre_book_1).setOnClickListener {
                    selectedBookId = bg1.id
                    switchScreen("book_detail")
                }
                view.findViewById<View>(R.id.card_genre_book_2).setOnClickListener {
                    selectedBookId = bg2.id
                    switchScreen("book_detail")
                }
                view.findViewById<View>(R.id.card_genre_book_3).setOnClickListener {
                    selectedBookId = bg3.id
                    switchScreen("book_detail")
                }
                view.findViewById<View>(R.id.card_genre_book_4).setOnClickListener {
                    selectedBookId = bg4.id
                    switchScreen("book_detail")
                }
                view.findViewById<View>(R.id.card_genre_book_5).setOnClickListener {
                    selectedBookId = bg5.id
                    switchScreen("book_detail")
                }

                // filter tabs triggers styling toggling
                val pBtn = view.findViewById<TextView>(R.id.btn_genre_popular)
                val nBtn = view.findViewById<TextView>(R.id.btn_genre_newest)
                val cBtn = view.findViewById<TextView>(R.id.btn_genre_completed)
                val oBtn = view.findViewById<TextView>(R.id.btn_genre_ongoing)

                fun resetGenreFilterBtns() {
                    pBtn.setBackgroundResource(R.drawable.bg_chip)
                    nBtn.setBackgroundResource(R.drawable.bg_chip)
                    cBtn.setBackgroundResource(R.drawable.bg_chip)
                    oBtn.setBackgroundResource(R.drawable.bg_chip)

                    pBtn.setTextColor(getColor(R.color.colorOnBackground))
                    nBtn.setTextColor(getColor(R.color.colorOnBackground))
                    cBtn.setTextColor(getColor(R.color.colorOnBackground))
                    oBtn.setTextColor(getColor(R.color.colorOnBackground))
                }

                pBtn.setOnClickListener {
                    resetGenreFilterBtns()
                    pBtn.setBackgroundResource(R.drawable.bg_pill_accent)
                    pBtn.setTextColor(getColor(R.color.colorOnPrimary))
                    Toast.makeText(this, "Lọc thể loại: Phổ biến nhất", Toast.LENGTH_SHORT).show()
                }
                nBtn.setOnClickListener {
                    resetGenreFilterBtns()
                    nBtn.setBackgroundResource(R.drawable.bg_pill_accent)
                    nBtn.setTextColor(getColor(R.color.colorOnPrimary))
                    Toast.makeText(this, "Lọc thể loại: Mới nhất", Toast.LENGTH_SHORT).show()
                }
                cBtn.setOnClickListener {
                    resetGenreFilterBtns()
                    cBtn.setBackgroundResource(R.drawable.bg_pill_accent)
                    cBtn.setTextColor(getColor(R.color.colorOnPrimary))
                    Toast.makeText(this, "Lọc thể loại: Đã Hoàn Thành", Toast.LENGTH_SHORT).show()
                }
                oBtn.setOnClickListener {
                    resetGenreFilterBtns()
                    oBtn.setBackgroundResource(R.drawable.bg_pill_accent)
                    oBtn.setTextColor(getColor(R.color.colorOnPrimary))
                    Toast.makeText(this, "Lọc thể loại: Đang Cập Nhật", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun updateReaderTypography(viewGroup: ViewGroup) {
        val childCount = viewGroup.childCount
        for (i in 0 until childCount) {
            val child = viewGroup.getChildAt(i)
            if (child is TextView) {
                // Apply dynamic font size calculated on slider values
                child.setTextSize(TypedValue.COMPLEX_UNIT_SP, if (child.id == R.id.txt_reader_title) readerFontSize + 6 else readerFontSize)
                
                // Toggle serif vs plain sans typefaces
                if (readerFontFamily == "literata") {
                    child.setTypeface(Typeface.SERIF, if (child.id == R.id.txt_reader_title) Typeface.BOLD else Typeface.NORMAL)
                } else {
                    child.setTypeface(Typeface.SANS_SERIF, if (child.id == R.id.txt_reader_title) Typeface.BOLD else Typeface.NORMAL)
                }
            }
        }
    }

    private fun applyThemeColorsToReader(canvas: View) {
        val txtTitle = canvas.findViewById<TextView>(R.id.txt_reader_title)
        val txtP1 = canvas.findViewById<TextView>(R.id.txt_reader_p1)
        val txtP2 = canvas.findViewById<TextView>(R.id.txt_reader_p2)
        val txtP3 = canvas.findViewById<TextView>(R.id.txt_reader_p3)
        val txtP4 = canvas.findViewById<TextView>(R.id.txt_reader_p4)

        val headerBar = canvas.findViewById<View>(R.id.layout_reader_header)
        val bottomBar = canvas.findViewById<View>(R.id.layout_reader_bottom_bar)
        val sheetCard = canvas.findViewById<View>(R.id.card_reader_settings_sheet)

        val bgCol: Int
        val textCol: Int
        val sheetBg: Int

        when (readerTheme) {
            "sepia" -> {
                bgCol = getColor(R.color.colorBackgroundSepia)
                textCol = getColor(R.color.colorOnBackgroundSepia)
                sheetBg = getColor(R.color.colorSurfaceContainerHigh)
            }
            "dark" -> {
                bgCol = getColor(R.color.colorBackgroundDark)
                textCol = getColor(R.color.colorOnBackgroundDark)
                sheetBg = getColor(R.color.colorSurfaceContainerDark)
            }
            else -> { // white
                bgCol = getColor(R.color.colorBackground)
                textCol = getColor(R.color.colorOnBackground)
                sheetBg = getColor(R.color.colorSurfaceContainerLowest)
            }
        }

        // Apply theme color swaps programmatically
        canvas.setBackgroundColor(bgCol)
        headerBar.setBackgroundColor(bgCol)
        bottomBar.setBackgroundColor(bgCol)
        sheetCard.setBackgroundColor(sheetBg)

        // Set typeface colors on all paragraphs
        txtTitle?.setTextColor(if (readerTheme == "dark") textCol else getColor(R.color.colorPrimary))
        txtP1?.setTextColor(textCol)
        txtP2?.setTextColor(textCol)
        txtP3?.setTextColor(textCol)
        txtP4?.setTextColor(textCol)
    }

    // Async Image Loader natively built inside Kotlin coroutines
    private fun ImageView.loadImage(url: String) {
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

    override fun onDestroy() {
        super.onDestroy()
        // clean up all concurrent image networking workers on destroy
        for (job in imageJobs.values) {
            job.cancel()
        }
    }
}
