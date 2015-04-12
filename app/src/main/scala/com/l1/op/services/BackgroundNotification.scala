package com.l1.op.services

import android.app.{NotificationManager, PendingIntent, Service}
import android.content.{BroadcastReceiver, Context, Intent}
import android.os.IBinder
import android.support.v4.app.NotificationCompat
import com.l1.op.R
import com.l1.op.activity.SignInSignUpActivity

/**
 * Created by Tarun on 4/11/2015.
 */


class BackgroundNotificationReceiver extends BroadcastReceiver {

  override def onReceive(ctx: Context, intent: Intent) = {
    val intent: Intent = new Intent(ctx, classOf[BackgroundNotificationService])
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    ctx.startService(intent)
  }
}

class BackgroundNotificationService extends Service {

  override def onBind(intent: Intent): IBinder = {
    return null
  }

  override def onStart(intent: Intent, startID: Int) = {
    val context: Context = this.getApplicationContext
    val notificationManager: NotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE).asInstanceOf[NotificationManager]

    val mIntent: Intent = new Intent(this, classOf[SignInSignUpActivity])
    val pendingIntent = PendingIntent.getActivity(context, 0, mIntent, PendingIntent.FLAG_CANCEL_CURRENT)
    val builder: NotificationCompat.Builder = new NotificationCompat.Builder(this)
      .setContentIntent(pendingIntent)
      .setSmallIcon(R.drawable.l1op_logo)
      .setContentTitle("L1OP")
      .setContentText(getResources.getString(R.string.notificationMsg))
      .setAutoCancel(true)

    notificationManager.notify(1, builder.build)

  }
}