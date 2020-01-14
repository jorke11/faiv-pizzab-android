package com.jorgepinedo.fivepizza.Dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.jorgepinedo.fivepizza.Models.Categories;
import com.jorgepinedo.fivepizza.Models.Orders;

import java.util.List;

@Dao
public interface OrdersDAO {

    @Query("SELECT * from orders")
    List<Orders> getAllOrders();

    @Query("SELECT * from orders WHERE status_id=1 and id= :id")
    Orders getOrder(int id);

    @Query("SELECT * from orders WHERE status_id=1")
    Orders getOrderCurrent();

    @Insert
    void insert(Orders... orders);

    @Update
    void update(Orders... orders);

    @Query("DELETE FROM orders")
    void deleteAll();
}
