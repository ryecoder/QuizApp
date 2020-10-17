package com.budzter.quizapp

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_questions.*

class QuestionsActivity : AppCompatActivity(), View.OnClickListener{

    var mQuestionsData: ArrayList<Question>? = null
    var mCurrentQuestion: Int = 1
    var mSelectedChoice: Int = 0
    var nextQuestion = false
    private var mUserName: String? = null
    private var mScore: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_questions)

        mQuestionsData = Constants.getData()

        setQuestion()

        tv_option_one.setOnClickListener(this)
        tv_option_two.setOnClickListener(this)
        tv_option_three.setOnClickListener(this)
        tv_option_four.setOnClickListener(this)
        btn_submit.setOnClickListener(this)

        // get username from the main activity to be passed to result
        mUserName = intent.getStringExtra(Constants.NAME)

    }

    // set question to UI
    private fun setQuestion(){
        setChoicesDefault()

        val question: Question? = mQuestionsData!![mCurrentQuestion - 1]

        tv_question.text = question!!.question
        iv_image.setImageResource(question.image)
        pb_progressbar.progress = mCurrentQuestion
        tv_progress.text = "$mCurrentQuestion/${mQuestionsData!!.size}"
        tv_option_one.text = question.optionOne
        tv_option_two.text = question.optionTwo
        tv_option_three.text = question.optionThree
        tv_option_four.text = question.optionFour
        btn_submit.text = "SUBMIT"
    }

    // set all choices back to non-selected
    private fun setChoicesDefault(){
        val options = ArrayList<TextView>()
        options.add(tv_option_one)
        options.add(tv_option_two)
        options.add(tv_option_three)
        options.add(tv_option_four)

        for (option in options){
            option.background = ContextCompat.getDrawable(this, R.drawable.choices_bg)
            option.setTextColor(Color.parseColor("#7A8089"))
            option.typeface = Typeface.DEFAULT
        }
    }

    // set the selected text view and update the current position selected
    private fun setSelected(tv: TextView, position: Int){
        setChoicesDefault()
        mSelectedChoice = position
        tv.background = ContextCompat.getDrawable(this, R.drawable.choices_selected_bg)
        tv.setTextColor(Color.parseColor("#363A43"))
        tv.typeface = Typeface.DEFAULT_BOLD
    }

    // this sets the color for the text view
    private fun setAnswerViews(option: Int, drawableBg: Int){
        when(option){
            1 -> {
                tv_option_one.background = ContextCompat.getDrawable(this, drawableBg)
            }
            2 -> {
                tv_option_two.background = ContextCompat.getDrawable(this, drawableBg)
            }
            3 -> {
                tv_option_three.background = ContextCompat.getDrawable(this, drawableBg)
            }
            4 -> {
                tv_option_four.background = ContextCompat.getDrawable(this, drawableBg)
            }
        }
    }

    // implementation of the OnclickListener interface, applied to each choices on OnCreate
    override fun onClick(v: View?) {
        when(v?.id){
            R.id.tv_option_one -> {
                setSelected(tv_option_one, 1)
            }
            R.id.tv_option_two -> {
                setSelected(tv_option_two, 2)
            }
            R.id.tv_option_three -> {
                setSelected(tv_option_three, 3)
            }
            R.id.tv_option_four -> {
                setSelected(tv_option_four, 4)
            }
            R.id.btn_submit -> {

                // Constants.makeToast(this, "Current Question: $mCurrentQuestion, Question Size: ${mQuestionsData!!.size}")

                if(nextQuestion){
                    setQuestion()
                    nextQuestion = false

                } else {

                    val question = mQuestionsData!![mCurrentQuestion - 1]

                    // set the color of the wrong answer (if wrong)
                    if(mSelectedChoice != question.correctAnswer){
                        setAnswerViews(mSelectedChoice, R.drawable.choices_wrong_bg)
                    } else {
                        mScore++
                    }

                    // set the color fo the correct answer
                    setAnswerViews(question.correctAnswer, R.drawable.choices_correct_bg)

                    // set finish or next to button depending if questions are exhausted
                    if(mCurrentQuestion == mQuestionsData!!.size){
                        btn_submit.text = "FINISH"

                        // open the result activity and pass the required data
                        val intent = Intent(this, ResultActivity::class.java)
                        intent.putExtra(Constants.NAME, mUserName)
                        intent.putExtra(Constants.SCORE, mScore)
                        intent.putExtra(Constants.TOTAL_QUESTION, mQuestionsData!!.size)
                        startActivity(intent)
                        finish()

                    } else {
                        btn_submit.text = "GO TO NEXT QUESTION"
                        // moves the counter points to the next question
                        mCurrentQuestion++
                        nextQuestion = true
                    }
                }
            }
        }
    }
}