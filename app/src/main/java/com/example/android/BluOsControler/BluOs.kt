package com.example.android.BluOsControler

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

class BluOs(context: Context) {

    private var BluOsContext = context
    var BluOsIP = "192.168.1.73"
    var BluOsPowerState    = "NA"
    var BluOsUrl = "http://${BluOsIP}:11000"
    var BluOsError = ""
    var AlbumTitre = ""
    var Artist = ""
    var Song = ""
    var BluOsState = ""
    var StreamFormat = ""
    var Service = ""
    var Quality = ""
    var Title1 = ""
    var Title2 = ""
    var Title3 = ""
    var ArtworkUrl = " "
    var Artwork : Bitmap? = null
    var AlbumListXML = ""
    var datasetAlbum  = mutableListOf(Album("","","","","",""))


    fun BluOsCmd( cmd : String ) {
        println("BluOs Cmd: " + cmd)
        val queue = Volley.newRequestQueue(BluOsContext)
        val stringRequest = StringRequest(
                Request.Method.GET,
                BluOsUrl + cmd,
                { response -> },
                { error -> }
        )
        queue.add(stringRequest)
    }

    fun BluOsBrowseAlbum() {
        val favoriteUrl = "/Albums?service=Qobuz&browseIsFavouritesContext=1&category=FAVOURITES&start=30&end=100"
        println("BluOs Get Status : " + BluOsUrl + favoriteUrl)
        val response = URL(BluOsUrl+favoriteUrl).readText()
        println("response : "+response)
        AlbumListXML = response
        val factory = XmlPullParserFactory.newInstance()
        factory.isNamespaceAware = true
        val parser = factory.newPullParser()
        parser.setInput(response.reader())
        var eventType = parser.eventType
        datasetAlbum.clear()
        var newalbum = Album("","","","","","")
        while (eventType != XmlPullParser.END_DOCUMENT) {
            val tagname = parser.name
            when (eventType) {
                XmlPullParser.START_TAG -> {
                    if (tagname == "title")   {
                        newalbum.title = parser.nextText()
                    }
                    if (tagname == "art")  {
                        newalbum.artist = parser.nextText()
                    }
                    if (tagname == "album")  {
                        newalbum = Album("","","","","","")
                        newalbum.albumId = parser.getAttributeValue(null, "albumid");
                        newalbum.artistId = parser.getAttributeValue(null, "artistid");
                        newalbum.date = parser.getAttributeValue(null, "date");
                        newalbum.quality = parser.getAttributeValue(null, "quality");
                    }
                }
                XmlPullParser.END_TAG -> {
                    if (tagname == "album")  {
                        datasetAlbum.add(newalbum)
                    }
                }
            }
            eventType = parser.next()
        }

    }

    fun BluOsBrowseAlbum2() {
        val favoriteUrl = "/Albums?service=Qobuz&browseIsFavouritesContext=1&category=FAVOURITES&start=30&end=100"
        println("BluOs Get Status : " + BluOsUrl + favoriteUrl)
        val queue = Volley.newRequestQueue(BluOsContext)
        val stringRequest = StringRequest(
                Request.Method.GET,
                BluOsUrl + favoriteUrl,
                { response ->
                    try {
                        // println(response.toString())
                        AlbumListXML = response.toString()
                        val factory = XmlPullParserFactory.newInstance()
                        factory.isNamespaceAware = true
                        val parser = factory.newPullParser()
                        parser.setInput(response.reader())
                        var eventType = parser.eventType
                        datasetAlbum.clear()
                        var newalbum = Album("","","","","","")
                        while (eventType != XmlPullParser.END_DOCUMENT) {
                            val tagname = parser.name
                            when (eventType) {
                                XmlPullParser.START_TAG -> {
                                    if (tagname == "title")   {
                                        newalbum.title = parser.nextText()
                                    }
                                    if (tagname == "art")  {
                                        newalbum.artist = parser.nextText()
                                    }
                                    if (tagname == "album")  {
                                        newalbum = Album("","","","","","")
                                        newalbum.albumId = parser.getAttributeValue(null, "albumid");
                                        newalbum.artistId = parser.getAttributeValue(null, "artistid");
                                        newalbum.date = parser.getAttributeValue(null, "date");
                                        newalbum.quality = parser.getAttributeValue(null, "quality");
                                    }
                                }
                                XmlPullParser.END_TAG -> {
                                    if (tagname == "album")  {
                                        datasetAlbum.add(newalbum)
                                    }
                                }
                            }
                            eventType = parser.next()
                        }
                        println("dataset Album: ")
                        for ( album in datasetAlbum ) {
                            println("Artist:"+album.artist+" Album:"+album.title+" AlbumId:"+album.albumId+" ArtistId :"+album.artistId+" Date :"+album.date+" Qualitu :"+album.quality)
                        }
                    }
                    catch (e: IOException) {
                        e.printStackTrace()
                    }
                },
                { error -> }
        )
        queue.add(stringRequest)
    }

