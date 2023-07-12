package com.abelflynn.pushups

import android.media.MediaPlayer
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.ImageView
import android.widget.TextView

class ExerciseActivity : AppCompatActivity() {

    private lateinit var pushupImageView: ImageView
    private lateinit var counterTextView: TextView
    private lateinit var soundSelection: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_exercise)

        val pushUps = intent.getIntExtra("pushUps", 0)
        val mode = intent.getStringExtra("mode") ?: "Средний"
        soundSelection = intent.getStringExtra("soundSelection") ?: "Нет"


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

                        // Play sound when reaching 1
                        playSound(soundSelection, true)
                    } else {
                        counter++
                    }
                } else {
                    if (counter >= fallDuration) {
                        isRising = true
                        counter = 0

                        // Play sound when reaching 0
                        playSound(soundSelection, false)
                    } else {
                        counter++
                    }
                }
                handler.postDelayed(this, 1)
            }
        })
    }

    private fun playSound(selection: String, isRise: Boolean) {
        Log.d("PlaySound", "playSound called with selection: $selection, isRise: $isRise")

        val sound = when (selection) {
            "Виолончель" -> if (isRise) R.raw.viol2 else R.raw.viol1
            "Клик" -> if (isRise) R.raw.click2 else R.raw.click1
            "Капля" -> if (isRise) R.raw.bulk2 else R.raw.bulk1
            "Дыхание" -> if (isRise) R.raw.vdyh else R.raw.vydoh
            "Опасность" -> if (isRise) R.raw.puk2 else R.raw.puk1
            else -> null
        }

        Log.d("PlaySound", "Selected sound: $sound")

        if (sound != null) {
            Log.d("PlaySound", "Creating and playing MediaPlayer with sound: $sound")
            val mediaPlayer = MediaPlayer.create(applicationContext, sound)
            mediaPlayer.setOnCompletionListener {
                Log.d("PlaySound", "MediaPlayer completed playing sound: $sound")
                it.release()
            }
            mediaPlayer.start()
        } else {
            Log.d("PlaySound", "Sound was null, not playing any sound")
        }
    }


}
