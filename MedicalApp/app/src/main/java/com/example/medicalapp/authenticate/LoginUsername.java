package com.example.medicalapp.authenticate;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;

import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.medicalapp.R;


import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

public class LoginUsername extends AppCompatActivity implements View.OnClickListener {

    private Toolbar toolbar;

    private EditText usernameET;
    private Button nextB;
    private TextView statusinfoTV;
    private String username;
    private LottieAnimationView progressBar;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_username);

        //TOOLBAR
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //WIDGETS
        usernameET = findViewById(R.id.username);
        nextB = findViewById(R.id.next_login);
        nextB.setOnClickListener(this);
        statusinfoTV = findViewById(R.id.statusInfo);
        progressBar = findViewById(R.id.progressBar);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.next_login:
                statusinfoTV.setOnClickListener(null);
                username = usernameET.getText().toString();
                if(validateUsername()){
                    statusinfoTV.setText("");
                    if(connectedToNetwork()){ progressBar.setVisibility(View.VISIBLE); volley(); }
                    else{ NoInternetAlertDialog(); }
                }
                break;

            case R.id.statusInfo:
                Intent openSignupUsernameActivity = new Intent("android.intent.action.SIGNUPUSERNAME");
                openSignupUsernameActivity.putExtra("username", username);
                startActivity(openSignupUsernameActivity);
                break;

            default:
                break;
        }
    }

    @SuppressWarnings("MissingPermission")
    public boolean connectedToNetwork(){
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if( activeNetworkInfo!=null)
        {
            return activeNetworkInfo.isConnected();
        }

        return false;

    }
    public void NoInternetAlertDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("You are not connected to the internet. ");

        builder.setPositiveButton("Try again", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(connectedToNetwork()){
                    progressBar.setVisibility(View.VISIBLE);
                    System.out.println("network status"+ connectedToNetwork());
                    volley();
                }else{ NoInternetAlertDialog(); }
            }
        });

        builder.setNegativeButton("Settings", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent openSettings = new Intent();
                openSettings.setAction(Settings.ACTION_WIRELESS_SETTINGS);
                openSettings.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(openSettings);
            }
        });
        Dialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    private boolean validateUsername() {
        if(username.length() == 10){
            return true;
        }
        else if(username.length() == 0){
            username="";
            statusinfoTV.setText("enter mobile number");
            statusinfoTV.setTextColor(this.getResources().getColor(R.color.errorColor));
        }
        else{
            username="";
            statusinfoTV.setText("invalid");
            statusinfoTV.setTextColor(this.getResources().getColor(R.color.errorColor));
        }
        return false;
    }

    private void volley() {

    }


    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
