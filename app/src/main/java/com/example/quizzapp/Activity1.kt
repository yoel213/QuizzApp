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
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase

class Activity1 : AppCompatActivity() {
    private lateinit var playButton: Button
    private lateinit var optionButton: Button
    private var posSelected: Int = 0
    private val MAINACTIVITY_REQUEST_CODE = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_1)

        val db = Room.databaseBuilder(
            applicationContext,
            QuestionDataBase::class.java, "ay-library"
        ).allowMainThreadQueries().addCallback(object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                db.execSQL("INSERT INTO Questions (question_id,question,optionA, optionB,optionC,optionD,theme_id)" +
                            "VALUES (0,'¿Cuánto es 2+2?','A)4','B)5','C)6','D)7',0)")
                db.execSQL("INSERT INTO Questions (question_id,question,optionA, optionB,optionC,optionD,theme_id)" +
                            "VALUES (1,'¿Cuánto es 2+3?','A)5','B)4','C)6','D)7',0)")
                db.execSQL("INSERT INTO Questions (question_id,question,optionA, optionB,optionC,optionD,theme_id)" +
                        "VALUES (2,'¿Cuánto es 2+4?','A)6','B)4','C)5','D)7',0)")
                db.execSQL("INSERT INTO Questions (question_id,question,optionA, optionB,optionC,optionD,theme_id)" +
                        "VALUES (3,'¿Cuánto es 2+5?','A)7','B)4','C)5','D)6',0)")
                db.execSQL("INSERT INTO Questions (question_id,question,optionA, optionB,optionC,optionD,theme_id)" +
                        "VALUES (4,'¿Cuánto es 1+3?','A)4','B)5','C)6','D)7',0)")
                db.execSQL("INSERT INTO Questions (question_id,question,optionA, optionB,optionC,optionD,theme_id)" +
                        "VALUES (5,'¿Cuál es la capital de Mexico?','A)CDMX','B)FRANCIA','C)TOKYO','D)MADRID',1)")
                db.execSQL("INSERT INTO Questions (question_id,question,optionA, optionB,optionC,optionD,theme_id)" +
                        "VALUES (6,'¿Cuál es la capital de Francia?','A)FRANCIA','B)CDMX','C)TOKYO','D)MADRID',1)")
                db.execSQL("INSERT INTO Questions (question_id,question,optionA, optionB,optionC,optionD,theme_id)" +
                        "VALUES (7,'¿Cuál es la capital de Japon?','A)TOKYO','B)FRANCIA','C)CDMX','D)MADRID',1)")
                db.execSQL("INSERT INTO Questions (question_id,question,optionA, optionB,optionC,optionD,theme_id)" +
                        "VALUES (8,'¿Cuál es la capital de España?','A)MADRID','B)FRANCIA','C)TOKYO','D)CDMX',1)")
                db.execSQL("INSERT INTO Questions (question_id,question,optionA, optionB,optionC,optionD,theme_id)" +
                        "VALUES (9,'¿Cuál es la capital de Inglaterra?','A)LONDRES','B)FRANCIA','C)TOKYO','D)MADRID',1)")

                db.execSQL("INSERT INTO Theme (theme_id,theme)" +
                        "VALUES (0,'Matematicas')")
                db.execSQL("INSERT INTO Theme (theme_id,theme)" +
                        "VALUES (1,'Geografia')")


            }
        }).build()
        val questions = db.questionandsolverdao()
        val questionarray = questions.getAllQuestions()

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