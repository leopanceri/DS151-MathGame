package com.leonardo.math_game

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.view.inputmethod.InputMethodManager


class MainActivity : AppCompatActivity() {

    private lateinit var expressionTextView: TextView
    private lateinit var answerEditText: EditText
    private lateinit var submitButton: Button
    private lateinit var correctAnswerTextView: TextView
    private lateinit var nextButton: Button

    private var currentQuestion = 1
    private var score = 0
    private var correctAnswer = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Ajustar para edge-to-edge layout se necessário
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Inicializar as views
        expressionTextView = findViewById(R.id.expressionTextView)
        answerEditText = findViewById(R.id.answerEditText)
        submitButton = findViewById(R.id.submitButton)
        correctAnswerTextView = findViewById(R.id.correctAnswerTextView)
        nextButton = findViewById(R.id.nextButton)

        // Gerar a primeira expressão
        generateNewExpression()

        // Configurar os listeners dos botões
        setupListeners()
    }

    private fun generateNewExpression() {
        val num1 = (0..99).random()
        val num2 = (0..99).random()
        correctAnswer = num1 + num2
        expressionTextView.text = "$num1 + $num2 = ?"
        answerEditText.text.clear()
        correctAnswerTextView.visibility = View.GONE
        nextButton.visibility = View.GONE
        submitButton.visibility = View.VISIBLE
        answerEditText.visibility = View.VISIBLE
        findViewById<View>(R.id.main).setBackgroundColor(Color.WHITE)
    }

    private fun setupListeners() {
        submitButton.setOnClickListener {
            checkAnswer()
        }

        nextButton.setOnClickListener {
            if (currentQuestion <= 5) {
                generateNewExpression()
            } else {
                showFinalScore()
            }
        }
    }

    private fun checkAnswer() {
        val userAnswer = answerEditText.text.toString().toIntOrNull()

        if (userAnswer == null) {
            Toast.makeText(this, "Por favor, insira um número válido.", Toast.LENGTH_SHORT).show()
            return
        }

        if (userAnswer == correctAnswer) {
            // Resposta correta
            findViewById<View>(R.id.main).setBackgroundColor(Color.GREEN)
            score += 20
        } else {
            // Resposta incorreta
            findViewById<View>(R.id.main).setBackgroundColor(Color.RED)
            correctAnswerTextView.visibility = View.VISIBLE
            correctAnswerTextView.text = "Resposta Correta: $correctAnswer"
        }

        // Preparar para a próxima questão ou finalizar
        currentQuestion++
        submitButton.visibility = View.GONE
        answerEditText.visibility = View.GONE
        nextButton.visibility = View.VISIBLE

        if (currentQuestion > 5) {
            nextButton.text = "Ver Nota Final"
        }

        // Ocultar o teclado virtual
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(answerEditText.windowToken, 0)

    }

    private fun showFinalScore() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Resultado Final")
        builder.setMessage("Sua nota é: $score de 100")
        builder.setPositiveButton("OK") { _, _ ->
            // Reiniciar o jogo ou fechar o app
            finish()
        }
        val dialog = builder.create()
        dialog.show()
    }
}
