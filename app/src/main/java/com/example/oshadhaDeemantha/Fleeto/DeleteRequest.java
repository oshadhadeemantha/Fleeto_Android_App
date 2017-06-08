package com.example.thilinidineshika.mapdirection;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by OshadhaDeemantha on 3/19/2017.
 */

public class DeleteRequest extends StringRequest {

    private static final String Delete_Request_URL="http://fleeto.000webhostapp.com/delete.php";
    private Map<String, String> params;

    @Override
    public Map<String, String> getParams() {
        return params;
    }

    public DeleteRequest(int item_id, Response.Listener<String> listener) {
        super(Method.POST,Delete_Request_URL, listener,null);
        params = new HashMap<String, String>();
        params.put("itemid",item_id+"");
    }
}
