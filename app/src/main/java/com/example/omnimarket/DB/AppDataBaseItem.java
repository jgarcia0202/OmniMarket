package com.example.omnimarket.DB;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.omnimarket.Item;

@Database(entities = {Item.class}, version = 1)
public abstract class AppDataBaseItem extends RoomDatabase {
    public static final String DATABASE_NAME = "Item.db";
    public static final String ITEM_TABLE = "item_table";

    private static volatile AppDataBaseItem instance;
    private static final Object LOCK = new Object();

    public abstract ItemDAO ItemDAO();

    public static AppDataBaseItem getInstance(Context context){
        if(instance == null){
            synchronized (LOCK){
                if(instance == null){
                    instance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDataBaseItem.class,DATABASE_NAME).build();
                }
            }
        }
        return instance;
    }

}

