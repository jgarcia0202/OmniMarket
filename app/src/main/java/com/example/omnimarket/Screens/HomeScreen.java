package com.example.omnimarket.Screens;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.omnimarket.DB.AppDataBase;
import com.example.omnimarket.DB.UserDAO;
import com.example.omnimarket.R;
import com.example.omnimarket.User;
import com.example.omnimarket.databinding.HomepageBinding;

import java.util.List;

public class HomeScreen extends AppCompatActivity {
    HomepageBinding binding;

    private static final String HOME_SCREEN = "com.example.omnimarket.Screens.HomeScreen";

    TextView mWelcomeMessage;

    Button mPastOrders;
    Button mCancel;
    Button mFind;
    Button mLogout;
    Button mAdmin;
    Button mDelete;

    UserDAO mUserDAO;

    User mReturnedUser;
    boolean adminMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage);

        binding = HomepageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mWelcomeMessage = binding.welcomeMessage;
        mPastOrders = binding.pastOrdersButton;
        mCancel = binding.cancelOrderButton;
        mFind = binding.findButton;
        mLogout = binding.logoutButton;
        mAdmin = binding.adminModeButton;
        mDelete = binding.deleteUserButton;
        mReturnedUser = (User) getIntent().getSerializableExtra("USER_KEY");

        mUserDAO = Room.databaseBuilder(this, AppDataBase.class, AppDataBase.DATABASE_NAME)
                .allowMainThreadQueries()
                .build()
                .UserDao();

        //BUTTON CLICK FUNCTIONS BELOW
        mLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = LoginScreen.getIntent(getApplicationContext());
                startActivity(intent);
            }
        });

        mFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = ShopScreen.getIntent(getApplicationContext());
                startActivity(intent);
            }
        });

        mDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmDeleteDialog(mReturnedUser);
            }
        });

        mAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (adminMode == false){
                    Toast.makeText(getApplicationContext(), "ADMIN MODE ON", Toast.LENGTH_SHORT).show();
                    adminMode = true;
                    return;
                }
                Toast.makeText(getApplicationContext(), "ADMIN MODE OFF", Toast.LENGTH_SHORT).show();
                adminMode = false;
            }
        });

        // ADMIN PRIVELAGES BELOW
        if (mReturnedUser.getAdmin() == true){
            mWelcomeMessage.append("Administrator " + mReturnedUser.getName());
            mAdmin.setVisibility(View.VISIBLE);
        } else if (mReturnedUser.getAdmin() == false) {
            mWelcomeMessage.append("" + mReturnedUser.getName());
        }
    }// END OF ONCREATE

    public static Intent getIntent(Context context, User user){
        Intent intent = new Intent(context, HomeScreen.class);
        return intent;
    }

    public void confirmDeleteDialog(User user){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirmation")
                .setMessage("Are you sure you want to delete user?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which){
                        mUserDAO.delete(user);
                        Toast.makeText(getApplicationContext(), "User Has Been Deleted", Toast.LENGTH_SHORT).show();
                        Intent intent = LoginScreen.getIntent(getApplicationContext());
                        startActivity(intent);
                    }
        })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(), "User Will Not Be Deleted", Toast.LENGTH_SHORT).show();
                        return;
                    }
                })
                .show();
    }

}