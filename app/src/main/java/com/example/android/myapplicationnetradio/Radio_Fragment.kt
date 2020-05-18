package com.example.android.myapplicationnetradio

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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_radio, container, false)

        val viewModel: MainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        val nameObserver = Observer<String> { newName ->
            println("Second Fragment Observer !!!! NewName ="+newName)
            if (newName != "na" ) {
                radioSelectedName.text = viewModel.dataset[newName.toInt()].title+"\n"
                radioSelectedName.append(viewModel.dataset[newName.toInt()].sources[0])
            }
        }
        // Observe the LiveData, passing in this activity as the LifecycleOwner and the observer.
        viewModel.selectedRadio.observe(viewLifecycleOwner, nameObserver)

        return rootView
    }

}
