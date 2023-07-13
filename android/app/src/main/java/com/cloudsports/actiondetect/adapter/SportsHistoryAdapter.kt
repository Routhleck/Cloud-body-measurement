package com.cloudsports.actiondetect.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cloudsports.actiondetect.R
import com.cloudsports.actiondetect.SportsHistoryActivity
import com.cloudsports.actiondetect.data.Sport

class SportsHistoryAdapter(private val sports: List<Sport>) : RecyclerView.Adapter<SportsHistoryAdapter.ViewHolder>() {
    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.sport_name)
        val time: TextView = view.findViewById(R.id.sport_time)
        val results: TextView = view.findViewById(R.id.sport_results)
        val dateTime: TextView = view.findViewById(R.id.sport_date_time)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_sports_history, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val sport = sports[position]
        holder.name.text = sport.name
        holder.time.text = sport.time.toString()
        holder.results.text = sport.results
        holder.dateTime.text = sport.dateTime
    }

    override fun getItemCount() = sports.size
}