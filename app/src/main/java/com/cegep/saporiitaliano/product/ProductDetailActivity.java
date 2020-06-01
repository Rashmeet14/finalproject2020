package com.cegep.saporiitaliano.product;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.bumptech.glide.Glide;
import com.cegep.saporiitaliano.R;
import com.cegep.saporiitaliano.SaporiItalianoApplication;
import com.cegep.saporiitaliano.model.Product;

@SuppressWarnings("ConstantConditions")
public class ProductDetailActivity extends AppCompatActivity {

    private static final String KEY_PRODUCT = "PRODUCT";

    private int quantity = 1;

    public static Intent getCallingIntent(Context context, Product product) {
        Intent intent = new Intent(context, ProductDetailActivity.class);
        intent.putExtra(KEY_PRODUCT, product);
        return intent;
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!SaporiItalianoApplication.user.isAdmin) {
            setTheme(R.style.AppTheme_Dark);
        }
        setContentView(R.layout.activity_product_detail);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        final Product product = getIntent().getParcelableExtra(KEY_PRODUCT);

        final ImageView productImageView = findViewById(R.id.product_image);
        final TextView productNameTextView = findViewById(R.id.product_name);
        final TextView productPriceTextView = findViewById(R.id.product_price);

        Glide.with(ProductDetailActivity.this)
                .load(product.imageUri)
                .into(productImageView);

        productNameTextView.setText(product.name);
        productPriceTextView.setText("$ " + product.price);
        toolbar.setTitle(product.name);

        final TextView quantityTextView = findViewById(R.id.quantity_text);
        final TextView totalPriceTextView = findViewById(R.id.total_price_text);

        quantityTextView.setText(String.valueOf(1));
        totalPriceTextView.setText("$ " + product.price);
        findViewById(R.id.add_item_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quantity += 1;
                quantityTextView.setText(String.valueOf(quantity));
                totalPriceTextView.setText("$ " + quantity * product.price);
            }
        });

        findViewById(R.id.remove_item_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quantity = Math.max(1, quantity - 1);
                quantityTextView.setText(String.valueOf(quantity));
                totalPriceTextView.setText("$ " + quantity * product.price);
            }
        });

        findViewById(R.id.add_to_cart_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                product.quantity = quantity;
                SaporiItalianoApplication.products.add(product);
                finish();
            }
        });
    }
}
