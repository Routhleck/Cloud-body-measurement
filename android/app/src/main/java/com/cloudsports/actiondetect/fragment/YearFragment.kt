package com.cloudsports.actiondetect.fragment

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

class YearFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_year, container, false)

        val grade = getGrade(2023, 175.0, 60.0, 3000, 220.0, 20.0, 20, 6.0, 200.0) // 提供具体的数据来获取 Grade 对象

        viewManager = LinearLayoutManager(context)
        viewAdapter = TestGradeAdapter(grade.items)

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
        // 在这里，你可以使用year变量来获取你需要的数据，并显示在你的视图上
        // 例如：
        val textView = requireView().findViewById<TextView>(R.id.tv_year)
        textView.text = year.toString()
    }




    private fun getGrade(
        year: Int,
        height: Double,
        weight: Double,
        vitalCapacity: Int,
        standingLongJump: Double,
        sitAndReach: Double,
        pullOrSitUp: Int,
        sprint50m: Double,
        longDistanceRun: Double
    ): Grade {
        // 通过提供的具体数据来获取 Grade 对象
        val gradeItemList: List<GradeItem> = listOf(
            GradeItem("height", "身高（厘米）", height),
            GradeItem("weight", "体重（千克）", weight),
            GradeItem("vital_capacity", "肺活量（毫升）", vitalCapacity.toDouble()),
            GradeItem("standing_long_jump", "立定跳远（厘米）", standingLongJump),
            GradeItem("sit_and_reach", "坐位体前屈（厘米）", sitAndReach),
            GradeItem("pull_or_sit_up", "引体/仰卧（次）", pullOrSitUp.toDouble()),
            GradeItem("sprint_50m", "50米（秒）", sprint50m),
            GradeItem("long_distance_run", "800/1000米（分'秒）", longDistanceRun)
        )

        return Grade(year, gradeItemList)
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

