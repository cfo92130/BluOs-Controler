package com.example.android.bluOsControler

import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class BluOsStatus(
        var AlbumTitre: String = "",
        var albumId: String = "",
        var songId: String = "",
        var artistId: String = "",
        var Artist: String = "",
        var Song: String = "",
        var BluOsState: String = "",
        var StreamFormat: String = "",
        var Service: String = "",
        var Quality: String = "",
        var Title1: String = "",
        var Title2: String = "",
        var Title3: String = "",
        var Etag: String = "",
        var ArtworkUrl: String = "",
        var totlen: Int = 100,
        var secs: Int = 0,
        var Artwork: Bitmap? = null
)

class Album(
        var date: String = "",
        var quality: String = "",
        var albumId: String = "",
        var artistId: String = "",
        var title: String = "",
        var artist: String = "",
        var art: Bitmap? = null
)

class Song(
        var songId: String = "",
        var id: String = "",
        var albumId: String = "",
        var service: String = "",
        var artistId: String = "",
        var title: String = "",
        var art: String = "",
        var alb: String = "",
        var fn: String = "",
        var quality: String = ""
)

class Item(
        var text: String = "",
        var browseKey: String = "",
        var type: String = ""
)

class PlayListItem(
        var name: String = "",
        var tracks: String = "",
        var id: String = "",
        var description: String = ""
)

class MainViewModel : ViewModel() {
    // var datasetAlbum =  mutableListOf(Album("","","","","",""))
    val selectedAlbumId: MutableLiveData<String> = MutableLiveData("na")
    val selectedArtistId: MutableLiveData<String> = MutableLiveData("na")
    val selectedPlayListId: MutableLiveData<String> = MutableLiveData("na")
    val selectedBrowseKey: MutableLiveData<String> = MutableLiveData("na")
    val selectedTab: MutableLiveData<Int> = MutableLiveData(0)
    var selectedTitle : String = " Title"

}
