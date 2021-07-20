package com.example.dropcapedittextsample.ui.span

import android.graphics.Typeface
import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import android.text.TextPaint
import android.text.style.MetricAffectingSpan
import androidx.annotation.ColorInt
import androidx.annotation.Px
import com.example.dropcapedittextsample.DropCapDefaultValue

class DropCapSpan() : MetricAffectingSpan(), Parcelable {
    @Px
    var textSize: Float = DropCapDefaultValue.TEXT_SIZE
    var isBold: Boolean = false
    var isItalic: Boolean = false
    var isUnderline: Boolean = false

    @ColorInt
    var textColor: Int = DropCapDefaultValue.NONE_COLOR
    var typeface: Typeface? = null
    var letterSpace = DropCapDefaultValue.LETTER_SPACE

    constructor(parcel: Parcel) : this() {
        textSize = parcel.readFloat()
        isBold = parcel.readByte() != 0.toByte()
        isItalic = parcel.readByte() != 0.toByte()
        isUnderline = parcel.readByte() != 0.toByte()
        textColor = parcel.readInt()
        letterSpace = parcel.readFloat()
    }

    override fun updateDrawState(textPaint: TextPaint?) {
        textPaint?.let { apply(it) }
    }

    override fun updateMeasureState(textPaint: TextPaint) {
        apply(textPaint)
    }

    private fun apply(textPaint: TextPaint) {
        applyTextSize(textPaint)
        applyUnderline(textPaint)
        applyTextColor(textPaint)
        applyTypefaceWithStyle(textPaint)
        applyLetterSpace(textPaint)
        applyBaselineShift(textPaint)
    }

    private fun makeStyle(): Int {
        var style = Typeface.NORMAL
        if (isBold) style = style or Typeface.BOLD
        if (isItalic) style = style or Typeface.ITALIC

        return style
    }

    private fun applyTypefaceWithStyle(textPaint: TextPaint) {
        val tf: Typeface? = when {
            typeface != null -> typeface
            textPaint.typeface != null -> textPaint.typeface
            else -> null
        }

        val oldStyle = tf?.style ?: Typeface.NORMAL
        val newStyle = makeStyle()

        val styledTypeface =
            if (tf != null) Typeface.create(tf, newStyle)
            else Typeface.defaultFromStyle(newStyle)

        val fakeStyle: Int = oldStyle and styledTypeface.style.inv()

        if ((fakeStyle and Typeface.BOLD) != 0) textPaint.isFakeBoldText = true
        if ((fakeStyle and Typeface.ITALIC) != 0) textPaint.textSkewX = -0.25f

        textPaint.typeface = styledTypeface
    }

    private fun applyTextColor(textPaint: TextPaint) {
        if (textColor == DropCapDefaultValue.NONE_COLOR) return
        textPaint.color = textColor
    }

    private fun applyUnderline(textPaint: TextPaint) {
        textPaint.isUnderlineText = isUnderline
    }

    private fun applyTextSize(textPaint: TextPaint, isDp: Boolean = false) {
        if (isDp) textSize *= textPaint.density
        textPaint.textSize = textSize
    }

    private fun applyLetterSpace(textPaint: TextPaint) {
        if (letterSpace == DropCapDefaultValue.LETTER_SPACE) return

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            textPaint.letterSpacing = letterSpace
        }
    }

    private fun applyBaselineShift(textPaint: TextPaint) {
        val height = textPaint.fontMetrics.bottom - textPaint.fontMetrics.descent
        textPaint.baselineShift -= height.toInt()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeFloat(textSize)
        parcel.writeByte(if (isBold) 1 else 0)
        parcel.writeByte(if (isItalic) 1 else 0)
        parcel.writeByte(if (isUnderline) 1 else 0)
        parcel.writeInt(textColor)
        parcel.writeFloat(letterSpace)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<DropCapSpan> {
        override fun createFromParcel(parcel: Parcel): DropCapSpan {
            return DropCapSpan(parcel)
        }

        override fun newArray(size: Int): Array<DropCapSpan?> {
            return arrayOfNulls(size)
        }
    }
}