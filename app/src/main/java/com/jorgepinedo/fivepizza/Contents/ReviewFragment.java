package com.jorgepinedo.fivepizza.Contents;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.jorgepinedo.fivepizza.Adapters.ListMenuAdapterReview;
import com.jorgepinedo.fivepizza.Adapters.ListMenuDrinkAdapter;
import com.jorgepinedo.fivepizza.Database.App;
import com.jorgepinedo.fivepizza.FinishActivity;
import com.jorgepinedo.fivepizza.MainActivity;
import com.jorgepinedo.fivepizza.Models.Orders;
import com.jorgepinedo.fivepizza.Models.OrdersDetail;
import com.jorgepinedo.fivepizza.Models.Products;
import com.jorgepinedo.fivepizza.Models.Review;
import com.jorgepinedo.fivepizza.R;
import com.jorgepinedo.fivepizza.Tools.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ReviewFragment extends Fragment implements ListMenuAdapterReview.EventCustomer{

    App app_db;
    RecyclerView recycler_review,recycler_reviewed;
    ListMenuAdapterReview listMenuAdapter,listedMenuAdapter;
    List<Products> list;
    List<Review> listReviewMain,listReviewTotal,listReviewDrink,listedReviewMain,listedReviewTotal,listedReviewDrink;


    Button payment,other_pizza;
    StringRequest stringRequest;
    TextView total_review;

    private static final String IP = Utils.IP;

    RequestQueue requestQueue;

    public ReviewFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        app_db = Utils.newInstanceDB(getActivity());

        View view = inflater.inflate(R.layout.fragment_review, container, false);

        listReviewMain = app_db.ordersDetailDAO().getReviewNotIn(7,1);
        listReviewDrink = app_db.ordersDetailDAO().getReviewIn(7,1);

        listReviewTotal = new ArrayList<>();

        listReviewTotal.addAll(Utils.joinAditionals(listReviewMain,app_db));
        listReviewTotal.addAll(listReviewDrink);

        listedReviewMain = app_db.ordersDetailDAO().getReviewNotIn(7,2);
        listedReviewDrink = app_db.ordersDetailDAO().getReviewIn(7,2);

        listedReviewTotal = new ArrayList<>();

        listedReviewTotal.addAll(Utils.joinAditionals(listedReviewMain,app_db));
        listedReviewTotal.addAll(listedReviewDrink);


        recycler_review = view.findViewById(R.id.recycler_review);
        recycler_reviewed = view.findViewById(R.id.recycler_reviewed);
        total_review = view.findViewById(R.id.total_review);

        String total_review_calc = Utils.numberFormat(app_db.ordersDetailDAO().getTotal(2));

        total_review.setText("$"+total_review_calc);

        payment = view.findViewById(R.id.payment);
        other_pizza = view.findViewById(R.id.other_pizza);

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycler_review.setLayoutManager(linearLayoutManager);

        listMenuAdapter = new ListMenuAdapterReview(listReviewTotal,R.layout.card_product_review,getActivity(), (ListMenuAdapterReview.EventCustomer) this,app_db);
        recycler_review.setAdapter(listMenuAdapter);


        LinearLayoutManager linearLayoutManager2=new LinearLayoutManager(getActivity());
        linearLayoutManager2.setOrientation(LinearLayoutManager.VERTICAL);
        recycler_reviewed.setLayoutManager(linearLayoutManager2);

        listedMenuAdapter = new ListMenuAdapterReview(listedReviewTotal,R.layout.card_product_review,getActivity(), (ListMenuAdapterReview.EventCustomer) this,app_db);
        recycler_reviewed.setAdapter(listedMenuAdapter);


        payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createOrder();
            }

        });
        other_pizza.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                app_db.ordersDetailDAO().updateNewRequest();
                Intent i = new Intent(getActivity(),MainActivity.class);
                Bundle b = new Bundle();
                b.putString("status","new");
                i.putExtras(b);
                startActivity(i);
            }
        });

        ((MainActivity)getActivity()).hideInformation(true);

        return view;
    }


    public void createOrder(){
        String url=IP+"/api/orders";
        Log.d("JORKE",url);

        //startActivity(new Intent(getActivity(), FinishActivity.class));

        stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    Orders orders = app_db.ordersDAO().getOrderCurrent();

                    JSONObject obj = new JSONObject(response);

                    Log.d("JORKE-1",response);
                    /*Toast.makeText(getActivity(),"Solicitud creada #"+obj.getString("OrderID"),Toast.LENGTH_LONG).show();*/


                    startActivity(new Intent(getActivity(), FinishActivity.class));


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("JORKE",error.getMessage()+"");
                        Toast.makeText(getActivity(),"Problemas con la solicitud",Toast.LENGTH_LONG).show();
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

           @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String,String>();
                params.put("header",getOrderHeader());
                params.put("detail",getListDetailAll());
                Log.d("JORKE-SEND",params.toString());
                return params;
            }


        };

        requestQueue= Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }


    public String getOrderHeader(){
        HashMap<String, String> params = new HashMap<>();
        params.put("DineInTableID","1");
        params.put("StationID","1");
        params.put("OrderStatus","1");
        params.put("GuestCheckPrinted","false");
        Gson gson = new Gson();
        return gson.toJson(params);
    }

    public String getListDetailAll(){
        ArrayList<Map<String, String>> myList=new ArrayList<>();

        List<OrdersDetail> details = app_db.ordersDetailDAO().getAllOrdersDetail(1);

        Products products;

        for(OrdersDetail row:details){
            products = app_db.productsDAO().getProductById(row.getProduct_id());
            Map<String, String> params = new HashMap<String,String>();
            //params.put("priority",products.getPriority()+"");
            params.put("Quantity",row.getQuantity()+"");
            params.put("MenuItemID",products.getPos_id()+"");
            params.put("type_id",products.getType_id()+"");

            myList.add(params);
        }

        Gson gson = new Gson();
        return gson.toJson(myList);
    }

    public String getListDetail(){
        ArrayList<Map<String, String>> myList=new ArrayList<>();

        int pos_id=0;

        for(Review row:listReviewTotal){
            //products = app_db.productsDAO().getProductByPosId(row.getPos_id());
            Map<String, String> params = new HashMap<String,String>();
            //params.put("priority",products.getPriority()+"");
            params.put("Quantity",row.getQuantity()+"");
            pos_id = (row.getPos_id() == 0)?327:row.getPos_id();
            params.put("MenuItemID",pos_id+"");

            myList.add(params);
        }

        Gson gson = new Gson();
        return gson.toJson(myList);
    }

    @Override
    public void onClickUpdateTotal() {
        ((MainActivity)getActivity()).loadTotal();
    }
}
