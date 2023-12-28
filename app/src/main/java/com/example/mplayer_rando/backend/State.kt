package com.example.mplayer_rando.backend

import android.content.Context
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.util.Log
import java.io.File


class State(context: Context) {

    var albums = mutableListOf<Album>()
    var artists = mutableListOf<Artist>()
    var tracks = mutableListOf<Track>()
    val db = DatabaseHandler(context)

    fun clearState() {
        albums.clear()
        artists.clear()
        tracks.clear()
    }

    fun loadContentFromStorage(context:Context,directories: List<String>) {

        this.clearState()

        val contents = findFileInDirs(directories)

        Log.d("AppMainUi", "file found: ${contents.size}")

        val audioFiles = contents.filter { file: File -> AUDIO_EXT_FILE.contains(file.extension) }

        val mmr = MediaMetadataRetriever()

        for (audio in audioFiles) {

            Log.d("MainAppUi", "process ${audio.absolutePath}")

            if (!db.trackExist(audio.absolutePath)) {

                mmr.setDataSource(audio.absolutePath)

                var albumId: Int? = null
                var artistId: Int? = null

                var title = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE)

                if (title == null) {
                    title = "Unknown"
                }

                val albumName = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM)
                val artistName = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST)
                val cdStr = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DISC_NUMBER)
                val tNumberStr = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_CD_TRACK_NUMBER)
                var tnum: Int? = null
                var cd: Int? = null

                if (tNumberStr != null ) {
                    tnum = Integer.parseInt(tNumberStr.split("/").first())
                }

                if (cdStr != null) {

                    cd = Integer.parseInt(cdStr)
                }

                val albumCover = mmr.embeddedPicture


                if (artistName != null) {

                    artistId = db.getArtistIdByName(artistName)

                    if (artistId == null) {
                        db.addArtist(artistName)
                        artistId = db.getArtistIdByName(artistName)
                    }

                }


                if (albumName != null) {

                    albumId = db.getAlbumIdByTitle(albumName)

                    if (albumId == null) {

                        db.addAlbum(albumName,albumCover, artistId)
                        albumId = db.getAlbumIdByTitle(albumName)

                    }

                }

                db.addTrack(title,tnum, cd, audio.absolutePath,albumId,artistId)

            } else {
                Log.d("AppMainUi", "track already stored in db: ${audio.absolutePath}")
            }
        }
    }

    fun loadFromDb() {
        tracks = db.getTracks()
        albums = db.getAlbums()
        artists = db.getArtists()
    }


    fun dbIsEmpty() : Boolean { return (tracks.isEmpty() && albums.isEmpty() && artists.isEmpty()) }

    fun updateDb(context: Context) {

        loadContentFromStorage(context,listOf("/storage/emulated/0/Music/"))

        Log.d("AppMainUi", "Content Found:")
        debugLog()

        val dbTracks = db.getTracks()
        val dbArtists = db.getArtists()
        val dbAlbums = db.getAlbums()

        Log.d("AppMainUi", "Database Entry:")

        dbTracks.forEach { Log.d("AppMainUi", "$it") }
        dbAlbums.forEach { Log.d("AppMainUi", "$it") }
        dbArtists.forEach { Log.d("AppMainUi", "$it") }

        // new elements found

        // rm deleted element
        // TODO: rm deleted element

        clearState()
    }


    fun getArtistEntry() : MutableList<String> {

        val artistEntry = mutableListOf<String>()

        artists.forEach { artist: Artist -> artistEntry.add(artist.name)  }

        return artistEntry

    }

    fun getTracksOfAlbum(album: String) : List<List<Track>>? {

        val id = db.getAlbumIdByTitle(album) ?: return null

        val tracks = db.getTracksByAlbumId(id)
        val trackByCd = mutableListOf<MutableList<Track>>()

        if (tracks.isNotEmpty()) {
            var found = false
            for (track in tracks) {

                found = false

                for (cdTrack in trackByCd) {
                    if (cdTrack.first().cd == track.cd) {
                        found = true
                        cdTrack.add(track)
                        break
                    }
                }

                if (!found) {
                    trackByCd.add(mutableListOf<Track>(track))
                }

            }

        }

        return trackByCd
    }



    fun debugLog() {
        tracks.forEach { Log.d("AppMainUi", "track: $it") }
        albums.forEach { Log.d("AppMainUi","album: $it") }
        artists.forEach { Log.d("AppMainUi", "artist: $it") }
    }

}
