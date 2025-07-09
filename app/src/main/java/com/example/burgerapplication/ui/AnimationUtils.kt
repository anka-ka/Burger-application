package com.example.burgerapplication.ui

import android.content.Context
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView

object AnimationUtils {
    fun animateProductToCart(
        productImage: ImageView,
        basketIcon: ImageView,
        rootLayout: ViewGroup,
        context: Context
    ) {
        val productLocation = IntArray(2)
        val cartLocation = IntArray(2)

        productImage.getLocationOnScreen(productLocation)
        basketIcon.getLocationOnScreen(cartLocation)

        val startX = productLocation[0].toFloat()
        val startY = productLocation[1].toFloat()
        val endX = cartLocation[0].toFloat()
        val endY = cartLocation[1].toFloat()

        val animatedView = ImageView(context).apply {
            layoutParams = FrameLayout.LayoutParams(productImage.width, productImage.height)
            setImageDrawable(productImage.drawable)
            x = startX
            y = startY
        }

        rootLayout.addView(animatedView)
        val offsetX = productImage.width * 0.4f

        animatedView.animate()
            .x(endX-offsetX)
            .y(endY)
            .scaleX(0.1f)
            .scaleY(0.1f)
            .setDuration(600)
            .withEndAction {
                rootLayout.removeView(animatedView)
            }
            .start()
    }
}