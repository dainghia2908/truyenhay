package com.example

import android.graphics.Typeface
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity

class ReaderViewActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reader_view)

        // Header back navigation
        findViewById<View>(R.id.btn_reader_back)?.setOnClickListener {
            finish()
        }

        // Expandable settings sheet controller
        val settingsSheet = findViewById<View>(R.id.card_reader_settings_sheet)
        val darkenOverlay = findViewById<View>(R.id.view_reader_darken_overlay)
        val btnTrigger = findViewById<View>(R.id.btn_reader_settings)

        btnTrigger?.setOnClickListener {
            settingsSheet?.visibility = View.VISIBLE
            darkenOverlay?.visibility = View.VISIBLE
        }

        darkenOverlay?.setOnClickListener {
            settingsSheet?.visibility = View.GONE
            darkenOverlay.visibility = View.GONE
        }

        // Font sizes adjusts
        val articleContainer = findViewById<ViewGroup>(R.id.layout_reader_article)
        val fontSizeSlider = findViewById<SeekBar>(R.id.slider_reader_font_size)

        fontSizeSlider?.progress = (BookRepository.readerFontSize - 10).toInt()
        fontSizeSlider?.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                BookRepository.readerFontSize = 10f + progress
                if (articleContainer != null) {
                    updateReaderTypography(articleContainer)
                }
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        // Font Family toggles
        val btnLiterata = findViewById<TextView>(R.id.btn_reader_font_literata)
        val btnInter = findViewById<TextView>(R.id.btn_reader_font_inter)

        btnLiterata?.setOnClickListener {
            BookRepository.readerFontFamily = "literata"
            btnLiterata.setBackgroundResource(R.drawable.bg_card_rounded)
            btnLiterata.setTextColor(getColor(R.color.colorPrimary))
            btnInter?.setBackgroundResource(R.drawable.bg_chip)
            btnInter?.setTextColor(getColor(R.color.colorOutline))
            if (articleContainer != null) {
                updateReaderTypography(articleContainer)
            }
        }

        btnInter?.setOnClickListener {
            BookRepository.readerFontFamily = "inter"
            btnInter.setBackgroundResource(R.drawable.bg_card_rounded)
            btnInter.setTextColor(getColor(R.color.colorPrimary))
            btnLiterata?.setBackgroundResource(R.drawable.bg_chip)
            btnLiterata?.setTextColor(getColor(R.color.colorOutline))
            if (articleContainer != null) {
                updateReaderTypography(articleContainer)
            }
        }

        // Ambient Background Theme switcher logic
        val btnWhite = findViewById<View>(R.id.btn_theme_white)
        val btnSepia = findViewById<View>(R.id.btn_theme_sepia)
        val btnDark = findViewById<View>(R.id.btn_theme_dark)

        val chkWhite = findViewById<TextView>(R.id.lbl_theme_white_checked)
        val chkSepia = findViewById<TextView>(R.id.lbl_theme_sepia_checked)
        val chkDark = findViewById<TextView>(R.id.lbl_theme_dark_checked)

        fun refreshThemeCheckmarks() {
            chkWhite?.text = if (BookRepository.readerTheme == "white") "✔" else ""
            chkSepia?.text = if (BookRepository.readerTheme == "sepia") "✔" else ""
            chkDark?.text = if (BookRepository.readerTheme == "dark") "✔" else ""
        }

        val mainRootCanvas = findViewById<View>(R.id.scroll_reader_canvas)?.parent as? View

        fun applyActiveTheme() {
            if (mainRootCanvas != null) {
                applyThemeColorsToReader(mainRootCanvas)
            }
        }

        btnWhite?.setOnClickListener {
            BookRepository.readerTheme = "white"
            refreshThemeCheckmarks()
            applyActiveTheme()
        }
        btnSepia?.setOnClickListener {
            BookRepository.readerTheme = "sepia"
            refreshThemeCheckmarks()
            applyActiveTheme()
        }
        btnDark?.setOnClickListener {
            BookRepository.readerTheme = "dark"
            refreshThemeCheckmarks()
            applyActiveTheme()
        }

        // Run initializations
        refreshThemeCheckmarks()
        applyActiveTheme()
        if (articleContainer != null) {
            updateReaderTypography(articleContainer)
        }

        findViewById<View>(R.id.btn_reader_offline_download)?.setOnClickListener {
            Toast.makeText(this, "Đã lưu trữ ngoại tuyến thành công chương VIP này!", Toast.LENGTH_SHORT).show()
        }

        findViewById<View>(R.id.btn_reader_prev)?.setOnClickListener {
            Toast.makeText(this, "Bạn đã ở chương đầu tiên", Toast.LENGTH_SHORT).show()
        }
        findViewById<View>(R.id.btn_reader_next)?.setOnClickListener {
            Toast.makeText(this, "Tính năng đọc chương kế cần VIP! Hãy nạp VIP nhé", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateReaderTypography(viewGroup: ViewGroup) {
        val childCount = viewGroup.childCount
        for (i in 0 until childCount) {
            val child = viewGroup.getChildAt(i)
            if (child is TextView) {
                child.setTextSize(TypedValue.COMPLEX_UNIT_SP, if (child.id == R.id.txt_reader_title) BookRepository.readerFontSize + 6 else BookRepository.readerFontSize)
                
                if (BookRepository.readerFontFamily == "literata") {
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

        when (BookRepository.readerTheme) {
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

        canvas.setBackgroundColor(bgCol)
        headerBar?.setBackgroundColor(bgCol)
        bottomBar?.setBackgroundColor(bgCol)
        sheetCard?.setBackgroundColor(sheetBg)

        txtTitle?.setTextColor(if (BookRepository.readerTheme == "dark") textCol else getColor(R.color.colorPrimary))
        txtP1?.setTextColor(textCol)
        txtP2?.setTextColor(textCol)
        txtP3?.setTextColor(textCol)
        txtP4?.setTextColor(textCol)
    }
}
