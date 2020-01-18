package com.libangliang.supermarket;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class AdminCategoryActivity extends AppCompatActivity {

    private ImageView fruitAndVege,meatAndFish, dairyAndEgg, bakery;
    private ImageView frozen, drinks, household, beauty;
    private ImageView toiletries, homeware, baby, pet;

    @Override
    public void onBackPressed() {
        //Disable back button
        // Do Here what ever you want do on back press;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_category);

        fruitAndVege = findViewById(R.id.fruit_and_vegetable_pic);
        meatAndFish = findViewById(R.id.meat_and_fish_pic);
        dairyAndEgg = findViewById(R.id.diary_and_egg_pic);
        bakery = findViewById(R.id.bakery_pic);

        frozen = findViewById(R.id.frozen_pic);
        drinks = findViewById(R.id.drinks_pic);
        household = findViewById(R.id.household_pic);
        beauty = findViewById(R.id.beauty_pic);

        toiletries = findViewById(R.id.toiletries_and_health_pic);
        homeware = findViewById(R.id.homeware_pic);
        baby = findViewById(R.id.baby_pic);
        pet = findViewById(R.id.pet_pic);







        fruitAndVege.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this,AdminAddNewProductActivity.class);
                intent.putExtra("category","fruitAndVege");
                startActivity(intent);
            }
        });

        meatAndFish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this,AdminAddNewProductActivity.class);
                intent.putExtra("category","meatAndFish");
                startActivity(intent);
            }
        });

        dairyAndEgg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this,AdminAddNewProductActivity.class);
                intent.putExtra("category","dairyAndEgg");
                startActivity(intent);
            }
        });

        bakery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this,AdminAddNewProductActivity.class);
                intent.putExtra("category","bakery");
                startActivity(intent);
            }
        });

        frozen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this,AdminAddNewProductActivity.class);
                intent.putExtra("category","frozen");
                startActivity(intent);
            }
        });

        drinks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this,AdminAddNewProductActivity.class);
                intent.putExtra("category","drinks");
                startActivity(intent);
            }
        });

        household.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this,AdminAddNewProductActivity.class);
                intent.putExtra("category","household");
                startActivity(intent);
            }
        });

        beauty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this,AdminAddNewProductActivity.class);
                intent.putExtra("category","beauty");
                startActivity(intent);
            }
        });

        toiletries.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this,AdminAddNewProductActivity.class);
                intent.putExtra("category","toiletries");
                startActivity(intent);
            }
        });

        homeware.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this,AdminAddNewProductActivity.class);
                intent.putExtra("category","homeware");
                startActivity(intent);
            }
        });

        baby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this,AdminAddNewProductActivity.class);
                intent.putExtra("category","baby");
                startActivity(intent);
            }
        });

        pet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this,AdminAddNewProductActivity.class);
                intent.putExtra("category","pet");
                startActivity(intent);
            }
        });
    }
}
