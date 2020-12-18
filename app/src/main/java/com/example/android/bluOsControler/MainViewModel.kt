package com.example.android.bluOsControler

import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class Album (
        var date : String,
        var quality : String,
        var albumId : String,
        var artistId : String,
        var title : String,
        var artist : String,
        var art : Bitmap? = null
        )

class Song (
        var songId : String,
        var id : String,
        var albumId : String,
        var service : String,
        var artistId: String,
        var title : String,
        var art : String,
        var alb : String,
        var fn: String,
        var quality : String
        )


class MainViewModel : ViewModel() {

    var datasetAlbum =  mutableListOf(Album("","","","","",""))
    val selectedAlbum: MutableLiveData<String> = MutableLiveData("na")
}
