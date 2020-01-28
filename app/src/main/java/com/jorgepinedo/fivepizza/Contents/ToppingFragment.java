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
import android.widget.ImageView;
import android.widget.Toast;

import com.jorgepinedo.fivepizza.Adapters.ListMenuAdapter;
import com.jorgepinedo.fivepizza.Adapters.ListMenuAdapterTopping;
import com.jorgepinedo.fivepizza.Database.App;
import com.jorgepinedo.fivepizza.MainActivity;
import com.jorgepinedo.fivepizza.Models.Ingredients;
import com.jorgepinedo.fivepizza.Models.Orders;
import com.jorgepinedo.fivepizza.Models.OrdersDetail;
import com.jorgepinedo.fivepizza.Models.Products;
import com.jorgepinedo.fivepizza.R;
import com.jorgepinedo.fivepizza.Tools.Utils;

import java.util.List;


public class ToppingFragment extends Fragment  implements ListMenuAdapterTopping.OnDragListener{

    RecyclerView recycler_favorite,recycler_protein,recycler_vegetable;
    ListMenuAdapterTopping listMenuAdapterfav,listMenuAdapterVeg,listMenuAdapterPro;
    ImageView image_table,image_massa,image_salsa,image_queso,image_topping,image_topping2,image_step;

    List<Products> favorite,protein,vegetable;
    Products current_product;
    App app_db;

    String topping_1,topping_2;

