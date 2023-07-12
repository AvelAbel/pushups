package com.abelflynn.pushups

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import android.widget.TextView

class ExerciseActivity : AppCompatActivity() {

    private lateinit var pushupImageView: ImageView
    private lateinit var counterTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_exercise)

        val pushUps = intent.getIntExtra("pushUps", 0)
        val mode = intent.getStringExtra("mode")

        var riseDuration: Long = 50L // значение по умолчанию

        when (mode) {
            "Медленный" -> riseDuration = 100L
            "Средний" -> riseDuration = 50L
            "Быстрый" -> riseDuration = 20L
        }

        pushupImageView = findViewById(R.id.pushupImageView)
        counterTextView = findViewById(R.id.counterTextView)
        counterTextView.text = "0"

        val handler = Handler(Looper.getMainLooper())
        var isRising = false
        var counter = 0
        val fallDuration = riseDuration * 2 // продолжительность падения в миллисекундах

        handler.post(object : Runnable {
            override fun run() {
                if (counterTextView.text.toString().toInt() >= pushUps) {
                    pushupImageView.setImageResource(R.drawable.pushupsfinal)
                    return
                }

                val currentProgress = if (isRising) counter.toFloat() / riseDuration else 1 - counter.toFloat() / fallDuration
                val imageResource = when {
                    currentProgress >= 0.95 -> R.drawable.pushups95
                    currentProgress >= 0.9 -> R.drawable.pushups90
                    currentProgress >= 0.85 -> R.drawable.pushups85
                    currentProgress >= 0.8 -> R.drawable.pushups80
                    currentProgress >= 0.75 -> R.drawable.pushups75
                    currentProgress >= 0.7 -> R.drawable.pushups70
                    currentProgress >= 0.65 -> R.drawable.pushups65
                    currentProgress >= 0.6 -> R.drawable.pushups60
                    currentProgress >= 0.55 -> R.drawable.pushups55
                    currentProgress >= 0.5 -> R.drawable.pushups50
                    currentProgress >= 0.45 -> R.drawable.pushups45
                    currentProgress >= 0.4 -> R.drawable.pushups40
                    currentProgress >= 0.35 -> R.drawable.pushups35
                    currentProgress >= 0.3 -> R.drawable.pushups30
                    currentProgress >= 0.25 -> R.drawable.pushups25
                    currentProgress >= 0.2 -> R.drawable.pushups20
                    currentProgress >= 0.15 -> R.drawable.pushups15
                    currentProgress >= 0.1 -> R.drawable.pushups10
                    currentProgress >= 0.05 -> R.drawable.pushups5
                    currentProgress >= 0 -> R.drawable.pushups0
                    else -> R.drawable.pushupsfinal
                }
                pushupImageView.setImageResource(imageResource)

                if (isRising) {
                    if (counter >= riseDuration) {
                        isRising = false
                        counter = 0
                        if (counterTextView.text.toString().toInt() < pushUps) {
                            counterTextView.text = (counterTextView.text.toString().toInt() + 1).toString()
                        }
                    } else {
                        counter++
                    }
                } else {
                    if (counter >= fallDuration) {
                        isRising = true
                        counter = 0
                    } else {
                        counter++
                    }
                }
                handler.postDelayed(this, 1)
            }
        })
    }
}
