package com.example.jarvis

import android.app.Activity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.speech.RecognizerIntent
import android.content.Intent
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class MainActivity : AppCompatActivity(), TextToSpeech.OnInitListener {
    private lateinit var tts: TextToSpeech
    private lateinit var btnSpeak: Button
    private lateinit var edtMessage: EditText
    private lateinit var btnSend: Button
    private lateinit var chatView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tts = TextToSpeech(this, this)
        btnSpeak = findViewById(R.id.btnSpeak)
        edtMessage = findViewById(R.id.edtMessage)
        btnSend = findViewById(R.id.btnSend)
        chatView = findViewById(R.id.chatView)

        btnSpeak.setOnClickListener { startVoiceInput() }
        btnSend.setOnClickListener { sendMessage() }
    }

    private fun startVoiceInput() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak now...")
        startActivityForResult(intent, 100)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == Activity.RESULT_OK && data != null) {
            val result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            if (result != null && result.isNotEmpty()) {
                edtMessage.setText(result[0])
            }
        }
    }

    private fun sendMessage() {
        val userMsg = edtMessage.text.toString()
        if (userMsg.isNotEmpty()) {
            chatView.append("You: $userMsg\n")
            val botReply = getBotReply(userMsg)
            chatView.append("Jarvis: $botReply\n")
            speak(botReply)
            edtMessage.text.clear()
        }
    }

    // Placeholder for actual AI response logic
    private fun getBotReply(msg: String): String {
        return "You said: $msg" // Later connect with OpenAI API for smart reply
    }

    private fun speak(text: String) {
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "")
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            tts.language = Locale.getDefault()
        }
    }

    override fun onDestroy() {
        if (tts != null) {
            tts.stop()
            tts.shutdown()
        }
        super.onDestroy()
    }
}