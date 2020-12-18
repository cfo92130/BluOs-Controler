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

    lateinit var BluOsInstance: BluOs
    var TimerGetStatus = Timer()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_reader, container, false)

        BluOsInstance = BluOs()

        // Set SeekBar Color
        val seek = rootView.findViewById<SeekBar>(R.id.seekBar)
        val progressDrawable: Drawable = seek.progressDrawable.mutate()
        progressDrawable.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN)
        seek.progressDrawable = progressDrawable

        // Define Buttons actions
        val button1 = rootView.findViewById<Button>(R.id.buttonPrevious)
        button1.setOnClickListener { BluOsInstance.Cmd("/Back") }

        val button2 = rootView.findViewById<Button>(R.id.buttonNext)
        button2.setOnClickListener { BluOsInstance.Cmd("/Skip") }

        val button3 = rootView.findViewById<Button>(R.id.buttonPlay)
        button3.setOnClickListener {
            println("Hi its Play")
            if (BluOsInstance.BluOsState != "play") {
                BluOsInstance.Cmd("/Play")
            } else {
                BluOsInstance.Cmd("/Pause")
            }
        }

        val button4 = rootView.findViewById<Button>(R.id.button)
        button4.setOnClickListener { thread {  BluOsInstance.Playlist() } }

        rootView.findViewById<Button>(R.id.albuminfo).setOnClickListener{ println("Albumid:"+BluOsInstance.albumId )
            startActivity(context?.let {
                it1 -> DisplayInfoActivity.newIntent(it1,
                    "http://192.168.1.73:11000/Info?service=Qobuz&albumid=" + BluOsInstance.albumId,
                    BluOsInstance.Title2,
                    BluOsInstance.Title3)
            })
        }

        rootView.findViewById<Button>(R.id.artistinfo).setOnClickListener{
            startActivity(context?.let {
                it1 -> DisplayInfoActivity.newIntent(it1,
                    "http://192.168.1.73:11000/Info?service=Qobuz&service=Qobuz&artistid=" + BluOsInstance.artistId,
                    BluOsInstance.Title2,
                    "")
            })
        }
        rootView.findViewById<Button>(R.id.songinfo).setOnClickListener{
            startActivity(context?.let {
                it1 -> DisplayInfoActivity.newIntent(it1,
                    "http://192.168.1.73:11000/Info?service=Qobuz&songid=" + BluOsInstance.songId,
                    BluOsInstance.Title2,
                    BluOsInstance.Title3+" "+BluOsInstance.Title1)
            })
        }

        rootView.findViewById<Button>(R.id.artist).setOnClickListener{
            thread {
                val viewModel: MainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
                BluOsInstance.BrowseAlbum("/Albums?service=Qobuz&artistid="+BluOsInstance.artistId)
                viewModel.datasetAlbum = BluOsInstance.datasetAlbum
                for (album in viewModel.datasetAlbum) {
                    println("Artist:" + album.artist + " Album:" + album.title + " AlbumId:" + album.albumId + " ArtistId :" + album.artistId + " Date :" + album.date + " Qualitu :" + album.quality)
                }
                activity?.supportFragmentManager
                        ?.beginTransaction()
                        ?.replace(R.id.fragment_container, Fragment_Album_List())
                        ?.commit()
            }
        }

        return rootView
    }

    override fun onResume() {
        super.onResume()

        TimerGetStatus = Timer()
        TimerGetStatus.schedule(timerTask {
            BluOsInstance.GetStatus()
            activity!!.runOnUiThread { UIRefresh() }
        }, 1000, 2000)
    }

    override fun onPause() {
        super.onPause()
        TimerGetStatus.cancel()
    }

    fun UIRefresh () {
        // artist.text = "Artist : "+BluOsInstance.Artist
        // album.text = "Album : "+BluOsInstance.Album
        // song.text = "Song : "+ BluOsInstance.Song
        bluOsStatus.text = BluOsInstance.BluOsState
        if (BluOsInstance.BluOsState != "play") {
            buttonPlay.foreground = ContextCompat.getDrawable(this.requireContext(), android.R.drawable.ic_media_play)
        } else {
            buttonPlay.foreground = ContextCompat.getDrawable(this.requireContext(), android.R.drawable.ic_media_pause)
        }
        streamFormat.text = BluOsInstance.StreamFormat
        service.text = BluOsInstance.Service
        quality.text = BluOsInstance.Quality
        song.text = BluOsInstance.Title1
        artist.text = BluOsInstance.Title2
        album.text = BluOsInstance.Title3
        seekBar.max = BluOsInstance.totlen
        seekBar.progress = BluOsInstance.secs
        // artWorkUrl.text = "ArtWorkUrl : "+BluOsInstance.ArtworkUrl
        imageView.setImageBitmap(BluOsInstance.Artwork)
    }

}