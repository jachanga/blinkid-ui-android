package com.microblink.documentscanflow.ui.scanlineanimator

import android.graphics.PorterDuff
import android.support.annotation.UiThread
import android.support.v4.content.ContextCompat
import android.view.View
import android.view.animation.Animation
import com.microblink.documentscanflow.ui.utils.AccelerateDecelerateReverseInterpolator
import android.view.animation.Transformation
import android.widget.ImageView
import com.microblink.documentscanflow.R
import com.microblink.documentscanflow.R.id.arrowRight
import kotlinx.android.synthetic.main.mb_include_camera_overlay.*
import org.jetbrains.anko.px2dip


class DefaultScanLineAnimator(private val scanLineView: ImageView,
                              private val scanContainer: View) : ScanLineAnimator {

    private var containerHeight = 0f
    private var scanLineHeight = 0f
    private var scanAnimation = createAnimation()

    init {
        scanContainer.addOnLayoutChangeListener(object : View.OnLayoutChangeListener {
            override fun onLayoutChange(v: View?, left: Int, top: Int, right: Int, bottom: Int, oldLeft: Int, oldTop: Int, oldRight: Int, oldBottom: Int) {
                containerHeight = scanContainer.height * 1.0f
                if (containerHeight > 0) {
                    scanContainer.removeOnLayoutChangeListener(this)
                }
                startAnimation()
            }
        })

        scanLineView.visibility = View.VISIBLE
        scanLineView.addOnLayoutChangeListener(object : View.OnLayoutChangeListener {
            override fun onLayoutChange(v: View?, left: Int, top: Int, right: Int, bottom: Int, oldLeft: Int, oldTop: Int, oldRight: Int, oldBottom: Int) {
                scanLineHeight = scanLineView.height * 1.0f
                if (scanLineHeight > 0) {
                    scanLineView.removeOnLayoutChangeListener(this)
                }
                startAnimation()
            }
        })

        scanLineView.drawable.mutate()
                .setColorFilter(ContextCompat.getColor(scanLineView.context, R.color.mbIconScanLine), PorterDuff.Mode.MULTIPLY)
    }

    @UiThread
    override fun onScanStart() {
        startAnimation()
    }

    @UiThread
    override fun onScanPause() {
        scanAnimation.cancel()
        scanLineView.visibility = View.GONE
    }

    @UiThread
    override fun onScanResume() {
        startAnimation()
    }

    private fun canStartAnimation() = containerHeight > 0 && scanLineHeight > 0

    private fun startAnimation() {
        if (!canStartAnimation()) {
            return
        }

        if (scanAnimation.hasStarted()) {
            scanAnimation.cancel()
            scanAnimation = createAnimation()
        }

        val previewHeightDp = scanContainer.px2dip(containerHeight.toInt())
        val scanLineAnimDuration = 4.167f * previewHeightDp + 1250

        scanAnimation.apply {
            duration = scanLineAnimDuration.toLong()
            interpolator = AccelerateDecelerateReverseInterpolator()
            repeatCount = Animation.INFINITE
        }

        scanLineView.startAnimation(scanAnimation)
    }

    private fun createAnimation(): Animation {
        return object : Animation() {
            override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
                val translateHeight = containerHeight * interpolatedTime - scanLineHeight / 2
                scanLineView.translationY = translateHeight
            }
        }
    }

}