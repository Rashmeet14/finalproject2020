package com.cegep.saporiitaliano.auth;

import android.content.Intent;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.telephony.PhoneNumberUtils;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.cegep.saporiitaliano.R;
import com.cegep.saporiitaliano.SaporiItalianoApplication;
import com.cegep.saporiitaliano.main.MainActivity;
import com.cegep.saporiitaliano.model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

@SuppressWarnings("ConstantConditions")
public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        final TextInputEditText nameEditText = findViewById(R.id.name_text);
        final TextInputEditText emailEditText = findViewById(R.id.email_text);
        final TextInputEditText phoneNumberEditText = findViewById(R.id.phone_number_text);
        final TextInputEditText addressEditText = findViewById(R.id.address_text);
        final TextInputEditText passwordEditText = findViewById(R.id.password_text);

        phoneNumberEditText.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

        findViewById(R.id.create_account_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                if (TextUtils.isEmpty(name) || TextUtils.isEmpty(password)) {
                    Toast.makeText(SignUpActivity.this, "Name or Password is empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                final User user = new User();
                user.name = name;
                user.email = emailEditText.getText().toString();
                user.address = addressEditText.getText().toString();
                user.phone = PhoneNumberUtils.normalizeNumber(phoneNumberEditText.getText().toString());
                user.password = password;

                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                DatabaseReference users = databaseReference.child("Users");
                String newUserKey = users.push().getKey();
                user.id = newUserKey;
                databaseReference.child("Users").child(newUserKey).setValue(user)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(SignUpActivity.this, "Account created successfully", Toast.LENGTH_SHORT).show();
                                SaporiItalianoApplication.user = user;
                                Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(SignUpActivity.this, "Failed to create new account", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        findViewById(R.id.cancel_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
