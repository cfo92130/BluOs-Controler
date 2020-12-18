/*
* Copyright 2017 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*     http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/


package com.example.android.bluOsControler

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ViewAnimator
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.tabs.TabLayout
import kotlin.concurrent.thread


class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val tabLayout = findViewById<TabLayout>(R.id.tabLayout)
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                // get the current selected tab's position and replace the View accordingly
                println("Tab position = " + tab.position)
                val output = findViewById<ViewAnimator>(R.id.viewAnimator) as ViewAnimator
                output.displayedChild = tab.position
            }
            override fun onTabUnselected(tab: TabLayout.Tab) {
            }
            override fun onTabReselected(tab: TabLayout.Tab) {
            }
        })

        // Observe the LiveData, passing in this activity as the LifecycleOwner and the observer.
        val BluOsInstance = BluOs()
        val viewModel: MainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        val nameObserver = Observer<String> { newName ->
            println("New Album to play =$newName")
            if (newName != "na") {
                println("Artist =" + viewModel.datasetAlbum[newName.toInt()].artist)
                println("Album  =" + viewModel.datasetAlbum[newName.toInt()].title)
                BluOsInstance.Play(viewModel.datasetAlbum[newName.toInt()].albumId)
                // Display Now Playing Tab
                tabLayout.getTabAt(0)?.select()
            }
        }
        viewModel.selectedAlbum.observe(this, nameObserver)

        // Initialize Album list with Favorite
        thread {
            BluOsInstance.BrowseAlbum("/Albums?service=Qobuz&browseIsFavouritesContext=1&category=FAVOURITES&end=100")
            viewModel.datasetAlbum = BluOsInstance.datasetAlbum
            for (album in viewModel.datasetAlbum) {
                println("Artist:" + album.artist + " Album:" + album.title + " AlbumId:" + album.albumId + " ArtistId :" + album.artistId + " Date :" + album.date + " Qualitu :" + album.quality)
            }
            supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.fragment_container, Fragment_Album_List())
                    .commit()
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.menu_toggle_log -> {
            println("Option selected ...")
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    companion object {
        var SelectedAlbum = 0
    }
}
