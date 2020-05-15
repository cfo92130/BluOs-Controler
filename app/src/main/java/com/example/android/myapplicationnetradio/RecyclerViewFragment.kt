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

package com.example.android.myapplicationnetradio

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types

class RecyclerViewFragment : androidx.fragment.app.Fragment() {

    private lateinit var recyclerView: androidx.recyclerview.widget.RecyclerView
    private lateinit var dataset: List<Radio>
    private val listType = Types.newParameterizedType( List::class.java, Radio::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initDataset()
    }
    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.recycler_view_frag,
                container, false).apply { tag = TAG}

        recyclerView = rootView.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = androidx.recyclerview.widget.GridLayoutManager(activity, SPAN_COUNT)
        recyclerView.adapter = CustomAdapter(dataset)
        return rootView
    }

    private fun initDataset() {

        var radios : String
        radios = this.context?.assets?.open("RadioList.json").use {
            it!!.bufferedReader().use {
                it.readText()
            }
        }
        println("My Radio List :"+ radios.toString())
        val moshi = Moshi.Builder().build()
        val adapter: JsonAdapter<List<Radio>> = moshi.adapter(listType)
        dataset = adapter.fromJson(radios)!!

        for ( radio in dataset ) {
            println("Radio:"+radio.title+" WebUrl:"+radio.websiteUrl+" URI:"+ radio.sources[0])
        }
    }

    companion object {
        private val TAG = "RecyclerViewFragment"
        private val SPAN_COUNT = 3
    }
}
