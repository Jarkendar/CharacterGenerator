package com.skrzypczak.charactergenerator

import android.graphics.Bitmap

interface PageListener {
    fun getScreenShot(): Bitmap
}