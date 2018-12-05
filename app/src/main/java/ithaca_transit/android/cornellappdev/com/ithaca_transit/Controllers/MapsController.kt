package ithaca_transit.android.cornellappdev.com.ithaca_transit.Controllers

import android.app.Application
import android.app.PendingIntent.getActivity
import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.View.GONE
import android.widget.LinearLayout
import android.support.v7.app.AppCompatActivity
import com.cornellappdev.android.eatery.MainListAdapter
import ithaca_transit.android.cornellappdev.com.ithaca_transit.Models.Place
import ithaca_transit.android.cornellappdev.com.ithaca_transit.Utils.App
import java.security.AccessController.getContext

class MapsController: MainListAdapter.ListAdapterOnClickHandler{

    lateinit var mRecView: RecyclerView
    private lateinit var listAdapter : MainListAdapter

    companion object {
        // Dummy place objects
        val place1 = Place("To Goldwin Smith - Ithaca Commons")
        val place2 = Place("To Duffield - The Johnson Museum")
        val place3 = Place("To The Lux - Gates Hall")
        val placeList = arrayOf(place1, place2, place3)
    }

    fun setDynamicRecyclerView(context: Context)
    {
        mRecView.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(context,  LinearLayout.HORIZONTAL, false)
        mRecView.setLayoutManager(layoutManager)
        listAdapter = MainListAdapter(context,
                this, placeList)
        mRecView.setAdapter(listAdapter)
        mRecView.setVisibility(View.VISIBLE)
        listAdapter.notifyDataSetChanged()
    }

    override fun onClick(position: Int, list: Array<Place>) {

    }


}
