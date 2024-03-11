package com.example.amazonclone.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.amazonclone.Interfaces.ItemClickListener;
import com.example.amazonclone.R;

public class ViewProductsHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView addProductName, addProductPrice;
    private ItemClickListener itemClickListener;
    public ImageView addProductImg;
    public ViewProductsHolder(@NonNull View itemView) {
        super(itemView);

        addProductName = itemView.findViewById(R.id.prod_name);
        addProductPrice = itemView.findViewById(R.id.prod_price);
        addProductImg = itemView.findViewById(R.id.prod_image);
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view, getAdapterPosition(),false);
    }

    public void setItemClickListener(ItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }
}
