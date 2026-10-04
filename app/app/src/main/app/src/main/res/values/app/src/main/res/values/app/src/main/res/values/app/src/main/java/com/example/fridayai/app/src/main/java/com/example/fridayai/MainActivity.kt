package com.example.fridayai

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.speech.tts.TextToSpeech
import android.widget.*
import java.net.HttpURLConnection
import java.net.URL
import java.util.Locale
import kotlin.concurrent.thread
import org.json.JSONArray
import org.json.JSONObject

class MainActivity : Activity(), TextToSpeech.OnInitListener {

    private lateinit var input: EditText
    private lateinit var output: TextView
    private lateinit var apiKey: EditText
    private lateinit var tts: TextToSpeech

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val layout = LinearLayout(this)
        layout.orientation = LinearLayout.VERTICAL
        layout.setPadding(24, 30, 24, 20)
        layout.setBackgroundColor(0xFF071018.toInt())

        val title = TextView(this)
        title.text = "F R I D A Y  AI"
        title.textSize = 26f
        title.setTextColor(0xFF55D6FF.toInt())
        layout.addView(title)

        apiKey = EditText(this)
        apiKey.hint = "OpenAI API key"
        apiKey.setTextColor(0xFFFFFFFF.toInt())
        apiKey.setHintTextColor(0xFF888888.toInt())
        layout.addView(apiKey)

        output = TextView(this)
        output.text = "FRIDAY is ready."
        output.textSize = 17f
        output.setTextColor(0xFFFFFFFF.toInt())
        layout.addView(
            output,
            LinearLayout.LayoutParams(-1, 0, 1f)
        )

        input = EditText(this)
        input.hint = "Ask FRIDAY..."
        input.setTextColor(0xFFFFFFFF.toInt())
        input.setHintTextColor(0xFF888888.toInt())
        layout.addView(input)

        val ask = Button(this)
        ask.text = "ASK FRIDAY"
        ask.setOnClickListener {
            askFriday(input.text.toString())
        }
        layout.addView(ask)

        val mic = Button(this)
        mic.text = "🎙 SPEAK"
        mic.setOnClickListener {
            startVoice()
        }
        layout.addView(mic)

        setContentView(layout)

        tts = TextToSpeech(this, this)

        if (checkSelfPermission(Manifest.permission.RECORD_AUDIO)
            != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(
                arrayOf(Manifest.permission.RECORD_AUDIO),
                100
            )
        }
    }

    private fun startVoice() {
        val intent = Intent(
            RecognizerIntent.ACTION_RECOGNIZE_SPEECH
        )

        intent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )

        startActivityForResult(intent, 200)
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 200 && resultCode == RESULT_OK) {
            val results = data?.getStringArrayListExtra(
                RecognizerIntent.EXTRA_RESULTS
            )

            val text = results?.firstOrNull()

            if (!text.isNullOrBlank()) {
                input.setText(text)
                askFriday(text)
            }
        }
    }

    private fun askFriday(question: String) {

        if (question.isBlank()) return

        val key = apiKey.text.toString().trim()

        if (key.isEmpty()) {
            output.text = "Please enter your OpenAI API key."
            return
        }

        output.text = "FRIDAY is thinking..."

        thread {

            try {

                val connection =
                    URL("https://api.openai.com/v1/responses")
                        .openConnection() as HttpURLConnection

                connection.requestMethod = "POST"
                connection.setRequestProperty(
                    "Authorization",
                    "Bearer $key"
                )
                connection.setRequestProperty(
                    "Content-Type",
                    "application/json"
                )
                connection.doOutput = true

                val json = JSONObject()

                json.put("model", "gpt-5.6")
                json.put("input", question)

                connection.outputStream.use {
                    it.write(
                        json.toString()
                            .toByteArray(Charsets.UTF_8)
                    )
                }

                val responseCode = connection.responseCode

                val stream =
                    if (responseCode in 200..299)
                        connection.inputStream
                    else
                        connection.errorStream

                val response = stream
                    .bufferedReader()
                    .use { it.readText() }

                if (responseCode !in 200..299) {
                    throw Exception(
                        "HTTP $responseCode"
                    )
                }

                val result = JSONObject(response)

                val answer =
                    result.optString(
                        "output_text",
                        "No response received."
                    )

                runOnUiThread {

                    output.text = answer

                    tts.speak(
                        answer,
                        TextToSpeech.QUEUE_FLUSH,
                        null,
                        "friday"
                    )
                }

            } catch (e: Exception) {

                runOnUiThread {
                    output.text =
                        "Error: ${e.message}"
                }
            }
        }
    }

    override fun onInit(status: Int) {

        if (status == TextToSpeech.SUCCESS) {
            tts.language = Locale.getDefault()
        }
    }

    override fun onDestroy() {

        if (::tts.isInitialized) {
            tts.shutdown()
        }

        super.onDestroy()
    }
}
