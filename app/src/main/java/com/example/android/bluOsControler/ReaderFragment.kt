package com.example.android.bluOsControler

import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.SeekBar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.fragment_reader.*
import java.util.*
import kotlin.concurrent.thread
import kotlin.concurrent.timerTask


class ReaderFragment : Fragment() {

    lateinit var bluOsStatus: BluOsStatus
    lateinit var bluOsInstance: BluOs
    var timerGetStatus = Timer()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_reader, container, false)
        bluOsStatus = BluOsStatus()
        bluOsInstance = BluOs()

        // Set SeekBar Color
        val seek = rootView.findViewById<SeekBar>(R.id.seekBar)
        val progressDrawable: Drawable = seek.progressDrawable.mutate()
        progressDrawable.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN)
        seek.progressDrawable = progressDrawable

        // Define Buttons actions
        val button1 = rootView.findViewById<Button>(R.id.buttonPrevious)
        button1.setOnClickListener { bluOsInstance.cmd("/Back") }

        val button2 = rootView.findViewById<Button>(R.id.buttonNext)
        button2.setOnClickListener { bluOsInstance.cmd("/Skip") }

        val button3 = rootView.findViewById<Button>(R.id.buttonPlay)
        button3.setOnClickListener {
            println("Hi its Play")
            if (bluOsStatus.BluOsState != "play") {
                bluOsInstance.cmd("/Play")
            } else {
                bluOsInstance.cmd("/Pause")
            }
        }

        val button4 = rootView.findViewById<Button>(R.id.button)
        button4.setOnClickListener { thread {  bluOsInstance.playList() } }

        rootView.findViewById<Button>(R.id.albuminfo).setOnClickListener{ println("Albumid:"+ bluOsStatus.albumId )
            startActivity(context?.let {
                it1 -> DisplayInfoActivity.newIntent(it1,
                    "http://192.168.1.73:11000/Info?service=Qobuz&albumid=" + bluOsStatus.albumId,
                    bluOsStatus.Title2,
                    bluOsStatus.Title3)
            })
        }

        rootView.findViewById<Button>(R.id.artistinfo).setOnClickListener{
            startActivity(context?.let {
                it1 -> DisplayInfoActivity.newIntent(it1,
                    "http://192.168.1.73:11000/Info?service=Qobuz&service=Qobuz&artistid=" + bluOsStatus.artistId,
                    bluOsStatus.Title2,
                    "")
            })
        }
        rootView.findViewById<Button>(R.id.songinfo).setOnClickListener{
            startActivity(context?.let {
                it1 -> DisplayInfoActivity.newIntent(it1,
                    "http://192.168.1.73:11000/Info?service=Qobuz&songid=" + bluOsStatus.songId,
                    bluOsStatus.Title2,
                    bluOsStatus.Title3+" "+ bluOsStatus.Title1)
            })
        }

        // Display Albums for an Artist
        rootView.findViewById<Button>(R.id.artist).setOnClickListener{
                val viewModel: MainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
            viewModel.selectedArtistId.postValue(bluOsStatus.artistId)
        }

        return rootView
    }

    override fun onResume() {
        super.onResume()

        timerGetStatus = Timer()
        timerGetStatus.schedule(timerTask {
            bluOsStatus = bluOsInstance.getStatus(bluOsStatus)
            activity!!.runOnUiThread { UIRefresh() }
        }, 1000, 2000)
    }

    override fun onPause() {
        super.onPause()
        timerGetStatus.cancel()
    }

    fun UIRefresh () {
        // artist.text = "Artist : "+BluOsInstance.Artist
        // album.text = "Album : "+BluOsInstance.Album
        // song.text = "Song : "+ BluOsInstance.Song
        bluOsState.text = bluOsStatus.BluOsState
        if (bluOsStatus.BluOsState != "play") {
            buttonPlay.foreground = ContextCompat.getDrawable(this.requireContext(), android.R.drawable.ic_media_play)
        } else {
            buttonPlay.foreground = ContextCompat.getDrawable(this.requireContext(), android.R.drawable.ic_media_pause)
        }
        streamFormat.text = bluOsStatus.StreamFormat
        service.text = bluOsStatus.Service
        quality.text = bluOsStatus.Quality
        song.text = bluOsStatus.Title1
        artist.text = bluOsStatus.Title2
        album.text = bluOsStatus.Title3
        seekBar.max = bluOsStatus.totlen
        seekBar.progress = bluOsStatus.secs
        // artWorkUrl.text = "ArtWorkUrl : "+BluOsInstance.ArtworkUrl
        imageView.setImageBitmap(bluOsStatus.Artwork)
    }

}