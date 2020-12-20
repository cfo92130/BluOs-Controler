/*
* Copyright (C) 2014 The Android Open Source Project
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

package com.example.android.bluOsControler

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView


class FragmentBrowseDetail(private val dataSet: MutableList<Item>, myViewModel: MainViewModel ) :
        androidx.recyclerview.widget.RecyclerView.Adapter<FragmentBrowseDetail.ViewHolder>() {

    private val localViewModel = myViewModel

    class ViewHolder(v: View, myViewModel: MainViewModel, myDataSet : MutableList<Item>)  : androidx.recyclerview.widget.RecyclerView.ViewHolder(v) {
        var text: TextView
        var browseKey: TextView

        init {
            // Define click listener for the ViewHolder's View.
             v.setOnClickListener {
                 Log.d(TAG, "Element $adapterPosition clicked.")
                 myViewModel.selectedBrowseKey.postValue(myDataSet[adapterPosition].browseKey)
             }
            text = v.findViewById(R.id.text)
            browseKey = v.findViewById(R.id.browseKey)

        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.browse_detail, viewGroup, false)
        return ViewHolder(v, localViewModel, dataSet)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        Log.d(TAG, "Element $position set.")

        viewHolder.text.text = dataSet[position].text
        viewHolder.browseKey.text = dataSet[position].browseKey

    }

    override fun getItemCount() = dataSet.size

    companion object {
        private const val TAG = "CustomAdapter"
    }
}
