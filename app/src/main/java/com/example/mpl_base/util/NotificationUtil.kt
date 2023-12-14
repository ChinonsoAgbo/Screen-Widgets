package com.example.mpl_base.util

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.mpl_base.R

/**
 * Utility class for handling notifications in the application.
 */
class NotificationUtil
{
    companion object
    {
        private var notificationId: Int = 1

        /**
         * Creates a notification channel for high-priority notifications.
         *
         * @param context The application context.
         */
        fun createNotificationChannel(context: Context)
        {

            // name von channnel ergeben
            // so can man es in string resourcen einlagern
            val name = context.getString(R.string.channel_name)
            val description =  context.getString(R.string.channel_description) // Define Channel description
            val channel = NotificationChannel(context.getString(R.string.channel_id),name, NotificationManager.IMPORTANCE_HIGH)

            channel.description = description // set description
            channel.setShowBadge(true)
            channel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC // damit es auf der log screen wie whatsapp angezeigt werden, bei _Private wird geszeigt nur das es ein notification da ist aber icht was da ist


            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            notificationManager.createNotificationChannel(channel)


        }

        /**
         * Sends a notification with the specified title, text, icon, and intent.
         *
         * @param context The application context.
         * @param title   The title of the notification.
         * @param text    The text content of the notification.
         * @param icon    The resource ID of the notification icon.
         * @param intent  The intent to be triggered when the notification is clicked.
         */
        fun sendNotification(context: Context, title: String, text: String, icon: Int, intent: Intent)
        {
            // Create a PendingIntent for the notification

            val pendingIntent = PendingIntent.getActivity(
                context,
                notificationId,
                intent ,
                PendingIntent.FLAG_UPDATE_CURRENT
            ) // hier weil update wird immer noch neue intent gesendet

            val builder = NotificationCompat.Builder(context, context.getString(R.string.channel_id))
                .setSmallIcon(icon)
                .setContentTitle(title)
                .setContentText(text)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setContentIntent(pendingIntent) // Damit was passiert wenn auf die notifcation clicket
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setDefaults(NotificationCompat.DEFAULT_VIBRATE or NotificationCompat.DEFAULT_SOUND)
                .setAutoCancel(true)

            with(NotificationManagerCompat.from(context)){
                if( ActivityCompat.checkSelfPermission(context,Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                    return

                }

                cancelAll()
                notify(notificationId,builder.build())
                notificationId++            }


        }
    }
}