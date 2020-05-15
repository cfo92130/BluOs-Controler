package com.example.android.myapplicationnetradio

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class Log_Fragment : Fragment() {

    val clickCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        println("Log Fragment : OnCreate")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var click = 0
        println("Log Fragment : OnCreateView")
        click = savedInstanceState?.getInt("KEY_CLICK_COUNT") ?: 0
        println("KEY_CLICK_COUNT "+click.toString())
        return inflater.inflate(R.layout.log_frag, container, false)
    }

    override fun onDestroy() {
        println("Log Fragment : OnDestroy")
        super.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        println("KEY_CLICK_COUNT "+ clickCount.toString())
        outState.putInt("KEY_CLICK_COUNT", clickCount + 1)
    }

    companion object {
    }
}
