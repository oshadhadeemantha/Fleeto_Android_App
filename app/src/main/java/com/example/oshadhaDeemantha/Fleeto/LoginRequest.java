package com.example.thilinidineshika.mapdirection;

import android.app.DownloadManager;
import android.service.voice.VoiceInteractionSession;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by OshadhaDeemantha on 3/19/2017.
 */

public class LoginRequest extends StringRequest{
    private static final String Login_Request_URl = "http://fleeto.000webhostapp.com/login.php";
    private Map<String, String> params;


    public LoginRequest(String username, String password, Response.Listener<String> listener) {
        super(Method.POST,Login_Request_URl, listener,null);
        params = new HashMap<>();
        params.put("username", username);
        params.put("password", password);
    }

    @Override
    protected Map<String, String> getParams() {
        return params;
    }
}
