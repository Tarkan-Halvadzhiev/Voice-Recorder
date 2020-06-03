package com.cisco.voicerecorder.custom.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import com.cisco.voicerecorder.R
import com.cisco.voicerecorder.RecordLibrary
import java.io.File

class CustomRecordAdapter(context: Context, list: List<File>?) : BaseAdapter() {

    private val context: Context? = context
    private var records: List<File>? = list
    private var recordLibrary: RecordLibrary = RecordLibrary()

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        val layoutInflater: LayoutInflater = LayoutInflater.from(context)
        val voiceRecord = layoutInflater.inflate(R.layout.custom_record_item, parent, false)

        val textView = voiceRecord.findViewById<TextView>(R.id.song_name)
        val fileName: String? = records?.get(position)?.name
        textView.text = fileName

        val startPlaying = voiceRecord.findViewById<ImageView>(R.id.play_button)
        val stopPlaying = voiceRecord.findViewById<ImageView>(R.id.stop_button)

        val seekBar = voiceRecord.findViewById<SeekBar>(R.id.seek_bar)

        startPlayingAudioEventListener(startPlaying, stopPlaying, fileName, seekBar)
        stopPlayingEventListener(stopPlaying, startPlaying, seekBar)

        val picture = voiceRecord.findViewById<ImageView>(R.id.image_view)

        //Functionality is not fully working
        //deleteMediaFileEventListener(picture, fileName)

        return voiceRecord
    }

    private fun deleteMediaFileEventListener(
        picture: ImageView,
        fileName: String?
    ) {
        recordLibrary.deleteMediaFileEventListener(picture, fileName)
    }

    private fun stopPlayingEventListener(
        stopPlaying: ImageView,
        startPlaying: ImageView,
        seekBar: SeekBar
    ) {
        stopPlaying.setOnClickListener {
            startPlaying.visibility = View.VISIBLE
            stopPlaying.visibility = View.INVISIBLE
            recordLibrary.stopAudio()
            seekBar.progress = 0
        }
    }

    private fun startPlayingAudioEventListener(
        startPlaying: ImageView,
        stopPlaying: ImageView,
        fileName: String?,
        seekBar: SeekBar
    ) {
        startPlaying.setOnClickListener {
            startPlaying.visibility = View.INVISIBLE
            stopPlaying.visibility = View.VISIBLE
            recordLibrary.startAudio(stopPlaying, startPlaying, fileName, seekBar)
        }
    }

    override fun getItem(position: Int): File? {
        return records?.get(position)
    }

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getCount(): Int = records?.size ?: 0

}