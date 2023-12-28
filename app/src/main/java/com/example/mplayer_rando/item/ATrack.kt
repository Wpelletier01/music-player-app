package com.example.mplayer_rando.item

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import com.example.mplayer_rando.R

class ATrack @kotlin.jvm.JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0,
    title:String,
    tnum: Int?
) : LinearLayout(context, attrs, defStyleAttr) {

    init {
        LayoutInflater.from(context).inflate(R.layout.album_track_item, this, true)

        val atitle = findViewById<TextView>(R.id.aTrackName)
        val tnumber = findViewById<TextView>(R.id.trackn)

        atitle.text = title
        tnumber.text = tnum.toString()


    }

}