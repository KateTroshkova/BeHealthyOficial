package com.be_healthy_license_2014141300.be_healthy.disease

import android.app.Activity
import com.be_healthy_license_2014141300.be_healthy.R

class Cataract(): Disease() {

    constructor(activity: Activity):this(){
        val res=activity.resources
        name=res.getString(R.string.cataract)
        description=res.getString(R.string.cataract_description)
        symptoms= mutableListOf(res.getString(R.string.double_vision),
                res.getString(R.string.spots_before_eyes),
                res.getString(R.string.blurred_vision),
                res.getString(R.string.colouring_of_objects_in_yellowish_color),
                res.getString(R.string.difficulty_reading),
                res.getString(R.string.difficulties_when_manipulating_small_objects),
                res.getString(R.string.photophobia),
                res.getString(R.string.violation_of_the_twilight_vision),
                res.getString(R.string.weakened_the_perception_of_colors),
                res.getString(R.string.vision_impairment),
                res.getString(R.string.clouding_of_the_pupil))
        fillList(activity, treatment, R.array.cataract_treatment)
        fillList(activity, magic, R.array.cataract_magic)
    }
}