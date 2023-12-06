package com.example.omnimarket.DB;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.omnimarket.Item;
import com.example.omnimarket.User;

@Database(entities = {User.class, Item.class}, version = 1, exportSchema = false)
public abstract class AppDataBase extends RoomDatabase {
    public static final String DATABASE_NAME = "OmniMarket.db";
    public static final String USER_TABLE = "user_table";
    public static final String ITEM_TABLE = "item_table";

    private static volatile AppDataBase instance;
    private static final Object LOCK = new Object();

    public abstract UserDAO UserDao();
    public abstract ItemDAO ItemDao();

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

    //public static final Migration MIGRATION_1_2 = new Migration(1,2) {
    //        @Override
    //        public void migrate(SupportSQLiteDatabase database) {
    //            database.execSQL("CREATE TABLE IF NOT EXISTS item_table (...)");
    //        }
    //    };

}