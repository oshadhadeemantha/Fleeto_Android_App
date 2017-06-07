package com.example.thilinidineshika.mapdirection;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ItemListActivity extends AppCompatActivity {

    String JsonUrl = "http://fleeto.000webhostapp.com/item.php?driverId="+Driver.driver_id;
    RequestQueue requestQueue;
    ArrayList<HashMap<String, String>> itemList;
    private static final String TAG_ID = "itemid";
    private static final String TAG_NAME = "item_name";
    private static final String TAG_Customer ="cu_name";
    ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);

        final Button bdelete =(Button) findViewById(R.id.delete);
        final EditText etItem=(EditText)findViewById(R.id.itemid);

        bdelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String itemid = etItem.getText().toString();
                int lastitemid=Integer.parseInt(itemid);

                Response.Listener<String> responseListner = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            boolean success =jsonObject.getBoolean("success");

                            if(success){

                                Toast.makeText(ItemListActivity.this,"Delete Success",Toast.LENGTH_SHORT).show();
                                Intent intent = getIntent();
                                finish();
                                startActivity(intent);
                            }

                            else{
                                Toast.makeText(ItemListActivity.this,"Delete Fail",Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {

                            e.printStackTrace();
                        }
                    }
                };

                DeleteRequest deleteRequest = new DeleteRequest(lastitemid,responseListner);
                RequestQueue requestQueue = Volley.newRequestQueue(ItemListActivity.this);
                requestQueue.add(deleteRequest);
            }
        });

        list = (ListView) findViewById(R.id.listView);
        itemList = new ArrayList<HashMap<String,String>>();

        requestQueue = Volley.newRequestQueue(this);

        JsonArrayRequest arrayreq = new JsonArrayRequest(JsonUrl, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                try {

                    JSONObject values = response.getJSONObject(0);
                    JSONArray itemarray = values.getJSONArray("data");



                    for(int i=0;i<itemarray.length();i++) {

                        JSONObject jsonObject = itemarray.getJSONObject(i);


                        String itemid= jsonObject.getString("itemid");
                        String item_name= jsonObject.getString("item_name");
                        String customer= jsonObject.getString("cu_name");
                        HashMap<String,String> items = new HashMap<String,String>();

                        items.put(TAG_ID,"Item ID : "+itemid);
                        items.put(TAG_NAME,"Item Name : "+item_name);
                        items.put(TAG_Customer,"Customer : "+customer);

                        itemList.add(items);



                    }

                    ListAdapter adapter = new SimpleAdapter(ItemListActivity.this,itemList,R.layout.list,
                            new String[]{TAG_ID,TAG_NAME,TAG_Customer},
                            new int[]{R.id.itemid,R.id.item_name,R.id.customer});
                    list.setAdapter(adapter);



                } catch (JSONException e) {
                    e.printStackTrace();
                }
             }
        },

                    new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    Log.e("Volly","Erro");
                }
          }
        );
        requestQueue.add(arrayreq);
    }

}
