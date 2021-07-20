package com.example.dropcapedittextsample.ui.dropcapview

import android.content.ClipboardManager
import android.content.Context
import android.graphics.Color
import android.text.*
import android.text.method.ArrowKeyMovementMethod
import android.text.method.MovementMethod
import android.util.AttributeSet
import com.example.dropcapedittextsample.R
import kotlin.math.*


class DropCapEditText @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = R.attr.editTextStyle
) : DropCapTextView(context, attributeSet, defStyleAttr){
    init {
        setBackgroundColor(Color.TRANSPARENT)
    }

    override fun onTextContextMenuItem(id: Int) = when {
        (id == android.R.id.paste) -> {
            pasteTextAtCursorPosition()
            true
        }
        else -> super.onTextContextMenuItem(id)
    }

    private fun pasteTextAtCursorPosition() {
        text?.let {
            val clipboardManager =
                context.getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager ?: return

            val selStart = selectionStart
            val selEnd = selectionEnd
            val start = max(0, min(selStart, selEnd))
            val end = max(0, max(selStart, selEnd))

            val pastText = clipboardManager.primaryClip?.getItemAt(0)?.text.toString()

            it.replace(start, end, pastText)
        }
    }

    override fun getFreezesText() = true

    override fun getDefaultEditable() = true

    override fun getDefaultMovementMethod(): MovementMethod = ArrowKeyMovementMethod.getInstance()

    override fun setEllipsize(ellipsis: TextUtils.TruncateAt) {
        require(ellipsis != TextUtils.TruncateAt.MARQUEE) {
            ("EditText cannot use the ellipsize mode "
                    + "TextUtils.TruncateAt.MARQUEE")
        }
        super.setEllipsize(ellipsis)
    }

    override fun getAccessibilityClassName(): CharSequence? {
        return DropCapEditText::class.java.name
    }
}