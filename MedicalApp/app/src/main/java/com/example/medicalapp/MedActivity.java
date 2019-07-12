package com.example.medicalapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MedActivity extends AppCompatActivity {
    String str[];
    private TextView tv;
    String fin="_";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_med);
        Intent i=getIntent();

        String s=i.getStringExtra("list");
        s=s.replace("'"," ");
         str=s.split(",");
tv=findViewById(R.id.tv2);
        for(int j=0;j<str.length;j++)
        {
            System.out.println(str[j]);

        }

        FirebaseDatabase.getInstance().getReference().child("Disease").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot ds:dataSnapshot.getChildren())
                {
                    for(int j=0;j<str.length;j++)
                    {
                     if((ds.getKey().trim().toLowerCase()).equals(str[j].trim().toLowerCase()))
                     {
                         fin=fin+ds.getValue().toString()+"_";
                     }
                    }
                }
          System.out.println(fin);
                fin=fin.replace("_",",");
             fin=fin.substring(1,fin.length()-1);
                tv.setText(fin);
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




    }
}
