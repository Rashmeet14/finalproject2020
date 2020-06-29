package com.cegep.saporiitaliano.main.home;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.cegep.saporiitaliano.ApiInterface;
import com.cegep.saporiitaliano.R;
import com.cegep.saporiitaliano.SearchJob;
import com.cegep.saporiitaliano.auth.SignUpActivity;
import com.cegep.saporiitaliano.model.Product;
import com.cegep.saporiitaliano.model.PushNotification;
import com.cegep.saporiitaliano.model.User;
import com.cegep.saporiitaliano.product.ProductAdapter;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@SuppressWarnings("ConstantConditions")
public class AddProductActivity extends Activity {

    private static final String KEY_CATEGORY_ID = "CATEGORY_ID";

    private static final String KEY_CATEGORY_NAME = "CATEGORY_NAME";

    private static final int REQUEST_CODE_CHOOSE_IMAGE = 482;

    private static final int PERMISSION_REQUEST_CODE = 9843;
    ArrayList<Product> products;
    private EditText nameEditText;

    private EditText priceEditText,product_description,product_Weight;

    private EditText quantityEditText;

    private ImageView productImageView;
public String flag="false";
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
        product_description=findViewById(R.id.product_description);
        product_Weight=findViewById(R.id.product_Weight);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        findViewById(R.id.choose_image_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(AddProductActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_GRANTED) {
                        chooseImage();
                    } else {
                        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
                    }
                } else {
                    chooseImage();
                }
            }
        });

        findViewById(R.id.add_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name = nameEditText.getText().toString();
                final String quantityText = quantityEditText.getText().toString();
                final String priceText = priceEditText.getText().toString();
                final String desp=product_description.getText().toString();
                final String Weight=product_Weight.getText().toString();
flag="false";

                if (TextUtils.isEmpty(name) || TextUtils.isEmpty(quantityText) || TextUtils.isEmpty(priceText)||TextUtils.isEmpty(desp)) {
                    Toast.makeText(AddProductActivity.this, "Invalid form data", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(priceText.equals("0")){
                    Toast.makeText(AddProductActivity.this, "Price can't be zero", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(quantityText.equals("0")){
                    Toast.makeText(AddProductActivity.this, "Quantity can't be zero", Toast.LENGTH_SHORT).show();
                    return;
                }
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                Query query = databaseReference.child("Category");
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                       // products = new ArrayList<>();
                        for (DataSnapshot mainRoot : dataSnapshot.getChildren()) {
                            for(DataSnapshot child : mainRoot.child("CatData").getChildren()){
                                Product product = child.getValue(Product.class);
                              if(product.name.toLowerCase().equals(name.toLowerCase())){
                                  Toast.makeText(AddProductActivity.this, "Item Already Exist", Toast.LENGTH_SHORT).show();
                                  flag="true";
                                  return;
                              }
                            }

                        }
                        if(flag.equals("false")) {
                            try {
                                int quantity = Integer.parseInt(quantityText);
                                long price = Long.parseLong(priceText);
                                if (TextUtils.isEmpty(selectedImage)) {
                                    createProduct(name, price, quantity, desp, Weight);
                                } else {
                                    uploadImage(name, price, quantity, desp, Weight);
                                }
                            } catch (NumberFormatException e) {
                                Toast.makeText(AddProductActivity.this, "Invalid form data", Toast.LENGTH_SHORT).show();
                            }
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(AddProductActivity.this, "Failed to create new account", Toast.LENGTH_SHORT).show();
                    }
                });


























            }
        });
    }

    private void chooseImage() {
        Matisse.from(AddProductActivity.this)
                .choose(MimeType.ofImage())
                .countable(false)
                .forResult(REQUEST_CODE_CHOOSE_IMAGE);
    }

    private void createProduct(String name, long price, int quantity,String desp,String Weight) {
        createProduct(name, price, quantity, null,desp,Weight);
    }

    private void createProduct(String name, long price, int quantity, String filePath,String desp,String Weight) {
        Product product = new Product();
        product.name = name;
        product.price = price;
        product.quantity = quantity;
        product.imageUri = filePath;
        product.CategoryId=categoryId;
        product.description=desp;
        product.Weight=Weight;
        DatabaseReference catDataReference = FirebaseDatabase.getInstance().getReference().child("Category").child(categoryId).child("CatData");
        String newProductKey = catDataReference.push().getKey();
        catDataReference.child(newProductKey).setValue(product)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        Gson gson = new GsonBuilder().setLenient().create();
                        OkHttpClient client = new OkHttpClient();
                        Retrofit retrofit = new Retrofit.Builder()
                                .baseUrl("https://inventorycontrol-7587a.web.app/")
                                .client(client)
                                .addConverterFactory(GsonConverterFactory.create(gson))
                                .build();
                        ApiInterface service = retrofit.create(ApiInterface.class);
                        Call<PushNotification> call = service.loginUser();
                        call.enqueue(new Callback<PushNotification>() {
                            @Override
                            public void onResponse(Call<PushNotification> call, Response<PushNotification> response) {

                                if (response.body() != null && response.isSuccessful()) {

                                    Toast.makeText(AddProductActivity.this, response.body().getStatus(), Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                                else
                                {
                                    Toast.makeText(AddProductActivity.this, "No Response from Server!", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<PushNotification> call, Throwable t) {
                                Toast.makeText(AddProductActivity.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AddProductActivity.this, "Failed to create product", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void uploadImage(final String name, final long price, final int quantity, final String desp,final String Weight) {
        String selectedImageName = selectedImage.substring(selectedImage.lastIndexOf("/") + 1);
        try {
            InputStream stream = new FileInputStream(new File(selectedImage));
            StorageReference storageReference = FirebaseStorage.getInstance().getReference();
            StorageReference categoryStorageReference = storageReference.child(categoryName);
            final StorageReference imageStorageReference = categoryStorageReference.child(selectedImageName);

            UploadTask uploadTask = imageStorageReference.putStream(stream);
            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    return imageStorageReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        createProduct(name, price, quantity, task.getResult().toString(),desp,Weight);
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                chooseImage();
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
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