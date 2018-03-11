package finalcom.javadmorsali.adsfinder;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);
        View parentLayout = findViewById(android.R.id.content);
        IsInternerConnected(parentLayout);
    }



    public boolean IsInternerConnected(View v) {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            Handler h=new Handler();
            h.postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(Splash.this, ShowAdsList.class));
                    finish();
                }
            },3000);
            return true;
        } else {
            ShowSnack(v, "عدم اتصال به اینترنت!", "تلاش دوباره");
            return false;
        }

    }


/*
    private void TryIsInternerConnected(View v) {
        try {
            IsInternerConnected(v);

        } catch (Exception ex) {
            ShowSnack(v, "عدم اتصال به اینترنت!", "تلاش دوباره");
        }
    }

*/


    private void SetNotification() {
        NotificationCompat.Builder mBuilder= new NotificationCompat.Builder(this);

        Intent intent = new Intent(this, Splash.class);
        //PendingIntent pendingIntent= PendingIntent.getActivity(this,0,i,0);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        mBuilder.setAutoCancel(true);
        mBuilder.setDefaults(NotificationCompat.DEFAULT_ALL);
        mBuilder.setWhen(20000);
        mBuilder.setTicker("Ticker");
        mBuilder.setContentInfo("Info");
        mBuilder.setContentIntent(pendingIntent);
        mBuilder.setSmallIcon(R.drawable.adsfinder);
        mBuilder.setContentTitle("New notification title");
        mBuilder.setContentText("Notification text");
        mBuilder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));

        NotificationManager notificationManager= (NotificationManager)getSystemService(this.NOTIFICATION_SERVICE);
        notificationManager.notify(2,mBuilder.build());
    }

    private void ShowSnack(View v, String Text, String Action) {

        View.OnClickListener lis = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IsInternerConnected(v);

            }
        };

        Snackbar snackbar = Snackbar
                .make(v, Text, Snackbar.LENGTH_LONG)
                .setAction(Action, lis);

        snackbar.setActionTextColor(Color.RED);

        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.YELLOW);
        snackbar.show();
    }


    public void checkInternet(View view) {
        IsInternerConnected(view);
    }
}
