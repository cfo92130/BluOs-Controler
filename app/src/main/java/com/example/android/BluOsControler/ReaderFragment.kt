package com.example.android.BluOsControler

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
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

        val button1 = rootView.findViewById<Button>(R.id.buttonPrevious)
        button1.setOnClickListener { BluOsInstance.BluOsCmd("/Back") }

        val button2 = rootView.findViewById<Button>(R.id.buttonNext)
        button2.setOnClickListener { BluOsInstance.BluOsCmd("/Skip") }

        val button3 = rootView.findViewById<Button>(R.id.buttonPlay)
        button3.setOnClickListener {
            println("Hi its Play")
            if (BluOsInstance.BluOsState != "play") {
                BluOsInstance.BluOsCmd("/Play")
            } else {
                BluOsInstance.BluOsCmd("/Pause")
            }
        }

        val button4 = rootView.findViewById<Button>(R.id.buttonTest)
        button4.setOnClickListener {
            thread {
            BluOsInstance.BluOsBrowseAlbum()
            println("dataset Album: ")
            for ( album in BluOsInstance.datasetAlbum ) {
                println("Artist:"+album.artist+" Album:"+album.title+" AlbumId:"+album.albumId+" ArtistId :"+album.artistId+" Date :"+album.date+" Qualitu :"+album.quality)
            }
            }
        }

        return rootView
    }

    override fun onResume() {
        super.onResume()
        BluOsInstance = context?.let { BluOs(it) }!!
        TimerGetStatus = Timer()
        TimerGetStatus.schedule(timerTask {
            BluOsInstance.BluOsGetStatus()
            activity!!.runOnUiThread { UIRefresh() }
        }, 1000, 3000)
    }

    override fun onPause() {
        super.onPause()
        TimerGetStatus.cancel()
    }

    fun UIRefresh () {
        // artist.text = "Artist : "+BluOsInstance.Artist
        // album.text = "Album : "+BluOsInstance.Album
        // song.text = "Song : "+ BluOsInstance.Song
        bluOsStatus.text = "Status : "+ BluOsInstance.BluOsState
        streamFormat.text = "Format : "+BluOsInstance.StreamFormat
        service.text = "Service : "+BluOsInstance.Service
        quality.text = "Quality : "+BluOsInstance.Quality
        title1.text = BluOsInstance.Title1
        title2.text = BluOsInstance.Title2
        title3.text = BluOsInstance.Title3
        artWorkUrl.text = "ArtWorkUrl : "+BluOsInstance.ArtworkUrl
        imageView.setImageBitmap(BluOsInstance.Artwork)
    }

}