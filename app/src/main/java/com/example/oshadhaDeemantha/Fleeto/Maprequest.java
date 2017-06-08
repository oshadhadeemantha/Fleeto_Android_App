package com.example.thilinidineshika.mapdirection;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by OshadhaDeemantha on 3/16/2017.
 */

public class Maprequest extends StringRequest {

    private static final String Map_Request_URL="http://fleeto.000webhostapp.com/newmap.php";
    private Map<String, String> params;

    public Maprequest(String driver_id, Response.Listener<String> listener) {
        super(Method.POST,Map_Request_URL, listener,null);
        params = new HashMap<String, String>();
        params.put("driver_id",driver_id);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
