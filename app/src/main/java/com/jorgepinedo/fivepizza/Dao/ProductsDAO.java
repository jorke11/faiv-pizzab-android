package com.jorgepinedo.fivepizza.Dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.jorgepinedo.fivepizza.Models.Categories;
import com.jorgepinedo.fivepizza.Models.Products;

import java.util.List;

@Dao
public interface ProductsDAO {

    @Query("SELECT * from products")
    List<Products> getAllProducts();

    @Query("SELECT * from products where category_id = :category_id")
    List<Products> getAllProductsCategory(int category_id);

    @Query("SELECT * from products WHERE id= :id")
    Products getProductById(int id);

    @Query("SELECT * from products WHERE url= :url")
    Products getProductByUrl(String url);

    @Query("SELECT * from products WHERE pos_id= :pos_id")
    Products getProductByPosId(int pos_id);

    @Insert
    void insert(Products... products);

    @Update
    void update(Products... products);

    @Query("DELETE FROM products")
    void delete();
}
