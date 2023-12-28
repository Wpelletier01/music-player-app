package com.example.mplayer_rando.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.example.mplayer_rando.R
import com.example.mplayer_rando.backend.OnTrackPress
import com.example.mplayer_rando.backend.Track
import com.example.mplayer_rando.item.TrackItem



class TrackFragment(tracks: List<Track>) : Fragment() {

    var tracks = tracks

    lateinit var dataPasser: OnTrackPress

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        arguments?.let {
            tracks
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {



        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.tracks, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val lcontainer = view.findViewById<LinearLayout>(R.id.track_list)

        tracks.forEach {

            val nlayout = TrackItem(
                requireContext(),
                title = it.title,
                album = it.artist,
                artist = it.artist
            )

            val textPart = nlayout.findViewById<LinearLayout>(R.id.trackText)

            textPart.setOnClickListener {
                if (it.isPressed) {
                    dataPasser.onTrackPressed(nlayout.title,nlayout.description)
                }
            }

            lcontainer.addView(nlayout)
        }


    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        dataPasser = context as OnTrackPress
    }


    fun setContent(ntracks: List<Track>) {
        tracks = ntracks
    }

}