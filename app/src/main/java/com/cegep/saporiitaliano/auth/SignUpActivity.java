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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

@SuppressWarnings("ConstantConditions")
public class SignUpActivity extends AppCompatActivity {

    private TextInputEditText emailEditText;
    private TextInputEditText addressEditText;
    private TextInputEditText phoneNumberEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        final TextInputEditText nameEditText = findViewById(R.id.name_text);
        emailEditText = findViewById(R.id.email_text);
        final TextInputEditText confirmEmailEditText = findViewById(R.id.confirm_email_text);
        phoneNumberEditText = findViewById(R.id.phone_number_text);
        addressEditText = findViewById(R.id.address_text);
        final TextInputEditText passwordEditText = findViewById(R.id.password_text);
        final TextInputEditText confirmPasswordEditText = findViewById(R.id.confirm_password_text);

        phoneNumberEditText.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

        findViewById(R.id.create_account_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name = nameEditText.getText().toString();
                final String password = passwordEditText.getText().toString();

                if (TextUtils.isEmpty(name) || TextUtils.isEmpty(password)) {
                    Toast.makeText(SignUpActivity.this, "Name or Password is empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                String email = emailEditText.getText().toString();
                String confirmEmail = confirmEmailEditText.getText().toString();
                if (!email.equals(confirmEmail)) {
                    Toast.makeText(SignUpActivity.this, "Email and confirmed email are not same", Toast.LENGTH_SHORT).show();
                    return;
                }

                String confirmPassword = confirmPasswordEditText.getText().toString();
                if (!password.equals(confirmPassword)) {
                    Toast.makeText(SignUpActivity.this, "Password and confirm password are not same", Toast.LENGTH_SHORT).show();
                    return;
                }

                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                Query query = databaseReference.child("Users");
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            User user = snapshot.getValue(User.class);
                            user.id = snapshot.getKey();
                            if (name.equals(user.name)) {
                                Toast.makeText(SignUpActivity.this, "User already exist. Please choose other usernamel", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }

                        createNewUser(name, password);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
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

    private void createNewUser(String name, String password) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        final User user = new User();
        user.name = name;
        user.email = emailEditText.getText().toString();
        user.address = addressEditText.getText().toString();
        user.phone = PhoneNumberUtils.normalizeNumber(phoneNumberEditText.getText().toString());
        user.password = password;

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
}
