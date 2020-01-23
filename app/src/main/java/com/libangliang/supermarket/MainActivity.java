package com.libangliang.supermarket;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.libangliang.supermarket.Model.Users;
import com.libangliang.supermarket.Prevalent.Prevalent;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {


    private Button registerButton, loginButton;

    private String parentDBName = "Users";

    static MainActivity mainActivity;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        registerButton = (Button) findViewById(R.id.main_register_btn);
        loginButton = (Button) findViewById(R.id.main_login_btn);

        mainActivity = this;

        Paper.init(this);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });

        String userPhoneKey = Paper.book().read(Prevalent.userPhoneKey);
        String userPasswordKey = Paper.book().read(Prevalent.userPasswordKey);

        if(userPhoneKey != "" && userPasswordKey != ""){
            if(!TextUtils.isEmpty(userPhoneKey) && !TextUtils.isEmpty(userPasswordKey)){
                Toast.makeText(MainActivity.this,"Try to log you in...",Toast.LENGTH_SHORT).show();
                allowAccess(userPhoneKey,userPasswordKey);

            }
        }

    }

    public static MainActivity getInstance(){
        return mainActivity;
    }


    private void allowAccess(final String phone, final String password) {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(parentDBName).child(phone).exists()){
                    //found phone in database
                    //retrieve data and feed it in a User class
                    Users userData = dataSnapshot.child(parentDBName).child(phone).getValue(Users.class);
                    if(userData.getPhone().equals(phone)){
                        //then check password

                        if(userData.getPassword().equals(password)){
                            //login success
                            Toast.makeText(MainActivity.this,"Login Successfully.",Toast.LENGTH_SHORT).show();
                            //send user to home activity
                            Intent intent = new Intent(MainActivity.this,HomeActivity.class);

                            //pass current user to main activity
                            Prevalent.currentOnlineUser = userData;
                            startActivity(intent);

                        }
                        else{
                            //not correct password
                            Toast.makeText(MainActivity.this,"Password Incorrect.",Toast.LENGTH_SHORT).show();
                        }
                    }


                }
                else{
                    //count not find in database
                    Toast.makeText(MainActivity.this,"Account with "+phone +"does not exist",
                            Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
