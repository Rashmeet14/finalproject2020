package com.cegep.saporiitaliano.product;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.cegep.saporiitaliano.R;
import com.cegep.saporiitaliano.model.Product;

class ProductViewHolder extends RecyclerView.ViewHolder {

    private ImageView productImageView;

    private TextView productNameTextView;

    private TextView productPriceTextView;

    ProductViewHolder(@NonNull View itemView) {
        super(itemView);

        productImageView = itemView.findViewById(R.id.product_image);
        productNameTextView = itemView.findViewById(R.id.product_name);
        productPriceTextView = itemView.findViewById(R.id.product_price);
    }

    @SuppressLint("SetTextI18n")
    void bind(Product product) {
        Glide.with(productImageView)
                .load(product.imageUri)
                .into(productImageView);
        productNameTextView.setText(product.name);
        productPriceTextView.setText("$" + product.price);
    }
}
