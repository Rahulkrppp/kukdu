package com.kukdudelivery

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kukdudelivery.Adapter.OrderDetailsListAdapter
import com.kukdudelivery.Model.GroupOrderX
import com.kukdudelivery.databinding.ActivityGroupOrderListBinding
import com.kukdudelivery.util.DividerItemDecorator
import com.kukdudelivery.util.parcelable


class GroupOrderListActivity : BaseActivity() {
    private var binding:ActivityGroupOrderListBinding?=null
    var orderList:GroupOrderX ? = null
    var orderDetailsListAdapter:OrderDetailsListAdapter?= null

    companion object {
        const val ORDER_LIST = "order"


        fun newInstance(activity: Activity,orderList: GroupOrderX) = Intent(activity, GroupOrderListActivity::class.java).apply {
            putExtra(ORDER_LIST, orderList)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGroupOrderListBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        initCompontent()

    }

    private fun initCompontent() {
            orderList = intent.parcelable(ORDER_LIST)
        Log.e("", "initCompontent:$orderList", )
        initRecyclerView()
        binding!!.tvBack.setOnClickListener {
            finish()
        }
        binding!!.tvItems.text="#${orderList?.group_order_id}"
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initRecyclerView() {
        binding?.apply {
            orderDetailsListAdapter = OrderDetailsListAdapter(this@GroupOrderListActivity, orderList!!.orders) { view, model, position ->
                when (view.id) {

                }
            }
            rv.layoutManager = LinearLayoutManager(this@GroupOrderListActivity)
            /*val dividerItemDecoration: RecyclerView.ItemDecoration = DividerItemDecorator(
                ContextCompat.getDrawable(this@GroupOrderListActivity, R.drawable.divider)!!)
            rv.addItemDecoration(dividerItemDecoration)*/
            rv.adapter = orderDetailsListAdapter
        }
    }
}