package com.jorgepinedo.fivepizza;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.room.Room;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.jorgepinedo.fivepizza.Adapters.ListMenuAdapter;
import com.jorgepinedo.fivepizza.Adapters.ListMenuAdapterAdmin;
import com.jorgepinedo.fivepizza.Database.App;
import com.jorgepinedo.fivepizza.Models.Orders;
import com.jorgepinedo.fivepizza.Models.Products;
import com.jorgepinedo.fivepizza.Tools.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminActivity extends AppCompatActivity{

    CheckBox checkbox_printer;
    TextInputEditText tv_ipaddress,tv_table;
    RecyclerView recycler_products;
    ListMenuAdapterAdmin listMenuAdapter;
    List<Products> list;
    App app_db;
    StringRequest stringRequest;
    RequestQueue requestQueue;
<<<<<<< HEAD
    String IP = Utils.IP;
=======
    String IP;
>>>>>>> Kiosko

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        IP=Utils.getItem(this,"IP_SERVER");
<<<<<<< HEAD

=======
>>>>>>> Kiosko
        app_db = Room.databaseBuilder(this,App.class,"spad-five")
                .allowMainThreadQueries()
                .build();

        list = app_db.productsDAO().getAllProductsCategory(new int[]{1,2,3,4,5,6,7});


        checkbox_printer = findViewById(R.id.checkbox_printer);
        tv_ipaddress = findViewById(R.id.tv_ipaddress);
        tv_table = findViewById(R.id.tv_table);
        recycler_products = findViewById(R.id.recycler_products);

        if(Utils.getItem(AdminActivity.this,"PRINTER").equals("true")){
            checkbox_printer.setChecked(true);
        }

        tv_ipaddress.setText(Utils.getItem(this,"IP_SERVER"));
        tv_table.setText(Utils.getItem(this,"TABLE"));

        listMenuAdapter = new ListMenuAdapterAdmin(list,R.layout.card_product_item,this,app_db);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycler_products.setLayoutManager(linearLayoutManager);

        recycler_products.setAdapter(listMenuAdapter);
    }

    public void onClick(View view) {

        String url="";

        switch(view.getId()) {
            case R.id.btn_update:

                if (checkbox_printer.isChecked()){
                    Utils.setItem(AdminActivity.this,"PRINTER","true");
                }else{
                    Utils.setItem(AdminActivity.this,"PRINTER","false");
                }

                Utils.setItem(AdminActivity.this,"IP_SERVER",tv_ipaddress.getText().toString());
                Utils.setItem(AdminActivity.this,"TABLE",tv_table.getText().toString());

                Toast.makeText(AdminActivity.this,"Parametros Guardados",Toast.LENGTH_SHORT).show();

                break;

            case R.id.btn_restart:
                Orders orders = app_db.ordersDAO().getOrderCurrent();
                orders.setOrder_post_id(0);
                orders.setStatus_id(1);
                app_db.ordersDAO().update(orders);
                app_db.ordersDetailDAO().deleteDetail(orders.getId());
                Toast.makeText(AdminActivity.this,"Orden Reiniciada",Toast.LENGTH_SHORT).show();
                break;

            case R.id.btn_update_product:
                url = IP+"/api/products";

                Log.d("JORKE",url);

                stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        try {
                            JSONArray objArr = new JSONArray(response);
                            for(int i=0;i<objArr.length();i++){
                                JSONArray subArr = null;
                                subArr = objArr.getJSONArray(i);


                                for(int j=0;j<subArr.length();j++){
                                    JSONObject jsonObject = null;
                                    jsonObject = subArr.getJSONObject(j);
                                    Log.d("JORKE-server",jsonObject.toString());
                                    Products products = null;

                                    if(jsonObject.getString("category").equals("1")){
                                        Log.d("JORKE-id","one "+jsonObject.getString("id"));
                                        products = app_db.productsDAO().getProductByPos(Integer.parseInt(jsonObject.getString("id")),7);
                                    }else{
                                        Log.d("JORKE-id","two "+jsonObject.getString("id"));
                                        products = app_db.productsDAO().getProductByPosId(Integer.parseInt(jsonObject.getString("id")));
                                    }

                                    if(products!=null){

                                        products.setPrice(Float.parseFloat(jsonObject.getString("price")));
                                        app_db.productsDAO().update(products);
                                        Log.d("JORKE-P",products.toString());
                                    }else{
                                        Log.d("JORKE-notfount","this");

                                    }

                                }

                            }

                            listMenuAdapter.notifyDataSetChanged();

                            Toast.makeText(AdminActivity.this,"Products Update from server",Toast.LENGTH_SHORT).show();


                        } catch (JSONException e) {
                            Log.d("JORKE-e",e.getMessage());
                            e.printStackTrace();
                        }
                    }
                },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d("JORKE",error.getMessage()+"");
                                Toast.makeText(AdminActivity.this,"Problemas con la solicitud",Toast.LENGTH_LONG).show();
                            }
                        }
                ){
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> headers = new HashMap<String, String>();
                        headers.put("Content-Type", "application/x-www-form-urlencoded");
                        //params.put("Content-Type", "application/json; charset=utf-8");
                        headers.put("Accept", "application/json");
                        return headers;
                    }
                };

                requestQueue= Volley.newRequestQueue(this);
                requestQueue.add(stringRequest);



                break;
            case R.id.btn_close:
                finish();
                break;

        }
    }

}
