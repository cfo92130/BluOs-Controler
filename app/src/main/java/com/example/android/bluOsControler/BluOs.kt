package com.example.android.bluOsControler

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread

class BluOs() {

    var BluOsIP = "192.168.1.73"
    var BluOsPowerState    = "NA"
    var BluOsUrl = "http://${BluOsIP}:11000"
    var BluOsError = ""
    var AlbumTitre = ""
    var albumId = ""
    var songId = ""
    var artistId = ""
    var Artist = ""
    var Song = ""
    var BluOsState = ""
    var StreamFormat = ""
    var Service = ""
    var Quality = ""
    var Title1 = ""
    var Title2 = ""
    var Title3 = ""
    var Etag = ""
    var ArtworkUrl = " "
    var Artwork : Bitmap? = null
    var datasetAlbum  = mutableListOf(Album("","","","","",""))
    var datasetSong = mutableListOf(Song("","","","","","","","","",""))
    var totlen = 100
    var secs = 0

    fun Cmd(cmd : String ) {
        println("BluOs Cmd: " + cmd)
        thread {  val response = URL(BluOsUrl+cmd).readText() }
    }

    fun Play(albumid : String ) {
        println("BluOs Play: " + albumid)
        thread {
            val response1 = URL(BluOsUrl+"/Clear").readText()  // Clear Playlist first !
            val response2 = URL(BluOsUrl+"/Add?service=Qobuz&playnow=1&albumid="+albumid).readText()
        }
    }

    fun BrowseAlbum(favoriteUrl: String) {
        // val favoriteUrl = "/Albums?service=Qobuz&browseIsFavouritesContext=1&category=FAVOURITES&end=100"
        println("BluOs Get Status : " + BluOsUrl + favoriteUrl)
        val response = URL(BluOsUrl+favoriteUrl).readText()
        println("response : "+response)
        // AlbumListXML = response
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

    fun Playlist() {
        val favoriteUrl = "/Playlist?start=0"
        println("BluOs Get Playlist : " + BluOsUrl + favoriteUrl)
        val response = URL(BluOsUrl+favoriteUrl).readText()
        // println("response : "+response)
        val factory = XmlPullParserFactory.newInstance()
        factory.isNamespaceAware = true
        val parser = factory.newPullParser()
        parser.setInput(response.reader())
        var eventType = parser.eventType
        datasetSong.clear()
        var newsong = Song("","","","","","","","","","")
        while (eventType != XmlPullParser.END_DOCUMENT) {
            val tagname = parser.name
            when (eventType) {
                XmlPullParser.START_TAG -> {
                    if (tagname == "title")   {
                        newsong.title = parser.nextText()
                    }
                    if (tagname == "art")  {
                        newsong.art = parser.nextText()
                    }
                    if (tagname == "alb")  {
                        newsong.alb = parser.nextText()
                    }
                    if (tagname == "fn")  {
                        newsong.fn = parser.nextText()
                    }
                    if (tagname == "quality")  {
                        newsong.quality = parser.nextText()
                    }
                    if (tagname == "song")  {
                        newsong = Song("","","","","","","","","","")
                        newsong.songId = parser.getAttributeValue(null, "songid");
                        newsong.id = parser.getAttributeValue(null, "id");
                        newsong.albumId = parser.getAttributeValue(null, "albumid");
                        newsong.service = parser.getAttributeValue(null, "service");
                        newsong.artistId = parser.getAttributeValue(null, "artistid");
                    }
                }
                XmlPullParser.END_TAG -> {
                    if (tagname == "song")  {
                        datasetSong.add(newsong)
                    }
                }
            }
            eventType = parser.next()
        }
        //for (song in datasetSong) {println("SongId:"+song.songId+" Id:"+song.id+ " AlbumId:"+song.albumId+ " Service:"+song.service+ " ArtistId :"+song.artistId) }
    }

    fun GetStatus() {
        println("BluOs Get Status : " + BluOsUrl + "/Status")
        val response = URL(BluOsUrl+"/Status").readText()
        //println("BluOs Get Status Out : " + response.toString())
        var newEtag = ""
        try {
            BluOsError = ""
            val factory = XmlPullParserFactory.newInstance()
            factory.isNamespaceAware = true
            val parser = factory.newPullParser()
            parser.setInput(response.reader())
            var eventType = parser.eventType

            AlbumTitre = ""
            Artist = ""
            Song = ""
            BluOsState = ""
            StreamFormat = ""
            Service = ""
            Quality =  ""
            Title1 = ""
            Title2 = ""
            Title3 = ""

            while (eventType != XmlPullParser.END_DOCUMENT) {
                val tagname = parser.name
                when (eventType) {
                    XmlPullParser.START_TAG -> {
                        if (tagname == "status") {
                            newEtag = parser.getAttributeValue(null, "etag")
                        }
                        if (tagname == "album") {
                            AlbumTitre = parser.nextText()
                        }
                        if (tagname == "song") {
                            Song = parser.nextText()
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
                        if (tagname == "totlen") {
                            totlen = parser.nextText().toInt()
                        }
                        if (tagname == "secs") {
                            secs = parser.nextText().toInt()
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

        if (( newEtag != Etag) and (Service != "") ) {
            println("New etag !")
            Etag = newEtag
            Artwork = GetImage(Service, ArtworkUrl)
            // Todo  Get the Playlist to find  artistid & albumid ...
            Playlist()
            albumId = ""
            songId = ""
            artistId = ""
            for ( song in datasetSong ) {
                if ( Song == song.id ) {
                    albumId = song.albumId
                    songId = song.songId
                    artistId = song.artistId
                }

            }
            println("SongId:"+songId+ " AlbumId:"+albumId+ " ArtistId :"+artistId)
        }
    }

    fun GetImage( Service : String, Url : String) : Bitmap? {
        println("GetImage :"+Url)
        try {
            var url = URL(BluOsUrl)
            if (( Service == "Qobuz" ) or ( Service == "Deezer" ))  {
                url = URL(BluOsUrl + Url)
                val con: HttpURLConnection = url.openConnection() as HttpURLConnection
                con.instanceFollowRedirects = false
                con.connect()
                val location: String = con.getHeaderField("Location")
                println("Redirect location : "+location)
                url = URL(location)
            }
            else if ( Service == "TuneIn") {
                url = URL(Url)
            }
            else if ( Service == "USB") {
                url = URL(BluOsUrl + Url)
            }
            else {
                return null
            }
            return BitmapFactory.decodeStream(url.openConnection().getInputStream())
        }
        catch (e: Exception) {
            println("Error Message" + e.message.toString())
            e.printStackTrace()
        }
        return null
    }
}