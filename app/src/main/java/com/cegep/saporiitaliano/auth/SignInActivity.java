package com.cegep.saporiitaliano.auth;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.cegep.saporiitaliano.R;
import com.cegep.saporiitaliano.SaporiItalianoApplication;
import com.cegep.saporiitaliano.main.MainActivity;
import com.cegep.saporiitaliano.model.User;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

@SuppressWarnings("ConstantConditions")
public class SignInActivity extends AppCompatActivity {

    private TextView forgetPassword;
    public String toogleValue="Client";
    private TextInputEditText usernameEditText,passwordEditText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        final String client = getString(R.string.client);
        final String clientName = getString(R.string.client_name);
        final String adminName = getString(R.string.admin_name);

        final TextInputLayout usernameInputLayout = findViewById(R.id.username_input);
        forgetPassword=(TextView) findViewById(R.id.forgetPassword);
        TabLayout tabLayout = findViewById(R.id.tabs);

         usernameEditText = findViewById(R.id.username_text);
        passwordEditText = findViewById(R.id.password_text);

        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignInActivity.this, ForgetPassword.class);
                startActivity(intent);
                finish();
            }
        });

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                usernameInputLayout.setHint(client.equals(tab.getText().toString()) ? clientName : adminName);
               // Log.d("testt",tab.getText().toString());
                usernameEditText.setText("");
                passwordEditText.setText("");
                toogleValue=tab.getText().toString();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        findViewById(R.id.sign_in_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String username = usernameEditText.getText().toString();
                final String password = passwordEditText.getText().toString();
                if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
                    Toast.makeText(SignInActivity.this, "Username or Password is empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (toogleValue.equals("Client") && username.equals("admin@gmail.com")) {
                    Toast.makeText(SignInActivity.this, "Can't Login Admin from User Section", Toast.LENGTH_SHORT).show();
                    return;
                }


                if (toogleValue.equals("Admin") && !username.equals("admin@gmail.com")) {
                    Toast.makeText(SignInActivity.this, "Can't Login User from Admin Section", Toast.LENGTH_SHORT).show();
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
                            if (username.equals(user.email) && password.equals(user.password)) {
                                SaporiItalianoApplication.user = user;
                            }
                        }

                        if (SaporiItalianoApplication.user == null) {
                            Toast.makeText(SignInActivity.this, "Incorrect username or password", Toast.LENGTH_SHORT).show();
                        } else {
                            if ("admin@gmail.com".equals(username)) {
                                SaporiItalianoApplication.user.isAdmin = true;
                            }

                            Toast.makeText(SignInActivity.this, "Successfully logged in", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(SignInActivity.this, "Failed to load login details", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        findViewById(R.id.sign_up_text).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
                intent.putExtra("dataLoded", "SIGNUP");
                startActivity(intent);
            }
        });
    }
}