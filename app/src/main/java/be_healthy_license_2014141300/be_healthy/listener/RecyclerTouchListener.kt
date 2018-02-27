package com.be_healthy_license_2014141300.be_healthy.listener

import android.view.MotionEvent
import android.support.v7.widget.RecyclerView
import android.content.Context
import android.view.GestureDetector


internal class RecyclerTouchListener(context: Context, recycleView: RecyclerView, private val clicklistener: ClickListener?) :
        RecyclerView.OnItemTouchListener {
    private val gestureDetector: GestureDetector

    init {
        gestureDetector = GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
            override fun onSingleTapUp(e: MotionEvent): Boolean {
                return true
            }

            override fun onLongPress(event: MotionEvent) {
                val child = recycleView.findChildViewUnder(event.x, event.y)
                if (child != null && clicklistener != null) {
                    clicklistener.onLongClick(child, recycleView.getChildAdapterPosition(child))
                }
            }
        })
    }

    override fun onInterceptTouchEvent(rv: RecyclerView, event: MotionEvent): Boolean {
        val child = rv.findChildViewUnder(event.x, event.y)
        if (child != null && clicklistener != null && gestureDetector.onTouchEvent(event)) {
            clicklistener.onClick(child, rv.getChildAdapterPosition(child))
        }
        return false
    }

    override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {}

    override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}
}