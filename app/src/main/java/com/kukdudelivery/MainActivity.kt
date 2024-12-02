package com.kukdudelivery

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.Spinner
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kukdudelivery.Adapter.DashboardAdapter
import com.kukdudelivery.Adapter.DashboardPastAdapter
import com.kukdudelivery.Adapter.GroupOrderPickAdapter
import com.kukdudelivery.Adapter.SpinnerAdapter
import com.kukdudelivery.ApiController.DashboardResponse
import com.kukdudelivery.ApiController.DateFilterResponse
import com.kukdudelivery.Model.DateFilterInfo
import com.kukdudelivery.Model.GroupOrder
import com.kukdudelivery.Model.GroupOrderX
import com.kukdudelivery.WebApi.WebServiceCaller
import com.kukdudelivery.databinding.ActivityLoginBinding
import com.kukdudelivery.databinding.ActivityMainBinding
import com.kukdudelivery.util.DividerItemDecorator
import com.kukdudelivery.util.TTextView
import com.kukdudelivery.util.Utility
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : BaseActivity() {

    var mActivity: Activity? = null
    var binding:ActivityMainBinding?=null
    var spDateFilter: Spinner? = null
    var mAdapter: DashboardAdapter? = null
    var pastAdapter: DashboardPastAdapter? = null
    var groupOrderList:ArrayList<GroupOrderX> = arrayListOf()
    var groupOrderAdapter: GroupOrderPickAdapter? =null
    private fun goTo(cls: Class<*>) {
        startActivity(Intent(mActivity, cls))
    }

    private fun initBottom() {
        findViewById<View>(R.id.ic_past).setOnClickListener { goTo(CompleteOrdersActivity::class.java) }
        findViewById<View>(R.id.tvDelivered).setOnClickListener { goTo(CompleteOrdersActivity::class.java) }
        findViewById<View>(R.id.ic_recent).setOnClickListener { goTo(ActivityRecentOrder::class.java) }
        findViewById<View>(R.id.tvRecent).setOnClickListener { goTo(ActivityRecentOrder::class.java) }
        findViewById<View>(R.id.ic_account).setOnClickListener { goTo(ActivityAccount::class.java) }
        findViewById<View>(R.id.tvAccount).setOnClickListener { goTo(ActivityAccount::class.java) }
        (findViewById<View>(R.id.tvhome) as TextView).setTextColor(
            ContextCompat.getColor(
                mActivity!!, R.color.red_theme_color
            )
        )
        findViewById<View>(R.id.help).setOnClickListener {
            makePhoneCall(appPreferences.getString("HELP"))
            //                gotoGPay(mActivity);
        }

        /* findViewById(R.id.llCod).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mActivity, CompleteOrdersActivity.class).putExtra("IS_COD", true));
            }
        });*/
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Utility.TEZ_REQUEST_CODE) {
            // Process based on the data in response.
            Log.e("TEZ_REQUEST_CODE::", "RESULT::" + data!!.getStringExtra("Status"))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)
       // setContentView(R.layout.activity_main)
        mActivity = this
        try {
            initBottom()
            /*tvUserName = findViewById(R.id.tvUserName)
            noorder = findViewById(R.id.noorder)*/
            /*rvPast = findViewById(R.id.rv_past);
            rvCOD = findViewById(R.id.rv_cod);

            txt_assigned_order = findViewById(R.id.txt_assigned_order);
            txt_delivered_order = findViewById(R.id.txt_delivered_order);
            txt_cod_amount = findViewById(R.id.txt_cod_amount);
            txt_todays_earning = findViewById(R.id.txt_todays_earning);
            txt_weekly_earning = findViewById(R.id.txt_weekly_earning);

            spDateFilter = findViewById(R.id.spDateFilter);*/initData()
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    private fun initData() {
        binding!!.tvUserName.text = String.format("Welcome %s", appPreferences.getString("USERNAME"))
        /* rvCOD!!.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
         rvPast!!.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))*/
    }

    override fun onResume() {
        super.onResume()
        getGroupDashboardData()
        //dateFilter
    }

    private fun getDashboardData(searchDate: String) {
        try {
            if (!Utility.isNetworkAvailable(this)) {
                showInfoMsg("please check internet connection")
            } else {
                Utility.showProgress(this)
                val client = WebServiceCaller.getClient()
                val responseCall =
                    client.getDashboard(appPreferences.getString("USERID"), searchDate)
                responseCall.enqueue(object : Callback<DashboardResponse?> {
                    override fun onResponse(
                        call: Call<DashboardResponse?>,
                        response: Response<DashboardResponse?>
                    ) {
                        Utility.hideProgress()
                        if (response.isSuccessful && response.body() != null) {
                            val dashboardResponse = response.body()
                            if (dashboardResponse!!.isValid) {
                                val dashboard = dashboardResponse.dashboard
                                binding!!.txtCodAmount.text =
                                    Utility.getCurrencyAmount(dashboard.totalCOD)
                                binding!!.txtTodaysEarning!!.text =
                                    Utility.getCurrencyAmount(dashboard.todayEarning)
                                binding!!.txtTodaysEarning!!.text =
                                    Utility.getCurrencyAmount(dashboard.weeklyEarning)
                                binding!!.txtAssignedOrder!!.text = dashboard.totalAllotedOrders
                                binding!!.txtDeliveredOrder!!.text = dashboard.totalDeliveredOrders

                                /* if (notNullEmpty(dashboard.codOrders)) {
                                    mAdapter = new DashboardAdapter(mActivity, dashboard.codOrders);
                                    rvCOD.setAdapter(mAdapter);
                                }*/if (Utility.notNullEmpty(dashboard.weeklyOrders)) {
                                    binding!!.rvPast.visibility = View.VISIBLE
                                    pastAdapter =
                                        DashboardPastAdapter(mActivity, dashboard.weeklyOrders)
                                    binding!!.rvPast.setAdapter(pastAdapter)
                                } else {
                                    binding!!.rvPast.visibility = View.GONE
                                }
                            } else {
                                showInfoMsg(dashboardResponse.ResponseMsg)
                            }
                        } else {
                            showInfoMsg(response.message())
                        }
                    }

                    override fun onFailure(call: Call<DashboardResponse?>, t: Throwable) {
                        Utility.hideProgress()
                        showErrorMsg("please contact admin")
                    }
                })
            }
        } catch (ex: Exception) {
            showInfoMsg(ex.toString())
            sendMail(
                """
    ${PasOrderAPIError}User ID = ${appPreferences.getString("USERID")}
    Error = $ex
    """.trimIndent()
            )
        }
    }



    private val dateFilter: Unit
        private get() {
            try {
                if (!Utility.isNetworkAvailable(this)) {
                    showInfoMsg("please check internet connection")
                } else {
                    Utility.showProgress(this)
                    val client = WebServiceCaller.getClient()
                    val responseCall = client.getDateFilter()
                    responseCall.enqueue(object : Callback<DateFilterResponse?> {
                        override fun onResponse(
                            call: Call<DateFilterResponse?>,
                            response: Response<DateFilterResponse?>
                        ) {
                            Utility.hideProgress()
                            if (response.isSuccessful && response.body() != null) {
                                val dashboardResponse = response.body()
                                if (dashboardResponse!!.isValid) {
                                    if (Utility.notNullEmpty(dashboardResponse.arrayList)) {
//                                    getDashboardData(dashboardResponse.arrayList.get(0).searchDate);
//                                    dashboardResponse.arrayList.add(0, new DateFilterInfo("Select Delivery Time"));
                                        spDateFilter!!.setAdapter(
                                            SpinnerAdapter(
                                                mActivity!!,
                                                R.layout.row,
                                                dashboardResponse.arrayList
                                            )
                                        )
                                        spDateFilter!!.onItemSelectedListener =
                                            object : OnItemSelectedListener {
                                                override fun onItemSelected(
                                                    adapterView: AdapterView<*>?,
                                                    view: View,
                                                    i: Int,
                                                    l: Long
                                                ) {
                                                    val filterInfo =
                                                        spDateFilter!!.getSelectedItem() as DateFilterInfo
                                                    getDashboardData(filterInfo.searchDate)
                                                }

                                                override fun onNothingSelected(adapterView: AdapterView<*>?) {}
                                            }
                                        spDateFilter!!.setSelection(0)
                                    }
                                } else {
                                    Utility.hideProgress()
                                    showInfoMsg(dashboardResponse.ResponseMsg)
                                }
                            } else {
                                Utility.hideProgress()
                                showInfoMsg(response.message())
                            }
                        }

                        override fun onFailure(call: Call<DateFilterResponse?>, t: Throwable) {
                            Utility.hideProgress()
                            showErrorMsg("please contact admin")
                        }
                    })
                }
            } catch (ex: Exception) {
                showInfoMsg(ex.toString())
                sendMail(
                    """
    ${PasOrderAPIError}User ID = ${appPreferences.getString("USERID")}
    Error = $ex
    """.trimIndent()
                )
            }
        }

    private fun getGroupDashboardData() {
        try {
            if (!Utility.isNetworkAvailable(this)) {
                showInfoMsg("please check internet connection")
            } else {
                Utility.showProgress(this)
                val client = WebServiceCaller.getClient()
                val responseCall =
                    client.getGroupDashboard(appPreferences.getString("USERID"))
                responseCall.enqueue(object : Callback<GroupOrder?> {
                    override fun onResponse(
                        call: Call<GroupOrder?>,
                        response: Response<GroupOrder?>
                    ) {
                        Utility.hideProgress()
                        if (response.isSuccessful && response.body() != null) {
                            val dashboardResponse = response.body()
                            dashboardResponse?.GroupOrders
                            groupOrderList.clear()
                            dashboardResponse?.GroupOrders?.let { groupOrderList.addAll(it) }
                            initRecyclerView()
                        } else {
                            showInfoMsg(response.message())
                        }
                    }

                    override fun onFailure(call: Call<GroupOrder?>, t: Throwable) {
                        Utility.hideProgress()
                        showErrorMsg("please contact admin")
                    }
                })
            }
        } catch (ex: Exception) {
            showInfoMsg(ex.toString())
            sendMail(
                """
    ${PasOrderAPIError}User ID = ${appPreferences.getString("USERID")}
    Error = $ex
    """.trimIndent()
            )
        }
    }
    @SuppressLint("NotifyDataSetChanged")
    private fun initRecyclerView() {
        binding?.apply {
            groupOrderAdapter = GroupOrderPickAdapter(this@MainActivity, groupOrderList) { view, model, position ->
                when (view.id) {
                    R.id.cl_main->{
                        startActivity(GroupOrderListActivity.newInstance(this@MainActivity,model))
                    }
                }
            }
            rvGroupAddress.layoutManager = LinearLayoutManager(this@MainActivity)
            val dividerItemDecoration: RecyclerView.ItemDecoration = DividerItemDecorator(ContextCompat.getDrawable(this@MainActivity, R.drawable.divider)!!)
            rvGroupAddress.addItemDecoration(dividerItemDecoration)
            rvGroupAddress.adapter = groupOrderAdapter
        }
    }
}
