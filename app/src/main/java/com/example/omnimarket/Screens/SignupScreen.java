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
import com.example.omnimarket.DB.UserDAO;
import com.example.omnimarket.R;
import com.example.omnimarket.User;
import com.example.omnimarket.databinding.SignupBinding;

import java.util.List;

public class SignupScreen extends AppCompatActivity {
    SignupBinding binding;

    TextView mInstructions;
    TextView mAlreadyUser;

    EditText mUserInfo;
    EditText mNameInfo;
    EditText mPassInfo;
    EditText mConfirmPass;

    Button mSignupButton;
    Button mLoginButton;

    UserDAO mUserDAO;
    User mUser = null;

    List<User> mUserList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        binding = SignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mInstructions = binding.instructions;
        mAlreadyUser = binding.alreadyUser;
        mUserInfo = binding.usernameSignup;
        mNameInfo = binding.nameSignup;
        mPassInfo = binding.passwordSignup;
        mConfirmPass = binding.confirmPasswordSignup;
        mSignupButton = binding.signupConfirmButton;
        mLoginButton = binding.loginReturnButton;

        mUserDAO = Room.databaseBuilder(this, AppDataBase.class, AppDataBase.DATABASE_NAME)
                .allowMainThreadQueries()
                .build()
                .UserDao();

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = LoginScreen.getIntent(getApplicationContext());
                startActivity(intent);
            }
        });
        mSignupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewUser();
            }
        });
    }// END OF ONCREATE

    public static Intent getIntent(Context context){
        Intent intent = new Intent(context, SignupScreen.class);
        return intent;
    }

    //MADE IN GYMLOGS TO DISPLAY LOGS IN MAIN DISPLAY, KEEP IN CASE NEEDED
    private void refreshDisplay(){
        mUserList = mUserDAO.getUsers();
        //if (mUserList.isEmpty())
    }
    private void createNewUser(){
        String username = mUserInfo.getText().toString();
        String name = mNameInfo.getText().toString();
        String password = mPassInfo.getText().toString();
        String confirmPass = mConfirmPass.getText().toString();
        if (!confirmPass.equals(password)){
            Toast.makeText(getApplicationContext(), "Passwords Do Not Match, Try Again!", Toast.LENGTH_SHORT).show();
            return;
        }
        mUserList = mUserDAO.getUsers();
        for (User user: mUserList){
            if (user.getUserName().equals(username)){
                Toast.makeText(getApplicationContext(), "Username Already In Use!", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        mUser = new User(username, name, password,false);
        Toast.makeText(getApplicationContext(), "Welcome " + mUser.getName() + "!", Toast.LENGTH_SHORT).show();
        Intent intent = HomeScreen.getIntent(getApplicationContext(), mUser);
        mUserDAO.insert(mUser);
        startActivity(intent);
    }

}