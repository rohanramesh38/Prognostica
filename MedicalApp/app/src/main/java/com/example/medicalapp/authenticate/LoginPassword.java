package com.example.medicalapp.authenticate;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;

import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.airbnb.lottie.LottieAnimationView;

import com.example.medicalapp.R;


public class LoginPassword extends AppCompatActivity implements View.OnClickListener{

    private Toolbar toolbar;

    private EditText passwordET;
    private Button loginB;
    private TextView statusinfoTV, usernameTV;
    private String password, username;
    private LottieAnimationView progressBar;
    private String token;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_password);

        //TOOLBAR
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

       /* FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w("Failure", "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        token = task.getResult().getToken();

                        // Log and toast
                        Log.d("Token", token);
                        //Toast.makeText(LoginPassword.this, token, Toast.LENGTH_SHORT).show();
                    }
                });

        *///WIDGETS
        //usernameTV = findViewById(R.id.username);
        passwordET = findViewById(R.id.password);
        loginB = findViewById(R.id.login);
        loginB.setOnClickListener(this);
        statusinfoTV = findViewById(R.id.statusInfo);
        statusinfoTV.setOnClickListener(this);
        progressBar = findViewById(R.id.progressBar);

        //GET INTENT
        Intent i = getIntent();
        if(i.hasExtra("username")){
            username = i.getStringExtra("username");
            //usernameTV.setText(username);
        }else{ finish(); }
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.login:
                password = passwordET.getText().toString();
                if(validatePassword()){
                    statusinfoTV.setText("Forgot Password?");
                    if(connectedToNetwork()){progressBar.setVisibility(View.VISIBLE); volley(); }
                    else{ NoInternetAlertDialog(); } }
                break;

            case R.id.statusInfo:
               /* statusinfoTV.setTextColor(getResources().getColor(R.color.blueColor));
                System.out.println("working...");
                Intent openForgotPasswordActivity = new Intent(LoginPassword.this,ForgotPassword.class);
                openForgotPasswordActivity.putExtra("username", username);
                startActivity(openForgotPasswordActivity);
                */break;

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

    private boolean validatePassword() {
        if(password.length()>4){ return true; }
        else if(password.length()==0){
            statusinfoTV.setText("Empty Password");
            //statusinfoTV.setTextColor(this.getResources().getColor(R.color.errorColor));
        }
        else{
            statusinfoTV.setText("Incvalid Password");
          //  statusinfoTV.setTextColor(this.getResources().getColor(R.color.errorColor));
        }
        return false;
    }


    private void volley() {
    }


    private void saveCredentials(String id, String name, String phonenumber,String mail, boolean status, boolean otplogin) {
        SharedPreferences sp = getApplicationContext().getSharedPreferences("com.optn.PRIVATEDATA", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("loggedIn", status);
        editor.putString("id", id);
        editor.putString("name", name);
        editor.putString("phonenumber", phonenumber);
        editor.putString("email",mail);
        if(!otplogin){
            if(sp.contains("otp")){
                editor.remove("otp");
            }
        }
        editor.apply();
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
