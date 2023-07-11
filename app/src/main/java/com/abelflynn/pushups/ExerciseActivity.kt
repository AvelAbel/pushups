package com.abelflynn.pushups

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import android.os.Handler
import android.os.Looper

class ExerciseActivity : AppCompatActivity() {

    private lateinit var scaleView: ScaleView
    private lateinit var counterTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercise)

        val pushUps = intent.getIntExtra("pushUps", 0)
        val mode = intent.getStringExtra("mode")

        var riseDuration: Long = 200L // значение по умолчанию

        when (mode) {
            "Медленный" -> riseDuration = 300L
            "Средний" -> riseDuration = 200L
            "Быстрый" -> riseDuration = 100L
        }

        scaleView = findViewById(R.id.scaleView)
        counterTextView = findViewById(R.id.counterTextView)
        counterTextView.text = "0"

        // Начинаем с полной шкалы
        scaleView.setProgress(1f)

        val handler = Handler(Looper.getMainLooper())
        var isRising = false // Теперь начинаем с падения
        var counter = 0
        val fallDuration = riseDuration / 2 // продолжительность падения в миллисекундах

        handler.post(object : Runnable {
            override fun run() {
                val currentProgress = scaleView.getProgress()
                if (isRising) {
                    scaleView.setProgress(currentProgress + 1f / riseDuration)
                    if (currentProgress >= 1) {
                        isRising = false
                        counter++
                        counterTextView.text = counter.toString()
                    }
                } else {
                    scaleView.setProgress(currentProgress - 1f / fallDuration)
                    if (currentProgress <= 0) {
                        isRising = true
                        if (counter >= pushUps) {
                            return
                        }
                    }
                }
                handler.postDelayed(this, 1)
            }
        })
    }
}
