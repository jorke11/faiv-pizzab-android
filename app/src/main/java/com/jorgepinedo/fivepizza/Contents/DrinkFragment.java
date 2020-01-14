package com.jorgepinedo.fivepizza.Contents;

import android.content.ClipData;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.jorgepinedo.fivepizza.Adapters.ListMenuDrinkAdapter;
import com.jorgepinedo.fivepizza.Database.App;
import com.jorgepinedo.fivepizza.MainActivity;
import com.jorgepinedo.fivepizza.Models.Orders;
import com.jorgepinedo.fivepizza.Models.OrdersDetail;
import com.jorgepinedo.fivepizza.Models.Products;
import com.jorgepinedo.fivepizza.R;
import com.jorgepinedo.fivepizza.Tools.Utils;

import java.util.List;


public class DrinkFragment extends Fragment implements ListMenuDrinkAdapter.eventCustom{


    RecyclerView recycler_products;
    ListMenuDrinkAdapter listMenuAdapter;
    ImageView image_table,image_massa,image_salsa,image_queso,image_topping,image_topping2;

    List<Products> list;
    App app_db;
    Button btn_next;
    String topping_1,topping_2;


    public DrinkFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_drink, container, false);
        app_db = Utils.newInstanceDB(getActivity());
        list = app_db.productsDAO().getAllProductsCategory(7);
        recycler_products = view.findViewById(R.id.recycler_products);
        btn_next = view.findViewById(R.id.btn_next);
        image_table = view.findViewById(R.id.image_table);
        image_massa = view.findViewById(R.id.image_massa);
        image_salsa = view.findViewById(R.id.image_salsa);
        image_queso = view.findViewById(R.id.image_queso);
        image_topping = view.findViewById(R.id.image_topping);
        image_topping2 = view.findViewById(R.id.image_topping2);

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycler_products.setLayoutManager(linearLayoutManager);

        listMenuAdapter = new ListMenuDrinkAdapter(list,R.layout.card_product_drink,getActivity(), (ListMenuDrinkAdapter.eventCustom) this,app_db);
        recycler_products.setAdapter(listMenuAdapter);


        String masa = Utils.getItem(getActivity(),"masa");
        String salsa= Utils.getItem(getActivity(),"salsa");
        String queso= Utils.getItem(getActivity(),"queso");
        topping_1 = Utils.getItem(getActivity(),"topping_1");
        topping_2 = Utils.getItem(getActivity(),"topping_2");

        int id;

        if(!masa.isEmpty()){
            id = getActivity().getResources().getIdentifier("com.jorgepinedo.fivepizza:drawable/" + masa, null, null);
            image_massa.setImageResource(id);
        }

        if(!salsa.isEmpty()){
            id = getActivity().getResources().getIdentifier("com.jorgepinedo.fivepizza:drawable/" + salsa, null, null);
            image_salsa.setImageResource(id);
        }

        if(!queso.isEmpty()){
            id = getActivity().getResources().getIdentifier("com.jorgepinedo.fivepizza:drawable/" + queso, null, null);
            image_queso.setImageResource(id);
        }


        if(!topping_1.isEmpty()){
            id = getActivity().getResources().getIdentifier("com.jorgepinedo.fivepizza:drawable/" + topping_1, null, null);
            image_topping.setImageResource(id);
        }

        if(!topping_2.isEmpty()){
            id = getActivity().getResources().getIdentifier("com.jorgepinedo.fivepizza:drawable/" + topping_2, null, null);
            image_topping2.setImageResource(id);
        }

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).chageFragment(new ReviewFragment());
                ((MainActivity)getActivity()).enableBtnReview();
            }
        });

        ((MainActivity)getActivity()).hideInformation(true);


        return view;
    }

    @Override
    public void onClick(Products products,boolean type_event) {

        OrdersDetail row = app_db.ordersDetailDAO().getOrdersByProductId(products.getId());

        if(type_event) {
            if (row == null) {
                Orders orders=app_db.ordersDAO().getOrderCurrent();
                app_db.ordersDetailDAO().insertAll(new OrdersDetail(orders.getId(), products.getId(), 0));
            } else {
                app_db.ordersDetailDAO().updateQuantity(row.getId(), row.getQuantity() + 1);
            }
        }else{

            int total = row.getQuantity() - 1;
            Log.d("JORKE",total+"");
            if(total == 0){
                app_db.ordersDetailDAO().delete(row);
            }else{
                app_db.ordersDetailDAO().updateQuantity(row.getId(), total);
            }
        }

        listMenuAdapter.notifyDataSetChanged();
        ((MainActivity)getActivity()).loadTotal();

    }
}
