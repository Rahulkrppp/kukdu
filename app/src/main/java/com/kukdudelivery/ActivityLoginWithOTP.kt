package com.kukdudelivery

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import com.kukdudelivery.ApiController.ForgotPasswordResponse
import com.kukdudelivery.WebApi.WebServiceCaller
import com.kukdudelivery.databinding.ActivityLoginWithOtpBinding
import com.kukdudelivery.util.Utility
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ActivityLoginWithOTP : BaseActivity() {
    var binding: ActivityLoginWithOtpBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginWithOtpBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        binding!!.btnGo.setOnClickListener(View.OnClickListener {
            try {
                if (TextUtils.isEmpty(binding!!.txtPhone.getText().toString())) {
                    openAlert("Please enter mobile number")
                } else {
                    resetOTPAPI()
                }
            } catch (ex: Exception) {
                ex.printStackTrace()
                sendMail(
                    """
    ${LoginOTPAPIError}User ID = ${appPreferences.getString("USERID")}
    Error = $ex
    """.trimIndent()
                )
            }
        })
    }

    private fun resetOTPAPI() {
        try {
            if (!Utility.isNetworkAvailable(this)) {
                showInfoMsg("please check internet connection")
            } else {
                Utility.showProgress(this@ActivityLoginWithOTP)
                val login = WebServiceCaller.getClient()
                val responseCall = login.resetPassword(
                    binding!!.txtPhone!!.getText().toString().trim { it <= ' ' })
                responseCall.enqueue(object : Callback<ForgotPasswordResponse> {
                    override fun onResponse(
                        call: Call<ForgotPasswordResponse>,
                        response: Response<ForgotPasswordResponse>
                    ) {
                        Utility.hideProgress()
                        if (response.isSuccessful) {
                            if (response.body()!!.ResponseCode == 1) {
                                finish()
                                startActivity(
                                    Intent(
                                        this@ActivityLoginWithOTP,
                                        ActivityOTP::class.java
                                    ).putExtra("mobile", binding!!.txtPhone!!.getText().toString())
                                        .putExtra("PAGE", "LOGIN")
                                )
                            } else {
                                showInfoMsg(response.body()!!.ResponseMsg)
                            }
                        } else {
                            showInfoMsg(response.message())
                        }
                    }

                    override fun onFailure(call: Call<ForgotPasswordResponse>, t: Throwable) {
                        Utility.hideProgress()
                        showErrorMsg("please contact admin")
                    }
                })
            }
        } catch (ex: Exception) {
            showInfoMsg(ex.toString())
            sendMail(
                """
    ${LoginOTPAPIError}User ID = ${appPreferences.getString("USERID")}
    Error = $ex
    """.trimIndent()
            )
        }
    }
}
