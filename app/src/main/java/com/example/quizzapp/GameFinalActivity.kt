package com.example.quizzapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView

const val GAMEFINALACTIVITY_RESUMEN = "GAMEFINALACTIVITY_RESUMEN"

class GameFinalActivity : AppCompatActivity() {
    private var puntajeTotal :String ="0"
    private var puntajeTotalInt :Int=0
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

        puntajeTotal=intent.getIntExtra(GAMEFINALACTIVITY_RESUMEN,0).toString()
        puntajeTextView.text=puntajeTotal
        puntajeTotalInt= puntajeTotal.toInt()

        if(puntajeTotalInt==20){
            val stars = listOf(star1,star2,star3,star4,star5)
            for (star in stars){
                star.setImageResource(R.drawable.star)
            }
        }
        else if (puntajeTotalInt >=17) {
            val stars = listOf(star1, star2, star3, star4)
            for (star in stars) {
                star.setImageResource(R.drawable.star)
            }
            star5.setImageResource(R.drawable.starvacia)
        }
        else if (puntajeTotalInt >=12) {
            val stars = listOf(star1, star2, star3)
            for (star in stars) {
                star.setImageResource(R.drawable.star)
            }
            star4.setImageResource(R.drawable.starvacia)
            star5.setImageResource(R.drawable.starvacia)
        }
        else if (puntajeTotalInt >=6) {
            val stars = listOf(star1, star2)
            for (star in stars) {
                star.setImageResource(R.drawable.star)
            }
            star3.setImageResource(R.drawable.starvacia)
            star4.setImageResource(R.drawable.starvacia)
            star5.setImageResource(R.drawable.starvacia)
        }
        else if (puntajeTotalInt >=1) {
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
    }
}