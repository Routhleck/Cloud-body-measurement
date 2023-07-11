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

class RegisterActivity : AppCompatActivity(), View.OnClickListener {

    companion object {
        const val RESULT_CODE_REGISTER = 0
    }

    private lateinit var btnRegister: Button
    private lateinit var etAccount: EditText
    private lateinit var etPass: EditText
    private lateinit var etPassConfirm: EditText
    private lateinit var cbAgree: CheckBox

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        supportActionBar?.title = "注册"

        etAccount = findViewById(R.id.et_account)
        etPass = findViewById(R.id.et_password)
        etPassConfirm = findViewById(R.id.et_password_Confirm)
        cbAgree = findViewById(R.id.cb_agree)
        btnRegister = findViewById(R.id.btn_register)

        btnRegister.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        val name = etAccount.text.toString()
        val pass = etPass.text.toString()
        val passConfirm = etPassConfirm.text.toString()

        when {
            TextUtils.isEmpty(name) -> {
                Toast.makeText(this@RegisterActivity, "用户名不能为空！", Toast.LENGTH_LONG).show()
                return
            }
            TextUtils.isEmpty(pass) -> {
                Toast.makeText(this@RegisterActivity, "密码不能为空！", Toast.LENGTH_LONG).show()
                return
            }
            !TextUtils.equals(pass, passConfirm) -> {
                Toast.makeText(this@RegisterActivity, "密码不一致！", Toast.LENGTH_LONG).show()
                return
            }
            !cbAgree.isChecked -> {
                Toast.makeText(this@RegisterActivity, "请同意用户协议！", Toast.LENGTH_LONG).show()
                return
            }
            else -> {
                val spf = getSharedPreferences("spfRecorid", MODE_PRIVATE)
                val edit = spf.edit()
                edit.putString("account", name)
                edit.putString("password", pass)
                edit.apply()

                val intent = Intent()
                val bundle = Bundle()
                bundle.putString("account", name)
                bundle.putString("password", pass)
                intent.putExtras(bundle)
                setResult(RESULT_CODE_REGISTER, intent)
                Toast.makeText(this@RegisterActivity, "注册成功！", Toast.LENGTH_LONG).show()
                finish()
            }
        }
    }
}
