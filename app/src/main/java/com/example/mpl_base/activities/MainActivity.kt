package com.example.mpl_base.activities

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.collectAsState
import com.example.mpl_base.R
import com.example.mpl_base.databinding.ActivityMainBinding
import com.example.mpl_base.util.CalcUtil
import com.example.mpl_base.util.IS_PRIME
import com.example.mpl_base.util.NotificationUtil
import com.example.mpl_base.util.RANDOM_NUMBER
import java.sql.Array
import java.util.Objects
import kotlin.properties.Delegates

class MainActivity : AppCompatActivity()
{
    private lateinit var randomNumberTv: TextView
    private lateinit var randomizeBtn: Button
   private lateinit var notifyTrueBtn: ImageButton
   private lateinit var notifyFalseBtn: ImageButton
    private var number by Delegates.notNull<Int>()
    private lateinit var intent:Intent
    private lateinit var notifyIntent: Intent
    //private lateinit var array: ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // zum Notification auszuführen
                 NotificationUtil.createNotificationChannel(this)



        setUI()


    }

    private fun setUI(){
        randomNumberTv = findViewById(R.id.main_random_number)

        randomizeBtn = findViewById(R.id.main_btn_randomize)
        notifyTrueBtn = findViewById(R.id.true_btn)
       notifyFalseBtn = findViewById(R.id.false_btn)

        //array = ArrayList() // Initialize the ArrayList



       randomizeBtn.setOnClickListener {
            updateRandomNumber()
            number = randomNumberTv.text.toString().toInt()

        }


        // handle the true btn notifications
        updateRandomNumber()


         notifyTrueBtn.setOnClickListener{

            val isPrime = CalcUtil.checkIfPrime(number)

            val title: String
            val text: String
            val icon: Int

            if ( isPrime){
                title = getString(R.string.yay)
                text = String.format(getString(R.string.answer_text),
                    number, getString(R.string.is_text))

                icon = R.drawable.icon_true
              // array.addAll(listOf(text,number.toString()))

                intent = Intent(this, TrueActivity::class.java)
                intent.putExtra("myIntent", text)
                startActivity(intent) // move to the next Activity

                // The Notification
                notifyIntent = Intent(this, TrueActivity::class.java)


            } else{
                title = getString(R.string.nay)
                text = String.format(getString(R.string.answer_text), number, getString(R.string.is_not_text))
                icon = R.drawable.icon_false

                // Pass the value to the array

                // Init the intent
                intent = Intent(this, FalseActivity::class.java)
                // put the values to the intent
                intent.putExtra("myIntent", text)
                notifyIntent = Intent(this, FalseActivity::class.java)

                startActivity(intent)
            }


             notifyIntent.putExtra(RANDOM_NUMBER, text)
             notifyIntent.putExtra(IS_PRIME, isPrime)
           NotificationUtil.sendNotification(this, title, text, icon, notifyIntent)
        }

        // handle the false btn notifications
        notifyFalseBtn.setOnClickListener{
            val isPrime = CalcUtil.checkIfPrime(number)

            val title: String
            val text: String
            val icon: Int

            if ( !isPrime){
                title = getString(R.string.yay)
                text = String.format(getString(R.string.answer_text),
                    number, getString(R.string.is_not_text))
                icon = R.drawable.icon_true

                intent = Intent(this, TrueActivity::class.java)
                intent.putExtra("myIntent", text)
                notifyIntent = Intent(this, TrueActivity::class.java)

                startActivity(intent)
            } else{
                title = getString(R.string.nay)
                text = String.format(getString(R.string.answer_text), number, getString(R.string.is_not_text))
                icon = R.drawable.icon_false
                //array.addAll(listOf(text,number.toString()))

                intent = Intent(this, FalseActivity::class.java)
                intent.putExtra("myIntent", text)

                notifyIntent = Intent(this, FalseActivity::class.java)

                startActivity(intent)
            }

            notifyIntent.putExtra(RANDOM_NUMBER, text)
            notifyIntent.putExtra(IS_PRIME, isPrime)

            NotificationUtil.sendNotification(this, title, text, icon, notifyIntent)
        }



    }



    private fun updateRandomNumber(){
        val randomNumber = CalcUtil.rng()
        randomNumberTv.text = randomNumber.toString()
        number = randomNumber
    }

}