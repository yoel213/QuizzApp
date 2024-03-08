package com.example.quizzapp

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

data class QuizQuestion(
    val id :Int,
    val text: String,
    val optionA: String,
    val optionB: String,
    val optionC: String,
    val optionD: String,
    val correctOption: String,
    var hasUsedHint: Boolean = false,
    var hasAnswered: Boolean = false,
    val theme: String
)
data class optionsAnswer(
    val id: Int,
    val optionA: String,
    val optionB: String,
    val optionC: String,
    val optionD: String
)
const val MAINACTIVITY_SELECT_SPINNER = "MAINACTIVITY_SELECT_SPINNER"
class MainActivity : AppCompatActivity() {

    private lateinit var questionText: TextView
    private lateinit var aButton: Button
    private lateinit var bButton: Button
    private lateinit var cButton: Button
    private lateinit var dButton: Button
    private lateinit var nextButton: Button
    private lateinit var prevButton: Button
    private lateinit var questionNumberText: TextView
    private lateinit var totalQuestionsText: TextView
    private lateinit var hintButton: Button
    private lateinit var hintCountText: TextView
    private lateinit var themeText: TextView
    private var questions: MutableList<QuizQuestion> = emptyList<QuizQuestion>().toMutableList()
    private var currentQuestion: Int = 0
    private var hintCount: Int = 5
    private var correctAnswersInARow: Int = 0
    private var posSelect :Int =0
    private var posiblesrespuestas: MutableList<optionsAnswer> = emptyList<optionsAnswer>().toMutableList()


    private val allQuestions: List<QuizQuestion> = listOf(
        QuizQuestion(0,"¿Cuánto es 2+2?", "A)4", "B)5", "C)6", "D)7", "A", false, false, "Matematicas"),
        QuizQuestion(1,"¿Cuánto es 2+3?", "A)4", "B)5", "C)6", "D)7", "B",  false, false, "Matematicas"),
        QuizQuestion(2,"¿Cuánto es 2+4?", "A)4", "B)5", "C)6", "D)7", "C",  false, false, "Matematicas"),
        QuizQuestion(3,"¿Cuánto es 2+5?", "A)4", "B)5", "C)6", "D)7", "D",  false, false, "Matematicas"),
        QuizQuestion(4,"¿Cuánto es 1+3?", "A)4", "B)5", "C)6", "D)7", "A", false, false, "Matematicas"),
        QuizQuestion(5,"¿Cuál es la capital de Mexico?", "A)CDMX", "B)FRANCIA", "C)TOKYO", "D)MADRID", "A", false, false, "Geografia"),
        QuizQuestion(6,"¿Cuál es la capital de Francia?", "A)CDMX", "B)FRANCIA", "C)TOKYO", "D)MADRID", "B",  false, false, "Geografia"),
        QuizQuestion(7,"¿Cuál es la capital de Japon?", "A)CDMX", "B)FRANCIA", "C)TOKYO", "D)MADRID", "C", false, false, "Geografia"),
        QuizQuestion(8,"¿Cuál es la capital de España?", "A)CDMX", "B)FRANCIA", "C)TOKYO", "D)MADRID", "D",  false, false, "Geografia"),
        QuizQuestion(9,"¿Cuál es la capital de Inglaterra?", "A)LONDRES", "B)FRANCIA", "C)TOKYO", "D)MADRID", "A", false, false, "Geografia"),
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        questionText = findViewById(R.id.question_text)
        aButton = findViewById(R.id.a_button)
        bButton = findViewById(R.id.b_button)
        cButton = findViewById(R.id.c_button)
        dButton = findViewById(R.id.d_button)
        nextButton = findViewById(R.id.next_button)
        prevButton = findViewById(R.id.prev_button)
        questionNumberText = findViewById(R.id.question_number)
        totalQuestionsText = findViewById(R.id.total_questions)
        hintButton = findViewById(R.id.hint_button)
        hintCountText = findViewById(R.id.hint_count)
        themeText = findViewById(R.id.theme_text)

        // Set up button click listeners
        aButton.setOnClickListener { checkAnswer("${posiblesrespuestas[currentQuestion].optionA}") }
        bButton.setOnClickListener { checkAnswer("${posiblesrespuestas[currentQuestion].optionB}") }
        cButton.setOnClickListener { checkAnswer("${posiblesrespuestas[currentQuestion].optionC}") }
        dButton.setOnClickListener { checkAnswer("${posiblesrespuestas[currentQuestion].optionD}") }
        nextButton.setOnClickListener { nextQuestion() }
        prevButton.setOnClickListener { prevQuestion() }
        //hintButton.setOnClickListener { useHint() }
        nextQuestionSet()
        posSelect=intent.getIntExtra(MAINACTIVITY_SELECT_SPINNER,0)
        optionShuffled()
        updatebutton()
    }

