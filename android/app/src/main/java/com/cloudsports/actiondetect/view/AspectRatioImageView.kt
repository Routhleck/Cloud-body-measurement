package com.cloudsports.actiondetect.view

import android.content.Context
import android.util.AttributeSet

class AspectRatioImageView(context: Context, attrs: AttributeSet) : androidx.appcompat.widget.AppCompatImageView(context, attrs) {
    private var aspectRatio = 4f / 3f

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = measuredWidth
        val height = (width / aspectRatio).toInt()
        setMeasuredDimension(width, height)

    }
}