package com.cegep.saporiitaliano.main.home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import com.cegep.saporiitaliano.R;
import com.cegep.saporiitaliano.model.Product;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

@SuppressWarnings("ConstantConditions")
public class AddProductActivity extends Activity {

    private static final String KEY_CATEGORY_ID = "CATEGORY_ID";

    private static final String KEY_CATEGORY_NAME = "CATEGORY_NAME";

    private static final int REQUEST_CODE_CHOOSE_IMAGE = 482;

    private EditText nameEditText;

    private EditText priceEditText;

    private EditText quantityEditText;

    private ImageView productImageView;

    private String categoryName;

    private String selectedImage;
    private String categoryId;

    public static Intent getCallingIntent(Context context, String categoryId, String categoryName) {
        Intent intent = new Intent(context, AddProductActivity.class);
        intent.putExtra(KEY_CATEGORY_ID, categoryId);
        intent.putExtra(KEY_CATEGORY_NAME, categoryName);
        return intent;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        categoryId = getIntent().getStringExtra(KEY_CATEGORY_ID);
        categoryName = getIntent().getStringExtra(KEY_CATEGORY_NAME);

        Toolbar toolbar = findViewById(R.id.toolbar);
        nameEditText = findViewById(R.id.product_name);
        priceEditText = findViewById(R.id.product_price);
        quantityEditText = findViewById(R.id.product_quantity);
        productImageView = findViewById(R.id.product_image);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        findViewById(R.id.choose_image_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Matisse.from(AddProductActivity.this)
                        .choose(MimeType.ofImage())
                        .forResult(REQUEST_CODE_CHOOSE_IMAGE);
            }
        });

        findViewById(R.id.add_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameEditText.getText().toString();
                String quantityText = quantityEditText.getText().toString();
                String priceText = priceEditText.getText().toString();

                if (TextUtils.isEmpty(name) || TextUtils.isEmpty(quantityText) || TextUtils.isEmpty(priceText)) {
                    Toast.makeText(AddProductActivity.this, "Invalid form data", Toast.LENGTH_SHORT).show();
                    return;
                }

                int quantity = 0;
                try {
                    quantity = Integer.parseInt(quantityText);
                } catch (NumberFormatException e) {
                    Toast.makeText(AddProductActivity.this, "Invalid form data", Toast.LENGTH_SHORT).show();
                    return;
                }

                long price = 0;
                try {
                    price = Long.parseLong(priceText);
                } catch (NumberFormatException e) {
                    Toast.makeText(AddProductActivity.this, "Invalid form data", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(selectedImage)) {
                    createProduct(name, price, quantity);
                } else {
                    uploadImage(name, price, quantity);
                }
            }
        });
    }

    private void createProduct(String name, long price, int quantity) {
        createProduct(name, price, quantity, null);
    }

    private void createProduct(String name, long price, int quantity, String filePath) {
        Product product = new Product();
        product.name = name;
        product.price = price;
        product.quantity = quantity;
        product.imageUri = filePath;

        DatabaseReference catDataReference = FirebaseDatabase.getInstance().getReference().child("Category").child(categoryId).child("CatData");
        String newProductKey = catDataReference.push().getKey();
        catDataReference.child(newProductKey).setValue(product)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(AddProductActivity.this, "Product successfully created", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AddProductActivity.this, "Failed to create product", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void uploadImage(String name, long price, int quantity) {
        String selectedImageName = selectedImage.substring(selectedImage.lastIndexOf("/") + 1);
        try {
            InputStream stream = new FileInputStream(new File(selectedImage));
            StorageReference storageReference = FirebaseStorage.getInstance().getReference();
            StorageReference categoryStorageReference = storageReference.child(categoryName);
            final StorageReference imageStorageReference = categoryStorageReference.child(selectedImageName);

            UploadTask uploadTask = imageStorageReference.putStream(stream);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    String path = taskSnapshot.getMetadata().getPath();
                    path.substring(0);
                }
            });
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AddProductActivity.this, "Failed to upload image", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to create product", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHOOSE_IMAGE && resultCode == RESULT_OK) {
            List<String> selected = Matisse.obtainPathResult(data);
            if (selected != null && !selected.isEmpty()) {
                selectedImage = selected.get(0);
                productImageView.setImageURI(Matisse.obtainResult(data).get(0));
            }
        }
    }
}