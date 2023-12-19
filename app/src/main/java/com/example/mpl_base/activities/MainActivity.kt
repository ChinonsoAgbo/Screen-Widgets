package com.example.mpl_base.activities

import com.example.mpl_base.activities.viewModel.NumberViewModel
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.lifecycle.Observer
import com.example.mpl_base.R
import com.example.mpl_base.util.CalcUtil
import com.example.mpl_base.util.IS_PRIME
import com.example.mpl_base.util.NotificationUtil
import com.example.mpl_base.util.RANDOM_NUMBER
import kotlin.properties.Delegates

/**
 * Main activity of the application responsible for displaying a random number,
 * allowing users to randomize the number, and providing options to notify whether
 * the number is prime or not.
 */
class MainActivity : AppCompatActivity()
{
    private lateinit var randomNumberTv: TextView
    private lateinit var randomizeBtn: Button
   private lateinit var notifyTrueBtn: ImageButton
   private lateinit var notifyFalseBtn: ImageButton
    private var number by Delegates.notNull<Int>()
    private lateinit var intent:Intent
    private lateinit var notificationIntent: Intent

    private lateinit var numberViewModel: NumberViewModel
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Create a notification channel
        NotificationUtil.createNotificationChannel(this)

        // Get an instance of NumberViewModel
        numberViewModel = NumberViewModel.getInstance()

        // Set up the user interface
        setUI()

        // set up an observer for numberState liveData
        numberViewModel.numberState.observe(this, Observer {numberData->
            // This block will be called whenever the value of numberState changes
            val randomNumber = numberData?.randomData ?: 0
            number=randomNumber
            randomNumberTv.text = randomNumber.toString()

        })

    }
    /**
     * Set up the user interface components and their respective click listeners.
     */
    private fun setUI(){
        randomNumberTv = findViewById(R.id.main_random_number)

        randomizeBtn = findViewById(R.id.main_btn_randomize)
        notifyTrueBtn = findViewById(R.id.true_btn)
       notifyFalseBtn = findViewById(R.id.false_btn)


       randomizeBtn.setOnClickListener {

           numberViewModel.returnRandomNumber()  // Update the random number using the ViewModel
            number = randomNumberTv.text.toString().toInt()

        }

        // Handle the true button notifications
         notifyTrueBtn.setOnClickListener{

            val isPrime = CalcUtil.checkIfPrime(number)

            val title: String
            val text: String
            val icon: Int

            if ( isPrime){
                // set string values
                title = getString(R.string.yay)
                text = String.format(getString(R.string.answer_text),
                    number, getString(R.string.is_text))

                icon = R.drawable.icon_true

                intent = Intent(this, TrueActivity::class.java)
                intent.putExtra("myIntent", text)

                // The Notification intent
                notificationIntent = Intent(this, TrueActivity::class.java)
                startActivity(intent) // move to the next Activity

            } else{
                title = getString(R.string.nay)
                text = String.format(getString(R.string.answer_text), number, getString(R.string.is_not_text))
                icon = R.drawable.icon_false

                // Init the intent button intent
                intent = Intent(this, FalseActivity::class.java)
                // put the values to the intent
                intent.putExtra("myIntent", text)
                // Init the intent Notification intent
                notificationIntent = Intent(this, FalseActivity::class.java)
                startActivity(intent)
            }


             notificationIntent.putExtra(RANDOM_NUMBER, text)
             notificationIntent.putExtra(IS_PRIME, isPrime)
           NotificationUtil.sendNotification(this, title, text, icon, notificationIntent)
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
                notificationIntent = Intent(this, TrueActivity::class.java)

                startActivity(intent)
            } else{
                // set string values
                title = getString(R.string.nay)
                text = String.format(getString(R.string.answer_text), number, getString(R.string.is_not_text))
                icon = R.drawable.icon_false

                // Init the button intent
                intent = Intent(this, FalseActivity::class.java)
                intent.putExtra("myIntent", text)
                // Init the intent Notification intent
                notificationIntent = Intent(this, FalseActivity::class.java)

                startActivity(intent)
            }

            notificationIntent.putExtra(RANDOM_NUMBER, text)
            notificationIntent.putExtra(IS_PRIME, isPrime)

            NotificationUtil.sendNotification(this, title, text, icon, notificationIntent)
        }



    }

}