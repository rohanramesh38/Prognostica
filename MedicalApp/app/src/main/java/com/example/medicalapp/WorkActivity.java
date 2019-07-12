package com.example.medicalapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.medicalapp.fragments.DocsFragment;
import com.example.medicalapp.fragments.SearchFragment;
import com.example.medicalapp.fragments.SettingsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomnavigation.BottomNavigationView.OnNavigationItemSelectedListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class WorkActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work);
        BottomNavigationView navigationView= findViewById(R.id.navigation);

        DatabaseReference db= FirebaseDatabase.getInstance().getReference().child("Symptoms");

        DatabaseReference db1= FirebaseDatabase.getInstance().getReference().child("Disease");


        navigationView.setOnNavigationItemSelectedListener(new OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {



                Fragment fragment;
                switch(menuItem.getItemId())
                {
                    case R.id.nav_home:
                        fragment=new SearchFragment();
                        loadFragment(fragment);
                        break;

                    case R.id.nav_docs:
                        fragment=new DocsFragment();
                        loadFragment(fragment);

                        break;

                    case R.id.nav_setting:

                        fragment=new SettingsFragment();
                        loadFragment(fragment);

                        break;
                }
                return false;
            }
        });

   navigationView.setSelectedItemId(R.id.nav_home);
    }


    private void loadFragment(Fragment fragment)
    {
        FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container,fragment);
        transaction.addToBackStack(null);
        transaction.commit();

    }
}