    private fun optionShuffled() {
        for(num in 0 until questions.size){
            val question = questions[num]
            val shuffledOptions = listOf(
                question.optionA,
                question.optionB,
                question.optionC,
                question.optionD
            ).shuffled()
            posiblesrespuestas.add(optionsAnswer(question.id,shuffledOptions[0], shuffledOptions[1],shuffledOptions[2],shuffledOptions[3]))
        }
    }

    private fun nextQuestionSet() {
        questions = allQuestions.shuffled().take(10).toMutableList()
        currentQuestion = 0
        hintCount = 5
        correctAnswersInARow = 0
        updateUI()
    }

    private fun nextQuestion() {
        //resetButtonColors()
        currentQuestion = (currentQuestion + 1) % questions.size
        updateUI()
        updatebutton()
        answercorrect()
    }

    private fun updatebutton() {
        aButton.text = posiblesrespuestas[currentQuestion].optionA.substring(2)
        bButton.text = posiblesrespuestas[currentQuestion].optionB.substring(2)
        cButton.text = posiblesrespuestas[currentQuestion].optionC.substring(2)
        dButton.text = posiblesrespuestas[currentQuestion].optionD.substring(2)
    }

    private fun prevQuestion() {
        //resetButtonColors()
        currentQuestion = (currentQuestion - 1 + questions.size) % questions.size
        updateUI()
        updatebutton()
    }

    private fun updateUI() {
        val current = currentQuestion + 1
        val total = questions.size
        val question = questions[currentQuestion]

        questionText.text = question.text
        themeText.text = question.theme
        questionNumberText.text = "Pregunta $current"
        totalQuestionsText.text = "de $total"
        hintCountText.text = "Pistas: $hintCount"

    }
    private fun answercorrect(){
        var optionCorrect = questions[currentQuestion].correctOption[0]
        Log.d("yoeld","$optionCorrect")



    }





















    private fun resetButtonColors() {
        val buttons = listOf(aButton, bButton, cButton, dButton)

        for (button in buttons) {
            button.setBackgroundColor(ContextCompat.getColor(this, R.color.defaultButtonColor))
        }
    }

    private fun setColorForButton(button: Button, colorResId: Int) {
        button.setBackgroundColor(ContextCompat.getColor(this, colorResId))
    }

    private fun enableAllButtons() {
        val buttons = listOf(aButton, bButton, cButton, dButton)

        for (button in buttons) {
            button.isEnabled = true
        }
    }

    private fun disableAllButtons() {
        val buttons = listOf(aButton, bButton, cButton, dButton)

        for (button in buttons) {
            button.isEnabled = false
        }
    }

    private fun checkAnswer(selectedOption: String) {
        Log.d("yoel1","${selectedOption[0]}")
        val question = questions[currentQuestion]
        Log.d("yoel2","${question.correctOption}")
        val isCorrect = selectedOption[0].toString() == question.correctOption
        Log.d("yoel3","$isCorrect")

        question.hasAnswered = true

        if (isCorrect) {
            question.hasUsedHint = true
            correctAnswersInARow++
            if (correctAnswersInARow % 2 == 0) {
                hintCount++
            }
        } else {
            correctAnswersInARow = 0
        }

        val selectedButton = getButtonById(getButtonIdByOption(selectedOption))
        setColorForButton(selectedButton, getColorForOption(isCorrect))
        resetButtonColorsExcept(selectedButton)

        disableAllButtons()
    }

    private fun resetButtonColorsExcept(selectedButton: Button) {
        val buttons = listOf(aButton, bButton, cButton, dButton)
        for (button in buttons) {
            if (button != selectedButton) {
                button.setBackgroundColor(ContextCompat.getColor(this, R.color.defaultButtonColor))
            }
        }
    }

    private fun getButtonIdByOption(option: String): Int {
        return when (option) {
            "A" -> R.id.a_button
            "B" -> R.id.b_button
            "C" -> R.id.c_button
            "D" -> R.id.d_button
            else -> R.id.a_button
        }
    }

    private fun getButtonById(buttonId: Int): Button {
        return findViewById(buttonId)
    }

    private fun getColorForOption(isCorrect: Boolean): Int {
        return if (isCorrect) R.color.correctAnswer else R.color.incorrectAnswer
    }

    /*private fun useHint() {
        val question = questions[currentQuestion]
        if (!question.hasAnswered && hintCount > 0 && question.incorrectOptions.isNotEmpty()) {
            val incorrectOption = question.incorrectOptions.removeLast()
            val incorrectButton = getButtonById(getButtonIdByOption(incorrectOption.substring(0, 1)))
            incorrectButton.isEnabled = false
            question.hasUsedHint = true
            hintCount--
            hintCountText.text = "Pistas: $hintCount"

            if (question.incorrectOptions.isEmpty()) {
                val correctButton = getButtonById(getButtonIdByOption(question.correctOption))
                setColorForButton(correctButton, getColorForOption(true))
                disableAllButtons()
            }
        }

    }*/
}
/* for (questionid in questions) { con esto cumpruebas que si esta revolviendo las preguntas
 Log.d("yoel","${questionid.id}")
 }*/
