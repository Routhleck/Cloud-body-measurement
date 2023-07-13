package com.cloudsports.actiondetect

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.cloudsports.actiondetect.data.Sport
import com.cloudsports.actiondetect.data.User
import com.cloudsports.actiondetect.debug.ToastDebug
import com.cloudsports.actiondetect.model.GlobalVariable
import com.cloudsports.actiondetect.netWorkUtils.Grade
import com.cloudsports.actiondetect.netWorkUtils.UserLogin
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar?.title = "登录"

        initView()
        initData()

        btnLogin.setOnClickListener {
            val account = etAccount.text.toString()
            val password = etPassword.text.toString()
            if (check(account,password)==-1) {
                toast.show("密码错误或用户名和密码不匹配")
            }
            else if (check(account,password)==-2){
                toast.show("传输过程出现问题，请检查网络或练习服务器管理员")
            }
            else{
                GlobalVariable.userId=check(account,password)
                toast.show("恭喜你，登录成功！")
//                GlobalVariable.userName = account

                val spf = getSharedPreferences("spfRecorid", MODE_PRIVATE)
                val edit = spf.edit()
                edit.putBoolean("isRemember", cbRemember.isChecked)
                if (cbRemember.isChecked) {
                    edit.putString("account", account)
                    edit.putString("password", password)
                }
                edit.apply()

                val intent = Intent(this@LoginActivity, HomeActivity::class.java)
                intent.putExtra("account", account)
                startActivity(intent)
                this@LoginActivity.finish()
            }
        }
    }

    /*
    输入用户id，得到一个json object
     */
    private fun getRecordById(user_id:Int):JSONObject?{
        val repository = Grade()
        val result = runBlocking {
            val result =repository.getRecord(user_id)
            if(result!=null){
                return@runBlocking result
            }
            else{
                return@runBlocking null
            }
        }
        // 从record提取totalCount和totalTime以及ItemList
        GlobalVariable.totalCount = result?.get("totalCount") as Int
        GlobalVariable.totalTime = (result.get("totalTime") as Int) / 60
        val temp_sportsHistoryList = result.get("itemList") as List<*>?

        // 将temp提取count, value, time, practiceTime, name放到sportsHistoryList中
        val sportsHistoryList = mutableListOf<Sport>()
        if (temp_sportsHistoryList != null) {
            for (i in temp_sportsHistoryList.indices) {
                val temp = temp_sportsHistoryList[i] as JSONObject
                val count = temp.get("count") as Int
//                val value = temp.get("value") as Int
                val time = (temp.get("time") as String)
                val practiceTime = (temp.get("practiceTime") as Int)
                val name = temp.get("name") as String
                sportsHistoryList.add(Sport(name, practiceTime, count.toString(), time))
            }
        }

        GlobalVariable.sportsHistoryList = sportsHistoryList

        return result
    }

    private fun check(name : String,password : String ): Int? {
        val repository = UserLogin()
        val loginRequest = User.LoginRequest(name,password)

        val judge = runBlocking {
            val result = repository.userLogin(loginRequest)
            if (result != null){
                val codeJudge=result.get("code")
                val userId= result.get("data") as Int
                /*
                user_id:表示用户名和密码匹配并传递成功并且回传user_id
                -2:传输过程出现问题
                -1：用户名和密码不匹配
                 */
                if (codeJudge=="200"){

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

