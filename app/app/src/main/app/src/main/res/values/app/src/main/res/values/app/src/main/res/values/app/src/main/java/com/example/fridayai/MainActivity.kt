package com.example.fridayai

import android.app.Activity
import android.os.Bundle
import android.graphics.Color
import android.view.Gravity
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView

class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val layout = LinearLayout(this)
        layout.orientation = LinearLayout.VERTICAL
        layout.setPadding(30, 40, 30, 30)
        layout.setBackgroundColor(Color.rgb(7, 16, 24))

        val title = TextView(this)
        title.text = "F R I D A Y  AI"
        title.textSize = 28f
        title.setTextColor(Color.rgb(85, 214, 255))
        title.gravity = Gravity.CENTER
        layout.addView(title)

        val status = TextView(this)
        status.text = "FRIDAY is ready."
        status.textSize = 18f
        status.setTextColor(Color.WHITE)
        status.setPadding(0, 40, 0, 40)
        layout.addView(status)

        val input = EditText(this)
        input.hint = "Ask FRIDAY..."
        input.setTextColor(Color.WHITE)
        input.setHintTextColor(Color.GRAY)
        layout.addView(input)

        val button = Button(this)
        button.text = "ASK FRIDAY"

        button.setOnClickListener {
            val question = input.text.toString().trim()

            if (question.isEmpty()) {
                status.text = "Please type something."
            } else {
                status.text = "You asked:\n\n$question"
            }
        }

        layout.addView(button)

        setContentView(layout)
    }
}
override fun onBackPressed() {
    // Back button दबाने पर कुछ नहीं होगा
}   layout.addView(button)

        setContentView(layout)
    }

    override fun onBackPressed() {
        // Back button दबाने पर app बंद नहीं होगा
    }
}
