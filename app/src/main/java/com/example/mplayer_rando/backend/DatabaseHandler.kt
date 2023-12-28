package com.example.mplayer_rando.backend

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.net.Uri
import androidx.core.net.toUri

private val TABLES_NAME : Array<String> = arrayOf("tracks", "albums", "artist")
private val DATABASE_NAME = "content.db"
private val DATABASE_VERSION = 1
private val TRACK_TABLE =
    """
    CREATE TABLE tracks (
        id          INTEGER PRIMARY KEY AUTOINCREMENT,
        tnumber     int,
        cd          int,
        title       varchar(255),
        uri         varchar(510),
        album       int,
        artist      int
    )    
    """.trimIndent()

private val ALBUM_TABLE =
    """
    CREATE TABLE albums (
        id      INTEGER  PRIMARY KEY AUTOINCREMENT,
        title   varchar(255),
        artist  int,
        cover   blob
    )   
    """.trimIndent()

private val ARTIST_TABLE =
    """
    CREATE TABLE artists (
        id      INTEGER PRIMARY KEY AUTOINCREMENT,
        name    varchar(255)
    )
    """.trimIndent()


class DbException(message: String) : Exception(message) {}


class DatabaseHandler(context:Context) :
    SQLiteOpenHelper(context, DATABASE_NAME,null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(ALBUM_TABLE)
        db?.execSQL(ARTIST_TABLE)
        db?.execSQL(TRACK_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("Not yet implemented")
    }

    fun getTracks() : MutableList<Track> {
        val tracks = mutableListOf<Track>()

        val db = this.readableDatabase
        val query = "SELECT * FROM tracks"
        val cursor = db.rawQuery(query,null)

        if (cursor.moveToFirst()) {

            do {

                val id = cursor.getInt(0)
                val tnumber = cursor.getInt(1)
                val cd = cursor.getInt(2)
                val name  = cursor.getString(3)
                val uri = cursor.getString(4)
                val albumId = cursor.getInt(5)
                val artistId = cursor.getInt(6)
                val album = this.getAlbumTitleById(albumId)
                val artist = this.getArtistNameById(artistId)

                tracks.add(Track(id,tnumber, cd, name, uri, album, artist))

            } while (cursor.moveToNext())

        }

        return tracks
    }

    fun getAlbums() : MutableList<Album> {

        val albums = mutableListOf<Album>()

        val db = this.readableDatabase
        val query = "Select * FROM albums"
        val cursor = db.rawQuery(query,null)

        if (cursor.moveToFirst()) {
            do {

                val id = cursor.getInt(0)
                val title = cursor.getString(1)
                val artistId = cursor.getInt(2)
                val cover =  cursor.getBlob(3)

                val artist = getArtistNameById(artistId)

                albums.add(Album(id,title,artist,cover))

            } while (cursor.moveToNext())
        }

        return albums
    }

    fun getArtists() : MutableList<Artist> {
        val artists = mutableListOf<Artist>()

        val db = this.readableDatabase
        val query = "Select * FROM artists"
        val cursor = db.rawQuery(query,null)

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(0)
                val name = cursor.getString(1)

                artists.add(Artist(id,name))

            } while (cursor.moveToNext())
        }

        return artists
    }


    fun addTrack(title: String, tnumber: Int?, cd: Int?, uri: String, album: Int?, artist: Int?) {

        val db = this.writableDatabase

        val row = ContentValues()

        row.put("title", title)
        row.put("tnumber", tnumber)
        row.put("cd",  cd)
        row.put("uri", uri.toString())
        row.put("album", album)
        row.put("artist", artist)

        db.insert("tracks",null, row)

        db.close()
    }

    fun addAlbum(title: String, cover: ByteArray?, artist: Int?) {

        val db = this.writableDatabase

        val row = ContentValues()

        row.put("title", title)
        row.put("cover", cover)
        row.put("artist", artist)

        db.insert("albums", null, row)

        db.close()

    }

    fun addArtist(name: String) {
        val db = this.writableDatabase
        val row = ContentValues()

        row.put("name", name)

        db.insert("artists",null,row)

        db.close()
    }


    fun rmRow(elemId: Int, table: String) {

        if (!TABLES_NAME.contains(table)) {
            throw DbException("Try to remove element in a table that don't exist: $table")
        }

        val db = this.writableDatabase

        var nbRow = db.delete("tracks","id = ?", arrayOf("$elemId"))

        if (nbRow < 1) {
            throw DbException("Try to delete a track that dont exist: track id '$elemId'")
        }

        db.close()
    }

    fun getAlbumTitleById(id: Int) : String? {
        var query = "SELECT title FROM albums WHERE id = ?"

        val db = this.readableDatabase

        val cursor = db.rawQuery(query, arrayOf(id.toString()))

        if ( cursor == null || cursor.count != 1 ) {
            return null
        }

        cursor.moveToFirst()

        val result = cursor.getString(0)

        cursor.close()

        return result
    }

    fun getArtistNameById(id: Int) : String? {
        var query = "SELECT name FROM artists WHERE id = ?"
        val db = this.readableDatabase
        val cursor = db.rawQuery(query, arrayOf(id.toString()))

        if ( cursor == null || cursor.count != 1 ) {
            return null
        }

        cursor.moveToFirst()

        val result = cursor.getString(0)

        cursor.close()

        return result

    }

    fun getArtistIdByName(name: String) : Int? {
        var query = "SELECT id FROM artists WHERE name = ?"
        val db = this.readableDatabase
        val cursor = db.rawQuery(query, arrayOf(name))

        if ( cursor == null || cursor.count != 1 ) {
            return null
        }

        cursor.moveToFirst()
        val result =  cursor.getInt(0)

        cursor.close()

        return result
    }

    fun getAlbumIdByTitle(title: String) : Int? {
        var query = "SELECT id FROM albums WHERE title = ?"
        val db = this.readableDatabase
        val cursor = db.rawQuery(query, arrayOf(title))

        if ( cursor == null || cursor.count != 1 ) {
            return null
        }

        cursor.moveToFirst()
        val result = cursor.getInt(0)

        cursor.close()

        return result
    }

    fun getNbArtist() : Int {

        var query = "SELECT Count(id) FROM artists"
        val db = this.readableDatabase
        val cursor = db.rawQuery(query, null)

        if ( cursor == null) {
            throw DbException("Unable to create a cursor from the total of artist")
        }

        cursor.moveToFirst()
        val result = cursor.getInt(0)

        cursor.close()

        return result

    }

    fun trackExist(uri: String) : Boolean {
        var query = "SELECT title FROM tracks WHERE uri = ?"
        val db = this.readableDatabase
        val cursor = db.rawQuery(query, arrayOf(uri))
            ?: throw DbException("Unable to create an cursor for track validation")

        val count = cursor.count

        cursor.close()

        return (count > 0)
    }

    fun getTracksByAlbumId(id: Int) : List<Track> {

        val tracks = mutableListOf<Track>()

        val query = "SELECT * FROM tracks WHERE album = ?"
        val db = this.readableDatabase
        val cursor = db.rawQuery(query, arrayOf(id.toString()))

        if (cursor.moveToFirst()) {

            do {

                val tid = cursor.getInt(0)
                val tnumber = cursor.getInt(1)
                val cd = cursor.getInt(2)
                val name  = cursor.getString(3)
                val uri = cursor.getString(4)
                val albumId = cursor.getInt(5)
                val artistId = cursor.getInt(6)

                val album = this.getAlbumTitleById(albumId)
                val artist = this.getArtistNameById(artistId)

                tracks.add(Track(tid,tnumber, cd, name, uri, album, artist))

            } while (cursor.moveToNext())

        }

        return tracks
    }

}