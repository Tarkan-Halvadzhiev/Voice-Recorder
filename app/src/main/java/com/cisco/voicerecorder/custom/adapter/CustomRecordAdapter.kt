package com.cisco.voicerecorder.custom.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.cisco.voicerecorder.R
import java.io.File

class CustomRecordAdapter(context: Context, list: List<File>?) : BaseAdapter() {

    private var context: Context? = context
    private var list: List<File>? = list

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        val layoutInflater: LayoutInflater =  LayoutInflater.from(context)
        val voiceRecord = layoutInflater.inflate(R.layout.custom_record_item, parent, false)

        val textView = voiceRecord.findViewById<TextView>(R.id.song_name)
        textView.text = list?.get(position)?.name
        return voiceRecord
    }

    override fun getItem(position: Int): File? {
        return list?.get(position)
    }

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getCount(): Int = list?.size ?: 0

}