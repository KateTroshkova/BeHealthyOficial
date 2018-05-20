package be_healthy_license_2014141300.be_healthy.slide_helper

import android.support.v7.widget.RecyclerView
import com.be_healthy_license_2014141300.be_healthy.adapter.SavedDiseaseAdapter
import com.be_healthy_license_2014141300.be_healthy.adapter.SymptomsAdapter
import com.be_healthy_license_2014141300.be_healthy.slide_helper.ListHelper

class SymptomsListHelper(dragDirs:Int, swipeDirs:Int, listener: OnSwipeListener): ListHelper(dragDirs, swipeDirs, listener) {

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState:Int) {
        if (viewHolder != null) {
            val foregroundView = (viewHolder as SymptomsAdapter.MyViewHolder).viewForeground
            getDefaultUIUtil().onSelected(foregroundView)
        }
    }

    override fun onChildDrawOver(canvas: android.graphics.Canvas,
                                 recyclerView: RecyclerView,
                                 viewHolder: RecyclerView.ViewHolder,
                                 dX:Float,
                                 dY:Float,
                                 actionState:Int,
                                 isCurrentlyActive:Boolean) {
        val foregroundView = (viewHolder as SymptomsAdapter.MyViewHolder).viewForeground
        getDefaultUIUtil().onDrawOver(canvas, recyclerView, foregroundView, dX, dY, actionState, isCurrentlyActive)
    }

    override fun clearView(recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?) {
        val foregroundView = (viewHolder as SymptomsAdapter.MyViewHolder).viewForeground
        getDefaultUIUtil().clearView(foregroundView)
    }

    override fun onChildDraw(canvas: android.graphics.Canvas,
                             recyclerView: RecyclerView,
                             viewHolder: RecyclerView.ViewHolder,
                             dX:Float,
                             dY:Float,
                             actionState:Int,
                             isCurrentlyActive:Boolean){
        val foregroundView = (viewHolder as SymptomsAdapter.MyViewHolder).viewForeground
        getDefaultUIUtil().onDraw(canvas, recyclerView, foregroundView, dX, dY, actionState, isCurrentlyActive)
    }
}