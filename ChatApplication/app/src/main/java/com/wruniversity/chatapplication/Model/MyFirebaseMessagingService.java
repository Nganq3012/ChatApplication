package com.wruniversity.chatapplication.Model;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.wruniversity.chatapplication.Chat;
import com.wruniversity.chatapplication.MainActivity;
import com.wruniversity.chatapplication.Model.NotificationData;
import com.wruniversity.chatapplication.R;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * Created by laptop88 on 3/17/2017.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    String image;
    String title;
    String textMessage;
    String sound;
    int id;
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        image=remoteMessage.getNotification().getIcon();
        title=remoteMessage.getNotification().getTitle();
        textMessage=remoteMessage.getNotification().getBody();
        sound=remoteMessage.getNotification().getSound();
        id =0;
        Object obj=remoteMessage.getData().get("id");
        if (obj != null) {
            id = Integer.valueOf(obj.toString());
        }
        FirebaseDatabase.getInstance().getReference().child("messages/"+ Chat.pathNameReceive).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                sendNotification(new NotificationData(image,id,title,textMessage,sound));
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    private void sendNotification(NotificationData notificationData)
    {
        Intent intentNotification =new Intent(this,MainActivity.class);
        intentNotification.putExtra("MESSSAGES TEXT", notificationData.getTextMessage());
        intentNotification.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intentNotification,
                PendingIntent.FLAG_ONE_SHOT);
        NotificationCompat.Builder notiBuilder=null;
        try {
            notiBuilder = new NotificationCompat.Builder(this).setSmallIcon(R.drawable.ic_sms_black_24dp)
                    .setContentTitle(notificationData.getTitle()).
                            setContentText(notificationData.getTextMessage()).
                            setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)).
                            setContentIntent(pendingIntent);
        }
        catch (NullPointerException e) {
            e.printStackTrace();
        }

        if (notiBuilder != null) {
            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(notificationData.getId(), notiBuilder
                    .build());
        } else {
            Log.d("notification ", "Failed notificationBuilder");
        }
    }
}
