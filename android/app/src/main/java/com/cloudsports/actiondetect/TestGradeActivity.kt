package com.cloudsports.actiondetect

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.cloudsports.actiondetect.adapter.TestGradeAdapter
import com.cloudsports.actiondetect.adapter.ViewPagerAdapter
import com.cloudsports.actiondetect.data.Grade
import com.cloudsports.actiondetect.data.GradeItem
import com.cloudsports.actiondetect.fragment.YearFragment

class TestGradeActivity : AppCompatActivity() {


    private lateinit var viewPager: ViewPager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_grade)


        viewPager = findViewById(R.id.viewpager)

        viewPager.adapter = ViewPagerAdapter(supportFragmentManager)

        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
                // no-op
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                // no-op
            }

            override fun onPageSelected(position: Int) {
                // 当用户滑动到新的页面时调用
                // 在这里，你可以更新你的Grade数据
                val year = 2023 - position
                updateGradeForYear(year)
            }
        })



    }

    fun updateGradeForYear(year: Int): Grade {
        // 更新Grade数据
        return getGrade(year, 175.0, 60.0, 3000, 220.0, 20.0, 20, 6.0, 200.0)
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


}
