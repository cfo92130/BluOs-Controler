package com.example.android.bluOsControler

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity

class DisplayInfoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_info)

        supportActionBar?.setDisplayHomeAsUpEnabled(true);
        supportActionBar?.title = intent.getStringExtra(INTENT_artist)
        supportActionBar?.subtitle = intent.getStringExtra(INTENT_album)
        val url = intent.getStringExtra(INTENT_url)
        findViewById<WebView>(R.id.web).loadUrl(url)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        private const val INTENT_url = "url"
        private const val INTENT_artist = "artist"
        private const val INTENT_album = "album"
        fun newIntent(context: Context, url : String , artist : String, album: String): Intent {
            val intent = Intent(context, DisplayInfoActivity::class.java)
            intent.putExtra(INTENT_url, url)
            intent.putExtra(INTENT_artist, artist)
            intent.putExtra(INTENT_album, album)
            return intent
        }
    }
}