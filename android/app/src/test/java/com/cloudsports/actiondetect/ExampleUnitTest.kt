package com.cloudsports.actiondetect.actiondetect

import com.cloudsports.actiondetect.data.User
import com.cloudsports.actiondetect.model.GlobalVariable
import com.cloudsports.actiondetect.netWorkUtils.GradeAPI
import com.cloudsports.actiondetect.netWorkUtils.UserLoginAPI
import kotlinx.coroutines.runBlocking
import org.junit.Test

import org.junit.Before

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Before
    fun setup() {
        // 初始化 android-json 库的模拟支持
        System.setProperty("org.mockito.mock.android-json.enabled", "true")
    }
    @Test
    fun login(){

        val repository= UserLoginAPI()
        // 创建一个 User.LoginRequest 对象
        val loginRequest = User.LoginRequest("admin", "admin")


        // 使用 runBlocking 函数来在主线程中执行协程代码
        runBlocking {
            val result = repository.userLogin(loginRequest)

            if (result != null) {
                println("请求成功，返回结果：$result")
            } else {
                println("请求失败")
            }
        }
        println(GlobalVariable.userId)
    }
    @Test
    fun main(){
        val repository= GradeAPI()
//    // 创建一个 User.LoginRequest 对象
        runBlocking {
            val result = repository.upadteGradeByUserId(1)
            println(result)
        }
    }
    @Test
    fun main1(){
        val repository = GradeAPI()
        runBlocking {
            val result = repository.updateGrade(1,"pullUp","3","4")
            println(result)
        }
    }


    @Test
    fun main2(){
        val repository = GradeAPI()
        runBlocking {
            val result = repository.getRecord(1)
            println(result)
        }
    }
}