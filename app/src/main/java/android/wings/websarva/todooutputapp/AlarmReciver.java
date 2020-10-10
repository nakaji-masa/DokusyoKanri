package android.wings.websarva.todooutputapp;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class AlarmReciver extends BroadcastReceiver {

    public static String NOTIFICATION_ID = "notificationId";

    public static String NOTIFICATION_CONTENT = "content";

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onReceive(Context context, Intent intent){

        NotificationChannel channel = new NotificationChannel(NOTIFICATION_ID, "アクションプランの確認", NotificationManager.IMPORTANCE_DEFAULT);
        NotificationManager notificationManager = (NotificationManager)context.getSystemService(context.NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(channel);



        String content = intent.getStringExtra(NOTIFICATION_CONTENT);



        notificationManager.notify(0, buildNotification(context, content));
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private Notification buildNotification(Context context, String content) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, NOTIFICATION_ID);

        builder.setContentTitle("アクションプランは実行できていますか？");
        builder.setContentText(content);

        builder.setSmallIcon(R.drawable.ic_launcher_background);
        builder.setStyle(new NotificationCompat.BigTextStyle());

        DatabaseHelper helper = new DatabaseHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();
        String id = "";
        try{
            String sql = "SELECT _id FROM BookList WHERE bookActionplan = " + "'" + content + "'";

            Cursor cursor = db.rawQuery(sql, null);

            while(cursor.moveToNext()){
                int idx = cursor.getColumnIndex("_id");
                id = cursor.getString(idx);
            }


        }

        finally {
            db.close();
        }

        int idInt = Integer.parseInt(id);

        Intent intent = new Intent(context, Book_detail.class);
        intent.putExtra("_id", idInt);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        builder.setContentIntent(pendingIntent);
        builder.setAutoCancel(true);

        Log.d("build", "start");

        return builder.build();


    }
}
