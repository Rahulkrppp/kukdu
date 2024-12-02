package com.kukdudelivery.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kukdudelivery.Model.Order
import com.kukdudelivery.databinding.ItemOrderListBinding

class OrderDetailsListAdapter (var context: Context, var list: ArrayList<Order>,
                               var clickListener: (view: View, model: Order, position: Int) -> Unit = { _: View, _: Order, _: Int -> }
) : RecyclerView.Adapter<OrderDetailsListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemOrderListBinding.inflate(
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
            OrderStatus.text=item.payment_status
            OrderDate.text=item.order_date
            OrderNo.text="#${item.order_id}"
            OrderAmount.text="₹ ${item.item_total}"
            VendorName.text=item.vendor_name
            VendorAddress.text=item.customer_address
            txtArea.text=item.shipping_area
            VendorMobile.text=item.vendor_mobile
            DeliverySlot.text="${item.delivery_time}(${item.delivery_date})"
            Status.text=item.order_status
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(val itemBinding: ItemOrderListBinding) :
        RecyclerView.ViewHolder(itemBinding.root)
}