package com.cegep.saporiitaliano.product;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.bumptech.glide.Glide;
import com.cegep.saporiitaliano.R;
import com.cegep.saporiitaliano.SaporiItalianoApplication;
import com.cegep.saporiitaliano.model.Product;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("ConstantConditions")
public class ProductDetailActivity extends AppCompatActivity {

    private static final String KEY_PRODUCT = "PRODUCT";
    private static final String KEY_CATEGORY_ID = "CATEGORY_ID";
private TextView stockValue,itemWeight;
    private int quantity = 1;
    int StockValue;
    private String categoryId;
    public static Intent getCallingIntent(Context context, Product product, String categorykeyId) {
        Intent intent = new Intent(context, ProductDetailActivity.class);
        intent.putExtra(KEY_PRODUCT, product);
        intent.putExtra(KEY_CATEGORY_ID, categorykeyId);
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
        categoryId = getIntent().getStringExtra(KEY_CATEGORY_ID);
        final Product product = getIntent().getParcelableExtra(KEY_PRODUCT);
        stockValue=findViewById(R.id.stockValue);
        itemWeight=findViewById(R.id.itemWeight);
        final ImageView productImageView = findViewById(R.id.product_image);
        final TextView productNameTextView = findViewById(R.id.product_name);
        final TextView productPriceTextView = findViewById(R.id.product_price);
        final TextView productDescriptionTextView = findViewById(R.id.product_description);

        Glide.with(ProductDetailActivity.this)
                .load(product.imageUri)
                .into(productImageView);

        productNameTextView.setText(product.name);
        itemWeight.setText(product.Weight);
        productPriceTextView.setText("$ " + product.price);
        productDescriptionTextView.setText(product.description);
       stockValue.setText(String.valueOf(product.quantity));
        Log.d("quantity", String.valueOf(product.quantity));
        toolbar.setTitle(product.name);

        final TextView quantityTextView = findViewById(R.id.quantity_text);
        final TextView totalPriceTextView = findViewById(R.id.total_price_text);

        quantityTextView.setText(String.valueOf(1));
        totalPriceTextView.setText("$ " + product.price);
        findViewById(R.id.add_item_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(quantity==product.quantity){
                    Toast.makeText(ProductDetailActivity.this, "You have crossed the Stock Limit", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(quantity==10){
                    Toast.makeText(ProductDetailActivity.this, "You can't add more then 10", Toast.LENGTH_SHORT).show();
                    return;
                }
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

                if(product.quantity==0){
                    Toast.makeText(ProductDetailActivity.this, "Stock is empty", Toast.LENGTH_SHORT).show();
                    return;
                }


                for(int i=0;i<SaporiItalianoApplication.products.size();i++){
                    if(SaporiItalianoApplication.products.get(i).key.equals(product.key)){
                        Log.d("hgghgh", "onClick: hnnkn");
                        Toast.makeText(ProductDetailActivity.this, "Item is Already Added in the cart", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                StockValue= (int) product.quantity;
                product.quantity = quantity;
                product.CategoryId=categoryId;
                int newquant= (int) (StockValue+product.quantity);
                product.stockValue=StockValue;
                SaporiItalianoApplication.products.add(product);
                deleteQuantityFromFirebase();
                finish();
            }

            private void deleteQuantityFromFirebase() {
                Log.d("KEYID",product.key);

                HashMap<String, Object> result = new HashMap<>();
                int newquantity= (int) (StockValue-product.quantity);
                result.put("quantity",newquantity);

                FirebaseDatabase.getInstance().getReference().child("Category").child(categoryId).child("CatData").child(product.key).updateChildren(result).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });

            }
        });

    }
}
