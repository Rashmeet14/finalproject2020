package com.cegep.saporiitaliano.product;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.cegep.saporiitaliano.AdminEditItem;
import com.cegep.saporiitaliano.R;
import com.cegep.saporiitaliano.SaporiItalianoApplication;
import com.cegep.saporiitaliano.main.home.AddProductActivity;
import com.cegep.saporiitaliano.model.Product;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("ConstantConditions")
public class ProductListActivity extends AppCompatActivity implements ProductItemClickListener<Product> {

    private static final String KEY_CATEGORY_DATA = "CATEGORY_DATA";
    private static final String KEY_CATEGORY_NAME = "CATEGORY_NAME";
public  String categorykeyId;
    public static Intent getCallingIntent(Context context, String categoryKey, String categoryName) {
        Intent intent = new Intent(context, ProductListActivity.class);
        intent.putExtra(KEY_CATEGORY_DATA, categoryKey);
        intent.putExtra(KEY_CATEGORY_NAME, categoryName);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!SaporiItalianoApplication.user.isAdmin) {
            setTheme(R.style.AppTheme_Dark);
        }
        setContentView(R.layout.activity_product_list);

        final String categoryName = getIntent().getStringExtra(KEY_CATEGORY_NAME);
        final String categoryId = getIntent().getStringExtra(KEY_CATEGORY_DATA);
         categorykeyId=categoryId;
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        toolbar.setTitle(categoryName);

        if (SaporiItalianoApplication.user.isAdmin) {
            toolbar.inflateMenu(R.menu.menu_product_list);
            toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    if (item.getItemId() == R.id.menu_add_product) {
                        Intent intent = AddProductActivity.getCallingIntent(ProductListActivity.this, categoryId, categoryName);
                        startActivity(intent);
                        return true;
                    }

                    return false;
                }
            });
        }

        final RecyclerView productsList = findViewById(R.id.recycler_view);
        productsList.setHasFixedSize(true);

        String categoryKey = getIntent().getStringExtra(KEY_CATEGORY_DATA);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        DatabaseReference productsReference = databaseReference.child("Category").child(categoryKey).child("CatData");
        productsReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Product> products = new ArrayList<>();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    Product product = child.getValue(Product.class);
                    product.key = child.getKey();
                    products.add(product);
                }

                productsList.setAdapter(new ProductAdapter(products, ProductListActivity.this));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ProductListActivity.this, "Failed to fetch products list", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDeleteIconClicked(Product product, int position) {
        String categoryKey = getIntent().getStringExtra(KEY_CATEGORY_DATA);
        DatabaseReference productReference = FirebaseDatabase.getInstance().getReference().child("Category").child(categoryKey).child("CatData")
                .child(product.key);
        productReference.removeValue();
    }

    @Override
    public void onItemClick(Product product, int position) {
        if (!SaporiItalianoApplication.user.isAdmin) {
            Log.d("login", "In client");
            startActivity(ProductDetailActivity.getCallingIntent(this, product,categorykeyId));
        }
         if(SaporiItalianoApplication.user.email.equals("admin@gmail.com")){
             Log.d("login", "In admin");
             Intent intent = AdminEditItem.getCallingIntent(ProductListActivity.this, product,categorykeyId);
             startActivity(intent);

            //startActivity(AdminEditItem.getCallingIntent(this, product,categorykeyId));
        }
    }
}
