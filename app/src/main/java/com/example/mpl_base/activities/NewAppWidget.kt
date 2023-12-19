package com.example.mpl_base.activities

import com.example.mpl_base.activities.viewModel.NumberViewModel
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.RemoteViews
import androidx.lifecycle.Observer
import com.example.mpl_base.R
import com.example.mpl_base.model.NumberData
import com.example.mpl_base.util.APP_WIDGET_ID
import com.example.mpl_base.util.CalcUtil
import com.example.mpl_base.util.IS_PRIME
import com.example.mpl_base.util.NotificationUtil
import com.example.mpl_base.util.RANDOM_NUMBER
import com.example.mpl_base.util.WidgetActionEnum

/**
 * Implementation of App Widget functionality.
 */
class NewAppWidget : AppWidgetProvider() {
    private lateinit var numberViewModel: NumberViewModel
   private var numberObserver: Observer<NumberData>? = null
    private var randomNumber = 0
    private var toNotify = false

    /**
     * Called when the App Widget is updated.
     */

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // Get an instance of NumberViewModel
        numberViewModel = NumberViewModel.getInstance()
        // Observe changes in numberState LiveData
         numberObserver = Observer<NumberData> { newRandomNumber ->
            randomNumber = newRandomNumber.randomData

            for (appWidgetId in appWidgetIds) {
                updateAppWidget(context, appWidgetManager, appWidgetId, randomNumber,toNotify)
            }

        }
        numberViewModel.numberState.observeForever(numberObserver!!)

        // initial random number
        randomNumber = (numberViewModel.numberState.value?.randomData ?: numberViewModel.returnRandomNumber()) as Int
    }


    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
        numberObserver?.let { numberViewModel.numberState.removeObserver(it) }

    }

    /**
     * Called when a widget receives a broadcast Intent.
     */
    override fun onReceive(context: Context?, intent: Intent?) {
        val appWidgetId = intent!!.getIntExtra(APP_WIDGET_ID,0)

        Log.e("myIntentAction",intent.action.toString())

        numberViewModel = NumberViewModel.getInstance() // Init Viewmodel

        // Observe changes in numberState LiveData
        numberViewModel.numberState.observeForever { numberData ->
            // This will be called whenever the value of numberState changes
            randomNumber = numberData?.randomData ?: 0 //  update number value with the current random Number from randomData or when null set 0
        }

        // take care of  ui actions
        when(intent.action){

            WidgetActionEnum.REFRESH.toString() -> {

                numberViewModel.returnRandomNumber()
                toNotify = false
                updateAppWidget(context!!,AppWidgetManager.getInstance(context),appWidgetId,randomNumber,toNotify)
            }

            WidgetActionEnum.TRUEBUTTON.toString() ->{
                toNotify = true
                checkTrueButtonResult(context!!,AppWidgetManager.getInstance(context),appWidgetId,randomNumber,toNotify)

                Log.e("IntentAction","trueButton")

            }
            WidgetActionEnum.FALSEBUTTON.toString() ->{
                toNotify = true
                checkFalseButtonResult(context!!,AppWidgetManager.getInstance(context),appWidgetId,randomNumber,toNotify)

                Log.e("IntentAction","falseButton")

            }

        }
        super.onReceive(context, intent)
    }
}

/**
 * Update the App Widget with the provided number and notification flag.
 */
internal fun updateAppWidget(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int, number:Int,toNotify: Boolean
) {
    val widgetText = context.getString(R.string.is_prime_question)
    // Construct the RemoteViews object
    val views = RemoteViews(context.packageName, R.layout.new_app_widget)
    views.setTextViewText (R.id.appwidget_title,context.getString(R.string.is_prime_question))
    views.setTextViewText (R.id.appwidget_number, number.toString())

    views.setOnClickPendingIntent(R.id.appwidget_btn, refreshRandomNumber(context,appWidgetId))
    // button for correct
    views.setOnClickPendingIntent(R.id.widget_right_button, notifyRightButton(context,appWidgetId,number,toNotify))
    // button for wrong
    views.setOnClickPendingIntent(R.id.widget_wrong_button, notifyWrongButton(context,appWidgetId,number,toNotify))

    // Instruct the widget manager to update the widget
    appWidgetManager.updateAppWidget(appWidgetId, views)
}
/**
 * Create a PendingIntent for refreshing the random number on the widget.
 */
