package com.libangliang.supermarket;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.libangliang.supermarket.Model.Users;
import com.libangliang.supermarket.Prevalent.Prevalent;

import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity {

    private EditText phoneNumberInput, passwordInput;
    private Button loginButton;
    private ProgressBar progressBar;

    private String parentDBName = "Users";

    private AppCompatCheckBox rememberMe;

    private TextView adminLink, notAdminLink;

    private TextView login_text;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        phoneNumberInput = (EditText) findViewById(R.id.login_phone_number_input);
        passwordInput = (EditText) findViewById(R.id.login_password_input);
        loginButton = (Button) findViewById(R.id.login_login_btn);

        progressBar = (ProgressBar) findViewById(R.id.login_progressBar);

        login_text = findViewById(R.id.login_login_text);

        rememberMe = (AppCompatCheckBox) findViewById(R.id.login_remeber_me_checkbox);
        Paper.init(this);

        adminLink = (TextView) findViewById(R.id.login_admin_login);
        notAdminLink = (TextView) findViewById(R.id.login_not_admin_login);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });

        adminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginButton.setText("Admin Login");
                adminLink.setVisibility(TextView.INVISIBLE);
                notAdminLink.setVisibility(TextView.VISIBLE);
                rememberMe.setVisibility(AppCompatCheckBox.INVISIBLE);
                login_text.setText("Admin Login");
                parentDBName = "Admins";
            }
        });

        notAdminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginButton.setText("Login");
                adminLink.setVisibility(TextView.VISIBLE);
                notAdminLink.setVisibility(TextView.INVISIBLE);
                parentDBName = "Users";
            }
        });





    }

    private void loginUser() {
        String phoneNumber = phoneNumberInput.getText().toString();
        String password = passwordInput.getText().toString();

        if(TextUtils.isEmpty(phoneNumber)){
            Toast.makeText(this,"Empty Phone Number.",Toast.LENGTH_SHORT).show();
        }
        if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"Empty Password.",Toast.LENGTH_SHORT).show();
        }

        else{
            //login check valid or not
            progressBar.setVisibility(ProgressBar.VISIBLE);
            
            CheckAccessAccount(phoneNumber,password);
        }

    }

    private void CheckAccessAccount(final String phone, final String password) {

        if(rememberMe.isChecked()){
            Paper.book().write(Prevalent.userPhoneKey,phone);
            Paper.book().write(Prevalent.userPasswordKey,password);
        }

        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(parentDBName).child(phone).exists()){
                    //found phone in database
                    //retrive data and feed it in a User class
                    Users userData = dataSnapshot.child(parentDBName).child(phone).getValue(Users.class);
                    if(userData.getPhone().equals(phone)){
                        //then check password
                        progressBar.setVisibility(progressBar.INVISIBLE);
                        if(userData.getPassword().equals(password)){


                            //check if is admin login
                            if(parentDBName.equals("Admins")){
                                Toast.makeText(LoginActivity.this,"Admin Login Successfully.",Toast.LENGTH_SHORT).show();
                                //send user to home activity
                                Intent intent = new Intent(LoginActivity.this,AdminCategoryActivity.class);
                                startActivity(intent);
                                finish();

                            }
                            else if(parentDBName.equals("Users")){
                                //login success
                                Toast.makeText(LoginActivity.this,"Login Successfully.",Toast.LENGTH_SHORT).show();
                                //send user to home activity
                                Intent intent = new Intent(LoginActivity.this,HomeActivity.class);
                                //pass current user info to prevalent class
                                Prevalent.currentOnlineUser = userData;


                                startActivity(intent);

                                finish();
                            }



                        }
                        else{
                            //not correct password
                            Toast.makeText(LoginActivity.this,"Password Incorrect.",Toast.LENGTH_SHORT).show();
                        }
                    }


                }
                else{
                    //count not find in database
                    progressBar.setVisibility(ProgressBar.INVISIBLE);
                    Toast.makeText(LoginActivity.this,"Account with "+phone +"does not exist",
                            Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
