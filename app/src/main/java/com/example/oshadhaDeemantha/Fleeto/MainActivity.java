package com.example.thilinidineshika.mapdirection;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity extends AppCompatActivity {


    RequestQueue requestQueue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseMessaging.getInstance().subscribeToTopic("test");
        String token= FirebaseInstanceId.getInstance().getToken().toString();
        Toast.makeText(MainActivity.this,"Success Login", Toast.LENGTH_LONG).show();


        Response.Listener<String> notifiresponcelisner = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        };

        NotificationRequest notificationRequest = new NotificationRequest(Driver.driver_id,token,notifiresponcelisner);
        RequestQueue notifi = Volley.newRequestQueue(MainActivity.this);
        notifi.add(notificationRequest);

        ImageButton bMap = (ImageButton) findViewById(R.id.bmap);
        ImageButton bQr = (ImageButton) findViewById(R.id.bqr);
        ImageButton blive = (ImageButton) findViewById(R.id.blive);
        ImageButton bjab = (ImageButton) findViewById(R.id.bjob);
        Button blogout = (Button) findViewById(R.id.blogout);

        bMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Response.Listener<String> reStringListener = new Response.Listener<String>(){

                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(MainActivity.this,"Success Login Map",Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(MainActivity.this,PathGoogleMapActivity.class);
                        intent.putExtra("response",response);
                        MainActivity.this.startActivity(intent);
                    }
                };
                Maprequest maprequest = new Maprequest(new Driver().driver_id,reStringListener);
                requestQueue = Volley.newRequestQueue(MainActivity.this);
                requestQueue.add(maprequest);

            }
        });

        // QR Reader
        bQr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Intent intent = new Intent(MainActivity.this,QRreaderActivity.class);
               // MainActivity.this.startActivity(intent);

            }
        });

        blive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,LiveStreamActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });

        bjab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent viewIntent = new Intent(MainActivity.this, ItemListActivity.class);
                MainActivity.this.startActivity(viewIntent);

            }
        });

        blogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent logout = new Intent(MainActivity.this,LoginActivity.class);
                logout.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(logout);

            }
        });
    }
    }

