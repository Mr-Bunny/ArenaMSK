package com.example.arenamsk.utils

import android.graphics.*
import android.widget.ImageView

object ImageUtils {

    fun createCircleBitmap(source: Bitmap): Bitmap {
        val output = Bitmap.createBitmap(
            source.width,
            source.height, Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(output)

        val color = -0xbdbdbe
        val paint = Paint()
        val rect = Rect(
            0, 0, source.width,
            source.height
        )

        paint.isAntiAlias = true
        canvas.drawARGB(0, 0, 0, 0)
        paint.color = color
        canvas.drawCircle(source.width / 2f, source.height / 2f, source.width / 2.35f, paint)
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        canvas.drawBitmap(source, rect, rect, paint)

        return output
    }

    fun getImageFromView(imageView: ImageView): Bitmap {
        val bitmap = Bitmap.createBitmap(imageView.width, imageView.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        imageView.draw(canvas)

        return bitmap
    }
}