package com.kamilglonek.farmmanager.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.kamilglonek.farmmanager.R;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText etUsername = (EditText) findViewById(R.id.etUsername);
        final EditText etPassword = (EditText) findViewById(R.id.etDayNumber);

        final Button bLogin = (Button) findViewById(R.id.bLogin);
        final TextView registerLink = (TextView) findViewById(R.id.tvRegisterHere);
        final CheckBox cbRememberPrefs = (CheckBox) findViewById(R.id.cbRememberPrefs);

        ArrayList<String> loginData = new ArrayList<>();
        registerLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                LoginActivity.this.startActivity(registerIntent);
            }
        });

        bLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // display message if username or password are not given
                final String username = etUsername.getText().toString();
                final String password = etPassword.getText().toString();
                    if (username.matches("") || password.matches("")) {
                        AlertDialog missmatchedPasswordsAlert = new AlertDialog.Builder(LoginActivity.this).create();
                        missmatchedPasswordsAlert.setMessage("Username and password required");
                        missmatchedPasswordsAlert.show();
                    }
                    // login
                    else {

                        ParseUser.logInInBackground(username, password, new LogInCallback() {
                                @Override
                                public void done(ParseUser user, ParseException e) {

                                    if (e == null) {
                                        Log.i("Signup", "Login successful");

                                        if(cbRememberPrefs.isChecked()) {
                                            loginData.add(username);
                                            loginData.add(password);
                                            saveData(loginData);
                                        }

                                            Intent userIntent = new Intent(LoginActivity.this, UserActivity.class);
                                            LoginActivity.this.startActivity(userIntent);

                                    } else {
                                        Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                        });
                    }
                }
        });
    }

    public void saveData(ArrayList<String> loginData) {
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(loginData);
        editor.putString("passes", json);
        editor.apply();
    }
}