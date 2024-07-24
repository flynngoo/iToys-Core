package com.itoys.android.uikit.components.recyclerview

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.itoys.android.logcat.logcat
import com.itoys.android.uikit.R
import com.itoys.android.uikit.model.StepsModel
import com.itoys.android.uikit.model.StepsStatus
import com.itoys.android.utils.expansion.color
import com.itoys.android.utils.expansion.dp2px
import com.itoys.android.utils.expansion.then

/**
 * @Author Gu Fanfan
 * @Email fanfan.work@outlook.com
 * @Date 2023/12/18
 */
class StepsDecoration(
    private val context: Context,
    private val steps: List<StepsModel>,
) : RecyclerView.ItemDecoration() {

    /** icon size  */
    private val stepsIconSize = 22.dp2px()

    /** item margin top */
    private val marginTop = 25.dp2px()

    /** item line width */
    private val lineWidth = 1.dp2px()

    private val linePaint by lazy {
        Paint().apply {
            color = context.color(R.color.uikit_colorful_E5E6EB)
            strokeWidth = lineWidth.toFloat()
        }
    }

    private val completedLinePaint by lazy {
        Paint().apply {
            color = context.color(R.color.uikit_colorful_42B2F9)
            strokeWidth = lineWidth.toFloat()
        }
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        // 获取layoutParams参数
        val layoutParams = view.layoutParams as RecyclerView.LayoutParams
        outRect.top = marginTop
        val viewIndex = layoutParams.viewLayoutPosition
        if (viewIndex < steps.size - 1) {
            outRect.right = steps[viewIndex].margin
        }
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)
        logcat { "onDraw: state -> $state" }
        drawLine(c, parent)
    }

    private fun drawLine(c: Canvas, parent: RecyclerView) {
        val childSize = parent.childCount
        for (index in 0 until childSize) {
            val item = steps[index]
            val itemView = parent.getChildAt(index)
            val bitmapLeft = itemView.left + (itemView.width / 2).toFloat() - stepsIconSize / 2
            val bitmapRight = bitmapLeft + stepsIconSize

            val bitmap = BitmapFactory.decodeResource(
                context.resources, getStepsIcon(index, item.status)
            )
            val bitmapRect = Rect(0, 0, stepsIconSize, stepsIconSize)
            val dst = RectF(
                bitmapLeft,
                0f,
                bitmapRight,
                stepsIconSize.toFloat()
            )
            c.drawBitmap(bitmap, bitmapRect, dst, Paint())

            if (index != 0) {
                c.drawLine(
                    itemView.left.toFloat(),
                    stepsIconSize / 2f,
                    bitmapLeft - stepsIconSize / 2,
                    stepsIconSize / 2f,
                    leftStepsLine(index)
                )
            }

            if (index < childSize - 1) {
                c.drawLine(
                    bitmapRight + stepsIconSize / 2,
                    stepsIconSize / 2f,
                    itemView.right.toFloat() + item.margin,
                    stepsIconSize / 2f,
                    rightStepsLine(item.status)
                )
            }
        }
    }

    /**
     * 获取steps icon
     */
    private fun getStepsIcon(index: Int, status: StepsStatus): Int {
        return when (status) {
            StepsStatus.NotStarted -> when (index) {
                0 -> R.drawable.uikit_ic_steps1
                1 -> R.drawable.uikit_ic_steps2_not_reached
                2 -> R.drawable.uikit_ic_steps3_not_reached
                3 -> R.drawable.uikit_ic_steps4_not_reached
                else -> R.drawable.uikit_ic_steps1
            }

            StepsStatus.Progress -> when (index) {
                0 -> R.drawable.uikit_ic_steps1
                1 -> R.drawable.uikit_ic_steps2
                2 -> R.drawable.uikit_ic_steps3
                3 -> R.drawable.uikit_ic_steps4
                else -> R.drawable.uikit_ic_steps1
            }

            StepsStatus.Completed -> R.drawable.uikit_ic_steps_completed
        }
    }

    /**
     * 获取steps line
     */
    private fun rightStepsLine(status: StepsStatus): Paint {
        return (status == StepsStatus.Completed).then(completedLinePaint, linePaint)
    }

    /**
     * 获取steps line
     */
    private fun leftStepsLine(index: Int): Paint {
        val lastItem = try {
            steps[index - 1]
        } catch (e: Exception) {
            null
        }

        return (lastItem?.status == StepsStatus.Completed).then(completedLinePaint, linePaint)
    }
}