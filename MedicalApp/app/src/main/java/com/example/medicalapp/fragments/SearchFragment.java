package com.example.medicalapp.fragments;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.example.medicalapp.MedActivity;
import com.example.medicalapp.MyVolleyRequest;
import com.example.medicalapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {
    private String str="--";
    private List<String> list,listSend;
    private  String strForFlask="";
    private ListView listView;
    public SearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_search, container, false);
        list=new ArrayList<String>();
        listSend=new ArrayList<String>();

        listView=v.findViewById(R.id.listSearch);

        FirebaseDatabase.getInstance().getReference().child("Symptoms").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot d1:dataSnapshot.getChildren())
                {
                    list.add(d1.getValue().toString());
                }

                System.out.println(list);
                ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,list);

                listView.setAdapter(arrayAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if(listSend.indexOf(list.get(position))<0)
                {
                    // view.setBackground(R.color.colorPrimary);
                    strForFlask=strForFlask+list.get(position);
                    listSend.add(list.get(position));
                    volley();
                    System.out.println(listSend);
                }
                else {
                    // view.setBackgroundColor(R.color.whiteColor);
                }
                System.out.println(strForFlask);
            }
        });




        return v;

    }

//http://192.168.43.204:5051/predict
    private  void volley()
    {
        try{
            System.out.println("endpoint");
            JSONObject jsonObject1 = new JSONObject();
            jsonObject1.put("data", listSend);
            JsonObjectRequest jsonObjectRequest3 = new JsonObjectRequest(Request.Method.POST,
                    "http://10.10.56.58:5051/predict", jsonObject1,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            System.out.println("driver status :"+response);

                            if(response!=null){
                                try {

                                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                                    String result=response.get("disease").toString().replace("[","");
                                    result=result.replace("]","");

                                    str=result.replace("'"," ");
                                    str=result.replace("\""," ");
                                    System.out.println(str);

                                    builder.setTitle(" Prognosis ");
                                    builder.setMessage(result);

                                    builder.setPositiveButton("Add Symp", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    });

                                    builder.setNegativeButton("Info", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                            Intent i=new Intent(getActivity(), MedActivity.class);
                                            i.putExtra("list",str);

                                           //           listSend.clear();

                                            startActivity(i);
                                        }
                                    });

                                    builder.setNeutralButton("done",new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                           listSend.clear();
                                        }
                                    });





                                    Dialog dialog = builder.create();
                                    dialog.setCanceledOnTouchOutside(false);
                                    dialog.show();


                                }
                                catch(Exception e){
                                    e.printStackTrace();
                                }
                            }

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            NetworkResponse networkResponse = error.networkResponse;
                            if(networkResponse!=null){
                                int statusCode = networkResponse.statusCode;

                                if(statusCode== HttpURLConnection.HTTP_NOT_FOUND){

                                }
                                error.printStackTrace();
                            }
                        }
                    }



            );
            MyVolleyRequest.getInstance(getActivity()).addToRequestQueue(jsonObjectRequest3);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}