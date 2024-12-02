package com.kukdudelivery.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kukdudelivery.Model.Order
import com.kukdudelivery.Model.OrderPickupAddres
import com.kukdudelivery.databinding.ItemOrderAddressBinding

class GroupOderChildAdapter (var context: Context, var list: ArrayList<OrderPickupAddres>,
                             var clickListener: (view: View, model: OrderPickupAddres, position: Int) -> Unit = { _: View, _: OrderPickupAddres, _: Int -> }
) : RecyclerView.Adapter<GroupOderChildAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemOrderAddressBinding.inflate(
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
            VendorName.text=item.vendor_name
            VendorAddress.text=item.vendor_address
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(val itemBinding: ItemOrderAddressBinding) :
        RecyclerView.ViewHolder(itemBinding.root)
}