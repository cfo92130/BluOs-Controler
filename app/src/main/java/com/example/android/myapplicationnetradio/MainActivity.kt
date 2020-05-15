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


package com.example.android.myapplicationnetradio

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ViewAnimator
import androidx.appcompat.app.AppCompatActivity
import com.example.android.common.activities.SampleActivityBase
import com.example.android.common.logger.Log
import com.example.android.common.logger.LogFragment
import com.example.android.common.logger.LogWrapper
import com.example.android.common.logger.MessageOnlyLogFilter
import com.google.android.material.tabs.TabLayout

/**
 * A simple launcher activity containing a summary sample description, sample log and a custom
 * [android.support.v4.app.Fragment] which can display a view.
 *
 *
 * For devices with displays with a width of 720dp or greater, the sample log is always visible,
 * on other devices it's visibility is controlled by an item on the Action Bar.
 */

class Radio (
        val title : String,
        val websiteUrl : String,
        val sources : List<String>
)

class MainActivity : SampleActivityBase() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val tabLayout = findViewById<TabLayout>(R.id.tabLayout)
        tabLayout.addOnTabSelectedListener( object: TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                // get the current selected tab's position and replace the View accordingly
                println("Tab position = "+ tab.position )
                val output = findViewById<ViewAnimator>(R.id.viewAnimator) as ViewAnimator
                output.displayedChild = tab.position
            }
            override fun onTabUnselected(tab: TabLayout.Tab) {
            }
            override fun onTabReselected(tab: TabLayout.Tab) {
            }
        })
        //initializeLogging();
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



    /** Create a chain of targets that will receive log data  */
    override fun initializeLogging() {
        // Wraps Android's native log framework.
        val logWrapper = LogWrapper()

        // Using Log, front-end to the logging chain, emulates android.util.log method signatures.
        Log.logNode = logWrapper

        // Filter strips out everything except the message text.
        val msgFilter = MessageOnlyLogFilter()
        logWrapper.next = msgFilter

        // On screen logging via a fragment with a TextView.

        val logFragment = supportFragmentManager.findFragmentById(R.id.log_fragment) as LogFragment
        msgFilter.next = logFragment.logView

        Log.i(TAG, "Ready")
    }

    companion object {
        val TAG = "MainActivity"
        var CurrentSelectView : View? = null
    }
}
