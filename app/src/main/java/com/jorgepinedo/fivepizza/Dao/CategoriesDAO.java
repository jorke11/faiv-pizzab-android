package com.jorgepinedo.fivepizza.Dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.jorgepinedo.fivepizza.Models.Categories;
import com.jorgepinedo.fivepizza.Models.Products;

import java.util.List;

@Dao
public interface CategoriesDAO {

    @Query("SELECT * from categories")
    List<Categories> getAllCategories();

    @Query("SELECT * from categories WHERE id= :id")
    Categories getCategoryById(int id);

    @Insert
    void insertAll(Categories... categories);

    @Query("DELETE FROM categories")
    void deleteAll();
}
