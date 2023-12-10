package com.example.omnimarket;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.omnimarket.DB.AppDataBase;

@Entity(tableName = AppDataBase.PURCHASE_DATABASE)
public class Purchase {

    @PrimaryKey(autoGenerate = true)
    private int mPurchaseID;

    private int userID;
    private int itemID;

    public Purchase(int userID, int itemID) {
        this.userID = userID;
        this.itemID = itemID;
    }

    @Override
    public String toString() {
        return "Purchase{" +
                "mPurchaseID=" + mPurchaseID +
                ", userID=" + userID +
                ", itemID=" + itemID +
                '}';
    }

    public int getPurchaseID() {
        return mPurchaseID;
    }

    public void setPurchaseID(int purchaseID) {
        mPurchaseID = purchaseID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getItemID() {
        return itemID;
    }

    public void setItemID(int itemID) {
        this.itemID = itemID;
    }
}
