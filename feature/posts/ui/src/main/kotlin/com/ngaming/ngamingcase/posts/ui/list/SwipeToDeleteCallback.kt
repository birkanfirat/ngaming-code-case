package com.ngaming.ngamingcase.posts.ui.list

import android.graphics.Canvas
import android.graphics.drawable.ColorDrawable
import android.view.View
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.ngaming.ngamingcase.posts.ui.R
import com.ngaming.ngamingcase.core.ui.R as CoreUiR

/** Satır kaydırılınca arkada kırmızı zemin ve çöp ikonu çıkarıyor. */
class SwipeToDeleteCallback(
    recyclerView: RecyclerView,
    private val onSwiped: (position: Int) -> Unit,
) : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.START or ItemTouchHelper.END) {

    private val context = recyclerView.context
    private val background = ColorDrawable(ContextCompat.getColor(context, R.color.swipe_delete_background))
    private val icon = checkNotNull(AppCompatResources.getDrawable(context, R.drawable.ic_delete_24))
    private val iconMargin = context.resources.getDimensionPixelSize(CoreUiR.dimen.spacing_large)

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder,
    ) = false

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        viewHolder.bindingAdapterPosition
            .takeIf { it != RecyclerView.NO_POSITION }
            ?.let(onSwiped)
    }

    override fun onChildDraw(
        canvas: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean,
    ) {
        if (dX != 0f) {
            drawBackdrop(canvas, viewHolder.itemView, dX)
        }
        super.onChildDraw(canvas, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }

    private fun drawBackdrop(canvas: Canvas, itemView: View, dX: Float) {
        val swipingRight = dX > 0
        if (swipingRight) {
            background.setBounds(itemView.left, itemView.top, itemView.left + dX.toInt(), itemView.bottom)
        } else {
            background.setBounds(itemView.right + dX.toInt(), itemView.top, itemView.right, itemView.bottom)
        }
        background.draw(canvas)

        val iconTop = itemView.top + (itemView.height - icon.intrinsicHeight) / 2
        if (swipingRight) {
            val left = itemView.left + iconMargin
            icon.setBounds(left, iconTop, left + icon.intrinsicWidth, iconTop + icon.intrinsicHeight)
        } else {
            val right = itemView.right - iconMargin
            icon.setBounds(right - icon.intrinsicWidth, iconTop, right, iconTop + icon.intrinsicHeight)
        }
        if (background.bounds.width() > icon.intrinsicWidth + iconMargin * 2) {
            icon.draw(canvas)
        }
    }
}