    public ToppingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_topping, container, false);

        app_db = Utils.newInstanceDB(getActivity());
        favorite = app_db.productsDAO().getAllProductsCategory(new int[]{4});
        protein = app_db.productsDAO().getAllProductsCategory(new int[]{5});
        vegetable = app_db.productsDAO().getAllProductsCategory(new int[]{6});

        recycler_favorite = view.findViewById(R.id.recycler_favorite);
        recycler_protein = view.findViewById(R.id.recycler_protein);
        recycler_vegetable = view.findViewById(R.id.recycler_vegetable);

        image_table = view.findViewById(R.id.image_table);
        image_massa = view.findViewById(R.id.image_massa);
        image_salsa = view.findViewById(R.id.image_salsa);
        image_queso = view.findViewById(R.id.image_queso);
        image_topping = view.findViewById(R.id.image_topping);
        image_topping2 = view.findViewById(R.id.image_topping2);
        image_step = view.findViewById(R.id.image_step);

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recycler_favorite.setLayoutManager(linearLayoutManager);

        listMenuAdapterfav = new ListMenuAdapterTopping(favorite,R.layout.card_product_item_altern,getActivity(), (ListMenuAdapterTopping.OnDragListener) this,app_db);
        recycler_favorite.setAdapter(listMenuAdapterfav);

        /*Utils.setItem(getActivity(),"topping_1","");
        Utils.setItem(getActivity(),"topping_2","");*/


        LinearLayoutManager linearLayoutManagerFavorite=new LinearLayoutManager(getActivity());
        linearLayoutManagerFavorite.setOrientation(LinearLayoutManager.HORIZONTAL);
        recycler_protein.setLayoutManager(linearLayoutManagerFavorite);

        listMenuAdapterPro = new ListMenuAdapterTopping(protein,R.layout.card_product_item_altern,getActivity(), (ListMenuAdapterTopping.OnDragListener) this,app_db);
        recycler_protein.setAdapter(listMenuAdapterPro);

        LinearLayoutManager linearLayoutManagerVegetable=new LinearLayoutManager(getActivity());
        linearLayoutManagerVegetable.setOrientation(LinearLayoutManager.HORIZONTAL);
        recycler_vegetable.setLayoutManager(linearLayoutManagerVegetable);

        listMenuAdapterVeg = new ListMenuAdapterTopping(vegetable,R.layout.card_product_item_altern,getActivity(), (ListMenuAdapterTopping.OnDragListener) this,app_db);
        recycler_vegetable.setAdapter(listMenuAdapterVeg);

        image_table.setOnDragListener(dragListener);

        String masa = Utils.getItem(getActivity(),"masa");
        String salsa= Utils.getItem(getActivity(),"salsa");
        String queso= Utils.getItem(getActivity(),"queso");
        topping_1 = Utils.getItem(getActivity(),"topping_1");
        topping_2 = Utils.getItem(getActivity(),"topping_2");


        int id;

        id = getActivity().getResources().getIdentifier("com.jorgepinedo.fivepizza:drawable/primer_topping", null, null);
        image_step.setImageResource(id);


        List<Ingredients> ingredients= app_db.ordersDetailDAO().getcategoryExists(new int[]{1,2,3});

        for (Ingredients row:ingredients){
            if(row.getCategory_id() == 1){
                id = getActivity().getResources().getIdentifier("com.jorgepinedo.fivepizza:drawable/" + row.getUrl(), null, null);
                image_massa.setImageResource(id);
            }else if(row.getCategory_id() == 2){
                id = getActivity().getResources().getIdentifier("com.jorgepinedo.fivepizza:drawable/" + row.getUrl(), null, null);
                image_queso.setImageResource(id);
            }else if(row.getCategory_id() == 3){
                id = getActivity().getResources().getIdentifier("com.jorgepinedo.fivepizza:drawable/" + row.getUrl(), null, null);
                image_salsa.setImageResource(id);
            }
        }

        List<Ingredients> toppings = app_db.ordersDetailDAO().getcategoryExists(new int[]{4,5,6});

        if(toppings.size()>0){
            if(toppings.size() == 1){
                id = getActivity().getResources().getIdentifier("com.jorgepinedo.fivepizza:drawable/segundo_topping", null, null);
                image_step.setImageResource(id);
                id = getActivity().getResources().getIdentifier("com.jorgepinedo.fivepizza:drawable/"+toppings.get(0).getUrl(), null, null);
                image_topping.setImageResource(id);
                image_topping2.setImageDrawable(null);
            }

            if(toppings.size() == 2){
                id = getActivity().getResources().getIdentifier("com.jorgepinedo.fivepizza:drawable/segundo_topping", null, null);
                image_step.setImageResource(id);

                id = getActivity().getResources().getIdentifier("com.jorgepinedo.fivepizza:drawable/"+toppings.get(0).getUrl(), null, null);
                image_topping.setImageResource(id);

                int id2 = getActivity().getResources().getIdentifier("com.jorgepinedo.fivepizza:drawable/"+toppings.get(1).getUrl(), null, null);
                image_topping2.setImageResource(id2);

                if(toppings.get(0).getPriority() > toppings.get(1).getPriority()){
                    image_topping.setImageResource(id2);
                    image_topping2.setImageResource(id);
                }
            }

        }else{
            Log.d("JORKE","else");
            id = getActivity().getResources().getIdentifier("com.jorgepinedo.fivepizza:drawable/primer_topping", null, null);
            image_step.setImageResource(id);
            image_topping.setImageDrawable(null);
            image_topping2.setImageDrawable(null);
        }

        ((MainActivity)getActivity()).hideInformation(false);

        return view;
    }

    @Override
    public void onDragLongClick(View view,Products products) {
        Log.d("JORKE","asd");
        current_product=products;
        ClipData clipData = ClipData.newPlainText("","");
        View.DragShadowBuilder dragShadowBuilder=new View.DragShadowBuilder(view);
        view.startDrag(clipData,dragShadowBuilder,view,0);
    }


    @Override
    public void onClick(int id,int total) {

        Log.d("JORKE-total",total+"");
        Log.d("JORKE-id",id+"");

        List<Ingredients> ingredients = app_db.ordersDetailDAO().getcategoryExists(new int[]{4,5,6});

        if(ingredients.size() > 0){
            if(ingredients.size()==1){
                id = getActivity().getResources().getIdentifier("com.jorgepinedo.fivepizza:drawable/segundo_topping", null, null);
                image_step.setImageResource(id);
                id = getActivity().getResources().getIdentifier("com.jorgepinedo.fivepizza:drawable/"+ingredients.get(0).getUrl(), null, null);
                image_topping.setImageResource(id);
                image_topping2.setImageDrawable(null);
            }

            if(ingredients.size() == 2){
                id = getActivity().getResources().getIdentifier("com.jorgepinedo.fivepizza:drawable/"+ingredients.get(1).getUrl(), null, null);
                image_topping2.setImageResource(id);

                final Fragment fragment =new DrinkFragment();

                ((MainActivity) getActivity()).chageFragment(fragment);
                ((MainActivity) getActivity()).enableBtnsSix();
            }

        }else{
            id = getActivity().getResources().getIdentifier("com.jorgepinedo.fivepizza:drawable/primer_topping", null, null);
            image_step.setImageResource(id);
            image_topping.setImageDrawable(null);
            image_topping2.setImageDrawable(null);
        }


        listMenuAdapterfav.notifyDataSetChanged();
        listMenuAdapterPro.notifyDataSetChanged();
        listMenuAdapterVeg.notifyDataSetChanged();

        ((MainActivity)getActivity()).loadTotal();
    }

    View.OnDragListener dragListener=new View.OnDragListener(){

        @Override
        public boolean onDrag(View v, DragEvent event) {

            int dragAction = event.getAction();

            final View view = (View) event.getLocalState();

            switch (dragAction){
                case DragEvent.ACTION_DRAG_ENTERED:
                    Log.d("JORKE","ACTION_DRAG_ENTERED");
                    break;

                case DragEvent.ACTION_DRAG_EXITED:
                    Log.d("JORKE","ACTION_DRAG_EXITED");
                    break;

                case DragEvent.ACTION_DROP:

                    /*((MainActivity)getActivity()).chageFragment(new DrinkFragment());
                    ((MainActivity)getActivity()).enableBtnsSix();*/


                  /*  int total = app_db.ordersDetailDAO().categoryExists(4,5,6);

                    Log.d("JORKE-total",total+"");

                    if(total < 2){

                        OrdersDetail row = app_db.ordersDetailDAO().getOrdersByProductId(current_product.getId());

                        int id;

                        if(row == null){
                            app_db.ordersDetailDAO().insertAll(new OrdersDetail(1,current_product.getId(),1));
                            if(total == 0){
                                Utils.setItem(getActivity(),"topping_1",current_product.getUrl());
                                id = getActivity().getResources().getIdentifier("com.jorgepinedo.fivepizza:drawable/" + current_product.getUrl(), null, null);
                                image_topping.setImageResource(id);
                            }else if(total==1){
                                Utils.setItem(getActivity(),"topping_2",current_product.getUrl());
                                id = getActivity().getResources().getIdentifier("com.jorgepinedo.fivepizza:drawable/" + current_product.getUrl(), null, null);
                                image_topping2.setImageResource(id);
                            }else{
                                Log.d("JORKE","exceso "+total);
                            }

                        }else{
                            Log.d("JORKE","else");
                        }

                        //final List<OrdersDetail> list = app_db.ordersDetailDAO().getOrdersByCategories("4,5,6");


                    }else{
                        Toast.makeText(getActivity(),"Solo puedes dos Toppings!",Toast.LENGTH_LONG).show();
                    }


                    listMenuAdapterfav.notifyDataSetChanged();
                    listMenuAdapterPro.notifyDataSetChanged();
                    listMenuAdapterVeg.notifyDataSetChanged();

                    ((MainActivity)getActivity()).loadTotal();
*/

                    break;
            }

            return true;
        }
    };



    /*@Override
    public void onClick(View view, Products products) {
        current_product=products;

        int total = app_db.ordersDetailDAO().categoryExists(4,5,6);

        if(total < 2){
            final OrdersDetail parent = app_db.ordersDetailDAO().getParent();
            OrdersDetail row = app_db.ordersDetailDAO().getOrdersByProductId(current_product.getId());

            int id;

            if(row == null){

                Orders orders = app_db.ordersDAO().getOrderCurrent();
                app_db.ordersDetailDAO().insertAll(new OrdersDetail(orders.getId(),current_product.getId(),parent.getId()));
                if(total == 0){
                    Utils.setItem(getActivity(),"topping_1",current_product.getUrl());
                    id = getActivity().getResources().getIdentifier("com.jorgepinedo.fivepizza:drawable/" + current_product.getUrl(), null, null);
                    image_topping.setImageResource(id);
                }else if(total==1){
                    Utils.setItem(getActivity(),"topping_2",current_product.getUrl());
                    id = getActivity().getResources().getIdentifier("com.jorgepinedo.fivepizza:drawable/" + current_product.getUrl(), null, null);
                    image_topping2.setImageResource(id);
                }else{
                    Log.d("JORKE","exceso "+total);
                }

            }else{
                Log.d("JORKE","else");
            }

        }else{
            Toast.makeText(getActivity(),"Solo puedes dos Toppings!",Toast.LENGTH_LONG).show();
        }


        listMenuAdapterfav.notifyDataSetChanged();
        listMenuAdapterPro.notifyDataSetChanged();
        listMenuAdapterVeg.notifyDataSetChanged();

        ((MainActivity)getActivity()).loadTotal();
    }*/
}
