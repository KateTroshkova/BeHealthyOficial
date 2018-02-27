package com.be_healthy_license_2014141300.be_healthy.slide_helper

import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper

open class ListHelper(dragDirs:Int, swipeDirs:Int, var listener: OnSwipeListener): ItemTouchHelper.SimpleCallback(dragDirs, swipeDirs) {

    interface OnSwipeListener{
        fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int, position: Int)
    }

    override fun onMove(recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?, viewHolder2: RecyclerView.ViewHolder?): Boolean {
        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder?, p1: Int) {
        listener.onSwiped(viewHolder!!, p1, viewHolder.adapterPosition)
    }
}