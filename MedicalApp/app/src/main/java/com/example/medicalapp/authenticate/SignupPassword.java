package com.example.medicalapp.authenticate;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.medicalapp.R;


public class SignupPassword extends AppCompatActivity implements View.OnClickListener {

    private Toolbar toolbar;

    private EditText passwordET, confirmPasswordET;
    private Button nextB;
    private TextView statusinfoTV, usernameTV;
    private String password, username;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_password);

        //TOOLBAR
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //WIDGETS
        usernameTV = findViewById(R.id.username);
        passwordET = findViewById(R.id.password);
        confirmPasswordET = findViewById(R.id.confirmPassword);
        statusinfoTV = findViewById(R.id.statusInfo);
        nextB = findViewById(R.id.next_signup2);
        nextB.setOnClickListener(this);

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
            case R.id.next_signup2:
                password = passwordET.getText().toString();
                if(validatePassword()){
                    statusinfoTV.setText("");
                    Intent openSignupDetailActivity = new Intent("android.intent.action.SIGNUPDETAIL");
                    Bundle b = new Bundle();
                    b.putString("username", username);
                    b.putString("password", password);
                    openSignupDetailActivity.putExtras(b);
                    startActivity(openSignupDetailActivity);
                }
                break;

            default:
                break;
        }
    }

    private boolean validatePassword() {
        String confirmPassword = confirmPasswordET.getText().toString();
        if(password.length()==0){
            statusinfoTV.setText("enter a password");
            statusinfoTV.setTextColor(this.getResources().getColor(R.color.errorColor));
        }
        else if(password.length()<5){
            statusinfoTV.setText("enter a stronger password");
            statusinfoTV.setTextColor(this.getResources().getColor(R.color.errorColor));
        }
        else if(confirmPassword.length()==0){
            statusinfoTV.setText("confirm your pwd");
            statusinfoTV.setTextColor(this.getResources().getColor(R.color.errorColor));
        }
        else if(!password.equals(confirmPassword)){
            statusinfoTV.setText("password dosent match");
            statusinfoTV.setTextColor(this.getResources().getColor(R.color.errorColor));
        }
        else{
            return true;
        }
        return false;
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
