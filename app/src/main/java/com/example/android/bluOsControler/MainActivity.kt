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

        val viewModel: MainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        // Observe the LiveData selectAlbumId and Play it
        viewModel.selectedAlbumId.observe(this,
                Observer<String> { albumId ->
                    println("New Album to play =$albumId")
                    val bluOsInstance = BluOs()
                    if (albumId != "na") bluOsInstance.play(albumId)
                    tabLayout.getTabAt(0)?.select()
                }
        )

        // Observe the LiveData selectedTab
        viewModel.selectedTab.observe(this,
                Observer<Int> { newTab ->
                    println("New Tab to Display =$newTab")
                    tabLayout.getTabAt(newTab)?.select()
                }
        )

        // Observe the LiveData selectedArtistId and display albums list
        viewModel.selectedArtistId.observe(this,
                Observer<String> { artistId ->
                    println("New ArtistId to Display =$artistId")
                    if (artistId != "na") {
                        thread {
                            val bluOsInstance = BluOs()
                            val dataSetAlbum = bluOsInstance.browseAlbum("/Albums?service=Qobuz&artistid=" + artistId)
                            for (album in dataSetAlbum) {
                                println("Artist:" + album.artist + " Album:" + album.title + " AlbumId:" + album.albumId + " ArtistId :" + album.artistId + " Date :" + album.date + " Qualitu :" + album.quality)
                            }
                            supportFragmentManager
                                    .beginTransaction()
                                    .replace(R.id.fragment_container2, FragmentAlbumList(dataSetAlbum))
                                    .commit()
                        }
                        // Display Now Playing Tab
                        tabLayout.getTabAt(2)?.select()
                    }
                }
        )

        // Observe the LiveData selectedBrowseKey and display item list
        viewModel.selectedBrowseKey.observe(this,
                Observer<String> { browseKey ->
                    println("New ArtistId to Display =$browseKey")

                    if (browseKey != "na") {
                        thread {
                            val bluOsInstance = BluOs()
                            if (browseKey.startsWith("/Albums?")) {
                                val dataSetAlbum = bluOsInstance.browseAlbum(browseKey)
                                for (album in dataSetAlbum) {
                                    println("Artist:" + album.artist + " Album:" + album.title + " AlbumId:" + album.albumId + " ArtistId :" + album.artistId + " Date :" + album.date + " Qualitu :" + album.quality)
                                }
                                supportFragmentManager
                                        .beginTransaction()
                                        .replace(R.id.fragment_container2, FragmentAlbumList(dataSetAlbum))
                                        .commit()
                            } else if (browseKey.startsWith("/Playlists?")) {
                                val dataSetPlayList = bluOsInstance.browsePlayList(browseKey)
                                supportFragmentManager
                                        .beginTransaction()
                                        .replace(R.id.fragment_container2, FragmentPlayListList(dataSetPlayList))
                                        .commit()
                            } else {
                                val dataSetItem = bluOsInstance.browse(browseKey)
                                for (item in dataSetItem) {
                                    println("text:" + item.text + " browseKey:" + item.browseKey + " type:" + item.type)
                                }
                                supportFragmentManager
                                        .beginTransaction()
                                        .replace(R.id.fragment_container2, FragmentBrowseList(dataSetItem))
                                        .commit()
                            }
                        }
                        // Display Now Playing Tab
                        tabLayout.getTabAt(2)?.select()
                    }
                }
        )

        // Initialize Album list with Favorite
        thread {
            val bluOsInstance = BluOs()
            val dataSetAlbum = bluOsInstance.browseAlbum("/Albums?service=Qobuz&browseIsFavouritesContext=1&category=FAVOURITES&end=100")
            for (album in dataSetAlbum) {
                println("Artist:" + album.artist + " Album:" + album.title + " AlbumId:" + album.albumId + " ArtistId :" + album.artistId + " Date :" + album.date + " Qualitu :" + album.quality)
            }
            supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.fragment_container, FragmentAlbumList(dataSetAlbum))
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
        // var SelectedAlbumxx = 0
    }
}
