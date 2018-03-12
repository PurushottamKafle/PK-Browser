package com.android.pkbrowser;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class WaitingActivity extends AppCompatActivity {
    private static int timer_first = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting);

        new Handler().postDelayed(new Runnable(){

            @Override
            public void run(){
                Intent intent3 = new Intent(WaitingActivity.this, MainActivity.class);
                startActivity(intent3);
                finish();
            }

        }, timer_first)                                                                  ;


    }
}
