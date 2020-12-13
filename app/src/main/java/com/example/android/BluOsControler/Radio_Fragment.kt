package com.example.android.BluOsControler

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.fragment_radio.*

/**
 * A simple [Fragment] subclass.
 * Use the [Radio_Fragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class Radio_Fragment : Fragment() {

    lateinit var BluOsInstance: BluOs

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_radio, container, false)

        BluOsInstance =  BluOs()
        val viewModel: MainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        val nameObserver = Observer<String> { newName ->
            println("Second Fragment Observer !!!! NewName ="+newName)
            if (newName != "na" ) {
                radioSelectedName.text = viewModel.datasetAlbum[newName.toInt()].title+"\n"
                radioSelectedName.append(viewModel.datasetAlbum[newName.toInt()].artist+"\n")
                println("Album  ="+radioSelectedName.text)
                BluOsInstance.Play(viewModel.datasetAlbum[newName.toInt()].albumId)
            }
        }
        // Observe the LiveData, passing in this activity as the LifecycleOwner and the observer.
        viewModel.selectedAlbum.observe(viewLifecycleOwner, nameObserver)
        return rootView
    }

}
