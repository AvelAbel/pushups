package com.abelflynn.pushups

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private var selectedPushUps = 1
    private var selectedMode = ""
    private var selectedSound = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_main)

        val pushUpsSpinner: Spinner = findViewById(R.id.pushUpsSpinner)
        val modeSpinner: Spinner = findViewById(R.id.modeSpinner)
        val soundSpinner: Spinner = findViewById(R.id.soundSpinner)
        val startButton: Button = findViewById(R.id.startButton)

        val sharedPref = getPreferences(Context.MODE_PRIVATE) ?: return
        selectedPushUps = sharedPref.getInt("selectedPushUps", 1)
        selectedMode = sharedPref.getString("selectedMode", "Средний") ?: "Средний"
        selectedSound = sharedPref.getString("selectedSound", "Капля") ?: "Капля"

        val soundArray = resources.getStringArray(R.array.sound_array)
        ArrayAdapter.createFromResource(
            this,
            R.array.sound_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            soundSpinner.adapter = adapter
            soundSpinner.setSelection(soundArray.indexOf(selectedSound))
        }

        soundSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedSound = soundArray[position]
                with(sharedPref.edit()) {
                    putString("selectedSound", selectedSound)
                    apply()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        val pushUpsArray = Array(200) { i -> (i + 1).toString() }
        val pushUpsAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, pushUpsArray)
        pushUpsSpinner.adapter = pushUpsAdapter
        pushUpsSpinner.setSelection(selectedPushUps - 1)

        val modeArray = resources.getStringArray(R.array.mode_array)
        ArrayAdapter.createFromResource(
            this,
            R.array.mode_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            modeSpinner.adapter = adapter
            modeSpinner.setSelection(modeArray.indexOf(selectedMode))
        }

        pushUpsSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedPushUps = position + 1
                with(sharedPref.edit()) {
                    putInt("selectedPushUps", selectedPushUps)
                    apply()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        modeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                selectedMode = modeArray[position]
                with(sharedPref.edit()) {
                    putString("selectedMode", selectedMode)
                    apply()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        startButton.setOnClickListener {
            startButton.isEnabled = false
            object: CountDownTimer(4000, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    startButton.text = "Старт через ${millisUntilFinished / 1000}"
                }
                override fun onFinish() {
                    val intent = Intent(this@MainActivity, ExerciseActivity::class.java)
                    intent.putExtra("pushUps", selectedPushUps)
                    intent.putExtra("mode", selectedMode)
                    intent.putExtra("soundSelection", selectedSound)
                    startActivity(intent)
                    startButton.isEnabled = true
                    startButton.text = "Начать"
                }
            }.start()
        }
    }
}
