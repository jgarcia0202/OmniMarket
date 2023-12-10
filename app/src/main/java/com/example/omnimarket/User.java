package com.example.omnimarket;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.omnimarket.DB.AppDataBase;

import java.io.Serializable;
import java.util.List;

@Entity(tableName = AppDataBase.USER_TABLE)
public class User implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int mUserID;

    private String mUserName;
    private String mName;
    private String mPassword;
    private boolean mIsAdmin;

    public User(String userName, String name, String password, boolean isAdmin) {
        mUserName = userName;
        mName = name;
        mPassword = password;
        mIsAdmin = isAdmin;
    }

    @Override
    public String toString() {
        return  "UserName = " + mUserName + "\n" +
                "Name = " + mName + "\n" +
                "IsAdmin = " + mIsAdmin + "\n" ;
    }

    public int getUserID() {
        return mUserID;
    }

    public void setUserID(int userID) {
        mUserID = userID;
    }

    public String getUserName() {
        return mUserName;
    }

    public void setUserName(String userName) {
        mUserName = userName;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String password) {
        mPassword = password;
    }

    public boolean getAdmin() {
        return  mIsAdmin;
    }

    public void setAdmin(Boolean admin) {
        mIsAdmin = admin;
    }

    public boolean isAdmin() {
        return mIsAdmin;
    }
}
