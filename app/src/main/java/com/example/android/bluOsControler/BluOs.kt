package com.example.android.bluOsControler

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread

class BluOs {

    private var bluOsIP = "192.168.1.73"
    private var bluOsUrl = "http://${bluOsIP}:11000"

    fun cmd(cmd: String) {
        println("BluOs Cmd: $cmd")
        thread { URL(bluOsUrl + cmd).readText() }
    }

    fun playAlbum(albumId: String) {
        println("BluOs Play: $albumId")
        thread {
            URL("$bluOsUrl/Clear").readText()  // Clear Playlist first !
            URL("$bluOsUrl/Add?service=Qobuz&playnow=1&albumid=$albumId").readText()
        }
    }

    fun playList(playListId: String) {
        println("BluOs Play: $playListId")
        thread {
            URL("$bluOsUrl/Clear").readText()  // Clear Playlist first !
            URL("$bluOsUrl/Add?service=Qobuz&playnow=1&playlistid=$playListId").readText()
        }
    }
    fun browsePlayList(favoriteUrl: String): MutableList<PlayListItem> {
        val dataSetItem = mutableListOf(PlayListItem())
        println("BluOs Browse PlayList Status : $bluOsUrl$favoriteUrl")
        val response = URL(bluOsUrl + favoriteUrl).readText()
        println("response : $response")
        val factory = XmlPullParserFactory.newInstance()
        factory.isNamespaceAware = true
        val parser = factory.newPullParser()
        parser.setInput(response.reader())
        var eventType = parser.eventType
        dataSetItem.clear()
        var newItem = PlayListItem()
        while (eventType != XmlPullParser.END_DOCUMENT) {
            val tagName = parser.name
            when (eventType) {
                XmlPullParser.START_TAG -> {
                    when (tagName) {
                        "name" -> {
                            newItem = PlayListItem()
                            newItem.tracks = parser.getAttributeValue(null, "tracks")
                            newItem.id = parser.getAttributeValue(null, "id")
                            newItem.description = parser.getAttributeValue(null, "description")
                            newItem.name = parser.nextText()
                            dataSetItem.add(newItem)
                        }
                    }
                }
            }
            eventType = parser.next()
        }
        for (item in dataSetItem) {
            println("name:" + item.name + " tracks:" + item.tracks + " id:" + item.id + " description:" + item.description)
        }
        return dataSetItem
    }

    fun browse(favoriteUrl: String): MutableList<Item> {
        val dataSetItem = mutableListOf(Item())
        println("BluOs Browse Status : $bluOsUrl/Browse?key=$favoriteUrl")
        val response = URL(bluOsUrl + "/Browse?key=" + favoriteUrl).readText()
        println("response : $response")
        val factory = XmlPullParserFactory.newInstance()
        factory.isNamespaceAware = true
        val parser = factory.newPullParser()
        parser.setInput(response.reader())
        var eventType = parser.eventType
        dataSetItem.clear()
        var newItem = Item()
        while (eventType != XmlPullParser.END_DOCUMENT) {
            val tagName = parser.name
            when (eventType) {
                XmlPullParser.START_TAG -> {
                    when (tagName) {
                        "item" -> {
                            newItem = Item()
                            newItem.text = parser.getAttributeValue(null, "text")
                            newItem.browseKey = parser.getAttributeValue(null, "browseKey")
                            newItem.type = parser.getAttributeValue(null, "type")
                        }
                    }
                }
                XmlPullParser.END_TAG -> {
                    if (tagName == "item") {
                        dataSetItem.add(newItem)
                    }
                }
            }
            eventType = parser.next()
        }
        for (item in dataSetItem) {
            println("text:" + item.text + " browseKey:" + item.browseKey + " type:" + item.type)
        }
        return dataSetItem
    }

    fun browseAlbum(favoriteUrl: String): MutableList<Album> {
        val dataSetAlbum = mutableListOf(Album())
        println("BluOs Get Status : $bluOsUrl$favoriteUrl")
        val response = URL(bluOsUrl + favoriteUrl).readText()
        println("response : $response")
        val factory = XmlPullParserFactory.newInstance()
        factory.isNamespaceAware = true
        val parser = factory.newPullParser()
        parser.setInput(response.reader())
        var eventType = parser.eventType
        dataSetAlbum.clear()
        var newalbum = Album()
        while (eventType != XmlPullParser.END_DOCUMENT) {
            val tagName = parser.name
            when (eventType) {
                XmlPullParser.START_TAG -> {
                    when (tagName) {
                        "title" -> newalbum.title = parser.nextText()
                        "art" -> newalbum.artist = parser.nextText()
                        "album" -> {
                            newalbum = Album()
                            newalbum.albumId = parser.getAttributeValue(null, "albumid")
                            newalbum.artistId = parser.getAttributeValue(null, "artistid")
                            newalbum.date = parser.getAttributeValue(null, "date")
                            newalbum.quality = parser.getAttributeValue(null, "quality")
                        }
                    }
                }
                XmlPullParser.END_TAG -> {
                    if (tagName == "album") {
                        dataSetAlbum.add(newalbum)
                    }
                }
            }
            eventType = parser.next()
        }
        return dataSetAlbum
    }

