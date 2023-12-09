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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_menu);

        binding = AdminMenuBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mHomeButt = binding.adminHome;
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



    }// END OF CREATE

    public void returnUsers(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("LIST OF ACTIVE USERS")
                .setMessage(getUsers())
                .setNeutralButton("Exit", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which){

                        return;

                    }
                })
                .show();
    }

    public String getUsers(){
        mActiveUsers = mShopDAO.getUsers();
        String returnedString = "";
        for (User user: mActiveUsers){
            returnedString = returnedString + user.toString();
        }
        return returnedString;
    }
    public static Intent getIntent(Context context, User user){
        Intent intent = new Intent(context, AdminMenu.class);
        intent.putExtra("USER_KEY", user.getUserID());
        return intent;
    }

}
