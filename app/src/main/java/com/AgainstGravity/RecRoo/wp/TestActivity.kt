package com.AgainstGravity.RecRoo.wp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.AgainstGravity.RecRoo.R

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_test.*

class TestActivity : AppCompatActivity(), View.OnClickListener {
    private var currentPos: Int = 1
    private var questionList: ArrayList<Question>? = null
    private var selectedAnswer: Int = 0


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        questionList  = Quiz.getQues()

        setQues()

        tv_ans_one.setOnClickListener(this)
        tv_ans_two.setOnClickListener(this)
        tv_ans_three.setOnClickListener(this)
        tv_ans_four.setOnClickListener(this)

    }

    private fun setQues(){

        val question = questionList!![currentPos-1]

        defaultAnsView()

        if(currentPos == questionList!!.size) {
            btn_submit.text = "Finish"
        } else {
            btn_submit.text = "Submit"
        }

        progressBar.progress = currentPos
        tv_progress.text ="$currentPos"+"/" + progressBar.max


        tv_ques.text = question.ques
        tv_ans_one.text = question.ansOne
        tv_ans_two.text = question.ansTwo
        tv_ans_three.text = question.ansThree
        tv_ans_four.text = question.ansFour
        btn_submit.setOnClickListener(this)
    }

    private fun defaultAnsView() {
        val answers = ArrayList<TextView>()
        answers.add(0, tv_ans_one)
        answers.add(1,tv_ans_two)
        answers.add(2,tv_ans_three)
        answers.add(3,tv_ans_four)

        for (answer in answers) {
            answer.setTextColor(Color.parseColor("#363A43"))
            answer.typeface = Typeface.DEFAULT
            answer.background = ContextCompat.getDrawable(
                this, R.drawable.default_ans_border
            )
        }
    }

    override fun onClick(view: View?) {
        when(view?.id) {
            R.id.tv_ans_one -> {
                selectedAnsView(tv_ans_one, 1)
            }
            R.id.tv_ans_two -> {
                selectedAnsView(tv_ans_two,2)
            }
            R.id.tv_ans_three -> {
                selectedAnsView(tv_ans_three, 3)
            }
            R.id.tv_ans_four -> {
                selectedAnsView(tv_ans_four, 4)
            }
            R.id.btn_submit -> {
                currentPos++
                when{
                    currentPos <= questionList!!.size -> {
                        setQues()
                    } else -> {
                    if(currentPos==questionList!!.size) {
                        btn_submit.text = "Finish"
                    } else {
                        btn_submit.text = "Submit"
                    }
                    selectedAnswer = 0

                    val intent = Intent(this, End::class.java)
                    startActivity(intent)
                    finish()
                }
                }
            }
        }
    }

    private fun selectedAnsView(tv: TextView, selectedOptionNum: Int){
        defaultAnsView()
        selectedAnswer = selectedOptionNum
        tv.setTextColor(Color.parseColor("#363A43"))
        tv.setTypeface(tv.typeface, Typeface.BOLD)
        tv.background = ContextCompat.getDrawable(
            this, R.drawable.selected_ans_border)
    }
}