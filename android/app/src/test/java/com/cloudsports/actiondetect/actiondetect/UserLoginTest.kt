package com.cloudsports.actiondetect.actiondetect
import UserLogin
import com.cloudsports.actiondetect.data.User
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.json.JSONObject


class UserLoginTest {

    private lateinit var userLogin: UserLogin

    @BeforeEach
    fun setup() {
        userLogin = UserLogin()
    }

    @Test
    fun testUserLogin_success() {
        // 创建一个有效的 LoginRequest
        val loginRequest = User.LoginRequest("admin", "admin")

        userLogin.userLogin(loginRequest) { response ->
            assertNotNull(response, "Response should not be null")

            val code = response?.getString("code")
            assertEquals("200", code, "Response code should be 200")

            val message = response?.getString("message")
            assertEquals("Success", message, "Message should be Success")

            val data = response?.getInt("data")
            assertEquals(1, data, "Data should be 1")
        }
    }

    @Test
    fun testUserLogin_invalidCredentials() {
        // 创建一个无效的 LoginRequest
        val loginRequest = User.LoginRequest("invalidUserName", "invalidPassword")

        userLogin.userLogin(loginRequest) { response ->
            assertNull(response, "Response should be null for invalid credentials")
        }
    }

    @Test
    fun testUserLogin_networkError() {
        // 你可能需要设置一种模拟网络错误的方式。一种可能的做法是使用 mock server
        // 假设我们已经设置好，我们将 URL 设置为无效的 URL
        userLogin.url = "https://cloudsports.top:9090"

        val loginRequest = User.LoginRequest("validUserName", "validPassword")

        userLogin.userLogin(loginRequest) { response ->
            assertNull(response, "Response should be null for network errors")
        }
    }
}
