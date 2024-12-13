package com.dicoding.sahabatgula.ui.navigation_ui.scan

import android.graphics.ImageFormat
import android.graphics.Rect
import android.graphics.YuvImage
import android.media.Image
import java.io.ByteArrayOutputStream

object ImageUtils {

    fun Image.toJPEGByteArray(): ByteArray {
        if (format != ImageFormat.YUV_420_888) {
            throw IllegalArgumentException("Only YUV_420_888 format can be processed, found format: $format")
        }

        val buffer = planes[0].buffer
        val data = ByteArray(buffer.remaining())
        buffer.get(data)

        val yuvImage = YuvImage(data, ImageFormat.NV21, width, height, null)
        val out = ByteArrayOutputStream()
        yuvImage.compressToJpeg(Rect(0, 0, width, height), 50, out)  // Compress with 50% quality
        return out.toByteArray()
    }
}
