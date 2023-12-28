package com.example.mplayer_rando.item

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import com.example.mplayer_rando.R

class TrackItem @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0,
    title:String,
    album:String?,
    artist:String?
) : LinearLayout(context, attrs, defStyleAttr) {

    val title = title
    val album = album
    val artist = artist
    var description : String = "Unknown | Unknown"

    init {
        LayoutInflater.from(context).inflate(R.layout.track_item, this, true)

        val title_textv = findViewById<TextView>(R.id.track_title) as TextView
        val desc_textv = findViewById<TextView>(R.id.track_description) as TextView

        if (album != null && artist == null) {
            description = "$album | Unknown"
        } else if (album == null && artist != null) {
            description = "Unknown | $artist"
        } else if (album != null) {
            description = "$album | $artist"
        }


        title_textv.text = this.title
        desc_textv.text = this.description



    }



}