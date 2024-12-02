package com.kukdudelivery

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import com.kukdudelivery.ApiController.LoginResponse
import com.kukdudelivery.WebApi.WebServiceCaller
import com.kukdudelivery.databinding.ActivityLoginBinding
import com.kukdudelivery.util.Utility
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ActivityLogin : BaseActivity() {
    var binding : ActivityLoginBinding? =null

    //9925834234
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        try {

            binding!!.Login.setOnClickListener(View.OnClickListener {
                if (TextUtils.isEmpty( binding!!.Mobile.getText().toString().trim { it <= ' ' })) {
                    showErrorMsg("please enter mobile number")
                } else if (TextUtils.isEmpty( binding!!.Password.getText().toString().trim { it <= ' ' })) {
                    showErrorMsg("enter password")
                } else APICALL()
            })
            binding!!.btnLoginwithOTP.setOnClickListener(View.OnClickListener {
                finish()
                startActivity(Intent(this@ActivityLogin, ActivityLoginWithOTP::class.java))
            })
            binding!!.btnForgotPassword.setOnClickListener(View.OnClickListener {
                finish()
                startActivity(Intent(this@ActivityLogin, ActivityForgotPassword::class.java))
            })
        } catch (ex: Exception) {
            println("Login Error--->$ex")
        }
    }

    private fun APICALL() {
        try {
            if (!Utility.isNetworkAvailable(this)) {
                showInfoMsg("please check internet connection")
            } else {
                Utility.showProgress(this@ActivityLogin)
                val login = WebServiceCaller.getClient()
                val responseCall = login.Login( binding!!.Mobile.getText().toString().trim { it <= ' ' },
                    binding!!.Password.getText().toString().trim { it <= ' ' },
                    "123",
                    "android",
                    appPreferences.getString("DEVICE_KEY")
                )
                responseCall.enqueue(object : Callback<LoginResponse?> {
                    override fun onResponse(
                        call: Call<LoginResponse?>,
                        response: Response<LoginResponse?>
                    ) {
                        Utility.hideProgress()
                        if (response.isSuccessful) {
                            val memberResponse = response.body()
                            if (memberResponse!!.isValid) {
                                appPreferences["USERID"] = memberResponse.usertbl.UserID
                                appPreferences["USERNAME"] = memberResponse.usertbl.UserName
                                appPreferences["MOBILE"] = memberResponse.usertbl.UserMobile
                                appPreferences["EMAIL"] = memberResponse.usertbl.UserEmail
                                appPreferences["USERTYPE"] = memberResponse.usertbl.UserType
                                appPreferences["HELP"] = memberResponse.Help
                                finish()
                                startActivity(Intent(this@ActivityLogin, MainActivity::class.java))
                            } else {
                                showErrorMsg(memberResponse.ResponseMsg)
                            }
                        } else {
                            showInfoMsg(response.message())
                        }
                    }

                    override fun onFailure(call: Call<LoginResponse?>, t: Throwable) {
                        Utility.hideProgress()
                        showErrorMsg("please contact admin")
                        sendMail(
                            """
    ${LoginAPIError}User ID = ${appPreferences.getString("USERID")}
    Error = $t
    """.trimIndent()
                        )
                    }
                })
            }
        } catch (ex: Exception) {
            showInfoMsg(ex.toString())
            sendMail(
                """
    ${LoginAPIError}User ID = ${appPreferences.getString("USERID")}
    Error = $ex
    """.trimIndent()
            )
        }
    }
}
