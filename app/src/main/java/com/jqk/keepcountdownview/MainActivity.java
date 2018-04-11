package com.jqk.keepcountdownview;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.jqk.countdownlibrary.CountdownView;

public class MainActivity extends AppCompatActivity {

    private CountdownView countdownView;

    private int progress = 0;

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch(msg.what) {
                case 1000:
                    progress++;
                    countdownView.setProgressRate(progress);
                    handler.sendEmptyMessageDelayed(1001, 10);
                    break;
                case 1001:
                    progress++;

                    if (progress == 100) {
                        countdownView.finish();
                    } else {
                        countdownView.setProgressRate(progress);
                        handler.sendEmptyMessageDelayed(1001, 10);
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        countdownView = (CountdownView)findViewById(R.id.countdownView);

        handler.sendEmptyMessage(1000);
    }
}
