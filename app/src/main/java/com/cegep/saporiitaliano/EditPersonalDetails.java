package com.cegep.saporiitaliano;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cegep.saporiitaliano.auth.SignUpActivity;
import com.cegep.saporiitaliano.main.MainActivity;
import com.cegep.saporiitaliano.model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class EditPersonalDetails extends AppCompatActivity {

    private Button UpdateUser;
    private EditText personalname,peronalEmail,personalPhone;
    private EditText personalStreet,personalApartment,personalCity,personalState,personalZip,personalCountry;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_personal_details);
        personalname=findViewById(R.id.personalname);
       // peronalEmail=findViewById(R.id.peronalEmail);
        personalStreet=findViewById(R.id.personalStreet);
        personalApartment=findViewById(R.id.personalApartment);
        personalCity=findViewById(R.id.personalCity);
        personalState=findViewById(R.id.personalState);
        personalZip=findViewById(R.id.personalZip);
        personalCountry=findViewById(R.id.personalCountry);



        personalPhone=findViewById(R.id.personalPhone);

        personalname.setText(SaporiItalianoApplication.user.name);

        personalStreet.setText(SaporiItalianoApplication.user.addressOne);
        personalApartment.setText(SaporiItalianoApplication.user.addressTwo);
        personalCity.setText(SaporiItalianoApplication.user.city);
        personalState.setText(SaporiItalianoApplication.user.state);
        personalZip.setText(SaporiItalianoApplication.user.zip);
        personalCountry.setText(SaporiItalianoApplication.user.country);

       // peronalEmail.setText(SaporiItalianoApplication.user.email);
//        personalAddress.setText(SaporiItalianoApplication.user.address);
        personalPhone.setText(SaporiItalianoApplication.user.phone);

        UpdateUser=findViewById(R.id.UpdateUser);
        UpdateUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                HashMap<String, Object> result = new HashMap<>();
                String newName = personalname.getText().toString();
                String newEMail = SaporiItalianoApplication.user.email;
                String newStreet=personalStreet.getText().toString();
                String newAPPT=personalApartment.getText().toString();
                String newCity=personalCity.getText().toString();
                String newSate=personalState.getText().toString();
                String newZip=personalZip.getText().toString();
                String newCountry=personalCountry.getText().toString();
               // String newPhone=PhoneNumberUtils.normalizeNumber(personalPhone.getText().toString());
                //   user.address = personalAddress.getText().toString();
                    String newPhone = PhoneNumberUtils.normalizeNumber(personalPhone.getText().toString());
                //user.password = SaporiItalianoApplication.user.password;
                //user.id=SaporiItalianoApplication.user.id;

                if(TextUtils.isEmpty(newName)||TextUtils.isEmpty(newEMail)||TextUtils.isEmpty(newStreet)||TextUtils.isEmpty(newAPPT)||TextUtils.isEmpty(newCity)||TextUtils.isEmpty(newSate)||TextUtils.isEmpty(newZip)||TextUtils.isEmpty(newCountry)){
                    Toast.makeText(EditPersonalDetails.this, "Please Fill All Details", Toast.LENGTH_SHORT).show();
                    return;
                }





                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                result.put("name",newName);
                result.put("addressOne",newStreet);
                result.put("addressTwo",newAPPT);
                result.put("city",newCity);
                result.put("state",newSate);
                result.put("zip",newZip);
                result.put("country",newCountry);
                result.put("phone",newPhone);


                final User user = new User();
                user.name = personalname.getText().toString();
                user.email = SaporiItalianoApplication.user.email;
                user.addressOne=personalStreet.getText().toString();
                user.addressTwo=personalApartment.getText().toString();
                user.city=personalCity.getText().toString();
                user.state=personalState.getText().toString();
                user.zip=personalZip.getText().toString();
                user.country=personalCountry.getText().toString();
             //   user.address = personalAddress.getText().toString();
                user.phone = PhoneNumberUtils.normalizeNumber(personalPhone.getText().toString());
                user.password = SaporiItalianoApplication.user.password;
                user.id=SaporiItalianoApplication.user.id;


                databaseReference.child("Users").child(SaporiItalianoApplication.user.id).updateChildren(result)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(EditPersonalDetails.this, "User Updated", Toast.LENGTH_SHORT).show();
                                SaporiItalianoApplication.user = user;
                                Intent intent = new Intent(EditPersonalDetails.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(EditPersonalDetails.this, "Failed to create new account", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }
}