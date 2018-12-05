package ithaca_transit.android.cornellappdev.com.ithaca_transit

import android.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.android.synthetic.main.route_options_fragment.view.*

class OptionsFragment : Fragment(){

    lateinit var allRoutesText:TextView

    companion object {
        fun newInstance(): Fragment{
            return OptionsFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater!!.inflate(R.layout.route_options_fragment, container, false)

        view.allRoutes.setOnClickListener { view ->
            val extendedFragment = ExtendedFragment
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.container, extendedFragment.newInstance())
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()

        }


        return view
    }



}