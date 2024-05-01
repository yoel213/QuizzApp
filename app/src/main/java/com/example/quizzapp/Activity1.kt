package com.example.quizzapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast

class Activity1 : AppCompatActivity() {
    private lateinit var playButton: Button
    private lateinit var optionButton: Button
    private var posSelected: Int = 0
    private val MAINACTIVITY_REQUEST_CODE = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_1)
        playButton = findViewById(R.id.play_button)
        optionButton = findViewById(R.id.option_button)
        setupSpinnerBasic()

        playButton.setOnClickListener { v ->
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra(MAINACTIVITY_SELECT_SPINNER, posSelected)
            startActivityForResult(intent, MAINACTIVITY_REQUEST_CODE)
        }

        optionButton.setOnClickListener { _ ->
            val intent = Intent(this, OptionActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupSpinnerBasic() {
        val spinner: Spinner = findViewById(R.id.spinnerDificult)
        val adapter: ArrayAdapter<CharSequence> = ArrayAdapter.createFromResource(
            this,
            R.array.ArraySpinner,
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                posSelected = position
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }


}