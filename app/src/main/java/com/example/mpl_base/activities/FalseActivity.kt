package com.example.mpl_base.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.example.mpl_base.R
import com.example.mpl_base.util.RANDOM_NUMBER

class FalseActivity : AppCompatActivity() {
    private lateinit var backBtn: Button
    private lateinit var textViewFalseTv: TextView
    private lateinit var bckFalseIntent: Intent
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_false)

        backBtn = findViewById(R.id.back_false_ActivityBtn)
        textViewFalseTv = findViewById(R.id.textView_False_Tv)



        val intentValue = intent.getStringExtra("myIntent") ?: intent.getStringExtra(
            RANDOM_NUMBER)
        textViewFalseTv.text = intentValue

        backBtn.setOnClickListener{

            bckFalseIntent = Intent(this,MainActivity::class.java)
           // bckFalseIntent.putStringArrayListExtra("intentBck",intentValue)
            startActivity(bckFalseIntent)
        }

    }
}