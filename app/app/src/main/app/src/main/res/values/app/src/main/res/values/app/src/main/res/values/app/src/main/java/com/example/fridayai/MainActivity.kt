package com.example.fridayai

import android.app.Activity
import android.os.Bundle
import android.widget.TextView

class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val text = TextView(this)
        text.text = "FRIDAY AI\n\nApp is working!"
        text.textSize = 24f
        text.setPadding(30, 100, 30, 30)

        setContentView(text)
    }
}
