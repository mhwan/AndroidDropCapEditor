package com.example.dropcapedittextsample.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

//화면 회전시 드롭캡 Map을 저장하기 위한 래퍼 클래스
@Parcelize
data class DropCapArrayMapWrapper(val map: Map<Int, DropCapSpanData>) : Parcelable
