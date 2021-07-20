package com.example.dropcapedittextsample.ui

import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.example.dropcapedittextsample.R
import com.example.dropcapedittextsample.ui.dropcapview.DropCapEditText

class MainActivity : AppCompatActivity() {
    private val boldButton by lazy { findViewById<ImageButton>(R.id.bold_button) }
    private val underlineButton by lazy { findViewById<ImageButton>(R.id.underline_button) }
    private val italicButton by lazy { findViewById<ImageButton>(R.id.italic_button) }
    private val size42Button by lazy { findViewById<TextView>(R.id.text_size_42_button) }
    private val size180Button by lazy { findViewById<TextView>(R.id.text_size_180_button) }
    private val redColorButton by lazy { findViewById<Button>(R.id.text_color_red_button) }
    private val orangeColorButton by lazy { findViewById<Button>(R.id.text_color_orange_button) }
    private val serifButton by lazy { findViewById<TextView>(R.id.typeface_serif_button) }
    private val nanumButton by lazy { findViewById<TextView>(R.id.typeface_nanum_button) }
    private val dropCapEditText by lazy { findViewById<DropCapEditText>(R.id.paragraphDropCapEditText) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setToolbarButton()
    }

    private fun setToolbarButton() {
        boldButton.setOnClickListener { dropCapEditText.toggleBoldSelectedDropCap() }
        underlineButton.setOnClickListener { dropCapEditText.toggleUnderlineSelectedDropCap() }
        italicButton.setOnClickListener { dropCapEditText.toggleItalicSelectedDropCap() }
        size42Button.setOnClickListener { dropCapEditText.setTextSizeSelectedDropCap(42, true) }
        size180Button.setOnClickListener { dropCapEditText.setTextSizeSelectedDropCap(180) }
        redColorButton.setOnClickListener {
            dropCapEditText.setTextColorSelectedDropCap(
                ContextCompat.getColor(
                    baseContext,
                    R.color.red
                )
            )
        }
        orangeColorButton.setOnClickListener {
            dropCapEditText.setTextColorSelectedDropCap(
                ContextCompat.getColor(
                    baseContext,
                    R.color.orange
                )
            )
        }
        serifButton.setOnClickListener { dropCapEditText.setTypefaceSelectedDropCap(Typeface.SERIF) }
        nanumButton.setOnClickListener {
            ResourcesCompat.getFont(this, R.font.nanumgothics)
                ?.let { it1 -> dropCapEditText.setTypefaceSelectedDropCap(it1) }
        }
    }
}