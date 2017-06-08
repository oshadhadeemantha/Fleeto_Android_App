package com.example.thilinidineshika.mapdirection;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by OshadhaDeemantha on 3/20/2017.
 */

public class NotificationRequest extends StringRequest {

    private static final String Notification_URL="http://fleeto.000webhostapp.com/notifi.php";
    private Map<String, String> params;
    public NotificationRequest(String driver_id,String token, Response.Listener<String> listener) {
        super(Method.POST,Notification_URL, listener,null);
        params = new HashMap<String, String>();
        params.put("driverid",driver_id);
        params.put("Token",token);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
