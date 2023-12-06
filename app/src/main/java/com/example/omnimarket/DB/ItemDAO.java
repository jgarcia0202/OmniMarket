package com.example.omnimarket.DB;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.omnimarket.Item;

import java.util.List;

@Dao
public interface ItemDAO {

    @Insert
    void insert(Item... items);

    @Update
    void update(Item... items);

    @Delete
    void delete(Item item);

    @Query("SELECT * FROM " + AppDataBase.ITEM_TABLE)
    List<Item> getItems();

    //@Query("SELECT * FROM " + AppDataBase.ITEM_TABLE +" WHERE mItemName = :name")
    //    Item getItemByName(String name);

}
