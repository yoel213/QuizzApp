package com.example.quizzapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.room.Room
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

data class QuizQuestion(
    val id :Int,
    val text: String,
    val optionA: String,
    val optionB: String,
    val optionC: String,
    val optionD: String,
    val correctOption: String,
    var incorrectOptions: MutableList<String> = mutableListOf(),
    var optionSelect :Int,
    var isCorrect: Boolean,
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
        QuizQuestion(0,"¿Cuánto es 2+2?", "A)4", "B)5", "C)6", "D)7", "A", mutableListOf("B", "C", "D"),0,false,false, false, "Matematicas"),
        QuizQuestion(1,"¿Cuánto es 2+3?", "A)5", "B)4", "C)6", "D)7", "A",  mutableListOf("B", "C", "D"),0,false,false, false, "Matematicas"),
        QuizQuestion(2,"¿Cuánto es 2+4?", "A)6", "B)5", "C)4", "D)7", "A",  mutableListOf("B", "C", "D"),0,false,false, false, "Matematicas"),
        QuizQuestion(3,"¿Cuánto es 2+5?", "A)7", "B)5", "C)6", "D)4", "A",  mutableListOf("B", "C", "D"),0,false,false, false, "Matematicas"),
        QuizQuestion(4,"¿Cuánto es 1+3?", "A)4", "B)5", "C)6", "D)7", "A", mutableListOf("B", "C", "D"),0,false,false, false, "Matematicas"),
        QuizQuestion(5,"¿Cuál es la capital de Mexico?", "A)CDMX", "B)FRANCIA", "C)TOKYO", "D)MADRID", "A", mutableListOf("B", "C", "D"),0,false,false, false, "Geografia"),
        QuizQuestion(6,"¿Cuál es la capital de Francia?", "A)FRANCIA", "B)CDMX", "C)TOKYO", "D)MADRID", "A",  mutableListOf("B", "C", "D"),0,false,false, false, "Geografia"),
        QuizQuestion(7,"¿Cuál es la capital de Japon?", "A)TOKIO", "B)FRANCIA", "C)CDMX", "D)MADRID", "A", mutableListOf("B", "C", "D"), 0,false, false, false, "Geografia"),
        QuizQuestion(8,"¿Cuál es la capital de España?", "A)MADRID", "B)FRANCIA", "C)TOKYO", "D)CDMX", "A",  mutableListOf("B", "C", "D"),0,false,false, false, "Geografia"),
        QuizQuestion(9,"¿Cuál es la capital de Inglaterra?", "A)LONDRES", "B)FRANCIA", "C)TOKYO", "D)MADRID", "A", mutableListOf("B", "C", "D"),0,false, false, false, "Geografia"),
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val db =Room.databaseBuilder(
            applicationContext, QuestionDataBase::class.java, "ay-library"
        ).build()
        GlobalScope.launch {
            val questions = db.questionandsolverdao()
            val questionarray = questions.getAllQuestions()
        }

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
        posSelect=intent.getIntExtra(MAINACTIVITY_SELECT_SPINNER,0)
        // Set up button click listeners
        aButton.setOnClickListener { checkAnswer(1,"${posiblesrespuestas[currentQuestion].optionA}") }
        bButton.setOnClickListener { checkAnswer(2, "${posiblesrespuestas[currentQuestion].optionB}") }
        cButton.setOnClickListener { checkAnswer(3,"${posiblesrespuestas[currentQuestion].optionC}") }
        dButton.setOnClickListener { checkAnswer(4,"${posiblesrespuestas[currentQuestion].optionD}") }
        nextButton.setOnClickListener { nextQuestion() }
        prevButton.setOnClickListener { prevQuestion() }
        hintButton.setOnClickListener { useHint() }
        nextQuestionSet()

        optionShuffled()
        updatebutton()
        enableAllButtons()
        disableAllButtons()
        checkAswerRequest()
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
        resetButtonColors()
        checkAllAnswred()
        currentQuestion = (currentQuestion + 1) % questions.size
        updateUI()
        updatebutton()
        enableAllButtons()
        disableAllButtons()
        checkAswerRequest()
    }

    private fun updatebutton() {
        aButton.text = posiblesrespuestas[currentQuestion].optionA.substring(2)
        bButton.text = posiblesrespuestas[currentQuestion].optionB.substring(2)
        cButton.text = posiblesrespuestas[currentQuestion].optionC.substring(2)
        dButton.text = posiblesrespuestas[currentQuestion].optionD.substring(2)
    }

    private fun prevQuestion() {
        resetButtonColors()
        checkAllAnswred()
        currentQuestion = (currentQuestion - 1 + questions.size) % questions.size
        updateUI()
        updatebutton()
        enableAllButtons()
        disableAllButtons()
        checkAswerRequest()
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

    private fun resetButtonColors() {
        val buttons = listOf(aButton, bButton, cButton, dButton)

        for (button in buttons) {
            button.setBackgroundColor(ContextCompat.getColor(this, R.color.defaultButtonColor))
        }
    }


    private fun enableAllButtons() {
        val buttons = listOf(aButton, bButton, cButton, dButton)

        if (!questions[currentQuestion].hasAnswered){
            for (button in buttons) {
                button.isEnabled = true
            }
        }
    }

    private fun disableAllButtons() {
        val buttons = listOf(aButton, bButton, cButton, dButton)
        if (questions[currentQuestion].hasAnswered)
            for (button in buttons) {
                button.isEnabled = false
        }
    }
    private fun checkAswerRequest(){
        if(questions[currentQuestion].optionSelect !=0 && questions[currentQuestion].isCorrect){
            when(questions[currentQuestion].optionSelect) {
                1 -> {
                    aButton.setBackgroundColor(ContextCompat.getColor(this, R.color.correctAnswer))
                    bButton.setBackgroundColor(ContextCompat.getColor(this, R.color.incorrectAnswer))
                    cButton.setBackgroundColor(ContextCompat.getColor(this, R.color.incorrectAnswer))
                    dButton.setBackgroundColor(ContextCompat.getColor(this, R.color.incorrectAnswer))
                }

                2 -> {
                    bButton.setBackgroundColor(ContextCompat.getColor(this, R.color.correctAnswer))
                    aButton.setBackgroundColor(ContextCompat.getColor(this, R.color.incorrectAnswer))
                    cButton.setBackgroundColor(ContextCompat.getColor(this, R.color.incorrectAnswer))
                    dButton.setBackgroundColor(ContextCompat.getColor(this, R.color.incorrectAnswer))
                }

                3 -> {
                    cButton.setBackgroundColor(ContextCompat.getColor(this, R.color.correctAnswer))
                    aButton.setBackgroundColor(ContextCompat.getColor(this, R.color.incorrectAnswer))
                    bButton.setBackgroundColor(ContextCompat.getColor(this, R.color.incorrectAnswer))
                    dButton.setBackgroundColor(ContextCompat.getColor(this, R.color.incorrectAnswer))
                }

                4 -> {
                    dButton.setBackgroundColor(ContextCompat.getColor(this, R.color.correctAnswer))
                    aButton.setBackgroundColor(ContextCompat.getColor(this, R.color.incorrectAnswer))
                    cButton.setBackgroundColor(ContextCompat.getColor(this, R.color.incorrectAnswer))
                    bButton.setBackgroundColor(ContextCompat.getColor(this, R.color.incorrectAnswer))
                }
            }
        }
        else if (questions[currentQuestion].hasAnswered && !questions[currentQuestion].isCorrect){
            dButton.setBackgroundColor(ContextCompat.getColor(this, R.color.incorrectAnswer))
            aButton.setBackgroundColor(ContextCompat.getColor(this, R.color.incorrectAnswer))
            cButton.setBackgroundColor(ContextCompat.getColor(this, R.color.incorrectAnswer))
            bButton.setBackgroundColor(ContextCompat.getColor(this, R.color.incorrectAnswer))
        }

        else {resetButtonColors()}
    }

    private fun checkAnswer(id:Int, selectedOption: String) {
        val question = questions[currentQuestion]
        val isCorrect = selectedOption[0].toString() == question.correctOption

        questions[currentQuestion].hasAnswered = true
        disableAllButtons()
        if (isCorrect) {
            question.isCorrect=true
            Toast.makeText(this, "respuesta conrrecta",Toast.LENGTH_SHORT).show()
            correctAnswersInARow++
            if (correctAnswersInARow % 2 == 0) {
                hintCount++
            }
            when(id){
                1 ->{ aButton.setBackgroundColor(ContextCompat.getColor(this,R.color.correctAnswer))
                questions[currentQuestion].optionSelect=1}
                2 -> {bButton.setBackgroundColor(ContextCompat.getColor(this,R.color.correctAnswer))
                    questions[currentQuestion].optionSelect=2}
                3 -> {cButton.setBackgroundColor(ContextCompat.getColor(this,R.color.correctAnswer))
                    questions[currentQuestion].optionSelect=3}
                4 -> {dButton.setBackgroundColor(ContextCompat.getColor(this,R.color.correctAnswer))
                    questions[currentQuestion].optionSelect=4}
            }

        }
        else {
            correctAnswersInARow = 0
            Toast.makeText(this, "respuesta Inconrrecta",Toast.LENGTH_SHORT).show()
            when(id){
                1 ->{ aButton.setBackgroundColor(ContextCompat.getColor(this,R.color.incorrectAnswer))
                    questions[currentQuestion].optionSelect=1}
                2 -> {bButton.setBackgroundColor(ContextCompat.getColor(this,R.color.incorrectAnswer))
                    questions[currentQuestion].optionSelect=2}
                3 -> {cButton.setBackgroundColor(ContextCompat.getColor(this,R.color.incorrectAnswer))
                    questions[currentQuestion].optionSelect=3}
                4 -> {dButton.setBackgroundColor(ContextCompat.getColor(this,R.color.incorrectAnswer))
                    questions[currentQuestion].optionSelect=4}
            }
        }
    }
    private fun getButtonIdByOption(option: String): Int {
        return when (option) {
            posiblesrespuestas[currentQuestion].optionA[0].toString() -> R.id.a_button
            posiblesrespuestas[currentQuestion].optionB[0].toString() -> R.id.b_button
            posiblesrespuestas[currentQuestion].optionC[0].toString() -> R.id.c_button
            posiblesrespuestas[currentQuestion].optionD[0].toString() -> R.id.d_button
            else -> R.id.a_button
        }
    }
    private fun getButtonById(buttonId: Int): Button {
        return findViewById(buttonId)
    }
    private fun getColorForOption(isCorrect: Boolean): Int {
        return if (isCorrect) R.color.correctAnswer else R.color.incorrectAnswer
    }
    private fun setColorForButton(button: Button, colorResId: Int) {
        button.setBackgroundColor(ContextCompat.getColor(this, colorResId))
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
                question.hasAnswered =true
                question.isCorrect=true
                when(correctButton.id){
                    2131296270 -> questions[currentQuestion].optionSelect=1
                    2131296345 -> questions[currentQuestion].optionSelect=2
                    2131296358 -> questions[currentQuestion].optionSelect=3
                    2131296399 -> questions[currentQuestion].optionSelect=4
                }
            }
        }
    }
    private fun checkAllAnswred(){
        var i=0
        for (answer in questions){
            if(answer.hasAnswered){
               i++
            }
        }
        if(i==10){
            var total =0
            for (question in questions){
                if (question.isCorrect){
                    total += 1
                }
                if(question.hasUsedHint){
                    total -= 1
                }
                if(!question.hasUsedHint){
                    total += 1
                }

            }
            val intent1 = Intent(this, GameFinalActivity::class.java)
            intent1.putExtra(GAMEFINALACTIVITY_RESUMEN,total)
            intent1.putExtra(GAMEFINALACTIVITY_DIFFICULT,posSelect)
            startActivity(intent1)
        }

    }
}

