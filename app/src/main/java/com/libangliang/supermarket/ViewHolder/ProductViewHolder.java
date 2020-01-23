package com.libangliang.supermarket.ViewHolder;


import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.libangliang.supermarket.Interface.ItemClickListener;
import com.libangliang.supermarket.R;


//Learning note: 2020.1.21
//extends allow this class to use all function from RecyclerView.ViewHolder
//implements allow to access antother class function
public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView cardProductName, cardProductDescription, cardProductPrice;
    public ImageView cardProductImage;

    public ItemClickListener listener;

    public ProductViewHolder(@NonNull View itemView) {
        super(itemView);

        //in constructor function, link the xml items
        cardProductImage = itemView.findViewById(R.id.card_product_image);
        cardProductName = itemView.findViewById(R.id.card_product_name);
        cardProductDescription = itemView.findViewById(R.id.card_product_description);
        cardProductPrice = itemView.findViewById(R.id.card_product_price);

    }

    public void setItemClickListener(ItemClickListener listener){
        this.listener = listener;
    }
    @Override
    public void onClick(View v) {
        listener.onClick(v,getAdapterPosition(),false);
    }
}
