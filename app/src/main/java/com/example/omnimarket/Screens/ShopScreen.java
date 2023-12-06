package com.example.omnimarket.Screens;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.omnimarket.DB.AppDataBase;
import com.example.omnimarket.DB.ItemDAO;
import com.example.omnimarket.DB.UserDAO;
import com.example.omnimarket.Item;
import com.example.omnimarket.R;
import com.example.omnimarket.User;
import com.example.omnimarket.databinding.ActivityMainBinding;
import com.example.omnimarket.databinding.StoreBinding;

import java.util.List;

public class ShopScreen extends AppCompatActivity {

    StoreBinding binding;

    TextView mShopWelcome;
    TextView mItemDisplay;

    EditText mSearchBox;

    Button mHome;
    Button mSearchBut;

    ItemDAO mItemDAO;

    List<Item> mItemList;
    User mReturnedUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.store);

        binding = StoreBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mShopWelcome = binding.shopWelcome;
        mItemDisplay = binding.itemDisplay;
        mHome = binding.shopHomeButton;
        mSearchBox = binding.searchBox;
        mSearchBut = binding.searchButton;
        mReturnedUser = (User) getIntent().getSerializableExtra("USER_KEY");

        mItemDAO = Room.databaseBuilder(this, AppDataBase.class, AppDataBase.DATABASE_NAME)
                .allowMainThreadQueries()
                .build()
                .ItemDao();

        mHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = HomeScreen.getIntent(getApplicationContext(), mReturnedUser);
                intent.putExtra("USER_KEY", mReturnedUser);
                startActivity(intent);
            }
        });

        checkForItems();


    }// END OF ONCREATE

    public static Intent getIntent(Context context){
        Intent intent = new Intent(context, ShopScreen.class);
        return intent;
    }

    private void checkForItems(){
        mItemList = mItemDAO.getItems();
        Item testItem = new Item("testItem", 10, 2, "this is a test item");
        if (mItemList.isEmpty()){
            mItemDAO.insert(testItem);
        }
        for (Item item : mItemList){
            mItemDisplay.append(item.getName());
        }
    }

}