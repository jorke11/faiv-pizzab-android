package com.jorgepinedo.fivepizza.Dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.jorgepinedo.fivepizza.Models.Categories;
import com.jorgepinedo.fivepizza.Models.Orders;
import com.jorgepinedo.fivepizza.Models.OrdersDetail;
import com.jorgepinedo.fivepizza.Models.Review;

import java.util.List;

@Dao
public interface OrdersDetailDAO {

    @Query("SELECT * from OrdersDetail")
    List<OrdersDetail> getAllOrdersDetail();

    @Query("SELECT * from OrdersDetail where status_id=:status_id")
    List<OrdersDetail> getAllOrdersDetail(int status_id);

    @Query("SELECT * from OrdersDetail WHERE id= :id")
    OrdersDetail getOrderDetailById(int id);

    @Query("SELECT * from OrdersDetail WHERE product_id= :product_id and status_id=1")
    OrdersDetail getOrdersByProductId(int product_id);

    @Query("SELECT * from OrdersDetail WHERE parent_id=0 and status_id=1")
    OrdersDetail getParent();

    @Query("select o.* \n" +
            "from OrdersDetail o\n" +
            "join products p ON p.id=o.product_id " +
            "WHERE p.category_id=:category_id and o.status_id = 1")
    OrdersDetail getOrdersByCategoryId(int category_id);

    @Query("select o.* \n" +
            "from OrdersDetail o\n" +
            "join products p ON p.id=o.product_id " +
            "WHERE p.category_id IN(:categories)")
    List<OrdersDetail> getOrdersByCategories(String categories);

    @Query("select o.id,o.status_id,p.title,p.price,o.quantity,p.pos_id,(p.price * o.quantity) as subtotal\n" +
            "from OrdersDetail o \n" +
            "JOIN products p ON p.id= o.product_id \n" +
            "WHERE p.category_id IN(:categories) and o.status_id=:status_id")
    List<Review> getReviewIn(int categories,int status_id);

    @Query("select o.id,o.status_id,p.title,p.price,o.quantity,p.pos_id,(p.price * o.quantity) as subtotal\n" +
            "from OrdersDetail o \n" +
            "JOIN products p ON p.id= o.product_id \n" +
            "WHERE o.parent_id=:parent_id")
    List<Review> getChild(int parent_id);

    @Query("select o.id,o.status_id,p.title,p.price,o.quantity,p.pos_id,(p.price * o.quantity) as subtotal\n" +
            "from OrdersDetail o \n" +
            "JOIN products p ON p.id= o.product_id \n" +
            "WHERE p.category_id NOT IN(:categories) and o.parent_id=0 and o.status_id =:status_id")
    List<Review> getReviewNotIn(int categories,int status_id);

    @Query("SELECT count(*) from OrdersDetail WHERE product_id= :product_id")
    int productExists(int product_id);


    @Query("select count(*) as total \n" +
            "from OrdersDetail o\n" +
            "join products p ON p.id=o.product_id " +
            "WHERE (p.category_id=:cat1 or p.category_id=:cate2 or p.category_id=:cate3) and o.status_id=1")
    int categoryExists(int cat1,int cate2,int cate3);

    @Query("select sum(p.price * d.quantity) as total \n" +
            "from OrdersDetail d\n" +
            "join products p ON p.id=d.product_id " +
            "JOIN Orders o On o.id=d.order_id and o.status_id=1 and d.status_id=:status_id")
    int getTotal(int status_id);

    @Query("select sum(p.grams) gram\n" +
            "from OrdersDetail o\n" +
            "JOIN products p ON p.id = o.product_id and status_id=2")
    int getGrams();

    @Query("UPDATE OrdersDetail set quantity=:quantity where id=:id")
    void updateQuantity(int id,int quantity);

    @Query("UPDATE OrdersDetail set product_id=:product_id where id=:id")
    void updateChangeProduct(int id,int product_id);

    @Query("UPDATE OrdersDetail set quantity=:quantity where parent_id=:id")
    void updateChild(int id,int quantity);

    @Query("UPDATE OrdersDetail set status_id=2 where status_id=1")
    void updateNewRequest();

    @Query("UPDATE OrdersDetail set status_id=3 where order_id=:order_id")
    void updateFinishOrder(int order_id);

    @Insert
    void insertAll(OrdersDetail... ordersDetails);

    @Query("DELETE FROM OrdersDetail")
    void deleteAll();

    @Query("DELETE FROM OrdersDetail where parent_id=:parent_id")
    void deleteAllChild(int parent_id);

    @Query("DELETE FROM OrdersDetail where id=:id")
    void deleteById(int id);

    @Delete
    void delete(OrdersDetail... ordersDetail);
}
