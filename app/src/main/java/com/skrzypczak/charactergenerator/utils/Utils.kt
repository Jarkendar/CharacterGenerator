package com.skrzypczak.charactergenerator.utils

import android.graphics.Bitmap
import android.graphics.Canvas
import android.view.View

fun View.createViewBitmap(): Bitmap {
    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    draw(canvas)
    return bitmap
}

fun View.hide() {
    visibility = View.GONE
}

fun View.show() {
    visibility = View.VISIBLE
}

fun String.capitalizeWords(): String =
    split(" ").joinToString(" ") { word -> word.replaceFirstChar { char -> char.uppercase() } }