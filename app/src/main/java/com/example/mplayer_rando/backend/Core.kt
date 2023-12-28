package com.example.mplayer_rando.backend

import android.net.Uri
import java.io.File

// couple of file extension support for audio file that can be listed here:
// https://developer.android.com/guide/topics/media/platform/supported-formats
val AUDIO_EXT_FILE: Array<String> = arrayOf(
    "3gp",
    "mp4",
    "m4a",
    "flac",
    "mp3",
    "wav"
)

// supported for now
val IMAGE_EXT_FILE: Array<String> = arrayOf(
    "png",
    "jpeg",
    "webp"
)


// directories must be valid directory no check are made
fun findFileInDirs(directories: List<String>) : List<File> {

    val files = mutableListOf<File>();

    for (directory in directories) {
        val directoriesToVisit = mutableListOf<String>()

        directoriesToVisit.add(directory)

        while (directoriesToVisit.isNotEmpty()) {

            val contents = File(directoriesToVisit.first()).listFiles()

            for (content in contents!!) {

                if (content.isFile) {
                    files.add(content)
                } else if (content.isDirectory) {
                    directoriesToVisit.add(content.absolutePath)
                }

            }

            directoriesToVisit.removeAt(0)
        }
    }

    return files
}


enum class PlayerAction {
    BACK,
}


interface OnTrackPress {
    fun onTrackPressed(title: String, description: String)
}

interface OnPlayerAction {
    fun onPlayerAction(action: PlayerAction)
}

interface OnAlbumPress {
    fun onAlbumPressed(album: Album)
}