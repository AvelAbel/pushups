package com.abelflynn.pushups

import android.content.Context
import android.content.Intent
import android.os.Bundle
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
        val soundSpinner: Spinner = findViewById(R.id.soundSpinner) // замените id на id вашего Spinner для звуков
        val startButton: Button = findViewById(R.id.startButton)

        // Восстановление выбранного режима, количества отжиманий и звука
        val sharedPref = getPreferences(Context.MODE_PRIVATE) ?: return
        selectedPushUps = sharedPref.getInt("selectedPushUps", 1)
        selectedMode = sharedPref.getString("selectedMode", "Средний") ?: "Средний"
        selectedSound = sharedPref.getString("selectedSound", "Капля") ?: "Капля" // по умолчанию выбрана "Капля"

        val soundArray = resources.getStringArray(R.array.sound_array) // замените на массив со значениями звуков
        ArrayAdapter.createFromResource(
            this,
            R.array.sound_array,  // замените на массив со значениями звуков
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            soundSpinner.adapter = adapter
            soundSpinner.setSelection(soundArray.indexOf(selectedSound)) // Установка начального положения
        }

        soundSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                selectedSound = soundArray[position]
                // Сохранение выбранного звука
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
        pushUpsSpinner.setSelection(selectedPushUps - 1) // Установка начального положения

        val modeArray = resources.getStringArray(R.array.mode_array)
        ArrayAdapter.createFromResource(
            this,
            R.array.mode_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            modeSpinner.adapter = adapter
            modeSpinner.setSelection(modeArray.indexOf(selectedMode)) // Установка начального положения
        }

        // Обработка выбора в спиннерах
        pushUpsSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                selectedPushUps = position + 1 // Позиция начинается с 0, отжимания - с 1
                // Сохранение выбранного количества отжиманий
                with(sharedPref.edit()) {
                    putInt("selectedPushUps", selectedPushUps)
                    apply()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        modeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                selectedMode = modeArray[position]
                // Сохранение выбранного режима
                with(sharedPref.edit()) {
                    putString("selectedMode", selectedMode)
                    apply()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        startButton.setOnClickListener {
            val intent = Intent(this, ExerciseActivity::class.java)
            intent.putExtra("pushUps", selectedPushUps)
            intent.putExtra("mode", selectedMode)
            intent.putExtra("soundSelection", selectedSound)
            startActivity(intent)
        }
    }
}
