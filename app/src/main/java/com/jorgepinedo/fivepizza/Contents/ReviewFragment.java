package com.jorgepinedo.fivepizza.Contents;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.LoginFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
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
import com.jorgepinedo.fivepizza.PaymentActivity;
import com.jorgepinedo.fivepizza.R;
import com.jorgepinedo.fivepizza.Tools.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ReviewFragment extends Fragment implements ListMenuAdapterReview.EventCustomer{

    App app_db;
    RecyclerView recycler_review,recycler_reviewed;
    ListMenuAdapterReview listMenuAdapter,listedMenuAdapter,adapterConfirm;
    List<Review> listReviewMain,listReviewTotal,listReviewDrink,listedReviewMain,listedReviewTotal,listedReviewDrink;


    Button payment,other_pizza;
    StringRequest stringRequest;
    private String IP="";
    RequestQueue requestQueue;

    private ProgressBar spinner;


    public ReviewFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        app_db = Utils.newInstanceDB(getActivity());

        View view = inflater.inflate(R.layout.fragment_review, container, false);

        IP = Utils.getItem(getActivity(),"IP_SERVER");

        int[] status={1,2};

        listReviewMain = app_db.ordersDetailDAO().getReviewNotIn(7,status);

        listReviewDrink = app_db.ordersDetailDAO().getReviewIn(7,status);

        listReviewTotal = new ArrayList<>();

        listReviewTotal.addAll(Utils.joinAditionals(listReviewMain,app_db));
        listReviewTotal.addAll(listReviewDrink);

        int[] status_fin={3};

        listedReviewMain = app_db.ordersDetailDAO().getReviewNotIn(7,status_fin);
        listedReviewDrink = app_db.ordersDetailDAO().getReviewIn(7,status_fin);

        listedReviewTotal = new ArrayList<>();

        listedReviewTotal.addAll(Utils.joinAditionals(listedReviewMain,app_db));
        listedReviewTotal.addAll(listedReviewDrink);


        recycler_review = view.findViewById(R.id.recycler_review);
        recycler_reviewed = view.findViewById(R.id.recycler_reviewed);



        payment = view.findViewById(R.id.payment);
        other_pizza = view.findViewById(R.id.other_pizza);
        spinner = view.findViewById(R.id.progressBar);


        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycler_review.setLayoutManager(linearLayoutManager);

        listMenuAdapter = new ListMenuAdapterReview(listReviewTotal,R.layout.card_product_review,getActivity(), (ListMenuAdapterReview.EventCustomer) this,app_db);
        recycler_review.setAdapter(listMenuAdapter);


        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(getActivity());
        linearLayoutManager2.setOrientation(LinearLayoutManager.VERTICAL);
        recycler_reviewed.setLayoutManager(linearLayoutManager2);

        listedMenuAdapter = new ListMenuAdapterReview(listedReviewTotal,R.layout.card_product_review,getActivity(), (ListMenuAdapterReview.EventCustomer) this,app_db);
        recycler_reviewed.setAdapter(listedMenuAdapter);



        payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmOrder();
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


    public void confirmOrder(){



        final AlertDialog dialog;
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
        View mView=getLayoutInflater().inflate(R.layout.dialog_confirm_order,null);

        Button accept = mView.findViewById(R.id.btn_accept);
        Button btn_cancel = mView.findViewById(R.id.btn_cancel);

        RecyclerView recycler_confirm = mView.findViewById(R.id.recycler_confirm);

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycler_confirm.setLayoutManager(linearLayoutManager);

        adapterConfirm = new ListMenuAdapterReview(listReviewTotal,R.layout.card_product_review,getActivity(), (ListMenuAdapterReview.EventCustomer) this,app_db);
        recycler_confirm.setAdapter(adapterConfirm);

        mBuilder.setView(mView);

        int width = (int)(getResources().getDisplayMetrics().widthPixels*0.90);
        int height = (int)(getResources().getDisplayMetrics().heightPixels*0.90);

        dialog = mBuilder.create();
        //dialog.getWindow().setLayout(width, height);
        dialog.setTitle("");
        dialog.show();


        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createOrder();
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    public void createOrder(){
        other_pizza.setEnabled(false);
        payment.setVisibility(View.GONE);
        spinner.setVisibility(View.VISIBLE);

        String url=IP+"/api/orders";
        Log.d("JORKE",url);

        final Orders orders = app_db.ordersDAO().getOrderCurrent();


        if(Utils.getItem(getActivity(),"PRINTER").equals("false")){
            app_db.ordersDetailDAO().printedOrder(orders.getId());
            startActivity(new Intent(getActivity(), FinishActivity.class));
        }else{
            stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    try {
                        JSONObject obj = new JSONObject(response);
                        Log.d("JORKE-1",response);

                        orders.setOrder_post_id(Integer.parseInt(obj.getString("OrderID")));
                        app_db.ordersDAO().update(orders);

                        other_pizza.setEnabled(true);
                        payment.setVisibility(View.VISIBLE);
                        spinner.setVisibility(View.GONE);
                        app_db.ordersDetailDAO().printedOrder(orders.getId());
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
                            payment.setVisibility(View.VISIBLE);
                            spinner.setVisibility(View.GONE);
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
                    //params.put("detail",getListDetailAll());
                    params.put("detail",getListDetailFormated());
                    Log.d("JORKE-SEND",params.toString());
                    return params;
                }
            };

            requestQueue= Volley.newRequestQueue(getContext());
            requestQueue.add(stringRequest);
        }


    }


    public String getOrderHeader(){
        Orders orders = app_db.ordersDAO().getOrderCurrent();
        HashMap<String, String> params = new HashMap<>();
        params.put("DineInTableID",Utils.getItem(getActivity(),"TABLE"));
        params.put("StationID","1");
        params.put("OrderStatus","1");
        params.put("type_id",orders.getType_id()+"");
        params.put("order_pos_id",orders.getOrder_post_id()+"");
        params.put("GuestCheckPrinted","false");
        Gson gson = new Gson();
        return gson.toJson(params);
    }

    public String getListDetailAll(){
        ArrayList<Map<String, String>> myList=new ArrayList<>();

        List<OrdersDetail> details = app_db.ordersDetailDAO().getAllOrdersDetail(new int[]{1,2});

        Products products;

        for(OrdersDetail row:details){
            products = app_db.productsDAO().getProductById(row.getProduct_id());
            Map<String, String> params = new HashMap<String,String>();
            //params.put("priority",products.getPriority()+"");
            params.put("Quantity",row.getQuantity()+"");
            params.put("MenuItemID",products.getPos_id()+"");
            params.put("category_id",products.getCategory_id()+"");
            myList.add(params);
        }

        Gson gson = new Gson();
        return gson.toJson(myList);
    }

    public String getListDetailFormated(){
        ArrayList<Map<String, String>> myList=new ArrayList<>();

        Products products;

        List<OrdersDetail> masas_list = app_db.ordersDetailDAO().getOrdersByCategories(new int[]{1,7},new int[]{1,2});

        for (OrdersDetail row:masas_list){
            Map<String, String> params = new HashMap<String,String>();
            products = app_db.productsDAO().getProductById(row.getProduct_id());

            params.put("id",row.getId()+"");
            params.put("Quantity",row.getQuantity()+"");
            params.put("MenuItemID",products.getPos_id()+"");
            params.put("category_id",products.getCategory_id() +"");

            if(products.getCategory_id() == 1){
                List<OrdersDetail> det = app_db.ordersDetailDAO().getOrdersByCategories(new int[]{2,3,4,5,6},row.getId());

                ArrayList<Map<String, String>> myListDet =new ArrayList<>();
                JSONArray jsonArray=new JSONArray();

                Products products1;

                for (OrdersDetail data:det){
                    Map<String, String> params_sub = new HashMap<String,String>();
                    products1 = app_db.productsDAO().getProductById(data.getProduct_id());
                    params_sub.put("id",data.getId()+"");
                    params_sub.put("Quantity",data.getQuantity()+"");
                    params_sub.put("MenuItemID",products1.getPos_id()+"");
                    params_sub.put("category_id",products1.getCategory_id()+"");
                    myListDet.add(params_sub);
                }

                Gson gson = new Gson();


                params.put("adds",gson.toJson(myListDet));
            }

            myList.add(params);
        }


        /*for(Review row:listReviewTotal){
            products = app_db.productsDAO().getProductByPosId(row.getPos_id());

            if(products.getCategory_id() == 1){
                Log.d("JORKE-ord",row.getId()+"");
                List<Review> list = app_db.ordersDetailDAO().getChild(row.getId());
                JSONArray det = new JSONArray();

                for (Review data:list){

                    try{
                        JSONObject desc=new JSONObject();
                        desc.put("id",data.getId()+"");

                        det.put(desc);

                    }catch (JSONException e){

                    }
                }

                params.put("adds",det.toString());
            }



            //params.put("priority",products.getPriority()+"");
            params.put("id",row.getId()+"");
            params.put("Quantity",row.getQuantity()+"");
            params.put("MenuItemID",products.getPos_id()+"");



            myList.add(params);
        }

        Log.d("JORKE-res",myList.toString());*/

        Gson gson = new Gson();
        return gson.toJson(myList);
    }

    public String getListDetail(){
            ArrayList<Map<String, String>> myList=new ArrayList<>();

            int pos_id=0;
            Products products;

            for(Review row:listReviewTotal){
                products = app_db.productsDAO().getProductByPosId(row.getPos_id());
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

