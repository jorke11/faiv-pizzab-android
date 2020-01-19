package com.jorgepinedo.fivepizza.Contents;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.jorgepinedo.fivepizza.Adapters.ListMenuAdapter;
import com.jorgepinedo.fivepizza.Adapters.ListMenuAdapterCheese;
import com.jorgepinedo.fivepizza.Database.App;
import com.jorgepinedo.fivepizza.MainActivity;
import com.jorgepinedo.fivepizza.Models.Orders;
import com.jorgepinedo.fivepizza.Models.OrdersDetail;
import com.jorgepinedo.fivepizza.Models.Products;
import com.jorgepinedo.fivepizza.R;
import com.jorgepinedo.fivepizza.Tools.Utils;

import java.util.List;


public class QuesoFragment extends Fragment implements ListMenuAdapterCheese.OnDragListener{

    RecyclerView recycler_products;
    ListMenuAdapterCheese listMenuAdapter;
    ImageView image_table,image_massa,image_salsa;
    List<Products> list;
    App app_db;
    Products current_product;

    public QuesoFragment() {
        // Required empty public constructor
    }


    /**
     * Menu QUESO
     */

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cheese, container, false);

        app_db = Utils.newInstanceDB(getActivity());
        list = app_db.productsDAO().getAllProductsCategory(new int[]{2});


        recycler_products = view.findViewById(R.id.recycler_products);
        image_table = view.findViewById(R.id.image_table);
        image_massa = view.findViewById(R.id.image_massa);
        image_salsa = view.findViewById(R.id.image_salsa);

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycler_products.setLayoutManager(linearLayoutManager);

        listMenuAdapter = new ListMenuAdapterCheese(list,R.layout.card_product_item,getActivity(), (ListMenuAdapterCheese.OnDragListener) this,app_db);
        recycler_products.setAdapter(listMenuAdapter);

        image_table.setOnDragListener(dragListener);

        String masa = Utils.getItem(getActivity(),"masa");
        String salsa= Utils.getItem(getActivity(),"salsa");

        int id;

        if(!masa.isEmpty()){
            id = getActivity().getResources().getIdentifier("com.jorgepinedo.fivepizza:drawable/" + masa, null, null);
            image_massa.setImageResource(id);
        }

        if(!salsa.isEmpty()){
            id = getActivity().getResources().getIdentifier("com.jorgepinedo.fivepizza:drawable/" + salsa, null, null);
            image_salsa.setImageResource(id);
        }

        ((MainActivity)getActivity()).hideInformation(false);

        return view;
    }

    @Override
    public void onDragLongClick(View view, Products products) {
        current_product=products;
        ClipData clipData = ClipData.newPlainText("","");
        View.DragShadowBuilder dragShadowBuilder=new View.DragShadowBuilder(view);
        view.startDrag(clipData,dragShadowBuilder,view,0);
    }



    @Override
    public void onClick(View view, Products products,String url) {
        Utils.setItem(getActivity(),"queso",url);
        forwardFragment();
        ((MainActivity)getActivity()).loadTotal();
    }


    public void forwardFragment(){
        final Fragment fragment = new ToppingFragment();
        ((MainActivity)getActivity()).chageFragment(fragment);
        ((MainActivity)getActivity()).enableBtnsFive();

    }

    View.OnDragListener dragListener=new View.OnDragListener(){

        @Override
        public boolean onDrag(View v, DragEvent event) {

            int dragAction = event.getAction();

            final View view= (View) event.getLocalState();

            switch (dragAction){
                case DragEvent.ACTION_DRAG_ENTERED:
                    Log.d("JORKE","ACTION_DRAG_ENTERED");
                    break;

                case DragEvent.ACTION_DRAG_EXITED:
                    Log.d("JORKE","ACTION_DRAG_EXITED");
                    break;

                case DragEvent.ACTION_DROP:

                 /*   final Fragment fragment =new ToppingFragment();
                    final OrdersDetail row=app_db.ordersDetailDAO().getOrdersByCategoryId(current_product.getCategory_id());

                    if(row != null){
                        if(row.getProduct_id()==current_product.getId()){
                            Toast.makeText(getActivity(),"Ya tienes la "+current_product.getTitle()+" seccionada!",Toast.LENGTH_LONG).show();
                        }else {
                            Products old_product = app_db.productsDAO().getProductById(row.getProduct_id());
                            AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                            alert.setTitle("Cambio de producto");
                            alert.setMessage("Seleccionado: (" + current_product.getTitle() + ")\nA Reemplazar : (" + old_product.getTitle() + ")\n¿Estas seguro de cambiarlo?");
                            alert.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    ((MainActivity) getActivity()).chageFragment(fragment);
                                    ((MainActivity) getActivity()).enableBtnsFive();
                                    Utils.setItem(getActivity(),"queso",current_product.getUrl());
                                    app_db.ordersDetailDAO().updateQuantity(row.getId(), current_product.getId());
                                }
                            });
                            alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    ((MainActivity) getActivity()).chageFragment(fragment);
                                    ((MainActivity) getActivity()).enableBtnsFive();
                                }
                            });

                            alert.create().show();
                        }
                    }else{
                        ((MainActivity)getActivity()).chageFragment(fragment);
                        ((MainActivity)getActivity()).enableBtnsFive();
                        app_db.ordersDetailDAO().insertAll(new OrdersDetail(1,current_product.getId(),1));
                        Utils.setItem(getActivity(),"queso",current_product.getUrl());
                    }


                    ((MainActivity)getActivity()).loadTotal();*/

                    break;
            }

            return true;
        }
    };
}
