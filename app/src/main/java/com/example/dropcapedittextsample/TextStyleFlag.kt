package com.example.dropcapedittextsample

import androidx.annotation.IntDef

const val BOLD_STYLE_FLAG = 1
const val ITALIC_STYLE_FLAG = 2
const val UNDERLINE_STYLE_FLAG = 4

@IntDef(
    BOLD_STYLE_FLAG,
    ITALIC_STYLE_FLAG,
    UNDERLINE_STYLE_FLAG
)
@Retention(AnnotationRetention.SOURCE)
annotation class TexStyleFlag



