package com.jorgepinedo.fivepizza.Adapters;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jorgepinedo.fivepizza.Database.App;
import com.jorgepinedo.fivepizza.Models.Orders;
import com.jorgepinedo.fivepizza.Models.OrdersDetail;
import com.jorgepinedo.fivepizza.Models.Products;
import com.jorgepinedo.fivepizza.R;
import com.jorgepinedo.fivepizza.Tools.Utils;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ListMenuAdapterCheese extends RecyclerView.Adapter<ListMenuAdapterCheese.MenuViewHolder> {

    private static final String TAG = "CardAdapterRecicleView";
    private List<Products> listItem,filterList;
    private int resource;
    private Activity activity;
    View view;
    App app_db;
    private OnDragListener onDragListener;

    public ListMenuAdapterCheese(List<Products> listItem, int resource, Activity activity, OnDragListener onDragListener, App app_db) {
        this.listItem = listItem;
        this.resource = resource;
        this.activity = activity;
        this.filterList = new ArrayList<Products>();
        this.filterList.addAll(this.listItem);
        this.onDragListener = onDragListener;
        this.app_db = app_db;
    }

    @NonNull
    @Override
    public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        view = LayoutInflater.from(viewGroup.getContext()).inflate(resource, viewGroup, false);

        return new MenuViewHolder(view,onDragListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuViewHolder holder, int i) {
        final Products row = filterList.get(i);

        //DecimalFormat formatter = new DecimalFormat("###,###,##0.00");

        String price = Utils.numberFormat(Math.ceil(row.getPrice()*1.19));

        //String title = (row.getTitle().length()>13)?row.getTitle().substring(0,13)+"..":row.getTitle();

        //holder.title.setText(title);
        holder.title.setText(row.getTitle());
        holder.price.setText("$"+price);


        int id = activity.getResources().getIdentifier("com.jorgepinedo.fivepizza:drawable/mini_" + row.getUrl(), null, null);
        holder.imageView.setImageResource(id);

        OrdersDetail detail = app_db.ordersDetailDAO().getOrdersByProductId(row.getId());

        if(detail != null){
            //holder.title.setTypeface(null, Typeface.BOLD);
            id = activity.getResources().getIdentifier("com.jorgepinedo.fivepizza:drawable/checked", null, null);
            holder.imageView.setImageResource(id);
        }

        /*holder.imageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                onDragListener.onDragLongClick(v,row);
                return true;
            }
        });*/

        holder.card_complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                OrdersDetail det = app_db.ordersDetailDAO().getOrdersByProductId(row.getId());
                Products products = app_db.productsDAO().getProductById(row.getId());
                Orders orders = app_db.ordersDAO().getOrderCurrent();

                OrdersDetail parent = app_db.ordersDetailDAO().getParent();

                app_db.ordersDetailDAO().deleteByCategory(products.getCategory_id(),parent.getId());

                String url="";


                if(det != null){
                    url="";
                }else{
                    app_db.ordersDetailDAO().insertAll(new OrdersDetail(orders.getId(),row.getId(),parent.getId()));
                    url = row.getUrl();
                }

                notifyDataSetChanged();

                onDragListener.onClick(v,row,url);
            }
        });


    }


    @Override
    public int getItemCount() {
        return filterList.size();
    }

    public class MenuViewHolder extends RecyclerView.ViewHolder{

        private TextView title,price;
        private CardView card_complete;
        OnDragListener onDragListener;
        CircleImageView imageView;

        public MenuViewHolder(View itemView, OnDragListener onDragListener) {
            super(itemView);

            title = itemView.findViewById(R.id.title);
            price = itemView.findViewById(R.id.price);
            imageView = itemView.findViewById(R.id.imageView);
            card_complete = itemView.findViewById(R.id.card_complete);
            this.onDragListener = onDragListener;

            //imageView.setOnLongClickListener(this);

        }
    }

    public interface OnDragListener{
        void onDragLongClick(View view, Products products);
        void onClick(View view, Products products,String url);

    }
}
