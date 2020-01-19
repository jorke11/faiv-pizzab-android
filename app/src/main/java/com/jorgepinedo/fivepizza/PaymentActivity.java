package com.jorgepinedo.fivepizza;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.jorgepinedo.fivepizza.Adapters.ListMenuAdapterPayment;
import com.jorgepinedo.fivepizza.Adapters.ListMenuAdapterReview;
import com.jorgepinedo.fivepizza.Contents.OneFragment;
import com.jorgepinedo.fivepizza.Database.App;
import com.jorgepinedo.fivepizza.Models.Orders;
import com.jorgepinedo.fivepizza.Models.Review;
import com.jorgepinedo.fivepizza.Tools.Utils;

import java.util.ArrayList;
import java.util.List;

public class PaymentActivity extends AppCompatActivity  implements ListMenuAdapterPayment.EventCustomer{

    App app_db;
    RecyclerView recycler_review;
    ListMenuAdapterPayment listMenuAdapter;
    Button btn_pay;
    TextView tv_service,tv_total;

    float total = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        app_db = Utils.newInstanceDB(this);

        recycler_review = findViewById(R.id.recycler_review);
        btn_pay = findViewById(R.id.btn_pay);
        tv_service = findViewById(R.id.tv_service);
        tv_total = findViewById(R.id.tv_total);

        total = app_db.ordersDetailDAO().getTotal(new int[]{3});

        String total_formated=Utils.numberFormat(Math.round(total));

        tv_total.setText(total_formated);

        List<Review> listReviewMain,listReviewTotal,listReviewDrink;

        int[] state={3};

        listReviewMain = app_db.ordersDetailDAO().getReviewNotIn(7,state);

        listReviewDrink = app_db.ordersDetailDAO().getReviewIn(7,state);

        listReviewTotal = new ArrayList<>();

        listReviewTotal.addAll(Utils.joinAditionals(listReviewMain,app_db));
        listReviewTotal.addAll(listReviewDrink);

        final Orders orders = app_db.ordersDAO().getOrderCurrent();

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycler_review.setLayoutManager(linearLayoutManager);

        listMenuAdapter = new ListMenuAdapterPayment(listReviewTotal,R.layout.card_product_payment,this, (ListMenuAdapterPayment.EventCustomer) this,app_db);
        recycler_review.setAdapter(listMenuAdapter);

        btn_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final AlertDialog dialog;

                AlertDialog.Builder mBuilder = new AlertDialog.Builder(PaymentActivity.this);
                View mView=getLayoutInflater().inflate(R.layout.dialog_add_service,null);

                Button no_accept = mView.findViewById(R.id.btn_noaccept);
                Button accept = mView.findViewById(R.id.btn_accept);

                mBuilder.setView(mView);

                dialog = mBuilder.create();
                dialog.setTitle("");
                dialog.show();

                no_accept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        orders.setService(0);
                        app_db.ordersDAO().update(orders);
                        tv_service.setText("S0");

                        String total_formated = Utils.numberFormat(Math.round(total));
                        tv_total.setText(total_formated);
                        finishOrder(orders);

                    }
                });

                accept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();

                        float total_temp = total / 1.19f;
                        int service  = Math.round(total_temp * 0.10f);
                        int totallocal = Math.round(service + total);
                        orders.setService(total);
                        app_db.ordersDAO().update(orders);

                        String total_service = Utils.numberFormat(Math.round(service));
                        String total_formated = Utils.numberFormat(Math.round(totallocal));

                        tv_service.setText(total_service);
                        tv_total.setText(total_formated);
                        finishOrder(orders);
                    }
                });
            }
        });

    }

    private void finishOrder(final Orders orders){
        final AlertDialog dialog;
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(PaymentActivity.this);
        View mView=getLayoutInflater().inflate(R.layout.dialog_close_waiter,null);

        Button accept = mView.findViewById(R.id.btn_accept);

        mBuilder.setView(mView);

        int width = (int)(getResources().getDisplayMetrics().widthPixels*0.90);
        int height = (int)(getResources().getDisplayMetrics().heightPixels*0.90);

        dialog = mBuilder.create();
        dialog.getWindow().setLayout(width, height);
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
                Utils.setItem(PaymentActivity.this,"status","finalizado");
                Intent i = new Intent(PaymentActivity.this,LockActivity.class);
                Bundle b = new Bundle();
                b.putString("from","payment");
                i.putExtras(b);
                startActivity(i);
            }
        });
    }

    private void cleanImage() {
        Utils.setItem(this,"masa","");
        Utils.setItem(this,"queso","");
        Utils.setItem(this,"salsa","");
        Utils.setItem(this,"topping_1","");
        Utils.setItem(this,"topping_2","");
    }

    @Override
    public void onClickUpdateTotal() {

    }
}
