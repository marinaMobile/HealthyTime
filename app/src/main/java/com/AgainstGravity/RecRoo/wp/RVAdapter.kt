package com.AgainstGravity.RecRoo.wp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.AgainstGravity.RecRoo.R

class RVAdapter(private val data:List<RVDataClass>) : RecyclerView.Adapter<RVAdapter.CustomViewHolder>() {

    class CustomViewHolder(parent: View): RecyclerView.ViewHolder(parent) {
        val img: ImageView = parent.findViewById(R.id.custom_img)
        val txt: TextView = parent.findViewById(R.id.custom_title)
        val desc: TextView = parent.findViewById(R.id.custom_text)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        return CustomViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_layout,parent,false))
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.img.setImageResource(data[position].img)
        holder.txt.text = data[position].txt
        holder.desc.text = data[position].desc
    }

    override fun getItemCount(): Int = data.count()
}