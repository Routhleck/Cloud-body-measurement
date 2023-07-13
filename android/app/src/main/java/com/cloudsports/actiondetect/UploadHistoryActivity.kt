package com.cloudsports.actiondetect

import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.cloudsports.actiondetect.debug.ToastDebug
import com.cloudsports.actiondetect.model.GlobalVariable
import com.cloudsports.actiondetect.netWorkUtils.Grade
import kotlinx.coroutines.runBlocking
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class UploadHistoryActivity : AppCompatActivity() {
    private val toast: ToastDebug = ToastDebug(this)

    private fun check(user_id: Int? =GlobalVariable.userId,item:String,number:String) {

    }


    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload_history)

        // 初始化界面
        val username_text = requireViewById<TextView>(R.id.username_text)
        val sport_name_text = requireViewById<TextView>(R.id.sport_name_text)
        val sport_result_text = requireViewById<TextView>(R.id.sport_result_text)
        val sport_time_text = requireViewById<TextView>(R.id.sport_time_text)
        val sport_date_time_text = requireViewById<TextView>(R.id.sport_date_time_text)

        // 获取intent过来的数据
        val username = GlobalVariable.userName
        var actionName = GlobalVariable.actionName
        val actionCount = GlobalVariable.actionCount
        val actionTime = GlobalVariable.actionTime

        // 获取当前日期和时间
        val currentDateTime = LocalDateTime.now()

        // 格式化为字符串
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val formattedDateTime = currentDateTime.format(formatter)

        val actionDateTime = formattedDateTime

        // 转化动作名
        when (actionName) {
            "pullUp" -> actionName = "引体向上"
            "pushUp" -> actionName = "俯卧撑"
            "squat" -> actionName = "深蹲"
            "sitUp" -> actionName = "仰卧起坐"
        }

        // 设置界面
        username_text.text = "用户名: $username"
        sport_name_text.text = "动作名: $actionName"
        sport_result_text.text = "运动结果: $actionCount"
        sport_time_text.text = "运动用时: $actionTime"
        sport_date_time_text.text = "运动时间: $actionDateTime"


        val uploadButton = findViewById<Button>(R.id.upload_button)
        uploadButton.setOnClickListener {
            // 处理上传逻辑
            val repository = Grade()
            val judge = runBlocking {
                val updateRequest =com.cloudsports.actiondetect.data.Grade.updateRequest(GlobalVariable.userId,actionName!!,actionCount.toString(),actionTime.toString())
                val result =repository.updateGrade(GlobalVariable.userId,actionName!!,actionCount.toString(),actionTime.toString())
                if(result != null){
                    val codeJudge = result.get("code")
                    if (codeJudge == "200")
                    {
                        toast.show("上传成功")

                    }
                    else{
                        toast.show("上传失败")

                    }
                }
            }

        }
    }
}