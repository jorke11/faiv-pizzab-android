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
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.jorgepinedo.fivepizza.Adapters.ListMenuAdapterReview;
import com.jorgepinedo.fivepizza.Adapters.ListMenuDrinkAdapter;
import com.jorgepinedo.fivepizza.Database.App;
import com.jorgepinedo.fivepizza.FinishActivity;
import com.jorgepinedo.fivepizza.LockActivity;
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


<<<<<<< HEAD
        validateTotal();
=======
        int total = app_db.ordersDetailDAO().getTotal(new int[]{1});

        if(total==0){
            Toast.makeText(getActivity(),"No tienes productos Seleccionados",Toast.LENGTH_SHORT).show();
            final Fragment fragment = new MasaFragment();
            ((MainActivity)getActivity()).chageFragment(fragment);
            ((MainActivity)getActivity()).enableBtnsTwo();
        }

        Log.d("JORKE",total+" a");

>>>>>>> Kiosko

        View view = inflater.inflate(R.layout.fragment_review, container, false);

        IP = Utils.getItem(getActivity(),"IP_SERVER");

        int[] status={1,2};

        Orders orders=app_db.ordersDAO().getOrderCurrent();

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
<<<<<<< HEAD
        recycler_reviewed = view.findViewById(R.id.recycler_reviewed);
=======
>>>>>>> Kiosko

        payment = view.findViewById(R.id.payment);
        other_pizza = view.findViewById(R.id.other_pizza);
        spinner = view.findViewById(R.id.progressBar);


        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycler_review.setLayoutManager(linearLayoutManager);

        listMenuAdapter = new ListMenuAdapterReview(listReviewTotal,R.layout.card_product_review,getActivity(), (ListMenuAdapterReview.EventCustomer) this,app_db);
        recycler_review.setAdapter(listMenuAdapter);


       /* LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(getActivity());
        linearLayoutManager2.setOrientation(LinearLayoutManager.VERTICAL);
        recycler_reviewed.setLayoutManager(linearLayoutManager2);

        listedMenuAdapter = new ListMenuAdapterReview(listedReviewTotal,R.layout.card_product_review,getActivity(), (ListMenuAdapterReview.EventCustomer) this,app_db);
        recycler_reviewed.setAdapter(listedMenuAdapter);*/


        payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateTotal()){
                    showCookie();
                    //createOrder();
                }
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

    public boolean validateTotal(){
        int total = app_db.ordersDetailDAO().getTotal(new int[]{1});

        if(total == 0){
            Toast.makeText(getActivity(),"No tienes productos Seleccionados",Toast.LENGTH_SHORT).show();
            final Fragment fragment = new MasaFragment();
            ((MainActivity)getActivity()).chageFragment(fragment);
            ((MainActivity)getActivity()).enableBtnsTwo();
            return false;
        }else{
            return true;
        }
    }

<<<<<<< HEAD

    public void confirmOrder(){

=======
>>>>>>> Kiosko
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
                dialog.dismiss();
                showCookie();
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    public void generarTicket(int order_id){


        final Orders orders = app_db.ordersDAO().getOrderCurrent();

        final AlertDialog dialog;
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
        View mView=getLayoutInflater().inflate(R.layout.dialog_finish_order,null);

        Button accept = mView.findViewById(R.id.btn_accept);
        TextView number_order = mView.findViewById(R.id.number_order);
        number_order.setText(order_id+"");

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
                dialog.dismiss();
                orders.setStatus_id(2);
                app_db.ordersDAO().update(orders);
                app_db.ordersDetailDAO().updateFinishOrder(orders.getId());
                cleanImage();
                Utils.setItem(getActivity(),"status","finalizado");

                startActivity(new Intent(getActivity(),MainActivity.class));
            }
        });

    }
    private void cleanImage() {
        Utils.setItem(getActivity(),"masa","");
        Utils.setItem(getActivity(),"queso","");
        Utils.setItem(getActivity(),"salsa","");
        Utils.setItem(getActivity(),"topping_1","");
        Utils.setItem(getActivity(),"topping_2","");
    }


    public void showCookie(){

        final AlertDialog dialog;
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
        final View mView=getLayoutInflater().inflate(R.layout.dialog_cookie,null);

        Button accept = mView.findViewById(R.id.btn_accept);
        Button btn_cancel = mView.findViewById(R.id.btn_cancel);

        Button btn_plus = mView.findViewById(R.id.btn_plus);
        Button btn_minus = mView.findViewById(R.id.btn_minus);

        final TextView tv_total_item = mView.findViewById(R.id.tv_total_item);

        btn_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int total = 1;
                total+=Integer.parseInt((String) tv_total_item.getText());
                tv_total_item.setText(total+"");
            }
        });

        btn_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int total=0;
                total = Integer.parseInt((String) tv_total_item.getText());
                total--;
                if(total >= 0){
                    tv_total_item.setText(total+"");
                }
            }
        });


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
                dialog.dismiss();
                int quantity = Integer.parseInt((String) tv_total_item.getText());

                if(quantity > 0){
                    List<Products> cookie=app_db.productsDAO().getAllProductsCategory(new int[]{8});
                    Orders orders = app_db.ordersDAO().getOrderCurrent();
                    app_db.ordersDetailDAO().insertAll(new OrdersDetail(orders.getId(),cookie.get(0).getId(),0));
                    OrdersDetail row = app_db.ordersDetailDAO().getOrdersByProductId(cookie.get(0).getId());
                    app_db.ordersDetailDAO().updateQuantity(row.getId(), quantity);
                }

                createOrder();
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                createOrder();
            }
        });
    }

