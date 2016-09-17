package com.festember16.app;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Ajay Nataraj on 9/14/2016.
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    private String text ;
    private String title;
    private String cluster ;
    private String feedback;
    NotificationDetails notif = new NotificationDetails();
    String registered_event;
    List<Events> eventses;
    private NotificationDBHandler notificationDBHandler = new NotificationDBHandler(this);
    private Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
    private DBHandler dbHandler = new DBHandler(this);



    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        VolleySingleton volleySingleton = VolleySingleton.getsInstance();
        final RequestQueue requestQueue = volleySingleton.getmRequestQueue();


        Log.d(TAG, "From: " + remoteMessage.getFrom());


        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            remoteMessage.getData();
            Calendar c = Calendar.getInstance();
            String currentDateandTime = new SimpleDateFormat("HH:mm").format(new Date());

            String t=currentDateandTime+" , Day "+String.valueOf(c.get(Calendar.DATE)-22);



                Map<String, String> params = remoteMessage.getData();
                JSONObject js = new JSONObject(params);
                Log.d("JSON_OBJECT", js.toString());



            notif.text = js.optString("text");
            notif.title = js.optString("title");
            notif.cluster = js.optString("cluster");
            notif.cluster=notif.cluster.replaceAll("_"," ").toUpperCase();
            Log.d(TAG,"replace : "+ notif.cluster);
            notif.type = js.optString("type");
            notif.event = js.optString("event");
            notif.event = notif.event.replaceAll("_"," ").toUpperCase();
            Log.d(TAG,"replace event : "+ notif.event);

            notif.time = t;
            notificationDBHandler.addNotification(notif);

            Log.d(TAG,"written in DB");

            if(notif.cluster.equalsIgnoreCase("general")&&notif.type.equalsIgnoreCase("general")){
                Log.d(TAG,"tag working");
                 generalNotif(notif);
            }
            else if(!notif.cluster.equalsIgnoreCase("general")){
                if(notif.type.equalsIgnoreCase("feedback")){
                    feedbackGeneral(notif);
                }else{
                    eventses = new ArrayList<>();

                    SharedPreferences preferences = getSharedPreferences(
                            DetailsFragment.REGISTERED_EVENTS,
                            MODE_PRIVATE
                    );

                    Map<String, Boolean> map = (Map<String, Boolean>) preferences.getAll();

                    Iterator iterator = map.entrySet().iterator();

                    while(iterator.hasNext()){
                        Map.Entry<String, Boolean> entry = (Map.Entry<String, Boolean>) iterator.next();

                        int id = Integer.parseInt(entry.getKey());

                        eventses.add(dbHandler.getEvent(id));

                    }
                    for(int k=0;k<eventses.size();k++){
                        eventses.get(k).setName(eventses.get(k).getName().replaceAll("_"," ").toUpperCase());
                        if(eventses.get(k).getName().contains(notif.event)){
                            eventSpecific(notif);
                        }
                    }


                }
            }else if(notif.cluster.equalsIgnoreCase("general")){
                clusterGeneral(notif);
            }




        }


        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            sendNotification(remoteMessage.getNotification().getBody());
        }


    }
    private void generalNotif(NotificationDetails notificationDetails){
        Intent intent = new Intent(this, Notification.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 , intent,
                PendingIntent.FLAG_ONE_SHOT);


        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_stat_name)
                .setContentTitle(notificationDetails.title)
                .setColor(267683369)
                .setContentText(notificationDetails.text)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notificationBuilder.build());

    }

    private void eventSpecific(NotificationDetails notificationDetails){

        Intent intent = new Intent(this, Notification.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 , intent,
                PendingIntent.FLAG_ONE_SHOT);


        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_stat_name)
                .setContentTitle(notificationDetails.title)
                .setColor(267683369)
                .setContentText(notificationDetails.text)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(1, notificationBuilder.build());
    }
    private void clusterGeneral (NotificationDetails notificationDetails){
        Log.d(TAG,"general working");
        Intent intent = new Intent(this, Notification.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 , intent,
                PendingIntent.FLAG_ONE_SHOT);


        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_stat_name)
                .setContentTitle("Festember 16")
                .setColor(267683369)
                .setContentText("Check for new notifications")
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(2, notificationBuilder.build());
    }

    private void feedbackGeneral (NotificationDetails notificationDetails){

        Intent intent = new Intent(this, FeedbackActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 , intent,
                PendingIntent.FLAG_ONE_SHOT);


        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_stat_name)
                .setContentTitle("Festember 16")
                .setColor(267683369)
                .setContentText("Give your valuable feedback")
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(3, notificationBuilder.build());
    }


    private void sendNotification(String messageBody) {
        Intent intent = new Intent(this, Notification.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 , intent,
                PendingIntent.FLAG_ONE_SHOT);


        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_stat_name)
                .setContentTitle("Festember")
                .setColor(267683369)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notificationBuilder.build());
    }
}