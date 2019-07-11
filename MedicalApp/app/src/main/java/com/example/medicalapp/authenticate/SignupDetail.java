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

import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.medicalapp.R;


import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

public class SignupDetail extends AppCompatActivity implements View.OnClickListener {

    private Toolbar toolbar;

    private String email="", name="", username="", password="";
    private EditText emailET, firstNameET, lastNameET;
    private TextView nameInfoTV, emailInfoTV;
    private Button signupB;
    private LottieAnimationView progressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_detail);

        //TOOLBAR
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //WIDGET
        emailET = findViewById(R.id.email);
        firstNameET = findViewById(R.id.firstName);
        lastNameET = findViewById(R.id.lastName);
        nameInfoTV = findViewById(R.id.nameInfo);
        emailInfoTV = findViewById(R.id.emailInfo);
        signupB = findViewById(R.id.signup);
        signupB.setOnClickListener(this);
        progressBar = findViewById(R.id.progressBar);

        //GET INTENT
        Intent i = getIntent();
        Bundle b = i.getExtras();
        if(b!=null){
            if((!b.getString("username").equals("")) & (!b.getString("password").equals(""))){
                username = i.getStringExtra("username");
                password = i.getStringExtra("password");

            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.signup:
                String lastN = lastNameET.getText().toString();
                String firstN = firstNameET.getText().toString();
                if(lastN.length()>0){
                    name = firstN + " " + lastN;
                }
                else{
                    name = firstN;
                }
                email = emailET.getText().toString();
                if(validateName() & validateEmail()){
                    if(connectedToNetwork()){ progressBar.setVisibility(View.VISIBLE); volley(); }
                    else{ NoInternetAlertDialog(); }
                }
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

    private boolean validateEmail() {
        if(email.length()==0){
            emailInfoTV.setText("Enter Your Mail");
         //   emailInfoTV.setTextColor(this.getResources().getColor(R.color.errorColor));
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailInfoTV.setText("Enter a valid Mail");
           // emailInfoTV.setTextColor(this.getResources().getColor(R.color.errorColor));
        }
        else{
            emailInfoTV.setText("");
            return true;
        }
        return false;
    }

    private boolean validateName() {
        if(name.length()==0){
            nameInfoTV.setText("Enter Your Name");
            //nameInfoTV.setTextColor(this.getResources().getColor(R.color.errorColor));
        }
        else{
            nameInfoTV.setText("");
            return true;
        }
        return false;
    }

    private void volley() {
        //CLEARING ERROR
        nameInfoTV.setText("");
        emailInfoTV.setText("");

    }

    private void saveCredentials(String id, String name, String phonenumber,String mail) {
        SharedPreferences sp = getApplicationContext().getSharedPreferences("com.optn.PRIVATEDATA", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("loggedIn", true);
        editor.putString("id", id);
        editor.putString("name", name);
        editor.putString("phonenumber", phonenumber);
        editor.putString("email",mail);
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
