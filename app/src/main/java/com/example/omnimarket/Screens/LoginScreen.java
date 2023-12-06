package com.example.omnimarket.Screens;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.omnimarket.DB.AppDataBase;
import com.example.omnimarket.DB.UserDAO;
import com.example.omnimarket.R;
import com.example.omnimarket.User;
import com.example.omnimarket.databinding.ActivityMainBinding;

import java.util.List;

public class LoginScreen extends AppCompatActivity {
    ActivityMainBinding binding;

    private static final String LOGIN_SCREEN = "com.example.omnimarket.Screens.LoginScreen";

    TextView mSignInDisplay;
    TextView mSignUpDisplay;

    EditText mUser;
    EditText mPass;

    Button mLogin;
    Button mSignUp;

    UserDAO mUserDAO;

    List<User> mUserList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mSignInDisplay = binding.signInTextBox;
        mSignUpDisplay = binding.signUpTextBox;
        mUser = binding.usernameInput;
        mPass = binding.passwordInput;
        mLogin = binding.loginButton;
        mSignUp = binding.signUpButton;

        mUserDAO = Room.databaseBuilder(this, AppDataBase.class, AppDataBase.DATABASE_NAME)
                .allowMainThreadQueries()
                .build()
                .UserDao();

        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkUser();

            }
        });
        mSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = SignupScreen.getIntent(getApplicationContext());
                startActivity(intent);
            }
        });
        checkForUsers();



    }// END OF ONCREATE

    public static Intent getIntent(Context context){
        Intent intent = new Intent(context, LoginScreen.class);
        return intent;
    }

    private void checkUser(){
        String username = mUser.getText().toString();
        String password = mPass.getText().toString();
        User user = mUserDAO.getUserByUserName(username, password);
        if (user == null){
            Toast.makeText(this, "No User Found, If You Don't Have An Account Please Sign Up", Toast.LENGTH_SHORT).show();
        } else if (user.getUserName().equals(username) && user.getPassword().equals(password)) {
            Toast.makeText(this, "Everything Matches", Toast.LENGTH_SHORT).show();
            Intent intent = HomeScreen.getIntent(getApplicationContext(), user);
            intent.putExtra("USER_KEY", user);
            startActivity(intent);
        }

    }

    private void checkForUsers(){
        mUserList = mUserDAO.getUsers();
        User testUser = new User("testuser1", "testuser1", "testuser1", false);
        User admin = new User("admin2", "admin2", "admin2", true);
        if (mUserList.isEmpty()){
            mUserDAO.insert(testUser);
            mUserDAO.insert(admin);
        }
    }
}