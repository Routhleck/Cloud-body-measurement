package com.cloudsports.actiondetect

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class HomeActivity : AppCompatActivity() {

    private lateinit var tvContent: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        supportActionBar?.title = "首页"

        tvContent = findViewById(R.id.tv_content)
        val intent = intent
        val account = intent.getStringExtra("account")
        tvContent.text = "欢迎你：$account"
    }

    //退出登录按钮点击事件
    fun loginOut(view: View) {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}