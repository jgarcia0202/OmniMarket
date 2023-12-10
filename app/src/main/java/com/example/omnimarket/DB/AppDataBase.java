package com.example.omnimarket.DB;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.omnimarket.Item;
import com.example.omnimarket.Purchase;
import com.example.omnimarket.User;

@Database(entities = {User.class, Item.class, Purchase.class}, version = 1, exportSchema = false)
public abstract class AppDataBase extends RoomDatabase {
    public static final String DATABASE_NAME = "OmniMarket.db";
    public static final String USER_TABLE = "user_table";
    public static final String ITEM_TABLE = "item_table";
    public static final String PURCHASE_DATABASE = "purchase_table";

    private static volatile AppDataBase instance;
    private static final Object LOCK = new Object();

    public abstract ShopDAO ShopDAO();

    public static AppDataBase getInstance(Context context){
        if(instance == null){
            synchronized (LOCK){
                if(instance == null){
                    instance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDataBase.class,DATABASE_NAME).build();
                }
            }
        }
        return instance;
    }
}