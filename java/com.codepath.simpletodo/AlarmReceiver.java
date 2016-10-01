package com.codepath.simpletodo;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.widget.Toast;
import android.support.v4.app.NotificationCompat;

import com.codepath.simpletodo.Model.Todo;

import java.io.File;

public class AlarmReceiver extends BroadcastReceiver {

    private Todo todo;

    public AlarmReceiver setTodo(Todo todo) {
        this.todo = todo;
        return this;
    }

    @Override
    public void onReceive(Context context, Intent arg1) {

        PendingIntent pi = PendingIntent.getActivity(context, 0, new Intent(context, MainActivity.class), 0);

        NotificationManager manager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        String RESOURCE_PATH = ContentResolver.SCHEME_ANDROID_RESOURCE + "://";
        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);//Uri.parse(RESOURCE_PATH + context.getPackageName() + "/raw/" + "beep.mp3");
        NotificationCompat.Builder notification = new NotificationCompat.Builder(context)
                .setContentTitle(CommonConstants.appName)
                .setContentText(todo.getDescription())
                .setContentIntent(pi)
                .setSmallIcon(R.drawable.ic_alarm_white_24dp)
                .setTicker(todo.getDescription())
                .setSound(sound);
        manager.notify((int)this.todo.getPrimaryKey(), notification.build());

    }
}
