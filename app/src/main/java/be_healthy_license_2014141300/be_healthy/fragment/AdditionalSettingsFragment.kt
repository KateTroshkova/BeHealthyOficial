package be_healthy_license_2014141300.be_healthy.fragment

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.be_healthy_license_2014141300.be_healthy.R
import com.be_healthy_license_2014141300.be_healthy.ShareManager

class AdditionalSettingsFragment : Fragment() {

    protected val EYE=3
    protected val IMB=4
    protected val ALARM=5
    protected  val SAVE=6
    protected val SETTINGS=7

    private var mListener: OnFragmentInteractionListener? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        if (mListener==null){
            if (activity is OnFragmentInteractionListener){
                mListener= activity as OnFragmentInteractionListener
            }
            else{
                Log.e("LOG", "I'm stupid bastard")
            }
        }
        var view=inflater!!.inflate(R.layout.fragment_additional_settings, container, false)
        var settingsText=view.findViewById<TextView>(R.id.s_t)
        settingsText.setOnClickListener(object:View.OnClickListener{
            override fun onClick(p0: View?) {
                mListener?.onFragmentInteraction(SETTINGS)
            }
        })
       // var alarmText=view.findViewById<TextView>(R.id.a_t)
       // alarmText.setOnClickListener(object:View.OnClickListener{
        //    override fun onClick(p0: View?) {
        //        mListener?.onFragmentInteraction(ALARM)
        //    }
        //})
        var eyeText=view.findViewById<TextView>(R.id.e_t)
        eyeText.setOnClickListener(object:View.OnClickListener{
            override fun onClick(p0: View?) {
                mListener?.onFragmentInteraction(EYE)
            }
        })
        var ibmText=view.findViewById<TextView>(R.id.f_t)
        ibmText.setOnClickListener(object:View.OnClickListener{
            override fun onClick(p0: View?) {
                mListener?.onFragmentInteraction(IMB)
            }
        })
        var saveText=view.findViewById<TextView>(R.id.save_t)
        saveText.setOnClickListener(object:View.OnClickListener{
            override fun onClick(p0: View?) {
                mListener?.onFragmentInteraction(SAVE)
            }
        })
        return view
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            mListener = context
        } else {
            throw RuntimeException(context!!.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    interface OnFragmentInteractionListener {
        fun onFragmentInteraction(id:Int)
    }
}
