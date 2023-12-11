package com.example.omnimarket.Screens;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.omnimarket.DB.AppDataBase;
import com.example.omnimarket.DB.ShopDAO;
import com.example.omnimarket.Item;
import com.example.omnimarket.Purchase;
import com.example.omnimarket.R;
import com.example.omnimarket.User;
import com.example.omnimarket.databinding.HomepageBinding;
import com.example.omnimarket.databinding.PastOrdersBinding;

import java.util.List;

public class PastOrders extends AppCompatActivity {
    PastOrdersBinding binding;

    private static final String USER_ID_KEY = "com.example.omnimarket.USER_ID_KEY";
    TextView mWelcomeMessage;

    Button mHomeButt;

    TextView mOrdersText;
    LinearLayout mItemsDisplay;
    ScrollView mScrollView;

    ShopDAO mShopDAO;

    User mReturnedUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.past_orders);

        binding = PastOrdersBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mWelcomeMessage = binding.pastOrdersText;
        mHomeButt = binding.pastOrderToHome;
        mOrdersText = binding.pastOrdersText;
        mScrollView = binding.scrollPastOrders;
        mItemsDisplay = binding.pastOrdersDisplay;

        mReturnedUser = (User) getIntent().getSerializableExtra("USER_KEY");

        mShopDAO = Room.databaseBuilder(this, AppDataBase.class, AppDataBase.DATABASE_NAME)
                .allowMainThreadQueries()
                .build()
                .ShopDAO();

        checkItems();

        mHomeButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = HomeScreen.getIntent(getApplicationContext(), mReturnedUser);
                intent.putExtra("USER_KEY", mReturnedUser);
                startActivity(intent);
            }
        });

    }//END OF CREATE

    public void checkItems(){
        List<Purchase> purchaseList = mShopDAO.getPurchasesByUserId(mReturnedUser.getUserID());
        if (purchaseList.isEmpty()){
            mItemsDisplay.removeAllViews();
            TextView mItemView = new TextView(this);
            mItemView.setWidth(900);
            mItemView.setTextSize(30);
            mItemView.setText(R.string.no_items_purchased);
            mItemsDisplay.addView(mItemView);
            return;
        }
        displayItems(purchaseList);
    }

    public void displayItems(List<Purchase> purchases){
        mItemsDisplay.removeAllViews();
        for (Purchase purchase: purchases){
            Item item = mShopDAO.getItemById(purchase.getItemID());
            TextView mItemView = new TextView(this);
            mItemView.setWidth(900);
            mItemView.setTextSize(15);
            mItemView.setText("Price: $" + item.getPrice() + "\n" +
                    "Amount Owned: " + purchase.getQuantity() + "\n" +
                    "\nDetails: " + item.getDescription() + ".\n");
            mItemsDisplay.addView(mItemView);

            Button mRemoveAllButt = new Button(this);
            mRemoveAllButt.setText(R.string.remove_all);
            mItemsDisplay.addView(mRemoveAllButt);

            Button mRemoveOneButt = new Button(this);
            mRemoveOneButt.setText(R.string.remove_1);
            mItemsDisplay.addView(mRemoveOneButt);

            TextView mItemViewSeperator = new TextView(this);
            mItemViewSeperator.setWidth(900);
            mItemViewSeperator.setTextSize(15);
            mItemViewSeperator.append("------------------------------------------------------------------\n");
            mItemsDisplay.addView(mItemViewSeperator);

            mRemoveAllButt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    confirmRemoveDialog(item);
                }
            });
            mRemoveOneButt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    confirmRemoveOneDialog(item);
                }
            });

        }
    }

    public void confirmRemoveDialog(Item item){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Remove Item")
                .setMessage("Are you sure you want to remove " + item.getName() + " from orders?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which){
                        handleRemove(item);
                        mShopDAO.update(item);
                        checkItems();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(), "Item will not be removed", Toast.LENGTH_SHORT).show();
                        return;
                    }
                })
                .show();
    }

    public void confirmRemoveOneDialog(Item item){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Remove Item")
                .setMessage("Are you sure you want to remove 1 " + item.getName() + " from orders?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which){
                        handleRemoveOne(item);
                        mShopDAO.update(item);
                        checkItems();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(), "Item will not be removed", Toast.LENGTH_SHORT).show();
                        return;
                    }
                })
                .show();
    }

    public void handleRemove(Item item){
        List<Purchase> purchases = mShopDAO.getPurchasesByItemId(item.getItemID());
        for (Purchase purchase: purchases){
            item.setQuantity(item.getQuantity()+purchase.getQuantity());
            mShopDAO.delete(purchase);
        }
        mShopDAO.update(item);
        Toast.makeText(getApplicationContext(), item.getName() + " has been removed!", Toast.LENGTH_SHORT).show();
    }

    public void handleRemoveOne(Item item){
        List<Purchase> purchases = mShopDAO.getPurchasesByItemId(item.getItemID());
        for (Purchase purchase: purchases){
            if (purchase.getQuantity() == 1){
                item.setQuantity(item.getQuantity()+purchase.getQuantity());
                mShopDAO.delete(purchase);
            }
            else {
                item.setQuantity(item.getQuantity()+ 1);
                purchase.setQuantity(purchase.getQuantity()-1);
                mShopDAO.update(purchase);
            }
        }
        mShopDAO.update(item);
        Toast.makeText(getApplicationContext(), "1 of " + item.getName() + " has been removed!", Toast.LENGTH_SHORT).show();
    }

    public static Intent getIntent(Context context, User user){
        Intent intent = new Intent(context, PastOrders.class);
        intent.putExtra(USER_ID_KEY, user.getUserID());
        return intent;
    }
}
