package com.example

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

object BookRepository {
    val booksList: MutableList<BookItem> = mutableListOf()

    init {
        booksList.addAll(
            listOf(
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
        )
    }

    var selectedBookId = "1"
    var previousScreen = "home"
    
    // Active configuration settings for story reader
    var readerTheme = "white" // white, sepia, dark
    var readerFontSize = 17f // default 17sp text size
    var readerFontFamily = "literata" // literata, inter
}
