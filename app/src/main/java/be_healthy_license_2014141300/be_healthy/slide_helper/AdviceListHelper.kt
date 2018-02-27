package com.be_healthy_license_2014141300.be_healthy.slide_helper

import com.be_healthy_license_2014141300.be_healthy.adapter.AdviceAdapter

class AdviceListHelper(dragDirs:Int, swipeDirs:Int, listener: ListHelper.OnSwipeListener): ListHelper(dragDirs, swipeDirs, listener) {

    override fun onSelectedChanged(viewHolder: android.support.v7.widget.RecyclerView.ViewHolder?, actionState:Int) {
        if (viewHolder != null) {
            val foregroundView = (viewHolder as AdviceAdapter.MyViewHolder).viewForeground
            getDefaultUIUtil().onSelected(foregroundView)
        }
    }

    override fun onChildDrawOver(canvas: android.graphics.Canvas,
                                 recyclerView: android.support.v7.widget.RecyclerView,
                                 viewHolder: android.support.v7.widget.RecyclerView.ViewHolder,
                                 dX:Float,
                                 dY:Float,
                                 actionState:Int,
                                 isCurrentlyActive:Boolean) {
        val foregroundView = (viewHolder as AdviceAdapter.MyViewHolder).viewForeground
        getDefaultUIUtil().onDrawOver(canvas, recyclerView, foregroundView, dX, dY, actionState, isCurrentlyActive)
    }

    override fun clearView(recyclerView: android.support.v7.widget.RecyclerView?, viewHolder: android.support.v7.widget.RecyclerView.ViewHolder?) {
        val foregroundView = (viewHolder as AdviceAdapter.MyViewHolder).viewForeground
        getDefaultUIUtil().clearView(foregroundView)
    }

    override fun onChildDraw(canvas: android.graphics.Canvas,
                             recyclerView: android.support.v7.widget.RecyclerView,
                             viewHolder: android.support.v7.widget.RecyclerView.ViewHolder,
                             dX:Float,
                             dY:Float,
                             actionState:Int,
                             isCurrentlyActive:Boolean){
        val foregroundView = (viewHolder as AdviceAdapter.MyViewHolder).viewForeground
        getDefaultUIUtil().onDraw(canvas, recyclerView, foregroundView, dX, dY, actionState, isCurrentlyActive)
    }
}