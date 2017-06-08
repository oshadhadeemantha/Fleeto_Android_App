package com.example.thilinidineshika.mapdirection;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText username= (EditText) findViewById(R.id.etuser);
        final EditText password= (EditText) findViewById(R.id.etpassword);
        final Button login = (Button) findViewById(R.id.blog);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String uname = username.getText().toString();
                final String pword = password.getText().toString();

                Response.Listener<String> responselisner = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject= new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");

                            if(success){
                                String id=jsonObject.getString("driver_id");
                                Driver driver=new Driver();
                                driver.driver_id=id;
                                Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                                LoginActivity.this.startActivity(intent);
                            }

                            else{
                                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                builder.setMessage("Login Failed")
                                        .setNegativeButton("Retry",null)
                                        .create()
                                        .show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                };

                LoginRequest  loginRequest = new LoginRequest(uname,pword,responselisner);
                RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
                queue.add(loginRequest);

            }
        });
    }
}
