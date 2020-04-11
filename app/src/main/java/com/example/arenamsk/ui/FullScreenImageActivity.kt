package com.example.arenamsk.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.arenamsk.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_full_screen_image.*

class FullScreenImageActivity : AppCompatActivity() {

    companion object {
        private const val ARG = "arg"

        fun getIntent(context: Context, imageUrl: String) = Intent(context,
            FullScreenImageActivity::class.java).apply {
            putExtra(ARG, imageUrl)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_full_screen_image)

        Picasso.get()
            .load(intent.getStringExtra(ARG))
            .into(image_container)
    }
}
