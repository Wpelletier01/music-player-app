package com.example.mplayer_rando.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableLayout
import android.widget.TableRow
import com.example.mplayer_rando.R
import com.example.mplayer_rando.backend.Artist
import com.example.mplayer_rando.item.AlbumItem
import com.example.mplayer_rando.item.ArtistItem


class ArtistFragment(artists: MutableList<Artist>) : Fragment() {

    var artists = artists

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            artists
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.artists, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val table = view.findViewById<TableLayout>(R.id.artistTable)

        Log.d("AppMainUi","")

        var rowCtn = 0
        var row = TableRow(context)

        for (artist in artists) {

            if (rowCtn >= 2) {
                table.addView(row)
                row = TableRow(context)
                rowCtn = 0
            }

            row.addView(ArtistItem(requireContext(),artist = artist.name))
            rowCtn++
        }


    }


}