package com.vaibhav.newsarticles.fcm

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.bumptech.glide.Glide
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.vaibhav.newsarticles.R
import java.net.URL


const val CHANNEL_ID="FCM"

// A base for Firebase Cloud Messaging
class FirebaseMessageReceiver : FirebaseMessagingService() {

    // Called when the the new Token is generate
    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }

    // Called when the Notification is Received
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        // Checked and implemented all the payloads data.
        message.data?.let {
            Log.d("Payloads","${it}")
            for (entry in it.entries.iterator()){
                // extracted all payloads from data.
                Log.d("${entry.key}","${entry.value}")
            }
        }

        // Created a new notification channel
        createNotificationChannel()

        // Extracted all notification details
        message.notification?.let {
            val bmp =  Glide.with(this).asBitmap().load(it.imageUrl).submit().get()
            showNotification(it.title!!,it.body!!,bmp)
        }


    }

    // Shows Push Notification to user
    private fun showNotification(title :String, body :String,bitmap: Bitmap){
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentText(body)
            .setContentTitle(title)
            .setLargeIcon(bitmap)
            .setStyle(NotificationCompat.BigPictureStyle().bigPicture(bitmap))
            .setSmallIcon(R.mipmap.ic_launcher)
            .build()

        try {
            NotificationManagerCompat.from(this).notify(121,notification)
        }catch (e:SecurityException){
            e.printStackTrace()
        }

    }

    private fun createNotificationChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Firebase Cloud Messaging", NotificationManager.IMPORTANCE_DEFAULT)
            getSystemService(NotificationManager::class.java).createNotificationChannel(channel)
        }
    }
}