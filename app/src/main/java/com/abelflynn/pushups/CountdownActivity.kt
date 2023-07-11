package com.abelflynn.pushups

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class CountdownActivity : AppCompatActivity() {

    private lateinit var countdownTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_countdown)

        val pushUps = intent.getIntExtra("pushUps", 0)
        val mode = intent.getStringExtra("mode")

        countdownTextView = findViewById(R.id.countdownTextView)

        val handler = Handler(Looper.getMainLooper())
        val countdownRunnable = object : Runnable {
            var countdown = 3

            override fun run() {
                if (countdown >= 1) {
                    countdownTextView.text = countdown.toString()
                    countdown--
                    handler.postDelayed(this, 1000)
                } else {
                    val intent = Intent(this@CountdownActivity, ExerciseActivity::class.java)
                    intent.putExtra("pushUps", pushUps)
                    intent.putExtra("mode", mode)
                    startActivity(intent)
                    finish()
                }
            }
        }

        handler.post(countdownRunnable)
    }
}
