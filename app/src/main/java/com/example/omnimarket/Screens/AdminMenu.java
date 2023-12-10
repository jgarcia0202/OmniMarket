package com.example.omnimarket.Screens;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
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
import com.example.omnimarket.R;
import com.example.omnimarket.User;
import com.example.omnimarket.databinding.AdminMenuBinding;
import com.example.omnimarket.databinding.PastOrdersBinding;

import java.util.List;

public class AdminMenu extends AppCompatActivity {
    AdminMenuBinding binding;
    TextView mMenuTitle;

    Button mHomeButt;
    Button mViewUsers;
    Button mViewItems;
    Button mAddItem;
    Button mDeleteItem;

    ShopDAO mShopDAO;

    User mReturnedUser;
    List<User> mActiveUsers;
    List<Item> mItemsOwned;
    List<Item> mItemsInShop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_menu);

        binding = AdminMenuBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mHomeButt = binding.adminHome;
        mMenuTitle = binding.adminText;
        mViewUsers = binding.viewUsers;
        mViewItems = binding.viewItems;
        mAddItem = binding.addItem;
        mDeleteItem = binding.deleteItem;

        mReturnedUser = (User) getIntent().getSerializableExtra("USER_KEY");

        mShopDAO = Room.databaseBuilder(this, AppDataBase.class, AppDataBase.DATABASE_NAME)
                .allowMainThreadQueries()
                .build()
                .ShopDAO();

        mHomeButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = HomeScreen.getIntent(getApplicationContext(), mReturnedUser);
                intent.putExtra("USER_KEY", mReturnedUser);
                startActivity(intent);
            }
        });
        mViewUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnUsers();
            }
        });

        mViewItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnedItems();
            }
        });

        mAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewItem();
            }
        });

        mDeleteItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteItem();
            }
        });


    }// END OF CREATE

    public void returnUsers(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("LIST OF ACTIVE USERS")
                .setMessage(getUsers())
                .setNeutralButton("Exit", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which){
                        dialog.dismiss();
                    }
                })
                .show();
    }

    public void returnedItems(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("LIST OF ITEMS IN STORE");

        ScrollView scrollView = new ScrollView(this);
        TextView textView = new TextView(this);
        textView.setText(getItems());
        scrollView.addView(textView);

        builder.setView(scrollView)
                .setNeutralButton("Exit", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which){
                        dialog.dismiss();
                    }
                })
                .show();
    }

    public void addNewItem(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter New Item Details");

        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        EditText nameEdit = new EditText(this);
        EditText priceEdit = new EditText(this);
        EditText quantityEdit = new EditText(this);
        EditText descriptionEdit = new EditText(this);

        nameEdit.setHint("Name");
        priceEdit.setHint("Price");
        quantityEdit.setHint("Quantity");
        descriptionEdit.setHint("Description");

        linearLayout.addView(nameEdit);
        linearLayout.addView(priceEdit);
        linearLayout.addView(quantityEdit);
        linearLayout.addView(descriptionEdit);

        builder.setView(linearLayout)

                .setPositiveButton("Add Item", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String name = nameEdit.getText().toString();
                        double price = Double.parseDouble(priceEdit.getText().toString());
                        double quantity = Double.parseDouble(quantityEdit.getText().toString());
                        String description = descriptionEdit.getText().toString();
                        Item item = new Item(name, price, 1, description, -1);

                        mShopDAO.insert(item);
                        Toast.makeText(getApplicationContext(),item.getName() + " has been added to item database", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which){
                        dialog.dismiss();
                    }
                })
                .show();
    }
    public void deleteItem(){
        mItemsInShop = mShopDAO.getItems();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Item Menu");

        AutoCompleteTextView autoCompleteTextView = new AutoCompleteTextView(this);
        LinearLayout linearLayout = new LinearLayout(this);

        linearLayout.setOrientation(LinearLayout.VERTICAL);
        ArrayAdapter<Item> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, mItemsInShop);
        autoCompleteTextView.setAdapter(adapter);
        autoCompleteTextView.setThreshold(1);
        autoCompleteTextView.setWidth(900);
        linearLayout.addView(autoCompleteTextView);
        autoCompleteTextView.setOnItemClickListener((parent, view, position, id) -> {
            Item item = (Item) parent.getItemAtPosition(position);
            displayItem(linearLayout, autoCompleteTextView, item);
        });

        builder.setView(linearLayout)
                .setNeutralButton("Exit", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which){
                        dialog.dismiss();
                    }
                })
                .show();
    }

//Methods for the alertDialogs
    public String getUsers(){
        mActiveUsers = mShopDAO.getUsers();
        String returnedString = "";
        for (User user: mActiveUsers){
            mItemsOwned = mShopDAO.getItemsByUserID(user.getUserID());
            returnedString = returnedString + user.toString() +
                    "Items Owned: " + mItemsOwned.size() + "\n----------------------------------\n";;
        }
        return returnedString;
    }

    public String getItems(){
        mItemsInShop = mShopDAO.getItems();
        String returnedString = "";
        for (Item item: mItemsInShop){
            returnedString = returnedString + item.toString() + "\n----------------------------------\n";;
        }
        return returnedString;
    }

    public static Intent getIntent(Context context, User user){
        Intent intent = new Intent(context, AdminMenu.class);
        intent.putExtra("USER_KEY", user.getUserID());
        return intent;
    }

    public void displayItem(LinearLayout linearLayout,AutoCompleteTextView autoCompleteTextView, Item item){
        linearLayout.removeAllViews();
        linearLayout.addView(autoCompleteTextView);
        TextView mItemView = new TextView(this);
        mItemView.setWidth(900);
        mItemView.setTextSize(15);
        mItemView.setText(item.toString());
        linearLayout.addView(mItemView);

        Button mDeleteButt = new Button(this);
        mDeleteButt.setText("Delete");
        linearLayout.addView(mDeleteButt);

        mItemView.append("---------------------------------------------------\n");

        mDeleteButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleOrder(item);
                linearLayout.removeAllViews();
            }
        });
        autoCompleteTextView.setText("");
    }

    public void handleOrder(Item item){
        Toast.makeText(this, item.getName() + " hase been removed from item database.", Toast.LENGTH_SHORT).show();
        mShopDAO.delete(item);
    }

}
