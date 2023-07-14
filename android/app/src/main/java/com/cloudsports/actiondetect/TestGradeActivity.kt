package com.cloudsports.actiondetect

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.cloudsports.actiondetect.adapter.ViewPagerAdapter
import com.cloudsports.actiondetect.data.Grade
import com.cloudsports.actiondetect.data.GradeItem
import com.cloudsports.actiondetect.debug.ToastDebug
import com.cloudsports.actiondetect.model.GlobalVariable
import kotlinx.coroutines.runBlocking
import org.json.JSONObject

class TestGradeActivity : AppCompatActivity() {


    private lateinit var viewPager: ViewPager

    private val toast = ToastDebug(this)

    init {
        GlobalVariable.gradeList = GlobalVariable.userId?.let { updateGradeForUserId(it) }!!
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_grade)

        updateGradeForYear(2023)


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

    fun updateGradeForYear(year: Int) {
        // 找到对应年份的Grade数据
        val grade = GlobalVariable.gradeList?.find { it.year == year.toString() }
        // 更新GlobalVariable中的tempGrade
        GlobalVariable.tempGrade = grade

    }

    /*
    根据用户id获取测试成绩，json object

     */
    private fun updateGradeForUserId(user_id : Int) : List<Grade>{
        val repository = com.cloudsports.actiondetect.netWorkUtils.GradeAPI()
        var response: JSONObject? = null
        try {
            response= runBlocking {
                val result =repository.upadteGradeByUserId(user_id)
                if(result!=null){
                    return@runBlocking result
                }
                else{
                    return@runBlocking null
                }
            }
            val result = response?.getJSONArray("data")

            val tempList = mutableListOf<Grade>()

            // 将result转换为gradeList
            for (i in 0 until result!!.length()){
                val jsonObj = result.getJSONObject(i)
                val year = jsonObj.getString("test_time")
                val height = jsonObj.getDouble("height")
                val weight = jsonObj.getDouble("weight")
                val vitalCapacity = jsonObj.getInt("vital_capacity")
                val standingLongJump = jsonObj.getDouble("standing_long_jump")
                val sitAndReach = jsonObj.getDouble("sit_and_reach")
                val pullOrSitUp = jsonObj.getInt("pull_up")
                val sprint50m = jsonObj.getDouble("sprint_50m")
                val longDistanceRun = jsonObj.getDouble("long_distance_run")
                val grade = getGrade(year, height, weight, vitalCapacity, standingLongJump, sitAndReach, pullOrSitUp, sprint50m, longDistanceRun)
                tempList.add(grade)
            }
            return tempList.toList()
        } catch (e: Exception) {
            Log.e("Network request failed", e.toString())
            toast.show("网络请求失败")
        }
        return emptyList()
    }

    private fun getGrade(
        year: String,
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