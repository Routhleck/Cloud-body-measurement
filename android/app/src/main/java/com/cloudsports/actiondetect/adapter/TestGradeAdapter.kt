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
        when (gradeItem.scoreLevel) {
            "flunk" -> {
                holder.tvScoreLevel.text = "不及格"
                // textView背景色设置为浅红色
                holder.tvScoreLevel.setBackgroundColor(0xFFFFC0CB.toInt())
            }
            "pass" -> {
                holder.tvScoreLevel.text = "及格"
                // textView背景色设置为浅黄色
                holder.tvScoreLevel.setBackgroundColor(0xFFFFFF00.toInt())
            }
            "good" -> {
                holder.tvScoreLevel.text = "良好"
                // textView背景色设置为浅蓝色
                holder.tvScoreLevel.setBackgroundColor(0xFF00FFFF.toInt())

            }
            "excellent" -> {
                holder.tvScoreLevel.text = "优秀"
                // textView背景色设置为浅绿色
                holder.tvScoreLevel.setBackgroundColor(0xFF00FF00.toInt())
            }
        }
    }

    override fun getItemCount() = myDataset.size
}
