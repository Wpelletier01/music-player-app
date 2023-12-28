package com.example.mplayer_rando.item

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import com.example.mplayer_rando.R


class AlbumItem @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0,
    title:String,
    artist:String?
) : LinearLayout(context, attrs, defStyleAttr) {


    init {
        LayoutInflater.from(context).inflate(R.layout.album_item,this,true)

        val albumText = findViewById<TextView>(R.id.albumTitle)
        val artistText = findViewById<TextView>(R.id.albumArtist)

        albumText.text = title

        if (artist != null) {
            artistText.text = artist
        } else {
            artistText.text = "Unknown"
        }


    }

}