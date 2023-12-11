package com.example.omnimarket;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.omnimarket.DB.AppDataBase;
import com.example.omnimarket.DB.ShopDAO;

@Entity(tableName = AppDataBase.PURCHASE_DATABASE)
public class Purchase {

    @PrimaryKey(autoGenerate = true)
    private int mPurchaseID;

    private int mUserID;
    private int mItemID;
    private int mQuantity;

    public Purchase(int userID, int itemID, int quantity) {
        this.mUserID = userID;
        this.mItemID = itemID;
        this.mQuantity = quantity;
    }

    @Override
    public String toString() {
        return "Purchase{" +
                "mPurchaseID=" + mPurchaseID +
                ", mUserID=" + mUserID +
                ", mItemID=" + mItemID +
                ", mQuantity=" + mQuantity +
                '}';
    }

    public int getPurchaseID() {
        return mPurchaseID;
    }

    public void setPurchaseID(int purchaseID) {
        mPurchaseID = purchaseID;
    }

    public int getUserID() {
        return mUserID;
    }

    public void setUserID(int userID) {
        mUserID = userID;
    }

    public int getItemID() {
        return mItemID;
    }

    public void setItemID(int itemID) {
        mItemID = itemID;
    }

    public int getQuantity() {
        return mQuantity;
    }

    public void setQuantity(int quantity) {
        mQuantity = quantity;
    }
}
