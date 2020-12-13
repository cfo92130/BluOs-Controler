/*
* Copyright (C) 2017 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*      http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package com.example.android.BluOsControler

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class Fragment_Radio_List : androidx.fragment.app.Fragment() {

    private lateinit var recyclerView: androidx.recyclerview.widget.RecyclerView

       override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_radio_list,
                container, false).apply { tag = TAG}

        recyclerView = rootView.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = androidx.recyclerview.widget.GridLayoutManager(activity, SPAN_COUNT)
        val viewModel: MainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        initDataset(viewModel)
        recyclerView.adapter = CustomAdapter(viewModel.datasetAlbum, viewModel)

        return rootView
    }

    private fun initDataset(viewModel: MainViewModel ) {

        val BluOsInstance = BluOs()
        println("BluOsInstance"+BluOsInstance)
        runBlocking {
            val job = GlobalScope.launch {
                if (BluOsInstance != null) {
                    BluOsInstance.BrowseAlbum()
                    viewModel.datasetAlbum = BluOsInstance.datasetAlbum
                }
            }
            job.join()
        }
        //runBlocking {  delay(2000) }

        for ( album in viewModel.datasetAlbum ) {
            println("Artist:"+album.artist+" Album:"+album.title+" AlbumId:"+album.albumId+" ArtistId :"+album.artistId+" Date :"+album.date+" Qualitu :"+album.quality)
        }
    }

    companion object {
        private const val TAG = "RecyclerViewFragment"
        private const val SPAN_COUNT = 2
    }
}

