package com.example.mplayer_rando.item

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import com.example.mplayer_rando.R

class ArtistItem @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0,
    artist:String
) : LinearLayout(context, attrs, defStyleAttr) {

    init {
        LayoutInflater.from(context).inflate(R.layout.artist_item, this, true)

        val name = findViewById<TextView>(R.id.artistName)
        name.text = artist



    }

}