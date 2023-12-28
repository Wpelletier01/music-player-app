package com.example.mplayer_rando.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import com.example.mplayer_rando.R
import com.example.mplayer_rando.backend.OnPlayerAction
import com.example.mplayer_rando.backend.PlayerAction


class PlayerFragment : Fragment() {

    lateinit var action: OnPlayerAction

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
        return inflater.inflate(R.layout.player, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val playBtn = view.findViewById<ImageButton>(R.id.play)
        val backBtn = view.findViewById<ImageButton>(R.id.playerBack)



        playBtn.setOnClickListener {
            if (it.isPressed) {
                Log.d("AppMainUi", "You press the play button")
            }
        }

        backBtn.setOnClickListener {
            if (it.isPressed) {
                action.onPlayerAction(PlayerAction.BACK)
            }
        }

    }

    fun setCurrentTrackData(title:String,description:String) {

        val textTitle = this.view?.findViewById<TextView>(R.id.playerTitle)
        val textDescription = this.view?.findViewById<TextView>(R.id.playDescription)

        if (textTitle != null) {
            textTitle.text = title
        }

        if (textDescription != null) {
            textDescription.text = description
        }

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        action = context as OnPlayerAction

    }



}