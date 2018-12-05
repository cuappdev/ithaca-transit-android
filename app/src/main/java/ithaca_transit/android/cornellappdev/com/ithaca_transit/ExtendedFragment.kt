package ithaca_transit.android.cornellappdev.com.ithaca_transit

import android.app.Fragment
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.route_options_extended_fragment.view.*


class ExtendedFragment : Fragment(){

    companion object {
        fun newInstance(): Fragment{
            return ExtendedFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater!!.inflate(R.layout.route_options_extended_fragment, container, false)
        val recyclerView = view.routes
        recyclerView.layoutManager = LinearLayoutManager(this.context, LinearLayout.VERTICAL, false)
        val adapter = RoutesRecyclerAdapter()
        view.routes.adapter = adapter
        return view

    }



}