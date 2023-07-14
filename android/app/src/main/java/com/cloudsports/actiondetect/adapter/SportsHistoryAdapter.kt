package com.cloudsports.actiondetect.adapter

import android.annotation.SuppressLint
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

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val sport = sports[position]
        val actionName_CN = when (sport.name) {
            "pullUp" -> "引体向上"
            "pushUp" -> "俯卧撑"
            "squat" -> "深蹲"
            "sitUp" -> "仰卧起坐"
            else -> "未知"
        }

        holder.name.text = "运动名: ${actionName_CN}"
        holder.time.text = "运动时长: ${sport.time}秒"
        holder.results.text = "运动结果: ${sport.results}次"
        holder.dateTime.text = "运动时间: ${sport.dateTime}"
    }

    override fun getItemCount() = sports.size
}