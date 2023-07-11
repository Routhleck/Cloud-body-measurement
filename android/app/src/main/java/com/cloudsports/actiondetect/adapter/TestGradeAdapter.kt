package com.cloudsports.actiondetect.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cloudsports.actiondetect.R
import com.cloudsports.actiondetect.data.GradeItem

class TestGradeAdapter(private val myDataset: List<GradeItem>) :
    RecyclerView.Adapter<TestGradeAdapter.MyViewHolder>() {

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTestItem: TextView = view.findViewById(R.id.tv_test_item)
        val tvTestResult: TextView = view.findViewById(R.id.tv_test_result)
        val tvTestScore: TextView = view.findViewById(R.id.tv_test_score)
        val tvScoreLevel: TextView = view.findViewById(R.id.tv_score_level)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_test_grade, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val gradeItem = myDataset[position]

        holder.tvTestItem.text = gradeItem.testName
        holder.tvTestResult.text = gradeItem.testResult.toString()
        holder.tvTestScore.text = gradeItem.testScore.toString()
        holder.tvScoreLevel.text = gradeItem.scoreLevel
    }

    override fun getItemCount() = myDataset.size
}
