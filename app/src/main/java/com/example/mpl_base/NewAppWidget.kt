package com.example.mpl_base

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.example.mpl_base.activities.MainActivity
import com.example.mpl_base.util.APP_WIDGET_ID
import com.example.mpl_base.util.CalcUtil
import com.example.mpl_base.util.WidgetActionEnum

/**
 * Implementation of App Widget functionality.
 */
class NewAppWidget : AppWidgetProvider() {
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId,0)
        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        val appWidgetId = intent!!.getIntExtra(APP_WIDGET_ID,0)

        when(intent.action){ // widget apfragen
            WidgetActionEnum.REFRESH.toString() -> {
                val number = CalcUtil.rng()
                updateAppWidget(context!!,AppWidgetManager.getInstance(context),appWidgetId,number)
            }
//            WidgetActionEnum.SYNC -> TODO()
//            WidgetActionEnum.NOTIFY -> TODO()


        }
        super.onReceive(context, intent)
    }
}

internal fun updateAppWidget(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int, number:Int
) {
    val widgetText = context.getString(R.string.appwidget_text)
    // Construct the RemoteViews object
    val views = RemoteViews(context.packageName, R.layout.new_app_widget)
    views.setTextViewText (R.id.appwidget_title,context.getString(R.string.appwidget_text))
    views.setTextViewText (R.id.appwidget_number, number.toString())

    views.setOnClickPendingIntent(R.id.appwidget_btn, refreshRandomNumber(context,appWidgetId))


    // Instruct the widget manager to update the widget
    appWidgetManager.updateAppWidget(appWidgetId, views)
}

internal fun refreshRandomNumber(context: Context, appWidgetId: Int ): PendingIntent{
    val refreshIntent = Intent(context, NewAppWidget::class.java)
    refreshIntent.putExtra(APP_WIDGET_ID,appWidgetId)
    refreshIntent.flags = Intent.FLAG_RECEIVER_FOREGROUND
    refreshIntent.action = WidgetActionEnum.REFRESH.toString()
    return PendingIntent.getBroadcast(context,appWidgetId,refreshIntent,PendingIntent.FLAG_UPDATE_CURRENT)
}