package com.jorgepinedo.fivepizza.Contents;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jorgepinedo.fivepizza.Database.App;
import com.jorgepinedo.fivepizza.MainActivity;
import com.jorgepinedo.fivepizza.Models.Orders;
import com.jorgepinedo.fivepizza.R;
import com.jorgepinedo.fivepizza.Tools.Utils;

public class OneFragment extends Fragment{

    App app_db;

    public OneFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        app_db = Utils.newInstanceDB(getActivity());
        View view = inflater.inflate(R.layout.fragment_one, container, false);
        CardView card_carry = view.findViewById(R.id.card_carry);
        CardView card_eat_here = view.findViewById(R.id.card_eat_here);

        final Orders orders = app_db.ordersDAO().getOrderCurrent();


        card_carry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orders.setType_id(3);
                app_db.ordersDAO().update(orders);

                ((MainActivity)getActivity()).hideInformation(false);
                ((MainActivity)getActivity()).chageFragment(new MasaFragment());
                ((MainActivity)getActivity()).enableBtnsTwo();
            }
        });

        card_eat_here.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orders.setType_id(2);
                app_db.ordersDAO().update(orders);

                ((MainActivity)getActivity()).hideInformation(false);
                ((MainActivity)getActivity()).chageFragment(new MasaFragment());
                ((MainActivity)getActivity()).enableBtnsTwo();
            }
        });


        ((MainActivity)getActivity()).enableBtnsOne();

        ((MainActivity)getActivity()).hideInformation(true);

        return view;
    }


}