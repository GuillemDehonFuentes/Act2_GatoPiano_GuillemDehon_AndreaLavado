package com.example.gatopiano

import android.annotation.SuppressLint
import android.media.SoundPool
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

        // Configurar SoundPool
        soundPool =
            SoundPool.Builder().setMaxStreams(28).build()

        // Cargar sonidos
        loadSounds()

        // Configurar listeners para todas las teclas
        setupKeyListeners()
    }

    @SuppressLint("DiscouragedApi")
    private fun loadSounds() {
        // Lista de nombres de archivos sin extensión
        val notes = listOf("miauC1", "miauC#1", "miauD1", "miauD#1", "miauE1", "miauF1", "miauF#1",
            "miauG1", "miauG#1", "miauA1", "miauA#1", "miauB1", "miauC2", "miauC#2", "miauD2",
            "miauD#2", "miauE2", "miauF2", "miauF#2", "miauG2", "miauG#2", "miauA2", "miauA#2",
            "miauB2", "miauC3", "miauC#3", "miauD3", "miauE3")
        notes.forEach { note ->
            val resId = resources.getIdentifier(note, "raw", packageName)
            if (resId != 0) {
                soundMap[note] = soundPool.load(this, resId, 1)
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupKeyListeners() {
        val allKeys = listOf(
            // Octava 3
            R.id.key_c3,
            R.id.key_c_sharp3,
            R.id.key_d3,
            R.id.key_d_sharp3,
            R.id.key_e3,
            R.id.key_f3,
            R.id.key_f_sharp3,
            R.id.key_g3,
            R.id.key_g_sharp3,
            R.id.key_a3,
            R.id.key_a_sharp3,
            R.id.key_b3,

            // Octava 4
            R.id.key_c4,
            R.id.key_c_sharp4,
            R.id.key_d4,
            R.id.key_d_sharp4,
            R.id.key_e4,
            R.id.key_f4,
            R.id.key_f_sharp4,
            R.id.key_g4,
            R.id.key_g_sharp4,
            R.id.key_a4,
            R.id.key_a_sharp4,
            R.id.key_b4,

            // Octava 5
            R.id.key_c5,
            R.id.key_d5,
            R.id.key_e5,
            R.id.key_c_sharp5
        )

        allKeys.forEach { id ->
            findViewById<View>(id).setOnTouchListener { view, event ->
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        playSound(view.tag as String)
                        view.setBackgroundResource(
                            if (id.toString().contains("sharp"))
                                R.drawable.black_key_pressed
                            else
                                R.drawable.white_key_pressed
                        )
                    }
                    MotionEvent.ACTION_UP -> {
                        view.setBackgroundResource(
                            if (id.toString().contains("sharp"))
                                R.drawable.black_key
                            else
                                R.drawable.white_key
                        )
                    }
                }
                true
            }
        }

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
        soundMap[note]?.let { soundId ->
            soundPool.play(soundId, 1.0f, 1.0f, 1, 0, 1.0f)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        soundPool.release()
    }
}