    fun BluOsGetStatus() {
        println("BluOs Get Status : " + BluOsUrl + "/Status")
        val queue = Volley.newRequestQueue(BluOsContext)
        val stringRequest = StringRequest(
                Request.Method.GET,
                BluOsUrl + "/Status",
                { response ->
                    // println("BluOs Get Status Out : " + response.toString())
                    try {

                        BluOsError = ""
                        val factory = XmlPullParserFactory.newInstance()
                        factory.isNamespaceAware = true
                        val parser = factory.newPullParser()
                        parser.setInput(response.reader())
                        var eventType = parser.eventType

                        AlbumTitre = " "
                        Artist = " "
                        Song = " "
                        BluOsState = " "
                        StreamFormat = " "
                        Service = " "
                        Quality = " "
                        Title1 = " "
                        Title2 = " "
                        Title3 = " "
                        ArtworkUrl = " "

                        while (eventType != XmlPullParser.END_DOCUMENT) {
                            val tagname = parser.name
                            when (eventType) {
                                XmlPullParser.START_TAG -> {
                                    if (tagname == "album") {
                                        AlbumTitre = parser.nextText()
                                    }
                                    if (tagname == "artist") {
                                        Artist = parser.nextText()
                                    }
                                    if (tagname == "state") {
                                        BluOsState = parser.nextText()
                                    }
                                    if (tagname == "streamFormat") {
                                        StreamFormat = parser.nextText()
                                    }
                                    if (tagname == "serviceName") {
                                        Service = parser.nextText()
                                    }
                                    if (tagname == "quality") {
                                        Quality = parser.nextText()
                                    }
                                    if (tagname == "title1") {
                                        Title1 = parser.nextText()
                                    }
                                    if (tagname == "title2") {
                                        Title2 = parser.nextText()
                                    }
                                    if (tagname == "title3") {
                                        Title3 = parser.nextText()
                                    }
                                    if (tagname == "image") {
                                        ArtworkUrl = parser.nextText()
                                    }
                              }
                            }
                            eventType = parser.next()
                        }
                        BluOsPowerState = "On"

                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                },
                { error ->
                    BluOsError = error.toString()
                    println("BluOs Get Status Out : didn't work!")
                    BluOsPowerState = "NA"
                }
        )
        queue.add(stringRequest)

        try {
            var url = URL(BluOsUrl)
            if (( Service == "Qobuz" ) or ( Service == "Deezer" ))  {
                url = URL(BluOsUrl + ArtworkUrl)
                val con: HttpURLConnection = url.openConnection() as HttpURLConnection
                con.setInstanceFollowRedirects(false)
                con.connect()
                val location: String = con.getHeaderField("Location")
                println("Redirect location : "+location)
                url = URL(location)
            }
            if ( Service == "TuneIn") {
                url = URL(ArtworkUrl)
            }
            if ( Service == "USB") {
                url = URL(BluOsUrl + ArtworkUrl)
            }
            Artwork = BitmapFactory.decodeStream(url.openConnection().getInputStream())
        }
        catch (e: Exception) {
            println("Error Message" + e.message.toString())
            e.printStackTrace()
        }
    }
}