internal fun refreshRandomNumber(context: Context, appWidgetId: Int ): PendingIntent{
    val refreshIntent = Intent(context, NewAppWidget::class.java)
    refreshIntent.putExtra(APP_WIDGET_ID,appWidgetId)
    refreshIntent.flags = Intent.FLAG_RECEIVER_FOREGROUND
    refreshIntent.action = WidgetActionEnum.REFRESH.toString()

    return PendingIntent.getBroadcast(context,appWidgetId,refreshIntent,PendingIntent.FLAG_UPDATE_CURRENT)

}
/**
 * Update the App Widget with the provided number and notification flag when the True button is clicked.
 */
internal fun checkTrueButtonResult (
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int, number:Int, toNotify:Boolean
) {
    val views = RemoteViews(context.packageName, R.layout.new_app_widget)
        // Construct the RemoteViews object
        views.setTextViewText (R.id.appwidget_number, number.toString())

    views.setOnClickPendingIntent(R.id.widget_right_button, notifyRightButton(context,appWidgetId,number,toNotify))

    appWidgetManager.updateAppWidget(appWidgetId, views)
}


/**
 * Update the App Widget with the provided number and notification flag when the False button is clicked.
 */
internal fun checkFalseButtonResult (
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int, number:Int, toNotify: Boolean
) {
    val views = RemoteViews(context.packageName, R.layout.new_app_widget)
    // Construct the RemoteViews object
    views.setTextViewText (R.id.appwidget_number, number.toString())
   views.setOnClickPendingIntent(R.id.widget_wrong_button, notifyWrongButton(context,appWidgetId,number,toNotify))

    appWidgetManager.updateAppWidget(appWidgetId, views)

}
/**
 * Create a PendingIntent for notifying the correct answer when the True button is clicked.
 */
internal fun notifyRightButton(context: Context, appWidgetId: Int, number:Int, toNotify: Boolean ): PendingIntent{


    val notificationIntent: Intent
    val isPrime = CalcUtil.checkIfPrime(number)
    val title: String
    val text: String
    val icon: Int

    val refreshIntent = Intent(context, NewAppWidget::class.java)
    refreshIntent.putExtra(APP_WIDGET_ID,appWidgetId)
    refreshIntent.flags = Intent.FLAG_RECEIVER_FOREGROUND
    refreshIntent.action = WidgetActionEnum.TRUEBUTTON.toString()

    if ( toNotify){

    if ( isPrime){
        title = "YEAH!!!"
        text = String.format("%d %s a prime number",
            number, "IS")

        icon = R.drawable.icon_true
        notificationIntent = Intent(context, TrueActivity::class.java)

    } else{
        title = "Ohh, bummer"
        text = String.format("%d %s a prime number",
            number, "IS Not")
        icon = R.drawable.icon_false
        notificationIntent = Intent(context, FalseActivity::class.java)

   }
        notificationIntent.putExtra(RANDOM_NUMBER, text)
        notificationIntent.putExtra(IS_PRIME, isPrime)
        NotificationUtil.sendNotification(context, title, text, icon, notificationIntent)
    }

    return PendingIntent.getBroadcast(context,appWidgetId,refreshIntent,PendingIntent.FLAG_UPDATE_CURRENT)


}
/**
 * Create a PendingIntent for notifying the correct answer when the False button is clicked.
 */
internal fun notifyWrongButton(context: Context, appWidgetId: Int, number:Int,toNotify: Boolean ): PendingIntent{

    val notificationIntent: Intent
    val isPrime = CalcUtil.checkIfPrime(number)
    val title: String
    val text: String
    val icon: Int

    val refreshIntent = Intent(context, NewAppWidget::class.java)
    refreshIntent.putExtra(APP_WIDGET_ID,appWidgetId)
    refreshIntent.flags = Intent.FLAG_RECEIVER_FOREGROUND
    refreshIntent.action = WidgetActionEnum.FALSEBUTTON.toString()

    if ( toNotify) {
        if (!isPrime) {
            title = "YEAH!!!"
            text = String.format(
                "%d %s a prime number",
                number, "IS Not"
            )
            icon = R.drawable.icon_true
            notificationIntent = Intent(context, TrueActivity::class.java)

        } else {
            title = "Ohh, bummer"
            text = String.format(
                "%d %s a prime number",
                number, "IS NOT"
            )

            icon = R.drawable.icon_false
            notificationIntent = Intent(context, FalseActivity::class.java)

        }

        notificationIntent.putExtra(RANDOM_NUMBER, text)
        notificationIntent.putExtra(IS_PRIME, isPrime)
        NotificationUtil.sendNotification(context, title, text, icon, notificationIntent)
    }


    return PendingIntent.getBroadcast(context,appWidgetId,refreshIntent,PendingIntent.FLAG_UPDATE_CURRENT)

}
