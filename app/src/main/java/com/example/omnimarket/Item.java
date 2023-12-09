package com.example.omnimarket;

import android.widget.Toast;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.omnimarket.DB.AppDataBase;


@Entity(tableName = AppDataBase.ITEM_TABLE)
public class Item {

    @PrimaryKey(autoGenerate = true)
    private int mItemID;

    private String mName;
    private double mPrice;
    private double mQuantity;
    private String mDescription;
    private int mUserID;

    public Item(String name, double price, double quantity, String description, int userID) {
        mName = name;
        mPrice = price;
        mQuantity = quantity;
        mDescription = description;

        mUserID = userID;
    }

    @Override
    public String toString() {
        return mName + "\n" +
                "Price: $" + mPrice + "\n"+
                "Availability: " + checkQuantity() + "\n" +
                "\n" +
                "Details: " + mDescription + ".\n";
    }

    public String toString2() {
        return mName + "\n" +
                "Price: $" + mPrice + "\n"+
                "\n" +
                "Details: " + mDescription + ".\n";
    }

    public int getUserID() {
        return mUserID;
    }

    public void setUserID(int userID) {
        mUserID = userID;
    }
    public String checkQuantity(){
        if (mQuantity <= 0){
            return ("Not Available");
        }
        return ("Available");
    }

    public int getItemID() {
        return mItemID;
    }

    public void setItemID(int itemID) {
        mItemID = itemID;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public double getPrice() {
        return mPrice;
    }

    public void setPrice(double price) {
        this.mPrice = price;
    }

    public double getQuantity() {
        return mQuantity;
    }

    public void setQuantity(double quantity) {
        this.mQuantity = quantity;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        this.mDescription = description;
    }

    public void reduceQuantity(){
        if (mQuantity <= 0){
            System.out.println("Cannot Purchase, There is no " + mName + " left");
            return;
        }
        mQuantity = mQuantity - 1;
    }

}
