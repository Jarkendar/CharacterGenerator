package com.skrzypczak.charactergenerator.utils

import android.graphics.drawable.Drawable
import androidx.databinding.BindingAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton

@BindingAdapter("app:srcCompat")
fun setSrcCompat(view: FloatingActionButton, imageId: Drawable) {
    view.setImageDrawable(imageId)
}