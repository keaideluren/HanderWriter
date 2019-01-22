package com.luren.handwrite

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View

/**
 * Created by Administrator 拇指/可爱的路人 on 2018/11/22 0022.
 * Email:513421345@qq.com
 * TODO
 */
class HandWriteView : View {
    private val pathList: ArrayList<MutableList<Path>> = ArrayList()
    private val qatoPathList: ArrayList<MutableList<HandPathDot>> = ArrayList()
    private val dotList: ArrayList<MutableList<Dot>> = ArrayList()
    private val paint: Paint = Paint()
    private val path: Path = Path()

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        paint.color = Color.BLACK
        paint.style = Paint.Style.FILL
        paint.isAntiAlias = true
    }

    private var downDot: Dot? = null
//    override fun onTouchEvent(event: MotionEvent?): Boolean {
//        if (event == null || event.action == MotionEvent.ACTION_CANCEL) {
//            return super.onTouchEvent(event)
//        }
//        val newPressure = Math.max((event.pressure - 0.15F) * 3, 0.0F) * 15
//
//        when (event.action) {
//            MotionEvent.ACTION_DOWN -> {
//                downDot = Dot(event.x, event.y, newPressure)
//                pathList.add(ArrayList())
//                dotList.add(ArrayList())
//                dotList.last().add(downDot!!)
//                qatoPathList.add(Path())
//                invalidate()
//            }
//            MotionEvent.ACTION_MOVE -> {
//                val movePath = Path()
//                movePath.reset()
//                val pathLength =
//                    Math.sqrt(
//                        Math.pow((event.x - downDot!!.x).toDouble(), 2.0)
//                                + Math.pow((event.y - downDot!!.y).toDouble(), 2.0)
//                    )
//                val cosPath = (event.x - downDot!!.x) / pathLength
//                val sinPath = (event.y - downDot!!.y) / pathLength
//
//                val xStart = downDot!!.pressure * sinPath
//                val yStart = downDot!!.pressure * cosPath
//
//                val xEnd = newPressure * sinPath
//                val yEnd = newPressure * cosPath
//
//                val startX = downDot!!.x - xStart
//                val startY = downDot!!.y + yStart
//                val endX = downDot!!.x + xStart
//                val endY = downDot!!.y - yStart
//
//                val startX1 = event.x - xEnd
//                val startY1 = event.y + yEnd
//                val endX1 = event.x + xEnd
//                val endY1 = event.y - yEnd
//                movePath.moveTo(startX.toFloat(), startY.toFloat())
//                movePath.lineTo(startX1.toFloat(), startY1.toFloat())
//                movePath.lineTo(endX1.toFloat(), endY1.toFloat())
//                movePath.lineTo(endX.toFloat(), endY.toFloat())
//                movePath.close()
//                downDot = Dot(event.x, event.y, newPressure)
//                pathList.last().add(movePath)
//                dotList.last().add(downDot!!)
//                invalidate()
//            }
//        }
//        return true
//    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_CANCEL) {
            return super.onTouchEvent(event)
        }
        val x3 = event.x
        val y3 = event.y
        val pressure = calculatePressure(event.pressure)
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                qatoPathList.add(ArrayList())
                qatoPathList.last()
                    .add(HandPathDot(x3, y3, x3, y3, x3, y3, x3, y3, x3, y3, x3, y3, x3, y3, pressure = pressure))
            }
            MotionEvent.ACTION_MOVE -> {
                if (qatoPathList.last().isEmpty()) {
                    return super.onTouchEvent(event)
                }

                if (qatoPathList.last().size == 1) {
                    val dot2 = qatoPathList.last().last()
                    val delta2X = calculateDeltaX(dot2.x, dot2.y, x3, y3, dot2.pressure)
                    val delta2Y = calculateDeltaY(dot2.x, dot2.y, x3, y3, dot2.pressure)

                    //开始点
                    val x2s = dot2.x - delta2X
                    val y2s = dot2.y + delta2Y
                    //返回点
                    val x2e = dot2.x + delta2X
                    val y2e = dot2.y - delta2Y

                    dot2.xs = x2s
                    dot2.ys = y2s
                    dot2.xe = x2e
                    dot2.ye = y2e
                    dot2.q1xs = x2s
                    dot2.q1ys = y2s
                    dot2.q2xs = x2e
                    dot2.q2ys = y2e
                    dot2.q1xe = x2s
                    dot2.q1ye = y2s
                    dot2.q2xe = x2e
                    dot2.q2ye = y2e

                    val deltaX = calculateDeltaX(dot2.x, dot2.y, x3, y3, pressure)
                    val deltaY = calculateDeltaY(dot2.x, dot2.y, x3, y3, pressure)

                    //开始点
                    val x3s = x3 - deltaX
                    val y3s = x3 + deltaY
                    //返回点
                    val x3e = x3 + deltaX
                    val y3e = x3 - deltaY
                    val hd = HandPathDot(x3, y3, x3s, y3s, x3e, y3e, x3s, y3s, x3s, y3s, x3e, y3e, x3e, y3e, pressure)
                    qatoPathList.last().add(hd)
                    invalidate()
                } else {
                    val dot1 = qatoPathList.last()[qatoPathList.last().size - 2]

                    val dot2 = qatoPathList.last().last()

                    val deltaX = calculateDeltaX(dot2.x, dot2.y, x3, y3, pressure)
                    val deltaY = calculateDeltaY(dot2.x, dot2.y, x3, y3, pressure)

                    //开始点
                    val x3s = x3 - deltaX
                    val y3s = x3 + deltaY
                    //返回点
                    val x3e = x3 + deltaX
                    val y3e = x3 - deltaY

                    //以下计算，使用带偏移计算
                    val slopeS = calculateSlope(dot1.xs, dot1.ys, x3s, y3s)
                    //开始控制点1
                    val q1xs = calculateP1X(dot1.xs, dot2.xs)
                    val q1ys = calculatePY(slopeS, dot1.xs, dot2.ys, q1xs)
                    //开始控制点2
                    val q2xs = calculateP2X(dot1.xs, dot2.xs)
                    val q2ys = calculatePY(slopeS, dot1.xs, dot2.ys, q2xs)
                    //返回控制点1
                    val q1xe = q1xs + 2 * deltaX
                    val q1ye = q1ys - 2 * deltaX
                    //返回控制点2
                    val q2xe = q2xs + 2 * deltaX
                    val q2ye = q2ys - 2 * deltaX
                    val hd = HandPathDot(x3, y3, x3s, y3s, x3e, y3e, x3s, y3s, x3s, y3s, x3e, y3e, x3e, y3e, pressure)
                    dot2.q1xs = q1xs
                    dot2.q1ys = q1ys
                    dot2.q2xs = q2xs
                    dot2.q2ys = q2ys
                    dot2.q1xe = q1xe
                    dot2.q1ye = q1ye
                    dot2.q2xe = q2xe
                    dot2.q2ye = q2ye
                    Log.i(
                        "HandWriteView",
                        "q1xs:$q1xs,q1ys:,q1ys:$q1ys:q1xe,$q1xe,q1ye:$q1ye,q2xe:$q2xe$,q2ys:$q2ys$,q2xe:$q2xe,q2ye:$q2ye"
                    )
                    qatoPathList.last().add(hd)
                    invalidate()
                }
            }
        }
        return true
    }

    /**
     * 上一条线的第二控制点X坐标
     * A  B两点x坐标差的2/3处
     */
    private fun calculateP1X(x1: Float, x2: Float): Float {
        return x2 - (x2 - x1) / 3
    }

    /**
     * 下一条线的第一控制点X坐标
     * B  C两点x坐标差的1/3处
     */
    private fun calculateP2X(x3: Float, x2: Float): Float {
        return x2 + (x3 - x2) / 3
    }

    /**
     * 控制点Y坐标
     * 点斜式 y-y1=(x-x1)*slope
     */
    private fun calculatePY(slope: Float, x2: Float, y2: Float, x: Float): Float {
        return (x - x2) * slope + y2
    }

    /**
     * 斜率计算
     * A  C点连线
     */
    private fun calculateSlope(x1: Float, y1: Float, x3: Float, y3: Float): Float {
        return (y3 - y1) / (x3 - x1)
    }

    /**
     * 线宽的X偏移量
     * @pressure 压力宽度
     */
    private fun calculateDeltaX(x1: Float, y1: Float, x2: Float, y2: Float, pressure: Float): Float {
        //斜率的正弦值
        val sin = y2 - y1 / calculateLength(x1, y1, x2, y2)
        return pressure * sin
    }

    /**
     * 线宽的Y偏移量
     */
    private fun calculateDeltaY(x1: Float, y1: Float, x2: Float, y2: Float, pressure: Float): Float {
        //斜率的余弦值
        val cos = x2 - x1 / calculateLength(x1, y1, x2, y2)
        return pressure * cos
    }

    /**
     * 勾股定理，计算两点距离
     */
    private fun calculateLength(x1: Float, y1: Float, x2: Float, y2: Float): Float {
        return Math.sqrt(Math.pow((x2 - x1).toDouble(), 2.0) + Math.pow((y2 - y1).toDouble(), 2.0)).toFloat()

    }

    private fun calculatePressure(pressure: Float): Float {
        return Math.max((pressure - 0.15F) * 3, 0.0F) * 15
    }

