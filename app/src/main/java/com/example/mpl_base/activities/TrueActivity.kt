package com.example.mpl_base.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.example.mpl_base.R
import com.example.mpl_base.util.RANDOM_NUMBER

class TrueActivity : AppCompatActivity() {
    private lateinit var backBtn: Button
    private lateinit var textViewTrueTv: TextView
    private lateinit var bckTrueIntent: Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_true)

        backBtn = findViewById(R.id.backTrueActivityBtn)
        textViewTrueTv = findViewById(R.id.textView_True_Tv)


        val intentValue = intent.getStringExtra("myIntent")?: intent.getStringExtra(
            RANDOM_NUMBER)

       // textViewTrueTv.text = intentValue?.get(0)
        textViewTrueTv.text = intentValue
        backBtn.setOnClickListener{

            bckTrueIntent = Intent(this,MainActivity::class.java)
            //bckTrueIntent.putStringArrayListExtra("intentBck",intentValue)
            startActivity(bckTrueIntent)

        }


    }
}