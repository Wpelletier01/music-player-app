package com.example.mplayer_rando.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TableLayout
import android.widget.TableRow
import com.example.mplayer_rando.R
import com.example.mplayer_rando.backend.Album
import com.example.mplayer_rando.backend.OnAlbumPress
import com.example.mplayer_rando.item.AlbumItem
import com.example.mplayer_rando.item.ArtistItem


class AlbumFragment(albumInfo:MutableList<Album>) : Fragment() {

    var albumInfo =  albumInfo

    lateinit var dataPasser : OnAlbumPress

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.albums, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val table = view.findViewById<TableLayout>(R.id.albumTable)
        var rowCtn = 0
        var row = TableRow(context)

        for (album in albumInfo) {

            if (rowCtn >= 2) {
                table.addView(row)
                row = TableRow(context)
                rowCtn = 0
            }

            var item = AlbumItem(requireContext(), title = album.title, artist = album.artist)
            var img = item.findViewById<ImageView>(R.id.albumCover)

            img.setOnClickListener {
                if (it.isPressed) {
                    Log.d("AppMainUi", "you pressed an album")
                    dataPasser.onAlbumPressed(album)
                }
            }
            row.addView(item)
            rowCtn++

        }

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        dataPasser = context as OnAlbumPress
    }


}