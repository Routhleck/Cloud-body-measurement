package com.cloudsports.actiondetect

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {

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
            if (userName.isEmpty()) {
                Toast.makeText(this@LoginActivity, "还没有注册账号！", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            if (TextUtils.equals(account, userName)) {
                if (TextUtils.equals(password, pass)) {
                    Toast.makeText(this@LoginActivity, "恭喜你，登录成功！", Toast.LENGTH_LONG).show()
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
                } else {
                    Toast.makeText(this@LoginActivity, "密码错误！", Toast.LENGTH_LONG).show()
                }
            } else {
                Toast.makeText(this@LoginActivity, "用户名错误", Toast.LENGTH_LONG).show()
            }
        }
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

