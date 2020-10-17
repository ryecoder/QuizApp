package com.budzter.quizapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_result.*

class ResultActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        // hides the status bar on this activity
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN

        // retrieve data from questions activity intent
        tv_username.text = intent.getStringExtra(Constants.NAME)
        val score = intent.getIntExtra(Constants.SCORE, 0)
        val totalQuestion = intent.getIntExtra(Constants.TOTAL_QUESTION, 0)
        tv_score.text = "Your score is $score out of $totalQuestion"

        btn_finish.setOnClickListener{
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

    }
}