<<<<<<< HEAD
    public void showCookie(){

        final AlertDialog dialog;
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
        final View mView=getLayoutInflater().inflate(R.layout.dialog_cookie,null);

        Button accept = mView.findViewById(R.id.btn_accept);
        Button btn_cancel = mView.findViewById(R.id.btn_cancel);

        Button btn_plus = mView.findViewById(R.id.btn_plus);
        Button btn_minus = mView.findViewById(R.id.btn_minus);

        final TextView tv_total_item = mView.findViewById(R.id.tv_total_item);

        btn_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int total = 1;
                total+=Integer.parseInt((String) tv_total_item.getText());
                tv_total_item.setText(total+"");
            }
        });

        btn_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int total=0;
                total = Integer.parseInt((String) tv_total_item.getText());
                total--;
                if(total >= 0){
                    tv_total_item.setText(total+"");
                }
            }
        });


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
                dialog.dismiss();
                int quantity = Integer.parseInt((String) tv_total_item.getText());

                if(quantity > 0){
                    List<Products> cookie=app_db.productsDAO().getAllProductsCategory(new int[]{8});
                    Orders orders = app_db.ordersDAO().getOrderCurrent();
                    app_db.ordersDetailDAO().insertAll(new OrdersDetail(orders.getId(),cookie.get(0).getId(),0));
                    OrdersDetail row = app_db.ordersDetailDAO().getOrdersByProductId(cookie.get(0).getId());
                    app_db.ordersDetailDAO().updateQuantity(row.getId(), quantity);
                }

                createOrder();
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                createOrder();
            }
        });
    }
=======
>>>>>>> Kiosko

    public void createOrder(){
        Log.d("JORKE","createOrder");
        other_pizza.setEnabled(false);
        payment.setVisibility(View.GONE);
        spinner.setVisibility(View.VISIBLE);

        String url=IP+"/api/orders";


        final Orders orders = app_db.ordersDAO().getOrderCurrent();


        if(Utils.getItem(getActivity(),"PRINTER").equals("false")){
            Log.d("JORKE","Without printer");
            app_db.ordersDetailDAO().printedOrder(orders.getId());
            startActivity(new Intent(getActivity(), FinishActivity.class));
        }else{
            Log.d("JORKE",url);
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
                        generarTicket(Integer.parseInt(obj.getString("OrderID")));
                        //startActivity(new Intent(getActivity(), FinishActivity.class));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("JORKE-eer",error.getMessage()+"");
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

            RetryPolicy mRetryPolicy = new DefaultRetryPolicy(
                    0,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);


            stringRequest.setRetryPolicy(mRetryPolicy);

<<<<<<< HEAD
            requestQueue = Volley.newRequestQueue(getContext());
=======
            requestQueue= Volley.newRequestQueue(getContext());
>>>>>>> Kiosko
            requestQueue.add(stringRequest);
        }
    }


    public String getOrderHeader(){
        Log.d("JORKE","getOrderHeader");
        Orders orders = app_db.ordersDAO().getOrderCurrent();
        Log.d("JORKE",orders.toString());
        HashMap<String, String> params = new HashMap<>();
        //params.put("DineInTableID",Utils.getItem(getActivity(),"TABLE"));
        params.put("StationID","1");
        params.put("OrderStatus","1");
        params.put("OrderType","2");
        //params.put("OrderType",orders.getType_id()+"");
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
        Log.d("JORKE","getListDetailFormated");
        ArrayList<Map<String, String>> myList=new ArrayList<>();

        Products products;

        List<OrdersDetail> masas_list = app_db.ordersDetailDAO().getOrdersByCategories(new int[]{1,7,8},new int[]{1,2});

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
