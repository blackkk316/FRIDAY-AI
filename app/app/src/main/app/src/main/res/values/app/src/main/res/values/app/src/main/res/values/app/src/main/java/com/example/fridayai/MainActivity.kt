package com.example.fridayai

import android.app.Activity
import android.os.Bundle
import android.graphics.Color
import android.widget.TextView

class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        try {
            val text = TextView(this)
            text.text = "FRIDAY AI\n\nApp is working!"
            text.textSize = 24f
            text.setTextColor(Color.WHITE)
            text.setBackgroundColor(Color.rgb(7, 16, 24))
            text.setPadding(30, 100, 30, 30)

            setContentView(text)

        } catch (e: Exception) {
            val error = TextView(this)
            error.text = "FRIDAY ERROR:\n\n${e.javaClass.name}\n\n${e.message}"
            error.textSize = 18f
            error.setTextColor(Color.RED)
            error.setPadding(30, 50, 30, 30)

            setContentView(error)
        }
    }
}

