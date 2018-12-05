package com.cornellappdev.android.eatery

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import ithaca_transit.android.cornellappdev.com.ithaca_transit.Models.Place

import ithaca_transit.android.cornellappdev.com.ithaca_transit.R
import ithaca_transit.android.cornellappdev.com.ithaca_transit.Utils.App

class MainListAdapter internal constructor(
        private var mContext: Context,
        private val mListAdapterOnClickHandler: ListAdapterOnClickHandler,
        private var mPlaceList: Array<Place>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var mQuery: String? = null

    interface ListAdapterOnClickHandler {
        fun onClick(position: Int, list: Array<Place>)
    }

    internal fun setList(list: Array<Place>, count: Int, query: String) {
        mQuery = query
        mPlaceList = list
        notifyDataSetChanged()
    }

    /**
     * Set view to layout of CardView
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder? {
        val view: View
        val layoutId: Int
        var viewHolder: RecyclerView.ViewHolder? = null

        layoutId = R.layout.card_item_maps
        System.out.println("context" + mContext);
        view = LayoutInflater.from(mContext).inflate(layoutId, parent, false)
        viewHolder = TextAdapterViewHolder(view)

        return viewHolder
    }

    override fun getItemCount(): Int {
        return mPlaceList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        val holder2 = holder as TextAdapterViewHolder
        holder2.cafe_name.setText(mPlaceList.get(position).name)
    }

    internal inner class ListAdapterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var placeName: TextView


        init {
            placeName = itemView.findViewById(R.id.place_name)
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            val adapterPosition = adapterPosition
            mListAdapterOnClickHandler.onClick(adapterPosition, mPlaceList)
        }
    }

    internal inner class TextAdapterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var cafe_name: TextView

        init {
            cafe_name = itemView.findViewById(R.id.place_name)
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            val adapterPosition = adapterPosition
            mListAdapterOnClickHandler.onClick(adapterPosition, mPlaceList)
        }
    }
}
