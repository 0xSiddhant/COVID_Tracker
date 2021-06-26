package com.example.covidtrackerindia

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import kotlinx.android.synthetic.main.item_list.view.*

class StateAdaptor(val list: List<StatewiseItem>) : BaseAdapter() {
    override fun getCount(): Int = list.size

    override fun getItem(position: Int)  = list[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(parent?.context )
            .inflate(
                R.layout.item_list,
                parent,
                false
            )

        val item = list[position]
        view.confirmedTv.text = item.confirmed
        view.recoveredTv.text = item.recovered
        view.deceasedTv.text = item.deaths
        view.activeTv.text = item.active
        view.stateTv.text = item.state 
        return view
    }
}