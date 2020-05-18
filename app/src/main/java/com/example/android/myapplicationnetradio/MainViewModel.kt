package com.example.android.myapplicationnetradio

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class Radio (
        val title : String,
        val websiteUrl : String,
        val sources : List<String>
)

class MainViewModel : ViewModel() {

    lateinit var dataset: List<Radio>

    val currentText2: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    val selectedRadio: MutableLiveData<String> = MutableLiveData("na")

}
