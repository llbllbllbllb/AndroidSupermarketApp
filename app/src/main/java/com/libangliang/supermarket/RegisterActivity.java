package com.libangliang.supermarket;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private Button createAccountBtn;
    private EditText nameInput, phoneNumberInput, passwordInput, confirmPasswordInput;

    private ProgressBar loadingBar;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        createAccountBtn = (Button) findViewById(R.id.register_register_btn);
        nameInput = (EditText) findViewById(R.id.register_username_input);
        phoneNumberInput = (EditText) findViewById(R.id.register_phone_number_input);
        passwordInput = (EditText) findViewById(R.id.register_password_input);
        confirmPasswordInput = (EditText) findViewById(R.id.register_confirm_password_input);

        loadingBar = (ProgressBar) findViewById(R.id.register_progressBar);




        createAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAccount();
            }
        });

    }

    private void createAccount() {

        String username = nameInput.getText().toString();
        String phoneNumber = phoneNumberInput.getText().toString();
        String password = passwordInput.getText().toString();
        String confirmPassword = confirmPasswordInput.getText().toString();

        //handle exception case

        if(TextUtils.isEmpty(username)){
            Toast.makeText(this,"Empty Username.",Toast.LENGTH_SHORT).show();
        }
        if(TextUtils.isEmpty(phoneNumber)){
            Toast.makeText(this,"Empty Phone Number.",Toast.LENGTH_SHORT).show();
        }
        if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"Empty Password.",Toast.LENGTH_SHORT).show();
        }
        if(TextUtils.isEmpty(confirmPassword)){
            Toast.makeText(this,"Empty Confirm Password",Toast.LENGTH_SHORT).show();
        }
        if(!password.equals(confirmPassword)){
            Toast.makeText(this,"Inconsistent Password",Toast.LENGTH_SHORT).show();
        }

        else{
            loadingBar.setVisibility(ProgressBar.VISIBLE);
            validatePhoneNumber(username,phoneNumber,password);

        }

    }

    private void validatePhoneNumber(final String username, final String phoneNumber, final String password) {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        //validate if account already exist
        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!(dataSnapshot.child("Users").child(phoneNumber).exists())){
                    //create new accout
                    //first put data into a hash map
                    HashMap<String, Object> userDataMap = new HashMap<>();
                    userDataMap.put("phone",phoneNumber);
                    userDataMap.put("password",password);
                    userDataMap.put("name",username);

                    RootRef.child("Users").child(phoneNumber).updateChildren(userDataMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(RegisterActivity.this,"Congratulations, Account Created",Toast.LENGTH_SHORT).show();
                                        loadingBar.setVisibility(ProgressBar.INVISIBLE);
                                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                        startActivity(intent);

                                    }
                                    else{
                                        Toast.makeText(RegisterActivity.this,"Network Failure, Please Try Again",Toast.LENGTH_SHORT).show();
                                        loadingBar.setVisibility(ProgressBar.INVISIBLE);
                                    }
                                }
                            });


                }
                else{

                    // phone number already used, pop up error message and turn to MainActivity

                    loadingBar.setVisibility(ProgressBar.INVISIBLE);
                    Toast.makeText(RegisterActivity.this,"Phone: "+phoneNumber+" already used, please login",Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
