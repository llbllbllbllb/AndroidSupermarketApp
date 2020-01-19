package com.libangliang.supermarket;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class AdminAddNewProductActivity extends AppCompatActivity {

    //receive the category name here
    private String categoryName, productDescription, productName, productPrice, saveCurrentDate, saveCurrentTime;

    private ImageView inputProductImage;
    private Button addNewProductButton;
    private EditText inputProductName, inputProductDescription, inputProductPrice;

    public final static int PICK_IMAGE = 1;

    private Uri imageUri;

    private String productRandomKey, downloadImageURL;
    //firebase storage
    private StorageReference productImageRef;

    private DatabaseReference productRef;

    private ProgressBar progressBar;

    private TextView select_image_text;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_new_product);

        //receive the category name here
        categoryName = getIntent().getExtras().get("category").toString();

        //folder name: ProductImages
        productImageRef = FirebaseStorage.getInstance().getReference().child("ProductImages");

        productRef = FirebaseDatabase.getInstance().getReference().child("Products");

        select_image_text = findViewById(R.id.select_image_text);
        progressBar = findViewById(R.id.add_new_product_progressBar);
        addNewProductButton = findViewById(R.id.add_new_product);
        inputProductImage = findViewById(R.id.select_product_image);
        inputProductName = findViewById(R.id.product_name);
        inputProductDescription = findViewById(R.id.product_description);
        inputProductPrice = findViewById(R.id.product_price);

        //allow select image
        inputProductImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenGallery();
            }
        });


        addNewProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateProduct();
            }
        });



    }

    private void validateProduct() {
        productDescription = inputProductDescription.getText().toString();
        productPrice = inputProductPrice.getText().toString();
        productName = inputProductName.getText().toString();

        if(imageUri == null){
            //user not select image
            Toast.makeText(this,"Product Image not selected",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(productDescription)){
            Toast.makeText(this,"Empty Product Description",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(productName)){
            Toast.makeText(this,"Empty Product Name",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(productPrice)){
            Toast.makeText(this,"Empty Product Price",Toast.LENGTH_SHORT).show();
        }
        else{
            //all verified content proceed to add item.
            StoreProductInfo();
        }
    }

    private void StoreProductInfo() {

        progressBar.setVisibility(progressBar.VISIBLE);

        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MM dd, yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());

        //combine date and time to create unique name for url
        productRandomKey = saveCurrentDate+saveCurrentTime;
        final StorageReference filePath = productImageRef.child(imageUri.getLastPathSegment()+productRandomKey+".jpg");

        final UploadTask uploadTask = filePath.putFile(imageUri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                String message = e.toString();
                progressBar.setVisibility(progressBar.INVISIBLE);
                Toast.makeText(AdminAddNewProductActivity.this,"Error: " + message, Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(AdminAddNewProductActivity.this,"Image Uploaded Successfully", Toast.LENGTH_SHORT).show();

                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if(!task.isSuccessful()){
                            throw task.getException();
                        }

                        //get image url
                        downloadImageURL = filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if(task.isSuccessful()){

                            Toast.makeText(AdminAddNewProductActivity.this,"Getting Product Image URL Successfully",Toast.LENGTH_SHORT).show();
                            saveProductInfoToDatabase();
                        }

                    }
                });
            }
        });

    }

    private void OpenGallery() {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        //function below create new activity with a return code
        startActivityForResult(Intent.createChooser(galleryIntent,"Select Product"),PICK_IMAGE);

    }

    private void saveProductInfoToDatabase(){
        HashMap<String,Object>  productMap = new HashMap<>();
        productMap.put("pid",productRandomKey);
        productMap.put("date",saveCurrentDate);
        productMap.put("time",saveCurrentTime);
        productMap.put("description",productDescription);
        productMap.put("image",downloadImageURL);
        productMap.put("category",categoryName);
        productMap.put("price",productPrice);
        productMap.put("name",productName);

        productRef.child(productRandomKey).updateChildren(productMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    progressBar.setVisibility(progressBar.INVISIBLE);
                    Toast.makeText(AdminAddNewProductActivity.this,"Product Added",Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(AdminAddNewProductActivity.this,AdminCategoryActivity.class);
                    startActivity(intent);
                }
                else{
                    progressBar.setVisibility(progressBar.INVISIBLE);
                    String message = task.getException().toString();
                    Toast.makeText(AdminAddNewProductActivity.this,"Error: "+message,Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //check select image activity functions well by checking these three params
        if(requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null){
            //retrieve selected image uri
            imageUri = data.getData();
            //set the image view to selected image
            inputProductImage.setImageURI(imageUri);

            select_image_text.setVisibility(TextView.INVISIBLE);
        }
    }
}
