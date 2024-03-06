package com.example.quizzapp

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

data class QuizQuestion(
    val text: String,
    val optionA: String,
    val optionB: String,
    val optionC: String,
    val optionD: String,
    val correctOption: String,
    var incorrectOptions: MutableList<String> = mutableListOf(),
    var hasUsedHint: Boolean = false,
    var hasAnswered: Boolean = false,
    val theme: String
)

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

    private val allQuestions: List<QuizQuestion> = listOf(
        QuizQuestion("¿Cuánto es 2+2?", "A)4", "B)5", "C)6", "D)7", "A", mutableListOf("B", "C", "D"), false, false, "Matematicas"),
        QuizQuestion("¿Cuánto es 2+3?", "A)4", "B)5", "C)6", "D)7", "B", mutableListOf("A", "C", "D"), false, false, "Matematicas"),
        QuizQuestion("¿Cuánto es 2+4?", "A)4", "B)5", "C)6", "D)7", "C", mutableListOf("A", "B", "D"), false, false, "Matematicas"),
        QuizQuestion("¿Cuánto es 2+5?", "A)4", "B)5", "C)6", "D)7", "D", mutableListOf("A", "B", "C"), false, false, "Matematicas"),
        QuizQuestion("¿Cuánto es 1+3?", "A)4", "B)5", "C)6", "D)7", "A", mutableListOf("B", "C", "D"), false, false, "Matematicas"),
        QuizQuestion("¿Cuál es la capital de Mexico?", "A)CDMX", "B)FRANCIA", "C)TOKYO", "D)MADRID", "A", mutableListOf("B", "C", "D"), false, false, "Geografia"),
        QuizQuestion("¿Cuál es la capital de Francia?", "A)CDMX", "B)FRANCIA", "C)TOKYO", "D)MADRID", "B", mutableListOf("A", "C", "D"), false, false, "Geografia"),
        QuizQuestion("¿Cuál es la capital de Japon?", "A)CDMX", "B)FRANCIA", "C)TOKYO", "D)MADRID", "C", mutableListOf("A", "B", "D"), false, false, "Geografia"),
        QuizQuestion("¿Cuál es la capital de España?", "A)CDMX", "B)FRANCIA", "C)TOKYO", "D)MADRID", "D", mutableListOf("A", "B", "C"), false, false, "Geografia"),
        QuizQuestion("¿Cuál es la capital de Inglaterra?", "A)LONDRES", "B)FRANCIA", "C)TOKYO", "D)MADRID", "A", mutableListOf("B", "C", "D"), false, false, "Geografia"),
    )

    private var questions: List<QuizQuestion> = emptyList()
    private var currentQuestion: Int = 0
    private var hintCount: Int = 5
    private var correctAnswersInARow: Int = 0

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
        aButton.setOnClickListener { checkAnswer("A") }
        bButton.setOnClickListener { checkAnswer("B") }
        cButton.setOnClickListener { checkAnswer("C") }
        dButton.setOnClickListener { checkAnswer("D") }
        nextButton.setOnClickListener { nextQuestion() }
        prevButton.setOnClickListener { prevQuestion() }
        hintButton.setOnClickListener { useHint() }

        nextQuestionSet()
    }

    private fun nextQuestionSet() {
        questions = allQuestions.shuffled().take(10)
        currentQuestion = 0
        hintCount = 5
        correctAnswersInARow = 0
        updateUI()
    }

    private fun nextQuestion() {
        resetButtonColors()
        currentQuestion = (currentQuestion + 1) % questions.size
        updateUI()
    }

    private fun prevQuestion() {
        resetButtonColors()
        currentQuestion = (currentQuestion - 1 + questions.size) % questions.size
        updateUI()
    }

    private fun updateUI() {
        val current = currentQuestion + 1
        val total = questions.size
        val question = questions[currentQuestion]

        questionText.text = question.text
        themeText.text = question.theme

        // Mezclar las opciones de respuesta
        val shuffledOptions = listOf(
            question.optionA,
            question.optionB,
            question.optionC,
            question.optionD
        ).shuffled()

        // Asignar las opciones mezcladas a los botones
        val correctOptionIndex = shuffledOptions.indexOf(question.optionA)
        aButton.text = shuffledOptions[0]
        bButton.text = shuffledOptions[1]
        cButton.text = shuffledOptions[2]
        dButton.text = shuffledOptions[3]

        // Obtener el botón de la respuesta correcta después de mezclar
        val correctButton = when (correctOptionIndex) {
            0 -> aButton
            1 -> bButton
            2 -> cButton
            3 -> dButton
            else -> null
        }

        questionNumberText.text = "Pregunta $current"
        totalQuestionsText.text = "de $total"
        hintCountText.text = "Pistas: $hintCount"

        // Enable buttons only if the question hasn't been answered
        if (!question.hasAnswered) {
            enableAllButtons()
        } else {
            disableAllButtons()
            val selectedButton = getButtonById(getButtonIdByOption(question.correctOption))
            setColorForButton(selectedButton, getColorForOption(true))
        }
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
        val question = questions[currentQuestion]
        val isCorrect = selectedOption == question.correctOption

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

    private fun useHint() {
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
    }
}