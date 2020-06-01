package com.cegep.saporiitaliano.product;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.cegep.saporiitaliano.R;
import com.cegep.saporiitaliano.SaporiItalianoApplication;
import com.cegep.saporiitaliano.model.Product;

class ProductViewHolder extends RecyclerView.ViewHolder {

    private ImageView productImageView;

    private TextView productNameTextView;

    private TextView productPriceTextView;

    private Product product;

    ProductViewHolder(@NonNull View itemView, final ProductItemClickListener<Product> itemClickListener) {
        super(itemView);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener.onItemClick(product, getAdapterPosition());
            }
        });

        ImageButton deleteButton = itemView.findViewById(R.id.delete_product_button);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener.onDeleteIconClicked(product, getAdapterPosition());
            }
        });
        deleteButton.setVisibility(SaporiItalianoApplication.user.isAdmin ? View.VISIBLE : View.GONE);

        productImageView = itemView.findViewById(R.id.product_image);
        productNameTextView = itemView.findViewById(R.id.product_name);
        productPriceTextView = itemView.findViewById(R.id.product_price);
    }

    @SuppressLint("SetTextI18n")
    void bind(Product product) {
        this.product = product;
        Glide.with(productImageView)
                .load(product.imageUri)
                .into(productImageView);
        productNameTextView.setText(product.name);
        productPriceTextView.setText("$" + product.price);
    }
}
