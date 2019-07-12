package com.example.medicalapp.utils;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.medicalapp.R;
import com.example.medicalapp.WorkActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

            Thread myThread = new Thread(){
                @Override
                public void run() {
                    try {
                        sleep(4000);
                    }
                    catch (InterruptedException e) { e.printStackTrace(); }
                    finally {

                        Intent openMainActivity = new Intent(MainActivity.this, WorkActivity.class);
                        startActivity(openMainActivity);
                        if(getLoginStatus()){


                        }
                        else{
                       //     Intent openIntroductionActivity = new Intent("android.intent.action.INTRODUCTION");
                         //   startActivity(openIntroductionActivity);
                        }
                    }
                }
            };
            myThread.start();
        }

        @Override
        protected void onStop() {
            finish();
            super.onStop();
        }

        private boolean getLoginStatus() {
            SharedPreferences sp = getApplicationContext().getSharedPreferences("com.kcg.PRIVATEDATA", Context.MODE_PRIVATE);
            if(sp.contains("loggedIn")){
                return sp.getBoolean("loggedIn",false); }
            return false;
        }

    }