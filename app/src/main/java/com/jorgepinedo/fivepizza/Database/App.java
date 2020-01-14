package com.jorgepinedo.fivepizza.Database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.jorgepinedo.fivepizza.Dao.CategoriesDAO;
import com.jorgepinedo.fivepizza.Dao.OrdersDAO;
import com.jorgepinedo.fivepizza.Dao.OrdersDetailDAO;
import com.jorgepinedo.fivepizza.Dao.ProductsDAO;
import com.jorgepinedo.fivepizza.Models.Categories;
import com.jorgepinedo.fivepizza.Models.Orders;
import com.jorgepinedo.fivepizza.Models.OrdersDetail;
import com.jorgepinedo.fivepizza.Models.Products;

@Database(entities = {
        Categories.class,
        Products.class,
        Orders.class,
        OrdersDetail.class
},version = 1)
public abstract class App extends RoomDatabase {
    public abstract CategoriesDAO categoriesDAO();
    public abstract ProductsDAO productsDAO();
    public abstract OrdersDAO ordersDAO();
    public abstract OrdersDetailDAO ordersDetailDAO();
}