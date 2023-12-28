package com.example.mplayer_rando.backend

import android.graphics.Bitmap
import android.net.Uri
import java.io.File


// Database Row Entry

data class Album (
    val id:         Int,
    val title:      String,
    val artist:     String?,
    // if no cover image, it will have the empty one
    val cover:      ByteArray?
)

data class Artist (
    val id:         Int,
    val name:       String
)

data class Track (
    val id:         Int,
    val tnumber:    Int?,
    val cd:         Int?,
    val title:      String,
    val fileUri:    String,
    val album:      String?,
    val artist:     String?,
)