//    override fun onDraw(canvas: Canvas) {
//        super.onDraw(canvas)
//        pathList.forEach {
//            it.forEach {
//                canvas.drawPath(it, paint)
//            }
//        }
//        dotList.forEach { it.forEach { canvas.drawCircle(it.x, it.y, it.pressure, paint) } }
//    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        qatoPathList.forEach {
            path.reset()
            path.moveTo(it[0].xs, it[0].ys)
            canvas.drawCircle(it[0].x, it[0].y, it[0].pressure, paint)
            for (i in 1 until it.size) {
                path.cubicTo(it[i - 1].q2xs, it[i - 1].q2ys, it[i].q1xs, it[i].q1ys, it[i].xs, it[i].ys)
            }
            path.lineTo(it.last().xe, it.last().ye)
            path.quadTo(it[it.size - 2].q2xe, it[it.size - 2].q2ye, it[it.size - 2].xe, it[it.size - 2].ye)
            for (j in it.size - 2 downTo 1) {
                path.cubicTo(it[j].q1xe, it[j].q1ye, it[j - 1].q2xe, it[j - 1].q2ye, it[j - 1].xe, it[j - 1].ye)
            }
            path.close()
            canvas.drawPath(path, paint)
        }
    }

    fun clear() {
        pathList.clear()
        dotList.clear()
        qatoPathList.clear()
        invalidate()
    }

    fun back() {
        if (pathList.isNotEmpty()) {
            pathList.removeAt(pathList.size - 1)
        }
        if (dotList.isNotEmpty()) {
            dotList.removeAt(dotList.size - 1)
        }
        if (qatoPathList.isNotEmpty()) {
            qatoPathList.removeAt(dotList.size - 1)
        }
        invalidate()
    }

    data class Dot(
        var x: Float = 0F,
        var y: Float = 0F,
        var pressure: Float = 0F
    )

    class HandPathDot(
        var x: Float = 0F,
        var y: Float = 0F,
        var xs: Float = 0F,
        var ys: Float = 0F,
        var xe: Float = 0F,
        var ye: Float = 0F,
        var q1xs: Float = 0F,
        var q1ys: Float = 0F,
        var q2xs: Float = 0F,
        var q2ys: Float = 0F,
        var q1xe: Float = 0F,
        var q1ye: Float = 0F,
        var q2xe: Float = 0F,
        var q2ye: Float = 0F,
        var pressure: Float = 0F
    )
}