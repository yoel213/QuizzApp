package com.example.quizzapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast

const val GAMEFINALACTIVITY_RESUMEN = "GAMEFINALACTIVITY_RESUMEN"
const val GAMEFINALACTIVITY_DIFFICULT = "GAMEFINALACTIVITY_DIFFICULT"

class GameFinalActivity : AppCompatActivity() {
    private var puntajeTotal :Int= 0
    private var difficult :Int =0
    private lateinit var star1 : ImageView
    private lateinit var star2 : ImageView
    private lateinit var star3 : ImageView
    private lateinit var star4 : ImageView
    private lateinit var star5 : ImageView
    private lateinit var puntajeTextView : TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_final)
        puntajeTextView =findViewById(R.id.Resultado)
        star1 =findViewById(R.id.star1)
        star2 =findViewById(R.id.star2)
        star3 =findViewById(R.id.star3)
        star4 =findViewById(R.id.star4)
        star5 =findViewById(R.id.star5)

        puntajeTotal=intent.getIntExtra(GAMEFINALACTIVITY_RESUMEN,0)
        difficult= intent.getIntExtra(GAMEFINALACTIVITY_DIFFICULT,0)
        if(puntajeTotal==20){
            val stars = listOf(star1,star2,star3,star4,star5)
            for (star in stars){
                star.setImageResource(R.drawable.star)
            }
        }
        else if (puntajeTotal >=17) {
            val stars = listOf(star1, star2, star3, star4)
            for (star in stars) {
                star.setImageResource(R.drawable.star)
            }
            star5.setImageResource(R.drawable.starvacia)
        }
        else if (puntajeTotal >=12) {
            val stars = listOf(star1, star2, star3)
            for (star in stars) {
                star.setImageResource(R.drawable.star)
            }
            star4.setImageResource(R.drawable.starvacia)
            star5.setImageResource(R.drawable.starvacia)
        }
        else if (puntajeTotal >=6) {
            val stars = listOf(star1, star2)
            for (star in stars) {
                star.setImageResource(R.drawable.star)
            }
            star3.setImageResource(R.drawable.starvacia)
            star4.setImageResource(R.drawable.starvacia)
            star5.setImageResource(R.drawable.starvacia)
        }
        else if (puntajeTotal >=1) {
            star1.setImageResource(R.drawable.star)
            star2.setImageResource(R.drawable.starvacia)
            star3.setImageResource(R.drawable.starvacia)
            star4.setImageResource(R.drawable.starvacia)
            star5.setImageResource(R.drawable.starvacia)
        }
        else {
            star1.setImageResource(R.drawable.starvacia)
            star2.setImageResource(R.drawable.starvacia)
            star3.setImageResource(R.drawable.starvacia)
            star4.setImageResource(R.drawable.starvacia)
            star5.setImageResource(R.drawable.starvacia)
        }
        Log.d("dificultad","$difficult")
        if(difficult==0){
            puntajeTextView.text=puntajeTotal.toString()
        }
        else if(difficult==1){
            puntajeTextView.text={puntajeTotal*2}.toString()
        }
        else if(difficult==2){
            puntajeTextView.text={puntajeTotal*3}.toString()
        }
    }
}