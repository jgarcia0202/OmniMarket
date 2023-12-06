package com.example.omnimarket;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.omnimarket.DB.AppDataBaseItem;

@Entity(tableName = AppDataBaseItem.ITEM_TABLE)
public class Item {

    @PrimaryKey(autoGenerate = true)
    private int mItemID;

    private String mName;
    private double mPrice;
    private double mQuantity;
    private String mDescription;

    public Item(String name, double price, double quantity, String description) {
        mName = name;
        mPrice = price;
        mQuantity = quantity;
        mDescription = description;
    }

    @Override
    public String toString() {
        return "Item{" +
                "mItemID='" + mItemID + '\'' +
                "mName='" + mName + '\'' +
                ", mPrice=" + mPrice +
                ", mQuantity=" + mQuantity +
                ", mDescription='" + mDescription + '\'' +
                '}';
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
}
