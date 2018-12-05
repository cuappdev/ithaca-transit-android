package ithaca_transit.android.cornellappdev.com.ithaca_transit

import android.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * Created by abdullahislam on 12/4/18.
 */
class ExtendedFragment : Fragment(){

    companion object {
        fun newInstance(): Fragment{
            return ExtendedFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.route_options_extended_fragment, container, false)

    }



}