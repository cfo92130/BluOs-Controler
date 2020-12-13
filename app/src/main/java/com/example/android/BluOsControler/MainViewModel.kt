package com.example.android.BluOsControler

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class Album (
        var date : String,
        var quality : String,
        var albumId : String,
        var artistId : String,
        var title : String,
        var artist : String
        )

class MainViewModel : ViewModel() {

    lateinit var datasetAlbum: MutableList<Album>

    val selectedAlbum: MutableLiveData<String> = MutableLiveData("na")

}
