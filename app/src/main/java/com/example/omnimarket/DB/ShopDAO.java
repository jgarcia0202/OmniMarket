package com.example.omnimarket.DB;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.omnimarket.Item;
import com.example.omnimarket.Purchase;
import com.example.omnimarket.User;

import java.util.List;

@Dao
public interface ShopDAO {

    @Insert
    void insert(User... users);

    @Update
    void update(User... users);

    @Delete
    void delete(User user);

    @Query("SELECT * FROM " + AppDataBase.USER_TABLE)
    List<User> getUsers();

    @Query("SELECT * FROM " + AppDataBase.USER_TABLE + " WHERE mUserName = :userName AND mPassword = :password")
    User getUserByUserName(String userName, String password);

    @Insert
    void insert(Item... items);

    @Update
    void update(Item... item);

    @Delete
    void delete(Item item);

    @Query("SELECT * FROM " + AppDataBase.ITEM_TABLE)
    List<Item> getItems();

    @Query("SELECT * FROM " + AppDataBase.ITEM_TABLE +" WHERE mUserID = :userID")
    List<Item> getItemsByUserID(int userID);

    @Insert
    void insert(Purchase... purchases);

    @Update
    void update(Purchase... purchase);

    @Delete
    void delete(Purchase purchase);
}
