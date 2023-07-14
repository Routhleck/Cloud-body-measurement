package com.cloudsports.actiondetect

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.cloudsports.actiondetect.data.User
import com.cloudsports.actiondetect.debug.ToastDebug
import com.cloudsports.actiondetect.netWorkUtils.UserRegisterAPI
import kotlinx.coroutines.runBlocking

class RegisterActivity : AppCompatActivity(), View.OnClickListener {

    companion object {
        const val RESULT_CODE_REGISTER = 0
    }

    private lateinit var btnRegister: Button
    private lateinit var etAccount: EditText
    private lateinit var etPass: EditText
    private lateinit var etPassConfirm: EditText
    private lateinit var cbAgree: CheckBox

    private val toast = ToastDebug(this)

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
                toast.show("用户名不能为空！")
                return
            }
            TextUtils.isEmpty(pass) -> {
                toast.show("密码不能为空！")
                return
            }
            !TextUtils.equals(pass, passConfirm) -> {
                toast.show("密码不一致！")
                return
            }
            !cbAgree.isChecked -> {
                toast.show("请同意用户协议！")
                return
            }
            else -> {
                val spf = getSharedPreferences("spfRecorid", MODE_PRIVATE)
                val edit = spf.edit()
                edit.putString("account", name)
                edit.putString("password", pass)
                edit.apply()
                val registerRequest= User.RegisterRequest(name,pass)
                val repository = UserRegisterAPI()
                runBlocking {
                    val result = repository.userRegitser(registerRequest)
                    if (result != null) {
                        val intent = Intent()
                        val bundle = Bundle()
                        bundle.putString("account", name)
                        bundle.putString("password", pass)
                        intent.putExtras(bundle)
                        setResult(RESULT_CODE_REGISTER, intent)
                        toast.show("注册成功！")
                        finish()
                    } else {
                        toast.show("注册失败！")
                    }
                }
                
            }
        }
    }
}
