package com.itoys.android.uikit.components.decoration

import android.content.Context
import android.graphics.*
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.itoys.android.uikit.R
import com.itoys.android.utils.expansion.dp2px

/**
 * @author Fanfan Gu <a href="mailto:stefan.gufan@gmail.com">Contact me.</a>
 * @date 02/08/2021 14:18
 * @desc
 */
class RecordDecoration(private var context: Context) : RecyclerView.ItemDecoration() {

    private val imageSize = 18.dp2px()
    private val dp1 = 1.dp2px()
    private val dp3 = 3.dp2px()
    private val dp22 = 22.dp2px()
    private val dp28 = 28.dp2px()

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        //获取layoutParams参数
        val layoutParams = view.layoutParams as RecyclerView.LayoutParams
        val viewIndex = layoutParams.viewLayoutPosition

        outRect.set(dp28, 0, 0, if (viewIndex < 2) dp22 else 0)
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)
        draw(c, parent)
    }

    private fun draw(c: Canvas, parent: RecyclerView) {
        val childSize = parent.childCount
        for (index in 0 until childSize) {
            val itemView = parent.getChildAt(index)
            val bitmapLeft = itemView.left - dp28

            val bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.uikit_ic_record_tag)
            val bitmapRect = Rect(0, 0, imageSize, imageSize)
            val dst = RectF(
                bitmapLeft.toFloat(), (itemView.top.toFloat() + dp1),
                (bitmapLeft + imageSize).toFloat(), (itemView.top + imageSize + dp1).toFloat()
            )
            c.drawBitmap(bitmap, bitmapRect, dst, Paint())

            if (index < childSize - 1) {
                val lineBitmap = BitmapFactory.decodeResource(context.resources, R.drawable.uikit_ic_record_line)
                val lineBitmapLeft = bitmapLeft + (imageSize / 2) - (dp1 / 2)
                val lineBitmapRect = Rect(0, 0, dp1, dp22)
                val lineDst = RectF(
                    lineBitmapLeft.toFloat(),
                    (itemView.top.toFloat() + imageSize),
                    (lineBitmapLeft + dp1).toFloat(),
                    (itemView.top + imageSize + (dp1 * 3) + dp22).toFloat()
                )
                c.drawBitmap(lineBitmap, lineBitmapRect, lineDst, Paint())
            }
        }
    }
}