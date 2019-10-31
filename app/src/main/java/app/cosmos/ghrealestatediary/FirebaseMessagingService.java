package app.cosmos.ghrealestatediary;


import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.messaging.RemoteMessage;


import java.net.URL;

import me.leolin.shortcutbadger.ShortcutBadger;

import static app.cosmos.ghrealestatediary.GlobalApplication.applicationLifecycleHandler;
import static app.cosmos.ghrealestatediary.GlobalApplication.getOtherLogin;


public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if ("1".equals(msg.obj.toString().trim())) {
                Toast.makeText(FirebaseMessagingService.this, "다른 기기에서 로그인을 시도하여 앱을 종료합니다.", Toast.LENGTH_SHORT).show();
            } else if ("2".equals(msg.obj.toString().trim())) {
                Toast.makeText(FirebaseMessagingService.this, "다른 기기에서 로그인을 시도하여 부동산 다이어리 앱을 종료합니다.", Toast.LENGTH_SHORT).show();
            }
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.i("FCM", ""+remoteMessage.getData());
        if ("text".equals(remoteMessage.getData().get("type").toString().trim())) {
            Log.i("FCM TEXT", ""+remoteMessage);
            if (applicationLifecycleHandler.isInBackground()) {
                sendNotificationText(remoteMessage.getData().get("text").toString().trim(), remoteMessage.getData().get("link").toString().trim());
            }
        } else if ("image".equals(remoteMessage.getData().get("type").toString().trim())) {
            Log.i("FCM IMAGE", ""+remoteMessage);
            if (applicationLifecycleHandler.isInBackground()) {
                sendNotificationImage(remoteMessage.getData().get("text").toString().trim(), remoteMessage.getData().get("image").toString().trim(), remoteMessage.getData().get("link").toString().trim());
            }
        } else if ("logout".equals(remoteMessage.getData().get("type"))) {
            SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
            boolean loginChecked = pref.getBoolean("loginChecked", false);
            if (loginChecked) {
                SharedPreferences.Editor editor = pref.edit();
                editor.clear();
                editor.commit();
                SharedPreferences autoLogin = getSharedPreferences("autoLogin", Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor2 = autoLogin.edit();
                editor2.clear();
                editor2.commit();
                ShortcutBadger.removeCount(getApplicationContext());
                if (!applicationLifecycleHandler.isInBackground()) {
                    Message msg = handler.obtainMessage();
                    msg.obj = "1";
                    handler.sendMessage(msg);
                    try {
                        Thread.sleep(3000);
                        Intent killApp = new Intent(FirebaseMessagingService.this, MainActivity.class);
                        killApp.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        killApp.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        killApp.putExtra("KILL_APP", true);
                        startActivity(killApp);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    getOtherLogin = true;
                    Message msg = handler.obtainMessage();
                    msg.obj = "2";
                    handler.sendMessage(msg);
                }
            } else {
                SharedPreferences.Editor editor = pref.edit();
                editor.clear();
                editor.commit();
                SharedPreferences autoLogin = getSharedPreferences("autoLogin", Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor2 = autoLogin.edit();
                editor2.clear();
                editor2.commit();
                ShortcutBadger.removeCount(getApplicationContext());
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void sendNotificationText(String text, String link) {


    Intent intent = null;
        if ("".equals(link)) {
            intent = new Intent(this, SplashActivity.class);
        } else {
            intent = new Intent(Intent.ACTION_VIEW).setData(Uri.parse(link));
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, (int) (System.currentTimeMillis()/1000), intent, PendingIntent.FLAG_ONE_SHOT);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder notificationBuilder = null;
        if (Build.VERSION.SDK_INT >= 26) {
            NotificationChannel notificationChannel = new NotificationChannel("부동산 다이어리", "부동산 다이어리", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(notificationChannel);
            notificationBuilder = new NotificationCompat.Builder(this, notificationChannel.getId());
        } else {
            notificationBuilder = new NotificationCompat.Builder(this);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            notificationBuilder.setSmallIcon(getResources().getIdentifier("icon_noti", "drawable", getPackageName()));
            notificationBuilder.setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.drawable.icon));
        } else {
            notificationBuilder.setSmallIcon(getResources().getIdentifier("icon", "drawable", getPackageName()));
        }
        notificationBuilder.setContentTitle("부동산 다이어리")
                .setContentText(text)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 })
                .setLights(Color.RED, 3000, 3000)
                .setContentIntent(pendingIntent);
        notificationManager.notify(0, notificationBuilder.build());
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void sendNotificationImage(String text, String image, String link) {
        Intent intent = null;
        if ("".equals(link)) {
            intent = new Intent(this, SplashActivity.class);
        } else {
            intent = new Intent(Intent.ACTION_VIEW).setData(Uri.parse(link));
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, (int) (System.currentTimeMillis()/1000), intent, PendingIntent.FLAG_ONE_SHOT);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder notificationBuilder = null;
        if (Build.VERSION.SDK_INT >= 26) {
            NotificationChannel notificationChannel = new NotificationChannel("부동산 다이어리", "부동산 다이어리", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(notificationChannel);
            notificationBuilder = new NotificationCompat.Builder(this, notificationChannel.getId());
        } else {
            notificationBuilder = new NotificationCompat.Builder(this);
        }
        try {
            URL url = new URL(image);
            Bitmap getBitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                notificationBuilder.setSmallIcon(getResources().getIdentifier("icon_noti", "drawable", getPackageName()));
                notificationBuilder.setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.drawable.icon));
            } else {
                notificationBuilder.setSmallIcon(getResources().getIdentifier("icon", "drawable", getPackageName()));
            }
            notificationBuilder.setContentTitle("부동산 다이어리")
                    .setContentText("아래로 드래그하여 알림을 확인해주세요.")
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 })
                    .setLights(Color.RED, 3000, 3000)
                    .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(getBitmap).setBigContentTitle("부동산 다이어리").setSummaryText(text))
                    .setContentIntent(pendingIntent);
            notificationManager.notify(0, notificationBuilder.build());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
