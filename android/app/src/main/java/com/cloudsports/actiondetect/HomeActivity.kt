package com.cloudsports.actiondetect

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.cloudsports.actiondetect.fragment.SettingsFragment
import com.cloudsports.actiondetect.fragment.SportCenterFragment
import com.cloudsports.actiondetect.fragment.UserCenterFragment
import com.google.android.material.bottomnavigation.BottomNavigationView


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

        val bottomNavigation: BottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_user_center -> {
                    // Replace the contents of the container with the new fragment
                    val userCenterFragment = UserCenterFragment()
                    val transaction = supportFragmentManager.beginTransaction()
                    transaction.replace(R.id.fragment_container, userCenterFragment)
                    transaction.commit()
                    true
                }

                R.id.nav_sport_center -> {
                    // Replace the contents of the container with the new fragment
                    val sportCenterFragment = SportCenterFragment()
                    val transaction = supportFragmentManager.beginTransaction()
                    transaction.replace(R.id.fragment_container, sportCenterFragment)
                    transaction.commit()
                    true
                }

                R.id.nav_settings -> {
                    // Replace the contents of the container with the new fragment
                    val settingsFragment = SettingsFragment()
                    val transaction = supportFragmentManager.beginTransaction()
                    transaction.replace(R.id.fragment_container, settingsFragment)
                    transaction.commit()
                    true
                }

                else -> false
            }
        }
    }

    //退出登录按钮点击事件
    fun loginOut(view: View) {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}

