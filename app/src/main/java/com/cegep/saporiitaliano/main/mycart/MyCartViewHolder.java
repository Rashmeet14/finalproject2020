package com.cegep.saporiitaliano.main.mycart;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.cegep.saporiitaliano.R;
import com.cegep.saporiitaliano.model.Product;

class MyCartViewHolder extends RecyclerView.ViewHolder {

    private ImageView productImageView;

    private TextView productNameTextView;

    private TextView productPriceTextView;

    private TextView productTotalPriceTextView;

    MyCartViewHolder(@NonNull View itemView) {
        super(itemView);

        productNameTextView = itemView.findViewById(R.id.product_name);
        productImageView = itemView.findViewById(R.id.product_image);
        productPriceTextView = itemView.findViewById(R.id.product_price);
        productTotalPriceTextView = itemView.findViewById(R.id.product_total_price);

    }

    @SuppressLint("SetTextI18n")
    void bind(Product product) {
        Glide.with(itemView)
                .load(product.imageUri)
                .into(productImageView);

        productNameTextView.setText(product.name);
        productPriceTextView.setText("$ " + product.price);
        productTotalPriceTextView.setText("$ " + product.quantity * product.price);
    }
}
