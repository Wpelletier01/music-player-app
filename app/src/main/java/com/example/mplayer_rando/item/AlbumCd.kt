package com.example.mplayer_rando.item

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import com.example.mplayer_rando.R

class AlbumCd @kotlin.jvm.JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0,
    cd: Int
) : LinearLayout(context, attrs, defStyleAttr) {

    init {
        LayoutInflater.from(context).inflate(R.layout.album_cd_item, this, true)

        val atitle = findViewById<TextView>(R.id.cd)
        val cdText = "CD: $cd"

        atitle.text = cdText


    }

}