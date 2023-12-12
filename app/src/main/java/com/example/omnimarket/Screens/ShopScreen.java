package com.example.omnimarket.Screens;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.omnimarket.DB.AppDataBase;
import com.example.omnimarket.DB.ShopDAO;
import com.example.omnimarket.Item;
import com.example.omnimarket.Purchase;
import com.example.omnimarket.R;
import com.example.omnimarket.User;
import com.example.omnimarket.databinding.StoreBinding;

import java.util.ArrayList;
import java.util.List;

public class ShopScreen extends AppCompatActivity {
    StoreBinding binding;
    ShopDAO mShopDAO;

    TextView mShopWelcome;
    LinearLayout mItemDisplay;
    ScrollView mScrollView;

AutoCompleteTextView mAutoCompleteTextView;
    Button mHome;

    List<Item> mItemList;
    Purchase mPurchase = null;
    User mReturnedUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.store);

        binding = StoreBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mShopWelcome = binding.shopWelcome;
        mItemDisplay = binding.itemDisplay;
        mScrollView = binding.scrollShop;
        mHome = binding.shopHomeButton;
        mAutoCompleteTextView = binding.searchDropDown;
        mReturnedUser = (User) getIntent().getSerializableExtra("USER_KEY");

        mShopDAO = Room.databaseBuilder(this, AppDataBase.class, AppDataBase.DATABASE_NAME)
                .allowMainThreadQueries()
                .build()
                .ShopDAO();

        mItemList = mShopDAO.getItems();

        ArrayAdapter<Item> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, mItemList);
        mAutoCompleteTextView.setAdapter(adapter);
        mAutoCompleteTextView.setThreshold(1);
        mAutoCompleteTextView.setOnItemClickListener((parent, view, position, id) -> {
            Item item = (Item) parent.getItemAtPosition(position);
            displayItem(item);
            // For example, you can access the properties of the selected item

        });

        mHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = HomeScreen.getIntent(getApplicationContext(), mReturnedUser);
                intent.putExtra("USER_KEY", mReturnedUser);
                startActivity(intent);
            }
        });


    }// END OF ONCREATE

    public static Intent getIntent(Context context){
        Intent intent = new Intent(context, ShopScreen.class);
        return intent;
    }


    public void handleOrder(Item item){
        mPurchase = mShopDAO.getPurchaseByUserItem(mReturnedUser.getUserID(), item.getItemID());
        if (mPurchase == null){
            mPurchase = new Purchase(mReturnedUser.getUserID(), item.getItemID(),1);
            mShopDAO.insert(mPurchase);
            item.reduceQuantity();
            Toast.makeText(this, item.getName() + " purchased!", Toast.LENGTH_SHORT).show();
            return;
        }
        else {
            mPurchase.setQuantity(mPurchase.getQuantity()+1);
            item.setQuantity(item.getQuantity()-1);
            mShopDAO.update(item);
            mShopDAO.update(mPurchase);
            Toast.makeText(this, item.getName() + " purchased!", Toast.LENGTH_SHORT).show();
        }

    }

    public void displayItem(Item item){
        mItemDisplay.removeAllViews();
        TextView mItemView = new TextView(this);
        mItemView.setWidth(900);
        mItemView.setTextSize(15);
        mItemView.setText(item.toString());
        mItemDisplay.addView(mItemView);

        Button mOrderButt = new Button(this);
        mOrderButt.setText("Order");
        if (item.getQuantity() > 0){
            mItemDisplay.addView(mOrderButt);
        }

        mItemView.append("---------------------------------------------------\n");

        mOrderButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleOrder(item);
                displayItem(item);
            }
        });
        mAutoCompleteTextView.setText("");
    }

}