package com.cegep.saporiitaliano;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cegep.saporiitaliano.auth.SignUpActivity;
import com.cegep.saporiitaliano.main.MainActivity;
import com.cegep.saporiitaliano.model.Product;
import com.cegep.saporiitaliano.product.ProductDetailActivity;
import com.cegep.saporiitaliano.product.ProductListActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminEditItem extends AppCompatActivity {
    private static final String KEY_PRODUCT = "PRODUCT";
    private static final String KEY_CATEGORY_ID = "CATEGORY_ID";
    private EditText name,descripttion,price,quantity,Weight;
    private Button UpdateItem;
    private String categoryId;
    public static Intent getCallingIntent(Context context, Product product, String categorykeyId) {
        Intent intent = new Intent(context, AdminEditItem.class);
        intent.putExtra(KEY_PRODUCT, product);
        intent.putExtra(KEY_CATEGORY_ID, categorykeyId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_edit_item);
        categoryId = getIntent().getStringExtra(KEY_CATEGORY_ID);
        final Product product = getIntent().getParcelableExtra(KEY_PRODUCT);
        name=findViewById(R.id.name);
        descripttion=findViewById(R.id.descripttion);
        price=findViewById(R.id.price);
        quantity=findViewById(R.id.quantity);
        Weight=findViewById(R.id.Weight);
        UpdateItem=findViewById(R.id.UpdateItem);
        UpdateItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
String updatedName=name.getText().toString();
String UpdatedDescription=descripttion.getText().toString();
String UpdatedPrice=price.getText().toString();
String UpdatedQuantity=quantity.getText().toString();
String updateWeight=Weight.getText().toString();

                Product updatedProduct = new Product();
                updatedProduct.name = updatedName;
                updatedProduct.price = Long.valueOf(UpdatedPrice);
                updatedProduct.quantity = Long.valueOf(UpdatedQuantity);
                updatedProduct.imageUri = product.imageUri;
                updatedProduct.description=UpdatedDescription;
                updatedProduct.Weight=updateWeight;
                updatedProduct.CategoryId=categoryId;

                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

                DatabaseReference updatedItem = databaseReference.child("Category").child(categoryId).child("CatData").child(product.key);
              ///  String newUserKey = users.push().getKey();
               // user.id = newUserKey;
                updatedItem.setValue(updatedProduct)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(AdminEditItem.this, "Updated", Toast.LENGTH_SHORT).show();
                                onBackPressed();

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(AdminEditItem.this, "Failed", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
        name.setText(product.name);
        descripttion.setText(product.description);
        price.setText( String.valueOf(product.price));
        quantity.setText(String.valueOf(product.quantity));
        Weight.setText(product.Weight);

        Log.d("www", "onCreate: "+product.Weight);
    }
}