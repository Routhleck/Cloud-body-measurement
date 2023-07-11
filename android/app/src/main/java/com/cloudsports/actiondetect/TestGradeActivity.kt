package com.cloudsports.actiondetect

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cloudsports.actiondetect.adapter.TestGradeAdapter
import com.cloudsports.actiondetect.data.Grade
import com.cloudsports.actiondetect.data.GradeItem

class TestGradeActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_grade)

        val grade = getGrade(2021, 175.0, 60.0, 3000, 220.0, 20.0, 20, 6.0, 200.0) // 提供具体的数据来获取 Grade 对象

        viewManager = LinearLayoutManager(this)
        viewAdapter = TestGradeAdapter(grade.items)

        recyclerView = findViewById<RecyclerView>(R.id.recyclerview).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }

        // 在获取到数据后，设置得分和等级的文本
        findViewById<TextView>(R.id.tv_grade).text = "得分：${grade.score}"
        findViewById<TextView>(R.id.tv_grade_level).text = "等级：${grade.level}"
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
