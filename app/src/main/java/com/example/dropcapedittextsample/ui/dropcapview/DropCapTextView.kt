package com.example.dropcapedittextsample.ui.dropcapview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.os.Parcelable
import android.text.*
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.Px
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.res.ResourcesCompat
import androidx.core.content.res.use
import com.example.dropcapedittextsample.*
import com.example.dropcapedittextsample.data.*
import com.example.dropcapedittextsample.handler.DropCapHandler
import com.example.dropcapedittextsample.ui.span.DropCapSpan
import com.example.dropcapedittextsample.utils.UnitConvertUtil


open class DropCapTextView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    private val defStyleAttr: Int = android.R.attr.textViewStyle,
) : AppCompatTextView(context, attributeSet, defStyleAttr), DropCapView {
    @Px
    var dropCapTextSize: Float = DropCapDefaultValue.TEXT_SIZE
        private set
    var isDropCapBold: Boolean = false
    var isDropCapItalic: Boolean = false
    var isDropCapUnderline: Boolean = false
    var dropCapTypeface: Typeface? = null

    @ColorInt
    var dropCapTextColor = DropCapDefaultValue.NONE_COLOR
    var dropCapLetterSpace = DropCapDefaultValue.LETTER_SPACE
    var dropCapLength = DropCapDefaultValue.DROP_CAP_LENGTH
    private val dropCapHandler by lazy { DropCapHandler(this) }
    private val changeWatchers = mutableListOf<WatcherSpanData>()
    private val spannable
        get() = text as Spannable
    override val textSequence: CharSequence
        get() = text as CharSequence

    private val textWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            applyDropCapSpan()
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            s?.let {
                dropCapHandler.indexHandler.doBeforeTextChanged(start, count, after)
            }
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            s?.let {
                dropCapHandler.indexHandler.doOnTextChanged(
                    start,
                    count,
                    it,
                    createDropCapPropertyWrapper()
                )
            }
        }
    }

    init {
        applyAttribute(attributeSet)
        this.isSaveEnabled = true
        this.setBackgroundColor(Color.TRANSPARENT)
        this.setTextIsSelectable(true)
        this.addTextChangedListener(textWatcher)
        initDropCapText()
        initLineHeight()
    }

    private fun initLineHeight() {
        val ratio = dropCapTextSize / textSize
        val distance =
            (paint.fontMetricsInt.bottom - paint.fontMetricsInt.descent) +
                    (paint.fontMetricsInt.ascent - paint.fontMetricsInt.top)
        val newDistance = ratio * distance
        val originalLineHeight = paint.fontMetricsInt.descent - paint.fontMetricsInt.ascent
        lineHeight = (newDistance + originalLineHeight).toInt()
    }

    private fun initDropCapText() {
        text?.let {
            val length = it.length
            if (length == 0) return
            dropCapHandler.indexHandler.doOnTextChanged(
                0,
                length,
                it,
                createDropCapPropertyWrapper())
            applyDropCapSpan()
        }
    }

    private fun createDropCapPropertyWrapper() = DefaultDropCapProperty(
        isDropCapBold,
        isDropCapItalic,
        isDropCapUnderline,
        dropCapTextSize,
        dropCapTextColor,
        dropCapTypeface,
        dropCapLetterSpace)

    private fun applyAttribute(attrs: AttributeSet?) {
        attrs?.let {
            context.theme.obtainStyledAttributes(
                it,
                R.styleable.DropCapTextView,
                defStyleAttr,
                0
            ).use { typedArray ->
                dropCapTextSize = typedArray.getDimensionPixelSize(
                    R.styleable.DropCapTextView_dropCapTextSize,
                    DropCapDefaultValue.TEXT_SIZE.toInt()
                ).toFloat()
                val style = typedArray.getInteger(
                    R.styleable.DropCapTextView_dropCapTextStyle,
                    DropCapDefaultValue.TEXT_STYLE
                )
                dropCapTextColor = typedArray.getColor(
                    R.styleable.DropCapTextView_dropCapTextColor,
                    DropCapDefaultValue.NONE_COLOR
                )
                val typefaceId = typedArray.getResourceId(
                    R.styleable.DropCapTextView_dropCapTypeface,
                    DropCapDefaultValue.NONE_TYPEFACE_RESOURCE
                )
                dropCapLetterSpace = typedArray.getFloat(
                    R.styleable.DropCapTextView_dropCapLetterSpace,
                    DropCapDefaultValue.LETTER_SPACE
                )
                dropCapLength = typedArray.getInt(
                    R.styleable.DropCapTextView_dropCapLength,
                    DropCapDefaultValue.DROP_CAP_LENGTH
                )
                if (typefaceId != DropCapDefaultValue.NONE_TYPEFACE_RESOURCE) {
                    setDropCapTypefaceByResource(typefaceId)
                }
                findFlags(style)
            }
        }
    }

    private fun containsFlag(@TexStyleFlag flagType: Int, flag: Int) =
        (flagType and flag) == flagType

    private fun findFlags(flag: Int) {
        isDropCapBold = containsFlag(BOLD_STYLE_FLAG, flag)
        isDropCapItalic = containsFlag(ITALIC_STYLE_FLAG, flag)
        isDropCapUnderline = containsFlag(UNDERLINE_STYLE_FLAG, flag)
    }

    override fun setText(text: CharSequence?, type: BufferType?) {
        super.setText(text, BufferType.EDITABLE)
    }

    override fun getText(): Editable? {
        val text = super.getText() ?: return null
        return if (text is Editable) {
            (super.getText() as Editable)
        } else {
            super.setText(text, BufferType.EDITABLE)
            (super.getText() as Editable)
        }
    }

    private fun applyDropCapSpan() {
        removeChangeWatchers()
        removeUnnecessaryDropCapSpan()
        setAllDropCapSpan()
        restoreChangeWatchers()
    }

    private fun removeChangeWatchers() {
        changeWatchers.clear()

        val watchers = spannable.getSpans(0, spannable.length, NoCopySpan::class.java)
        watchers.forEach {
            if (it is TextWatcher || it is SpanWatcher) {
                changeWatchers.add(
                    WatcherSpanData(
                        it,
                        spannable.getSpanStart(it),
                        spannable.getSpanEnd(it),
                        spannable.getSpanFlags(it)
                    )
                )
                spannable.removeSpan(it)
            }
        }
    }

    private fun restoreChangeWatchers() {
        changeWatchers.forEach {
            if (it.start >= 0 && it.end <= spannable.length) {
                spannable.setSpan(it.what, it.start, it.end, it.flag)
            }
        }
    }

    private fun removeUnnecessaryDropCapSpan() {
        dropCapHandler.indexHandler.removeDropCapData?.forEach {
            spannable.removeSpan(it.what)
        }
    }

    private fun removeAllDropCapSpan() {
        val dropCaps = spannable.getSpans(0, spannable.length, DropCapSpan::class.java)
        dropCaps.forEach {
            spannable.removeSpan(it)
        }
    }

    private fun setAllDropCapSpan() {
        if (spannable.isNotEmpty()) {
            dropCapHandler.indexHandler.applyDropCapData.keys.forEach { dropCapIdx ->
                setDropCapAtPosition(dropCapIdx)
            }
        }
    }

    private fun setDropCapAtPosition(dropCapIdx: Int) {
        if (checkRange(dropCapIdx)) {
            dropCapHandler.indexHandler.applyDropCapData[dropCapIdx]?.let { spanData ->
                setDropCapSpan(dropCapIdx, spanData)
            }
        }
    }

    private fun checkRange(start: Int) = start in (0 until length())

    private fun setDropCapSpan(dropCapIdx: Int, spanData: DropCapSpanData) {
        val dropCapLength = dropCapHandler.styleHandler.getDropCapLength(dropCapIdx, dropCapLength)
        if (dropCapLength == 0) return
        spannable.setSpan(
            spanData.what,
            dropCapIdx,
            dropCapIdx + dropCapLength,
            spanData.flag
        )
    }

    private fun changeSelectedDropCapSpan(
        changeDropCapProperty: ChangeDropCapProperty,
        isResizeLayout: Boolean = false,
    ) {
        val dropCapIdx = dropCapHandler.styleHandler.changeSelectionDropCapSpanProperty(
            changeDropCapProperty,
            selectionStart,
            selectionEnd,
            dropCapLength)
        if (dropCapIdx >= 0) {
            updateDropCapSpan(dropCapIdx, isResizeLayout)
        }
    }

    private fun updateDropCapSpan(dropCapIdx: Int, isResizeLayout: Boolean) {
        if (isHardwareAccelerated) setLayerType(View.LAYER_TYPE_SOFTWARE, null)
        if (isResizeLayout) {
            setDropCapAtPosition(dropCapIdx)
        } else invalidate()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        //span 스타일을 바꿔 onDraw가 끝났을 때 하드웨어 가속을 원상태로 돌려놓습니다.
        if (!isHardwareAccelerated) {
            setLayerType(View.LAYER_TYPE_NONE, null)
        }
    }

    fun setDropCapTypefaceByResource(fontResource: Int) {
        val typeface = ResourcesCompat.getFont(context, fontResource)
        typeface?.let {
            dropCapTypeface = it
        }
    }

    fun setDropCapTextSize(textSize: Int, isSP: Boolean = false) {
        dropCapTextSize = UnitConvertUtil.getPxSize(context, textSize, isSP)
    }

    fun toggleBoldSelectedDropCap() {
        changeSelectedDropCapSpan(ChangeDropCapProperty.Bold)
    }

    fun toggleItalicSelectedDropCap() {
        changeSelectedDropCapSpan(ChangeDropCapProperty.Italic)
    }

    fun toggleUnderlineSelectedDropCap() {
        changeSelectedDropCapSpan(ChangeDropCapProperty.Underline)
    }

    fun setTextSizeSelectedDropCap(textSize: Int, isSP: Boolean = false) {
        val textSizePx = UnitConvertUtil.getPxSize(context, textSize, isSP)
        changeSelectedDropCapSpan(ChangeDropCapProperty.TextSize(textSizePx), true)
    }

    fun setTextColorSelectedDropCap(@ColorInt color: Int) {
        changeSelectedDropCapSpan(ChangeDropCapProperty.TextColor(color))
    }

    fun setTypefaceSelectedDropCap(typeface: Typeface) {
        changeSelectedDropCapSpan(ChangeDropCapProperty.Typeface(typeface), true)
    }

    //화면회전시 이전에 적용된 드롭캡을 복구합니다.
    private fun restoreDropCapState(dropCaparrayMapWrapper: DropCapArrayMapWrapper) {
        removeChangeWatchers()
        removeAllDropCapSpan()
        dropCapHandler.restoreDropCapData(dropCaparrayMapWrapper)
        setAllDropCapSpan()
        restoreChangeWatchers()
    }

    override fun onSaveInstanceState(): Parcelable? {
        val bundle = Bundle()
        bundle.putParcelable(DROP_CAP_INSTANCE_STATE,
            dropCapHandler.getDropCapDataWrapper())
        bundle.putParcelable(SUPER_INSTANCE_STATE, super.onSaveInstanceState())
        return bundle
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        var superState = state
        if (superState is Bundle) {
            val dropCapArrayMapWrapper: DropCapArrayMapWrapper? =
                superState.getParcelable(DROP_CAP_INSTANCE_STATE)
            superState = superState.getParcelable(SUPER_INSTANCE_STATE)
            super.onRestoreInstanceState(superState)

            dropCapArrayMapWrapper?.let {
                restoreDropCapState(it)
            }
        }
    }

    companion object {
        const val SUPER_INSTANCE_STATE = "SuperInstanceState"
        const val DROP_CAP_INSTANCE_STATE = "DropCapInstanceState"
    }
}