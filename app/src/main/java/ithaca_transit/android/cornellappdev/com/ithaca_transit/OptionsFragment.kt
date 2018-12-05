package ithaca_transit.android.cornellappdev.com.ithaca_transit

import android.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

/**
 * Created by abdullahislam on 12/4/18.
 */
class OptionsFragment : Fragment(){

    lateinit var allRoutesText:TextView

    companion object {
        fun newInstance(): Fragment{
            return OptionsFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        allRoutesText = container!!.findViewById<TextView>(R.id.optionsText)
        return inflater?.inflate(R.layout.route_options_fragment, container, false)
    }



}