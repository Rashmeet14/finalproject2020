package com.cegep.saporiitaliano.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cegep.saporiitaliano.R;
import com.cegep.saporiitaliano.SaporiItalianoApplication;
import com.cegep.saporiitaliano.model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class ForgetPassword extends AppCompatActivity {

    private EditText user_email_forget,user_password_forget;
    private TextView TV_loginbutton_useremail_forget,usertoggle_forget,TV_loginbutton_user_forget,TV_loginbutton_user_forget_cancel;
    Boolean flag=false;
    private ImageView IV_back_forget;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        user_email_forget=findViewById(R.id.user_email_forget);
        IV_back_forget=findViewById(R.id.IV_back_forget);
        TV_loginbutton_user_forget_cancel=findViewById(R.id.TV_loginbutton_user_forget_cancel);
        usertoggle_forget=findViewById(R.id.usertoggle_forget);
        user_password_forget=findViewById(R.id.user_password_forget);
        TV_loginbutton_useremail_forget=findViewById(R.id.TV_loginbutton_useremail_forget);
        TV_loginbutton_user_forget=findViewById(R.id.TV_loginbutton_user_forget);


        IV_back_forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent profile = new Intent(getApplicationContext(),SignInActivity.class);
                startActivity(profile);
                finish();
            }
        });
        TV_loginbutton_user_forget_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent profile = new Intent(getApplicationContext(),SignInActivity.class);
                startActivity(profile);
                finish();
            }
        });

        TV_loginbutton_user_forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newPassword=user_password_forget.getText().toString();

                HashMap<String, Object> result = new HashMap<>();
                result.put("password",newPassword );



                //DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                //DatabaseReference users = databaseReference.child("Users").child(SaveLoginUser.user.id).child("password");

                FirebaseDatabase.getInstance().getReference().child("Users").child(SaporiItalianoApplication.user.id).updateChildren(result)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                Toast.makeText(ForgetPassword.this, "User Updated", Toast.LENGTH_SHORT).show();
                                // SaporiItalianoApplication.user = user;
                                Intent intent = new Intent(ForgetPassword.this, ForgetPassword.class);
                                startActivity(intent);
                                finish();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(ForgetPassword.this, "Failed !!", Toast.LENGTH_SHORT).show();
                            }
                        });

            }
        });
        TV_loginbutton_useremail_forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String  EmailValue=user_email_forget.getText().toString();
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                Query query = databaseReference.child("Users");
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            User user = snapshot.getValue(User.class);
                            user.id = snapshot.getKey();
                            if (EmailValue.equals(user.email)) {
                                SaporiItalianoApplication.user=user;
                                flag=true;
                                TV_loginbutton_useremail_forget.setVisibility(View.GONE);
                                usertoggle_forget.setVisibility(View.VISIBLE);
                                user_password_forget.setVisibility(View.VISIBLE);
                                TV_loginbutton_user_forget.setVisibility(View.VISIBLE);
                                TV_loginbutton_user_forget_cancel.setVisibility(View.VISIBLE);

                            }


                        }
                        if (flag==false) {
                            Toast.makeText(ForgetPassword.this, "Incorrect email", Toast.LENGTH_SHORT).show();
                        }



                    }




                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(ForgetPassword.this, "Failed to load login details", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


    }
}