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

    @Query("SELECT * FROM " + AppDataBase.ITEM_TABLE + " WHERE mItemID = :itemId")
    Item getItemById(int itemId);

    @Insert
    void insert(Purchase... purchases);

    @Update
    void update(Purchase... purchase);

    @Delete
    void delete(Purchase purchase);

    @Query("SELECT * FROM " + AppDataBase.PURCHASE_DATABASE + " WHERE mUserID = :userId AND mItemID = :itemId")
    Purchase getPurchaseByUserItem(int userId, int itemId);

    @Query("SELECT * FROM " + AppDataBase.PURCHASE_DATABASE + " WHERE mItemID = :itemId")
    Purchase getPurchaseByItemId( int itemId);

    @Query("SELECT * FROM " + AppDataBase.PURCHASE_DATABASE + " WHERE mUserID = :userId")
    List<Purchase> getPurchasesByUserId(int userId);

    @Query("SELECT * FROM " + AppDataBase.PURCHASE_DATABASE + " WHERE mItemID = :itemId")
    List<Purchase> getPurchasesByItemId(int itemId);
}
