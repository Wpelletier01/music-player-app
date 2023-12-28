package com.example.mplayer_rando.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.mplayer_rando.R
import com.example.mplayer_rando.backend.Album
import com.example.mplayer_rando.backend.Track
import com.example.mplayer_rando.item.ATrack
import com.example.mplayer_rando.item.AlbumCd


class AlbumContent : Fragment() {


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
        return inflater.inflate(R.layout.album_content, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

    fun setContent(album: Album, trackCds: List<List<Track>>?) {

        val name = this.view?.findViewById<TextView>(R.id.albumTitle)
        val artist = this.view?.findViewById<TextView>(R.id.artist)
        val trackList = this.view?.findViewById<LinearLayout>(R.id.tracks)

        if (name != null) {
            name.text = album.title
        }
        if (artist != null) {
            artist.text = album.artist
        }

        if (trackList != null && trackCds != null) {

            for ((cid,trackCd) in trackCds.sortedBy { tracks -> tracks.first().cd  }.withIndex()) {

                Log.d("AppMainUi","Add to CD # ${trackCd.first().cd}")

                trackList.addView(AlbumCd(this.requireContext(),cd = cid + 1))

                for (track in trackCd.sortedBy { track: Track -> track.tnumber }) {
                    Log.d("AppMainUi","add ${track.title} ")
                    trackList.addView(ATrack(this.requireContext(), title = track.title, tnum = track.tnumber))
                }
            }

        }


    }



}