package com.cloudsports.actiondetect.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cloudsports.actiondetect.R
import com.cloudsports.actiondetect.adapter.TestGradeAdapter
import com.cloudsports.actiondetect.data.Grade
import com.cloudsports.actiondetect.data.GradeItem
import com.cloudsports.actiondetect.model.GlobalVariable

class YearFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    private lateinit var grade: Grade

    companion object {
        private const val ARG_YEAR = "year"

        fun newInstance(year: Int): YearFragment {
            val fragment = YearFragment()
            val args = Bundle()
            args.putInt(ARG_YEAR, year)
            fragment.arguments = args
            return fragment
        }
    }

    private var year: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            year = arguments?.getInt(ARG_YEAR)
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_year, container, false)

        if (GlobalVariable.tempGrade != null) {
            grade = GlobalVariable.tempGrade!!
        } else {
            grade = Grade(year.toString(), listOf())
        }

        viewManager = LinearLayoutManager(context)
        viewAdapter = TestGradeAdapter(grade!!.items)

        recyclerView = view.findViewById<RecyclerView>(R.id.recyclerview).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }!!

        // 在获取到数据后，设置得分和等级的文本
        view.findViewById<TextView>(R.id.tv_grade).text = "得分：${grade.score}"
        when (grade.level) {
            "flunk" -> {
                view.findViewById<TextView>(R.id.tv_grade_level).text = "不及格"
                // textView背景色设置为浅红色
                view.findViewById<TextView>(R.id.tv_grade_level).setBackgroundColor(0xffff0000.toInt())
            }
            "pass" -> {
                view.findViewById<TextView>(R.id.tv_grade_level).text = "及格"
                // textView背景色设置为浅黄色
                view.findViewById<TextView>(R.id.tv_grade_level).setBackgroundColor(0xffffff00.toInt())
            }
            "good" -> {
                view.findViewById<TextView>(R.id.tv_grade_level).text = "良好"
                // textView背景色设置为浅蓝色
                view.findViewById<TextView>(R.id.tv_grade_level).setBackgroundColor(0xff00ffff.toInt())
            }

            "excellent" -> {
                view.findViewById<TextView>(R.id.tv_grade_level).text = "优秀"
                // textView背景色设置为浅绿色
                view.findViewById<TextView>(R.id.tv_grade_level).setBackgroundColor(0xff00ff00.toInt())
            }
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val textView = requireView().findViewById<TextView>(R.id.tv_year)
        textView.text = year.toString()

        // 在获取到数据后，设置得分和等级的文本
        val tvGrade = requireView().findViewById<TextView>(R.id.tv_grade)
        tvGrade.text = "得分：${grade.score}"
        val tvGradeLevel = requireView().findViewById<TextView>(R.id.tv_grade_level)
        tvGradeLevel.text = grade.level
        when (grade.level) {
            "flunk" -> {
                // textView背景色设置为浅红色
                tvGradeLevel.setBackgroundColor(0xffff0000.toInt())
            }
            "pass" -> {
                // textView背景色设置为浅黄色
                tvGradeLevel.setBackgroundColor(0xffffff00.toInt())
            }
            "good" -> {
                // textView背景色设置为浅蓝色
                tvGradeLevel.setBackgroundColor(0xff00ffff.toInt())
            }

            "excellent" -> {
                // textView背景色设置为浅绿色
                tvGradeLevel.setBackgroundColor(0xff00ff00.toInt())
            }
        }

    }


    // 根据具体逻辑计算总得分等级
    private fun calculateOverallScoreLevel(score: Int): String {
        // 这里根据具体的总得分等级逻辑进行计算
        return when (score) {
            in 90..100 -> "优秀"
            in 80..89 -> "良好"
            in 60..79 -> "及格"
            else -> "不及格"
        }
    }
}

