package com.cegep.saporiitaliano.main.home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import com.cegep.saporiitaliano.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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

public class AddProductActivity extends Activity {

    private static final String KEY_CATEGORY_NAME = "CATEGORY_NAME";

    private static final int REQUEST_CODE_CHOOSE_IMAGE = 482;

    private Toolbar toolbar;

    private EditText nameEditText;

    private EditText priceEditText;

    private EditText quantityEditText;

    private ImageView productImageView;

    private Button chooseImageButton;

    private Button addButton;
    private String categoryName;

    public static Intent getCallingIntent(Context context, String categoryName) {
        Intent intent = new Intent(context, AddProductActivity.class);
        intent.putExtra(KEY_CATEGORY_NAME, categoryName);
        return intent;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        categoryName = getIntent().getStringExtra(KEY_CATEGORY_NAME);

        toolbar = findViewById(R.id.toolbar);
        nameEditText = findViewById(R.id.product_name);
        priceEditText = findViewById(R.id.product_price);
        quantityEditText = findViewById(R.id.product_quantity);
        productImageView = findViewById(R.id.product_image);
        chooseImageButton = findViewById(R.id.choose_image_button);
        addButton = findViewById(R.id.add_button);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        chooseImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Matisse.from(AddProductActivity.this)
                        .choose(MimeType.ofImage())
                        .forResult(REQUEST_CODE_CHOOSE_IMAGE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHOOSE_IMAGE && resultCode == RESULT_OK) {
            List<String> selected = Matisse.obtainPathResult(data);
            if (selected != null && !selected.isEmpty()) {
                try {
                    String selectedImage = selected.get(0);
                    String selectedImageName = selectedImage.substring(selectedImage.lastIndexOf("/") + 1);
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
                }
            }
        }
    }
}