    fun playList(): MutableList<Song> {
        val dataSetSong = mutableListOf(Song())
        val favoriteUrl = "/Playlist?start=0"
        println("BluOs Get Playlist : $bluOsUrl$favoriteUrl")
        val response = URL(bluOsUrl + favoriteUrl).readText()
        // println("response : "+response)
        val factory = XmlPullParserFactory.newInstance()
        factory.isNamespaceAware = true
        val parser = factory.newPullParser()
        parser.setInput(response.reader())
        var eventType = parser.eventType
        dataSetSong.clear()
        var newsong = Song()
        while (eventType != XmlPullParser.END_DOCUMENT) {
            val tagName = parser.name
            when (eventType) {
                XmlPullParser.START_TAG -> {
                    when (tagName) {
                        "title" -> newsong.title = parser.nextText()
                        "art" -> newsong.art = parser.nextText()
                        "alb" -> newsong.alb = parser.nextText()
                        "fn" -> newsong.fn = parser.nextText()
                        "quality" -> newsong.quality = parser.nextText()
                        "song" -> {
                            newsong = Song()
                            newsong.songId = parser.getAttributeValue(null, "songid")
                            newsong.id = parser.getAttributeValue(null, "id")
                            newsong.albumId = parser.getAttributeValue(null, "albumid")
                            newsong.service = parser.getAttributeValue(null, "service")
                            newsong.artistId = parser.getAttributeValue(null, "artistid")
                        }
                    }
                }
                XmlPullParser.END_TAG -> {
                    if (tagName == "song") {
                        dataSetSong.add(newsong)
                    }
                }
            }
            eventType = parser.next()
        }
        //for (song in dataSetSong) {println("SongId:"+song.songId+" Id:"+song.id+ " AlbumId:"+song.albumId+ " Service:"+song.service+ " ArtistId :"+song.artistId) }
        return dataSetSong

    }

    fun getStatus(currentStatus: BluOsStatus): BluOsStatus {
        println("BluOs Get Status : $bluOsUrl/Status")
        val response = URL("$bluOsUrl/Status").readText()
        //println("BluOs Get Status Out : " + response.toString())
        val bluOsStatus = BluOsStatus()

        try {
            val factory = XmlPullParserFactory.newInstance()
            factory.isNamespaceAware = true
            val parser = factory.newPullParser()
            parser.setInput(response.reader())
            var eventType = parser.eventType

            while (eventType != XmlPullParser.END_DOCUMENT) {
                val tagName = parser.name
                when (eventType) {
                    XmlPullParser.START_TAG -> {
                        when (tagName) {
                            "status" -> bluOsStatus.Etag = parser.getAttributeValue(null, "etag")
                            "album" -> bluOsStatus.AlbumTitre = parser.nextText()
                            "song" -> bluOsStatus.Song = parser.nextText()
                            "artist" -> bluOsStatus.Artist = parser.nextText()
                            "state" -> bluOsStatus.BluOsState = parser.nextText()
                            "streamFormat" -> bluOsStatus.StreamFormat = parser.nextText()
                            "serviceName" -> bluOsStatus.Service = parser.nextText()
                            "quality" -> bluOsStatus.Quality = parser.nextText()
                            "title1" -> bluOsStatus.Title1 = parser.nextText()
                            "title2" -> bluOsStatus.Title2 = parser.nextText()
                            "title3" -> bluOsStatus.Title3 = parser.nextText()
                            "totlen" -> bluOsStatus.totlen = parser.nextText().toInt()
                            "secs" -> bluOsStatus.secs = parser.nextText().toInt()
                            "image" -> bluOsStatus.ArtworkUrl = parser.nextText()
                        }
                    }
                }
                eventType = parser.next()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

        if ((bluOsStatus.Etag != currentStatus.Etag) and (bluOsStatus.Service != "")) {
            println("New etag !")
            bluOsStatus.Artwork = getImage(bluOsStatus.Service, bluOsStatus.ArtworkUrl)
            // Get the Playlist to find  artistid & albumid ...
            val dataSetSong = playList()
            bluOsStatus.albumId = ""
            bluOsStatus.songId = ""
            bluOsStatus.artistId = ""
            for (song in dataSetSong) {
                if (bluOsStatus.Song == song.id) {
                    bluOsStatus.albumId = song.albumId
                    bluOsStatus.songId = song.songId
                    bluOsStatus.artistId = song.artistId
                }
            }
            println("SongId:" + bluOsStatus.songId + " AlbumId:" + bluOsStatus.albumId + " ArtistId :" + bluOsStatus.artistId)
        } else {
            bluOsStatus.Artwork = currentStatus.Artwork
            bluOsStatus.albumId = currentStatus.albumId
            bluOsStatus.songId = currentStatus.songId
            bluOsStatus.artistId = currentStatus.artistId
        }
        return bluOsStatus
    }

    fun getImage(Service: String, Url: String): Bitmap? {
        println("GetImage :$Url")
        try {
            var url = URL(bluOsUrl)
            if ((Service == "Qobuz") or (Service == "Deezer")) {
                url = URL(bluOsUrl + Url)
                val con: HttpURLConnection = url.openConnection() as HttpURLConnection
                con.instanceFollowRedirects = false
                con.connect()
                val location: String = con.getHeaderField("Location")
                println("Redirect location : " + location)
                url = URL(location)
            } else if (Service == "TuneIn") {
                url = URL(Url)
            } else if (Service == "USB") {
                url = URL(bluOsUrl + Url)
            } else {
                return null
            }
            return BitmapFactory.decodeStream(url.openConnection().getInputStream())
        } catch (e: Exception) {
            println("Error Message" + e.message.toString())
            e.printStackTrace()
        }
        return null
    }
}