package com.example.mplayer_rando

import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.mplayer_rando.backend.Album
import com.example.mplayer_rando.backend.OnAlbumPress
import com.example.mplayer_rando.backend.OnPlayerAction
import com.example.mplayer_rando.backend.OnTrackPress
import com.example.mplayer_rando.backend.PlayerAction
import com.example.mplayer_rando.backend.State
import com.example.mplayer_rando.fragment.AlbumContent
import com.example.mplayer_rando.fragment.AlbumFragment
import com.example.mplayer_rando.fragment.ArtistFragment
import com.example.mplayer_rando.fragment.PlayerFragment
import com.example.mplayer_rando.fragment.PlaylistFragment
import com.example.mplayer_rando.fragment.SettingsFragment
import com.example.mplayer_rando.fragment.TrackFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlin.system.exitProcess


class AppMainUI : AppCompatActivity(), OnTrackPress, OnPlayerAction, OnAlbumPress {

    lateinit var bnavigator: BottomNavigationView
    lateinit var swipeRefresh: SwipeRefreshLayout
    lateinit var active: Fragment
    var last:  Fragment? = null
    lateinit var playerFragment : PlayerFragment
    lateinit var trackFragment  : TrackFragment
    lateinit var albumFragment : AlbumFragment
    lateinit var artistFragment : ArtistFragment
    lateinit var playlistFragment : PlaylistFragment
    lateinit var settingsFragment : SettingsFragment
    lateinit var albumContent: AlbumContent
    lateinit var mPlayer : MediaPlayer

    val state = State(this)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_ui)

        state.loadFromDb()

        Log.d("AppMainUi","current db content:")
        state.debugLog()

        // fragment
        playerFragment = PlayerFragment()
        trackFragment = TrackFragment(state.tracks)
        albumFragment = AlbumFragment(state.albums)
        artistFragment = ArtistFragment(state.artists)
        playlistFragment = PlaylistFragment()
        settingsFragment = SettingsFragment()
        albumContent = AlbumContent()
        active = trackFragment


        initFragment()


        bnavigator = findViewById<BottomNavigationView>(R.id.navigator)

        bnavigator.setOnItemSelectedListener { navigateFragment(it.itemId) }


        swipeRefresh = findViewById<SwipeRefreshLayout>(R.id.swipeAction)

        swipeRefresh.setOnRefreshListener {
            Log.v("AppMainUi","database refresh trigger")

            clearFragment()
            refreshContent()

            when (active.tag!!) {
                "1" -> active = trackFragment
                "2" -> active = albumFragment
                "3" -> active = artistFragment
                "4" -> active = playlistFragment
                "5" -> active = settingsFragment
                "6" -> active = playerFragment
                "7" -> active = albumContent
            }

            active = trackFragment
            initFragment()

            swipeRefresh.isRefreshing = false
        }

        onBackPressedDispatcher.addCallback(this) {

            if (last == null) {
                finish()
                exitProcess(0)
            }


            supportFragmentManager
                .beginTransaction()
                .hide(active)
                .show(last!!)
                .commit()

            active = last!!
            last = null

            bnavigator.visibility = View.VISIBLE

        }
    }

    override fun onTrackPressed(title: String, description: String) {
        Log.d("AppMainUi", "you press: $title")

        bnavigator.visibility = View.GONE

        playerFragment.setCurrentTrackData(title,description)

        supportFragmentManager
            .beginTransaction()
            .hide(active)
            .show(playerFragment)
            .commit()

        last = active
        active = playerFragment

    }


    override fun onPlayerAction(action: PlayerAction) {
        when (action) {
            PlayerAction.BACK -> {
                bnavigator.visibility = View.VISIBLE

                supportFragmentManager
                    .beginTransaction()
                    .hide(active)
                    .show(last!!)
                    .commit()

                active = last!!
                last = null


            }
        }
    }

    override fun onAlbumPressed(album: Album) {
        // TODO: when we have no content in database and refresh it, here we still have nothing
        bnavigator.visibility = View.GONE
        albumContent.setContent(album,state.getTracksOfAlbum(album.title))

        supportFragmentManager
            .beginTransaction()
            .hide(active)
            .show(albumContent)
            .commit()

        last = active
        active = albumContent

    }

    fun refreshContent() {
        state.loadContentFromStorage(this,listOf("/storage/emulated/0/Music/"))
        state.loadFromDb()

        albumFragment = AlbumFragment(state.albums)
        trackFragment = TrackFragment(state.tracks)
        artistFragment = ArtistFragment(state.artists)

    }


    fun navigateFragment(itemId: Int) : Boolean {
        when (itemId) {
            R.id.track_page -> {
                supportFragmentManager
                    .beginTransaction()
                    .hide(active)
                    .show(trackFragment)
                    .commit()

                active = trackFragment
                last = null
            }
            R.id.album_page -> {
                supportFragmentManager
                    .beginTransaction()
                    .hide(active)
                    .show(albumFragment)
                    .commit()

                active = albumFragment
                last = null
            }
            R.id.artist_page -> {

                supportFragmentManager
                    .beginTransaction()
                    .hide(active)
                    .show(artistFragment)
                    .commit()

                active = artistFragment
                last = null

            }
            R.id.playlist_page -> {
                supportFragmentManager
                    .beginTransaction()
                    .hide(active)
                    .show(playlistFragment)
                    .commit()

                active = playlistFragment
                last = null
            }
            R.id.settings_page -> {
                supportFragmentManager
                    .beginTransaction()
                    .hide(active)
                    .show(settingsFragment)
                    .commit()

                active = settingsFragment
                last = null
            }
        }

        return true

    }

    fun clearFragment() {
        supportFragmentManager
            .beginTransaction()
            .hide(trackFragment)
            .hide(albumFragment)
            .hide(artistFragment)
            .hide(playlistFragment)
            .hide(settingsFragment)
            .hide(playerFragment)
            .hide(albumContent)
            .remove(trackFragment)
            .remove(albumFragment)
            .remove(artistFragment)
            .remove(playlistFragment)
            .remove(settingsFragment)
            .remove(playerFragment)
            .remove(albumContent)
            .commit()
    }

    fun initFragment() {

        supportFragmentManager
            .beginTransaction()
            .add(R.id.fragmentContainer,trackFragment,"1")
            .add(R.id.fragmentContainer,albumFragment,"2")
            .add(R.id.fragmentContainer,artistFragment, "3")
            .add(R.id.fragmentContainer,playlistFragment, "4")
            .add(R.id.fragmentContainer,settingsFragment, "5")
            .add(R.id.fragmentContainer,playerFragment,"6")
            .add(R.id.fragmentContainer,albumContent,"7")
            .hide(trackFragment)
            .hide(albumContent)
            .hide(albumFragment)
            .hide(artistFragment)
            .hide(playlistFragment)
            .hide(settingsFragment)
            .hide(playerFragment)
            .commit()

        supportFragmentManager
            .beginTransaction()
            .show(active)
            .commit()

    }


}

