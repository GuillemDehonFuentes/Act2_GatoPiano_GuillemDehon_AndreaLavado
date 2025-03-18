package com.example.gatopiano

import android.annotation.SuppressLint
import android.media.SoundPool
import android.media.AudioAttributes
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var soundPool: SoundPool
    private val soundMap = mutableMapOf<String, Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        soundPool = SoundPool.Builder().setMaxStreams(28).build()
        soundPool.setOnLoadCompleteListener { _, _, status ->
            if (status == 0) {
                android.util.Log.d("SoundPool", "All sounds loaded")
            }
        }

        loadSounds()
        setupKeyListeners()
    }

    @SuppressLint("DiscouragedApi")
    private fun loadSounds() {
        val notes = listOf(
            "miau_c_1", "miau_c_sharp_1", "miau_d_1", "miau_d_sharp_1",
            "miau_e_1", "miau_f_1", "miau_f_sharp_1", "miau_g_1",
            "miau_g_sharp_1", "miau_a_1", "miau_a_sharp_1", "miau_b_1",
            "miau_c_2", "miau_c_sharp_2", "miau_d_2", "miau_d_sharp_2",
            "miau_e_2", "miau_f_2", "miau_f_sharp_2", "miau_g_2",
            "miau_g_sharp_2", "miau_a_2", "miau_a_sharp_2", "miau_b_2",
            "miau_c_3", "miau_c_sharp_3", "miau_d_3", "miau_e_3"
        )

        notes.forEach { note ->
            val resId = resources.getIdentifier(note, "raw", packageName)
            if (resId != 0) {
                soundMap[note] = soundPool.load(this, resId, 1)
                android.util.Log.d("SoundLoad", "Loaded $note with resId: $resId")
            } else {
                android.util.Log.e("SoundLoad", "Failed to load: $note")
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupKeyListeners() {
        val allKeys = listOf(
            R.id.key_c3, R.id.key_c_sharp3, R.id.key_d3, R.id.key_d_sharp3,
            R.id.key_e3, R.id.key_f3, R.id.key_f_sharp3, R.id.key_g3,
            R.id.key_g_sharp3, R.id.key_a3, R.id.key_a_sharp3, R.id.key_b3,
            R.id.key_c4, R.id.key_c_sharp4, R.id.key_d4, R.id.key_d_sharp4,
            R.id.key_e4, R.id.key_f4, R.id.key_f_sharp4, R.id.key_g4,
            R.id.key_g_sharp4, R.id.key_a4, R.id.key_a_sharp4, R.id.key_b4,
            R.id.key_c5, R.id.key_d5, R.id.key_e5, R.id.key_c_sharp5
        )

        allKeys.forEach { id ->
            findViewById<View>(id).setOnTouchListener { view, event ->
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        playSound(view.tag as String)
                        view.background = getPressedBackground(view.id)
                        true
                    }
                    MotionEvent.ACTION_UP -> {
                        view.background = getNormalBackground(view.id)
                        true
                    }
                    else -> false
                }
            }
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun getNormalBackground(id: Int) = when (id) {
        // Teclas blancas (17)
        R.id.key_c3,
        R.id.key_d3,
        R.id.key_e3,
        R.id.key_f3,
        R.id.key_g3,
        R.id.key_a3,
        R.id.key_b3,
        R.id.key_c4,
        R.id.key_d4,
        R.id.key_e4,
        R.id.key_f4,
        R.id.key_g4,
        R.id.key_a4,
        R.id.key_b4,
        R.id.key_c5,
        R.id.key_d5,
        R.id.key_e5 -> getDrawable(R.drawable.white_key)

        // Teclas negras (11 - else)
        else -> getDrawable(R.drawable.black_key)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun getPressedBackground(id: Int) = when (id) {
        // Teclas blancas (17)
        R.id.key_c3,
        R.id.key_d3,
        R.id.key_e3,
        R.id.key_f3,
        R.id.key_g3,
        R.id.key_a3,
        R.id.key_b3,
        R.id.key_c4,
        R.id.key_d4,
        R.id.key_e4,
        R.id.key_f4,
        R.id.key_g4,
        R.id.key_a4,
        R.id.key_b4,
        R.id.key_c5,
        R.id.key_d5,
        R.id.key_e5 -> getDrawable(R.drawable.white_key_pressed)

        // Teclas negras (11 - else)
        else -> getDrawable(R.drawable.black_key_pressed)
    }

    private fun playSound(note: String) {
        val soundId = soundMap[note]
        if (soundId == null || soundId == 0) {
            android.util.Log.e("SoundError", "Sound not loaded for note: $note")
            return
        }
        soundPool.play(soundId, 1.0f, 1.0f, 1, 0, 1.0f)
    }

    override fun onDestroy() {
        super.onDestroy()
        soundPool.release()
    }
}