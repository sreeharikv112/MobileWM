package com.wmapp.ui.utility

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.wmapp.R
import com.wmapp.ui.cardetail.models.CarDetailGridItem

class CarDetailAdapter(var context: Context ,
                       val listItem: List<CarDetailGridItem>
                       ) : RecyclerView.Adapter<CarDetailAdapter.CarDetailViewHolder>(){

    private var mCardDetailList : List<CarDetailGridItem>

    init {
        mCardDetailList = listItem
    }

    fun addListItems(listItem:List<CarDetailGridItem>){
        this.mCardDetailList = listItem
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarDetailViewHolder {
        return  CarDetailViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.grid_item_view, parent, false))
    }

    override fun getItemCount(): Int {
        return mCardDetailList.size
    }

    override fun onBindViewHolder(holder: CarDetailViewHolder, position: Int) {
        val model: CarDetailGridItem = mCardDetailList[position]
        holder.detailKey.text = model.carDetailKey
        holder.detailValue.text = model.carDetailValue
    }


    class CarDetailViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var detailKey : TextView = itemView.findViewById(R.id.key_car_detail)
        var detailValue : TextView = itemView.findViewById(R.id.value_car_detail)
    }
}