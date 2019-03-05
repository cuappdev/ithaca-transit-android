package ithaca_transit.android.cornellappdev.com.ithaca_transit

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

class RoutesRecyclerAdapter() : RecyclerView.Adapter<RoutesRecyclerAdapter.ViewHolder>() {

    private val durations = arrayOf("8:30 - 8:45", "8:50 - 9:10", "9:10 - 9:30")

    private val descriptions = arrayOf("via Campus Road", "Via University Ave",
            "via West Ave")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoutesRecyclerAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.route_card, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: RoutesRecyclerAdapter.ViewHolder, position: Int) {
        holder.bindItems(durations[position],descriptions[position])
    }

    override fun getItemCount(): Int {
        return durations.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(duration: String, description: String) {
            val dur = itemView.findViewById(R.id.duration) as TextView
            val desc  = itemView.findViewById(R.id.route_description) as TextView
            dur.text = duration
            desc.text = description
        }
    }
}
