package com.cloudsports.actiondetect

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.cloudsports.actiondetect.data.Sport
import com.cloudsports.actiondetect.data.User
import com.cloudsports.actiondetect.debug.ToastDebug
import com.cloudsports.actiondetect.model.GlobalVariable
import com.cloudsports.actiondetect.netWorkUtils.GradeAPI
import com.cloudsports.actiondetect.netWorkUtils.UserLoginAPI
import kotlinx.coroutines.runBlocking
import org.json.JSONObject

class LoginActivity : AppCompatActivity() {

    private val toast = ToastDebug(this)


    companion object {
        const val REQUEST_CODE_REGISTER = 1
        const val TAG = "tag"
    }

    private lateinit var btnLogin: Button
    private lateinit var etAccount: EditText
    private lateinit var etPassword: EditText
    private lateinit var cbRemember: CheckBox
    private var userName = "a"
    private var pass = "123"

    private var isSuccess = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar?.title = "登录"

        initView()
        initData()

        btnLogin.setOnClickListener {
            val account = etAccount.text.toString()
            val password = etPassword.text.toString()

            when (val result = check(account, password)) {
                -1 -> toast.show("密码错误或用户名和密码不匹配")

                -2 -> toast.show("传输过程出现问题，请检查网络或练习服务器管理员")

                1 -> {
                    GlobalVariable.userId = result
                    toast.show("恭喜你，登录成功！")
                    val spf = getSharedPreferences("spfRecorid", MODE_PRIVATE)
                    val edit = spf.edit()
                    edit.putBoolean("isRemember", cbRemember.isChecked)
                    if (cbRemember.isChecked) {
                        edit.putString("account", account)
                        edit.putString("password", password)
                    }
                    edit.apply()

                    GlobalVariable.userName = account
                    getRecordById(GlobalVariable.userId!!)

                    if (isSuccess) {
                        val intent = Intent(this@LoginActivity, HomeActivity::class.java)
                        intent.putExtra("account", account)
                        startActivity(intent)
                        this@LoginActivity.finish()
                    }
                }
            }
        }
    }

    /*
    输入用户id，得到一个json object
     */
    private fun getRecordById(user_id:Int) {
        val repository = GradeAPI()
        var result: JSONObject? = null
        try {
            result = runBlocking {
                val response = repository.getRecord(user_id)
                if (response != null) {
                    return@runBlocking response
                } else {
                    return@runBlocking null
                }
            }
            result = result?.get("data") as JSONObject?

            // 从record提取totalCount和totalTime以及ItemList
            GlobalVariable.totalCount = (result?.get("totalCount") as Double).toInt()
            GlobalVariable.totalTime = ((result.get("totalTime") as Double) / 60).toInt()
            // 将itemList的元素转换为 Map 而不是 JSONObject
            val temp_sportsHistoryList = result.getJSONArray("ItemList")

            // 处理sportsHistoryList
            val sportsHistoryList = mutableListOf<Sport>()
            for (i in 0 until temp_sportsHistoryList.length()) {
                val temp = temp_sportsHistoryList.getJSONObject(i)
                val count = temp.getDouble("count").toInt()
                val time = temp.getString("time")
                val practiceTime = temp.getDouble("practiceTime").toInt()
                val name = temp.getString("name")
                sportsHistoryList.add(Sport(name, practiceTime, count.toString(), time))
            }


            GlobalVariable.sportsHistoryList = sportsHistoryList

        } catch (e: Exception) {
            // Log error message
            isSuccess = false
            Log.e("Network request failed", e.toString())
            toast.show("网络请求失败")
        }

    }

    private fun check(name : String,password : String ): Int? {
        val repository = UserLoginAPI()
        val loginRequest = User.LoginRequest(name,password)

        try {
            val judge = runBlocking {
                val result = repository.userLogin(loginRequest)
                if (result != null){
                    val codeJudge=result.get("code")
                    /*
                    user_id:表示用户名和密码匹配并传递成功并且回传user_id
                    -2:传输过程出现问题
                    -1：用户名和密码不匹配
                     */
                    if (codeJudge=="200"){
                        val userId = (result.get("data") as Double).toInt()
                        return@runBlocking userId
                    }
                    else{
                        return@runBlocking -1
                    }
                }
                else {
                    return@runBlocking -2
                }
            }
            return judge
        } catch (e: Exception) {
            // Log error message
            isSuccess = false
            Log.e("Network request failed", e.toString())
        }
        return -2
    }
    private fun initData() {
        val spf = getSharedPreferences("spfRecorid", MODE_PRIVATE)
        val isRemember = spf.getBoolean("isRemember", false)
        val account = spf.getString("account", "")
        val password = spf.getString("password", "")
        userName = account!!
        pass = password!!
        if (isRemember) {
            etAccount.setText(account)
            etPassword.setText(password)
            cbRemember.isChecked = true
        }
    }

    private fun initView() {
        btnLogin = findViewById(R.id.btn_login)
        etAccount = findViewById(R.id.et_account)
        etPassword = findViewById(R.id.et_password)
        cbRemember = findViewById(R.id.cb_remember)
    }

    fun toRegister(view: View) {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivityForResult(intent, REQUEST_CODE_REGISTER)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_REGISTER && resultCode == RegisterActivity.RESULT_CODE_REGISTER && data != null) {
            val extras = data.extras
            val account = extras?.getString("account", "")
            val password = extras?.getString("password", "")
            etAccount.setText(account)
            etPassword.setText(password)
            userName = account!!
            pass = password!!
        }
    }
}

