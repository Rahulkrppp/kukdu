package com.kukdudelivery.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kukdudelivery.Model.GroupOrderX
import com.kukdudelivery.databinding.ItemOrderPickBinding

class GroupOrderPickAdapter(
    var context: Context, var list: ArrayList<GroupOrderX>,
    var clickListener: (view: View, model:GroupOrderX, position: Int) -> Unit = { _: View, _:GroupOrderX, _: Int -> }
) : RecyclerView.Adapter<GroupOrderPickAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemOrderPickBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setIsRecyclable(false)
        val item = list[holder.absoluteAdapterPosition]
        holder.itemBinding.apply {
            OrderStatus.text=item.group_order_status
            OrderNo.text="#${item.group_order_id}"
            OrderAmount.text="₹ ${item.order_delivery_charges_high}"
            OrderDate.text=item.order_distance
            clMain.setOnClickListener {
                clickListener(clMain,item,holder.absoluteAdapterPosition)
            }

                  val   groupOrderAdapter = GroupOderChildAdapter(context, item.order_pickup_address) { view, model, position ->
                        when (view.id) {

                        }
                    }
                    rvAddress.layoutManager = LinearLayoutManager(context)
            rvAddress.adapter = groupOrderAdapter
            }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(val itemBinding: ItemOrderPickBinding) :
        RecyclerView.ViewHolder(itemBinding.root)
}