package com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.signalstrenhtmeter.indicators

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.speedmeter.SSpedoMeter
import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.speedmeter.Speedometer
import java.util.*

/**
 * this Library build By Anas Altair
 * see it on [GitHub](https://github.com/anastr/SpeedView)
 */
@Suppress("UNCHECKED_CAST")
abstract class SSIndicator<out I : SSIndicator<I>> (context: Context): Observable() {

    protected var indicatorPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val density: Float = context.resources.displayMetrics.density

    protected var speedometer : SSpedoMeter? = null

    /**
     * indicator width in pixel, this value has several meaning
     * between [SSIndicator.Indicators], it will be ignored
     * when using [ImageIndicator].
     */
    var width: Float = 0f
        set(indicatorWidth) {
            field = indicatorWidth
            speedometer?.let { updateIndicator() }
            setChanged()
            notifyObservers(null)
        }

    /**
     * change indicator's color,
     * this option will be ignored when using [ImageIndicator].
     */
    var color = 0xff2196F3.toInt()
        set(indicatorColor) {
            field = indicatorColor
            speedometer?.let { updateIndicator() }
            setChanged()
            notifyObservers(null)
        }

    /**
     * @return top Y position of the indicator.
     */
    open fun getTop(): Float = if (speedometer != null) speedometer!!.padding.toFloat() else 0f
    /**
     * @return Bottom Y position of the indicator.
     */
    open fun getBottom(): Float = getCenterY()
    /**
     * @return down point after center.
     */
    fun getLightBottom(): Float = if (getCenterY() > getBottom()) getBottom() else getCenterY()

    /**
     * @return x center of speedometer.
     */
    fun getCenterX(): Float = if (speedometer != null) speedometer!!.size / 2f else 0f

    /**
     * @return y center of speedometer.
     */
    fun getCenterY(): Float = if (speedometer != null) speedometer!!.size / 2f else 0f

    init {
        indicatorPaint.color = color
    }

    abstract fun draw(canvas: Canvas)
    /**
     * called when size change or color, width.
     * also when speedometer changed.
     *
     * this indicator must be observed by Speedometer when to call this method.
     */
    abstract fun updateIndicator()

    /**
     * @param withEffects if indicator have effects like BlurMaskFilter.
     */
    protected abstract fun setWithEffects(withEffects: Boolean)

    /**
     * to change indicator's data,
     * this method called by the library.
     * @param speedometer target speedometer.
     */
    fun setTargetSpeedometer(speedometer: SSpedoMeter): I {
        deleteObservers()
        addObserver(speedometer)
        this.speedometer = speedometer
        updateIndicator()
        return this as I
    }

    fun dpTOpx(dp: Float): Float {
        return dp * density
    }

    /**
     * @return size of Speedometer View without padding.
     */
    fun getViewSize(): Float {
        speedometer?.let { return it.size.toFloat() - it.padding * 2f }
        return 0f
    }

    /**
     * call this method to apply/remove blur effect for indicator.
     * @param withEffects effect.
     */
    fun withEffects(withEffects: Boolean) {
        setWithEffects(withEffects)
        speedometer?.let { updateIndicator() }
    }

    /** indicator's shape  */
    enum class Indicators {
        NoIndicator, NormalIndicator, NormalSmallIndicator, TriangleIndicator, SpindleIndicator, LineIndicator, HalfLineIndicator, QuarterLineIndicator, KiteIndicator, NeedleIndicator
    }

    companion object {

        /**
         * create new [SSIndicator] with default values.
         * @param context required.
         * @param indicator new indicator (Enum value).
         * @return new indicator object.
         */
        fun createIndicator(context: Context, speedometer: SSpedoMeter, indicator: Indicators): SSIndicator<*> {
            return when (indicator) {
                Indicators.NoIndicator -> SSNoIndicator(context)
                Indicators.NormalIndicator -> SSNormalIndicator(context)
                Indicators.NormalSmallIndicator -> SSNormalSmallIndicator(context)
                Indicators.TriangleIndicator -> SSTriangleIndicator(context)
                Indicators.SpindleIndicator -> SSSpindleIndicator(context)
                Indicators.LineIndicator -> SSLineIndicator(context, 1f)
                Indicators.HalfLineIndicator -> SSLineIndicator(context, .5f)
                Indicators.QuarterLineIndicator -> SSLineIndicator(context, .25f)
                Indicators.KiteIndicator -> SSKiteIndicator(context)
                Indicators.NeedleIndicator -> SSNeedleIndicator(context)

            }.setTargetSpeedometer(speedometer)
        }
    }
}
