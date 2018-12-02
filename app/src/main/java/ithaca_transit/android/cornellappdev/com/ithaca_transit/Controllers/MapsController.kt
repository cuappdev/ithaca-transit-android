package ithaca_transit.android.cornellappdev.com.ithaca_transit.Controllers

import android.app.PendingIntent.getActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.View.GONE
import android.widget.LinearLayout
import com.cornellappdev.android.eatery.MainListAdapter
import ithaca_transit.android.cornellappdev.com.ithaca_transit.Models.Place

class MapsController: MainListAdapter.ListAdapterOnClickHandler{


    lateinit var mRecView: RecyclerView
    private var listAdapter = MainListAdapter()

    companion object {
        // Dummy place objects
        val place1 = Place("Goldwin Smith")
        val place2 = Place("Duffield")
        val place3 = Place("Rockledge")
        val placeList = arrayOf(place1, place2, place3)
    }

    fun setDynamicRecyclerView()
    {
        mRecView.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(getActivity().getApplicationContext(), LinearLayout.VERTICAL, false)
        mRecView.setLayoutManager(layoutManager)

        listAdapter = MainListAdapter(getActivity().getApplicationContext(),
                this, placeList)
        mRecView.setAdapter(listAdapter)
        mRecView.setVisibility(View.VISIBLE)
        mRecView.setVisibility(GONE)
        listAdapter.notifyDataSetChanged()
    }

    override fun onClick(position: Int, list: Array<Place>) {

    }


}
