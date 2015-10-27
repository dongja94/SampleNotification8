package com.example.dongja94.samplenotification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    NotificationManager mNM;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        Button btn = (Button)findViewById(R.id.btn_send);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendNotification();
            }
        });

        btn = (Button)findViewById(R.id.btn_download);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDwonload();
            }
        });

        mNM = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
    }


    private void startDwonload() {
        progress = 0;
        mHandler.post(downloadRunnable);
    }

    Handler mHandler = new Handler(Looper.getMainLooper());
    int progress = 0;
    Runnable downloadRunnable = new Runnable() {
        @Override
        public void run() {
            if (progress <= 100) {
                NotificationCompat.Builder builder = new NotificationCompat.Builder(MainActivity.this);
                builder.setSmallIcon(R.mipmap.ic_launcher);
                builder.setTicker("download...");
                builder.setContentTitle("file download");
                builder.setContentText("download : " + progress);
                builder.setProgress(100, progress, false);

                builder.setDefaults(NotificationCompat.DEFAULT_ALL);

                builder.setOngoing(true);
                builder.setOnlyAlertOnce(true);
                mNM.notify(100, builder.build());

                progress += 10;
                mHandler.postDelayed(this, 500);
            } else {
                mNM.cancel(100);
            }

        }
    };

    int count = 0;

    private void sendNotification() {
        count++;
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setTicker("Notification Test...");
        builder.setContentTitle("Notification Title");
        builder.setContentText("noticiation text : " + count);
        builder.setDefaults(NotificationCompat.DEFAULT_ALL);
        builder.setAutoCancel(true);
        Intent intent = new Intent(this, NotifyActivity.class);
        intent.setData(Uri.parse("myscheme://" + getPackageName() + "/" + count));
        intent.putExtra(NotifyActivity.EXTRA_MESSAGE, "count : " + count);
        PendingIntent pi = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(pi);


        Notification notification = builder.build();

        mNM.notify(count, notification);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
